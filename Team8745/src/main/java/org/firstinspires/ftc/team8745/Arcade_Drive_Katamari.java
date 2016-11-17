package org.firstinspires.ftc.team8745;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by some guy "named" Nintendo8 on 11/6/2016.
 */
@Disabled
@TeleOp(name="Katamari One")
public class Arcade_Drive_Katamari extends OpMode {
    DcMotor leftFRONT;
    DcMotor rightFRONT;
    DcMotor leftBACK;
    DcMotor rightBACK;
    Boolean switcher = false;
    // Will switch with every loop using this boolean, hopefully
    public void init() {
        leftFRONT = hardwareMap.dcMotor.get("motor-left");
        rightFRONT = hardwareMap.dcMotor.get("motor-right");
        leftBACK = hardwareMap.dcMotor.get("motor-leftBACK");
        rightBACK = hardwareMap.dcMotor.get("motor-rightBACK");
        leftFRONT.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBACK.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    @Override
    public void loop() {
        //floats
        float left_stick_x = gamepad1.left_stick_x;
        float left_stick_y = gamepad1.left_stick_y;
        float rightTrigger = gamepad1.right_trigger;
        boolean rightBumperPressed = gamepad1.right_bumper;
        //floats

        // Other Stuff

        if (switcher = true) {
            leftFRONT.setPower(left_stick_y);
            rightFRONT.setPower(left_stick_y);
        }

// if pushed right

    if(left_stick_x>0) {
        if (switcher = false){
            leftFRONT.setPower(left_stick_x);
            rightFRONT.setPower(-left_stick_x);
        } }

// if pushed left

    if(left_stick_x<0){
        if (switcher == false){
            leftFRONT.setPower(-left_stick_x);
            rightFRONT.setPower(left_stick_x);
        } }

// If no y input (for emergency turns)

    if(left_stick_y==0) {
        if (left_stick_x < 0){
            leftFRONT.setPower(-left_stick_x);
            rightFRONT.setPower(left_stick_x);
        }
        else {
            leftFRONT.setPower (left_stick_x);
            rightFRONT.setPower (-left_stick_x);
        } }

    if(switcher = true) {
        leftFRONT.setPower(left_stick_y);
        rightFRONT.setPower(left_stick_y);
        switcher = false;}

// If no x input (for emergency zooming forward
    if (left_stick_x==0){
        rightFRONT.setPower(left_stick_y);
        leftFRONT.setPower (left_stick_y);}

    //Back Wheels
    rightBACK.setPower(rightFRONT.getPower());
    leftBACK.setPower(leftFRONT.getPower());


}}
