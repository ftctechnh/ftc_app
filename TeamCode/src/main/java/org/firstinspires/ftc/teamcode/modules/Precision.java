package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;

public class Precision {
    private Telemetry debug;

    private Map<String, Integer> startingPos;
    private Map<String, Double> startingAngle;
    private Map<String, ElapsedTime> startTimer;

    public Precision() {
        startingPos = new HashMap<>();
        startingAngle = new HashMap<>();
        startTimer = new HashMap<>();
    }

    public Precision(Telemetry telemetry) {
        this();
        this.debug = telemetry;
    }

    public void reset() {
        startingPos = new HashMap<>();
        startingAngle = new HashMap<>();
        startTimer = new HashMap<>();
    }

    public boolean destinationReached(DcMotor motor, double maxPower, double minPower, int distance, double flatness, int marginOfError, int timeoutms) {
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
            motor.setPower(0);
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
            power *= -Math.abs(minPower - maxPower) / Math.pow(-Math.abs(distance), flatness);
            power += Math.abs(maxPower);
            power *= Math.signum(error);
            motor.setPower(power);
        }

        return false;
    }

    public boolean destinationReached(DcMotor[] motors, double maxPower, double minPower, int distance, double flatness, int marginOfError, int timeoutms) {
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
            if (canDebug()) {
                debug.addData("DistanceError", error);
            }
            error = (int)Math.signum(error) * Math.abs(distance);
        }

        if (timer.milliseconds() > timeoutms) {
            startTimer.remove(tag);
            for (DcMotor motor : motors) {
                startingPos.remove(motor.getDeviceName());
                motor.setPower(0);
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
        double power = Math.pow(Math.abs(distance)-Math.abs(error), flatness);
        power *= -Math.abs(minPower - maxPower) / Math.pow(-Math.abs(distance), flatness);
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

    public boolean angleTurned(DcMotor[] leftMotors, DcMotor[] rightMotors, double heading, double maxPower, double minPower, double target, double flatness, double marginOfError, int timeoutms) {
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

        if (canDebug()) {
            debug.addData("AngleError", error);
        }

        if (Math.abs(error) > Math.abs(target)) {
            error = Math.signum(error) * Math.abs(target);
        }

        if (timer.milliseconds() > timeoutms) {
            startingAngle.remove(tag);
            startTimer.remove(tag);
            for (DcMotor motor : motors) {
                motor.setPower(0);
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
        double power = Math.pow(Math.abs(target)-Math.abs(error), flatness);
        power *= -Math.abs(minPower - maxPower) / Math.pow(-Math.abs(target), flatness);
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

    public boolean distanceTurned(DcMotor[] leftMotors, DcMotor[] rightMotors, double maxPower, double minPower, int distance, double flatness, int marginOfError, int timeoutms) {
        int start;
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

        int distanceElapsed = 0;
        for (DcMotor motor : leftMotors) {
            if (!startingPos.containsKey(motor.getDeviceName())) {
                start = motor.getCurrentPosition();
                startingPos.put(motor.getDeviceName(), start);
            } else {
                start = startingPos.get(motor.getDeviceName());
            }

            distanceElapsed -= motor.getCurrentPosition() - start;
            tag += motor.getDeviceName();
        }

        for (DcMotor motor : rightMotors) {
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

        if (canDebug()) {
            debug.addData("DistanceError", error);
        }

        if (timer.milliseconds() > timeoutms) {
            startTimer.remove(tag);
            for (DcMotor motor : motors) {
                startingPos.remove(motor.getDeviceName());
                motor.setPower(0);
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
        double power = Math.pow(Math.abs(distance)-Math.abs(error), flatness);
        power *= -Math.abs(minPower - maxPower) / Math.pow(-Math.abs(distance), flatness);
        power += Math.abs(maxPower);
        power *= Math.signum(error);

        for (DcMotor motor : leftMotors) {
            if (Math.abs(error) < Math.abs(marginOfError)) {
                motor.setPower(0);
            }
            else {
                motor.setPower(-power);
            }
        }

        for (DcMotor motor : rightMotors) {
            if (Math.abs(error) < Math.abs(marginOfError)) {
                motor.setPower(0);
            }
            else {
                motor.setPower(power);
            }
        }

        return false;
    }

    private boolean canDebug() {
        return debug != null;
    }
}
