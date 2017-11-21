package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by rohan and pahel on 11/20/2017.
 */
@TeleOp(name = "Glyph_Servo_Test")
//@Disabled
public class Glyph_Servo_Test extends OpMode{

    Servo glyphServo1;
    Servo glyphServo2;

    @Override
    public void init(){
        glyphServo1 = hardwareMap.servo.get("glyphServo1");
        glyphServo2 = hardwareMap.servo.get("glyphServo2");


    }

    @Override
    public void loop() {
        telemetry.addData("The value of the left servo is", glyphServo1.getPosition());
        telemetry.addData("The value of the right servo is", glyphServo2.getPosition());
        telemetry.update();
    }
}
