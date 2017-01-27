package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;

public class Precision {
    private Precision() {

    }

    private static Map<String, Integer> startingPos = new HashMap<>();
    private static Map<String, Double> startingAngle = new HashMap<>();
    private static Map<String, ElapsedTime> startTimer = new HashMap<>();

    public static void reset() {
        startingPos = new HashMap<>();
        startingAngle = new HashMap<>();
        startTimer = new HashMap<>();
    }

    public static boolean destinationReached(DcMotor motor, double maxPower, double minPower, int distance, double flatness, int marginOfError, int timeoutms) {
        int start;
        ElapsedTime timer;

        if (!startingPos.containsKey(motor.getDeviceName())) {
            start = motor.getCurrentPosition();
            startingPos.put(motor.getDeviceName(), start);
            timer = new ElapsedTime();
            timer.reset();
            startTimer.put(motor.getDeviceName(), timer);
        }
        else {
            start = startingPos.get(motor.getDeviceName());
            timer = startTimer.get(motor.getDeviceName());
        }

        int error = distance - (motor.getCurrentPosition() - start);
        if (Math.abs(error) > Math.abs(distance)) {
            error = (int)Math.signum(error) * Math.abs(distance);
        }

        if (timer.milliseconds() > timeoutms) {
            startingPos.remove(motor.getDeviceName());
            startTimer.remove(motor.getDeviceName());
            return true;
        }


        if (Math.abs(error) < Math.abs(marginOfError)) {
            motor.setPower(0);
        }
        else {
            // y = a(x-h)^n + k
            // a = (y-k)/(x-h)^n
            // power = y
            // x = error
            // k = maxPower
            // h = distance
            // a = (minPower-maxPower)/(0-distance)^flatness
            distance -= Math.signum(distance)*marginOfError;
            double power = Math.pow(Math.abs(error - distance), flatness);
            power *= -Math.abs(minPower - maxPower) / Math.pow(-distance, flatness);
            power += Math.abs(maxPower);
            power *= Math.signum(error);
            motor.setPower(power);
        }

        return false;
    }

    public static boolean destinationReached(DcMotor[] motors, double maxPower, double minPower, int distance, double flatness, int marginOfError, int timeoutms) {
        int start;
        ElapsedTime timer;

        String tag = "";

        int distanceElapsed = 0;
        for (DcMotor motor : motors) {
            if (!startingPos.containsKey(motor.getDeviceName())) {
                start = motor.getCurrentPosition();
                startingPos.put(motor.getDeviceName(), start);
            } else {
                start = startingPos.get(motor.getDeviceName());
            }

            distanceElapsed += motor.getCurrentPosition() - start;
            tag += motor.getDeviceName();
        }

        distanceElapsed /= motors.length;

        if (!startTimer.containsKey(tag)) {
            timer = new ElapsedTime();
            timer.reset();
            startTimer.put(tag, timer);
        }
        else {
            timer = startTimer.get(tag);
        }

        int error = distance - distanceElapsed;
        if (Math.abs(error) > Math.abs(distance)) {
            error = (int)Math.signum(error) * Math.abs(distance);
        }

        if (timer.milliseconds() > timeoutms) {
            for (DcMotor motor : motors) {
                startingPos.remove(motor.getDeviceName());
                startTimer.remove(motor.getDeviceName());
            }
            return true;
        }

        // y = a(x-h)^n + k
        // a = (y-k)/(x-h)^n
        // power = y
        // x = error
        // k = maxPower
        // h = distance
        // a = (minPower-maxPower)/(0-distance)^flatness
        distance -= Math.signum(distance)*marginOfError;
        double power = Math.pow(Math.abs(error - distance), flatness);
        power *= -Math.abs(minPower - maxPower) / Math.pow(-distance, flatness);
        power += Math.abs(maxPower);
        power *= Math.signum(error);

        for (DcMotor motor : motors) {
            if (Math.abs(error) < Math.abs(marginOfError)) {
                motor.setPower(0);
            }
            else {
                motor.setPower(power);
            }
        }

        return false;
    }

    public static boolean angleTurned(DcMotor[] leftMotors, DcMotor[] rightMotors, double heading, double maxPower, double minPower, double target, double flatness, double marginOfError, int timeoutms) {
        double start;
        ElapsedTime timer;

        String tag = "";

        DcMotor[] motors = new DcMotor[leftMotors.length + rightMotors.length];
        int motorIndex = 0;

        for (DcMotor motor : leftMotors) {
            motors[motorIndex++] = motor;
        }
        for (DcMotor motor : rightMotors) {
            motors[motorIndex++] = motor;
        }

        for (DcMotor motor : motors) {
            tag += motor.getDeviceName();
        }

        if (!startingAngle.containsKey(tag)) {
            start = heading;
            startingAngle.put(tag, start);
            timer = new ElapsedTime();
            timer.reset();
            startTimer.put(tag, timer);

        }
        else {
            start = startingAngle.get(tag);
            timer = startTimer.get(tag);
        }
        double angle = (heading - start)%360.0;
        double otherAngle = 360 - angle;
        if (otherAngle < angle) {
            angle = -otherAngle;
        }

        double error = (target - angle)%360.0;
        double otherError = 360 - error;
        if (otherError < error) {
            error = -otherError;
        }

        if (Math.abs(error) > Math.abs(target)) {
            error = Math.signum(error) * Math.abs(target);
        }

        if (timer.milliseconds() > timeoutms) {
            for (DcMotor motor : motors) {
                startingAngle.remove(motor.getDeviceName());
                startTimer.remove(motor.getDeviceName());
            }
            return true;
        }

        // y = a(x-h)^n + k
        // a = (y-k)/(x-h)^n
        // power = y
        // x = error
        // k = maxPower
        // h = distance
        // a = (minPower-maxPower)/(0-distance)^flatness
        target -= Math.signum(target)*marginOfError;
        double power = Math.pow(error - target, flatness);
        power *= -Math.abs(minPower - maxPower) / Math.pow(-target, flatness);
        power += Math.abs(maxPower);
        power *= Math.signum(error);

        for (DcMotor motor : leftMotors) {
            if (Math.abs(error) < Math.abs(marginOfError)) {
                motor.setPower(0);
            }
            else {
                motor.setPower(power);
            }
        }

        for (DcMotor motor : rightMotors) {
            if (Math.abs(error) < Math.abs(marginOfError)) {
                motor.setPower(0);
            }
            else {
                motor.setPower(-power);
            }
        }

        return false;
    }
}
