package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class PIDControlledNeveRestMotor { //
    public static final int MAX_TICKS_PER_SECOND = 2500; // Assuming a poorly-maintained 40:1 motor
    // NeveRest 40:1 motors that are well-oiled and maintained will have max speeds of around
    // 2600-2700 TPS (found through testing on 7/1/2017)

    public double K = 0.0001;
    public double SECONDS_PER_REPEAT = 100.0;
    public double INTEGRAL_CONSTANT = K / SECONDS_PER_REPEAT;


    DcMotor m;
    NullbotHardware robot;
    double reset;
    int targetSpeed; // Between -MAX_TICKS_PER_SECOND and +MAX_TICKS_PER_SECOND
    int previousEncoderPosition;
    int previousError;
    double previousSpeed;
    int deltaEncoderTicks;


    public PIDControlledNeveRestMotor (DcMotor motor, NullbotHardware r) {
        robot = r;
        m = motor;
        targetSpeed = 0;
        reset = 0;
        previousEncoderPosition = 0;
        previousSpeed = 0;

    }
    public void recalculateIntegralConstant() {
        reset = 0;
        INTEGRAL_CONSTANT = K / SECONDS_PER_REPEAT;
    }
    public void recalculateMotorSpeed (double newTarget) {
        // Set up our target
        if (newTarget != targetSpeed) {
            reset = 0; // Reset the reset upon changing targets
            // Convert number from -1 to 1 into a speed in TPS
            targetSpeed = Math.round((int) (newTarget * ((double) MAX_TICKS_PER_SECOND)));
        }

        // Calculate current speed (in TPS)
        deltaEncoderTicks = m.getCurrentPosition() - previousEncoderPosition;
        int speed = robot.hz * deltaEncoderTicks;
        int error = targetSpeed - speed;

        reset += error * INTEGRAL_CONSTANT;

        // Determine encoder position
        double proportional = K * error;
        double derivative = INTEGRAL_CONSTANT * (error - (targetSpeed - previousSpeed));

        double power = robot.clamp(proportional + reset + derivative);
        // Ensure that the robot never crashes from a miscalibrated controller

        m.setPower(power);

        // Store away important values
        previousEncoderPosition = m.getCurrentPosition();
        previousError = error; // Used for logging
        previousSpeed = speed;
    }
}
