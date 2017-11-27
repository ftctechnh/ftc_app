package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.OpModes.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.directcurrent.core.gamecontroller.Controller;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber.GlyphGrabber;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.RelicGrabber.RelicGrabber;


/**
 * Main TeleOp for Relic Recovery Robot
 */
@TeleOp(name = "Main TeleOp" , group = "Relic Recovery")
@SuppressWarnings({"unused", "ConstantConditions"})
public class TeleOpMain extends LinearOpMode
{
    private Base _base = new Base();        // Robot base

    private Controller _controller1 = new Controller();
    private Controller _controller2 = new Controller();


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

            _addTelem();

            telemetry.update();
        }

        _base.stop();
    }


    /**
     * Grabs input from the controllers and performs the various actions needed.
     */
    private void _grabInput()
    {
        // Read data ///////////////////////////////////////////////////////////////////////////////
        _controller1.read(gamepad1);
        _controller2.read(gamepad2);

        _base.imu.pull();


        // Allow driver control when requested /////////////////////////////////////////////////////
        if(_controller1.receivingInput())
        {
            _base.drivetrain.allowInput();
        }


        // Define controls /////////////////////////////////////////////////////////////////////////
        _base.drivetrain.run(-_controller1.leftY() , _controller1.rightX() , true);
        _base.lift.run(-_controller2.leftY());

        // Toggle drivetrain reversal
        if(_controller1.xClicked())
        {
            _base.drivetrain.flipDirection();
        }

        // Toggle drivetrain slow/fast mode
        if(_controller1.aClicked())
        {
            _base.drivetrain.flipSpeed();
        }

        // Glyph Grabber state machine
        if(_controller2.rightBumper())
        {
            _base.glyphGrabber.setState(GlyphGrabber.State.INPUT);
        }
        else if(_controller2.leftBumper())
        {
            _base.glyphGrabber.setState(GlyphGrabber.State.OUTPUT);
        }
        else
        {
            _base.glyphGrabber.setState(GlyphGrabber.State.STOP);
        }

        // Runs relic extender
        _base.relicExtender.run(_controller2.rightY());

        // State machine for relic grabber grabber
        if (_controller2.rightTrigger() >= .15)
        {
            _base.relicGrabber.setGrabState(RelicGrabber.GrabState.IN);
        }
        else if(_controller2.leftTrigger() >= .15)
        {
            _base.relicGrabber.setGrabState(RelicGrabber.GrabState.OUT);
        }
        else
        {
            _base.relicGrabber.setGrabState(RelicGrabber.GrabState.STOP);
        }

        // State machine for relic grabber turning
        if (_controller2.x())
        {
            _base.relicGrabber.setRotateState(RelicGrabber.RotateState.UP);
        }
        else if (_controller2.a())
        {
            _base.relicGrabber.setRotateState(RelicGrabber.RotateState.DOWN);
        }
        else
        {
            _base.relicGrabber.setRotateState(RelicGrabber.RotateState.STILL);
        }

        // IMU fast calibration
        if(_controller1.leftStickButtonClicked())
        {
            _base.imu.fastCalibrate();
        }
    }


    /**
     * Adds data to telemetry
     */
    private void _addTelem()
    {
        telemetry.addData("IMU XA" , _base.imu.xAngle());
        telemetry.addData("IMU YA" , _base.imu.yAngle());
        telemetry.addData("IMU ZA" , _base.imu.zAngle());

        telemetry.addData("Left Drive Encoder" , _base.drivetrain.leftEncoderCount());
        telemetry.addData("Right Drive Encoder" , _base.drivetrain.rightEncoderCount());
    }
}
