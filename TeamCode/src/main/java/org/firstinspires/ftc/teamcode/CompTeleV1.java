package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "CompTeleV1")
public class CompTeleV1 extends OpMode
{
    private CompRobot compRobot;
    private double wristPosition = 0;

    @Override
    public void init()
    {
        compRobot = new CompRobot(hardwareMap);
        gamepad2.setJoystickDeadzone(.3f);
        gamepad1.setJoystickDeadzone(.3f);
    }

    @Override
    public void loop()
    {
        //Gramepad 1 -- DRIVER
        //Driving robot
        telemetry.addData("LJoyStick= ", -gamepad1.left_stick_y);
        telemetry.addData("RJoyStick= ", -gamepad1.right_stick_y);
        telemetry.addData("Right Encod Ct: ", compRobot.getDriveRightOne().getCurrentPosition());
        telemetry.addData("Left Encod Ct: ", compRobot.getDriveLeftOne().getCurrentPosition());
        compRobot.driveMotors(-gamepad1.left_stick_y, -gamepad1.right_stick_y);

        // control the motor for climbing
        compRobot.getClimberMotor().setPower(gamepad1.right_trigger-gamepad1.left_trigger);
        telemetry.addData("CLimbMotorPow: ", gamepad1.right_trigger-gamepad1.left_trigger);
        telemetry.addData("ClimbMotorEnc: ", compRobot.getClimberMotor().getCurrentPosition());

        //"Presets" For climbing

        //Gamepad 2 -- Attachments

        //The grabber wheel stuff, AKA the mineral Ejector
        if (gamepad2.left_trigger > .2f)
        {
            compRobot.setGrabberWheelPower(-gamepad2.left_trigger);
        }
        else if (gamepad2.left_bumper)
        {
            compRobot.setGrabberWheelPower(1); //Positive power means you're spitting it out
        }
        else
        {
            compRobot.setGrabberWheelPower(0);
        }

        //This controls the wrist of the grabbers

        if (gamepad2.right_trigger > .2f)
        {
            wristPosition += .0038;
            if (wristPosition > 1)
                wristPosition = 1;
        }
        else if (gamepad2.right_bumper)
        {
            wristPosition -= .0038;
            if (wristPosition < 0)
                wristPosition = 0;
        }
        compRobot.getWristCollectorServo().setPosition(wristPosition);
        telemetry.addData("WristPosition: ", wristPosition);

        //Extender Controls (not climbing)
        compRobot.getCollectorLifterMotor().setPower(-gamepad2.left_stick_y);

        //The entire arm pivot controls or shoulder controls
        compRobot.getCollectorPivoterMotor().setPower(-gamepad2.right_stick_y/4);

        telemetry.update();
    }

    @Override
    public void stop()
    {
        compRobot.stopDriveMotors();
        super.stop();
    }
}
