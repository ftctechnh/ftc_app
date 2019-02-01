package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="holonomicDrive Curvy", group="Testing")
public class holonomicDrive_0_2 extends LinearOpMode
{
    Bogg robot;

    private ElapsedTime timer;

    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        waitForStart();

        timer = new ElapsedTime();
        Gamepad g1 = gamepad1;
        Gamepad g2 = gamepad2;

        while (opModeIsActive())
        {
            telemetry.addData("touchLander", robot.sensors.touchLanderIsPressed());
            if(!robot.manualRotate(true, -g2.right_stick_x / 3))
                if(!robot.manualRotate(true, g2.left_stick_x /3))
                    robot.manualCurvy(
                            g1.left_stick_button,
                            g1.left_stick_x,
                            robot.sensors.touchLanderIsPressed()? 0 : g1.left_stick_y,
                            g1.right_stick_x);

            if(g1.dpad_down)
                robot.setBrake(Bogg.Direction.On);
            else if(g1.dpad_up)
                robot.setBrake(Bogg.Direction.Off);


            if(g1.left_bumper)
                robot.dropMarker(Bogg.Direction.Left);
            else if(g1.right_bumper)
                robot.dropMarker(Bogg.Direction.Right);

            robot.manualLift(g1.y, g1.a);

            //When down
            if(g2.left_stick_button) {
                if(!leftButtonPressed) {
                    timer.reset();
                    leftButtonPressed = true;
                }
                robot.endEffector.flipUp(timer.seconds());
            }
            else
                leftButtonPressed = false;

            if(!robot.endEffector.extend(-g2.left_stick_y * 4));
                robot.endEffector.extend(g2.right_stick_y * 3);

            //When up
            if(g2.right_stick_button) {
                if(!rightButtonPressed) {
                    timer.reset();
                    rightButtonPressed = true;
                }
                robot.endEffector.flipDown(timer.seconds());
            }
            else
                rightButtonPressed = false;




            // Display the current value
            telemetry.addLine("'Pressing A must move the arm down/robot up.'");
            telemetry.addLine("Set brake: d-down. Remove brake: d-up.");
            robot.driveEngine.reportPositionsToScreen();
            telemetry.addData("touchBottom", robot.sensors.touchBottomIsPressed());
            telemetry.addData("touchTop", robot.sensors.touchTopIsPressed());

            telemetry.update();
            robot.update();
            idle();
        }
    }
}

