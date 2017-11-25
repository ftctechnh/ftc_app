package org.firstinspires.ftc.teamcode.TestCode.SpoolsPrototype;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;


/**
 * Robot Component that manages the spool aspect of the spools prototype
 */
class Spools extends RobotComponent
{
    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;

    @SuppressWarnings("WeakerAccess")
    public State state = State.STANDBY;


    /**
     * Several states that the spools can be in.
     *
     * STANDBY- the spools are inactive and ready for use
     * INTAKE- the spools will pull objects inward
     * OUTPUT- the spools will push things outward
     */
    enum State
    {
        STANDBY ,
        INTAKE ,
        OUTPUT
    }


    /**
     * Initializes the spools by hardware mapping the motors
     *
     * @param BASE The robot base used to create the hardware mapper
     */
    @Override
    public void init(final RobotBase BASE)
    {
        super.init(BASE);
        leftMotor = mapper.mapMotor("left" , DcMotorSimple.Direction.FORWARD);
        rightMotor = mapper.mapMotor("right" , DcMotorSimple.Direction.REVERSE);
    }


    /**
     * Runs the spools- operation is dependent upon Spool state.
     */
    public void run()
    {
        final double MAX_POWER = 1;

        switch(state)
        {
            case STANDBY:
                leftMotor.setPower(0);
                rightMotor.setPower(0);
                break;

            case INTAKE:
                leftMotor.setPower(MAX_POWER);
                rightMotor.setPower(MAX_POWER);
                break;

            case OUTPUT:
                leftMotor.setPower(-MAX_POWER);
                rightMotor.setPower(-MAX_POWER);
                break;
        }
    }
}
