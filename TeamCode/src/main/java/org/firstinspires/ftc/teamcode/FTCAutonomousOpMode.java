package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This class is the main autonomous mode.
 */

@TeleOp(name="Autonomous OpMode", group="Iterative OpMode")
public class FTCAutonomousOpMode extends OpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    /* Declare Motors. */
    private DcMotor motorLeft = null; // left motor
    private DcMotor motorRight = null; // right motor
    private DcMotor motorCollector = null; // collector motor

    /* Declare Servos. */
    private Servo servoLeft = null; // left servo (pushes beacon button)

    /* Declare Sensors. */
    private ColorSensor sensorDown = null;
    private ColorSensor sensorLeft = null;
    private DeviceInterfaceModule cdim = null;
    private ModernRoboticsI2cGyro sensorGyro = null;

    /* Declare driving variables.  tbc=to be configured*/
    private static final double DRIVE_CENTER_BALL_WAIT = 3.0; // tbc; the amount of time to drive to the center
    private static final int DRIVE_BEACON_LINE_ESCAPE = 1000; // tbc: the amount of time to keep driving after hitting white line

    /*
     * Code to run when FTCTestOpMode object is initialized
     */
    public FTCAutonomousOpMode() {
        this.status("FTCTestOpMode initialization");
    }

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        this.motorLeft = hardwareMap.dcMotor.get(FTCInterface.LEFT_MOTOR); // get left motor
        this.motorRight = hardwareMap.dcMotor.get(FTCInterface.RIGHT_MOTOR); // get right motor
        this.motorCollector = hardwareMap.dcMotor.get(FTCInterface.COLLECTOR_MOTOR); // get collector motor
        this.sensorDown = hardwareMap.colorSensor.get(FTCInterface.DOWN_SENSOR); // get down sensor
        this.sensorLeft = hardwareMap.colorSensor.get(FTCInterface.LEFT_SENSOR); // get left sensor
        this.sensorGyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get(FTCInterface.GYRO_SENSOR); // get gyro sensor
        // this.servoLeft = hardwareMap.servo.get TODO get servo and see if it is servocontroller or servo class
        // Report status for debugging purposes
        this.status("init()");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        // Report status for debugging purposes
        this.status("init_loop()");
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        /*     [report status for debugging purposes]      */
        this.status("start()");
        runtime.reset();

        // TODO: Lower motor speed at first so that the robot won't slip.

        /*     [move Cap Ball off center]     */
        this.status("Mission: Moving cap ball off center"); // report mission
        this.resetMotors(); // no harm in resetting the motors first
        motorLeft.setPower(1.0); // full power on left motor
        motorRight.setPower(1.0); // full power on right motor
        try { // attempt to
            Thread.sleep((int) (FTCAutonomousOpMode.DRIVE_CENTER_BALL_WAIT * 1000)); // wait until arrival
        } catch(Exception e) { // on error
            this.status(e.toString()); // report error
        }
        motorLeft.setPower(-1.0); // full power backwards on left motor
        motorRight.setPower(-1.0); // full power backwards on right motor
        try { // attempt to
            Thread.sleep((int) (FTCAutonomousOpMode.DRIVE_CENTER_BALL_WAIT * 1000)); // wait until arrival
        } catch(Exception e) { // on error
            this.status(e.toString()); // report error
        }
        this.resetMotors(); // stop motors


        /*     [toggle Beacons if incorrect color]     */
        this.status("Mission: Toggling beacons");
        motorLeft.setPower(1.0); // full power on left motor
        motorRight.setPower(1.0); // full power on right motor
        while(!isColor(sensorDown.red(), sensorDown.green(), sensorDown.blue(), "white")); // wait until driving over white line
        try { // attempt to
            Thread.sleep(FTCAutonomousOpMode.DRIVE_BEACON_LINE_ESCAPE); // wait a bit of time
        } catch(Exception e) { // on error
            this.status(e.toString()); // report error
        }

        while(!isColor(sensorDown.red(), sensorDown.green(), sensorDown.blue(), "white")); // wait until driving over white line
        try { // attempt to
            Thread.sleep(FTCAutonomousOpMode.DRIVE_BEACON_LINE_ESCAPE); // wait a bit of time
        } catch(Exception e) { // on error
            this.status(e.toString()); // report error
        }

    }

    /*
     * Turn the vehicle x degrees
     */
    public void turnDegrees(double degrees) {
        // get motor power
        double motorLeftPower = motorLeft.getPower();
        double motorRightPower = motorRight.getPower();

        // reset motors
        this.resetMotors();

        // calibrate stuff
        sensorGyro.calibrate();
        while(sensorGyro.isCalibrating());

        double calculatedDegrees = sensorGyro.rawZ() + degrees;
        int multiplier = 1;

        if(degrees > 0)
            multiplier = 1;
        else if(degrees < 0)
            multiplier = -1;
        else
            multiplier = 0;

        motorLeft.setPower(multiplier * 1.0);
        motorRight.setPower((0 - multiplier) * 1.0);
        while(Math.abs((int)calculatedDegrees - sensorGyro.rawZ())  > FTCInterface.ANGLE_THRESHOLD); // while the distance to the destination is more than the threshold allowed
        motorLeft.setPower(motorLeftPower);
        motorRight.setPower(motorRightPower);
    }

    /*
     * Drive x feet forward
     */
    public void driveFeet(double feet) {
        double constant = 1.00; // convert feet to seconds

        this.resetMotors();
        motorLeft.setPower(1.0);
        motorRight.setPower(1.0);
        try {
            Thread.sleep((int)(feet * constant));
        } catch(Exception e) {
            this.status(e.toString()); // report error
        }
        this.resetMotors();
    }

    /*
     * Drive until white line
     */
    public void driveLine() {
        this.resetMotors();
        motorLeft.setPower(1.0);
        motorRight.setPower(1.0);
        while(!isColor(sensorDown.red(), sensorDown.green(), sensorDown.blue(), "white")); // wait until driving over white line
        this.resetMotors();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Check to make sure motors aren't running too fast
        this.checkMotor(motorLeft);
        this.checkMotor(motorRight);
        this.checkMotor(motorCollector);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        // Stop motors
        this.resetMotors();

        // Report status for debugging purposes
        this.status("stop()");
    }

    /*
     * Check motor to make sure it isn't running faster than it should
     */
    public void checkMotor(DcMotor motor) {
        if(motorLeft.getPower() > 1.0)
            motorLeft.setPower(1.0);
        if(motorLeft.getPower() < -1.0)
            motorLeft.setPower(-1.0);
    }

    /*
     * Reset motors by setting power to 0
     */
    public void resetMotors() {
        motorLeft.setPower(0);
        motorRight.setPower(0);
        motorCollector.setPower(0);
    }

    /*
     * Check if the color inputted is the same as the string
     */
    public boolean isColor(double r, double g, double b, String name) {
        name = name.toLowerCase(); // get the lower case version of the name

        switch(name) {
            case "red":
                if(r >= FTCInterface.COLOR_THRESHOLD)
                    return true; // it is red
                break;
            case "green":
                if(g >= FTCInterface.COLOR_THRESHOLD)
                    return true; // it is green
                break;
            case "blue":
                if(b >= FTCInterface.COLOR_THRESHOLD)
                    return true; // it is blue
                break;
            case "white":
                if(r > FTCInterface.COLOR_THRESHOLD && g > FTCInterface.COLOR_THRESHOLD && b > FTCInterface.COLOR_THRESHOLD)
                    return true;
                break;
            case "black":
                if(r < FTCInterface.COLOR_THRESHOLD && g < FTCInterface.COLOR_THRESHOLD && b < FTCInterface.COLOR_THRESHOLD)
                    return true;
                break;
            default:
                // status("Unknown color (" + r + ", " + g + ", " + b + ")."); // unknown color
                return false;
        }

        return false; // unknown color
    }

    /*
     * Display status of application
     */
    public void status(String status) {
        String name = (status.startsWith("Mission: ") ? "Mission" : "Status");
        if(status.startsWith("Mission: "))
            status = status.substring(status.indexOf("Mission: "));
        Log.i("Status", status);
        telemetry.addData("Status", status);
        updateTelemetry(telemetry);
    }

}