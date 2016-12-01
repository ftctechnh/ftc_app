package org.firstinspires.ftc.team8200;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.team8200.*;
import org.firstinspires.ftc.team8200.HardwareK9bot;

import java.util.EventListenerProxy;

/*
 *
 * This is an example LinearOpMode that shows how to use
 * a legacy (NXT-compatible) Light Sensor.
 * It assumes that the light sensor is configured with a name of "sensor_light".
 *
 * You can use the X button on gamepad1 to turn Toggle the LED on and off.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@TeleOp(name = "Sensor: LEGO light", group = "Sensor")
@Disabled
public class LightMergeSensor extends LinearOpMode {

    LightSensor lightSensor;  // Hardware Device Object

    HardwareK9bot robot = new HardwareK9bot();

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // DcMotors
        DcMotor leftMotor = null;
        DcMotor rightMotor = null;

        // bPrevState and bCurrState represent the previous and current state of the button.
        boolean bPrevState = false;
        boolean bCurrState = false;

        // bLedOn represents the state of the LED.
        boolean bLedOn = true;

        // get a reference to our Light Sensor object.
        lightSensor = hardwareMap.lightSensor.get("light");

        // Set the LED state in the beginning.
        lightSensor.enableLed(bLedOn);

        // Timer
        ElapsedTime timer = new ElapsedTime();

        // wait for the start button to be pressed.
        waitForStart();

        // while the op mode is active, loop and read the light levels.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {

            // check the status of the x button .
            bCurrState = gamepad1.x;

            // check for button state transitions.
            if ((bCurrState == true) && (bCurrState != bPrevState))  {

                // button is transitioning to a pressed state.  Toggle LED
                bLedOn = !bLedOn;
                lightSensor.enableLed(bLedOn);
            }

            // update previous state variable.
            bPrevState = bCurrState;

            // send the info back to driver station using telemetry function.
            telemetry.addData("LED", bLedOn ? "On" : "Off");
            telemetry.addData("Raw", lightSensor.getRawLightDetected());
            telemetry.addData("Normal", lightSensor.getLightDetected());

            telemetry.update();

            leftMotor.setPower(0);
            rightMotor.setPower(0);

            while(timer.seconds() < 10){
                // Move bot forward
                robot.leftMotor.setPower(1);
                robot.rightMotor.setPower(1);

                if(lightSensor.getLightDetected() == 1){
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(1);
                }
            }

        }
    }
}
