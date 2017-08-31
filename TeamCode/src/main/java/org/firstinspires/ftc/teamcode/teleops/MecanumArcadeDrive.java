package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotplus.hardware.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Robot;

/**
 * @author Blake Abel, Alex Migala
 * @since 8/24/17
*/
@TeleOp(name="Mecanum Arcade Drive", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
public class MecanumArcadeDrive extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private Robot robot;

    /**
     * This is a variable that is being tested
     */
    public boolean testVar;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        robot = new Robot(hardwareMap);
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
            ((MecanumDrive) robot.getDrivetrain()).arcadeDrive(gamepad1.left_stick_x, gamepad1.left_stick_y);
        }
    }

    // Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {
    }

}
