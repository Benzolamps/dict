package rose;

import com.benzolamps.dict.component.IShuffleStrategy;
import com.benzolamps.dict.component.IShuffleStrategySetup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 乱序生成策略配置接口的实现
 * @author Benzolamps
 * @version 1.1.2
 * @datetime 2018-6-18 15:52:55
 */
@Component
public class RoseShuffleStrategySetup implements IShuffleStrategySetup {

    @Value("15")
    private int unitSize;

    @Value("2")
    private int unitTipRepeat;

    @Value("1")
    private int unitNontipRepeat;

    @Value("2")
    private int entireTipRepeat;

    @Value("1")
    private int entireNontipRepeat;

    @Value("true")
    private boolean shuffled;

    @Value("false")
    private boolean shuffleSeedSpecified;

    @Value("0")
    private int shuffleSeed;

    public void setUnitSize(int unitSize) {
        this.unitSize = unitSize;
    }

    public void setUnitTipRepeat(int unitTipRepeat) {
        this.unitTipRepeat = unitTipRepeat;
    }

    public void setUnitNontipRepeat(int unitNontipRepeat) {
        this.unitNontipRepeat = unitNontipRepeat;
    }

    public void setEntireTipRepeat(int entireTipRepeat) {
        this.entireTipRepeat = entireTipRepeat;
    }

    public void setEntireNontipRepeat(int entireNonTipRepeat) {
        this.entireNontipRepeat = entireNonTipRepeat;
    }

    public void setShuffled(boolean shuffled) {
        this.shuffled = shuffled;
    }

    public void setShuffleSeedSpecified(boolean shuffleSeedSpecified) {
        this.shuffleSeedSpecified = shuffleSeedSpecified;
    }

    public void setShuffleSeed(int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public int getUnitTipRepeat() {
        return unitTipRepeat;
    }

    public int getUnitNontipRepeat() {
        return unitNontipRepeat;
    }

    public int getEntireTipRepeat() {
        return entireTipRepeat;
    }

    public int getEntireNontipRepeat() {
        return entireNontipRepeat;
    }

    public boolean isShuffled() {
        return shuffled;
    }

    public boolean isShuffleSeedSpecified() {
        return shuffleSeedSpecified;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    @Override
    public IShuffleStrategy setup(int size, int hash) {
        return new RoseShuffleStrategy(size, hash, this);
    }
}
