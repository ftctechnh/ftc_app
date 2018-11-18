package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Function;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.GoldPosition;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.robotutil.Options;
import org.firstinspires.ftc.teamcode.robotutil.Vision;
import android.util.*;
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "AutonomousGold", group="FinalShit")


public class GoldAuto extends LinearOpMode {

    private DriveTrain dt;
    private Vision vision;
    private HangSlides hangSlides;

    private Options options;

    // Tune these
    private final double DIST_TO_GOLD_LR = 40;
    private final double DIST_TO_CRATER_CENTER = 50;
    private final double DIST_TO_DEPOT = 24;
    private final double DIST_TO_CRATER = 36;
    private final int GOLD_ALIGN_LOC = 550;
    private final double MIN_GOLD_ROTATION_POWER = 0.1;
    private final int GOLD_ROTATE_SLEEP_TIME = 300;
    private Logger l = new Logger("AUTO GOLD");

    private static double MIN_TURN_POWER = 0.07;
    private double goldAlignKp = 1.0 / 180.0;

    @Override
    public void runOpMode() throws InterruptedException {
        l.log("starting opmode");


        initialize();
        options.setOptions();
        waitForButton("Press button to init slides");
        hangSlides.moveSlides(Direction.DOWN,.5,2.5,5);
        waitForStart();
        hangSlides.moveSlides(Direction.UP,.5,2.5,5);

        if (opModeIsActive()) {

            goldAlignKp = 1.0 / options.getOption("kp").getValue();
            MIN_TURN_POWER = options.getOption("minPower").getValue();


            dt.strafe(Direction.RIGHT,3,100);
            sleep(500);
            dt.drive(Direction.FORWARD,6,100);
            sleep(500);

            rotateToGold2();
            GoldPosition goldPos;
            goldPos = determinePosition();
            dt.drive(Direction.FORWARD,30,1000);

            switch (goldPos) {
                case LEFT:
                    dt.drive(Direction.FORWARD, DIST_TO_GOLD_LR, 5);
                    dt.rotateTo(45, 5);
                    dt.drive(Direction.FORWARD, DIST_TO_DEPOT, 5);
                    break;
                case CENTER:
                    dt.drive(Direction.FORWARD, DIST_TO_CRATER_CENTER, 5);
                    break;
                case RIGHT:
                    dt.drive(Direction.FORWARD, DIST_TO_GOLD_LR, 5);
                    dt.rotateTo(-45, 5);
                    dt.drive(Direction.FORWARD, DIST_TO_DEPOT, 5);
                    break;
            }

//                waitForButton("forward 30 inches");
//                dt.drive(Direction.FORWARD, 30, 10);
//
//                waitForButton("rotate absoloute");
//                dt.rotateTo(0);
//                waitForButton("drive forward");
//                dt.drive(Direction.FORWARD, 12, 10);
//                waitForButton("rotate absoloute");
//                dt.rotate(Direction.CW, 12, 10);
        }

    }

    private Boolean rotateToGold2(){
        dt.rotateTo(GoldPosition.RIGHT.getValue(),100);
        waitForButton("press button to start scanning");
        int detectedGoldPosition = -1;
        while(detectedGoldPosition == -1 || Math.abs(detectedGoldPosition - GOLD_ALIGN_LOC) < 300){
            detectedGoldPosition  = vision.detectRobust(10);
            dt.move(Direction.CCW,MIN_GOLD_ROTATION_POWER);
            sleep(GOLD_ROTATE_SLEEP_TIME);
            dt.stopAll();

            if(dt.getImu().getAngle() > GoldPosition.LEFT.getValue()){
                return false;
                // looked thru full spectrum and did not find

            }
        }
        return true;
        //gound gold and reutrn true


    }
    private void rotateToGold() {
        dt.rotateTo(GoldPosition.RIGHT.getValue(),10000);
        if(Math.abs(vision.detectRobust(10) - GOLD_ALIGN_LOC) < 300){
            return;
        }
        dt.rotateTo(GoldPosition.CENTER.getValue(),10000);
        if(Math.abs(vision.detectRobust(10) - GOLD_ALIGN_LOC) < 300){
            return;
        }
        dt.rotateTo(GoldPosition.LEFT.getValue(),10000);
        if(Math.abs(vision.detectRobust(10) - GOLD_ALIGN_LOC) < 300){
            return;
        }
    }


    private void waitForButton(String message){
        telemetry.addLine(message);
        telemetry.update();
        while(!this.gamepad1.a && opModeIsActive() && !isStopRequested()){
            sleep(100);
        }
    }

    private void initialize() {
        dt = new DriveTrain(this);
        vision = new Vision(this);
        vision.startVision();
        hangSlides = new HangSlides(this);
        initOptions();

    }

    private void initOptions() {
        options = new Options(this);
        options.addQuantitativeOption("kp", 150, 360, 10);
        options.addQuantitativeOption("minPower", 0.1, 0.3, 0.02);
    }

    private void goldAlign(double power, double timeoutS, double kp) {
        double minError = 50;
        Telemetry.Item t = this.telemetry.addData("status","initializing");
        Telemetry.Item telGoldX = this.telemetry.addData("gold x","N/A");
        Telemetry.Item telError = this.telemetry.addData("error","idk");
        Telemetry.Item telPower = this.telemetry.addData("power","idk");

//        int j = vision.detectRobust(10);
//        while (j == -1) {
//            dt.rotate(Direction.CCW,5);
//            telGoldX.setValue(j);
//            t.setValue("cannot detectRobust(10:/");
//            telemetry.update();
//            j = vision.detectRobust(10);
//        }
//        dt.stopAll();
//
//        t.setValue("found!!!!!");
//        telGoldX.setValue(j);
//        telemetry.update();

        int goldX;
        int error;
        double propPower;
        double startTime = System.currentTimeMillis();

        do {
            goldX = vision.detectRobust(10);
            error = GOLD_ALIGN_LOC - goldX;

            if (goldX != -1) {

                propPower = kp * power * error;
                if (propPower < 0) {
                    propPower -= MIN_TURN_POWER;
                } else {
                    propPower += MIN_TURN_POWER;
                }

                telError.setValue(error);
                telGoldX.setValue(goldX);
                telPower.setValue(propPower);
                telemetry.update();

                dt.move(Direction.CCW, propPower);
                sleep(100);
                dt.stopAll();
                sleep(100);

                if (Math.abs(error) < minError) {
                    dt.stopAll();
                    sleep(300);
                    goldX = vision.detectRobust(10);
                    error = GOLD_ALIGN_LOC - goldX;
                }
            }

        } while (Math.abs(error) > minError &&
                (System.currentTimeMillis() - startTime) / 1000 < timeoutS && opModeIsActive() && !isStopRequested());

        dt.stopAll();
    }

    public GoldPosition determinePosition(){
        double p = dt.getImu().getAngle();
        double leftDiff = Math.abs(p-GoldPosition.LEFT.getValue());
        double centerDiff = Math.abs(p-GoldPosition.CENTER.getValue());
        double rightDiff = Math.abs(p-GoldPosition.RIGHT.getValue());


       if(leftDiff < centerDiff && leftDiff < rightDiff){
           return GoldPosition.LEFT;
       }
       if(rightDiff < centerDiff && rightDiff < leftDiff){
            return GoldPosition.RIGHT;
        }
        if(centerDiff <= leftDiff && centerDiff <= rightDiff){
            return GoldPosition.CENTER;
        }

        else{
           return null;
        }
    }
}
