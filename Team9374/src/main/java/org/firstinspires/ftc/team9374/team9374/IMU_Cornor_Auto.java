package org.firstinspires.ftc.team9374.team9374;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorAdafruitIMU;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

/**
 * Created by darwin on 11/21/16.
 */
@Autonomous(name = "Cornor_IMU")
public class IMU_Cornor_Auto extends LinearOpMode{

        // Uncomment this to add to the opmode list

        //----------------------------------------------------------------------------------------------
        // State Varibles
        //----------------------------------------------------------------------------------------------

        // The IMU sensor object
        BNO055IMU imu;

        // State used for updating telemetry
        float angles;
        Acceleration gravity;

        //Robot Hardware
        DcMotor left_f;
        DcMotor right_f;
        DcMotor left_b;
        DcMotor right_b;

        DcMotor shooter_l;
        DcMotor shooter_r;

        //Servo center;

        public ElapsedTime runTime = new ElapsedTime();

        final int tpr = 1120;   //Ticks per Rotation
        final int wheelDiameterInInches = 3;// All of out wheels will be inches this year
        int ticks;  //To Be used for later. Just have to define it here
        int clicks;
        // Please note that this needs to be changed for any wheel size that we decide to use


        //------------------------------------------------------------------------------------------
        // Main logic
        //------------------------------------------------------------------------------------------

        @Override public void runOpMode() throws InterruptedException {
            //--------------------------------------------------------------------------------------
            //Bunch of Robot Initilaztion code
            //--------------------------------------------------------------------------------------
            left_f = hardwareMap.dcMotor.get("Eng1-left");
            right_f = hardwareMap.dcMotor.get("Eng1-right");
            left_b = hardwareMap.dcMotor.get("Eng2-left");
            right_b = hardwareMap.dcMotor.get("Eng2-right");

            shooter_r = hardwareMap.dcMotor.get("Eng3-left");
            shooter_l = hardwareMap.dcMotor.get("Eng3-right");

            //center = hardwareMap.servo.get("Ser1-center");

            imu = hardwareMap.get(BNO055IMU.class, "imu");

            left_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            left_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            left_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            left_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            left_b.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_b.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            right_f.setDirection(DcMotorSimple.Direction.REVERSE);
            right_b.setDirection(DcMotorSimple.Direction.REVERSE);



            //shooter_r.setDirection(DcMotorSimple.Direction.REVERSE);

            runTime.reset();

            //left.setDirection(DcMotorSimple.Direction.REVERSE);//Or .FORWARD
            //--------------------------------------------------------------------------------------
            //End of Robot init code.
            //--------------------------------------------------------------------------------------

            // Set up the parameters with which we will use our IMU. Note that integration
            // algorithm here just reports accelerations to the logcat log; it doesn't actually
            // provide positional information.

            //--------------------------------------------------------------------------------------
            //IMU code
            //--------------------------------------------------------------------------------------
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;               // Defining units
            parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;  // Defining units
            parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled      = true;
            parameters.loggingTag          = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
            // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
            // and named "imu".
            imu.initialize(parameters);

            // Set up our telemetry dashboard

            // Wait until we're told to go
            waitForStart();

            // Start the logging of measured acceleration

            // Wait until we're told to go
            waitForStart();

            // Loop and update the dashboard
            while (opModeIsActive()) {
                //Getting our heading into telemetry
                angles   = AngleUnit.normalizeDegrees(imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZXY).firstAngle);
                telemetry.addData("Degrees (In heading)",angles);
                //----------------------------------------------------------------------------------
                //Moving twords the cornor vortex
                //----------------------------------------------------------------------------------
                //moveClicksForInches(110, .5);

                //Turn(90,.7, false);

                //moveClicksForInches(20,.5);


            }
        }
        //imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX)
        //----------------------------------------------------------------------------------------------
        // Formatting
        //----------------------------------------------------------------------------------------------

        String formatAngle(AngleUnit angleUnit, double angle) {
            return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
        }

        String formatDegrees(double degrees){
            return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
        }
        public void Turn(double target, double speed,boolean direction) {
            /*
            I am acutally really proud of myself for this method.
            This method moves the robot a certain amount of degrees.
            //True  = Counter-Clockwise
            //False = Clockwise
            */
            //-------------------------
            //          Keep in mind, angles = current heading
            //                        target = target heading

            if (angles < 0) {

            }
            //everything needs to be in integers.

            //This took a lot of time to come up with one number
            //Just saying.


            if (direction){         //Going counter-clockwise
                setALLposition(ticks);

                left_b.setPower(-speed);
                left_f.setPower(-speed);
                right_b.setPower(speed);
                right_f.setPower(speed);

            } else { //Going clockwise
                setALLposition(ticks);

                left_f.setPower(speed);
                left_b.setPower(speed);
                right_f.setPower(-speed);
                right_b.setPower(-speed);
            }
            while (true) {
                telemetry.addData("CurrentPos",left_f.getCurrentPosition());
                if ((left_f.getCurrentPosition() - ticks) < 5){
                    break;
                }
            }
        }
        public void Beta_Turn(int target, double speed, boolean directon){
        }
        public int calcClicksForInches(double distanceInInches) {
            //Currently there are 1120 different positions on any given wheel
            double revlutions = distanceInInches / (wheelDiameterInInches * Math.PI); //Find out how many revolutations
            clicks = (int) (revlutions * tpr); //This is a pretty big number, gonna be in the 1,000's
            return clicks; //The position to set the wheels to.
        }

        public void moveClicksForInches(double distance,double power) {
            clicks = calcClicksForInches(distance);

            setALLposition(calcClicksForInches(clicks));
            setALLpower(power);

            while (true) {
                telemetry.addData("Currently moving to", clicks);
                if (left_f.getCurrentPosition() > clicks) {
                    break;
                }

            }
        }

        public void setALLpower(double power){
            left_b.setPower(power);
            left_f.setPower(power);
            right_b.setPower(power);
            right_f.setPower(power);
        }
        public void setALLposition(int position) {
            left_b.setTargetPosition(position);
            left_f.setTargetPosition(position);
            right_b.setTargetPosition(position);
            right_f.setTargetPosition(position);

        }
    }



