package org.firstinspires.ftc.teamcode.boggiewheel_base.hardware.devices.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.framework.AbstractOpMode;
import org.firstinspires.ftc.teamcode.framework.SubsystemController;
import org.firstinspires.ftc.teamcode.framework.userHardware.PIDController;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.Logger;

import java.text.DecimalFormat;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static org.firstinspires.ftc.teamcode.framework.AbstractOpMode.isOpModeActive;

public class DriveController extends SubsystemController {

    private Drive drive;

    private PIDController drivePID;

    private PIDController straightPID;

    private double motorPosition, baseHeading = 0;

    private double turnY=0, turn_z=0, leftPower=0, rightPower=0, Drive_Power = 1.0;
    private boolean myTestDriveButtonInProgress = false;

    public ElapsedTime runtime;

    private DecimalFormat DF;

    private Logger logger;

    //Utility Methods
    public DriveController(){
        init();
    }

    public void init(){

        runtime = new ElapsedTime();

        opModeSetup();

        DF = new DecimalFormat("#.##");

        //Put general setup here
        drive = new Drive(hwMap);
        //drivePID = new PIDController(10,1.6,24, 2);
        drivePID = new PIDController(8,0.5,10, 1,0);
        straightPID = new PIDController(0.8,0.1,0,2);
        drive.setSlewSpeed(0.1);

        logger = new Logger("DriveControllerLog.txt");
    }

    public void stop(){
        drive.stop();

        logger.stop();
    }

    //Autonomous Methods
    public void turnTo(double angle, double speed, double error, int period){
        AbstractOpMode.delay(100);

        baseHeading = angle;

        telemetry.addData("starting turn segment---------------------------");
        drivePID.reset();
        drivePID.setMinimumOutput(0);
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double power;
        while(isOpModeActive()) {
            //While we are not in the error band keep turning
            while ( !atPosition(angle,drive.getHeading(),error) && isOpModeActive()) {
                telemetry.addDataDB("--------------------");
                //Use the PIDController class to calculate power values for the wheels
                if(angle-getHeading()>180) {
                    power = drivePID.output(angle, 180+(180+getHeading()));
                    telemetry.addData("How Far", 180+(180+getHeading()));
                }
                else {
                    power = drivePID.output(angle, getHeading());
                    telemetry.addData("How Far", getHeading());
                }
                setPower(-power*speed, power*speed);
                telemetry.addData("Heading", getHeading());
                telemetry.addData("Power", power);
                telemetry.update();
            }
            runtime.reset();
            while (runtime.milliseconds()<period){
                if((abs(getHeading() - angle)) > error && (abs(getHeading() + angle)) > error) break;
            }
            if((abs(getHeading() - angle)) > error && (abs(getHeading() + angle)) > error) continue;
            telemetry.addData("Final heading", getHeading());
            telemetry.update();
            drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            return;
        }
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void driveTo(double distance, double speed){
        AbstractOpMode.delay(100);

        telemetry.addData("starting drive segment---------------------------");
        drivePID.reset(); //Resets the PID values in the PID class to make sure we do not have any left over values from the last segment
        straightPID.reset();
        drivePID.setMinimumOutput(0);
        int position = (int)(distance * 50.8); //
        telemetry.addData("Encoder counts: " + position);
        double turn;
        speed = range(speed);
        telemetry.addData("Speed: " + speed);
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        drive.setPosisionP(5);
        double startHeading = baseHeading;
        telemetry.addData("Start Heading: " + startHeading);
        telemetry.update();
        double leftPower, rightPower;
        double power;
        while ((!atPosition(position, drive.getLeftPosition(), 15 ) && !atPosition(position, drive.getRightPosition(), 15)) && isOpModeActive()) {
            power = range(straightPID.output(position, drive.getRightPosition()));

            turn = drivePID.output(startHeading, getHeading());

            if(power>0) {
                leftPower = range(power * (speed - turn));
                rightPower = range(power * (speed + turn));
            } else {
                leftPower = range(power * (speed + turn));
                rightPower = range(power * (speed - turn));
            }

            drive.setPower(leftPower, rightPower);

            telemetry.addData("Encoder counts: " + position);
            telemetry.addData("Left Position: " + drive.getLeftPosition());
            telemetry.addData("Right Position: " + drive.getRightPosition());
            telemetry.addData("Left Power: " + leftPower);
            telemetry.addData("Right Power: " + rightPower);
            telemetry.addData("Heading: " + getHeading());
            telemetry.addData("PID Output: " + turn);
            telemetry.update();
        }

        for (int i=0; i<5; i++) {
            power = range(straightPID.output(position, (drive.getRightPosition() + drive.getLeftPosition())/2));

            turn = drivePID.output(startHeading, getHeading());

            if(power>0) {
                leftPower = range(power * (speed - turn));
                rightPower = range(power * (speed + turn));
            } else {
                leftPower = range(power * (speed + turn));
                rightPower = range(power * (speed - turn));
            }

            drive.setPower(leftPower, rightPower);

            telemetry.addData("Encoder counts: " + position);
            telemetry.addData("Left Position: " + drive.getLeftPosition());
            telemetry.addData("Right Position: " + drive.getRightPosition());
            telemetry.addData("Left Power: " + leftPower);
            telemetry.addData("Right Power: " + rightPower);
            telemetry.addData("Heading: " + getHeading());
            telemetry.update();
        }

        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setPosition(int position, double power){
        this.motorPosition = position;
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        drive.setPower(range(power),range(power));
        drive.setPosition(position);
    }

    public double getHeading(){
        return drive.getHeading();
    }

    public void resetAngleToZero() {
        drive.resetAngleToZero();
    }

    //TeleOp Methods
    public void setPower(double left, double right){
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        drive.setPower(range(left),range(right));
    }

    public void setY(double y){
        turnY = y;
        turnY = (float) scaleInput(turnY);
    }

    public void setZ(double z){
        turn_z = z;
        turn_z = (float) scaleInput(turn_z);
    }

    public void update(){
        //if button test is running don't drive robot
        if(myTestDriveButtonInProgress) {
            return;
        }
        leftPower = range((turnY + turn_z) * Drive_Power);
        rightPower = range((turnY - turn_z) * Drive_Power);
        telemetry.addData("turnY",DF.format(turnY));
        telemetry.addData("turn_z",DF.format(turn_z));
        telemetry.addData("Drive_Power",Drive_Power);

        telemetry.addData("left",DF.format(leftPower));
        telemetry.addData("right",DF.format(rightPower));
        drive.setPower(leftPower, rightPower);
    }

    //util methods
    public int[][] recordPath(int numSamples, int timeInterval) {
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int[][] values = new int[2][numSamples];
        runtime.reset();
        for (int i = 0; i < numSamples; i++) {
            while (runtime.milliseconds() < timeInterval && isOpModeActive()) ;
            values[0][i] = drive.getLeftPosition();
            values[1][i] = drive.getRightPosition();
            if(!isOpModeActive()) break;
            runtime.reset();
        }
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return values;
    }

    public void runPath(int[] left, int[] right, int timeInterval) {
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        drive.setPower(1,1);
        runtime.reset();
        for (int i = 0; i < left.length; i++) {
            while (runtime.milliseconds() < timeInterval);
            drive.setPosition(left[i], right[i]);
            runtime.reset();
        }
    }

    private double scaleInput(double val) {
        return range(pow(val, 3));
    }

    private double range(double val){
        if(val<-1) val=-1;
        if(val>1) val=1;
        return val;
    }

    private boolean atPosition(double x, double y, double error) {
        double upperRange = x + error;
        double lowerRange = x - error;

        return y >= lowerRange && y <= upperRange;
    }
}