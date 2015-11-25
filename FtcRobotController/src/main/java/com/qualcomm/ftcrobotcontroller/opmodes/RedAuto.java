package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// PushBotAuto
//


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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

        front_right.setDirection(DcMotor.Direction.REVERSE);
        back_right.setDirection(DcMotor.Direction.REVERSE);

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
    } // start

    @Override public void loop ()
    {
            switch(step){
                case 0:
                    run_motors(forward_power, forward_power);
                    break;
                case 1:
                    bSleep(2000);
                    break;
                case 2:
                    run_motors(forward_power, -forward_power);
                    break;
                case 3:
                    bSleep(1000);
                    break;
                case 4:
                    run_motors(forward_power, forward_power);
                    break;
                case 5:
                    bSleep(5000);
                    break;
                case 6:
                    run_motors(forward_power, -forward_power);
                    break;
                case 7:
                    bSleep(1800);
                    break;
                case 8:
                    run_motors(forward_power, forward_power);
                    break;
                case 9:
                    bSleep(1800);
                    break;
                case 10:
                    //drop climbers code
                    break;
                case 11:
                    run_motors(-forward_power, -forward_power);
                    break;
                case 12:
                    bSleep(3600);
                    break;
                case 13:
                    run_motors(-forward_power, forward_power);
                    break;
                case 14:
                    bSleep(1000);
                    break;
                case 15:
                    run_motors(forward_power, forward_power);
                    break;
                case 16:
                    bSleep(2000);
                case 17:
                    run_motors(forward_power, -forward_power);
                    break;
                case 18:
                    bSleep(3600);
                    break;
                case 19:
                    run_motors(forward_power, forward_power);
                    break;
                case 20:
                    bSleep(2500);
                    break;
                default:
                    stop_motors();
                    break;
            }
        step++;
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

    void run_motors_until(int lPower, int rPower, int distance){
        int lDistance, rDistance;
        if(lPower > 0) lDistance = distance;
        else lDistance = -distance;
        
        if(rPower > 0) rDistance = distance;
        else rDistance = -distance;
        runToPosition(lDistance, rDistance);
        run_motors(lPower, rPower);
    }

    void bSleep(long time){
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void turn_left_until(double power, double distance){
        runToPosition(distance, -distance);
        run_motors(power, -power);
    }

    void turn_right_until(double power, double distance){
        runToPosition(-distance, distance);
        run_motors(-power, power);
    }

    void resetEncoders(){
        front_left.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        front_right.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        back_left.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        back_right.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    void runToPosition(int lDistance, int rDistance){
    	front_left.setTargetPosition(lDistance);
    	front_right.setTargetPosition(rDistance);
    	back_left.setTargetPosition(lDistance);
    	back_right.setTargetPosition(rDistance);
    	
        front_left.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        front_right.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        back_left.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        back_right.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

} // PushBotAuto

