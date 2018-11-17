package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.Utils;
import org.firstinspires.ftc.teamcode.robotutil.Vision;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "AutonomousGold", group="FinalShit")

public class GoldAuto extends LinearOpMode {

    private DriveTrainNew dt;
    private Vision vision;

    private Telemetry.Item goldXTelem;
    private Telemetry.Item telemDirection;

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
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
//                options();
                waitForButton(String.format("Next: "));
                    goldAlign(.3,10);
            }
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


    private void updateOptions() {
        opMethod = telemetry.addData("Method", method);
        opDirection = telemetry.addData("Direction", direction);
        opPower = telemetry.addData("Power", power);
        opValue = telemetry.addData("Value", value);
    }

    private void options() {
        boolean confirmed = false;

        while(!confirmed) {

            if (gamepad1.a) {
                if (gamepad1.dpad_up) {
                    if (method < 1) {
                        method++;
                    }
                } else if (gamepad1.dpad_down) {
                    if (method > 0) {
                        method--;
                    }
                }
            } else if (gamepad1.b) {
                if (gamepad1.dpad_up) {
                    if (direction < 5) {
                        direction++;
                    }
                } else if (gamepad1.dpad_down) {
                    if (direction > 0) {
                        direction--;
                    }
                }
            } else if (gamepad1.x) {
                if (gamepad1.dpad_up) {
                    if (power < 1.0) {
                        power += 0.05;
                    }
                } else if (gamepad1.dpad_down) {
                    if (power > 0) {
                        power -= 0.05;
                    }
                }
            } else if (gamepad1.y) {
                if (gamepad1.dpad_up) {
                    if (value < 180) {
                        value++;
                    }
                } else if (gamepad1.dpad_down) {
                    if (value > 0) {
                        value--;
                    }
                }
            } else if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
                telemetry.addLine("Confirmed!");
                confirmed = true;
            }

            updateOptions();
            telemetry.update();

            Utils.waitFor(100);
        }
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

    private void goldAlign(double power, double timeoutS) {
        double minError = 10;

        if (vision.detect() == -1) {
            goldXTelem.setValue(-1);
            telemetry.update();
        } else {
            Direction direction;
            int goldX;
            int error;

            double startTime = System.currentTimeMillis();
            do {
                goldX = vision.detect();
                error = GOLD_ALIGN_LOC - goldX;

                if (error > 0) {
                    direction = Direction.CW;
                } else {
                    direction = Direction.CCW;
                }

                dt.move(direction, power);
                goldXTelem.setValue(goldX);
                telemetry.update();
            } while (goldX != -1 &&
                    Math.abs(error) > minError &&
                    (System.currentTimeMillis() - startTime) / 1000 < timeoutS);
        }
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

        updateOptions();
        telemetry.update();
    }

    private void haltUntilPressStart() {
        while (!gamepad1.start  && !isStopRequested()) {
            Utils.waitFor(300);
        }
    }

}
