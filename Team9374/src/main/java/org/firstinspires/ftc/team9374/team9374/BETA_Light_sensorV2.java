package org.firstinspires.ftc.team9374.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team9374.team9374.Hardware9374;

/**
 * Created by darwin on 11/29/16.
 *
 * 2nd version of the light sensor, has yet to e implememted into the nink main bot A
 */

//  must be set up such that center of the robot is aligned with the square directly to the left.
@Autonomous(name = "Beacon_AutoV2", group = "null")
public class BETA_Light_sensorV2 extends LinearOpMode {
    //Defining robot
    Hardware9374 robot = new Hardware9374();

    public void runOpMode() throws InterruptedException {
        //Initing robot
        robot.init(hardwareMap);
        super.waitForStart();

        robot.moveToPosition(61,.4);
        robot.Turn(90,.4,false);
        // What we have got next is a custom move command.
        // We are going forward untill we see both colors greather than 3
        // This will make shure we are in the middle.

        robot.setALLpower(.75);
        while (true){
            if (robot.CSensor.red() > 3 && robot.CSensor.blue() > 3){
                // If we get here than we know that we are in the center
                robot.resetEncoders();  //Standard, reset the encoders and stop the motors
                robot.setALLpower(0);
                break;
            }
        }
        //Next step, translating our robot to the right or left.
        //That has not been finished yet.
        //So im moving on, when it is finished put it here

        if (robot.CSensor.red() > robot.CSensor.blue()){
            // ( If red is on the left side. )
            // Translate to the right, back the exact same distance.

            robot.translate(true,.4,2);

            robot.resetEncoders();
            robot.setALLposition(robot.tpr);
            robot.left_b.setPower(1);
            robot.left_f.setPower(1);
            //We just pushed the button!!!

        } else if (robot.CSensor.blue() > robot.CSensor.red()){
            // ( If blue is on the left side. )
            // Translate to the right, back the exact same distance.

        }

    }

}