package org.firstinspires.ftc.teamcode.TestCode.CoreTest;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;


class TestComponent extends RobotComponent
{
    @SuppressWarnings("WeakerAccess")
    DcMotor motor;

    @Override
    public void init(final RobotBase BASE)
    {
        super.init(BASE);

        motor = mapper.mapMotor("motor" , DcMotorSimple.Direction.FORWARD);
    }


    void setPower(final double POWER)
    {
        motor.setPower(POWER);
    }
}
