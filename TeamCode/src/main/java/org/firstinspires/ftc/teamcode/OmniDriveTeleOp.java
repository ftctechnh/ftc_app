package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Peter on 9/22/2016.
 */
@TeleOp(name = "OmniBotTele", group = "Comp")
public class OmniDriveTeleOp extends OpMode
{
    private OmniDriveBot robot = new OmniDriveBot();

    public void init()
    {
        robot.init(hardwareMap);
        gamepad1.setJoystickDeadzone(0.01f);
    }

    public void loop()
    {
        //driver 1 controls
        robot.setLeftYIn(gamepad1.left_stick_y);
        robot.setLeftXIn(gamepad1.left_stick_x);
        robot.setRightXIn(gamepad1.right_stick_x);

        if(gamepad1.left_bumper == true)
        {
            robot.spin(90);
        }
        else if(gamepad1.right_bumper == true)
        {
            robot.spin(-90);
        }

        //driver 2 controls

        //scoop in and out controls
        if(gamepad2.left_bumper == true)
        {
            robot.setScooperServoPos(0.5f);
        }
        else if(gamepad2.left_trigger != 0)
        {
            robot.setScooperServoPos(1.0f);
        }
        else
        {
            robot.setScooperServoPos(0.0f);
        }
        //lifter up and down controls
        if(gamepad2.right_bumper == true)
        {
            robot.setLifterPower(1.0f);
        }
        else if(gamepad2.right_trigger != 0)
        {
            robot.setLifterPower(-1.0f);
        }
        else
        {
            robot.setScooperServoPos(0.0f);
        }
        //shooter pitch controls
        if(gamepad2.dpad_up == true)
        {
            robot.setShooterPitchServoPos(0.5f);
        }
        else if(gamepad2.dpad_down == true)
        {
            robot.setShooterPitchServoPos(1.0f);
        }
        else
        {
            robot.setShooterPitchServoPos(0.0f);
        }
        // rev up shooter motors
        if(gamepad2.a == true)
        {
            robot.setShooterPowerOne(1.0f);
            robot.setShooterPowerTwo(-1.0f);
        }
        else
        {
            robot.setShooterPowerOne(0.0f);
            robot.setShooterPowerTwo(0.0f);
        }

        //apply changes and move robot
        robot.drive();
    }
}
