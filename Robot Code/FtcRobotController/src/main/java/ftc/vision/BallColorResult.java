package ftc.vision;

import org.opencv.core.Scalar;

import static ftc.vision.BallColorResult.BallColor.UNKNOWN;

/**
 * Created by guberti on 9/29/2017.
 */

public class BallColorResult {

    public enum BallColor {
        RED     (ImageUtil.RED),
        GREEN   (ImageUtil.GREEN),
        BLUE    (ImageUtil.BLUE),
        UNKNOWN (ImageUtil.BLACK);

        public final Scalar color;

        BallColor(Scalar scalar){
            this.color = scalar;
        }
    }
    private final BallColor leftColor, rightColor;

    public BallColorResult() {
        this.leftColor = UNKNOWN;
        this.rightColor = UNKNOWN;
    }

    public BallColorResult(BallColor leftColor, BallColor rightColor) {
        this.leftColor = leftColor;
        this.rightColor = rightColor;
    }

    public String toString(){
        return leftColor + ", " + rightColor;
    }

    public BallColor getLeftColor() {
        return leftColor;
    }

    public BallColor getRightColor() {
        return rightColor;
    }

}
