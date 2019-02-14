package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="holonomicDrive Curvy", group="Testing")
public class holonomicDrive_0_2 extends LinearOpMode
{
    Bogg robot;

    private ElapsedTime timer;

    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    private boolean autoOverride = false;

    double pinch = .44;
    double driveAngle = Math.PI;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry, Bogg.Name.Bogg);
        robot.endEffector.contract.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addLine("ready");
        waitForStart();

        robot.endEffector.pivot.setPower(1);
        timer = new ElapsedTime();
        Gamepad g1 = gamepad1;
        Gamepad g2 = gamepad2;

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


            if(g1.left_bumper)
                robot.dropMarker(Bogg.Direction.Left);
            else if(g1.right_bumper)
                robot.dropMarker(Bogg.Direction.Right);

            if(g2.dpad_right){
                pinch += .1 * Bogg.averageClockTime;
                robot.endEffector.pinch.setPosition(pinch);
            }
            else if(g2.dpad_left) {
                pinch -= .1 * Bogg.averageClockTime;
                robot.endEffector.pinch.setPosition(pinch);
            }
            telemetry.addData("pinch", pinch);

            if(g2.dpad_up)
                robot.endEffector.pickleUp();
            if(g2.dpad_down)
                robot.endEffector.pickleDown();
            telemetry.addData("swing", robot.endEffector.swing.getPosition());

            //Lift
            robot.manualLift(g1.y, g1.a);

            //Drive angle
            if(g1.x)
                driveAngle = robot.sensors.getImuHeading();

            //Arm and drive automatic
            //When down
            if(g2.left_stick_button) {
                if(!leftButtonPressed) {
                    timer.reset();
                    leftButtonPressed = true;
                }
                if(timer.seconds() > 1)
                    autoOverride = !robot.driveEngine.moveOnPath(true, new double[]{Math.PI});

                robot.endEffector.flipUp(timer.seconds());
            }
            else
                leftButtonPressed = false;


            //When up
            if(g2.right_stick_button) {
                if(!rightButtonPressed) {
                    timer.reset();
                    rightButtonPressed = true;
                }
                if(timer.seconds() > .5)
                    autoOverride = !robot.driveEngine.moveOnPath(true, new double[]{Math.PI});

                robot.endEffector.flipDown(timer.seconds());
            }
            else
                rightButtonPressed = false;


            //Arm and drive manual
            if(!autoOverride) {
                if (!robot.manualRotate(true, g2.right_stick_x / 3))
                    if (!robot.manualRotate(true, -g2.left_stick_x / 3))
                        robot.manualCurvy(
                                g1.left_stick_button,
                                g1.left_stick_x,
                                g1.left_stick_y,
                                g1.right_stick_x);

                robot.driveEngine.driveAtAngle(MyMath.loopAngle(driveAngle, robot.sensors.getImuHeading()));

                //if the moveOnPaths have finished
                robot.driveEngine.checkpoints.clear();
                robot.driveEngine.resetForward();
            }

            if(!robot.endEffector.extend(-g2.left_stick_y)){
                if(!robot.endEffector.extend(g2.right_stick_y))
                    robot.endEffector.contract.setPower(0);
            }


            if(g2.y)
            {
                robot.endEffector.pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.endEffector.pivot.setPower(-1);
            }
            else if(g2.a)
            {
                robot.endEffector.pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.endEffector.pivot.setPower(1);
            }
            else
            {
                robot.endEffector.pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.endEffector.pivot.setPower(1);
            }

            //even if we never finish the turn, enable manual driving
            autoOverride = false;



            // Display the current values
            telemetry.addLine("'Pressing A must move the arm down/robot up.'");
            telemetry.addLine("Set brake: d-down. Remove brake: d-up.");
            robot.driveEngine.reportPositionsToScreen();

            telemetry.update();
            robot.update();
            idle();
        }
    }
}

