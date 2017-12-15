package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
/**
 * Created by BeehiveRobotics-3648 on 10/17/2017.
 */

@TeleOp(name = "relicArmTest", group = "linear OpMode")
@Disabled
public class relicArmTest extends OpMode {
    Servo servo;
    DcMotor motor;
    boolean armhasInitiated = false;

    @Override

    public void init() {
        servo = hardwareMap.servo.get("s1");
        motor = hardwareMap.dcMotor.get("m1");
        servo.setPosition(.1);

    }

    @Override
    public void loop() {
        double moveMotor = gamepad1.right_stick_x;
        /*
        if (encodervalue() > -3000 || encodervalue() < 1000){
            motor.setPower(moveMotor);
        }
        if (gamepad1.y){
            if(encodervalue() > 0){
                while (encodervalue() > 10) {
                    motor.setPower();
                }
                motor.setPower(0);
            }

                else{
                while (encodervalue() < -10){
                    motor.setPower();
                }
                motor.setPower(0);
            }
        }
        */
        encodervalue();

        if (gamepad1.a) {
            if (armhasInitiated == false) {
                servo.setPosition(1.0);
                armhasInitiated = true;
            }
            if (armhasInitiated) {
                servo.setPosition(.5);
                armhasInitiated = false;
            }
        }
        if (gamepad1.b) {
                servo.setPosition(0);
            armhasInitiated = false;
        }
        telemetry.addData("Servo Position: ", servo.getPosition());
        telemetry.update();
    }


    int encodervalue() {
        int m1;
        m1 = motor.getCurrentPosition();
        telemetry.addData("Encoder value: ", m1);
        telemetry.update();
        return m1;

    }

}
