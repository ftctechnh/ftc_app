package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractOpMode;
import org.firstinspires.ftc.teamcode.framework.userHardware.DoubleTelemetry;
import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.vuforia.VuforiaImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_GOLD_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_SILVER_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.TFOD_MODEL_ASSET;

public class TensorFlow {

    DoubleTelemetry telemetry = AbstractOpMode.getTelemetry();

    private VuforiaImpl vuforia;

    private TFObjectDetector tfod;

    private CameraOrientation orientation;

    public TensorFlow() {
        this(CameraOrientation.VERTICAL);
    }

    public TensorFlow(CameraOrientation cameraOrientation) {
        this(cameraOrientation, true);
    }

    public TensorFlow(CameraOrientation cameraOrientation, String camera) {
        this(cameraOrientation, camera, true);
    }

    public TensorFlow(CameraOrientation cameraOrientation, boolean led) {
        this(cameraOrientation, "BACK", led);
    }

    public TensorFlow(CameraOrientation cameraOrientation, String camera, boolean led) {
        orientation = cameraOrientation;

        do {
            vuforia = new VuforiaImpl(camera, false);
        } while (vuforia.getVuforia().getCameraCalibration() == null);

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfodWithViewer();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
            telemetry.update();
        }
    }

    public TensorFlow(CameraOrientation cameraOrientation, String camera, boolean led, boolean viewer) {
        orientation = cameraOrientation;

        do {
            vuforia = new VuforiaImpl(camera, false);
        } while (vuforia.getVuforia().getCameraCalibration() == null);

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfodWithoutViewer();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
            telemetry.update();
        }
    }

    private void initTfodWithViewer() {
        int tfodMonitorViewId = AbstractOpMode.getOpModeInstance().hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", AbstractOpMode.getOpModeInstance().hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.70;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia.getVuforia());
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    private void initTfodWithoutViewer() {
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters();
        tfodParameters.minimumConfidence = 0.70;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia.getVuforia());
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void start() {
        tfod.activate();
    }

    public void restart(){
        tfod.deactivate();
        tfod.activate();
    }

    public void pause() {
        tfod.deactivate();
    }

    public void stop() {
        tfod.shutdown();
    }

    public void setLED(boolean on) {
        vuforia.setLED(on);
    }

    public SamplePosition getSamplePosition() {
        SamplePosition position = SamplePosition.UNKNOWN;

        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            //List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            List<Recognition> updatedRecognitions = tfod.getRecognitions();
            if (updatedRecognitions != null) {
                ArrayList<Mineral> minerals = new ArrayList<>();
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getConfidence() < 0.5) continue;
                    minerals.add(new Mineral(recognition));
                }

                if (minerals.size() >= 3) position = samplePositionFromThreeMinerals(minerals);
                else if (minerals.size() > 0)
                    position = samplePositionFromOneMineral(minerals, updatedRecognitions.get(0).getImageWidth(), updatedRecognitions.get(0).getImageHeight());
            }
        }

        return position;
    }

    private SamplePosition samplePositionFromThreeMinerals(ArrayList<Mineral> minerals) {
        SamplePosition position = SamplePosition.UNKNOWN;

        Mineral[] samples = getBottomTwoSilverOneGold(minerals);

        if (samples != null) {
            Mineral goldMineral = null, silverMineralOne = null, silverMineralTwo = null;
            for (Mineral mineral : samples) {
                if (mineral.getType() == MineralType.GOLD) goldMineral = mineral;
                else if (silverMineralOne == null) silverMineralOne = mineral;
                else silverMineralTwo = mineral;
            }

            if (goldMineral == null || silverMineralOne == null || silverMineralTwo == null)
                return position;

            if (goldMineral.getX() != -1 && silverMineralOne.getX() != -1 && silverMineralTwo.getX() != -1) {
                if (goldMineral.getX() < silverMineralOne.getX() && goldMineral.getX() < silverMineralTwo.getX()) {
                    position = SamplePosition.LEFT;
                } else if (goldMineral.getX() > silverMineralOne.getX() && goldMineral.getX() > silverMineralTwo.getX()) {
                    position = SamplePosition.RIGHT;
                } else {
                    position = SamplePosition.CENTER;
                }
            }
        }

        return position;
    }

    private SamplePosition samplePositionFromOneMineral(ArrayList<Mineral> minerals, int width, int height) {
        SamplePosition position = SamplePosition.UNKNOWN;

        Mineral mineral = getBottomGoldMineral(minerals);

        if (mineral == null) return position;

        if (orientation == CameraOrientation.VERTICAL) {
            if (mineral.getX() < width / 3) position = SamplePosition.LEFT;
            else if (mineral.getX() < 2 * (width / 3)) position = SamplePosition.CENTER;
            else position = SamplePosition.RIGHT;
        } else {
            if (mineral.getX() < height / 3) position = SamplePosition.LEFT;
            else if (mineral.getX() < 2 * (height / 3)) position = SamplePosition.CENTER;
            else position = SamplePosition.RIGHT;
        }

        return position;
    }

    private Mineral[] getBottomThreeMinerals(ArrayList<Mineral> minerals) {
        if (minerals.size() < 3) return null;

        Mineral[] result = minerals.toArray(new Mineral[minerals.size()]);

        Arrays.sort(result, new SortByMineralY());

        Mineral[] last = {result[0], result[1], result[2]};

        return last;
    }

    private Mineral[] getBottomTwoSilverOneGold(ArrayList<Mineral> minerals) {

        ArrayList<Mineral> silverMinerals = new ArrayList<>();
        for (Mineral mineral : minerals) {
            if (mineral.getType() == MineralType.SILVER) silverMinerals.add(mineral);
        }

        if (silverMinerals.size() < 2) return null;

        Mineral goldMineral = getBottomGoldMineral(minerals);
        if (goldMineral == null) return null;

        Mineral[] silverResult = silverMinerals.toArray(new Mineral[silverMinerals.size()]);

        Arrays.sort(silverResult, new SortByMineralY());

        Mineral[] last = {goldMineral, silverResult[0], silverResult[1]};

        return last;
    }

    private Mineral getBottomGoldMineral(ArrayList<Mineral> minerals) {

        ArrayList<Mineral> golds = new ArrayList<>();

        for (Mineral mineral : minerals) {
            if (mineral.getType() == MineralType.GOLD) golds.add(mineral);
        }

        if (golds.size() < 1) return null;

        Mineral[] result = golds.toArray(new Mineral[golds.size()]);

        Arrays.sort(result, new SortByMineralY());

        return result[0];
    }

    private class SortByMineralY implements Comparator<Mineral> {
        @Override
        public int compare(Mineral lhs, Mineral rhs) {
            return rhs.getY() - lhs.getY();
        }
    }

    private class Mineral {

        private int x, y;

        double confidence;

        private MineralType mineralType;

        public Mineral(Recognition recognition) {
            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) mineralType = MineralType.GOLD;
            else mineralType = MineralType.SILVER;
            if (orientation == CameraOrientation.VERTICAL) {
                this.x = (int) ((recognition.getLeft() + recognition.getRight()) / 2);
                this.y = (int) ((recognition.getTop() + recognition.getBottom()) / 2);
            } else {
                this.x = (int) ((recognition.getTop() + recognition.getBottom()) / 2);
                this.y = recognition.getImageWidth() - (int) ((recognition.getLeft() + recognition.getRight()) / 2);
            }
            this.confidence = recognition.getConfidence();
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public double getConfidence() {
            return confidence;
        }

        public MineralType getType() {
            return mineralType;
        }
    }

    private enum MineralType {
        GOLD,
        SILVER
    }

    public enum CameraOrientation {
        VERTICAL,
        HORIZONTAL
    }
}