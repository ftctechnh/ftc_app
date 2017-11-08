package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotplus.gamepadwrapper.ControllerWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Robot;

/**
 * @author Blake Abel, Alex Migala
 */
@TeleOp(name="Mecanum Arcade Drive", group="Iterative Opmode")
public class MecanumArcadeDrive extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private Robot robot;
  
    private ControllerWrapper game1;

    private MecanumDrive drivetrain;

    private DcMotor raiser;
    private Servo grabber;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        robot = new Robot(hardwareMap);
        game1 = new ControllerWrapper(gamepad1);
        drivetrain = (MecanumDrive) robot.getDrivetrain();

        raiser = hardwareMap.dcMotor.get("raiser");
        grabber = hardwareMap.servo.get("grabber");

        raiser.setDirection(DcMotorSimple.Direction.REVERSE);
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

        drivetrain.complexDrive(gamepad1, telemetry);

        if(gamepad1.y){
            raiser.setPower(1);
        } else if (gamepad1.a) {
            raiser.setPower(-1);
        } else {
            raiser.setPower(0);
        }

        // Disabled gripper during open house because hardware hoohas
        if(gamepad1.left_bumper){
            grabber.setPosition(Math.max(0, grabber.getPosition() - 0.01));
        } else if (gamepad1.right_bumper){
            grabber.setPosition(Math.min(1, grabber.getPosition() + 0.01));
        }

        telemetry.addData("Servo Position", grabber.getPosition());


        /*
        if (gamepad1.a) {
            // cache the gamepad a button
            game1.getLetterInterface().getAButton().setPress(true);
        }
        else if (game1.getLetterInterface().getAButton().isPressed()) {
            // do something
            // set the interface back
            game1.getLetterInterface().getAButton().setPress(false);
        }
        */

        telemetry.addData("Main1", drivetrain.getmajorDiagonal().getMotor1().getPower());
        telemetry.addData("Minor1", drivetrain.getMinorDiagonal().getMotor1().getPower());
        telemetry.addData("Minor2", drivetrain.getMinorDiagonal().getMotor2().getPower());
        telemetry.addData("Main2", drivetrain.getmajorDiagonal().getMotor2().getPower());

    }

    // Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {

    }

}
