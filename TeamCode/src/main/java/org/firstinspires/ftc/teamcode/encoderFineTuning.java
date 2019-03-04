package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="  encoderFineTuning", group="Testing")
public class encoderFineTuning extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        Button a = new Button(gamepad1, Button.Location.A, Button.Type.Increment);
        Button y = new Button(gamepad1, Button.Location.Y, Button.Type.Increment);

        waitForStart();
        double deltaX = 0;
        double deltaY = 0;
        double deltaS = 0;


        while (opModeIsActive())
        {
            if(Math.abs(gamepad1.left_stick_x) > .5){
                deltaX = 36 * Math.signum(gamepad1.left_stick_x);
                deltaY = 0;
                deltaS = 0;
            }
            if(Math.abs(gamepad1.left_stick_y) > .5){
                deltaX = 0;
                deltaY = 36 * Math.signum(gamepad1.left_stick_y);
                deltaS = 0;
            }
            if(gamepad1.right_stick_x > .5){
                deltaX = 0;
                deltaY = 0;
                deltaS = Math.PI;
            }

            if(a.count() > 0)
            {
                double distance = (double)a.count() - (double)y.count() / 4;
                double ticks = 0;
                if(Math.abs(deltaX) > 0) {
                    double backTicks = robot.driveEngine.motors.get(0).getCurrentPosition();
                    double rightTicks = robot.driveEngine.motors.get(1).getCurrentPosition();
                    double leftTicks = robot.driveEngine.motors.get(2).getCurrentPosition();
                    ticks = backTicks - 2 * rightTicks - 2 * leftTicks;
                }
                else if(Math.abs(deltaY) > 0)
                {
                    double rightTicks = robot.driveEngine.motors.get(1).getCurrentPosition();
                    double leftTicks = robot.driveEngine.motors.get(2).getCurrentPosition();
                    ticks = 2 / Math.sqrt(3)* (rightTicks - leftTicks);
                }
                telemetry.addData("Distance Ticks", ticks);
                telemetry.addData("gamepad Distance", distance);

                if(gamepad1.start)
                {
                    DriveEngine.setEffectiveWheelDiameter(distance, ticks);
                }
            }


            telemetry.addData("deltaX", deltaX);
            telemetry.addData("deltaY", deltaY);
            telemetry.addData("deltaS", deltaS);

            telemetry.addData("P", robot.driveEngine.mP);
            telemetry.addData("D", robot.driveEngine.mD);

            robot.driveEngine.moveOnPath(true, true, new double[]{deltaX, deltaY, deltaS});



            if(gamepad1.dpad_right)
            {
                robot.driveEngine.mP += .005 * Bogg.averageClockTime;
            }
            else if(gamepad1.dpad_left)
            {
                robot.driveEngine.mP -= .005 * Bogg.averageClockTime;
            }

            if(gamepad1.b)
            {
                robot.driveEngine.mD += .2 * Bogg.averageClockTime;
            }
            else if(gamepad1.x)
            {
                robot.driveEngine.mD -= .2 * Bogg.averageClockTime;
            }

            if(gamepad1.left_stick_button || gamepad1.right_stick_button)
            {
                deltaX = deltaY = deltaS = 0;
            }

            if(gamepad1.back){
                robot.driveEngine.stop();
                break;
            }

            robot.update();
            idle();
        }
    }

}

