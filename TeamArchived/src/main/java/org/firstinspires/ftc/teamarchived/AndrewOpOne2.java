package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by robotics on 10/20/2015.
 */
public class AndrewOpOne2 extends OpMode {

    DcMotor leftBackMotor;
    DcMotor leftFrontMotor;
    DcMotor rightBackMotor;
    DcMotor rightFrontMotor;
    DcMotorController rightController;
    DcMotorController leftController;
    DcMotor armMotor;
    DcMotorController scrubcontroller;
    Servo armHook;
    ServoController scrubmind;
    final static double SCRUB_HOOK_UP = 0.0;
    final static double SCRUB_HOOK_DOWN = 1.0;
    private int v_state = 0;


    @Override
    public void init() {
        //Get hardware references
        leftBackMotor = hardwareMap.dcMotor.get("back_left_drive");
        leftFrontMotor = hardwareMap.dcMotor.get("front_left_drive");
        rightBackMotor = hardwareMap.dcMotor.get("back_right_motor");
        rightFrontMotor = hardwareMap.dcMotor.get("front_right_motor");
        leftController = hardwareMap.dcMotorController.get("right_controller");
        rightController = hardwareMap.dcMotorController.get("left_controller");
        armMotor = hardwareMap.dcMotor.get("scrub_motor");
        scrubcontroller = hardwareMap.dcMotorController.get("scrub_controller");
        armHook = hardwareMap.servo.get("scrub_hook");
        scrubmind = hardwareMap.servoController.get("scrub_mind");
        armHook.setPosition(SCRUB_HOOK_DOWN);

        //Reverses right motors
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        //leftController.setMotorChannelMode(1, DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftBackMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftFrontMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightBackMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightFrontMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //scrubcontroller.setMotorChannelMode(1, DcMotorController.RunMode.RUN_WITHOUT_ENCODERS)
    }

    public void start() {
        resetStartTime();
    }

    @Override
    public void loop() {


        switch (v_state) {
            case 0:
                setDrivePower(1,1);
                if (getRuntime() > 2.0){
                    v_state++;
                }
                break;

            case 1:
                setDrivePower(-1, 1);
                if (getRuntime() > 5.0) {
                    v_state++;
                }
                break;

            case 2:
                setDrivePower(1,1);
                if (getRuntime() > 10.0) {
                    v_state++;
                }
                break;

            case 3:
                setDrivePower(-1, 1);
                if (getRuntime() > 13.0) {
                    setDrivePower(0,0);
                    v_state++;
                }
                break;

            case 4:
                armMotor.setPower(0.3);
                if (getRuntime() > 14.0) {
                    v_state++;
                    armMotor.setPower(0);
                }
                break;

            case 5:
                armHook.setPosition(SCRUB_HOOK_UP);
                break;

            default:

        }


        telemetry.addData("time: ", getRuntime());
        telemetry.addData("state: ", v_state);
    }

    public void setDrivePower(double left, double right) {
        rightBackMotor.setPower(right);
        rightFrontMotor.setPower(right);
        leftBackMotor.setPower(left);
        leftFrontMotor.setPower(left);
    }
}
