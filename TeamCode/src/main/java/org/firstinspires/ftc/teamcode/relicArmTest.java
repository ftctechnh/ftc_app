package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
/**
 * Created by BeehiveRobotics-3648 on 10/17/2017.
 */

@TeleOp(name = "relicArmTest", group = "linear OpMode")

public class relicArmTest extends OpMode{
    Servo servo;
    DcMotor motor;
    @Override

    public void init() {
        servo = hardwareMap.servo.get("s1");
        motor = hardwareMap.dcMotor.get("m1");
        servo.setPosition(0);
        servo.setPosition(0);
    }

    @Override
    public void loop(){
        double moveMotor = gamepad1.right_stick_x;
        motor.setPower(moveMotor);

        if (gamepad1.a){

                servo.setPosition(1);
                servo.setPosition(1);
        }

    }
}
