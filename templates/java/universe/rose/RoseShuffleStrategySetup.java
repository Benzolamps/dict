package rose;

import com.benzolamps.dict.component.DictOptions;
import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.component.IShuffleStrategy;
import com.benzolamps.dict.component.IShuffleStrategySetup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 乱序生成策略配置接口的实现
 * @author Benzolamps
 * @version 1.1.2
 * @datetime 2018-6-18 15:52:55
 */
@Getter
@Setter
public class RoseShuffleStrategySetup implements IShuffleStrategySetup {

    private static final long serialVersionUID = -3170125922193664875L;

    @Value("10")
    @DictOptions({"10", "15", "20", "25", "30"})
    @DictPropertyInfo(display = "每个单元的单词个数")
    @NotEmpty
    private int unitSize;

    @Value("2")
    @Range(max = 5L)
    @DictPropertyInfo(display = "每个单元带提示的单词重复的次数")
    @NotEmpty
    private int unitTipRepeat;

    @Value("1")
    @Range(max = 5L)
    @DictPropertyInfo(display = "每个单元不带提示的单词重复的次数")
    @NotEmpty
    private int unitNontipRepeat;

    @Value("2")
    @Range(max = 5L)
    @DictPropertyInfo(display = "所有单词带提示重复的次数")
    @NotEmpty
    private int entireTipRepeat;

    @Value("1")
    @Range(max = 5L)
    @DictPropertyInfo(display = "所有单词不带提示重复的次数")
    @NotEmpty
    private int entireNontipRepeat;

    @Value("true")
    @DictPropertyInfo(display = "是否乱序")
    @NotEmpty
    private boolean shuffled;

    @Value("false")
    @DictPropertyInfo(display = "是否指定乱序的种子", description = "不指定则以当前时间戳为准，每次生成的次序都不一样")
    @NotEmpty
    private boolean shuffleSeedSpecified;

    @Value("0")
    @DictPropertyInfo(display = "乱序的种子", description = "如果指定乱序的种子，则相同的种子，相同的一组单词，生成的次序一样")
    @NotEmpty
    private int shuffleSeed;

    @Override
    public IShuffleStrategy setup(int size, int hash) {
        return new RoseShuffleStrategy(size, hash, this);
    }
}
