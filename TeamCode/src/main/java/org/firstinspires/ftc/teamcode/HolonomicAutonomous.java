package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


/**
 * Created by Swagster_Wagster on 9/29/17.
 */

@Autonomous(name = "Holonomic_Autonomous")
public class HolonomicAutonomous extends LinearOpMode {


    Autonomous_Functions af = new Autonomous_Functions();

    @Override
    public void runOpMode() throws InterruptedException {


        telemetry.addData("ACTION COMPLETED", "READY FOR BREACH");

        af.init(hardwareMap);

        af.moveMotorWithTime(Constants.speed_medium , 4000, Constants.forward);

        telemetry.addData("ACTION COMPLETED", "READY FOR ACTION");

        waitForStart();



    }

}
