package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="calibrate the wrist", group="Testing")
public class test_servos3 extends LinearOpMode
{
    Bogg robot;

    Gamepad g1;

    double pinch = .44;
    double swing = 0;

    boolean activatePinch;
    boolean activateSwing;
    boolean activateDrop;
    boolean activateBrake;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        g1 = gamepad1;
        waitForStart();

        while (opModeIsActive())
        {
            telemetry.addLine(robot.name.toString());


            telemetry.addData("pinch", pinch);
            if(Math.abs(gamepad1.left_stick_y) > .9)
                activatePinch = true;
            if(activatePinch)
                robot.endEffector.pinch.setPosition(pinch);


            telemetry.addData("swing", swing);
            if(Math.abs(gamepad1.right_stick_y) > .9)
                activateSwing = true;
            if(activateSwing)
                robot.endEffector.swing.setPosition(swing);


            if(g1.y)
                activateDrop = true;
            if(activateDrop)
                robot.dropMarker(Bogg.Direction.Up);


            if(gamepad1.left_stick_button)
                activateBrake = true;
            if(activateBrake)
                robot.setBrake(Bogg.Direction.Off);


            sleep(50);
            robot.update();
            idle();
        }
    }
}

