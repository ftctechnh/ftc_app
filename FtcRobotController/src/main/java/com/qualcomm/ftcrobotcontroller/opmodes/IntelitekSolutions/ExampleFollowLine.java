package com.qualcomm.ftcrobotcontroller.opmodes.IntelitekSolutions;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

public class ExampleFollowLine extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    OpticalDistanceSensor opticalDistanceSensor;

    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
    }

    @Override
    public void loop() {
        // Write the reflectance detected to a variable
        double reflectance = opticalDistanceSensor.getLightDetected();

        // If the sensor is on the line
        // only the right motor rotates to move it off the line
        if (reflectance >= 0.25) {
            rightMotor.setPower(-0.2);
            leftMotor.setPower(0);
       }
        // Otherwise (if the sensor is off the line)
        // only the left motor rotates to move it back toward the line
        else {
              leftMotor.setPower(-0.2);
              rightMotor.setPower(0);
            telemetry.addData("Reflectance", reflectance);
        }
    }
}


