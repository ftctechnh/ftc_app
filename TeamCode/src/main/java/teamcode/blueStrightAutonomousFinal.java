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

import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;

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

@Autonomous(name="blueStrightAutonomousFinal", group="Linear Opmode")  // @Autonomous(...) is the other common choice
@Disabled
public class blueStrightAutonomousFinal extends LinearOpMode {

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
    final private double CLOSECLAMPPOSITION = 1;
    final private double LIFTEDARMPOSITION = .55;
    final private double DOWNARMPOSITION = .8;

    final private double JEWELCLOSECLAMP = .05; // close clamp
    final private double JEWELCHOPTIME = JEWELCLOSECLAMP + 1; // jewel servo down
    final private double JEWELARMRAISE = JEWELCHOPTIME + .5; // arm servo up
    final private double SPINTOWIN = JEWELARMRAISE + .1; // turn to knock off jewel
    final private double JEWELSHEATHARM = SPINTOWIN + 2; // raise arm to sheath
    final private double JEWELSTOREARM = JEWELSHEATHARM + .5; // store arm
    final private double JEWELSPINBACK = JEWELSTOREARM + .1; // turn back to compensate for knock off jewel turn
    final private double LIFTARM = JEWELSPINBACK + 2; //lift arm

    final private double TURNTOLINEUPWITHCOLUMNS = LIFTARM + 1.3; // turn on face columns
    final private double DRIVETOWARDSCOLUMNS = TURNTOLINEUPWITHCOLUMNS + 1; // forward

    final private double LATERALLINEUPWITHCOLUMN = DRIVETOWARDSCOLUMNS + .7; // turn off motor

    final private double TURNOFFMOTORS = LATERALLINEUPWITHCOLUMN + .1; // turn off motor
    final private double PHASETHREEHALFHALF = TURNOFFMOTORS + .1; // lower arm
    final private double PHASEFOUR = PHASETHREEHALFHALF + 1.5; // drive forward
    final private double PHASEFIVE = PHASEFOUR + .5; // open clamp
    final private double PHASEFIVEHALF = PHASEFIVE + .1; //stop motors
    final private double PHASESIX = PHASEFIVEHALF + .4; //turn
    final private double PHASESEVEN = PHASESIX + .4; // back up
    final private double PHASESEVENHALF = PHASESEVEN + .1; // turn off motors

    final private double TURNTOWARDSGLYPHPIT = PHASESEVENHALF + .6; // turn towards glyph pit
    final private double DRIVETOGLYPHPIT = TURNTOWARDSGLYPHPIT + 2.4; // drive to the glyph pit
    final private double GRABABLOCK = DRIVETOGLYPHPIT + 1.5; // grab a block in the pit
    final private double BACKTOBASE = GRABABLOCK + 2.1; // grab a block in the pit
    final private double TURNTOFACECOLUMNS = BACKTOBASE + .6; // turn to face columns
    //turns off all motors at end


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");

        motor1.setDirection(DcMotor.Direction.FORWARD);
        motor2.setDirection(DcMotor.Direction.FORWARD);
        motor3.setDirection(DcMotor.Direction.FORWARD);

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

        jewelServo = hardwareMap.get(Servo.class, "jewelServo");
        armServo = hardwareMap.get(Servo.class, "armServo");
        leftClampServo = hardwareMap.get(Servo.class, "leftClampServo");
        rightClampServo = hardwareMap.get(Servo.class, "rightClampServo");

        // get a reference to the color sensor.
        sensorColor = hardwareMap.get(ColorSensor.class, "colorDistanceSensor");
        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "colorDistanceSensor");

        jewelServo.setDirection(Servo.Direction.FORWARD);
        armServo.setDirection(Servo.Direction.FORWARD);
        rightClampServo.setDirection(Servo.Direction.REVERSE);
        leftClampServo.setDirection(Servo.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        double elapsedTime;

        // run until the end of the match (driver presses STOP)
        boolean test = true;
        double speed = .8;
        boolean isDetected = false;
        while (opModeIsActive())
        {

            /*if(test) {
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
                stop(.15);
                turnOffMotors();
                jewelServo.setPosition(.65);
                stop(2);
                jewelServo.setPosition(0);
                stop(2);
                turn(-speed);
                stop(.2);
                turnOffMotors();
                test = false;
                runtime.reset();
            }*/
            elapsedTime = runtime.time();
            if (elapsedTime < JEWELCLOSECLAMP)
            {
                clamp(CLOSECLAMPPOSITION);
            }
            else if (elapsedTime < JEWELCHOPTIME)
            {
                jewelServo.setPosition(1);
            }
            else if (elapsedTime < JEWELARMRAISE)
            {
                armServo.setPosition(.9);
            }
            else if (elapsedTime < SPINTOWIN)
            {
                if (isJewelRed()&& !isDetected) {
                    // the red jewel is on the left of sensor
                    speed = -speed;
                    isDetected = !isDetected;
                }
                turn(speed);
            }
            else if (elapsedTime < JEWELSHEATHARM)
            {
                turnOffMotors();
                jewelServo.setPosition(.65);
            }
            else if (elapsedTime < JEWELSTOREARM)
            {
                jewelServo.setPosition(0);
            }
            else if (elapsedTime < JEWELSPINBACK)
            {
                turn(-speed);
            }
            else if (elapsedTime < LIFTARM) {
                turnOffMotors();
                armServo.setPosition(LIFTEDARMPOSITION);
            }
            else if (elapsedTime < TURNTOLINEUPWITHCOLUMNS)
            {
                turn(-0.5);
            }
            else if(elapsedTime < DRIVETOWARDSCOLUMNS)
            {
                drive(0, .5);
            }
            else if (elapsedTime < LATERALLINEUPWITHCOLUMN)
            {
                drive(.5,0);
            }
            else if(elapsedTime < TURNOFFMOTORS)
            {
                turnOffMotors();
            }
            else if (elapsedTime < PHASETHREEHALFHALF)
            {
                armServo.setPosition(.75);
            }
            else if (elapsedTime < PHASEFOUR)
            {
                drive(0, 0.5);
            }
            else if (elapsedTime < PHASEFIVE)
            {
                turnOffMotors();
                setClampPosition(OPENCLAMPPOSITION);
            }
            else if (elapsedTime < PHASEFIVEHALF)
            {
                turnOffMotors();
            }
            else if (elapsedTime < PHASESIX)
            {
                turn(-.5);
            }
            else if (elapsedTime < PHASESEVEN)
            {
                drive(0,-.5);
            }
            else if (elapsedTime < PHASESEVENHALF)
            {
                turnOffMotors();
            }
            /*else if (elapsedTime < TURNTOWARDSGLYPHPIT)
            {
                turn(1);
            }
            else if (elapsedTime < DRIVETOGLYPHPIT)
            {
                drive(0, 1);
            }
            else if (elapsedTime < GRABABLOCK)
            {
                turnOffMotors();
                setClampPosition(CLOSECLAMPPOSITION);
            }
            else if (elapsedTime < BACKTOBASE)
            {
                drive(0,-1);
            }
            else if (elapsedTime < TURNTOFACECOLUMNS)
            {
                turn(1);
            }*/
            else {
                turnOffMotors();
            }
            //updateTelemetry();
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

    public double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    //drive method that accepts two values, x and y motion
    public void drive(double x, double y)
    {
        double scale = 1;
        // for precise movement
        //if (gamepad1.right_bumper) {
        //    scale = 0.5;
        //}


        final float correctionZoneDegrees = 5;
        AngularVelocity v = gyro.getAngularVelocity();
        float v_x = v.xRotationRate; // positive is clockwise
        double correctionValue = 0;
        if (Math.abs(v_x) > correctionZoneDegrees)
        {
            correctionValue = (double)(v_x / 200.0);
        }

        double power1 = scale * x;
        double power2 = (scale * (((-.5) * x) - (sqrt(3)/2) * y)); //- correctionValue;
        double power3 = (scale * (((-.5) * x) + (sqrt(3)/2) * y)); //- correctionValue;



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
        double divisor = 2;
        // of the bumper is being held than make the robot turn slower
        if (gamepad1.left_bumper) {
            divisor = 4;
        }
        motor1.setPower(-speed/divisor);
        motor3.setPower(-speed/divisor);
        motor2.setPower(-speed/divisor);
    }

    private void setClampPosition(double newClampPosition)
    {
        rightClampServo.setPosition(newClampPosition);
        leftClampServo.setPosition(newClampPosition);
    }

    private void closeClamp(double currentPosition)
    {
        double newPosition = currentPosition + .0005;
        rightClampServo.setPosition(newPosition);
        leftClampServo.setPosition(newPosition);
    }

    private  void openClamp(double currentPosition)
    {
        double newPosition = currentPosition - .0005;
        rightClampServo.setPosition(newPosition);
        leftClampServo.setPosition(newPosition);
    }

    private void liftArm(double currentPosition)
    {
        double newPosition = currentPosition - .001;
        armServo.setPosition(newPosition);
    }

    private  void lowerArm(double currentPosition)
    {
        double newPosition = currentPosition + .001;
        armServo.setPosition(newPosition);
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

    public void stop(double seconds) {
        double currentTime = runtime.time();
        double totalTime = currentTime + seconds;

        while(currentTime < totalTime) {
            currentTime = runtime.time();
        }
    }

    // takes in a position from 0 (open all the way) to .5 (closed)
    private void clamp(double position)
    {
        rightClampServo.setPosition(position);
        leftClampServo.setPosition(position);
    }

}
