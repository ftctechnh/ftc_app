package org.firstinspires.ftc.teamcode.prototype;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.drivetrain.ROUSAutoHardware_WithServos;
import org.firstinspires.ftc.teamcode.fieldtracking.Tracker;

/**
 * Created by ROUS on 2/25/2017.
 */

@TeleOp(name = "SarahsVuForia", group = "Pushbot")
//@Disabled
public class SarahsFirstOp extends LinearOpMode {
    // Use a Pushbot's hardware


    @Override
    public void runOpMode() throws InterruptedException {
        ROUSAutoHardware_WithServos robot = new ROUSAutoHardware_WithServos();
        Tracker vftracker = new Tracker();
        vftracker.intializefunction(this.telemetry,new VectorF(8f, 3f, 7f));

      /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        robot.button.setPosition(.56);
        robot.servo.setPosition(.8);
        robot.leftshooter.setPower(-1);
        robot.rightshooter.setPower(1);
        sleep(1000);
        robot.leftshooter.setPower(0);
        robot.rightshooter.setPower(0);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        //This starts vuforia tracking objects on the field
        vftracker.beginTracking();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //VectorF position = vftracker.updateLastKnownLocation();

        }

    }
}
