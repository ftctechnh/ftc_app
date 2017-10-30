package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilToggle;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain;


/**
 * Main TeleOp for Relic Recovery Robot
 */
@TeleOp(name = "Main TeleOp" , group = "Relic Recovery")
public class OpMain extends LinearOpMode
{
    private Base _base = new Base();        // Robot base

    // Control Togglers
    private UtilToggle _toggleReverse = new UtilToggle();
    private UtilToggle _toggleSlow = new UtilToggle();



    @Override
    public void runOpMode() throws InterruptedException
    {
        _base.init(hardwareMap);

        waitForStart();

        _base.drivetrain.setState(Drivetrain.State.FORWARD_FAST);

        while(opModeIsActive())
        {
            _grabInput();

            _runComponents();

            telemetry.update();
        }
    }


    /**
     * Grabs input from the controllers and performs the various actions needed.
     */
    private void _grabInput()
    {
        if(_toggleReverse.isPressed(gamepad1.x))
        {
            _base.drivetrain.flipDirection();
        }

        if(_toggleSlow.isPressed(gamepad1.a))
        {
            _base.drivetrain.flipSpeed();
        }
    }


    private void _runComponents()
    {
        _base.drivetrain.run(-gamepad1.left_stick_y , gamepad1.right_stick_x , true);
        _base.lift.run(-gamepad2.left_stick_y);
        telemetry.addData("Controls, 1LY" , -gamepad1.left_stick_y);
        telemetry.addData("Controls, 1RX" , gamepad1.right_stick_x);
        telemetry.addData("Controls, 2LY" , -gamepad2.left_stick_y);
    }
}
