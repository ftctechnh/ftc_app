/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode.VelocityVortex;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

public abstract class AutonomousBase extends RobotOpModes
{
    protected GyroSensor gyro;

    private final double PI = 3.14159265358979;
    private final float WHEEL_DIAMETER = 2;
    private final float WHEEL_RADIUS = WHEEL_DIAMETER / 2;
    private final float WHEEL_CIRCUM = (float)(WHEEL_DIAMETER * PI);

    private final float GYRO_REST_CONSTANT = 598;

    private final int ENCODER_COUNTS = 1440;

    private int frontLeftEncoder;
    private int backLeftEncoder;
    private int frontRightEncoder;
    private int backRightEncoder;

    protected float linearX;
    protected float linearY;
    protected float turnX;

    protected int encoderCounts;

    @Override
    public void init()
    {
        super.init();

        gyro = hardwareMap.gyroSensor.get("gyro");

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Don't override loop()

    @Override
    public void stop()
    {
        super.stop();
    }

    protected void driveTo(final float distance , final float heading , final float speed)
    {
        float averageMotion;

        // Fancy trig wow
        // It's extremely hard to explain with typing
        // Here, follow this link to a picture: http://imgur.com/5C0n5c2
        linearX = (float)(Math.cos(heading));
        linearY = (float)(Math.sin(heading));

        // Essentially recycled code from the teleop
        frontLeftPow = speed * (linearX + linearY);
        backLeftPow = speed * (linearX + linearY);
        frontRightPow = speed * (linearY - linearX);
        backRightPow = speed * (linearY - linearX);

        motorFrontLeft.setPower(frontLeftPow);
        motorBackLeft.setPower(backLeftPow);
        motorFrontRight.setPower(frontRightPow);
        motorBackRight.setPower(backRightPow);

        // With 45 degree rollers, the strafe distance on Mecanum wheels
        // is the same as normal forward and backward distance.
        // See, the problem is, the wheels move at varying speeds
        // so setting them to the same encoder count doesn't work
        // I averaged the speeds because it makes the most sense to me.
        // Somebody go write a proof on these things.
        averageMotion = (frontLeftPow + backLeftPow + frontRightPow + backRightPow) / 4;

        // We have distance passed in above, the speed calculated above, and the wheel diameters
        // Using the encoder ticks, we can set the encoder values, assuming the speed is averageMotion
        encoderCounts = (int)(distance / WHEEL_CIRCUM * ENCODER_COUNTS);

        // Adjust encoder counts based on motors' actual speeds
        // The faster the speed, the more encoder counts
        // The slower the speed, the less encoder counts
        // It's counter-intuitive but remember that we're trying to balance things here
        frontLeftEncoder = (int)(encoderCounts * frontLeftPow / averageMotion);
        backLeftEncoder = (int)(encoderCounts * backLeftPow / averageMotion);
        frontRightEncoder = (int)(encoderCounts * frontRightPow / averageMotion);
        backRightEncoder = (int)(encoderCounts * backRightEncoder / averageMotion);

        // We're finally setting the encoder values to the motors
        // Exciting!
        motorFrontLeft.setTargetPosition(frontLeftEncoder);
        motorBackLeft.setTargetPosition(backLeftEncoder);
        motorFrontRight.setTargetPosition(frontRightEncoder);
        motorBackRight.setTargetPosition(backRightEncoder);

        return;
    }

    protected void turn(final float heading , final float speed)
    {
        float rotation;

        float degreesTraveled = 0;

        long startTime = System.currentTimeMillis();
        long endTime;
        long loopTime;

        while(degreesTraveled <= Math.abs(heading - 90)) // The distance we need to travel is the heading - 90
        {
            if(heading > 90 && heading < 270) // If the robot's turn goes through the 2nd or 3rd quadrants
            {
                motorFrontLeft.setPower(-speed);
                motorBackLeft.setPower(speed);
                motorFrontRight.setPower(speed);
                motorBackRight.setPower(-speed);
            }
            else
            {
                motorFrontLeft.setPower(speed);
                motorBackLeft.setPower(-speed);
                motorFrontRight.setPower(-speed);
                motorBackRight.setPower(speed);
            }
            rotation = (float)(gyro.getRotationFraction() - GYRO_REST_CONSTANT);
            endTime = System.currentTimeMillis();
            loopTime = endTime - startTime;

            degreesTraveled += loopTime * rotation;
        }

    }
}
