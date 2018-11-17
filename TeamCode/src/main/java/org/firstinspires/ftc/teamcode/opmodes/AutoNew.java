package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Util;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.robotutil.Options;
import org.firstinspires.ftc.teamcode.robotutil.Utils;
import org.firstinspires.ftc.teamcode.robotutil.Vision;

import java.lang.reflect.Method;
import java.util.Locale;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group="FinalShit")

public class AutoNew extends LinearOpMode {

    private DriveTrainNew dt;
    private HangSlides hs;
    private Vision vision;

    private Options options;

    private Telemetry.Item goldXTelem;
    private Telemetry.Item telemDirection;

    // Tune these
    private final double DIST_TO_GOLD = 12;
    private final double DIST_TO_DEPOT = 36;
    private final double DIST_TO_CRATER = 36;
    private final int GOLD_ALIGN_LOC = 100;

    private Telemetry.Item opMethod;
    private Telemetry.Item opDirection;
    private Telemetry.Item opPower;
    private Telemetry.Item opValue;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        initOptions();
        waitForStart();

        if (opModeIsActive()) {
//            waitForButton("LEFT 0.5");
//            dt.move(Direction.LEFT, 0.5);
//            waitForButton("stop");
//
//            waitForButton("FORWARD 0.5");
//            dt.move(Direction.FORWARD, 0.5);
//            waitForButton("stop");
//
//            waitForButton("RIGHT 0.5");
//            dt.moveP(Direction.RIGHT, 0.5, 18, 10);
//            waitForButton("stop");

            while (opModeIsActive()) {
                options.setOptions();
                String method = options.getOption("method").getChoice();
                String direction = options.getOption("direction").getChoice();
                double power = options.getOption("power").getValue();
                double value = options.getOption("value").getValue();

                Direction dir;
                switch (direction) {
                    case "FORWARD":
                        dir = Direction.FORWARD;
                        break;
                    case "BACK":
                        dir = Direction.BACK;
                        break;
                    case "RIGHT":
                        dir = Direction.RIGHT;
                        break;
                    case "LEFT":
                        dir = Direction.LEFT;
                        break;
                    case "CW":
                        dir = Direction.CW;
                        break;
                    case "CCW":
                        dir = Direction.CCW;
                        break;
                    case "UP":
                        dir = Direction.UP;
                        break;
                    case "DOWN":
                        dir = Direction.DOWN;
                        break;
                    default:
                        dir = Direction.FORWARD;
                }

                switch (method) {
                    case "move":
                        dt.move(dir, power);
                        waitForButton("Press a to stop");
                        dt.stopAll();
                        break;
                    case "moveP":
                        dt.moveP(dir, power, value, 10);
                        break;
                    case "rotateIMU":
                        dt.rotateIMUPID(dir, value, 10);
                        break;
                    case "moveSlides":
                        hs.moveSlides(dir, power, value, 10);
                        break;
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
    }

    private void initialize() {
        dt = new DriveTrainNew(this);
        hs = new HangSlides(this);
        vision = new Vision(this);

//        goldXTelem = telemetry.addData("Gold mineral position", -1);
//        telemDirection = telemetry.addData("Direction", "NONE");

        telemetry.update();
    }

    private void initOptions() {
        options = new Options(this);
        options.addCategoricalOption("method",
                new String[]{"move", "moveP", "rotateIMU", "moveSlides"});
        options.addCategoricalOption("direction",
                new String[]{"FORWARD", "BACK", "LEFT", "RIGHT", "CW", "CCW", "UP", "DOWN"});
        options.addQuantitativeOption("power", 0, 1, 0.05);
        options.addQuantitativeOption("value", 0, 180, 1);
    }

//    private void options() {
//        boolean confirmed = false;
//
//        while(!confirmed) {
//
//            if (gamepad1.a) {
//                if (gamepad1.dpad_up) {
//                    if (method < 1) {
//                        method++;
//                    }
//                } else if (gamepad1.dpad_down) {
//                    if (method > 0) {
//                        method--;
//                    }
//                }
//            } else if (gamepad1.b) {
//                if (gamepad1.dpad_up) {
//                    if (direction < 5) {
//                        direction++;
//                    }
//                } else if (gamepad1.dpad_down) {
//                    if (direction > 0) {
//                        direction--;
//                    }
//                }
//            } else if (gamepad1.x) {
//                if (gamepad1.dpad_up) {
//                    if (power < 1.0) {
//                        power += 0.05;
//                    }
//                } else if (gamepad1.dpad_down) {
//                    if (power > 0) {
//                        power -= 0.05;
//                    }
//                }
//            } else if (gamepad1.y) {
//                if (gamepad1.dpad_up) {
//                    if (value < 180) {
//                        value++;
//                    }
//                } else if (gamepad1.dpad_down) {
//                    if (value > 0) {
//                        value--;
//                    }
//                }
//            } else if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
//                telemetry.addLine("Confirmed!");
//                confirmed = true;
//            }
//
//            updateOptions();
//            telemetry.update();
//
//            Utils.waitFor(100);
//        }
//    }

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
        double minError = 100;

        if (vision.detect() == -1) {
            goldXTelem.setValue(-1);
        } else {
            Direction direction;
            int goldX;
            int error;

            double startTime = System.currentTimeMillis();
            do {
                goldX = vision.detect();
                error = GOLD_ALIGN_LOC - goldX;

                if (error < 0) {
                    direction = Direction.CW;
                }else if(error>0) {
                    direction = Direction.CCW;
                }else{
                    return;
                }

                dt.move(direction, power);
                goldXTelem.setValue(goldX);
                telemetry.update();
            } while (goldX != -1 &&
                    Math.abs(error) > minError &&
                    (System.currentTimeMillis() - startTime) / 1000 < timeoutS);
        }
        dt.stopAll();
        telemetry.update();
    }

    private void haltUntilPressStart() {
        while (!gamepad1.start  && !isStopRequested()) {
            Utils.waitFor(300);
        }
    }

}
