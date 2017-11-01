package ftc.vision;

import org.opencv.core.Scalar;

import static ftc.vision.CryptoBoxResult.CryptoBoxColor.UNKNOWN;

/**
 * Created by guberti on 9/29/2017.
 */

public class CryptoBoxResult {

    public enum CryptoBoxColor {
        RED     (ImageUtil.RED),
        GREEN   (ImageUtil.GREEN),
        BLUE    (ImageUtil.BLUE),
        UNKNOWN (ImageUtil.BLACK);

        public final Scalar color;

        CryptoBoxColor(Scalar scalar){
            this.color = scalar;
        }
    }
    private final CryptoBoxColor leftColor, rightColor;

    public CryptoBoxResult() {
        this.leftColor = UNKNOWN;
        this.rightColor = UNKNOWN;
    }

    public CryptoBoxResult(CryptoBoxColor leftColor, CryptoBoxColor rightColor) {
        this.leftColor = leftColor;
        this.rightColor = rightColor;
    }

    public String toString(){
        return leftColor + ", " + rightColor;
    }

    public CryptoBoxColor getLeftColor() {
        return leftColor;
    }

    public CryptoBoxColor getRightColor() {
        return rightColor;
    }

}
