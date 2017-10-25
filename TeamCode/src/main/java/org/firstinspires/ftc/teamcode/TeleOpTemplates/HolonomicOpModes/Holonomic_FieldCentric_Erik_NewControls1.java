package org.firstinspires.ftc.teamcode.TeleOpTemplates.HolonomicOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
- Name: Holonomic Field-Centric Tele-Op
- Creator[s]: Erik
- Date Created: 10/23/17
- Objective: To drive our holonomic robot around in a field centric manner, which is easier for drivers
           since they don't have to keep track of the front of the robot.
- Controls: The left joystick controls translational movement, while the right joystick controls rotation.
          and holding both bumpers sends the robot into ultra-turbo mode, which enhances its speed.
- Sensor Usage: Our Adafruit IMU gyro sensor is used in this program for detecting the robot's heading,
                which is used to determine what direction the robot needs to go with respect to its front.
- Key Algorithms: Lines (x-y) contains the algorithm that converts joystick input into actual motor
                  values. First the rectangular vector obtained from the joystick values is converted
                  into a polar vector. Then the robot heading is subtracted to find the angle that the
                  robot must travel with respect to its front. This angle is run through some trigonometry
                  to find ratios for the motor values, which are then adjusted to match the desired motor power.
- Uniqueness: Although this is a fairly standard field centric drive, the added customizations to give
              the driver more options(A/start/bumpers) is what makes this program stand out.
- Possible Improvements: The main algorithm is a bit lengthy possibly causing some lag, so if needed
                         the joystick polar vector could be converted back to rectangular and plugged
                         into the motors like in the robot centric program instead of using all of the
                         trigonometry. Also pivoting could be added in if found to be useful, but
                         I have not yet thought of a practical use for it.
 */

@TeleOp(name = "Holonomic Field-Centric Tele-Op Test 1", group = "holonomic Erik")
public class Holonomic_FieldCentric_Erik_NewControls1 extends OpMode
{
    Holonomic_Hardware robot;
    double angleFromDriver = Math.PI/2;
    double jTheta;
    double jp;
    double theta;
    boolean robotCentric = false;
    boolean toggle = false;
    int dpad = 0;
    int z = 1;

    @Override
    public void init ()
    {
        robot = new Holonomic_Hardware(hardwareMap, telemetry, true);
    }

    @Override
    public void loop ()
    {
        if(gamepad1.right_bumper)
            robotCentric = true;
        else
            robotCentric = false;

        if(gamepad1.left_bumper && toggle == false)
        {
            robotCentric = true;
            toggle = true;
            robot.time.reset();
        }
        else if(gamepad1.left_bumper && toggle && robot.time.seconds() == 1)
        {
            robotCentric = false;
            toggle = true;
        }
        else if(gamepad1.right_bumper == false && toggle == false)
        {
            robotCentric = false;
        }

        //Makes it so it becomes robot centric based on the last heading before pressing the button
        if(robotCentric == false)
            robot.updateGyro();

        jTheta = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x);

        jp = Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x + gamepad1.left_stick_y * gamepad1.left_stick_y);

        if(jp > 1)
            jp = 1;

        theta = (jTheta + angleFromDriver - robot.heading);

        telemetry.addData("Ultra Turbo Mode Activated", gamepad1.right_bumper && gamepad1.left_bumper);
        telemetry.addData(" Right Joystick X Axis:", gamepad1.right_stick_x);
        telemetry.addData("Joystick Direction", Math.toDegrees(jTheta));
        telemetry.addData("Joystick Magnitude", jp);
        telemetry.addData("Gyro Heading", robot.heading);
        telemetry.addData("toggle", toggle);
        telemetry.addData("robotCentric", robotCentric);

        if(gamepad1.dpad_up)
            dpad = 1;
        else if(gamepad1.dpad_right)
            dpad = 2;
        else if(gamepad1.dpad_down)
            dpad = 3;
        else if(gamepad1.dpad_left)
            dpad = 4;
        else
            dpad = 0;

        switch(dpad)
        {
            case 0:
                robot.drive(
                        (Math.sin(theta)+Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                        (Math.sin(theta)-Math.cos(theta))*jp/2 + gamepad1.right_stick_x,
                        (Math.sin(theta)-Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                        (Math.sin(theta)+Math.cos(theta))*jp/2 + gamepad1.right_stick_x
                );
                break;
            case 1: // 0
                if(robot.heading > 0 && robot.heading < Math.PI)
                {
                    while (robot.heading < 2*Math.PI && robot.heading > Math.PI)
                    {
                        robot.drive(
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 - z
                        );
                    }
                    dpad = 0;
                }
                else if(robot.heading > Math.PI)
                {
                    while (robot.heading < Math.PI) {
                        robot.drive(
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 + z
                        );
                    }
                    dpad = 0;
                }
                else
                    dpad =0;
                break;
            case 2: //270
                if(robot.heading > Math.PI/2 && robot.heading < 3*Math.PI/2)
                {
                    while (robot.heading < 3*Math.PI/2)
                    {
                        robot.drive(
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 + z
                        );
                    }
                    dpad = 0;
                }
                else if(robot.heading > 3*Math.PI/2 || robot.heading < Math.PI/2)
                {
                    while (robot.heading > 3*Math.PI/2) {
                        robot.drive(
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 - z,
                                (Math.sin(theta) - Math.cos(theta)) * jp / 2 + z,
                                (Math.sin(theta) + Math.cos(theta)) * jp / 2 - z
                        );
                    }
                    dpad = 0;
                }
                else
                    dpad =0;
                break;
            default:
                robot.drive(
                        (Math.sin(theta)+Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                        (Math.sin(theta)-Math.cos(theta))*jp/2 + gamepad1.right_stick_x,
                        (Math.sin(theta)-Math.cos(theta))*jp/2 - gamepad1.right_stick_x,
                        (Math.sin(theta)+Math.cos(theta))*jp/2 + gamepad1.right_stick_x
                );
        }

    }
}