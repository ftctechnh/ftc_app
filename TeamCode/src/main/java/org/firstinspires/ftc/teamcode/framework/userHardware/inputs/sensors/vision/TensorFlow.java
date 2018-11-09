package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.framework.AbstractOpMode;
import org.firstinspires.ftc.teamcode.framework.userHardware.DoubleTelemetry;
import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.vuforia.VuforiaImpl;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_GOLD_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_SILVER_MINERAL;
import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.TFOD_MODEL_ASSET;

public class TensorFlow {

    DoubleTelemetry telemetry = AbstractOpMode.getTelemetry();

    private VuforiaImpl vuforia;

    private TFObjectDetector tfod;

    public TensorFlow(String camera) {
        vuforia = new VuforiaImpl(camera);

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
            telemetry.update();
        }
    }

    public TensorFlow() {
        this("BACK");
    }

    private void initTfod() {
        int tfodMonitorViewId = AbstractOpMode.getOpModeInstance().hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", AbstractOpMode.getOpModeInstance().hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia.getVuforia());
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void stop(){
        tfod.shutdown();
    }

    public SamplePosition getSamplePosition(){
        SamplePosition position = SamplePosition.UNKNOWN;
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 3) {
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                            position = SamplePosition.LEFT;
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                            position = SamplePosition.RIGHT;
                        } else {
                            telemetry.addData("Gold Mineral Position", "Center");
                            position = SamplePosition.CENTER;
                        }
                    }
                }
                telemetry.update();
            }
        }
        return position;
    }
}