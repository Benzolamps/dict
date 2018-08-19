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
@Component
@Getter
@Setter
public class RoseShuffleStrategySetup implements IShuffleStrategySetup {

    private static final long serialVersionUID = -3170125922193664875L;

    @Value("15")
    @DictOptions({"10", "15", "20", "25", "30"})
    @DictPropertyInfo(display = "unit size", description = "每个单元的单词个数")
    @NotEmpty
    private int unitSize;

    @Value("2")
    @Range(max = 5L)
    @DictPropertyInfo(display = "unit tip repeat", description = "每个单元带提示的单词重复的次数")
    @NotEmpty
    private int unitTipRepeat;

    @Value("1")
    @Min(0L)
    @Max(5L)
    @DictPropertyInfo(display = "unit nontip repeat", description = "每个单元不带提示的单词重复的次数")
    @NotEmpty
    private int unitNontipRepeat;

    @Value("2")
    @DictOptions({"0", "1", "2", "3", "4", "5"})
    @DictPropertyInfo(display = "entire tip repeat", description = "所有单词带提示重复的次数")
    @NotEmpty
    private int entireTipRepeat;

    @Value("1")
    @DictOptions({"0", "1", "2", "3", "4", "5"})
    @DictPropertyInfo(display = "entire nontip repeat", description = "所有单词不带提示重复的次数")
    @NotEmpty
    private int entireNontipRepeat;

    @Value("true")
    @DictPropertyInfo(display = "shuffled", description = "是否乱序")
    @NotEmpty
    private boolean shuffled;

    @Value("false")
    @DictPropertyInfo(display = "shuffled seed specified", description = "是否指定乱序的种子, 不指定则以当前时间戳为准, 每次生成的次序都不一样")
    @NotEmpty
    private boolean shuffleSeedSpecified;

    @Value("0")
    @DictPropertyInfo(display = "shuffle seed", description = "乱序的种子, 如果将shuffle_seed_specified设置为true, 则相同的种子, 相同的一组单词, 生成的次序一样")
    @NotEmpty
    private int shuffleSeed;

    @Override
    public IShuffleStrategy setup(int size, int hash) {
        return new RoseShuffleStrategy(size, hash, this);
    }
}
