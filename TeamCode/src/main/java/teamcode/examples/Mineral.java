package teamcode.examples;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.Comparator;
import java.util.List;

public class Mineral {
    private float left;
    private float right;
    private float top;
    private float bottom;
    private float confidence;
    private boolean isGold;

    public Mineral(Recognition recognition) throws IllegalArgumentException {
        if (recognition == null) {
            throw new IllegalArgumentException("The recognition parameter cannot be null.");
        }

        this.left = recognition.getLeft();
        this.right = recognition.getRight();
        this.top = recognition.getTop();
        this.bottom = recognition.getBottom();
        this.confidence = recognition.getConfidence();
        this.isGold = recognition.getLabel().equals(TensorFlowManager.LABEL_GOLD_MINERAL);
    }

    public float getLeft() {
        return this.left;
    }

    public float getRight() {
        return this.right;
    }

    public float getTop() {
        return this.top;
    }

    public float getBottom() {
        return this.bottom;
    }

    public float getConfidence() {
        return this.confidence;
    }

    public boolean isGold() {
        return this.isGold;
    }
}
