package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by pston on 10/15/2017
 */

@TeleOp(name="Four Motor Test", group="Test")
public class FourMotorChasis extends OpMode {

    private double x;
    private double y;
    private double z;

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;

    private String LeftFront = "leftfront";
    private String RightFront = "rightfront";
    private String LeftRear = "leftrear";
    private String RightRear = "rightrear";

    public void setMotorPower(double x, double y, double z){
        /*
        Guide to motor Powers:
        Left Front: - (y + x + Z)
        Right Front: y - x - z
        Left Rear: y + x - z
        Right Rear: - (y - x + z)
         */
        this.leftFront.setPower(Range.clip(-(y+x+z),-1,1));
        this.rightFront.setPower(Range.clip((y-x-z),-1,1));
        this.leftRear.setPower(Range.clip(-(y-x+z),-1,1));
        this.rightRear.setPower(Range.clip((y+x-z), -1, 1));
    }

    @Override
    public void init() {
        leftFront = hardwareMap.dcMotor.get(LeftFront);
        rightFront = hardwareMap.dcMotor.get(RightFront);
        leftRear = hardwareMap.dcMotor.get(LeftRear);
        rightRear = hardwareMap.dcMotor.get(RightRear);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftRear.setPower(0);
        rightRear.setPower(0);
    }

    @Override
    public void loop() {
        x = gamepad1.left_stick_x;
        y = -gamepad1.left_stick_y;
        z = gamepad1.right_stick_x;

        setMotorPower(x, y, z);
    }
}
