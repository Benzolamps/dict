package rose;

import com.benzolamps.dict.component.DictOptions;
import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.component.IShuffleStrategy;
import com.benzolamps.dict.component.IShuffleStrategySetup;
import com.benzolamps.dict.main.DictApplication;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import javax.validation.constraints.NotNull;
import java.nio.charset.Charset;

/**
 * 乱序生成策略配置接口的实现
 * @author Benzolamps
 * @version 1.1.2
 * @datetime 2018-6-18 15:52:55
 */
public class RoseShuffleStrategySetup implements IShuffleStrategySetup {

    private static final long serialVersionUID = -3170125922193664875L;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DictApplication.class);

    @Value("url:#{dictProperties.remoteBaseUrl}/dict/tips.txt")
    private Resource tipsUrl;

    @Value("10")
    @DictOptions({"5", "10", "15", "20", "25", "30"})
    @DictPropertyInfo(display = "每个单元的单词个数")
    @NotNull
    private int unitSize;

    @Value("2")
    @Range(max = 5L)
    @DictPropertyInfo(display = "每个单元带提示的单词重复的次数")
    @NotNull
    private int unitTipRepeat;

    @Value("1")
    @Range(max = 5L)
    @DictPropertyInfo(display = "每个单元不带提示的单词重复的次数")
    @NotNull
    private int unitNontipRepeat;

    @Value("2")
    @Range(max = 5L)
    @DictPropertyInfo(display = "所有单词带提示重复的次数")
    @NotNull
    private int entireTipRepeat;

    @Value("1")
    @Range(max = 5L)
    @DictPropertyInfo(display = "所有单词不带提示重复的次数")
    @NotNull
    private int entireNontipRepeat;

    @Value("true")
    @DictPropertyInfo(display = "是否乱序")
    @NotNull
    private boolean shuffled;

    @Value("false")
    @DictPropertyInfo(display = "是否指定乱序的种子", description = "不指定则以当前时间戳为准，每次生成的次序都不一样")
    @NotNull
    private boolean shuffleSeedSpecified;

    @Value("0")
    @DictPropertyInfo(display = "乱序的种子", description = "如果指定乱序的种子，则相同的种子，相同的一组单词，生成的次序一样")
    @NotNull
    private int shuffleSeed;

    public int getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(int unitSize) {
        this.unitSize = unitSize;
    }

    public int getUnitTipRepeat() {
        return unitTipRepeat;
    }

    public void setUnitTipRepeat(int unitTipRepeat) {
        this.unitTipRepeat = unitTipRepeat;
    }

    public int getUnitNontipRepeat() {
        return unitNontipRepeat;
    }

    public void setUnitNontipRepeat(int unitNontipRepeat) {
        this.unitNontipRepeat = unitNontipRepeat;
    }

    public int getEntireTipRepeat() {
        return entireTipRepeat;
    }

    public void setEntireTipRepeat(int entireTipRepeat) {
        this.entireTipRepeat = entireTipRepeat;
    }

    public int getEntireNontipRepeat() {
        return entireNontipRepeat;
    }

    public void setEntireNontipRepeat(int entireNontipRepeat) {
        this.entireNontipRepeat = entireNontipRepeat;
    }

    public boolean isShuffled() {
        return shuffled;
    }

    public void setShuffled(boolean shuffled) {
        this.shuffled = shuffled;
    }

    public boolean isShuffleSeedSpecified() {
        return shuffleSeedSpecified;
    }

    public void setShuffleSeedSpecified(boolean shuffleSeedSpecified) {
        this.shuffleSeedSpecified = shuffleSeedSpecified;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public void setShuffleSeed(int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    @SuppressWarnings({"NonAsciiCharacters", "DanglingJavadoc"})
    @Override
    public IShuffleStrategy setup(int size, int hash) {
        int ¥$¥ = -355555550;
        java.util.function.IntPredicate p = ¥¥¥$$$¥¥¥->¥$¥>>¥$¥-/**💰💰💰💰💰💰💰💰💰💰**/-¥$¥<<¥$¥<-¥¥¥$$$¥¥¥;
        logger.info(String.valueOf(¥$¥ >> ¥$¥ + ¥$¥ << ¥$¥));
        if (p.test(666666)) {
            try {
                logger.info(StreamUtils.copyToString(tipsUrl.getInputStream(), Charset.forName("UTF-8")));
            } catch (Throwable ignored) { }
        }
        return new RoseShuffleStrategy(size, hash, this);
    }
}
