import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.NewRobot;
import org.firstinspires.ftc.teamcode.TankBase;

/**
 * Created by sahithithumuluri on 11/18/17.
 */
@Disabled
@Autonomous(name = "JavaTestingSahithi", group = "Auto")
public class JavaTestingSahithi extends LinearOpMode
{
    NewRobot newRobot;
    TankBase robot;
    public void runOpMode()
    {
        newRobot = new NewRobot(hardwareMap);
        robot = new TankBase(hardwareMap);
        waitForStart();
        char colorOfPlatform = newRobot.getColor(newRobot.getFloorColorSens());
        switch (colorOfPlatform)
        {
            case 'b':
                robot.driveStraight_In(-24, .25);
                break;
            case 'r':
                robot.driveStraight_In(24, .25);
                break;
            default:
                robot.pivot_IMU(270,.25);
        }
    }
}