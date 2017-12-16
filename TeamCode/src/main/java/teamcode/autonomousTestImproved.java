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
package teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.sqrt;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="autonomousTestImproved", group="Linear Opmode")  // @Autonomous(...) is the other common choice
@Disabled
public class autonomousTestImproved extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motor1; // back motor
    private DcMotor motor2; // left motor
    private DcMotor motor3; // right motor
    private Servo jewelServo;
    private Servo armServo;
    private Servo leftClampServo;
    private Servo rightClampServo;
    ColorSensor sensorColor;
    DistanceSensor sensorDistance;

    int cooldown = 1000;
    private boolean isClamped = true;
    private double row1Position = 0.2;
    private double row2Position = 0.4;
    private double row3Position = 0.6;
    private BNO055IMU gyro;
    final private double OPENCLAMPPOSITION = 0;
    final private double CLOSECLAMPPOSITION = .5;
    final private double LIFTEDARMPOSITION = .55;

    final private double[] JEWEL_PHASE = {
        2.0, // close clamp
        0.1, // lift clamp
        0.5, // arm falls
        0.5, // turn
        1.0, // bring arm up
        0.5, // put arm away
    };

    final private double[] VIEW_PHASE = {
        0.5, // undo turn
        2.0, // detect picture
        2.0, // approach column
        0.1, // turn off motors
        0.5, // open clamp
        0.3, // turn to push cube in slot
        0.5, // back up
        0.1, // turn off motors
    };

    final private double CLAMPBLOCK = 2; // clamp block
    final private double LIFTARM = CLAMPBLOCK + 2; //lift arm
    final private double PHASETHREE = LIFTARM + 1.8; // drive forward
    final private double PHASETHREEHALF = PHASETHREE + .1; // turn off motors
    final private double PHASEFOUR = PHASETHREEHALF + .5; // lowerarm
    final private double PHASEFIVE = PHASEFOUR + .6; // turn to face columns
    final private double PHASESIX = PHASEFIVE + 2; // drive stright
    final private double PHASESIXHALF = PHASESIX + .1; // turn off motors
    final private double PHASESEVEN = PHASESIXHALF + .5; // open clamp
    final private double PHASEEIGHT = PHASESEVEN + .5; // turn
    final private double PHASENINE = PHASEEIGHT + .3; // back up
    final private double PHASENINEHALF = PHASENINE + .1; // turn off motors

    //turns off all motors at end


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = false;
        parameters.loggingTag          = "gyro";
        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        gyro = hardwareMap.get(BNO055IMU.class, "gyro");
        gyro.initialize(parameters);

        armServo = hardwareMap.get(Servo.class, "armServo");
        leftClampServo = hardwareMap.get(Servo.class, "leftClampServo");
        rightClampServo = hardwareMap.get(Servo.class, "rightClampServo");
        jewelServo = hardwareMap.get(Servo.class , "jewelServo");

        // get a reference to the color sensor.
        sensorColor = hardwareMap.get(ColorSensor.class, "colorDistanceSensor");
        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "colorDistanceSensor");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        motor1.setDirection(DcMotor.Direction.FORWARD);
        motor3.setDirection(DcMotor.Direction.FORWARD);
        motor2.setDirection(DcMotor.Direction.FORWARD);

        armServo.setDirection(Servo.Direction.FORWARD);
        jewelServo.setDirection(Servo.Direction.FORWARD);
        rightClampServo.setDirection(Servo.Direction.REVERSE);
        leftClampServo.setDirection(Servo.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        double elapsedTime;

        // run until the end of the match (driver presses STOP)
        boolean test = true;
        while (opModeIsActive())
        {

            if(test) {
                //assume red jewel is on left
                clamp(CLOSECLAMPPOSITION);
                stop(2);
                jewelServo.setPosition(1);
                stop(2);
                armServo.setPosition(.9);
                stop(.5);
                double speed = 0.4;
                if (isJewelRed()) {
                    // the red jewel is on the left of sensor
                    speed = -speed;
                }
                turn(speed);
                stop(.05);
                turnOffMotors();
                jewelServo.setPosition(.65);
                stop(2);
                jewelServo.setPosition(0);
                stop(2);
                turn(-speed);
                stop(.05);
                turnOffMotors();
                test = false;
                runtime.reset();
            }

            elapsedTime = runtime.time();
            if (elapsedTime < CLAMPBLOCK)
            {
                setClampPosition(CLOSECLAMPPOSITION);
            }
            else if (elapsedTime < LIFTARM) {
                armServo.setPosition(LIFTEDARMPOSITION);
            }
            else if(elapsedTime < PHASETHREE)
            {
                drive(0, .5);
            }
            else if(elapsedTime < PHASETHREEHALF)
            {
                turnOffMotors();
            }
            else if (elapsedTime < PHASEFOUR)
            {
                armServo.setPosition(.7);
            }
            else if (elapsedTime < PHASEFIVE)
            {
                turn(.5/2);
            }
            else if (elapsedTime < PHASESIX)
            {
                drive(0, .5);
            }
            else if (elapsedTime < PHASESIXHALF)
            {
                turnOffMotors();
            }
            else if (elapsedTime < PHASESEVEN)
            {
                setClampPosition(OPENCLAMPPOSITION);
            }
            else if (elapsedTime < PHASEEIGHT)
            {
                turn(.5/2);
            }
            else if (elapsedTime < PHASENINE)
            {
                drive(0,-.5);
            }
            else if (elapsedTime < PHASENINEHALF)
            {
                turnOffMotors();
            }
            else {
                turnOffMotors();
            }
            updateTelemetry();


        }
    }

    public void stop(double seconds) {
        double currentTime = runtime.time();
        double totalTime = currentTime + seconds;

        while(currentTime < totalTime) {
            currentTime = runtime.time();
        }
    }

    public void updateTelemetry(){
        //double armServoValue = armServo.getPosition();
        //double leftClampValue = leftClampServo.getPosition();
        //double rightClampValue = rightClampServo.getPosition();
        //boolean aButton = gamepad1.a;
        telemetry.addData("Status", "Run Time   : " + runtime.toString());
        telemetry.addData("left motor power", motor2.getPower());
        telemetry.addData("right motor power", motor3.getPower());

        telemetry.update();
    }

    //drive method that accepts two values, x and y motion
    public void drive(double x, double y)
    {

        // for precise movement
        //if (gamepad1.right_bumper) {
        //    scale = 0.5;
        //}


        //final float correctionZoneDegrees = 5;
        //AngularVelocity v = gyro.getAngularVelocity();
        //float v_x = v.xRotationRate; // positive is clockwise
        //double correctionValue = 0;
        //if (Math.abs(v_x) > correctionZoneDegrees)
        //{
            //correctionValue = (double)(v_x / 200.0);
        //}

        double power1 = x;
        double power2 = ((((-.5) * x) - (sqrt(3)/2) * y)); //- correctionValue;
        double power3 = ((((-.5) * x) + (sqrt(3)/2) * y)); //- correctionValue;

        motor1.setPower(power1);
        motor2.setPower(power2);
        motor3.setPower(power3);

    }

    private void turnOffMotors()
    {
        motor1.setPower(0);
        motor3.setPower(0);
        motor2.setPower(0);
    }

    private void turn(double speed)
    {
        motor1.setPower(-speed);
        motor3.setPower(-speed);
        motor2.setPower(-speed);
    }

    // takes in a position from 0 (open all the way) to .5 (closed)
    private void clamp(double position)
    {
        rightClampServo.setPosition(position);
        leftClampServo.setPosition(position);
    }

    private void moveServo(Servo servo, double amount)
    {
        double targetPosition = servo.getPosition() + amount;
        servo.setPosition(targetPosition);
    }

    private void setClampPosition(double newClampPosition)
    {
        rightClampServo.setPosition(newClampPosition);
        leftClampServo.setPosition(newClampPosition);
    }

    private Boolean isJewelRed()
    {
        double differenceFactor = 1.5;
        double red = sensorColor.red();
        double blue = sensorColor.blue();
        if(red >= 1.5 * blue)
        {
            return true;
        }
        return false;
    }


}
