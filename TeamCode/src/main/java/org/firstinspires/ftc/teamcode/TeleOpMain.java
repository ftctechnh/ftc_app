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
package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@TeleOp(name="TeleOpMain", group="DM")
// @Disabled

public class TeleOpMain extends OpMode{

    /* Declare OpMode members. */
    HardwareDM robot       = new HardwareDM(); // use the class created to define a robot hardware

    /* Shooter constants */
    static final int     COUNTS_PER_MOTOR_REV    = 7 ;    // Neverrest w/ BB 4:1

    static final int     NR_MAX_RPM              = 6600;
    static final int     SHOOT_MAX_RPM           = NR_MAX_RPM * COUNTS_PER_MOTOR_REV;

    static double           shootSpeed              = .925;
    static boolean          shootPressed            = false;

    boolean intakeIn = false;       // intake running forward
    boolean intakeOut = false;      // intake running backward
    boolean intakeInPressed = false;    // Is button pressed
    boolean intakeOutPressed = false;    // Is button pressed

    /* Servo positions and constants */
    double          beaconPos = robot.BEACON_HOME;
    double          pivotPos = robot.PIVOT_HOME;
    double          BEACON_SPEED = 0.1;
    double          PIVOT_SPEED = 0.1;

    boolean         pickupDeployed = false;     // Has the cap ball forks been released

    // State used for reading Gyro b
    Orientation angles;
    Acceleration gravity;

    // Variables used for reading and processing Adafruit color sensor
    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F,0F,0F};
    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;
    View relativeLayout;

    ElapsedTime pickupDeployTimer = new ElapsedTime();


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Setup max shooter motor speed limit
        robot.lShoot.setMaxSpeed(SHOOT_MAX_RPM);
        robot.rShoot.setMaxSpeed(SHOOT_MAX_RPM);


        //Setup for Adafruit RGB sensor
        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        relativeLayout = ((Activity) robot.hwMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);

        updateTelemetry(telemetry);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {


        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        double throttle = -gamepad1.left_stick_y;
        double direction = gamepad1.right_stick_x;

        if (pickupDeployed) {
            // Slow down the driving since we have a cap ball -- and reverse which is front of robot
            throttle = -1.0 * throttle;
            direction = -0.8 * direction;
        }

        double right = throttle - direction;
        double left = throttle + direction;


        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);



        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)smoothPowerCurve(deadzone(right,0.10));
        left = (float)smoothPowerCurve(deadzone(left,0.10));

        robot.lfDrive.setPower(left);
        robot.lrDrive.setPower(left);
        robot.rfDrive.setPower(right);
        robot.rrDrive.setPower(right);


        // Send telemetry message to signify robot running;
        //telemetry.addData("left",  "%.2f", left);
        //telemetry.addData("right", "%.2f", right);


        // Firing cam
        if (gamepad2.right_trigger <= 0.2) {
            robot.fire.setPower(0.0);
        } else if (gamepad2.right_trigger > 0.2) {
            robot.fire.setPower(1.0);
        }

        // Shooter flywheel on/off
        if (gamepad2.left_trigger <= 0.2) {
            robot.lShoot.setPower(0.0);
            robot.rShoot.setPower(0.0);
        } else if (gamepad2.left_trigger > 0.2) {
            robot.lShoot.setPower(shootSpeed);
            robot.rShoot.setPower(shootSpeed);
        }

        // Adjust shooter speed
        if (gamepad2.dpad_down && !shootPressed) {
            shootSpeed -= 0.005;
            shootPressed = true;
        } else if (gamepad2.dpad_up && !shootPressed) {
            shootSpeed += 0.005;
            shootPressed = true;
        }
        if (shootPressed && !gamepad2.dpad_down && !gamepad2.dpad_up) {
            // Reset since no shoot speed adjustment pressed
            shootPressed = false;
        }

        beaconPos = robot.beacon.getPosition();
        pivotPos = robot.pivot.getPosition();

        // Adjust beacon servo position
        if (gamepad1.right_bumper) {
            beaconPos = robot.BEACON_MAX_RANGE;
        } else beaconPos = robot.BEACON_MIN_RANGE;



        // Check if ready to release the cap pickup forks
        // BOTH drivers must push button together to make this happen
        if (gamepad1.left_bumper && gamepad2.left_bumper) {
            if (pickupDeployed == false) {
                // First time we are trying to deploy
                pickupDeployed = true;
                pickupDeployTimer.reset();      // Set timer of how long to wait
                // Insert code to actually move the servo here
            }
        }

        // Adjust lift's pivot servo position
        if (pickupDeployed == true && pickupDeployTimer.milliseconds() > robot.DEPLOY_WAIT) {
            // The cap ball lift mechanism is ready to go!
            if (gamepad2.left_stick_y < -0.2) {
                pivotPos = robot.PIVOT_MAX_RANGE;
            } else pivotPos = robot.PIVOT_MIN_RANGE;

            pivotPos = Range.clip(pivotPos, robot.PIVOT_MIN_RANGE, robot.PIVOT_MAX_RANGE);
            robot.pivot.setPosition(pivotPos);


            // Lift up and down
            if ((gamepad2.right_stick_y < -0.2) && (!robot.liftLimit.isPressed())) {
                robot.liftMotor.setPower(1.0);
            } else if (gamepad2.right_stick_y > 0.2) {
                robot.liftMotor.setPower(-0.20);
            } else robot.liftMotor.setPower(0.0);

        }


        beaconPos = Range.clip(beaconPos, robot.BEACON_MIN_RANGE, robot.BEACON_MAX_RANGE);
        robot.beacon.setPosition(beaconPos);

        pivotPos = Range.clip(pivotPos, robot.PIVOT_MIN_RANGE, robot.PIVOT_MAX_RANGE);



        shootSpeed = Range.clip(shootSpeed, 0.0, 1.0);
        telemetry.addData("Shoot", shootSpeed);
        //telemetry.addData("beaconPos", beaconPos);
        //telemetry.addData("pivotPos", pivotPos);


        // Ball intake on/off
        if (gamepad1.left_trigger > 0.2) {
            // Pressing intake reverse button
            if (!intakeOutPressed) {
                // Haven't read this button press yet
                intakeOutPressed = true;
                if (intakeOut) {
                    // Already running out so stop it
                    robot.intake.setPower(0.0);
                    intakeOut = false;
                    intakeIn = false;
                } else {
                    // Not already in reverse so set it so
                    robot.intake.setPower(-1.0);
                    intakeOut = true;
                    intakeIn = false;
                }
            }
        } else {
            // Intake reverse button is not pressed
            intakeOutPressed = false;
        }
        if (gamepad1.right_trigger > 0.2) {
            // Pressing intake forward button
            if (!intakeInPressed) {
                // Haven't read this button press yet
                intakeInPressed = true;
                if (intakeIn) {
                    // Already running out so stop it
                    robot.intake.setPower(0.0);
                    intakeOut = false;
                    intakeIn = false;
                } else {
                    // Not already in forward so set it so
                    robot.intake.setPower(1.0);
                    intakeOut = false;
                    intakeIn = true;
                }
            }
        } else {
            // Intake reverse button is not pressed
            intakeInPressed = false;
        }


        // Read and report heading
        angles   = robot.imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);

        telemetry.addData("heading", formatAngle(angles.angleUnit, angles.firstAngle) );


        // Read and report Adafruit RGB sensor

        // convert the RGB values to HSV values.
        Color.RGBToHSV((robot.beaconColor.red() * 255) / 800, (robot.beaconColor.green() * 255) / 800,
                (robot.beaconColor.blue() * 255) / 800, hsvValues);

        // send the info back to driver station using telemetry function.
        //telemetry.addData("Clear", robot.beaconColor.alpha());
        //telemetry.addData("Red  ", robot.beaconColor.red());
        //telemetry.addData("Green", robot.beaconColor.green());
        //telemetry.addData("Blue ", robot.beaconColor.blue());
        telemetry.addData("Hue", hsvValues[0]);

        updateTelemetry(telemetry);


    }


    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    /**
     * This does the cubic smoothing equation on joystick value.
     * Assumes you have already done any deadzone processing.
     *
     * @param x  joystick input
     * @return  smoothed value
     */
    protected double smoothPowerCurve (double x) {
        //double a = this.getThrottle();
        double a = 1.0;         // Hard code to max smoothing
        double b = 0.05;		// Min power to overcome motor stall

        if (x > 0.0)
            return (b + (1.0-b)*(a*x*x*x+(1.0-a)*x));

        else if (x<0.0)
            return (-b + (1.0-b)*(a*x*x*x+(1.0-a)*x));
        else return 0.0;
    }

    /**
     * Add deadzone to a stick value
     *
     * @param rawStick  Raw value from joystick read -1.0 to 1.0
     * @param dz	Deadzone value to use 0 to 0.999
     * @return		Value after deadzone processing
     */
    protected double deadzone(double rawStick, double dz) {
        double stick;

        // Force limit to -1.0 to 1.0
        if (rawStick > 1.0) {
            stick = 1.0;
        } else if (rawStick < -1.0) {
            stick = -1.0;
        } else {
            stick = rawStick;
        }

        // Check if value is inside the dead zone
        if (stick >= 0.0){
            if (Math.abs(stick) >= dz)
                return (stick - dz)/(1 -  dz);
            else
                return 0.0;

        }
        else {
            if (Math.abs(stick) >= dz)
                return (stick + dz)/(1 - dz);
            else
                return 0.0;

        }
    }

    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = robot.imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
            gravity  = robot.imu.getGravity();
        }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return robot.imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return robot.imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });


    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

}
