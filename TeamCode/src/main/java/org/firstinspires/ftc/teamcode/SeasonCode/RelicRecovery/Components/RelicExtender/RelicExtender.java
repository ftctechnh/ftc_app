package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.RelicExtender;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;


/**
 * Relic Extender for our Relic Recovery robot (it's the cascading slides)
 */
public class RelicExtender extends RobotComponent
{
    private final double _outSpeed = 1;
    private final double _inSpeed = -1;
    private final double _idleSpeed = 0;

    private DcMotor _extenderMotor;


    public enum State
    {
        RInOut,
        Still
    }


    @Override
    public void init(final RobotBase BASE)
    {
        super.init(BASE);
        _extenderMotor = mapper.mapMotor("ExtenderMotor" , DcMotorSimple.Direction.REVERSE);
    }


    /**
     * Sets the state of the relic extender arm
     * @param STATE
     */
    public void setState(final State STATE)
    {
        switch(STATE)
        {
            case RInOut:
                _extenderMotor.setPower(_inSpeed);
                break;

            case Still:
                _extenderMotor.setPower(_idleSpeed);
                break;
        }

    }
}
