package org.firstinspires.ftc.teamcode.training.josh;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by ftc6347 on 6/27/17.
 */
@TeleOp(name = "JNS Training (Programming Bot)", group = "joshTraining")
public class JoshTraining extends OpMode {

    private DcMotor leftBack;
    private DcMotor leftFront;
    private DcMotor rightBack;
    private DcMotor rightFront;
    @Override
    public void init() {
        // init the Wheels
        leftBack = hardwareMap.dcMotor.get("bl");
        leftFront = hardwareMap.dcMotor.get("fl");
        rightBack = hardwareMap.dcMotor.get("br");
        rightFront = hardwareMap.dcMotor.get("fr");
    // set wheel direction
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
// set deadzone
        gamepad1.setJoystickDeadzone(0.1f);
    }

    @Override
    public void loop() {
        // declare movement vars
        double left;
        double right;

/**

        leftBack.setPower(left);
        leftFront.setPower(left);
        rightBack.setPower(right);
        rightFront.setPower(right);

        if(gamepad1.y){

            leftFront.setPower(1);
        }
        if(gamepad1.b){

            rightFront.setPower(1);
        }
        if(gamepad1.a){

            rightBack.setPower(1);
        }
        if(gamepad1.x){

            leftBack.setPower(1);
        }
 */
    // set moving direction var

        // check what the direction of the stick is and convert it to a simple dir
        // forward movement
        if(gamepad1.left_stick_y < 0 && (gamepad1.left_stick_x >= -0.1F && gamepad1.left_stick_x <= 0.1F )){
            leftBack.setPower(gamepad1.left_stick_y);
            leftFront.setPower(gamepad1.left_stick_y);
            rightBack.setPower(gamepad1.left_stick_y);
            rightFront.setPower(gamepad1.left_stick_y);
        }
        // backward movement
        else if(gamepad1.left_stick_y > 0 && (gamepad1.left_stick_x >= -0.1F && gamepad1.left_stick_x <= 0.1F)){
            leftBack.setPower(gamepad1.left_stick_y);
            leftFront.setPower(gamepad1.left_stick_y);
            rightBack.setPower(gamepad1.left_stick_y);
            rightFront.setPower(gamepad1.left_stick_y);
        }
        // left movement
        else if(gamepad1.left_stick_x < 0 && (gamepad1.left_stick_y >= -0.1F && gamepad1.left_stick_y <= 0.1F)){
            leftBack.setPower(gamepad1.left_stick_x);
            leftFront.setPower(-gamepad1.left_stick_x);
            rightBack.setPower(-gamepad1.left_stick_x);
            rightFront.setPower(gamepad1.left_stick_x);
        }
        // reset the powers
        else if(gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0){
            leftBack.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            rightFront.setPower(0);
        }
        //else if(gamepad1.left_stick_y < 0 && gamepad1.left_stick_x == 0){




    }




    }
