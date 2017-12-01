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
    private DcMotor _extenderMotor;


    /**
     * Initializes Relic Extender and hardware maps it
     *
     * @param BASE The robot base used to create the hardware mapper
     */
    @Override
    public void init(final RobotBase BASE)
    {
        super.init(BASE);
        _extenderMotor = mapper.mapMotor("extMotor" , DcMotorSimple.Direction.REVERSE);
    }


    /**
     * Runs the Relic Extender
     *
     * @param POWER Power to set the extender to
     */
    public void run(final double POWER)
    {
        _extenderMotor.setPower(POWER * .5);
    }


    /**
     * Stops the relic extender
     */
    @Override
    public void stop()
    {
        // Do nothing :)
    }
}
