package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Lift;


import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotCommand;


public class LiftToPosition extends RobotCommand
{
    private Lift _lift;

    private Position _position;
    private double _maxSpeed;
    private long _timeout;

    private boolean _busy = false;
    private boolean _interrupt = false;

    private Thread _t;


    /**
     * Creates a lift to position command
     *
     * @param LIFT Lift object that the command runs under
     */
    public LiftToPosition(final Lift LIFT)
    {
        _lift = LIFT;
    }


    /**
     * Predetermined positions to set the lift two based on glyph heights in the cryptobox
     */
    enum Position
    {
        GROUND(0) ,
        MID1(2_000) ,
        MID2(4_000) ,
        TOP(6_000);


        private int _encoderCount;


        Position(final int E_COUNT)
        {
            _encoderCount = E_COUNT;
        }


        /**
         * @return The encoder count of the position
         */
        public int encoderCount()
        {
            return _encoderCount;
        }
    }


    /**
     * Sets up LiftToPosition commmand for use by populating its parameters
     *
     * @param POSITION Position to set the lift to
     * @param MAX_SPEED Maximum speed to run the lift for- speed varies due to PID loop
     * @param TIMEOUT Length of time before command auto-quits
     */
    public void setParams(final Position POSITION , final double MAX_SPEED , final long TIMEOUT)
    {
        _position = POSITION;
        _maxSpeed = MAX_SPEED;
        _timeout = TIMEOUT;

        _lift.motor().setTargetPosition(POSITION.encoderCount());
    }



    @Override
    public void runSequentially()
    {
        _busy = true;

        if(_lift.motor().getMode() != DcMotor.RunMode.RUN_TO_POSITION)
        {
            _lift.motor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        _lift.motor().setPower(_maxSpeed);


        long startTime = System.currentTimeMillis();

        while(_lift.motor().isBusy() && _lift.base().opMode().opModeIsActive() && !_interrupt
                && System.currentTimeMillis() - startTime < _timeout)
        {
            // Nothing
        }

        _lift.motor().setPower(0);

        _busy = false;
    }


    @Override
    public void runParallel()
    {
        if(_t == null)
        {
            t = new Thread(this::runSequentially);

            _t.start();
        }
    }


    @Override
    public boolean isBusy()
    {
        return _interrupt;
    }


    @Override
    public void stop()
    {
        _interrupt = true;
    }
}
