package org.firstinspires.ftc.teamcode.Year_2017_18.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Year_2017_18.Robot.RobotCommands;

// Motor: NeveRest 40 each tick is 0.01682996064 of an inch.
//Motor: NeveRest 40 each 1120 ticks is 18.8495559168 in.
// Servos: Deluxe HItec HS-485HB

/*
 *                 README: Commands
 *--------------------------------------------------
 * //For driving.
 * myRobot.drive(leftSpeed, rightSpeed, timeInMilliseconds);
 *
 * //For the main claws.
 * myRobot.pulley(Speed, timeInMilliseconds);
 * myRobot.claw_grab();
 * myRobot.claw_release();
 *
 * //For the sensor claw.
 * myRobot.sensor_left();
 * myRobot.sensor_right();
 */

@Autonomous(name = "AutoRedRight", group = "AutoMode")
@Disabled

public class AutoRedRight extends LinearOpMode {
    RobotCommands myRobot = new RobotCommands();

    @Override
    public void runOpMode() throws InterruptedException {
        myRobot.setHardwareMap(hardwareMap);

        telemetry.addData("Status", "The robot has successfully initialized! ");
        telemetry.update();

        waitForStart();
        telemetry.addData("Status", "The robot has started running!");

        myRobot.claw_grab();  //Keeps a firm grip on the glyph.
        sleep(100);
        myRobot.color_rotate(0.6); //Sets the Rotation of the color arm to the center.
        sleep (100);
        myRobot.sensor_left(); //Raise the sensor arm down.
        sleep(5000);

        if (myRobot.hardware.colorSensor.blue() > myRobot.hardware.colorSensor.red()) {           //What to do if the jewel is blue.
            myRobot.color_rotate(0); //Knocks ball
            sleep(300);
            myRobot.sensor_middle(); //Brings arm up.
            sleep(1000);
            myRobot.color_rotate(0.75); //Rotates arm.
            sleep(100);
            myRobot.sensor_right(); //Brings it to rest.
            sleep(5000);
        }
        else if (myRobot.hardware.colorSensor.red() > myRobot.hardware.colorSensor.blue()) {      //What to do if the jewel is red.
            myRobot.color_rotate(1);
            sleep(300);
            myRobot.sensor_middle();
            sleep(1000);
            myRobot.color_rotate(0.75);
            sleep(100);
            myRobot.sensor_right();
            sleep(5000);
        }

        myRobot.drive(-0.05, -0.05, 1800);
        sleep(100);
        myRobot.drive(0.1, 0.1, 500); //Moves a bit forward to score the block.
    }
}