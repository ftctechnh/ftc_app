package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by jonathonmangan on 9/6/17.
 */
@TeleOp(name="Gear Protobot Tank", group="GearProtobot")
public class GearProtoBot extends OpMode{


    private DcMotor motorUpRight;
    private DcMotor motorDownRight;
    private DcMotor motorDownLeft;
    private DcMotor motorUpLeft;

    public void init() {
        motorUpRight = hardwareMap.dcMotor.get("upRight");
        motorDownRight = hardwareMap.dcMotor.get("downRight");
        motorDownLeft = hardwareMap.dcMotor.get("downLeft");
        motorUpLeft = hardwareMap.dcMotor.get("upLeft");
    }
    public void loop(){
        if(gamepad1.left_trigger > .1 || gamepad1.right_trigger > .1){
            if(gamepad1.left_trigger > gamepad1.right_trigger){
                motorUpLeft.setPower(gamepad1.left_trigger);
                motorDownLeft.setPower(gamepad1.left_trigger);
            }else{
                motorUpRight.setPower(gamepad1.right_trigger);
                motorDownRight.setPower(gamepad1.right_trigger);
            }
        }else{
            motorUpRight.setPower(gamepad1.left_stick_x);
            motorUpLeft.setPower(gamepad1.left_stick_y);
            motorDownLeft.setPower(gamepad1.left_stick_y);
            motorDownRight.setPower(gamepad1.left_stick_x);
        }
    }
}
