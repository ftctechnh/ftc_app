/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Functions extends LinearOpMode {
    static int average;

    static DcMotor rFmotor, rBmotor, lFmotor, lBmotor;
    static GyroSensor gyro;
    static ColorSensor lineColor;
    public static boolean pusherDown;
    public static double pusherDownPos =1, pusherUpPos = 0, liftDownPos = .75, liftUpPos = 0 , sideWallUpPos = 0, sideWallDownPos = .9 ;
    static int redThreshold = 50;
    static int blueThreshold =  80;
    static int blueAmbientThreshold = 10;
    static int redAmbientThreshold = 10;
    static int differentialThreshold = 10;
    static double adjustmentSpeed = 0.2;
    public void initHardware() {
        rFmotor = hardwareMap.dcMotor.get("rF");
        rBmotor = hardwareMap.dcMotor.get("rB");
        lFmotor = hardwareMap.dcMotor.get("lF");
        lBmotor = hardwareMap.dcMotor.get("lB");
        //sweeper = hardwareMap.dcMotor.get("sweeper");
        lBmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lFmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rBmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rBmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rFmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rBmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        lBmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        lFmotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public static void waitFor(int mill) {
        try {
            Thread.sleep(mill);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    public static double convertGamepad(float y) {
        int m;
        if (y < 0) {
            m = 1;
        } else {
            m = -1;
        }
        return m * (1 - Math.sqrt(1 - (y * y)));
    }

    public void stopAll() {
        lFmotor.setPower(0);
        lBmotor.setPower(0);
        rFmotor.setPower(0);
        rBmotor.setPower(0);
    }

    public void mecanumDrive(double fwdPower, double strafePower, double rotationPower) {

        lFmotor.setPower(fwdPower + strafePower + rotationPower);
        lBmotor.setPower(fwdPower - strafePower + rotationPower);
        rFmotor.setPower(fwdPower - strafePower - rotationPower);
        rBmotor.setPower(fwdPower + strafePower - rotationPower);
    }

    public void moveFwd(double power) {
        lFmotor.setPower(power);
        lBmotor.setPower(power);
        rFmotor.setPower(power);
        rBmotor.setPower(power);
    }

    public void moveBkwd(double power) {
        lFmotor.setPower(-power);
        lBmotor.setPower(-power);
        rFmotor.setPower(-power);
        rBmotor.setPower(-power);
    }

    public void rotateCW(double power) {
        lFmotor.setPower(power);
        lBmotor.setPower(power);
        rFmotor.setPower(-power);
        rBmotor.setPower(-power);
    }

    public void rotateCCW(double power) {
        lFmotor.setPower(-power);
        lBmotor.setPower(-power);
        rFmotor.setPower(power);
        rBmotor.setPower(power);
    }

    public void strafeLeft(double power) {
        lFmotor.setPower(power);
        lBmotor.setPower(-power);
        rFmotor.setPower(-power);
        rBmotor.setPower(power);
    }

    public void strafeRight(double power) {
        lFmotor.setPower(-power);
        lBmotor.setPower(power);
        rFmotor.setPower(power);
        rBmotor.setPower(-power);
    }

    public void moveTime(String direction, double power, int mill) {
        if (direction == "fwd") {
            moveFwd(power);
        } else if (direction == "bkwd") {
            moveBkwd(power);
        } else if (direction == "rCCW") {
            rotateCCW(power);
        } else if (direction == "rCW") {
            rotateCW(power);
        } else if (direction == "sL") {
            strafeLeft(power);
        } else if (direction == "sR") {
            strafeRight(power);
        }
        waitFor(mill);
        stopAll();
    }

    public void moveEncoder(String direction, double power, int rotations) {
        rFmotor.setTargetPosition(rotations);
        lFmotor.setTargetPosition(rotations);
        rBmotor.setTargetPosition(rotations);
        lBmotor.setTargetPosition(rotations);
        if (direction == "fwd") {
            moveFwd(power);
        } else if (direction == "bkwd") {
            moveBkwd(power);
        } else if (direction == "rCCW") {
            rotateCCW(power);
        } else if (direction == "rCW") {
            rotateCW(power);
        } else if (direction == "sL") {
            strafeLeft(power);
        } else if (direction == "sR") {
            strafeRight(power);
        }
        while (rFmotor.getCurrentPosition() <= rFmotor.getTargetPosition()
                && lFmotor.getCurrentPosition() <= rFmotor.getTargetPosition()
                && rBmotor.getCurrentPosition() <= rFmotor.getTargetPosition()
                && lFmotor.getCurrentPosition() <= rFmotor.getTargetPosition()) {
            waitFor(10);
        }
        stopAll();
    }

    public void rotateCWGyro(int degrees, double power, int timeout, Telemetry telemetry) {
        int start = gyro.getHeading();
        int heading;
        long endtime = System.currentTimeMillis() + (timeout * 1000);
        int protectedValue = start - 20;
        int target = start + degrees;
        if (target >= 360) {
            target = target - 360;
        }
        if (protectedValue < 0) {
            protectedValue = protectedValue + 360;
        } else if (start < target) {
            protectedValue = 360;
        }
        telemetry.clear();
        telemetry.update();
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protected Value", protectedValue);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);
        telemetry.update();

        waitFor(1000);
        rotateCW(power);
        heading = gyro.getHeading();
        if (heading > protectedValue) {
            heading = heading - 360;
        }
        //while ((heading < target) && (System.currentTimeMillis() < endtime)) {
        while (heading < target) {
            heading = gyro.getHeading();
            if (heading > protectedValue) {
                heading = heading - 360;
            }

            telemetry.addData("Start", start);
            telemetry.addData("Heading", gyro.getHeading());
            telemetry.addData("Target", target);
            telemetry.addData("Protected Value", protectedValue);
            telemetry.update();
        }

        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protect", protectedValue);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);

        telemetry.update();
    }

    public void rotateCCWGyro(int degrees, double power, int timeout, Telemetry telemetry) {
        int start = gyro.getHeading();
        int heading = start;
        long endtime = System.currentTimeMillis() + (timeout * 1000);
        int protectedValue = start + 20;
        int target = start + degrees;
        if (target >= 360) {
            target = target - 360;
        }
        if (protectedValue >= 360) {
            protectedValue = protectedValue - 360;
        } else if (target < start) {
            protectedValue = -1;
        }
        rotateCCW(power);
        while ((heading > target) && (System.currentTimeMillis() < endtime)) {
            heading = gyro.getHeading();
            if (heading < protectedValue) {
                heading = heading + 360;
            }

            telemetry.addData("Start", start);
            telemetry.addData("Heading", gyro.getHeading());
            telemetry.addData("Target", target);
            telemetry.addData("Protect", protectedValue);
            telemetry.update();
        }
        stopAll();
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protect", protectedValue);
        telemetry.update();
    }

    public void debugColor(Telemetry telemetry) {
        while (System.currentTimeMillis()
                < System.currentTimeMillis() + 10000000000L) {
            telemetry.addData("Alpha", lineColor.alpha());
            telemetry.addData("ARGB", lineColor.argb());
            telemetry.addData("Red", lineColor.red());
            telemetry.addData("Green", lineColor.green());
            telemetry.addData("Blue", lineColor.blue());
            telemetry.update();
            waitFor(500);
        }
    }

    public boolean detectWhite(Telemetry telemetry) {
        int red = lineColor.red();
        int green = lineColor.green();
        int blue = lineColor.blue();
        double average = (red + green + blue) / 3.0;
        //if(average > 15 /*+ Functions.colorError && red >= average-Functions.colorError && red <= average+Functions.colorError && green >= average-Functions.colorError && green <= average+Functions.colorError && blue >= average-Functions.colorError && blue <= average+Functions.colorError*/) {
        if (red > 10 && blue > 10 && green > 10) {
            /*telemetry.addData("Red", red);
            telemetry.addData("Blue" , blue);
            telemetry.addData("Green", green);
            telemetry.addData("Average" , average);
            telemetry.addData("Debug", "Success");
            telemetry.update();*/
            return true;
        }
        /*telemetry.addData("Red", red);
        telemetry.addData("Blue", blue);
        telemetry.addData("Green", green);
        telemetry.update();*/
        return false;
    }

    public void stopAtWhite(double power, long timeout, Telemetry telemetry) {
        moveFwd(power);
        long endTime = System.currentTimeMillis() + (timeout * 1000);
        while (System.currentTimeMillis() < endTime) {
            if (detectWhite(telemetry)) {
                break;
            }
        }
        stopAll();
        waitFor(5000);
    }
    public static boolean detectBlue(ColorSensor colorsensor) {
        int blue = colorsensor.blue();
        return blue > 5;
    }
    public static boolean detectRed(ColorSensor colorsensor) {
        int red = colorsensor.red();
        return red>5;
    }
    public static boolean detectWhite(ColorSensor colorsensor) {
        int red = colorsensor.red();
        int green = colorsensor.green();
        int blue = colorsensor.blue();
        average = (red + green + blue) / 3;
        return (average > 8);
    }
}


