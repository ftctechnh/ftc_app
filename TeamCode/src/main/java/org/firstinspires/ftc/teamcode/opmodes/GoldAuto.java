package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Util;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.Options;
import org.firstinspires.ftc.teamcode.robotutil.Utils;
import org.firstinspires.ftc.teamcode.robotutil.Vision;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "AutonomousGold", group="FinalShit")

public class GoldAuto extends LinearOpMode {

    private DriveTrainNew dt;
    private Vision vision;
    private static double MIN_TURN_POWER = 0.07;
    private Telemetry.Item goldXTelem;
    private Telemetry.Item telemDirection;

    private Options options;

    // Tune these
    private final double DIST_TO_GOLD = 12;
    private final double DIST_TO_DEPOT = 36;
    private final double DIST_TO_CRATER = 36;
    private final int GOLD_ALIGN_LOC = 550;

    // Options
    // Method: 0 = moveP, 1 = rotateIMU
    // Direction: 0 = FW, 1 = BW, 2 = R, 3 = L, 4 = CW, 5 = CCW
    private int method;
    private int direction;
    private double power;
    private double value;

    private Telemetry.Item opMethod;
    private Telemetry.Item opDirection;
    private Telemetry.Item opPower;
    private Telemetry.Item opValue;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        initOptions();
        options.setOptions();
        vision  = new Vision(this);
        vision.startVision();
        waitForStart();

        if (opModeIsActive()) {

            double kp = options.getOption("kp").getValue();
            MIN_TURN_POWER = options.getOption("minPower").getValue();
            goldAlign(.3,100000, 1.0/kp);
        }

            // Land

            // Reset to 0 degrees
//            dt.rotateToHeading(180 , 0.5, 5);

            // Align with gold mineral
//            goldAlign(0.5, 5);
//
//            // Encoder drive forward
//            dt.move(Direction.FORWARD, 0.5, DIST_TO_GOLD, 10);
//
//            // Encoder drive back
//            dt.move(Direction.FORWARD, 0.5, DIST_TO_GOLD, 10);
//
//            // Reset to 0 degrees
//            dt.rotateToHeading(0, 0.5, 5);
//
//            // Drive to depot
//            dt.move(Direction.FORWARD, 0.5, DIST_TO_DEPOT, 10);
//
//            // Drop off marker
//            /* IMPLEMENT MARKER DROPPING */
//
//            // Rotate toward crater
//            dt.rotateIMU(Direction.CW, 135, 0.5, 5);
//
//            // Drive to crater
//            dt.move(Direction.FORWARD, 0.5, DIST_TO_CRATER, 10);
    }

//        vision.shutDown();



    private void initOptions() {
        options = new Options(this);
        options.addQuantitativeOption("kp", 150, 360, 10);
        options.addQuantitativeOption("minPower", 0.1, 0.3, 0.02);
    }

    private void waitForButton(String message){
        telemetry.addLine(message);
        telemetry.update();
        while(!this.gamepad1.a && opModeIsActive() && !isStopRequested()){
            sleep(100);
        }
        telemetry.addLine("proceeding");
        telemetry.update();
    }

    private void goldAlign(double power, double timeoutS, double kp) {
        double minError = 10;
        goldXTelem = this.telemetry.addData("gold x","N/A");
        Telemetry.Item t = this.telemetry.addData("status","initializing");
        Telemetry.Item telDir = this.telemetry.addData("direction","idk");
        Telemetry.Item telError = this.telemetry.addData("error","idk");
        Telemetry.Item telPower = this.telemetry.addData("power","idk");
        int j = vision.detect();
        while (j == -1) {
//            dt.move(Direction.CCW, MIN_TURN_POWER);
//            sleep(200);
//            dt.stopAll();
            dt.rotateIMUPID(Direction.CCW, 20, 5);

            goldXTelem.setValue(j);
            t.setValue("cannot detect :/");
            telemetry.update();
            sleep(100);
            j = vision.detect();
        }
        dt.stopAll();
        t.setValue("found!!!!!");
        goldXTelem.setValue(j);
        telemetry.update();
        Direction direction;
        int goldX;
        int error;
        double propPower;
        double startTime = System.currentTimeMillis();
        do {
            goldX = vision.detect();
            error = GOLD_ALIGN_LOC - goldX;

            if (goldX != -1) {

//                if (error < 0) {
//                    direction = Direction.CW;
//                    telDir.setValue("CW");
//                } else {
//                    direction = Direction.CCW;
//                    telDir.setValue("CCW");
//                }
                telError.setValue(error);
                goldXTelem.setValue(goldX);
                propPower = kp * power * error;
                if (propPower < 0){
                    propPower -= MIN_TURN_POWER;
                }else{
                    propPower += MIN_TURN_POWER;
                }
                telPower.setValue(propPower);
                dt.move(Direction.CCW, propPower);
                telemetry.update();
                sleep(200);
                dt.stopAll();
                sleep(200);

//                sleep(50);
                if (Math.abs(error) < minError) {
                    dt.stopAll();
                    sleep(300);
                    goldX = vision.detect();
                    error = GOLD_ALIGN_LOC - goldX;
                }
            }
//            } else {
//                goldXTelem.setValue(goldX);
//                telemetry.update();
//                dt.stopAll();
//                sleep(500
//                );
//            }

        } while (Math.abs(error) > minError &&
                (System.currentTimeMillis() - startTime) / 1000 < timeoutS && opModeIsActive() && !isStopRequested());

        dt.stopAll();
        Utils.waitFor(5000);
    }

    private void initialize() {
        dt = new DriveTrainNew(this);
        vision = new Vision(this);

//        goldXTelem = telemetry.addData("Gold mineral position", -1);
//        telemDirection = telemetry.addData("Direction", "NONE");

        method = 0;
        direction = 0;
        power = 0.5;
        value = 18;

        telemetry.update();
    }

    private void haltUntilPressStart() {
        while (!gamepad1.start  && !isStopRequested()) {
            Utils.waitFor(300);
        }
    }

}
