package org.firstinspires.ftc.teamcode.Qualifier;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ftc8045 on 10/22/2017.
 */

public class RelicArm {
    public Servo relicElbowServo;
    public Servo relicClawServo;
    public DcMotor relicArmMotor  = null;
    public double elbowtop = 1.0;
    public double elbowup = 0.35;
    public double elbowdown = 0.0;
    public int    relicArmMotorMin = 0;
    public int    relicArmMotorMax = 5000;


    public void init(HardwareMap hardwareMap) {
        relicClawServo = hardwareMap.servo.get("relic_claw");
        relicElbowServo = hardwareMap.servo.get("relic_elbow");
        //jawOpen();
        //jawClosed();

        relicClawServo.setPosition(0.4);//For tucket into the robot
        relicElbowServo.setPosition(0.0);

        relicArmMotor = hardwareMap.get(DcMotor.class, "relic_arm");
        relicArmMotor.setDirection(DcMotor.Direction.REVERSE);
        relicArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        relicArmMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void clawOpen() {
        relicClawServo.setPosition(0.6);
    }

    public void clawClose() {
        relicClawServo.setPosition(1.0);
    }

    public void elbowUp() {
        relicElbowServo.setPosition(elbowup);
    }//Close to level

    public void elbowDown() {
        relicElbowServo.setPosition(0.0);
    }







}
