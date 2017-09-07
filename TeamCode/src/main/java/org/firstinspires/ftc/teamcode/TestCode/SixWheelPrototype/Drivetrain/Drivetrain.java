/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.teamcode.TestCode.SixWheelPrototype.Drivetrain;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Core.RobotBase;
import org.firstinspires.ftc.teamcode.Core.RobotComponent;


/**
 * Class that manages the 6-wheel drivetrain. It contains all of the commands that the drivetrain
 * can execute.
 */
public class Drivetrain extends RobotComponent
{
    // Drivetrain motors, left and right
    private DcMotor motorLeft = null;
    private DcMotor motorRight = null;


    /**
     * Initializes the drivetrain- calls the RobotComponent constructor
     */
    public Drivetrain()
    {
        super();
    }


    /**
     * Hardware maps the drivetrain
     *
     * @param BASE The RobotBase object this component is a part of
     */
    @Override
    public void init(final RobotBase BASE)
    {
        super.init(BASE);
        motorLeft = mapper.mapMotor("driveLeft" , DcMotorSimple.Direction.FORWARD);
        motorRight = mapper.mapMotor("driveRight" , DcMotorSimple.Direction.REVERSE);
    }


    /**
     * Standard drive- currently has testing values. The drivetrain is fixed at maximum speed.
     */
    public Command stdDrive = () ->
    {
        motorLeft.setPower(1);
        motorRight.setPower(1);
    };
}
