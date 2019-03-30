package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="holonomicDrive Competition", group="Competition")
public class holonomicDrive_0_2 extends LinearOpMode
{
    Bogg robot;

    private ElapsedTime timer;

    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    private boolean autoOverride = false;

    double driveAngle = Math.PI, initialAngle = Math.PI;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry, Bogg.Name.Bogg);
        telemetry.addLine("ready");
        waitForStart();

        if(robot.name == Bogg.Name.Bogg)
            robot.endEffector.pivot.setPower(1);
        timer = new ElapsedTime();
        Gamepad g1 = gamepad1;
        Gamepad g2 = gamepad2;

        Button rightButton = new Button(gamepad2, Button.Location.D_Right, Button.Type.Click);


        while (opModeIsActive())
        {
            if(g1.timestamp > g2.timestamp)
                telemetry.addLine("Using gamepad1");
            else if(g1.timestamp < g2.timestamp)
                telemetry.addLine("Using gamepad2");
            else
                telemetry.addLine("Know which gamepad you're using");


            //Servos
            if(g1.dpad_down)
                robot.setBrake(Bogg.Direction.On);
            else if(g1.dpad_up)
                robot.setBrake(Bogg.Direction.Off);


            if(g1.right_bumper)
                robot.dropMarker(Bogg.Direction.Down);
            else
                robot.dropMarker(Bogg.Direction.Up);


            if(robot.name == Bogg.Name.Bogg)
                telemetry.addData("swing", robot.endEffector.swing.getPosition());

            //Lift
            if(robot.manualLift(g1.y, g1.a))
                robot.driveEngine.stop();

            //Drive angle
            if(g1.x)
                driveAngle = initialAngle + robot.driveEngine.spinAngle();


            //Arm and drive manual
            robot.manualDrive2(
                    g1.left_stick_button,
                    g1.left_stick_x,
                    g1.left_stick_y,
                    g1.right_stick_x);

            robot.driveEngine.driveAtAngle(MyMath.loopAngle(driveAngle, robot.driveEngine.spinAngle()));

            //if the moveOnPaths have finished
            robot.driveEngine.checkpoints.clear();


            if(robot.name == Bogg.Name.Bogg)
                if(!robot.endEffector.extend(-g2.left_stick_y)){
                    if(!robot.endEffector.extend(g2.right_stick_y))
                        robot.endEffector.contract.setPower(0);
                }


            if(g1.back)
                break;

            // Display the current values
            telemetry.addLine("'Pressing A must move the arm down/robot up.'");
            telemetry.addLine("Set brake: d-down. Remove brake: d-up.");

            robot.update();
            idle();
        }
    }
}

