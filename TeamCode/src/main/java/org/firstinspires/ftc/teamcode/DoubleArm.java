package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
/**
 * Created by BeehiveRobotics-3648 on 9/19/2017.
 */
@TeleOp(name = "DoubleArm", group = "linear OpMode")
public class DoubleArm extends OpMode{
    Servo Claw;
    Servo UpDown;
    Servo BigRotation;
    
    @Override

    public void init(){
        Claw = hardwareMap.servo.get("s1");
        Claw.setPosition(0.6);

    }

    @Override
    public void loop(){
        if (gamepad1.a){
            if (Claw.getPosition() < 1.00) {
                Claw.setPosition(Claw.getPosition() + 0.075);
            }
        }
        if (gamepad1.b){
            if (Claw.getPosition() > 0.4) {
                Claw.setPosition(Claw.getPosition() - 0.075);
            }
        }
        telemetry.addData("ClawServoPos",Claw.getPosition());
        telemetry.update();
    }
}
