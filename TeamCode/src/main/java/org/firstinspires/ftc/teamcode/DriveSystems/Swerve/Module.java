package org.firstinspires.ftc.teamcode.DriveSystems.Swerve;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Module {

    double error;
    DcMotor motor;
    CRServo servo;
    AnalogInput encoder;
    double maxVoltage;
    double offset;
    boolean reverse;

    Module(DcMotor motor, CRServo servo, AnalogInput encoder, double maxVoltage, double offset) {
        this.motor = motor;
        this.servo = servo;
        this.encoder = encoder;
        this.maxVoltage = maxVoltage;
        this.offset = offset;
    }

    public void singleInstance(double expectedAngle) {
        PIDCalculator c = new PIDCalculator(1, 0, 0);
        double zero = zeroModule(encoder.getVoltage(), offset, maxVoltage);
        double timeInSecconds = System.nanoTime() / 1e9;
        error = moreEffecient(mapAngle(zero, maxVoltage), expectedAngle);
        double log1 = c.getPID(error, timeInSecconds);
        servo.setPower(log1 * -1);
    }

    //turn voltage into radians
    public double mapAngle(double voltage, double MaxVoltage) {
        return voltage / MaxVoltage * 2 * Math.PI;
    }

    //zeros module
    public double zeroModule(double encoderVoltage, double offsetValue, double maxVoltage) {
        if (encoderVoltage >= offsetValue) {
            return encoderVoltage - offsetValue;
        } else {
            return encoderVoltage - offsetValue + maxVoltage;
        }
    }


    //normalize the angle
    public static final double TAU = Math.PI * 2;

    public static double norm(double angle) {
        // return angle %TAU - Math.PI;
        angle = angle % TAU;

        angle = (angle + TAU) % TAU;

        if (angle > Math.PI) {
            angle -= TAU;
        }
        return angle;

    }



    //finds error
    public double calculateError(double currentPos, double expectedPos) {
        return norm(expectedPos - currentPos);
    }

    public double calculateReverseError(double currentPos, double expectedPos) {
        return norm(((expectedPos + Math.PI) % (Math.PI * 2)) - currentPos);
    }




    double moreEffecient(double currentPos, double expectedPos) {
        double regError = calculateError(currentPos, expectedPos);
        double reverseError = calculateReverseError(currentPos, expectedPos);
        if (Math.abs(regError) < Math.abs(reverseError)) {
            reverse = true;
            return regError;
        } else {
            reverse = false;
            return reverseError;
        }
    }
    void runMotor(double gamepadValue) {
        if (reverse) {
            motor.setPower(gamepadValue * -1);
        } else {
            motor.setPower(gamepadValue);
        }
    }

}

