package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.Utils.Utils;
import org.firstinspires.ftc.teamcode.robotutil.Vision;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Rotate Test", group="FinalShit")

public class RotateTest extends LinearOpMode {

    private DriveTrain dt;
    private HangSlides hs;
    private Vision vision;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        if (opModeIsActive()) {
            dt.drive(Direction.FORWARD, 12, 5);
            Utils.waitFor(5000);
            dt.rotateTo(-45, 5);
            Utils.waitFor(5000);
            dt.rotateTo(45, 5);
            Utils.waitFor(5000);
            dt.drive(Direction.BACK, 12, 5);
            Utils.waitFor(5000);
            dt.rotateTo(-45, 5);
            Utils.waitFor(5000);
            dt.rotateTo(45, 5);
            Utils.waitFor(5000);
            dt.rotate(Direction.CCW, 180, 5);

            dt.stopAll();
        }
    }

    private void initialize() {
        dt = new DriveTrain(this);
        hs = new HangSlides(this);
        vision = new Vision(this);
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
