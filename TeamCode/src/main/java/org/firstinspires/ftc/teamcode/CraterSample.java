package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


/** DRIVETRAIN CONFIGURATION
 *      front
 *      C   D
 * left       right
 *      B   A
 *      back
 */



@Autonomous(name = "CraterSample", group = "Auto")
public class CraterSample extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AZCMreL/////AAABmdWWWC3/vE83hRmhRnWGAul7owT5y1Mn4hOtumsnaMVr67TGWwxY7HgprjL5Ng7ryllGy3WuOPA2BLCwD3s1zxukV6N4Eo5sgzqntZSibOI2BPj2xm7fdQklrFDF+XqkxD9xpKRTDuZk39tRPXREvU0RIrC0gl1AdvmLNh4UN48GZRHtjgTz1cNTHq9HMO5q0bJ1FF4bQHyilcOeK6YoYSaLE+H9cpSarCjIP7H77Z7acyfzE0sjWxfGcONmQoREkhVC3VeOPh3HkRSMMnxxBj+uaDCLQkOA4Ejilr7YGceRz8HY9yy02vJs+oYCfCZOz97vIjF4rOZW171bH7CwMRhqNIWs0SS2oBydH1nVk/yR";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private int goldMineralX = -1;

    private DcMotor aDrive = null;
    private DcMotor bDrive = null;
    private DcMotor cDrive = null;
    private DcMotor dDrive = null;
    private DcMotor aMech = null;
    private DcMotor bMech = null;
    private DcMotor cMech = null;
    private DcMotor dMech = null;

    private double forward = 0;
    private double turn = 0;


    private String[] titles = new String[] {"armpower", "armtime", "armdist", "turn0power", "turn0time", "forward0power", "forward0time", "turn1power", "turn1time"} ;
    private double[] values = new double[] {    0.3,           2,      1000,        0.3,          3,             0.3,             5,            0.3,           7  };
    private Tuner tuner;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        /////////////////////////////////////////////////////////// INIT
        initVuforia();
        initTfod();

        aDrive = hardwareMap.get(DcMotor.class, "1-0");
        aDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        bDrive = hardwareMap.get(DcMotor.class, "1-1");
        bDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        cDrive = hardwareMap.get(DcMotor.class, "1-2");
        cDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        dDrive = hardwareMap.get(DcMotor.class, "1-3");
        dDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        aMech = hardwareMap.get(DcMotor.class, "2-0");
        bMech = hardwareMap.get(DcMotor.class, "2-1");
        cMech = hardwareMap.get(DcMotor.class, "2-2");
        dMech = hardwareMap.get(DcMotor.class, "2-3");

        tuner = new Tuner(titles, values, gamepad1, telemetry);

        telemetry.update();
        /////////////////////////////////////////////////////////// END INIT

        while (!opModeIsActive()){
            tuner.tune();
        }


        waitForStart();
        runtime.reset();

        while (opModeIsActive()){

            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    //if object detected
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                        }
                    }
                    telemetry.addData("goldMineralX",goldMineralX);
                    telemetry.update();
                    //end if object detected
                }
            }

            ///////////////////////////////////////////////////////////////// ACTIONS

            if (timedAction(0, tuner.get("armtime"))) { //arm
                if(cMech.getCurrentPosition() < tuner.get("armdist")){
                    cMech.setPower(tuner.get("armpower"));
                }
            }

            if (timedAction(tuner.get("armtime"), tuner.get("turn0time"))) { //turn to release hook
                forward = 0;
                turn = tuner.get("turn0power");
                cMech.setPower(0);
            }

            if (timedAction(tuner.get("turn0time"), tuner.get("forward0time"))) { //forward til near mineral
                forward = tuner.get("forward1time");
                turn = 0;
            }

            if (timedAction(tuner.get("forward0time"), tuner.get("turn0time"))) { //turn parallel to minerals
                forward = 0;
                turn = tuner.get("turn0power");
            }

            if (timedAction(6, 12) && goldMineralX == -1) { //forward til see gold
                forward = 0.1;
                turn = 0;
            }

            if (timedAction(12, 13)){ //turn to face gold
                forward = 0;
                turn = 0.1;
            }

            if (timedAction(13, 15)){ //forward to push gold using brush
                forward = 0.4;
                turn = 0;
                bMech.setPower(0.9);
            }

            if (timedAction(15, 20)){ //stop
                forward = 0;
                turn = 0;
                bMech.setPower(0);
            }

            ///////////////////////////////////////////////////////////////// END ACTIONS


            aDrive.setPower(forward + turn);
            bDrive.setPower(forward - turn);
            cDrive.setPower(forward - turn);
            dDrive.setPower(forward + turn);


            telemetry.addData("arm encoder", cMech.getCurrentPosition());
            telemetry.addData("forward",forward);
            telemetry.addData("turn",turn);
            telemetry.addData("runtime", runtime.seconds());
            telemetry.update();
        }




    }




    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    private Boolean timedAction(double start, double end){
        return (runtime.seconds() > start && runtime.seconds() < end);
    }


}
