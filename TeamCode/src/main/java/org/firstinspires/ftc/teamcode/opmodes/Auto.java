package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.robotutil.Options;
import org.firstinspires.ftc.teamcode.robotutil.Vision;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto Test", group="FinalShit")

public class Auto extends LinearOpMode {

    private DriveTrain dt;
    private HangSlides hs;
    private Vision vision;

    private Options options;

    // Tune these
    private static final double DIST_TO_GOLD = 12;
    private static final double DIST_TO_DEPOT = 36;
    private static final double DIST_TO_CRATER = 36;
    private static final int GOLD_ALIGN_LOC = 550;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        options.setOptions();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                String method = options.getOption("method").getChoice();
                String direction = options.getOption("direction").getChoice();
                double power = options.getOption("power").getValue();
                double inches = options.getOption("inches").getValue();
                double angle = options.getOption("angle").getValue();
                double timeoutS = options.getOption("timeoutS").getValue();

                Direction dir = Direction.valueOf(direction);

                switch (method) {
                    case "move":
                        dt.move(dir, power);
                        waitForButton("Press a to stop");
                        dt.stopAll();
                        break;
                    case "drive":
                        dt.drive(dir, inches, timeoutS);
                        break;
                    case "strafe":
                        dt.strafe(dir, inches, timeoutS);
                        break;
                    case "rotate":
                        dt.rotate(dir, angle, timeoutS);
                        break;
                    case "rotateTo":
                        dt.rotateTo(angle, timeoutS);
                        break;
                    case "moveSlides":
                        hs.moveSlides(dir, power, inches, timeoutS);
                        break;
                }

                options.setOptions();
            }

            dt.stopAll();
        }
    }

    private void initialize() {
        dt = new DriveTrain(this);
        hs = new HangSlides(this);
        vision = new Vision(this);
        initOptions();
    }

    private void initOptions() {
        options = new Options(this);
        options.addCategoricalOption("method",
                new String[]{"move", "drive", "strafe", "rotate", "rotateTo", "moveSlides"});
        options.addCategoricalOption("direction",
                new String[]{"FORWARD", "BACK", "LEFT", "RIGHT", "CW", "CCW", "UP", "DOWN"});
        options.addQuantitativeOption("power", 0, 1, 0.05);
        options.addQuantitativeOption("inches", 0, 24, 0.5);
        options.addQuantitativeOption("angle", 0, 180, 10);
        options.addQuantitativeOption("timeoutS", 0, 30, 1);
        options.addQuantitativeOption("1 / kp", 0, 360, 10);
        options.addQuantitativeOption("1 / ki", 0, 1000, 10);
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
}
