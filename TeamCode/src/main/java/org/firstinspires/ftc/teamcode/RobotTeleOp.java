package org.firstinspires.ftc.teamcode;

//------------------------------------------------------------------------------
//
// PushBotManual
//

import android.media.AudioManager;
import android.media.ToneGenerator;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
/**
 * Provide a basic manual operational mode that uses the left and right
 * drive motors, left arm motor, servo motors and gamepad input from two
 * gamepads for the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-01-06-01
 */

@TeleOp(name="TeleOp", group="Manual")

public class RobotTeleOp extends RobotTelemetry {
    //--------------------------------------------------------------------------
    //
    // PushBotManual
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */

    public RobotTeleOp()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-destruction.
    } // PushBotManual

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during
     * manual-operation.  The state machine uses gamepad input to transition
     * between states.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */

    @Override public void init() {
        super.init();

    }


    private boolean toggle = true;
    private boolean pressed;
    private boolean dpad;
    double x = 0.0;

    private double prev_left_y = 0, prev_right_y = 0;


    public void simulateLiftMovement() {
        arm_1_position(0.3D);
        arm_2_position(0.55D);
    }

    @Override
    public void start(){
        arm_3_position(0.4D);
    }

    @Override
    public void stop() {
        arm_1_position(0.26D);
        arm_2_position(0.54D);
    }
    @Override
    public void loop() {
        //
        // GAMEPAD 1
        // Manage the drive wheel motors.
        //
        float l_left_drive_power = scale_motor_power(-gamepad1.left_stick_y);
        float l_right_drive_power = scale_motor_power(-gamepad1.right_stick_y);


        if(!gamepad1.y) {
            pressed = false;
        }

        if(gamepad1.y) {
            if (toggle && !pressed){
                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                toggle = false;
                pressed = true;
            }else if (!pressed){
                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                toggle = true;
                pressed = true;
            }
        }

        if (gamepad1.dpad_up){
            if (toggle){
                set_drive_power(1f,1f);
            }else{
                set_drive_power(0.25f,0.25f);
            }
            dpad = true;
        }
        if (gamepad1.dpad_down){
            if (toggle){
                set_drive_power(-1f, -1f);
            }else{
                set_drive_power(-0.25f,-0.25f);
            }
            dpad = true;
        }
        if (gamepad1.dpad_left){
            if (toggle){
                set_drive_power(-1f,1f);
            }else{
                set_drive_power(-0.25f,0.25f);
            }

            dpad = true;
        }
        if (gamepad1.dpad_right){
            if (toggle){
                set_drive_power(1f, -1f);
            }else{
                set_drive_power(0.25f,-0.25f);
            }
            dpad = true;
        }

        if (!gamepad1.dpad_up && !gamepad1.dpad_left && !gamepad1.dpad_right && !gamepad1.dpad_down){
            dpad = false;
        }

        if (!dpad){
            double speadscale = 4.0;
            if (toggle){
                set_drive_power((l_left_drive_power / speadscale), (l_right_drive_power / speadscale));
            }else{
                set_drive_power((l_left_drive_power), (l_right_drive_power));
            }
        }


        //Manage the arm servos.
        //GAMEPAD 2
        //arm_1_position(get_arm_1_position() + gamepad2.left_stick_ y);
        //arm_2_position(get_arm_2_position() + gamepad2.right_stick_y);

        //Linear Controls
        double increment = 0.004;
        int delay = 35;
        if (gamepad2.left_stick_y > 0) {
            arm_1_position(get_arm_1_position() - increment);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if (gamepad2.left_stick_y < 0){
            arm_1_position(get_arm_1_position() + increment);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (gamepad2.right_stick_y > 0) {
            arm_2_position(get_arm_2_position() - increment);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if (gamepad2.right_stick_y < 0) {
            arm_2_position(get_arm_2_position() + increment);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Gripper
        if (gamepad2.left_bumper) {
            arm_3_position(0.0D);
        } else if (gamepad2.right_bumper) {
            arm_3_position(1.0D);
        }



        //if (gamepad2.x){ Nah.
        //    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        //    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
        //
        //    arm_home_position();
        //}

        /*Raw Controls
        double deadzone = .05;
        if (Math.abs(gamepad2.left_stick_y - prev_left_y) > deadzone) {
            arm_1_position(-gamepad2.left_stick_y);
            prev_left_y = gamepad2.left_stick_y;
        }
        if (Math.abs(gamepad2.right_stick_y - prev_right_y) > deadzone) {
            arm_2_position(-gamepad2.right_stick_y);
            prev_right_y = gamepad2.right_stick_y;
        }
        */

        //
        // Send telemetry data to the driver station.
        //
        update_telemetry(); // Update common telemetry
        update_gamepad_telemetry();

        telemetry.addData("ARM 1", get_arm_1_position());
        telemetry.addData("ARM 2", get_arm_2_position());
        telemetry.addData("ARM 3", get_arm_3_position());
        telemetry.addData("Sanic Mode", toggle);


    }
}
