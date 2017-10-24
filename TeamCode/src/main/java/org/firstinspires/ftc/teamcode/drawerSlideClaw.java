package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Aus on 10/24/2017.
 */

public class drawerSlideClaw extends OpMode {
    Servo claw;

    @Override

    public void init(){
        claw = hardwareMap.servo.get("s1" );

    }

    @Override
    public void loop(){
            if (gamepad1.a) {
                claw.setPosition(claw.getPosition()+0.001);
                if (gamepad1.b) {
                    claw.setPosition(claw.getPosition()-0.001);
        }
    }

}
