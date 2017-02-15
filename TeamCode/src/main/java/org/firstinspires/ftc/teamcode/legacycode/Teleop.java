package org.firstinspires.ftc.teamcode.legacycode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by 292486 on 5/17/2016.
 */
public class Teleop extends OpMode {

    private DcMotor fLeft, fRight, bLeft, bRight, lLift, rLift, intake;
    private DcMotor[] motors = {fLeft, fRight, bLeft, bRight, lLift, rLift, intake};
    private String[] motorNames = {"fLeft", "fRight", "bLeft", "bRight", "lLift", "rLift", "intake"};

    private double lSpeed, rSpeed;

    @Override
    public void init() {
        for(int i = 0; i < motors.length; i++){
            motors[i] = hardwareMap.dcMotor.get(motorNames[i]);
        }

        fLeft.setDirection(DcMotor.Direction.REVERSE);
        bLeft.setDirection(DcMotor.Direction.REVERSE);
        lLift.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        lSpeed = gamepad1.left_stick_y > 0.1 ? Math.pow(gamepad1.left_stick_y, 2) : 0;
        rSpeed = gamepad1.right_stick_y > 0.1 ? Math.pow(gamepad1.right_stick_y, 2) : 0;

        fLeft.setPower(lSpeed);
        bLeft.setPower(lSpeed);
        fRight.setPower(rSpeed);
        bRight.setPower(rSpeed);

        if(gamepad1.right_bumper){
            lLift.setPower(.5);
            rLift.setPower(.5);
        } else if(gamepad1.right_trigger > 0){
            lLift.setPower(-0.25);
            rLift.setPower(-0.25);
        } else {
            lLift.setPower(0);
            rLift.setPower(0);
        }

        if(gamepad1.a){
            intake.setPower(.5);
        } else if(gamepad1.b){
            intake.setPower(-.5);
        } else {
            intake.setPower(0);
        }
    }
}
