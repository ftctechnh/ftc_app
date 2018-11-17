package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.robotutil.Options;
import org.firstinspires.ftc.teamcode.robotutil.Vision;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group="FinalShit")

public class AutoNew extends LinearOpMode {

    private DriveTrainNew dt;
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
                double value = options.getOption("value").getValue();

                Direction dir = Direction.valueOf(direction);

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

                options.setOptions();
            }
        }
    }

    private void initialize() {
        dt = new DriveTrainNew(this);
        hs = new HangSlides(this);
        vision = new Vision(this);
        initOptions();
    }

    private void initOptions() {
        options = new Options(this);
        options.addCategoricalOption("method",
                new String[]{"move", "moveP", "rotateIMU", "moveSlides"});
        options.addCategoricalOption("direction",
                new String[]{"FORWARD", "BACK", "LEFT", "RIGHT", "CW", "CCW", "UP", "DOWN"});
        options.addQuantitativeOption("power", 0, 1, 0.05);
        options.addQuantitativeOption("value", 0, 180, 0.2);
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
