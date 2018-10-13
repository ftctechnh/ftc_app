package ftc.vision;

import org.opencv.core.Scalar;

public class BeaconColorResult {
    public BeaconColorResult(BeaconColor leftColor, BeaconColor rightColor) {
        this.leftColor = leftColor;
        this.rightColor = rightColor;
    }

    public enum BeaconColor {
        YELLOW (ftc.vision.ImageUtil.YELLOW),
        GREEN (ftc.vision.ImageUtil.GREEN),
        WHITE (ftc.vision.ImageUtil.WHITE),
        UNKNOWN (ftc.vision.ImageUtil.BLACK);
        public final Scalar color;
        BeaconColor(Scalar color) {
            this.color = color;
        }
    }
    private final BeaconColor leftColor, rightColor;

    public BeaconColor getLeftColor() {
        return leftColor;
    }

    public BeaconColor getRightColor() {
        return rightColor;
    }

    @Override
    public String toString(){
        return leftColor + ", " + rightColor;
    }
}