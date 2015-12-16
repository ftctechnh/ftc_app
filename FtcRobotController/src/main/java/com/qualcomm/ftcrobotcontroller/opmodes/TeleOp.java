package com.qualcomm.ftcrobotcontroller.opmodes;
import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * @author Darron and Caiti

 * created 12/9/2015
 *  ftc robot conroler teleop class
 */
public class TeleOp extends OpMode {
    DcMotor rightmotor;
    DcMotor leftmotor = hardwareMap.dcMotor.get("leftmotor");
    DcMotor leftmotor2 = hardwareMap.dcMotor.get("leftmotor2");
    DcMotor rightmotor2 = hardwareMap.dcMotor.get("rightmotor2");
    DcMotor arm;
    Servo shifter;
    boolean locked;
    String position;
    Accelorometer accel;
    ColorSensor sensorRGB = hardwareMap.colorSensor.get("color");
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
        rightmotor =hardwareMap.dcMotor.get("rightmotor");
        shifter = hardwareMap.servo.get("shifter");
        rightmotor.setDirection(DcMotor.Direction.REVERSE);
        rightmotor2.setDirection(DcMotor.Direction.REVERSE);
        arm = hardwareMap.dcMotor.get("arm");
        shifter.setPosition(.605);
        position = "b";
        accel = new Accelorometer();
    }
    @Override
/**
 * the loop to run during teleop
 * gives power to the motors and servos
 * based on user commands
 */
    public void loop()
    {
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;
        float rightY2 = -gamepad1.right_stick_y;
        if(gamepad1.left_bumper)
        {
            leftmotor.setPower(.5*leftY);
            rightmotor.setPower(.5*rightY);
            leftmotor2.setPower(.5*leftY);
            rightmotor2.setPower(.5*rightY);
        }
        else {

            leftmotor2.setPower(leftY);
            rightmotor2.setPower(rightY);
            leftmotor.setPower(leftY);
            rightmotor.setPower(rightY);
        }
         if (gamepad1.x) {
             if (position.equals("a")) {
                 shifter.setPosition(.583);
             } else if (position.equals("b")) {
                 shifter.setPosition(.582);
             } else {
                 shifter.setPosition(.585);
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
            }
        }
        arm.setPower(rightY2);
        telemetry.addData("is half power on", gamepad1.left_bumper);
        telemetry.addData("estop",locked);
        telemetry.addData("position", position);
        telemetry.addData("x",accel.getXAxis());
        telemetry.addData("y",accel.getYAxis());
        telemetry.addData("z",accel.getZAxis());
    }
}
