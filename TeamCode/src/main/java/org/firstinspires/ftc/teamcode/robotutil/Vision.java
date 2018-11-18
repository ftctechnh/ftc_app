
package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import static java.lang.Thread.sleep;

public class Vision {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY ="AQgn1d//////AAAAGS+F+GWwAEbtqn64lm+fvolRqft5tIJLGdUCsB51qVZHMP3UU8cTCBMKvjCBUTxHfkooO1dljaRLNzaDMMTbWw978Agd7qMrUQF/I4dsE+oVUhLVTHxHPl4r8T4LJ1+B5KHvXQyTr7S3bTU1xy/id/uACCppztVO6mH6Aj0FwY/v3lDYnL9sQNVi2DNXNrnQmmshyJC74C4Se8a6A/II7vcaQ00Ot3PlSB9LjH6K28EQ3oiLnc6tKTGjbU+uTBdoix2KUDL7xVa8c6biG2lcuu7j6dRrw/uvUrh7RpWcmvQDdoshtLlXLsvacLwr5NzMX+4quVkydj/3KRrixOKnepk0ZSPiSlt+J+ThynHcgevu";
    private Logger l = new Logger("VISION");
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    OpMode opMode;


    public Vision(OpMode opmode) {
        this.opMode = opmode;
    }
    public void startVision() {
        initVuforia();
        initTfod();
        tfod.activate();
    }
    public void shutDown(){
        tfod.shutdown();
    }

    public int detectRobust(int numPolls){
//        Telemetry.Item detected = opMode.telemetry.addData("Robust detection","INIT");
        int d;
        for(int i = 0;i<numPolls;i++){
            d = detect();
            l.logData("Poll #",i+1);
            l.logData("Val",d);
//            detected.setValue(String.valueOf(d) +"-  cycle: " +  String.valueOf(i));
            opMode.telemetry.update();
            if(d != -1){
                return d;
            }
            Utils.waitFor(10);
        }
        l.log("didnt find in max tries, returning -1");
        return -1;
    }

    public int detect() {
        l.log("detect() called");

        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
//            opMode.telemetry.addData("# Object Detected", updatedRecognitions.size());
            int goldMineralX = -1;
            for (Recognition recognition : updatedRecognitions) {
                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                    goldMineralX = (int) recognition.getLeft();
                }
            }
            l.logData("GoldX",goldMineralX);

            return goldMineralX;
        }else{
//            opMode.telemetry.addLine("No objects deteced :(");
            l.logData("GoldX",-1);
            return -1;
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        l.log("initialized vuforia");
        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
        l.log("initialized tfod");

    }
}
