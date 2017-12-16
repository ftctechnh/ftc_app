package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by jxfio on 12/15/2017.
 */

public class BlueLeft extends LinearOpMode{
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
        RF.setPosition(.3);
        LF.setPosition(.7);
        driver.forward(34,.5);
        while (opModeIsActive()) {
            driver.forward(-1,.5);
            driver.turn(40,.4);
            driver.forward(3,.5);
            driver.turn(-40,.4);
        }
    }
}
