package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Eric on 11/9/2017.
 */

@Autonomous(name="NorthRight", group="Autonomisisisisis")
public class NorthRight extends LinearOpMode {
    PengwinArm pengwinArm;
    PengwinFin pengwinFin;
    JeffThePengwin jeffThePengwin;
    private ElapsedTime runtime = new ElapsedTime();
    //
    static final double countsPerRevolution = 1220 ;//TODO Add gear reduction if needed
    static final double diameter = 4;
    static final double Pi = 3.141592653589793238462643383279502;
    static final double countify = countsPerRevolution/(diameter*Pi);//Counts per inch
    //
    ModernRoboticsI2cGyro lookify;
    //Push
    @Override
    public void runOpMode() throws InterruptedException {
        jeffThePengwin = new JeffThePengwin(hardwareMap);
        pengwinArm = new PengwinArm(hardwareMap);
        pengwinFin = new PengwinFin(hardwareMap);
        //
        startify();

        //For kids
        waitForStartify();
        //
        //woo hoo
        //
        //Insert Code Here
        forward(12, 15, .4);
    }
    //
    //
    //
    private void forward(double inches, double time, double speed){


        int move = (int)(Math.round(inches*countify));
        int leftFrontStart = jeffThePengwin.leftFrontMotor.getCurrentPosition();
        int leftFrontEnd = (leftFrontStart + move);
        int leftBackStart = jeffThePengwin.leftFrontMotor.getCurrentPosition();
        int leftBackEnd = (leftBackStart + move);
        int rightFrontStart = jeffThePengwin.leftFrontMotor.getCurrentPosition();
        int rightFrontEnd = rightFrontStart + move;
        int rightBackStart = jeffThePengwin.leftFrontMotor.getCurrentPosition();
        int rightBackEnd = rightBackStart + move;

        jeffThePengwin.leftBackMotor.setTargetPosition(leftBackEnd);
        jeffThePengwin.leftFrontMotor.setTargetPosition(leftFrontEnd);
        jeffThePengwin.rightBackMotor.setTargetPosition(rightBackEnd);
        jeffThePengwin.rightFrontMotor.setTargetPosition(rightFrontEnd);
        //
        switchify();//switch to rut to position

        //
        setDriveSpeed(speed);
        //
        runtime.reset();
        //
        while (opModeIsActive() &&
                (runtime.seconds() < time) &&
                (jeffThePengwin.isMoving())){
            telemetry.addData("endPosition", move);
            telemetry.addData("lbm position", jeffThePengwin.leftBackMotor.getCurrentPosition());
            telemetry.addData("lfm position", jeffThePengwin.leftFrontMotor.getCurrentPosition());
            telemetry.addData("rbm position", jeffThePengwin.rightBackMotor.getCurrentPosition());
            telemetry.addData("rfm position", jeffThePengwin.rightFrontMotor.getCurrentPosition());
            telemetry.addData("fin position", pengwinFin.fin.getPosition());
            telemetry.addData("Progress", runtime.seconds() / time + "%");
            telemetry.update();
        }
        telemetry.addData("Status",runtime.seconds()/time+"%");
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