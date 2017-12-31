/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.*;

public class WLP_MecanumWheels {

    final private int FrontLeft = 0;
    final private int FrontRight = 1;
    final private int RearLeft = 2;
    final private int RearRight = 3;

    private double left_x;
    private double left_y;
    private double right_x;

    double[] wheelPowers = new double[4];

    // private
    private WLP_MecanumWheels() {
        throw new AssertionError();
    }

    private void CalclulatePower() {

        double r = Math.hypot(left_x, left_y);
        double robotAngle = Math.atan2(left_y, left_x) - Math.PI / 4;
        double rightX = right_x;

        wheelPowers[FrontLeft] = r * Math.cos(robotAngle) + rightX;
        wheelPowers[FrontRight] = r * Math.sin(robotAngle) - rightX;
        wheelPowers[RearLeft] = r * Math.sin(robotAngle) + rightX;
        wheelPowers[RearRight] = r * Math.cos(robotAngle) - rightX;

        // Adjust maximum power from -1.0 to 1.0
        double absMax = 0.0;
        for (int i = 0; i < wheelPowers.length; i++) {
            if (Math.abs(wheelPowers[i]) > absMax) {
                absMax = Math.abs(wheelPowers[i]);
            }
        }

        if (absMax > 1.0) {
            for (int i = 0; i < wheelPowers.length; i++) {
                wheelPowers[i] = wheelPowers[i] / absMax;
            }
        }
    }

    public void UpdateInput(double left_x, double left_y, double right_x) {
        this.left_x = left_x;
        this.left_y = left_y;
        this.right_x = right_x;
        CalclulatePower();
    }

    // The only constructors
    WLP_MecanumWheels(double left_x, double left_y, double right_x) {
        UpdateInput(left_x, left_y, right_x);
    }

    public double getFrontLeftPower() {
        return wheelPowers[FrontLeft];
    }

    public double getFrontRighttPower() {
        return wheelPowers[FrontRight];
    }

    public double getRearLeftPower() {
        return wheelPowers[RearLeft];
    }

    public double getRearRightPower() {
        return wheelPowers[RearRight];
    }

    public void print(Boolean header) {
        if (header) {
            System.out.printf("left_x, left_y, right_x, FrontLeft, FrontRight, RearLeft, RearRight%n");
        }
        System.out.printf("%6.2f, %6.2f, %7.2f, %9.2f, %10.2f, %8.2f, %9.2f%n", left_x, left_y, right_x,
                wheelPowers[FrontLeft], wheelPowers[FrontRight], wheelPowers[RearLeft], wheelPowers[RearRight]);
    }

    public static void main(String[] args) {
        double JoyPos[] = { 1.0, 0.0, -1.0 };
        //double JoyPos[] = { 1.0, 0.75, 0.5, 0.25, 0.0, -0.5, -0.75, -1.0 };
        WLP_MecanumWheels dt = new WLP_MecanumWheels(0, 0, 0);
        dt.print(true);

        for (int left_x = 0; left_x < JoyPos.length; left_x++) {
            for (int left_y = 0; left_y < JoyPos.length; left_y++) {
                for (int right_x = 0; right_x < JoyPos.length; right_x++) {
                    dt.UpdateInput(JoyPos[left_x], JoyPos[left_y], JoyPos[right_x]);
                    dt.print(false);
                }
            }
        }
    }

}