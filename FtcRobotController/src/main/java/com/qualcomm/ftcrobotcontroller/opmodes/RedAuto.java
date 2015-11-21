package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// PushBotAuto
//


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import java.sql.Struct;

/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-01-06-01
 */
public class RedAuto extends OpMode

{
    //--------------------------------------------------------------------------
    //
    // MY AUTO
    //

    DcMotor front_left;
    DcMotor front_right;
    DcMotor back_left;
    DcMotor back_right;

    private static final String front_left_name = "front_left";
    private static final String front_right_name = "front_right";
    private static final String back_left_name = "back_left";
    private static final String back_right_name = "back_right";

    private static final double forward_power = 1.0;
    private static final double turn_power = 1.0;

    private static final double left_correction = 1.0;
    private static final double right_correction = 1.0;

    int step=0;
    int lastStep=0;




    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public RedAuto()

    {

    } // PushBotAuto

    @Override
    public void init() {

        front_left = hardwareMap.dcMotor.get(front_left_name);
        front_right = hardwareMap.dcMotor.get(front_right_name);
        back_left = hardwareMap.dcMotor.get(back_left_name);
        back_right = hardwareMap.dcMotor.get(back_right_name);

        //set servo positions later

        front_left.setDirection(DcMotor.Direction.REVERSE);
        back_left.setDirection(DcMotor.Direction.REVERSE);

        resetEncoders();

    }

    //--------------------------------------------------------------------------
    //
    // start
    //
    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     *
     * The system calls this member once when the OpMode is enabled.
     */
    @Override public void start ()
    {
        super.start();
        runToPosition();
    } // start

    @Override public void loop ()
    {
        if(lastStep != step){
            if(get_all_encoders()>10) {
                runToPosition();
                lastStep=step;
            }
        }
        else{
            switch(step){
                case 0:
                    if(run_motors_until(forward_power, forward_power, 4*2000)) step++;
                    break;
                case 1:
                    if(run_motors_until(forward_power, -forward_power, 4000)) step++;
                    telemetry.addData("Text", 0);
                    break;
                case 2:
                    if(run_motors_until(forward_power, forward_power, 10000)) step++;
                    break;
                case 3:
                    if(run_motors_until(turn_power, -turn_power, 1800)) step++;
                    break;
                case 4:
                    if(run_motors_until(forward_power, forward_power, 1800)) step+=2;
                    break;
                case 5:
                    //drop climbers code
                    step++;
                    break;
                case 6:
                    if(run_motors_until(-forward_power, -forward_power, 3600)) step++;
                    break;
                case 7:
                    if (run_motors_until(-turn_power, turn_power, 7200)) step++;
                    break;
                case 8:
                    if(run_motors_until(forward_power, forward_power, 20000)) step++;
                    break;
                case 9:
                    if(run_motors_until(turn_power, -turn_power, 3600)) step++;
                    break;
                case 10:
                    if(run_motors_until(forward_power, forward_power, 10000)) step++;
                    break;
                default:
                    stop_motors();
                    break;

            }
        }
        telemetry.addData("Stuff", get_all_encoders());
        telemetry.addData("Step", step);


    } // loop

    //supporting functions
    double get_all_encoders(){
        double ret=0;
        ret += Math.abs(front_left.getCurrentPosition()*left_correction);
        ret += Math.abs(front_right.getCurrentPosition()*right_correction);
        ret += Math.abs(back_left.getCurrentPosition()*left_correction);
        ret += Math.abs(back_right.getCurrentPosition()*right_correction);
        ret /= 4;
        return ret;
    }

    void run_motors(double lPower, double rPower){
        front_left.setPower(lPower);
        front_right.setPower(rPower);
        back_left.setPower(lPower);
        back_right.setPower(rPower);
    }

    void stop_motors(){
        run_motors(0, 0);
        resetEncoders();
    }

    boolean run_motors_until(double lPower, double rPower, double distance){
        run_motors(lPower, rPower);
        if(get_all_encoders() >= distance) {
            stop_motors();
            return true;
        }
        return false;
    }

    boolean turn_left_until(double power, double distance){
        run_motors(power, -power);
        if(get_all_encoders()>=distance) {
            stop_motors();
            return true;
        }
        else return false;
    }

    boolean turn_right_until(double power, double distance){
        run_motors(-power, power);
        if(get_all_encoders()>=distance){
            stop_motors();
            return true;
        }
        else return false;
    }

    void resetEncoders(){
        front_left.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        front_right.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        back_left.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        back_right.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    void runToPosition(){
        front_left.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        front_right.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        back_left.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        back_right.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        /*
        int falsePosition = 99999999;
        front_left.setTargetPosition(falsePosition);
        front_right.setTargetPosition(falsePosition);
        back_left.setTargetPosition(falsePosition);
        back_right.setTargetPosition(falsePosition);
        */
    }

} // PushBotAuto

