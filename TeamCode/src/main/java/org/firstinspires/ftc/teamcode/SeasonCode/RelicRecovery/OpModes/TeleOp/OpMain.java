package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilToggle;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain;


/**
 * Main TeleOp for Relic Recovery Robot
 */
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
    }


    /**
     * Grabs input from the controllers and performs the various actions needed.
     */
    private void _grabInput()
    {
        if(_toggleReverse.isPressed(gamepad1.x))
        {
            if(_base.drivetrain.state() == Drivetrain.State.FORWARD)
            {
                _base.drivetrain.setState(Drivetrain.State.REVERSE);
            }
            else
            {
                _base.drivetrain.setState(Drivetrain.State.FORWARD);
            }
        }
    }


    private void _runComponents()
    {
        _base.drivetrain.run(-gamepad1.left_stick_y , gamepad1.right_stick_x , true);
    }
}
