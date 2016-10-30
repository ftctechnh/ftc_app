package org.firstinspires.ftc.robotcontroller.internal.opmodes;

import android.text.style.LeadingMarginSpan;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;


@Autonomous(name="AutoOp", group ="Autonomous")
public class AutoOp extends LinearOpMode {
    //ColorSensor beacon;
    OpticalDistanceSensor floorRight, floorLeft;
    LegacyModule module;
    UltrasonicSensor distanceSensor;

    DcMotor left, right;


    @Override
    public void runOpMode() {

        //Initialization
        //beacon = hardwareMap.colorSensor.get("beacon");
        floorRight = hardwareMap.opticalDistanceSensor.get("floorRight");
        floorLeft = hardwareMap.opticalDistanceSensor.get("floorLeft");
        //beacon.enableLed(false);

        left = hardwareMap.dcMotor.get("2");
        right = hardwareMap.dcMotor.get("1");

        module = hardwareMap.legacyModule.get("module");
        distanceSensor = hardwareMap.ultrasonicSensor.get("distanceSensor");
        module.enable9v(5, true);

        double threshold = 0; //GIVE ME REAL VALUE

        // Move robot over to line

        //threshold += (floorLeft.getLightDetected() + floorRight.getLightDetected());
        //threshold += (floorLeft.getLightDetected() + floorRight.getLightDetected());
        //threshold += (floorLeft.getLightDetected() + floorRight.getLightDetected());
        //threshold += (floorLeft.getLightDetected() + floorRight.getLightDetected());
        //threshold += (floorLeft.getLightDetected() + floorRight.getLightDetected());

        //threshold = threshold / 10.0; // Does not truncate!
        //threshold = (floorLeft.getLightDetected() + floorRight.getLightDetected())/2;
        int distance = 10; // GIVE ME A NUMBA!!!!
        telemetry.addData("Movement", "none");


        while(distanceSensor.getUltrasonicLevel() > distance) {
            telemetry.addData("Left", floorLeft.getLightDetected());
            telemetry.addData("Right", floorRight.getLightDetected());
            telemetry.addData("Distance", distanceSensor.getUltrasonicLevel());
            telemetry.update();

            if(floorRight.getLightDetected() < floorLeft.getLightDetected()){
                telemetry.addData("Movement", "right");
                right.setPower(40);
                left.setPower(60);
            }
            else if(floorLeft.getLightDetected() < floorRight.getLightDetected()) {
                telemetry.addData("Movement", "left");
                left.setPower(40);
                right.setPower(60);
            }
            else {
                telemetry.addData("Movement", "straight");
                left.setPower(50);
                right.setPower(50);
            }

        }
        telemetry.addData("Movement", "stopped");

        //Check color

    }

}