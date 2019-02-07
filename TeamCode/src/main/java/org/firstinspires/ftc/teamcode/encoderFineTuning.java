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


            telemetry.addData("deltaX", deltaX);
            telemetry.addData("deltaY", deltaY);
            telemetry.addData("deltaS", deltaS);

            telemetry.addData("P", robot.driveEngine.mP);
            telemetry.addData("D", robot.driveEngine.mD);

            robot.driveEngine.moveOnPath(true, false, new double[]{deltaX, deltaY, deltaS});


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

            if(gamepad1.start || gamepad1.left_stick_button || gamepad1.right_stick_button)
            {
                deltaX = deltaY = deltaS = 0;
            }

            if(gamepad1.back){
                robot.driveEngine.stop();
                break;
            }

            telemetry.update();
            robot.update();
            idle();
        }
    }

}

