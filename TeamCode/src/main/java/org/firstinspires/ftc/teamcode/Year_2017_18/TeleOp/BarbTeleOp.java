package org.firstinspires.ftc.teamcode.Year_2017_18.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Year_2017_18.Robot.RobotHardware;

@TeleOp(name = "BarbTeleOp", group = "TeleOpMode")
@Disabled

public class BarbTeleOp extends OpMode
{

    public RobotHardware hardware = new RobotHardware();

    @Override
    public void start() {
        telemetry.addData("Status", "Robot has started running!");
    }


    @Override
    public void init() {

        hardware.init(hardwareMap);
        telemetry.addData("Status", "Robot has successfully initialized!");
    }


    @Override
    public void loop() {
/////////////////////////////////////////////////////////////////////////////////////////////////////
        float value_l = -gamepad1.left_stick_y; // Controls the drive chain
        float value_r = -gamepad1.right_stick_y;

        hardware.leftDrive.setPower(value_l * 0.7);
        hardware.rightDrive.setPower(value_r * 0.7);

        if (gamepad1.dpad_up) {hardware.leftDrive.setPower(1); hardware.rightDrive.setPower(1);}
        if (gamepad1.dpad_down) {hardware.leftDrive.setPower(-1); hardware.rightDrive.setPower(-1);}

////////////////////////////////////////////////////////////////////////////////////////////////////

        if (gamepad1.left_trigger > 0.5)                 //Pulley Down
        {
            hardware.pulleyArm.setPower(-0.5);
        } else if (gamepad1.right_trigger > 0.5)            //Pulley Up
        {
            hardware.pulleyArm.setPower(0.5);
        } else                                    // Pulley Stop
        {
            hardware.pulleyArm.setPower(0.0);
        }

////////////////////////////////////////////////////////////////////////////////////////////////////

        if (gamepad1.right_bumper)                 //Claw Grabs.
        {
            hardware.leftClaw.setPosition(0.8);
            hardware.rightClaw.setPosition(0.8);
        }

        if (gamepad1.left_bumper)                 //Claw Releases.
        {
            hardware.leftClaw.setPosition(0.3);
            hardware.rightClaw.setPosition(0.4);
        }

////////////////////////////////////////////////////////////////////////////////////////////////////

        if (gamepad2.a)       //Sensor Arm moves left.
        {
            hardware.sensorArm.setPosition(0);
        }
        if (gamepad2.b) //Sensor Arm moves right.
        {
            hardware.sensorArm.setPosition(1);
        }
        if (gamepad2.x)
        {
            hardware.colorRotate.setPosition(0);
        }
        if (gamepad2.y)
        {
            hardware.colorRotate.setPosition(1);
        }
////////////////////////////////////////////////////////////////////////////////////////////////////
        if (gamepad1.a)
        {
            hardware.relicArm.setPower(0.5);
        }
        else if (gamepad1.b)
        {
            hardware.relicArm.setPower(-0.5);
        }
        else
        {
            hardware.relicArm.setPower(0);
        }
        if (gamepad1.x)
        {
            hardware.relicClaw.setPosition(0);
        }
        if (gamepad1.y)
        {
            hardware.relicClaw.setPosition(1);
        }
    }


    @Override
    public void stop() {
        telemetry.addData("Status", "Robot has stopped!");
    }
}