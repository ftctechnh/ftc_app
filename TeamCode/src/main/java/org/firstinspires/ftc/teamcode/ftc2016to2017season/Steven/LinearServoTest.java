package org.firstinspires.ftc.teamcode.ftc2016to2017season.Steven;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Steven on 3/21/2017.
 */
@TeleOp(name = "linearservotest", group = "")
@Disabled
public class LinearServoTest extends OpMode {

    ElapsedTime runtime = new ElapsedTime();
    Servo linearservo;
    public void init() {
        linearservo = hardwareMap.servo.get("ServoPress");
        linearservo.setPosition(-0.5);
        telemetry.addData("","READY TO START");
        telemetry.addData("",linearservo.getPosition());
        telemetry.update();
    }

    @Override
    public void start(){
       // runtime.reset();
        linearservo.setPosition(1);
        telemetry.addData("",linearservo.getPosition());
        telemetry.update();
      //  telemetry.addData("Time",runtime.milliseconds());
       // telemetry.update();
       // runtime.reset();

    }

    @Override
    public void loop(){

    }
}
