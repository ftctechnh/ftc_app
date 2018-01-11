package org.firstinspires.ftc.teamcode.Deprecated;

/**
 * Created by Liam on 7/13/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

//teleop mode to use if you only have 2 motors

@TeleOp(name = "1MotorDrive", group = "linear OpMode")
@Disabled
public class Drive1Motor extends OpMode {

    DcMotor Motor;
    double speed = 0;

    @Override


    public void init() {
        Motor = hardwareMap.dcMotor.get("m1");

    }

    @Override
    public void loop() {
        speed = 0;
        if (gamepad1.a){
            speed = 0.5;
        }
        else if (gamepad1.b){
            speed = -0.5;
        }
        Motor.setPower(speed);
    }
}