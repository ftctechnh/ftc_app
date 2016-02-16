package com.qualcomm.ftcrobotcontroller.opmodes;
import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

/**
 * @author Darron and Caiti

 * created 12/9/2015
 *  ftc robot conroler teleop class
 */
public class TeleOp extends OpMode {
    DcMotor rightmotor;
    DcMotor leftmotor;
    DcMotor leftmotor2;
    DcMotor rightmotor2;
    Servo shifter;
    boolean locked;
    String position;
    Accelorometer accel;
    ColorSensor sensorRGB;
    DcMotor pully;
    DcMotor arm1;
    DcMotor arm2;
    Calendar time;
    float velocity1;
   double distanceX;
    long currenttime;
    long pasttime;
    long deltaTime;
    List<Float> list = new ArrayList<Float>();
    private void initsensor() throws InterruptedException {
        hardwareMap.logDevices();
        sensorRGB = hardwareMap.colorSensor.get("color");
        sensorRGB.enableLed(false);
    }
    private int getRed() throws InterruptedException {
        float hsvValues[] = {0F, 0F, 0F};
        Color.RGBToHSV(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue(), hsvValues);
        return sensorRGB.red();
    }
    private int getBlue() throws InterruptedException {
        float hsvValues[] = {0F, 0F, 0F};
        Color.RGBToHSV(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue(), hsvValues);
        return sensorRGB.blue();
    }
    /**
     * the main initalization loop for the robots teleop mode
     *
     */

    @Override
    public void init()
    {
        leftmotor = hardwareMap.dcMotor.get("leftmotor");
       leftmotor2 = hardwareMap.dcMotor.get("leftmotor2");
       rightmotor2 = hardwareMap.dcMotor.get("rightmotor2");
        rightmotor =hardwareMap.dcMotor.get("rightmotor");
        arm1 = hardwareMap.dcMotor.get("arm1");
        arm2 = hardwareMap.dcMotor.get("arm2");
        shifter = hardwareMap.servo.get("shifter");
        rightmotor.setDirection(DcMotor.Direction.REVERSE);
        rightmotor2.setDirection(DcMotor.Direction.REVERSE);
        shifter.setPosition(.605);
        position = "b";
        accel = new Accelorometer();
        accel.init();
        accel.start();
        velocity1 = 0;
        distanceX = 0;
               currenttime  = 1000* time.getTimeInMillis();
        pasttime=1000*time.getTimeInMillis();
    }
    /**
     * the loop to run during teleop
     * gives power to the motors and servos
     * based on user input.
     */
    @Override
    public void loop()
    {
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;
        float rightY2 = -gamepad2.right_stick_y;
        float leftY2 = -gamepad2.left_stick_y;
        float leftX = -gamepad1.left_stick_x;
        float rightX = -gamepad1.right_stick_x;
        float rightX2 = -gamepad2.right_stick_x;
        float leftX2 = -gamepad2.left_stick_x;

        leftmotor.setPower(leftY);
        rightmotor.setPower(rightY);
        leftmotor2.setPower(leftY);
        rightmotor2.setPower(rightY);
         if (gamepad1.x) {
             if (position.equals("a")) {
                 shifter.setPosition(.581);
             } else if (position.equals("b")) {
                 shifter.setPosition(.582);
             } else {
                 shifter.setPosition(.581);
             }
         }

        else if(gamepad1.a) {
            shifter.setPosition(.56);
             position = "a";
        }
        else if(gamepad1.b)
        {
            shifter.setPosition(.605);
            position = "b";
        }
        arm1.setPower(rightY2);
        arm2.setPower(leftY2);
        if (gamepad2.y)
        {
            pully.setPower(.5);
        }
        else if (gamepad2.x)
        {
            pully.setPower(-.5);
        }
        if (gamepad2.a)
        {
            while (!( gamepad2.b))
            {
                leftmotor.setPower(0);
                rightmotor.setPower(0);
                leftmotor2.setPower(.0);
                rightmotor2.setPower(.0);
                locked = true;
                telemetry.addData("estop",locked);
                telemetry.addData("to resume press", " b");
            }
        }
        telemetry.addData("is half power on", gamepad1.left_bumper);
        telemetry.addData("estop",locked);
        telemetry.addData("position", position);
        telemetry.addData("x", accel.getXAxis());
        telemetry.addData("y", accel.getYAxis());
        telemetry.addData("z",accel.getZAxis());
        telemetry.addData("distanceX", updateMovementX());
    }
    private double updateMovementX()
    {   float acceli = accel.getXAxis();
        currenttime=1000*time.getTimeInMillis();
       deltaTime = currenttime - pasttime;
        distanceX= distanceX+ (velocity1 * deltaTime)+(.5*acceli*(deltaTime*deltaTime));
        velocity1 = velocity1+(acceli*deltaTime);
        pasttime = currenttime;
        return distanceX;

    }
}
