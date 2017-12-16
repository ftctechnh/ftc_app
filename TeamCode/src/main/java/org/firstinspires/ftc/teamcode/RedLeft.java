package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by jxfio on 12/15/2017.
 */

@Autonomous(name="Red Left", group="robot2")
public class RedLeft extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor Left;
    private DcMotor Right;
    private Servo RF;
    private Servo LF;
    @Override
    public void runOpMode() {
        RF = hardwareMap.get(Servo.class, "right finger");
        LF = hardwareMap.get(Servo.class, "left finger");
        Left = hardwareMap.get(DcMotor.class, "left");
        Right = hardwareMap.get(DcMotor.class, "right");

        waitForStart();
        Driver driver = new DriverWithEncoder(Left,Right,2.5, 15.375);
        // run until the end of the match (driver presses STOP)
        Left.setDirection(DcMotor.Direction.REVERSE);
        //if you need to do keep it the right size comment 33/34 uncomment 35/36/38/39
        RF.setPosition(.5);
        LF.setPosition(.5);
        //RF.setPosition(.3);
        //LF.setPosition(.7);
        driver.forward(31,.5);
        //RF.setPosition(.5);
        //LF.setPosition(.5);
        driver.turn(90,.2);
        RF.setPosition(.3);
        LF.setPosition(.7);
        driver.forward(15,.5);
        while (opModeIsActive()) {
            driver.forward(-1,.5);
            driver.turn(40,.4);
            driver.forward(3,.5);
            driver.turn(-40,.4);
            driver.forward(3,.5);
            driver.turn(40,.4);
            driver.forward(-1,.5);
            driver.turn(-40,.4);
        }
    }
}
