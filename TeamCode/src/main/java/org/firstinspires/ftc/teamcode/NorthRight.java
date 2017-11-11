package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Eric on 11/9/2017.
 */

@Autonomous
@Disabled
public class NorthRight extends LinearOpMode {
    PengwinArm pengwinArm;
    JeffThePengwin jeffThePengwin;
    private ElapsedTime runtime = new ElapsedTime();
    //
    static final double countsPerRevolution = 560;//TODO Add gear reduction if needed
    static final double diameter = 4;
    static final double Pi = 3.141592653589793238462643383279502;
    static final double countify = countsPerRevolution*diameter*Pi;//Counts per inch
    //
    ModernRoboticsI2cGyro lookify;
    //
    @Override
    public void runOpMode() throws InterruptedException {
        //
        startify();
        //For kids
        waitForStartify();
        //
        //woo hoo
        //
        //Insert Code Here
        forward(6, 4, .5);
    }
    //
    //
    //
    private void forward(double inches, double time, double speed){
        int move = (int)(Math.round(inches*countify));
        jeffThePengwin.leftBackMotor.setTargetPosition(jeffThePengwin.leftBackMotor.getCurrentPosition() + move);
        jeffThePengwin.leftFrontMotor.setTargetPosition(jeffThePengwin.leftFrontMotor.getCurrentPosition() + move);
        jeffThePengwin.rightBackMotor.setTargetPosition(jeffThePengwin.rightBackMotor.getCurrentPosition() + move);
        jeffThePengwin.rightFrontMotor.setTargetPosition(jeffThePengwin.rightFrontMotor.getCurrentPosition() + move);
        //
        switchify();//switch to rut to position
        //
        setDriveSpeed(speed);
        //
        runtime.reset();
        //
        while (opModeIsActive() &&  (runtime.seconds() < time)){
            telemetry.addData("Progress",runtime.seconds()/time + "%");
            telemetry.update();
        }
        telemetry.clearAll();
        //
        switcheroo();//switch to run using encoder
    }
    //
    private void back(double inches, double time, double speed){
        int move = (int)(Math.round(inches*countify));
        jeffThePengwin.leftBackMotor.setTargetPosition(jeffThePengwin.leftBackMotor.getCurrentPosition() + -move);
        jeffThePengwin.leftFrontMotor.setTargetPosition(jeffThePengwin.leftFrontMotor.getCurrentPosition() + -move);
        jeffThePengwin.rightBackMotor.setTargetPosition(jeffThePengwin.rightBackMotor.getCurrentPosition() + -move);
        jeffThePengwin.rightFrontMotor.setTargetPosition(jeffThePengwin.rightFrontMotor.getCurrentPosition() + -move);
        //
        switchify();//switch to rut to position
        //
        setDriveSpeed(speed);
        //
        runtime.reset();
        //
        while (opModeIsActive() &&  (runtime.seconds() < time)){
            telemetry.addData("Progress",runtime.seconds()/time + "%");
            telemetry.update();
        }
        telemetry.clearAll();
        //
        switcheroo();//switch to run using encoder
    }
    //
    private void right(double inches, double time, double speed){
        int move = (int)(Math.round(inches*countify));
        jeffThePengwin.leftBackMotor.setTargetPosition(jeffThePengwin.leftBackMotor.getCurrentPosition() + move);
        jeffThePengwin.leftFrontMotor.setTargetPosition(jeffThePengwin.leftFrontMotor.getCurrentPosition() + -move);
        jeffThePengwin.rightBackMotor.setTargetPosition(jeffThePengwin.rightBackMotor.getCurrentPosition() + -move);
        jeffThePengwin.rightFrontMotor.setTargetPosition(jeffThePengwin.rightFrontMotor.getCurrentPosition() + move);
        //
        switchify();//switch to rut to position
        //
        setDriveSpeed(speed);
        //
        runtime.reset();
        //
        while (opModeIsActive() &&  (runtime.seconds() < time)){
            telemetry.addData("Progress",runtime.seconds()/time + "%");
            telemetry.update();
        }
        telemetry.clearAll();
        //
        switcheroo();//switch to run using encoder
    }
    //
    private void left(double inches, double time, double speed){
        int move = (int)(Math.round(inches*countify));
        jeffThePengwin.leftBackMotor.setTargetPosition(jeffThePengwin.leftBackMotor.getCurrentPosition() + -move);
        jeffThePengwin.leftFrontMotor.setTargetPosition(jeffThePengwin.leftFrontMotor.getCurrentPosition() + move);
        jeffThePengwin.rightBackMotor.setTargetPosition(jeffThePengwin.rightBackMotor.getCurrentPosition() + move);
        jeffThePengwin.rightFrontMotor.setTargetPosition(jeffThePengwin.rightFrontMotor.getCurrentPosition() + -move);
        //
        switchify();//switch to rut to position
        //
        setDriveSpeed(speed);
        //
        runtime.reset();
        //
        while (opModeIsActive() &&  (runtime.seconds() < time)){
            telemetry.addData("Progress",runtime.seconds()/time + "%");
            telemetry.update();
        }
        telemetry.clearAll();
        //
        switcheroo();//switch to run using encoder
    }
    //
    private void waitForStartify(){
        waitForStart();
    }
    //
    private void startify(){
        jeffThePengwin.leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        jeffThePengwin.leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        jeffThePengwin.rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        jeffThePengwin.rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //
        jeffThePengwin.leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jeffThePengwin.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jeffThePengwin.rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jeffThePengwin.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    //
    private void switchify(){
        jeffThePengwin.leftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        jeffThePengwin.leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        jeffThePengwin.rightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        jeffThePengwin.rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    //
    private void switcheroo(){
        jeffThePengwin.leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jeffThePengwin.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jeffThePengwin.rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jeffThePengwin.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //
        jeffThePengwin.leftBackMotor.setPower(0);
        jeffThePengwin.leftFrontMotor.setPower(0);
        jeffThePengwin.rightBackMotor.setPower(0);
        jeffThePengwin.rightFrontMotor.setPower(0);
    }
    //
    private void setDriveSpeed(double speed){
        jeffThePengwin.leftBackMotor.setPower(speed);
        jeffThePengwin.leftFrontMotor.setPower(speed);
        jeffThePengwin.rightBackMotor.setPower(speed);
        jeffThePengwin.rightFrontMotor.setPower(speed);
    }
}