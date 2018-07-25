package rose;

import com.benzolamps.dict.component.IShuffleStrategy;
import com.benzolamps.dict.util.*;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Supplier;

/**
 * 乱序生成策略的实现
 * @author Benzolamps
 * @version 1.1.2
 * @datetime 2018-6-18 15:47:47
 */
public class RoseShuffleStrategy implements IShuffleStrategy {

    /* 列表的容量 */
    private int size;

    /* 装配器 */
    private RoseShuffleStrategySetup setup;

    /* 生成策略供应 */
    private Iterator<Supplier<Iterator<Integer>>> supplier;

    /* 当前生成的序列 */
    private Iterator<Integer> currentGroup;

    /* 随机数 */
    private Random random;

    /* 当前生成的数字 */
    private int current = -1;

    /**
     * 构造器
     * @param size 表示要乱序的序列的总容量
     * @param hash 哈希值
     * @param setup 装配器
     */
    @SuppressWarnings("deprecation")
    public RoseShuffleStrategy(int size, int hash, RoseShuffleStrategySetup setup) {
        Assert.notNull(setup);
        Assert.state(size > 0);
        this.setup = setup;
        this.size = size;
        supplier = createSuppliers().iterator();
        random = setup.isShuffleSeedSpecified() ? new DictRandom(setup.getShuffleSeed(), hash) : new Random();
    }

    /* 创建生成策略生成器 */
    private List<Supplier<Iterator<Integer>>> createSuppliers() {
        List<Supplier<Iterator<Integer>>> suppliers = new ArrayList<>();
        if (setup.getUnitSize() < 1) {
            suppliers.add(() -> createIterator(0, size, setup.getUnitTipRepeat(), true));
            suppliers.add(() -> createIterator(0, size, setup.getUnitNontipRepeat(), false));
        } else {
            for (int i = 0; i < size; i += setup.getUnitSize()) {
                int begin = i;
                int end = begin + setup.getUnitSize();
                suppliers.add(() -> createIterator(begin, end, setup.getUnitTipRepeat(), true));
                suppliers.add(() -> createIterator(begin, end, setup.getUnitNontipRepeat(), false));
            }
        }
        suppliers.add(() -> createIterator(0, size, setup.getEntireTipRepeat(), true));
        suppliers.add(() -> createIterator(0, size, setup.getEntireNontipRepeat(), false));
        return suppliers;
    }

    /* 根据序列和重复次数生成一个迭代器 */
    private Iterator<Integer> createIterator(int start, int end, int repeat, boolean index) {
        start = DictMath.limit(start, 0, size - 1);
        end = DictMath.limit(end, start + 1, size);
        List<Integer> map = DictList.range(index ? start : start + size, index ? end : end + size);
        if (setup.isShuffled()) {
            return new Shuffler<>(map, random, repeat);
        } else {
            return new Repeater<>(map, repeat);
        }
    }

    @Override
    public boolean hasNext() {
        while (null == currentGroup || !currentGroup.hasNext()) {
            if (!supplier.hasNext()) {
                return false;
            }
            currentGroup = supplier.next().get();
        }
        return true;
    }

    @Override
    public Integer next() {
        if (hasNext()) {
            return (current = currentGroup.next()) % size;
        } else {
            current = -1;
            throw new NoSuchElementException();
        }
    }

    @Override
    public boolean visible() {
        if (current < 0) {
            throw new NoSuchElementException();
        }
        return current < size;
    }
}
