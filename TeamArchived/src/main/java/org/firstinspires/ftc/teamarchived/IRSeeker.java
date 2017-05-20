package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Ian Ramsey on 9/29/2015.
 */
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

public class IRSeeker extends OpMode {

    IrSeekerSensor irSeeker;
    DcMotor leftback;
    DcMotor leftfront;
    DcMotor rightback;
    DcMotor rightfront;

    public void init() {
        irSeeker = hardwareMap.irSeekerSensor.get("IR");
        leftback = hardwareMap.dcMotor.get("LeftBack");
        rightback = hardwareMap.dcMotor.get("RightBack");
        leftfront = hardwareMap.dcMotor.get("LeftFront");
        rightfront = hardwareMap.dcMotor.get("Rightfront");

    }
    public void loop() {
        if (irSeeker.signalDetected()) {
            telemetry.addData("Distance", irSeeker.getStrength());
            telemetry.addData("Angle", irSeeker.getAngle());
            double strength = irSeeker.getStrength();
            double angle = irSeeker.getAngle();

            if (strength < .5){
                if (angle != 0) {
                    if (angle > 0){
                        leftback.setPower(0.5);
                        leftfront.setPower(0.5);
                        rightback.setPower(0.25);
                        rightfront.setPower(0.25);
                    }
                    else {
                        leftback.setPower(0.25);
                        leftfront.setPower(0.25);
                        rightback.setPower(0.5);
                        rightfront.setPower(0.5);
                    }

                }
                else {
                    leftback.setPower(0.5);
                    leftfront.setPower(0.5);
                    rightback.setPower(0.5);
                    rightfront.setPower(0.5);
                }
            }
            }
        }
    }
