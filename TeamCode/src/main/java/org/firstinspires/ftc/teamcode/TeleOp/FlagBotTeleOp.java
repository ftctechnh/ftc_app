package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot.ClawbotHardware;
import org.firstinspires.ftc.teamcode.Robot.FlagBotHardware;

/**
 * Created by Kearneyg20428 on 2/7/2017.
 */
@TeleOp(name="Flagbot", group="TeleOp")
public class FlagBotTeleOp extends OpMode  {

    final double    CLAW_SPEED  = 0.05 ;
    double          clawOffset  = 0.0 ;

    FlagBotHardware robot       = new FlagBotHardware();

    @Override
    public void init() {

        robot.init(hardwareMap);

    }

    @Override
    public void loop() {
        double left;
        double right;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        left = -gamepad1.left_stick_y;
        right = -gamepad1.right_stick_y;
        robot.leftMotor.setPower(left);
        robot.rightMotor.setPower(right);




        // Do not let the servo position go too wide or the plastic gears will come unhinged.

        // Move both servos to new position.

        // Use gamepad buttons to move the arm up (Y) and down (A)
        if (gamepad1.a)
            robot.spinMotor.setPower(.75);
        else if (gamepad1.b)
            robot.spinMotor.setPower(-.75);
        else
            robot.spinMotor.setPower(0.0);

        // Send telemetry message to signify robot running;

    }
}
