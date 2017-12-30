package org.firstinspires.ftc.teamcode.relicrecovery;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
/**
 * Created by thund on 10/19/2017.
 */
public class JeffThePengwin {
    //
    static final double countify = 116.501;//Counts per inch
    //power variables
    DcMotor leftFrontMotor;
    DcMotor rightFrontMotor;
    DcMotor leftBackMotor;
    DcMotor rightBackMotor;
/** WE NAMED THE MOTORS**/
    public void setPowerInput(double powerInput) {
        this.powerInput = powerInput;
    }
    /** We set the power**/
    public void setDegreeOfPower(double degreeOfPower) {
        this.degreeOfPower = degreeOfPower;
    }
/**We set the degree of power**/
    double powerInput = 0;
    double degreeOfPower = 1;
    DigitalChannel up;
    DigitalChannel touchy;
    //Make it blue!
    public JeffThePengwin(HardwareMap hardwareMap){
        /*get motors*/
        leftBackMotor = hardwareMap.dcMotor.get("lback"); //left back
        rightBackMotor = hardwareMap.dcMotor.get("rback"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("lfront"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("rfront"); //right front
        //
        rightBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //
        up = hardwareMap.digitalChannel.get("up");
        touchy = hardwareMap.digitalChannel.get("touchy");
        /*positive direction is down, negative direction is up*/
    }
    //
    public double getTheFrontWheelPower(){
        if(touchy.getState()){
            return powerInput*degreeOfPower;
        }else {
            return powerInput * degreeOfPower * .5;
        }
    }
    //
    public double getTheBackWheelPower(){
        if(touchy.getState()){
            return powerInput*degreeOfPower;
        }
        else{
            return (powerInput  *2.0)* degreeOfPower;
        }
    }
    //christine added, (PLEASE) explain!
    /**This comment is in memory of a wonderful moment in time where Christine found a "fatal" flaw
     *that caused some unnecessary and unfortunate complications that made the autonomous code not
     * precise. Due to her brave actions of reporting this error to the lead coders, extreme
     * catastrophe was avoided. The small paragraph you are now reading is to immortalize the
     * historic message that terminated the imperfection of movement during the autonomous code.
    *-Eric Patton, 2017  **/
     public boolean isMoving(){
        return leftBackMotor.isBusy() &&
                leftFrontMotor.isBusy() &&
                rightBackMotor.isBusy() &&
                rightFrontMotor.isBusy();
    }
    //
    public void forwardToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        moveAllMotorsSameDirectionAndDistance(move);
        //
        switchify();/*switch to rut to position*/
        //
        powerInput = speed;
        bestowThePowerToAllMotors();
    }
    //
    public void backToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        moveAllMotorsSameDirectionAndDistance(-move);
        driveBackward();
        //
        switchify();/*switch to rut to position*/
        //
        powerInput = speed;
        bestowThePowerToAllMotors();
        //
    }
    //
    public void rightToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + -move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + -move);

        //
        switchify();/*switch to rut to position*/
        //
        powerInput = speed;
        bestowThePowerToAllMotors();
        //
    }
    //
    public void turnRightToPostion(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + -move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + -move);

        //
        switchify();/*switch to run to position*/
        //
        powerInput = speed;
        bestowThePowerToAllMotors();
        //
    }
    //
    public void turnLeftToPosition (double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + -move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + -move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);

        //
        switchify();/*witch to rut to position*/
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

        switchify();/*switch to run to position*/
        //
        powerInput = speed;
        bestowThePowerToAllMotors();
        //
    }
    //
    public void bestowThePowerToAllMotors(){
        leftBackMotor.setPower(getTheBackWheelPower());
        leftFrontMotor.setPower(getTheFrontWheelPower());
        rightBackMotor.setPower(getTheBackWheelPower());
        rightFrontMotor.setPower(getTheFrontWheelPower());
    }
    //
    public void moveAllMotorsSameDirectionAndDistance(int move){
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
    }
    //
    public void turnRight(){
        leftBackMotor.setPower(powerInput*degreeOfPower);
        leftFrontMotor.setPower(powerInput*degreeOfPower);
        rightBackMotor.setPower(-powerInput*degreeOfPower);
        rightFrontMotor.setPower(-powerInput*degreeOfPower);
    }
    //
    public void turnLeft(){
        leftBackMotor.setPower(-powerInput*degreeOfPower);
        leftFrontMotor.setPower(-powerInput*degreeOfPower);
        rightBackMotor.setPower(powerInput*degreeOfPower);
        rightFrontMotor.setPower(powerInput*degreeOfPower);
    }
    //
    public void driveForward(){
        bestowThePowerToAllMotors();
    }
    //
    public void driveBackward(){
        leftBackMotor.setPower(-powerInput*degreeOfPower);
        leftFrontMotor.setPower(-powerInput*degreeOfPower);
        rightBackMotor.setPower(-powerInput*degreeOfPower);
        rightFrontMotor.setPower(-powerInput*degreeOfPower);
    }
    //
    public void strafeLeft(){
        if(touchy.getState()){
            leftBackMotor.setPower(-getTheBackWheelPower());
            leftFrontMotor.setPower(getTheFrontWheelPower());
            rightBackMotor.setPower(getTheBackWheelPower());
            rightFrontMotor.setPower(-getTheFrontWheelPower());
        }else{
            leftBackMotor.setPower(-getTheBackWheelPower());
            leftFrontMotor.setPower(getTheFrontWheelPower());
            rightBackMotor.setPower(getTheBackWheelPower());
            rightFrontMotor.setPower(-getTheFrontWheelPower());
        }
    }
    //
    public void strafeRight(){
        leftBackMotor.setPower(getTheBackWheelPower());
        leftFrontMotor.setPower(-getTheFrontWheelPower());
        rightBackMotor.setPower(-getTheBackWheelPower());
        rightFrontMotor.setPower(getTheFrontWheelPower());
    }
    //
    public void switchify(){
        //leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setMotorMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    //
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
        /*set motor inputs to zero*/
        powerInput = 0;
        bestowThePowerToAllMotors();
    }
    /**gives the motor power**/
    //
    private void setMotorMode(DcMotor.RunMode runnyMode){
        leftBackMotor.setMode(runnyMode);
        leftFrontMotor.setMode(runnyMode);
        rightBackMotor.setMode(runnyMode);
        rightFrontMotor.setMode(runnyMode);
    }
}