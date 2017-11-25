package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.OpModes.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.directcurrent.core.gamecontroller.Controller;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain;


/**
 * Main TeleOp for Relic Recovery Robot
 */
@TeleOp(name = "Main TeleOp" , group = "Relic Recovery")
@SuppressWarnings({"unused", "ConstantConditions"})
public class TeleOpMain extends LinearOpMode
{
    private Base _base = new Base();        // Robot base

    private Controller _controller1 = new Controller();


    /**
     * Runs the main TeleOp
     *
     * @throws InterruptedException Exception when OpMode is interrupted
     */
    @Override
    public void runOpMode() throws InterruptedException
    {
        _base.init(hardwareMap , this);

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
        _controller1.read(gamepad1);

        if(_controller1.xClicked())
        {
            _base.drivetrain.flipDirection();
        }


        if(_controller1.aClicked())
        {
            _base.drivetrain.flipSpeed();
        }
    }


    /**
     * Runs components based on input given
     */
    private void _runComponents()
    {
        _base.drivetrain.run(-_controller1.leftY() , _controller1.rightX() , true);
        _base.lift.run(-gamepad2.left_stick_y);
        telemetry.addData("Controls, 1LY" , -gamepad1.left_stick_y);
        telemetry.addData("Controls, 1RX" , gamepad1.right_stick_x);
        telemetry.addData("Controls, 2LY" , -gamepad2.left_stick_y);
    }
}
