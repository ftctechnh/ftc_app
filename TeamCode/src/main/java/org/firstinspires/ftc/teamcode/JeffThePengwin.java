package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by thund on 10/19/2017.
 */

public class JeffThePengwin {
    //diameter stuff
    static final double WHEEL_DIAMETER_INCHEYS = 4.0;

    static final double countsPerRevolution = 1220 ;//TODO Add gear reduction if needed
    static final double Pi = 3.141592653589793238462643383279502;
    static final double countify = 116.501;//Counts per inch


    //power variables
    DcMotor leftFrontMotor;
    DcMotor rightFrontMotor;
    DcMotor leftBackMotor;
    DcMotor rightBackMotor;

    double powerInput = 0;
    double degreeOfPower = 1;
    DigitalChannel up;
    DigitalChannel touchy;


    public JeffThePengwin(HardwareMap hardwareMap){
        //get motors
        leftBackMotor = hardwareMap.dcMotor.get("lback"); //left back
        rightBackMotor = hardwareMap.dcMotor.get("rback"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("lfront"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("rfront"); //right front

        rightBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        up = hardwareMap.digitalChannel.get("up");
        touchy = hardwareMap.digitalChannel.get("touchy");
        //positive direction is down, negative direction is up
    }

    public double getTheFrontWheelPower(){
        return powerInput*degreeOfPower;
    }

    public double getTheBackWheelPower(){
        if(touchy.getState()){
            return (powerInput *1.2 )* degreeOfPower;
        }
        else{
            return (powerInput  *1.6)* degreeOfPower;
        }
    }
    //christine added, explain!
    public boolean isMoving(){
        return leftBackMotor.isBusy()&& leftFrontMotor.isBusy() && rightBackMotor.isBusy() && rightFrontMotor.isBusy();
    }



    public void forwardToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        moveAllMotorsSameDirectionAndDistance(move);
        //
        switchify();//switch to rut to position
        //
        powerInput = speed;
        bestowThePowerToAllMotors();
    }

    public void backToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        moveAllMotorsSameDirectionAndDistance(-move);
        driveBackward();
        //
        switchify();//switch to rut to position
        //
        powerInput = speed;
        bestowThePowerToAllMotors();
        //
    }
    public void rightToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + -move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + -move);

        //
        switchify();//switch to rut to position
        //
        powerInput = speed;
        bestowThePowerToAllMotors();
        //
    }

    public void turnRightToPostion(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + -move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + -move);

        //
        switchify();//switch to run to position
        //
        powerInput = speed;
        bestowThePowerToAllMotors();
        //
    }

    public void turnLeftToPosition (double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + -move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + -move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);

        //
        switchify();//switch to rut to position
        //
        powerInput = speed;
        bestowThePowerToAllMotors();
        //
    }
    //
    public void leftToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + -move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + -move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);

        switchify();//switch to run to position
        //
        powerInput = speed;
        bestowThePowerToAllMotors();
        //
    }
    public void bestowThePowerToAllMotors(){
        leftBackMotor.setPower(getTheBackWheelPower());
        leftFrontMotor.setPower(getTheFrontWheelPower());
        rightBackMotor.setPower(getTheBackWheelPower());
        rightFrontMotor.setPower(getTheFrontWheelPower());
    }

    public void moveAllMotorsSameDirectionAndDistance(int move){
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
    }


    public void turnRight(){
        leftBackMotor.setPower(powerInput*degreeOfPower);
        leftFrontMotor.setPower(powerInput*degreeOfPower);
        rightBackMotor.setPower(-powerInput*degreeOfPower);
        rightFrontMotor.setPower(-powerInput*degreeOfPower);
    }

    public void turnLeft(){
        leftBackMotor.setPower(-powerInput*degreeOfPower);
        leftFrontMotor.setPower(-powerInput*degreeOfPower);
        rightBackMotor.setPower(powerInput*degreeOfPower);
        rightFrontMotor.setPower(powerInput*degreeOfPower);
    }

    public void driveForward(){
        bestowThePowerToAllMotors();
    }


    public void driveBackward(){
        leftBackMotor.setPower(-powerInput*degreeOfPower);
        leftFrontMotor.setPower(-powerInput*degreeOfPower);
        rightBackMotor.setPower(-powerInput*degreeOfPower);
        rightFrontMotor.setPower(-powerInput*degreeOfPower);
    }

    public void strafeLeft(){
        leftBackMotor.setPower(-getTheBackWheelPower());
        leftFrontMotor.setPower(getTheFrontWheelPower());
        rightBackMotor.setPower(getTheBackWheelPower());
        rightFrontMotor.setPower(-getTheFrontWheelPower());
    }

    public void strafeRight(){
        leftBackMotor.setPower(getTheBackWheelPower());
        leftFrontMotor.setPower(-getTheFrontWheelPower());
        rightBackMotor.setPower(-getTheBackWheelPower());
        rightFrontMotor.setPower(getTheFrontWheelPower());
    }

    public void switchify(){
        //leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setMotorMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void startify(){
        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    //

    //
    public void switcheroo(){
        //
        // 30setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //
        powerInput = 0;
        bestowThePowerToAllMotors();
    }

    private void setMotorMode(DcMotor.RunMode runnyMode){
        leftBackMotor.setMode(runnyMode);
        leftFrontMotor.setMode(runnyMode);
        rightBackMotor.setMode(runnyMode);
        rightFrontMotor.setMode(runnyMode);
    }
}
