package com.qualcomm.ftcrobotcontroller.opmodes.customOpModes;

import com.qualcomm.ftcrobotcontroller.opmodes.FreshClasses.FreshMethods;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import java.util.Hashtable;

/**
 * Created by Naisan on 3/15/2016.
 */


public class TheFreshMenTeleOp extends OpMode {

    Hashtable<String, Float> valuesTable = new Hashtable<String, Float>();

    final float OFF = 0.0f,
                LIFT_POWER = 1.0f,
                SWEEPER_POWER = 1.0f,
                BASKET_OPEN = 0.5f,
                BASKET_CLOSE = 0.0f;

    final int   CSERVO_POWER = 100,
                CRSERVO_START = 600,
                CRSERVO_END = 2400;

    int     CSERVO_RIGHTP = 600,
            CSERVO_LEFTP = 600;

    private boolean LiftDown = true;

    //Motors
    DcMotor     M_backLeft, //andy
                M_backRight, //barry
                M_frontLeft, //carl
                M_frontRight, //daniel
                M_rackPinion,
                M_sweeper;
    //Servos
    PWMOutput   S_rackRight,
                S_rackLeft;

     Servo      S_bucket;

    //Sensors
    TouchSensor T_Lift;

    @Override
    public void init() {
        //DC Motors
        M_backLeft = hardwareMap.dcMotor.get("BackLeft");
        M_backRight = hardwareMap.dcMotor.get("BackRight");
        M_frontLeft = hardwareMap.dcMotor.get("FrontLeft");
        M_frontRight = hardwareMap.dcMotor.get("FrontLeft");
        M_rackPinion = hardwareMap.dcMotor.get("RackPinion");
        M_sweeper = hardwareMap.dcMotor.get("Sweeper");
        //Servos
        S_bucket = hardwareMap.servo.get("Bucket");
        S_rackLeft = hardwareMap.pwmOutput.get("RackLeft");
        S_rackRight = hardwareMap.pwmOutput.get("RackRight");
        //Sensors
        T_Lift = hardwareMap.touchSensor.get("TouchSensor1");

    }
    public void loop() {

        //Getting power values
        valuesTable = FreshMethods.get_motor_values_teleop(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        float xvalue = valuesTable.get("XValue");
        float yvalue = valuesTable.get("YValue");
        float turnValue = valuesTable.get("TurnValue");

        //Power Calculations
        double andypower = xvalue + (-yvalue) + turnValue;
        double barrypower = xvalue + yvalue + turnValue;
        double carlpower = -xvalue + (-yvalue) + turnValue;
        double danielpower = -xvalue + yvalue + turnValue;

        //Clipping
        Range.clip(andypower, -1, 1);
        Range.clip(barrypower, -1, 1);
        Range.clip(carlpower, -1, 1);
        Range.clip(danielpower, -1, 1);

        //Power Set
        M_backLeft.setPower(andypower);
        M_backRight.setPower(barrypower);
        M_frontLeft.setPower(carlpower);
        M_frontRight.setPower(danielpower);

        // Print Power Data
        telemetry.addData("Text", "_____RobotData_____");
        telemetry.addData("BackLeft","Back Left Motor: " + andypower);
        telemetry.addData("BackRight","Back Right Motor: " + barrypower);
        telemetry.addData("FrontLeft","Front Left Motor: " + carlpower);
        telemetry.addData("FrontRight","Front Right Motor: " + danielpower);

        // Lift Calculations
        if(gamepad1.x) {
            CSERVO_RIGHTP = CSERVO_RIGHTP + CSERVO_POWER;
            CSERVO_LEFTP = CSERVO_LEFTP + CSERVO_POWER;
            if (CSERVO_RIGHTP > CRSERVO_END)
                CSERVO_RIGHTP = CRSERVO_END;
            if (CSERVO_LEFTP > CRSERVO_END)
                CSERVO_LEFTP = CRSERVO_END;
            M_rackPinion.setPower(LIFT_POWER);
            S_rackRight.setPulseWidthOutputTime(CSERVO_RIGHTP);
            S_rackLeft.setPulseWidthOutputTime(CSERVO_LEFTP);
        }
        else if (gamepad1.y){
            CSERVO_RIGHTP = CSERVO_RIGHTP - CSERVO_POWER;
            CSERVO_LEFTP = CSERVO_LEFTP - CSERVO_POWER;
            if (CSERVO_LEFTP < CRSERVO_START)
                CSERVO_LEFTP = CRSERVO_START;
            if (CSERVO_RIGHTP < CRSERVO_START)
                CSERVO_RIGHTP = CRSERVO_START;
            S_rackRight.setPulseWidthOutputTime(CSERVO_RIGHTP);
            S_rackLeft.setPulseWidthOutputTime(CSERVO_LEFTP);
            M_rackPinion.setPower(-LIFT_POWER);
        }
        else{
            S_rackLeft.setPulseWidthOutputTime(CSERVO_LEFTP);
            S_rackRight.setPulseWidthOutputTime(CSERVO_RIGHTP);
            M_rackPinion.setPower(OFF);
        }
        //Sweeper Calculations
        if (gamepad1.right_trigger > 1){
            if (!T_Lift.isPressed())
                M_sweeper.setPower(SWEEPER_POWER);
        }
        else if (gamepad1.left_trigger > 1)
            M_sweeper.setPower(-SWEEPER_POWER);
        else
            M_sweeper.setPower(OFF);
        // Basket
        if (gamepad1.a)
            S_bucket.setPosition(BASKET_OPEN);
        else
            S_bucket.setPosition(BASKET_CLOSE);
    }
}



