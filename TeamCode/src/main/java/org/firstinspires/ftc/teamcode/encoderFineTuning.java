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
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        waitForStart();
        double deltaX = 0;
        double deltaY = 0;
        double deltaS = 0;

        double savedRadius = DriveEngine.robotRadius;
        double savedDiameter = DriveEngine.wheelDiameter;


        while (opModeIsActive())
        {
            deltaX += Math.round(gamepad1.left_stick_x * 10) /20;
            deltaY += Math.round(gamepad1.left_stick_y * 10) /20;
            deltaS += Math.round(gamepad1.right_stick_x * 10) * Math.PI/200;

            telemetry.addData("wheel diameter", DriveEngine.wheelDiameter);
            telemetry.addData("robot radius", DriveEngine.robotRadius);

            telemetry.addData("deltaX", deltaX);
            telemetry.addData("deltaY", deltaY);
            telemetry.addData("deltaS", deltaS);

            telemetry.addData("x inches", robot.driveEngine.xDist());
            telemetry.addData("y inches", robot.driveEngine.yDist());
            telemetry.addData("spin angle", robot.driveEngine.spinAngle());
            telemetry.addData("spin angle divided by pi", robot.driveEngine.spinAngle() / Math.PI);

            robot.driveEngine.moveOnPath(true, new double[]{deltaX, deltaY, deltaS});

            if(gamepad1.left_bumper || gamepad1.right_bumper)
            {
                robot.driveEngine.resetDistances();
                deltaX = deltaY = deltaS = 0;
            }

            if(gamepad1.dpad_up){
                DriveEngine.wheelDiameter += .01;
            }
            else if(gamepad1.dpad_down){
                DriveEngine.wheelDiameter -= .01;
            }
            else if(gamepad1.dpad_right){
                savedDiameter = DriveEngine.wheelDiameter;
            }
            else if(gamepad1.dpad_left){
                DriveEngine.wheelDiameter = savedDiameter;
            }

            if(gamepad1.y){
                DriveEngine.robotRadius += .01;
            }
            else if(gamepad1.a){
                DriveEngine.robotRadius -= .01;
            }
            else if(gamepad1.b){
                savedRadius = DriveEngine.robotRadius;
            }
            else if(gamepad1.x) {
                DriveEngine.robotRadius = savedRadius;
            }

            telemetry.update();
            idle();
        }
    }
}

