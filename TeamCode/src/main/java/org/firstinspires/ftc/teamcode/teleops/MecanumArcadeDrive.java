package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotplus.gamepadwrapper.ControllerWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Robot;

import java.util.ResourceBundle;

/**
 * @author Blake Abel, Alex Migala
 */
@TeleOp(name="Mecanum Arcade Drive", group="Iterative Opmode")
public class MecanumArcadeDrive extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private Robot robot;
  
    private ControllerWrapper game1;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        robot = new Robot(hardwareMap);
        game1 = new ControllerWrapper(gamepad1);
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {

    }

    // Code to run ONCE when the driver hits PLAY
    @Override
    public void start() {
        runtime.reset();
    }

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        telemetry.addData("Status", "Running: " + runtime.toString());

        if(robot.getDrivetrain() instanceof MecanumDrive) {
            ((MecanumDrive) robot.getDrivetrain()).complexDrive(gamepad1);
            ((MecanumDrive) robot.getDrivetrain()).dPadDrive(gamepad1);
        }

        if (gamepad1.a) {
            // cache the gamepad a button
            game1.getLetterInterface().getAButton().setPress(true);
        }
        else if (game1.getLetterInterface().getAButton().isPressed()) {
            // do something
            // set the interface back
            game1.getLetterInterface().getAButton().setPress(false);
        }
    }

    // Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {

    }

}
