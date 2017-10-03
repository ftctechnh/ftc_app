/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-05

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.teamcode.TestCode.SixWheelPrototype;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


/**
 * The main op mode for the six wheel prototype
 */
@TeleOp(name = "Six Wheel" , group = "Prototypes")
public class OpMain extends LinearOpMode
{
    private SixWheelBase robot = new SixWheelBase();

    /**
     * Overwritten method from LinearOpMode, runs the op mode
     *
     * @throws InterruptedException Thrown when the op mode is interrupted
     */
    @Override
    public void runOpMode() throws InterruptedException
    {
        // Hardware map the robot and wait for the start button to be pushed
        robot.init(hardwareMap);
        waitForStart();

        //noinspection StatementWithEmptyBody
        while(opModeIsActive() && !isStopRequested())
        {
//            robot.drivetrain.runSequential(robot.drivetrain.stdDrive);
        }
    }
}
