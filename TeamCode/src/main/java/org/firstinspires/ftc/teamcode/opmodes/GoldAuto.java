package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.robotutil.Options;
import org.firstinspires.ftc.teamcode.robotutil.Vision;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "AutonomousGold", group="FinalShit")

public class GoldAuto extends LinearOpMode {

    private DriveTrain dt;
    private Vision vision;
    private HangSlides hangSlides;

    private Options options;

    // Tune these
    private final double DIST_TO_GOLD = 12;
    private final double DIST_TO_DEPOT = 36;
    private final double DIST_TO_CRATER = 36;
    private final int GOLD_ALIGN_LOC = 550;

    private static double MIN_TURN_POWER = 0.07;
    private double goldAlignKp = 1.0 / 180.0;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        options.setOptions();
//        hangSlides.moveSlides(Direction.DOWN,.5,2.5,5);
        waitForStart();
//        hangSlides.moveSlides(Direction.UP,.5,2.5,5);
        dt.drive(Direction.FORWARD,6,100);
        dt.rotate(Direction.CW,25,10000);

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                goldAlignKp = 1.0 / options.getOption("kp").getValue();
                MIN_TURN_POWER = options.getOption("minPower").getValue();

                goldAlign(.3,100000, goldAlignKp);
                dt.drive(Direction.FORWARD, 30, 10);

                options.setOptions();
            }
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
        double minError = 10;
        Telemetry.Item t = this.telemetry.addData("status","initializing");
        Telemetry.Item telGoldX = this.telemetry.addData("gold x","N/A");
        Telemetry.Item telError = this.telemetry.addData("error","idk");
        Telemetry.Item telPower = this.telemetry.addData("power","idk");

        int j = vision.detectRobust(10);
        while (j == -1) {
            dt.rotate(Direction.CCW, 20, 5);

            telGoldX.setValue(j);
            t.setValue("cannot detectRobust(10:/");
            telemetry.update();
            sleep(100);
            j = vision.detectRobust(10);
        }
        dt.stopAll();

        t.setValue("found!!!!!");
        telGoldX.setValue(j);
        telemetry.update();

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
                sleep(200);
                dt.stopAll();
                sleep(200);

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
}
