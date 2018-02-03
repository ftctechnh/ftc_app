package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Lift;


import org.firstinspires.ftc.robotcontroller.internal.Core.RobotCommand;


public class LiftToPosition extends RobotCommand
{
    /**
     * Predetermined positions to set the lift two based on glyph heights in the cryptobox
     */
    enum Position
    {
        GROUND(0) ,
        MID1(2_000) ,
        MID2(4_000) ,
        TOP(6_000);


        private long _encoderCount;


        Position(final long E_COUNT)
        {
            _encoderCount = E_COUNT;
        }


        /**
         * @return The encoder count of the position
         */
        public long encoderCount()
        {
            return _encoderCount;
        }
    }


    public void setParams(final Position POSITION)
    {
        
    }



    @Override
    public void runSequentially()
    {

    }


    @Override
    public void runParallel()
    {

    }


    @Override
    public boolean isBusy()
    {
        return false;
    }


    @Override
    public void stop()
    {

    }
}
