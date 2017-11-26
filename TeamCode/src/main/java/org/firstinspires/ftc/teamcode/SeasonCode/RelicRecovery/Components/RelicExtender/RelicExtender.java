package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.RelicExtender;

import android.widget.Switch;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;

/**
 * Created by AlexPC on 11/25/2017.
 */

public class RelicExtender extends RobotComponent
{
    private final double OutSpeed = 1;
    private final double InSpeed = -1;
    private final double Still = 0;

    private DcMotor ExtenderMotor;


    public enum State
    {
        RInOut,
        Still
    }


    @Override
    public void init(final RobotBase BASE)
    {
        super.init(BASE);
        ExtenderMotor = mapper.mapMotor("ExtenderMotor" , DcMotorSimple.Direction.REVERSE);
    }


    public void setState(final State STATE)
    {
        switch(STATE)
        {
            case RInOut:
            ExtenderMotor.setPower(InSpeed);

            break;

            case Still:
            ExtenderMotor.setPower(Still);
        }

    }


}
