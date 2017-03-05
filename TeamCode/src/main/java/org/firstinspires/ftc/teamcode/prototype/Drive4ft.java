package org.firstinspires.ftc.teamcode.prototype;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivetrain.ROUSAutoHardware_WithServos;
import org.firstinspires.ftc.teamcode.drivetrain.DriveCommands;

/**
 * Created by Connor on 2/9/2017.
 */
@Autonomous(name="Drive4ft", group="Pushbot")
//@Disabled
public class Drive4ft extends LinearOpMode {
    ROUSAutoHardware_WithServos robot = new ROUSAutoHardware_WithServos();   // Use a Pushbot's hardware
    ModernRoboticsI2cGyro gyro = null;
    OpticalDistanceSensor ODS;
    private ElapsedTime runtime = new ElapsedTime();
        static final int LED_CHANNEL = 5;
        static final double COUNTS_PER_MOTOR_REV = 1680;    // eg: TETRIX Motor Encoder
        static final double DRIVE_GEAR_REDUCTION = .625;     // This is < 1.0 if geared UP
        static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
        static final double Pi = 3.141592653f;
        static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * Pi);

        // These constants define the desired driving/control characteristics
        // The can/should be tweaked to suite the specific robot drive train.
        static final double DRIVE_SPEED = .4;// Nominal speed for better accuracy.
        static final double DRIVE_SPEED2 = .2;
        static final double TURN_SPEED = 0.075;     // Nominal half speed for better accuracy.
        static final double SCAN_SPEED = .06;

        static final double HEADING_THRESHOLD = 10;      // As tight as we can make it with an integer gyro
        static final double P_TURN_COEFF = 0.05;     // Larger is more responsive, but also less stable
        static final double P_DRIVE_COEFF = .075;     // Larger is more responsive, but also less stable
        static final float Angle = 87;

        static final double UP = .3;
        static final double DOWN = .9;
    double odsReadingRaw;

    // odsReadingRaw to the power of (-0.5)
    static double odsReadingLinear;


        @Override
        public void runOpMode() throws InterruptedException {

            gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");

            telemetry.addData(">", "Calibrating Gyro");    //
            telemetry.update();

            gyro.calibrate();


            // make sure the gyro is calibrated before continuing
            while (!isStopRequested() && gyro.isCalibrating()) {
                sleep(50);
                idle();
            }

            telemetry.addData(">", "Gyro is Calibrated.");    //
            telemetry.update();
            telemetry.update();
            DriveCommands Command = new DriveCommands();
            Command.initializeForOpMode( this, hardwareMap );
            ODS = hardwareMap.opticalDistanceSensor.get("ods");
            odsReadingRaw = ODS.getRawLightDetected();                   //update raw value (This function now returns a value between 0 and 5 instead of 0 and 1 as seen in the video)
            odsReadingLinear = Math.pow(odsReadingRaw, -0.5);
            telemetry.addData("0 ODS Raw", odsReadingRaw);
            telemetry.addData("1 ODS linear", odsReadingLinear);
            telemetry.update();
                robot.init(hardwareMap);
                // Ensure the robot it stationary, then reset the encoders and calibrate the gyro.
                robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                // Send telemetry message to alert driver that we are calibrating;

                // Always call idle() at the bottom of your while(opModeIsActive()) loop


                robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            waitForStart();
            sleep(100);
            //Command.gyroDrive(this,DRIVE_SPEED, 36, 0);
            //sleep(250);
            //Command.gyroTurn(this,TURN_SPEED, -90);
            //sleep(250);
            //Command.gyroDrive(this,DRIVE_SPEED, 36, 90);
            //sleep(250);
            //Command.gyroTurn(this,TURN_SPEED,-180);
            //sleep(250);
            //Command.gyroDrive(this,DRIVE_SPEED, 34, -180);
            Command.OdsDrive(this,48, 20);
            sleep(2000);
            Command.OdsDrive(this, -48, 20);
            sleep(5000);
            stop();

            }

}

