
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;
import java.lang.Math;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

@Autonomous(name="Preciousss: Autonomous6217Red 1547", group="Preciousss")

/*
 * Created by Ben on 11/4/16.
 *
 */
public class Auto6217Reddy extends LinearOpMode {

    //FR = Front Right, FL = Front Left, BR = Back Right, BL = Back Left, BG = Ball Grabber
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    DcMotor motorWBTUno;
    DcMotor motorWBTDos;
    CRServo Conveyor1;
    CRServo Conveyor2;
    CRServo Sweeper1;
    CRServo Sweeper2;
    ColorSensor colorSensor;
    static ModernRoboticsI2cGyro gyro;
    OpticalDistanceSensor odsSensor;
    OpticalDistanceSensor odsSensor2;


    private ElapsedTime runtime = new ElapsedTime();

 //   public Auto6217Red() {
 //   }

    @Override
    public void runOpMode() {

        int xVal, yVal, zVal = 0;     // Gyro rate Values
        int heading = 0;              // Gyro integrated heading
        int angleZ = 0;
        boolean lastResetState = false;
        boolean curResetState = false;
        boolean blueDesired = false;
        boolean redDesired = true;
        double ls1;
        double ls2;

        motorFL = hardwareMap.dcMotor.get("c1_motor1");
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorFR = hardwareMap.dcMotor.get("c1_motor2");
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorBL = hardwareMap.dcMotor.get("c2_motor1");
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR = hardwareMap.dcMotor.get("c2_motor2");
        motorBR.setDirection(DcMotor.Direction.REVERSE);
        motorWBTUno = hardwareMap.dcMotor.get("c3_motor1");
        motorWBTDos = hardwareMap.dcMotor.get("c3_motor2");
        odsSensor = hardwareMap.opticalDistanceSensor.get("ods");
        odsSensor2 = hardwareMap.opticalDistanceSensor.get("ods2");
        colorSensor = hardwareMap.colorSensor.get("sensor_color");
        colorSensor.enableLed(true);
        Conveyor1 = hardwareMap.crservo.get("Servo1");
        Conveyor1.setDirection(CRServo.Direction.FORWARD);
        Sweeper1 = hardwareMap.crservo.get("Servo2");
        Sweeper2 = hardwareMap.crservo.get("Servo4");
        Conveyor2 = hardwareMap.crservo.get("Servo3");
        Conveyor2.setDirection(CRServo.Direction.REVERSE);

        // G Y R O  S E T U P

        gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();
        gyro.calibrate();
        while (gyro.isCalibrating()) {
            sleep(50);
            idle();
        }
        telemetry.addData(">", "Gyro Calibrated.  Press Start.");
        telemetry.update();

        waitForStart();

        gyro.resetZAxisIntegrator();

        angleZ = gyro.getIntegratedZValue();
        int initialHeading = gyro.getHeading();
        telemetry.addData("0","Initial Heading = %d", initialHeading);
        telemetry.addData("0","Initial AngleZ = %d", angleZ);
        telemetry.update();

        // B E G I N  A G A I N S T   W A L L

        move(0.f, -0.5f, 1.85f); // in front of center vortex

        motorWBTUno.setPower(-1);   // start to shoot
        motorWBTDos.setPower(1);

        Wait(1);

        Conveyor1.setPower(1);
        Conveyor2.setPower(1);
        Sweeper1.setPower(-1);
        Sweeper2.setPower(1);

        Wait(3); // finish shooting

        motorWBTUno.setPower(0);   // finish shooting
        motorWBTDos.setPower(0);

        Conveyor1.setPower(0);
        Conveyor2.setPower(0);
        Sweeper1.setPower(0);
        Sweeper2.setPower(0);

        pivotByZ(-35); // face opposite wall

        move(0.f, -.5f, 2.5f);   // in front of corner vortex

        pivotByZ(82);

        move(0.f, -.25f, .3f);

        // L I N E   F O L L O W
        boolean iFoundLine = false;
        while (!iFoundLine) {

            while (!iFoundLine) {

                letsGo(0.f, -0.1f);

                ls1 = lightLevel(odsSensor.getRawLightDetected());
                ls2 = lightLevel(odsSensor2.getRawLightDetected());

                double lightPctDiff = (ls2 - ls1) / Math.min(ls1, ls2);

                if (Math.abs(lightPctDiff) > 0.4) {
                    iFoundLine = true;
                }
                telemetry.addData("0", "Percent = %f", lightPctDiff);
                telemetry.addData("1", "iFoundLine = %b", iFoundLine);
                telemetry.update();
            }
            sR();

            float followSpeed = .3f;
            boolean iSeeColor = false;
            boolean iSeeBlue = false;
            boolean iSeeRed = false;
            int blueval = 0;
            int redval = 0;
            letsGo(-followSpeed, 0.f);
            while (!iSeeColor) {

                ls1 = lightLevel(odsSensor.getRawLightDetected());
                ls2 = lightLevel(odsSensor2.getRawLightDetected());

                double drift = (ls1 - ls2) / Math.min(ls1, ls2) * followSpeed / 3f;

                //move(followSpeed, (float) drift, 0.1f);

                blueval = colorSensor.blue();
                redval = colorSensor.red();
                iSeeRed = (blueval == 0) && (redval > 2);
                iSeeBlue = (blueval > 3) && (redval == 0);
                iSeeColor = iSeeRed || iSeeBlue;

                telemetry.addData("0", "iSeeBlue %b iSeeRed %b iSeeColor %b", iSeeBlue, iSeeRed, iSeeColor);
                telemetry.addData("1", "blue %02d red %02d", blueval, redval);
                telemetry.update();
            }
            sR();

            // Correct robot's angular drift during line follow

            //pivotByZ(-1);

            iSeeColor = false;
            boolean buttonPushed = false;
            while (!buttonPushed) {

                if (iSeeColor) {
                    if ((iSeeBlue && blueDesired) || (iSeeRed && !blueDesired)) {

                        // Found the button I want, so push it
                        move(0.5f, 0.f, 0.2f);
                        Wait(1);
                        move(-0.5f, 0.f, 0.2f);
                        buttonPushed = true;

                    } else {

                        // Didn't find the right color yet, so move right and keep searching
                        move(0.f, -0.125f, 0.6f);
                        move(0.5f, 0.f, 0.2f);
                        Wait(1);
                        move(-0.5f, 0.f, 0.35f);
                        buttonPushed = true;
                    }
                } else {
                    blueval = colorSensor.blue();
                    redval = colorSensor.red();
                    iSeeRed = (blueval == 0) && (redval > 2);
                    iSeeBlue = (blueval > 3) && (redval == 0);
                    iSeeColor = iSeeRed || iSeeBlue;
                }
                telemetry.addData("0", "iSeeBlue %b iSeeRed %b iSeeColor %b", iSeeBlue, iSeeRed, iSeeColor);
                telemetry.addData("1", "buttonPushed %b", buttonPushed);
                telemetry.addData("2", "blue %02d red %02d", blueval, redval);
                telemetry.update();
            }
        }
        pivotByZ(2);
        // S E C O N D   B E A C O N



        move(0.f, -.25f, 1.5f);

        // L I N E   F O L L O W
        iFoundLine = false;
        while (!iFoundLine) {

            while (!iFoundLine) {

                letsGo(0.f, -0.1f);

                ls1 = lightLevel(odsSensor.getRawLightDetected());
                ls2 = lightLevel(odsSensor2.getRawLightDetected());

                double lightPctDiff = (ls2 - ls1) / Math.min(ls1, ls2);

                if (Math.abs(lightPctDiff) > 0.4) {
                    iFoundLine = true;
                }
                telemetry.addData("0", "Percent = %f", lightPctDiff);
                telemetry.addData("1", "iFoundLine = %b", iFoundLine);
                telemetry.update();
            }
            sR();

            float followSpeed = .3f;
            boolean iSeeColor = false;
            boolean iSeeBlue = false;
            boolean iSeeRed = false;
            int blueval = 0;
            int redval = 0;
            letsGo(-followSpeed, 0.f);
            while (!iSeeColor) {

                ls1 = lightLevel(odsSensor.getRawLightDetected());
                ls2 = lightLevel(odsSensor2.getRawLightDetected());

                double drift = (ls1 - ls2) / Math.min(ls1, ls2) * followSpeed / 3f;

                //move(followSpeed, (float) drift, 0.1f);

                blueval = colorSensor.blue();
                redval = colorSensor.red();
                iSeeRed = (blueval == 0) && (redval > 2);
                iSeeBlue = (blueval > 3) && (redval == 0);
                iSeeColor = iSeeRed || iSeeBlue;

                telemetry.addData("0", "iSeeBlue %b iSeeRed %b iSeeColor %b", iSeeBlue, iSeeRed, iSeeColor);
                telemetry.addData("1", "blue %02d red %02d", blueval, redval);
                telemetry.update();
            }
            sR();

            //pivotByZ(-2);
            //move(0.f, .25f, .1f);

            iSeeColor = false;
            boolean buttonPushed = false;
            while (!buttonPushed) {

                if (iSeeColor) {
                    if ((iSeeBlue && blueDesired) || (iSeeRed && !blueDesired)) {

                        // Found the button I want, so push it
                        move(0.5f, 0.f, 0.3f);
                        Wait(1);
                        move(-0.5f, 0.f, 0.2f);
                        buttonPushed = true;

                    } else {

                        // Didn't find the right color yet, so move right and keep searching
                        move(0.f, -0.125f, 0.6f);
                        move(0.5f, 0.f, 0.3f);
                        Wait(1);
                        move(-0.5f, 0.f, 0.2f);
                        buttonPushed = true;
                    }
                } else {
                    blueval = colorSensor.blue();
                    redval = colorSensor.red();
                    iSeeRed = (blueval == 0) && (redval > 2);
                    iSeeBlue = (blueval > 3) && (redval == 0);
                    iSeeColor = iSeeRed || iSeeBlue;
                }
                telemetry.addData("0", "iSeeBlue %b iSeeRed %b iSeeColor %b", iSeeBlue, iSeeRed, iSeeColor);
                telemetry.addData("1", "buttonPushed %b", buttonPushed);
                telemetry.addData("2", "blue %02d red %02d", blueval, redval);
                telemetry.update();
            }
        }
    }

    void move(float posx, float posy, float waitTime) {
        float FRBLPower = posy + posx;
        float FLBRPower = posy - posx;
        motorFR.setPower(FRBLPower);
        motorFL.setPower(FLBRPower);
        motorBR.setPower(FLBRPower);
        motorBL.setPower(FRBLPower);
        Wait(waitTime);
        sR();
    }

    void pivotRight(float waitTime) {

        motorFL.setPower(-1.f);
        motorBL.setPower(-1.f);
        motorFR.setPower(1.f);
        motorBR.setPower(1.f);
        Wait(waitTime);
    }

    void pivotLeft(float waitTime) {

        motorFL.setPower(1.f);
        motorBL.setPower(1.f);
        motorFR.setPower(-1.f);
        motorBR.setPower(-1.f);
        Wait(waitTime);
    }

    void pivotBy(int angle) {

        // Positive angle turns clockwise with power given

        // Any faster than this and the gyro is far less accurate
        float power = .25f;

        // The gyro tends to overestimate the angle
        float fudgeFactor = 0.97f;
        int initialHeading = 0 ;
        angle = Math.round(angle * fudgeFactor);
        if (angle < 0) {
            // Counterclockwise for negative angle
            power = -power;
            // Always need a positive angle for later comparison involving absolute value
            angle = -angle;
        } else {
            initialHeading = 360;
        }

        gyro.resetZAxisIntegrator();
        motorFL.setPower(-power);
        motorBL.setPower(-power);
        motorFR.setPower(power);
        motorBR.setPower(power);

        int curHeading = 0;
        int iCount = 0;
        while (curHeading < angle) {
            iCount = iCount+1;
            curHeading = Math.abs(gyro.getHeading() - initialHeading);
            telemetry.addData("1", "%03d", curHeading);
            telemetry.addData("2", "%03d", gyro.getIntegratedZValue());
            telemetry.addData("3", "%03d", iCount);
            telemetry.update();
        }
        sR();
    }

    void pivotByZ(int angle) {

        // Positive angle turns clockwise with power given

        // Any faster than this and the gyro is far less accurate
        float power = .2f;

        // The gyro tends to overestimate the angle
        float fudgeFactor = 0.95f;
        angle = Math.round(angle * fudgeFactor);
        if (angle < 0) {
            // Counterclockwise for negative angle
            power = -power;
            // Always need a positive angle for later comparison involving absolute value
            angle = -angle;
        }

        gyro.resetZAxisIntegrator();
        motorFL.setPower(-power);
        motorBL.setPower(-power);
        motorFR.setPower(power);
        motorBR.setPower(power);

        int curHeading = 0;
        int iCount = 0;
        while (curHeading < angle) {
            iCount = iCount+1;
            curHeading = Math.abs(gyro.getIntegratedZValue());
            telemetry.addData("1", "%03d", curHeading);
            telemetry.addData("2", "%03d", gyro.getIntegratedZValue());
            telemetry.addData("3", "%03d", iCount);
            telemetry.update();
        }
        sR();
    }

    void pivotTo(int heading) {

        // Positive angle turns clockwise with power given

        // Any faster than this and the gyro is far less accurate
        float power = .5f;
        float tolerance = 0.1f * heading;

        int initialHeading = gyro.getHeading();
        motorFL.setPower(-power);
        motorBL.setPower(-power);
        motorFR.setPower(power);
        motorBR.setPower(power);

        int curHeading = 0;
        int iCount = 0;
        while (!((curHeading < heading + tolerance) && (curHeading > heading - tolerance))) {
            iCount = iCount++;
            curHeading = gyro.getHeading();
            telemetry.addData("1", "%03d", curHeading);
            telemetry.addData("2", "%03d", iCount);
            telemetry.update();
        }
        sR();
    }

    void letsGo(float posx, float posy) {
        float FRBLPower = posy - posx;
        float FLBRPower = posy + posx;
        motorFR.setPower(FRBLPower);
        motorFL.setPower(FLBRPower);
        motorBR.setPower(FLBRPower);
        motorBL.setPower(FRBLPower);
        Wait(0.1f);
    }

    void sR() {
        float power = 0.f;
        motorFL.setPower(power);
        motorBL.setPower(power);
        motorFR.setPower(power);
        motorBR.setPower(power);
    }

    void Wait(double WaitTime) {
        runtime.reset();
        while (runtime.seconds() < WaitTime) {
            telemetry.addData("5", " %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
    }

    double lightLevel(double odsVal) {
        /*
         * This method adjusts the light sensor input, read as an ODS,
         * to obtain values that are large enough to use to drive
         * the robot.  It does this by taking the third decimal values
         * and beyond as the working values.
         */
        // LB = left bumper, RB = right bumper.
        int prefix = (int) (odsVal * 10);
        odsVal = Math.pow(10, odsVal) * prefix;

        return (odsVal);
    }

    @TeleOp(name="Preciousss: GreenHornsTestCode ", group="Preciousss")

    public static class GreenHornsTestCode extends OpMode
    {
        /*FR = Front Right Wheel, FL = Front Left Wheel, BR = Back Righ Wheelt, BL = Back Left Wheel,
        BG = Ball Grabber, WBT = Wiffle Ball Thrower, BGL = Ball Grabber Lifter, BP = Blast Plate, CS1 = color sensor 1*/
        DcMotor motorFR;
        DcMotor motorFL;
        DcMotor motorBR;
        DcMotor motorBL;
        DcMotor motorBG;
        DcMotor motorWBT1;
        DcMotor motorBGL;
        DcMotor motorWBT2;
        CRServo Conveyor1;
        CRServo Conveyor2;
        CRServo Sweeper1;
        CRServo Sweeper2;

        private ElapsedTime     runtime = new ElapsedTime();

        private boolean RunLauncher = false;
        private int DelayCounter = 0;
        private int DelayTimer = 400 ;

        public GreenHornsTestCode() {}

        @Override
        public void init()
        {
          /*
           * Wheels: controller 1, motors 1,2,3,4
           */
            motorFL = hardwareMap.dcMotor.get("c1_motor1");
            motorFL.setDirection(DcMotor.Direction.FORWARD);
            motorFR = hardwareMap.dcMotor.get("c1_motor2");
            motorFR.setDirection(DcMotor.Direction.REVERSE);
            motorBL = hardwareMap.dcMotor.get("c2_motor1");
            motorBL.setDirection(DcMotor.Direction.FORWARD);
            motorBR = hardwareMap.dcMotor.get("c2_motor2");
            motorBR.setDirection(DcMotor.Direction.REVERSE);
            motorWBT1 = hardwareMap.dcMotor.get("c3_motor1");
            motorWBT2 = hardwareMap.dcMotor.get("c3_motor2");
            motorWBT2.setDirection(DcMotor.Direction.REVERSE);
            motorBGL = hardwareMap.dcMotor.get("c4_motor1");
            motorBG = hardwareMap.dcMotor.get("c4_motor2");
            Conveyor1 = hardwareMap.crservo.get("Servo1");
            Conveyor1.setDirection(CRServo.Direction.FORWARD);
            Sweeper1 = hardwareMap.crservo.get("Servo2");
            Sweeper2 = hardwareMap.crservo.get("Servo4");
            Conveyor2 = hardwareMap.crservo.get("Servo3");
            Conveyor2.setDirection(CRServo.Direction.REVERSE);




           /* telemetry.addData("Status", "Resetting Encoders");    //
            telemetry.update();
            telemetry.addData("WBG",  "Starting at %7d",
                   // motorWBT1.getCurrentPosition());
            telemetry.update());*/

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


        @Override
        public void loop() {

            /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                Read values from Game Controller
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

            // Stick values range from -1 to 1, where -1 is full up, and 1 is full down
            // Trigger values range from 0 to 1

            /* posx = Position X, posy = Position Y, LT = Left Trigger, RT = Right Trigger,
             a = Button a, y = Button y, x = Button x, b = Button b, UP = BGL Going Up, DOWN = BGL Going Down,
             AA = Arm Angle, Close = BG closing, Open = BG opening, BPD = Blast Protector Down, BPU = Blast Protector Up,
             GP = Grab Particles, PPIWBT = Put Partciles Into Wiffle Ball Thrower,Beacon = Push Beacons. */
            float FLBRPower = 0.f;
            float FRBLPower = 0.f;
            float posx = gamepad1.left_stick_x;
            float posy = gamepad1.left_stick_y;
            float LT = gamepad1.left_trigger;
            float RT = gamepad1.right_trigger;
            boolean a = gamepad1.a;
            boolean y = gamepad1.y;
            boolean x = gamepad1.x;
            boolean Close = gamepad1.dpad_right;
            boolean Open = gamepad1.dpad_left;
            boolean UP = gamepad1.dpad_up;
            boolean DOWN = gamepad1.dpad_down;

             /* ~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                Limit x and y values and adjust to power curve
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

            posx = Range.clip(posx, -1, 1);
            posy = Range.clip(posy, -1, 1);

            posx = (float) powerCurve(posx);
            posy = (float) powerCurve(posy);

            LT = (float) powerCurve(LT);
            RT = (float) powerCurve(RT);


            /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                Write power to wheels
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */





            // use ball thrower
            if (a) {
                Sweeper1.setPower(-1);
                Conveyor1.setPower(1);
                Conveyor2.setPower(1);
            }


            else {
                Sweeper1.setPower(0);

                if (!RunLauncher)  {
                    Conveyor1.setPower(0);
                    Conveyor2.setPower(0);
                }
            }

            if ( y ) {
                if (!RunLauncher) {
                    RunLauncher = true;
                    motorWBT1.setPower(-1);
                    motorWBT2.setPower(-1);
                    DelayCounter = 0;
                } else {
                    RunLauncher = false;
                }
            }

            telemetry.addData("5", "RunLauncher:" + " " +
                    String.format("%b", RunLauncher));

            if (RunLauncher) {
                DelayCounter++;

                if (DelayCounter >= DelayTimer) {
                    Sweeper2.setPower(
                            1);
                    Conveyor1.setPower(1);
                    Conveyor2.setPower(1);
                }
            }

            else {
                motorWBT1.setPower(0);
                motorWBT2.setPower(0);
                Sweeper2.setPower(0);

                if (!a)  {
                    Conveyor1.setPower(0);
                    Conveyor2.setPower(0);
                }

            }
            if (UP) {
                motorBGL.setPower(1.0);
            }

            else if (DOWN)  {
                motorBGL.setPower(-1.0);
            }

            else {
                motorBGL.setPower(0);
            }

            //ball Grabber
            if (Open) {
                motorBG.setPower(0.5);

            }

            else if (Close)  {
                motorBG.setPower(-1);

            }

            else {
                motorBG.setPower(0);
            }

            //  pivot left

            if (LT != 0)  {

                motorFL.setPower(LT);
                motorBL.setPower(LT);
                motorFR.setPower(-LT);
                motorBR.setPower(-LT);
            }

            //

            //  pivot right

            else if (RT != 0)  {

                motorFL.setPower(-RT);
                motorBL.setPower(-RT);
                motorFR.setPower(RT);
                motorBR.setPower(RT);
            }

            //  Driving

            else if ( ( posy != 0) || ( posx != 0 ) ) {

                FRBLPower = posy - posx;
                FLBRPower = posy + posx;
                motorFR.setPower( FRBLPower );
                motorFL.setPower( FLBRPower );
                motorBR.setPower( FLBRPower );
                motorBL.setPower( FRBLPower );

            }

            else {

                motorFR.setPower(0);
                motorFL.setPower(0);
                motorBR.setPower(0);
                motorBL.setPower(0);
            }


            /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                Write telemetry back to driver station
                NOTE: Output is sorted by the first field, hence the numbering
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */


            telemetry.addData("1", "Power, FRBL/FLBR: " +
                    String.format("%.2f", FRBLPower) + " " +
                    String.format("%.2f", FLBRPower));

            telemetry.addData("2", "Raw Joystick X, Y: " +
                    String.format("%.2f", gamepad1.left_stick_x) + " " +
                    String.format("%.2f", gamepad1.left_stick_y));

            telemetry.addData("3", "D-Pad U, R, D, L: " +
                    String.format("%b", gamepad1.dpad_up) + " " +
                    String.format("%b", gamepad1.dpad_right) + " " +
                    String.format("%b", gamepad1.dpad_down) + " " +
                    String.format("%b", gamepad1.dpad_left));

            telemetry.addData("0", "Trigger: " +
                    String.format("%.2f", gamepad1.left_trigger));
            telemetry.addData("0", "Trigger: " +
                    String.format("%.2f", gamepad1.right_trigger));
            telemetry.addData("4", "Right Bumper Left Bumper" +
                    String.format("%b", gamepad1.right_bumper)+ " " +
                    String.format("%b", gamepad1.left_bumper));



        }









        /*
         * Code to run when the op mode is first disabled goes here
         *
         * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
         */
        @Override
        public void stop() {
        }








        double powerCurve ( float dVal ) {
        /*
         * This method adjusts the joystick input to apply a power curve that
         * is less than linear.  This is to make it easier to drive
         * the robot more precisely at slower speeds.
         */
    // LB = left bumper, RB = right bumper.
            boolean LB = gamepad1.left_bumper;
            boolean RB = gamepad1.right_bumper;


            double dead = .2;
            double max = .75;

            if (LB == true && RB == false){
                max = 1;
            }

            if (LB == false && RB == true){
                max = .3;
            }

            int direction = 1;
            if (dVal < 0) {
                direction = -1;
            }

            double dScale = 0.0;
            if (Math.abs(dVal) < dead) {
                dScale = 0.0;
            } else {
                dScale = ((dVal * dVal) * max) * direction;
            }

            return (dScale);
        }




    }
}














