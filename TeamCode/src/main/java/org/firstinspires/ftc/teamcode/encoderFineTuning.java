package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

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

        robot.driveEngine.checkpoint.add(false);

        while (opModeIsActive())
        {
            deltaX += Math.round(robot.gamepad.left_stick_x * 10) /20;
            deltaY += Math.round(robot.gamepad.left_stick_y * 10) /20;
            deltaS += Math.round(robot.gamepad.right_stick_x * 10) * Math.PI/100;

            telemetry.addData("wheel diameter", DriveEngine.wheelDiameter);
            telemetry.addData("robot radius", DriveEngine.robotRadius);

            telemetry.addData("deltaX", deltaX);
            telemetry.addData("deltaY", deltaY);
            telemetry.addData("deltaS", deltaS);

            telemetry.addData("x inches", robot.driveEngine.xDist());
            telemetry.addData("y inches", robot.driveEngine.yDist());
            telemetry.addData("spin angle", robot.driveEngine.spinAngle());
            telemetry.addData("spin angle divided by pi", robot.driveEngine.spinAngle() / Math.PI);

            if(deltaS != 0)
            {
                robot.driveEngine.moveOnPath(new double[]{deltaS});
            }
            else
            {
                robot.driveEngine.moveOnPath(new double[]{deltaX, deltaY});
            }

            if(robot.gamepad.left_bumper || robot.gamepad.right_bumper)
            {
                robot.driveEngine.resetDistances();
                deltaX = deltaY = deltaS = 0;
            }

            if(robot.gamepad.dpad_up){
                DriveEngine.wheelDiameter += .01;
            }
            else if(robot.gamepad.dpad_down){
                DriveEngine.wheelDiameter -= .01;
            }
            else if(robot.gamepad.dpad_right){
                savedDiameter = DriveEngine.wheelDiameter;
            }
            else if(robot.gamepad.dpad_left){
                DriveEngine.wheelDiameter = savedDiameter;
            }

            if(robot.gamepad.y){
                DriveEngine.robotRadius += .01;
            }
            else if(robot.gamepad.a){
                DriveEngine.robotRadius -= .01;
            }
            else if(robot.gamepad.b){
                savedRadius = DriveEngine.robotRadius;
            }
            else if(robot.gamepad.x) {
                DriveEngine.robotRadius = savedRadius;
            }

            telemetry.update();
            idle();
        }
    }
}

