package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "ConceptAutonomousDepot" )
public class ConceptAutonomousDepot extends LinearOpMode
{

    ParadeBot walle;
    public void runOpMode()
    {

        walle = new ParadeBot(hardwareMap, this);
        waitForStart();

        // have servos move robot down to the ground
        walle.driveStraight_In(36, .4f);
        walle.stopDriveMotors();
        sleep(500);

        while(walle.getDistFromFront_In() > 24 && walle.getDistFromRight_In() > 24)
        {
            telemetry.addData("leftdist",walle.getDistFromFront_In());
            telemetry.addData("righdist",walle.getDistFromRight_In());
            telemetry.update();

            walle.driveMotorsAuto(.2f, .4f);
        }
        walle.stopDriveMotors();

        //drop marker into depot
        walle.pivot_IMU(-60f);
        //insert wall hug program
        // stop robot when distance sensor says the robot is X inches from wall
    }
}



