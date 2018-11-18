package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.Dumper;
import org.firstinspires.ftc.teamcode.robotutil.GoldPosition;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.robotutil.Logger;
import org.firstinspires.ftc.teamcode.robotutil.Options;
import org.firstinspires.ftc.teamcode.robotutil.Sweeper;
import org.firstinspires.ftc.teamcode.robotutil.Vision;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "AutonomousGold", group="FinalShit")


public class GoldAuto extends LinearOpMode {

    private DriveTrain dt;
    private Vision vision;
    private HangSlides hangSlides;
    private Dumper dumper;
    private Sweeper sweeper;

    private Options options;

    // Tune these
    private final double DIST_TO_GOLD_LR = 35;
    private final double DIST_TO_CRATER_CENTER = 40;
    private final double DIST_TO_DEPOT = 20;
    private final double DIST_TO_CRATER = 36;
    private final int GOLD_ALIGN_LOC = 550;
    private final double MIN_GOLD_ROTATION_POWER = .4;
    private final int GOLD_ROTATE_SLEEP_TIME = 350;
    private Logger l = new Logger("AUTO GOLD");
    private static double MIN_TURN_POWER = 0.07;
    private double goldAlignKp = 1.0 / 180.0;

    @Override
    public void runOpMode() throws InterruptedException {
        l.log("starting opmode");

        initialize();
//        options.setOptions();
        telemetry.addLine("press button to raise robot");
        telemetry.update();
        while(!this.gamepad1.a && !isStopRequested()){
            sleep(100);
        }

        hangSlides.moveSlides(Direction.DOWN,.5,2.5,5);
        waitForStart();

        if (opModeIsActive()) {
            l.log("moving down");
            hangSlides.moveSlides(Direction.UP,.5,2.5,5);

//            goldAlignKp = 1.0 / options.getOption("kp").getValue();
//            MIN_TURN_POWER = options.getOption("minPower").getValue();
            l.log("strafe right");
            dt.strafe(Direction.RIGHT,3,100);
            sleep(500);
            l.log("moving forward");
            dt.drive(Direction.FORWARD,4,100);
            sleep(500);
//            waitForButton("rotate to gold");
            l.log("rotate to gold");
            l.lineBreak();
            rotateToGold2();
            l.log("finished rotate to gold");
            l.lineBreak();
//            waitForButton("proceed after rotate to gold");
            GoldPosition goldPos;
            goldPos = determinePosition();
            waitForButton("turn");
            dt.rotateTo(goldPos.getValue(), 5);
            waitForButton("turn done");

            l.log("Gold pos: " + goldPos.toString());

//            dt.drive(Direction.FORWARD,30,1000);
            sweeper.reverseIntake();

            switch (goldPos) {
                case LEFT:
                    dt.drive(Direction.FORWARD, DIST_TO_GOLD_LR, 5);
                    dt.rotateTo(45, 5);
                    dt.drive(Direction.FORWARD, DIST_TO_DEPOT + 18, 5);
                    dt.rotateTo(45, 5);

                    break;
                case CENTER:
                    dt.drive(Direction.FORWARD, DIST_TO_CRATER_CENTER, 5);
                    dt.rotateTo(45, 5);
                    dt.drive(Direction.FORWARD, 12, 5);

                    break;
                case RIGHT:
                    dt.drive(Direction.FORWARD, DIST_TO_GOLD_LR, 5);
                    dt.rotateTo(-45, 5);
                    dt.drive(Direction.FORWARD, DIST_TO_DEPOT, 5);
                    dt.rotateTo(45, 5);

                    break;
            }


            dumper.dump();
            sleep(2000);
            sweeper.stop();



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
        l.log("Rotating to gold...");
        l.lineBreak();
        l.log("frank start");
        waitForButton("turn  next");
        dt.rotateTo(GoldPosition.RIGHT.getValue(),100);
        waitForButton("turn done");
        l.log("frank end");
        l.lineBreak();

        int detectedGoldPosition = -1;
        while(detectedGoldPosition == -1 || Math.abs(detectedGoldPosition - GOLD_ALIGN_LOC) > 300){
            detectedGoldPosition  = vision.detectRobust(10);
            l.logData("vision detecting position: ",dt.getImu().getAngle());
            dt.move(Direction.CCW,MIN_GOLD_ROTATION_POWER);
            sleep(GOLD_ROTATE_SLEEP_TIME);
            dt.stopAll();
            sleep(GOLD_ROTATE_SLEEP_TIME);

            if(dt.getImu().getAngle() < GoldPosition.LEFT.getValue()){
                l.log("went past left position");

                return false;

                // looked thru full spectrum and did not find

            }
        }
        l.log("returned from rotategold");
        l.lineBreak();
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
        dumper = new Dumper(this);
        dumper.retract();

        sweeper = new Sweeper(this);
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
        l.lineBreak();
        l.logData("current gyro",p);
        double leftDiff = Math.abs(p-GoldPosition.LEFT.getValue());
        double centerDiff = Math.abs(p-GoldPosition.CENTER.getValue());
        double rightDiff = Math.abs(p-GoldPosition.RIGHT.getValue());
        l.logData("left diff",leftDiff);
        l.logData("right diff",rightDiff);
        l.logData("center diff",centerDiff);


       if(leftDiff < centerDiff && leftDiff < rightDiff){
           l.log("left detected");
           l.lineBreak();

           return GoldPosition.LEFT;
       }
       if(rightDiff < centerDiff && rightDiff < leftDiff){
           l.log("right detected");
           l.lineBreak();

           return GoldPosition.RIGHT;
        }
        if(centerDiff <= leftDiff && centerDiff <= rightDiff){
           l.log("center detected");
            l.lineBreak();

            return GoldPosition.CENTER;
        }
        else{
            l.lineBreak();
            return null;
        }

    }
}
