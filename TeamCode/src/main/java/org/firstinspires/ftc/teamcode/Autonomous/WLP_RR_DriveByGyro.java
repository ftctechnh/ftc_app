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

package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.TeleOp.WLP_MecanumWheels;

/**
 * This file illustrates the concept of driving a path based on Gyro heading and encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that you have a Modern Robotics I2C gyro with the name "gyro"
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *  This code requires that the drive Motors have been configured such that a positive
 *  power command moves them forward, and causes the encoders to count UP.
 *
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 *  In order to calibrate the Gyro correctly, the robot must remain stationary during calibration.
 *  This is performed when the INIT button is pressed on the Driver Station.
 *  This code assumes that the robot is stationary when the INIT button is pressed.
 *  If this is not the case, then the INIT should be performed again.
 *
 *  Note: in this example, all angles are referenced to the initial coordinate frame set during the
 *  the Gyro Calibration process, or whenever the program issues a resetZAxisIntegrator() call on the Gyro.
 *
 *  The angle of movement/rotation is assumed to be a standardized rotation around the robot Z axis,
 *  which means that a Positive rotation is Counter Clock Wise, looking down on the field.
 *  This is consistent with the FTC field coordinate conventions set out in the document:
 *  ftc_app\doc\tutorial\FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

public class WLP_RR_DriveByGyro {

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0;    // 4 inches = 10.16 cm
    static final double     WHEEL_DIAMETER_CM       =   (WHEEL_DIAMETER_INCHES * 2.54);

    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     COUNTS_PER_CM           = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_CM * 3.1415);


    // These constants define the desired driving/control characteristics
    // The can/should be tweaked to suite the specific robot drive train.

    static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable


    /* Declare OpMode members. */

    // Declare Drive Train modors
    protected DcMotor frontLeft = null;
    protected DcMotor frontRight = null;
    protected DcMotor rearLeft = null;
    protected DcMotor rearRight = null;
    protected WLP_MecanumWheels wheels = new WLP_MecanumWheels();

    ModernRoboticsI2cGyro   gyro    = null; // Additional Gyro device


    // Global variables to be initialized in init function
    private Telemetry telemetry = null;
    private HardwareMap hardwareMap = null;
    private boolean isInitialized = false;
    private WLP_RR_Autonomous parent = null;


    public WLP_RR_DriveByGyro() {
    }

    // Code to run ONCE when the driver hits INIT
    public void init(Telemetry telemetry, HardwareMap hardwareMap, WLP_RR_Autonomous parent) {


        // Initialize hardware devices passed from parent
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.parent = parent;

        gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");



        // Initialize motors.
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");


        // Set Motor Directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        rearLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        rearRight.setDirection(DcMotor.Direction.REVERSE);

        // Set autonomous mode
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // Send telemetry message to alert driver that we are calibrating;
        telemetry.addData("DriveByGyro", "Calibrating Gyro");
        telemetry.update();

        ElapsedTime calTime =  new ElapsedTime();

        gyro.calibrate();

        // make sure the gyro is calibrated before continuing
        while (gyro.isCalibrating())  {
            parent.sleep(50);
            parent.idle();
        }

        gyro.resetZAxisIntegrator();

        // Send telemetry message to alert driver that we are calibrating;
        telemetry.addData("DriveByGyro", "Gyro calibaration took " + calTime.toString());

        isInitialized = true;
        telemetry.addData("DriveByGyro", "Initialization succeeded");
        telemetry.update();
    }


   /**
    *  Move the robot straight forward or backward to a desired distance
    *  Move will stop if either of these conditions occur:
    *  1) Move gets to the desired position
    *  2) Driver stops the opmode running.
    *
    * @param speed      Target speed for forward motion.
    * @param distanceInCm   Distance (in cm) to move from current position.  Negative distance means move backwards.
    */

   public void moveStraight(double speed, double distanceInCm) {

       int newLeftTarget = 0;
       int newRightTarget = 0;
       int moveCounts;
       double max;
       double error;
       double steer;
       double leftSpeed;
       double rightSpeed;

       // Ensure that the opmode is still active
       if (parent.opModeIsActive()) {

           // Determine new target position, and pass to motor controller
           moveCounts = (int) (distanceInCm * COUNTS_PER_CM);
           adjustTargetPosition(moveCounts);
           setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

           // start motion.
           speed = Range.clip(Math.abs(speed), 0.0, 1.0);

           if (distanceInCm < 0.0) {
               speed = -1.0 * speed;
           }

           wheels.UpdateInput(0.0, speed, 0.0);

           // Set power using the wheel calculation
           setPower();

           // keep looping while we are still active, and BOTH motors are running.
           while (parent.opModeIsActive() && frontLeft.isBusy() && frontRight.isBusy());

           // Stop all motion;
           setPower(0.0);

           // Turn off RUN_TO_POSITION
           setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

       }
   }

    /**
     *  Method to spin on central axis to point in a new direction.
     *  Move will stop if either of these conditions occur:
     *  1) Move gets to the heading (angle)
     *  2) Driver stops the opmode running.
     *
     * @param speed Desired speed of turn.
     * @param angle      -180 to +180 Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     */
    public void gyroTurn ( double speed, double angle) {

        // keep looping while we are still active, and not on heading.

        // determine turn power based on +/- error
        double error = getError(angle);


        while (parent.opModeIsActive() && (Math.abs(error) > HEADING_THRESHOLD )) {

            double right_x = getSteer(error, P_TURN_COEFF);
            wheels.UpdateInput(0.0, 0.0, right_x);
            error = getError(angle);
            telemetry.addData("DriveByGyro::GyroTurn:error", "%.2f", error);
            setPower();
        }
        // Stop all motors
        setPower(0.0);
    }

    /**
     * getError determines the error between the target angle and the robot's current heading
     * @param   targetAngle  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - gyro.getIntegratedZValue();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }


    /**
     * returns desired steering force.  +/- 1 range.  +ve = steer left
     * @param error   Error angle in robot relative degrees
     * @param PCoeff  Proportional Gain Coefficient
     * @return
     */
    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }



    // Sets the runmode to all motoros
    private void setRunMode(DcMotor.RunMode mode) {
        frontLeft.setMode(mode);
        frontRight.setMode(mode);
        rearLeft.setMode(mode);
        rearRight.setMode(mode);
    }

    // Updates move counts to all motors
    private void adjustTargetPosition(int moveCounts) {
        frontLeft.setTargetPosition( frontLeft.getCurrentPosition() + moveCounts);
        frontRight.setTargetPosition( frontRight.getCurrentPosition() + moveCounts);
        rearLeft.setTargetPosition( rearLeft.getCurrentPosition() + moveCounts);
        rearRight.setTargetPosition( rearRight.getCurrentPosition() + moveCounts);

    }

    // Set power using mecanum wheel
    private void setPower() {
        frontLeft.setPower(wheels.getFrontLeftPower());
        frontRight.setPower(wheels.getFrontRightPower());
        rearRight.setPower(wheels.getRearRightPower());
        rearLeft.setPower(wheels.getRearLeftPower());
        updatePowerTelemetry();
    }

    //set specified power
    private void setPower(double power) {
        updatePowerTelemetry();
        frontLeft.setPower(power);
        frontRight.setPower(power);
        rearRight.setPower(power);
        rearLeft.setPower(power);
    }

    //Update Power Telemetry values
    private void updatePowerTelemetry() {

        // Display drive status for the driver.
        telemetry.addData("DriveByGyro::frontLeft", "%.2f", frontLeft.getPower());
        telemetry.addData("DriveByGyro::frontRight", "%.2f", frontRight.getPower());
        telemetry.addData("DriveByGyro::rearLeft", "%.2f", rearLeft.getPower());
        telemetry.addData("DriveByGyro::rearRight", "%.2f", rearRight.getPower());
        telemetry.update();
        parent.sleep(1000);
    }
}
