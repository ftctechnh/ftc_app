package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@Autonomous(name = "SwitchAutoDepotV1")
public class SwitchAutoDepotV1 extends LinearOpMode
{
    CompRobot compRobot;
    VuforiaFunctions vuforiaFunctions;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap, this);
        vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
        compRobot.getSwitchSample().getState();
        compRobot.getSwitchDepot().getState();
        compRobot.getSwitchCrater().getState();
        compRobot.getSwitchDelay().getState();
        waitForStart();
        compRobot.climbDown();
        sleep(200);

        //if (compRobot.getSwitchSample().getState() = true)
        {

        }
        //if (compRobot.getSwitchDepot().getState() = true)
        {

        }
        //if (compRobot.getSwitchCrater().getState() = true)
        {

        }
        //if (compRobot.getSwitchDelay().getState() = true)
        {
            
        }
    }
}
