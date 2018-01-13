package org.firstinspires.ftc.teamcode.ftc2017to2018season.StressTess.rev;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.StressTess.rev.Autonomous_General;

//10-28-17
@Autonomous(name="Servo Test_", group = "REV")
@Disabled
public class revServo extends Autonomous_General {

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {


        sleep(1000);
        jewelServo.setPosition(0.8);
        telemetry.addData("Jewel Target", "0.8 (Down)");
        telemetry.update();
        sleep(1000);
        jewelServo.setPosition(0.45);
        telemetry.addData("Jewel Target", "0.45 (Middle)");
        telemetry.update();
        sleep(1000);
        jewelServo.setPosition(0.1);
        telemetry.addData("Jewel Target", "0.1 (Up)");
        telemetry.update();
        sleep(1000);
        jewelServo.setPosition(0.8);
        sleep(1000);
        jewelServo.setPosition(0.8);
        telemetry.addData("Jewel Target", "0.8 (Down)");
        telemetry.update();
        sleep(1000);
        jewelServo.setPosition(0.45);
        telemetry.addData("Jewel Target", "0.45 (Middle)");
        telemetry.update();
        sleep(1000);
        jewelServo.setPosition(0.1);
        telemetry.addData("Jewel Target", "0.1 (Up)");
        telemetry.update();
        sleep(1000);
        jewelServo.setPosition(0.8);
        sleep(1000);
        jewelServo.setPosition(0.8);
        telemetry.addData("Jewel Target", "0.8 (Down)");
        telemetry.update();
        sleep(1000);
        jewelServo.setPosition(0.45);
        telemetry.addData("Jewel Target", "0.45 (Middle)");
        telemetry.update();
        sleep(1000);
        jewelServo.setPosition(0.1);
        telemetry.addData("Jewel Target", "0.1 (Up)");
        telemetry.update();
        sleep(1000);
        jewelServo.setPosition(0.8);


        sleep(5000);


        moveAllServo(0.8,0.05, 0.85, 1000);
        moveAllServo(0.1,0.5, 0.35, 1000);
        moveAllServo(0.8,0.05, 0.35, 1000);
        moveAllServo(0.45,0.05, 0.85, 1000);
        moveAllServo(0.1,0.5, 0.35, 1000);
        moveAllServo(0.8,0.05, 0.85, 1000);



    }

    public void moveAllServo(double jewel, double glyphLeft, double glyphRight, long waitTime){

        jewelServo.setPosition(jewel);
        glyphServoLeft.setPosition(glyphLeft);
        glyphServoRight.setPosition(glyphRight);

        telemetry.addData("Jewel Target", jewel);
        telemetry.addData("Glyph Left Target", glyphLeft);
        telemetry.addData("Glyph Right Target", glyphRight);

        sleep(waitTime);
    }
}
