package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.RobotConfigNameable;
import com.qualcomm.robotcore.util.ElapsedTime;


//@Disabled
@Autonomous(name="RedFieldFacingDepot")
public class RedFieldFacingDepot extends LinearOpMode
{
    NewHardware robot = new NewHardware();
    private ElapsedTime runtime = new ElapsedTime();

    static final double FORWARD_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    static final double LIFT_SPEED = 0.3;
    static final double DRIVE_GEAR_REDUCTION = 1.333;
    ;
    static final double COUNTS_PER_MOTOR_REV = 1680;
    static final double COUNTS_FOR_LIFT = (COUNTS_PER_MOTOR_REV);
    static final double WHEEL_DIAMETER_INCHES = 4;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV) / (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double WIDTH_INCHES = 19;
    float hsvValues[] = {0F,0F,0F};
    final float values[] = hsvValues;


    @Override
    public void runOpMode()
    {
        robot.Initialize(hardwareMap);
        telemetry.addData("Status","Starting");
        telemetry.update();

        //robot.liftBot2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.liftBot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        //robot.liftBot2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //robot.liftBot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.liftBot2.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.liftBot.setDirection(DcMotorSimple.Direction.REVERSE);

        robot.leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        telemetry.addData("mode","waiting");
        telemetry.update();


        waitForStart();
        telemetry.addData("mode","running");
        telemetry.update();


        moveForward(55);
        robot.totemServo.setPosition(70);
        sleep(1000);
        encoderDrive(FORWARD_SPEED,-30,-30,5);
        turnClockwise(200);
        moveForward(62);

    }

    public void encoderDrive(double speed, double leftRotation, double rightRotation, double timeoutS){
        int newLeftTarget;
        int newRightTarget;
        if(opModeIsActive()) {
            newLeftTarget = robot.leftDrive.getCurrentPosition() + (int)(leftRotation * COUNTS_PER_INCH);
            newRightTarget = robot.rightDrive.getCurrentPosition() + (int)(rightRotation * COUNTS_PER_INCH);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            robot.leftDrive.setPower(Math.abs(speed));
            robot.rightDrive.setPower(Math.abs(speed));


            while (opModeIsActive() && (robot.leftDrive.isBusy() || robot.rightDrive.isBusy()) && runtime.seconds() < timeoutS) {

            }

            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);

            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    public void encoderLift(double speed, double leftInches, double rightInches, double timeoutS){
        int leftTarget;
        int rightTarget;
        if (opModeIsActive()) {
            leftTarget = robot.liftBot.getCurrentPosition() + (int)(leftInches);
            rightTarget = robot.liftBot2.getCurrentPosition() + (int)(rightInches);
            robot.liftBot2.setTargetPosition(leftTarget);
            robot.liftBot.setTargetPosition(rightTarget);

            robot.liftBot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.liftBot2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            robot.liftBot2.setPower(Math.abs(speed));
            robot.liftBot.setPower(Math.abs(speed));

            while (opModeIsActive() && robot.liftBot.isBusy() && robot.liftBot2.isBusy() && runtime.seconds() < timeoutS){

            }
            robot.liftBot2.setPower(0);
            robot.liftBot.setPower(0);

            robot.liftBot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.liftBot2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    public void moveForward(double Inches)
    {
        encoderDrive(FORWARD_SPEED, Inches, Inches, Inches);
    }
    public void turnClockwise(double degrees)
    {
        double distance = WIDTH_INCHES * Math.PI * degrees / 360;
        encoderDrive(FORWARD_SPEED, -distance, distance, distance);
    }
    public void turnCounterClockwise(double degrees)
    {
        double distance = WIDTH_INCHES * Math.PI * degrees / 360;
        encoderDrive(FORWARD_SPEED, distance, -distance, distance);
    }
    /*public void mineralID()
    {
        NormalizedRGBA colors = robot.sensor.getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsvValues);
        telemetry.addLine()
                .addData("H", "%.3f", hsvValues[0])
                .addData("S", "%.3f", hsvValues[1])
                .addData("V", "%.3f", hsvValues[2]);
        telemetry.addLine()
                .addData("a", "%.3f", colors.alpha)
                .addData("r", "%.3f", colors.red)
                .addData("g", "%.3f", colors.green)
                .addData("b", "%.3f", colors.blue);
        telemetry.update();

        double red = colors.red * 255;
        double green = colors.green * 255;
        double blue = colors.blue * 255;
        runtime.reset();
        while(runtime.seconds() < 10) {
            if (red > 2 && red < 5 && green > 1 && green < 3 && blue > 0.5 && blue < 2) {
                robot.mineralCollect.setPower(0.3);
            }else{
                robot.mineralCollect.setPower(-0.3);
            }
        }
    }*/



}