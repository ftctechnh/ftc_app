package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi Thumuluri on 10/5/18.
 */
@Autonomous(name = "FollowWall", group = "Auto")
public class FollowWall extends LinearOpMode
{
    ParadeBot paradeBot;
    public void runOpMode()
    {
        /*measure distance from sensor from wall
        switch (distanceWall)
        {
            case 'l' //l is left away from wall

    }
}
