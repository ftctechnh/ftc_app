
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name = "Azul Right", group = "Bacon Autonomous!")
//@Disabled
public class AhoraSiWey extends LinearOpMode {
    /* Declare all devices since hardware class isn't working */
    DcMotor frontLeftMotor;
    DcMotor backLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backRightMotor;
    DcMotor verticalArmMotor;
    DcMotor trayMotor;
    ColorSensor colorSensor;
    Servo gemServo;
    BNO055IMU imu;

    double frontLeft;
    double frontRight;
    double backLeft;
    double backRight;


    Orientation lastAngles = new Orientation();
    double globalAngle;
    double xPosUp = 0;
    double xPosDown = .55;
    int trayOut = -390;
    int trayIn = 10;
    OpenGLMatrix lastLocation = null;

    /* Give place holder values for the motors*/
    double FrontLeftPower = 0;
    double FrontRightPower = 0;
    double BackRightPower = 0;
    double BackLeftPower = 0;

    String teamColorPosition = "BlueRight";
//    String teamColorPosition = "BlueLeft";
//    String teamColorPosition = "RedRight";
//    String teamColorPosition = "RedLeft";

    /*{@link #vuforia} is the variable we will use to store our instance of the Vuforia localization engine.*/
    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() throws InterruptedException {


    /* To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);*/
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

    /* Our vurforia license key */
        parameters.vuforiaLicenseKey = "AbWq5Uv/////AAAAGcM6elMmVUOogS1JXLiPuvxUPJgstfq86yvjnKb87ZG0oftSUkO7FUXo+LPZdGe3ytBqkwQmXV6b0hiAMotK9TAX//BaE8tYQe0cJQzMPk5jjMAmLbZgZ1p3V9EQzp59pYvYvBMYzoNw7YzlpMNC3GzmXd40NyecOmx8Q6lp/tQikXO0yKGQLIoJpKtGfoVkxpmyCx/u4/6FYBAGyvZt8I8mz3UtGl/Yf366XKgNXq26uglpVfeurmB/cV5RzMVdVDTdyE/2yLqjalrAKgL2CZFv3iY/MnxW+pIyJUHbXUQVCUoB8SqULq7u948Vx+5w5ObesVFNzZ3jbBTBHwUWbpaJAZFGjmD1dRaVdS/GK74x";

    /*We also indicate which camera on the RC that we wish to use. Here we chose the back (HiRes) camera (for greater range)*/
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

    /* Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
    * in this data set: all three of the VuMarks in the game were created from this one template,
    * but differ in their instance id information.
    */
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

    /* Find all hardware in configuration */
        frontLeftMotor = hardwareMap.dcMotor.get("FL");
        backLeftMotor = hardwareMap.dcMotor.get("BL");
        frontRightMotor = hardwareMap.dcMotor.get("FR");
        backRightMotor = hardwareMap.dcMotor.get("BR");
        verticalArmMotor = hardwareMap.dcMotor.get("VAM");
        trayMotor = hardwareMap.dcMotor.get("TM");
        gemServo = hardwareMap.servo.get("gemservo");
        colorSensor = hardwareMap.colorSensor.get("colorsensor");


    /* Initialize with encoders */
        trayMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(100);
        trayMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    /* Set wheels to brake mode */
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    /* Reverse the left wheels */
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

    /* Set parameters for the gyro (imu)*/
        BNO055IMU.Parameters imuparameters = new BNO055IMU.Parameters();

        imuparameters.mode = BNO055IMU.SensorMode.IMU;
        imuparameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuparameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuparameters.loggingEnabled = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");

    /* Initialize all motors and servoes to their starting positions */
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        gemServo.setPosition(xPosUp);

    /* Initialize the gyro with the parameters above */
        imu.initialize(imuparameters);

    /* Tell driver the gyro is calibrating */
        telemetry.addLine("I'm calibrating...");
        telemetry.update();

    /* Make sure the imu gyro is calibrated before continuing */
        while (!isStopRequested() && !imu.isGyroCalibrated()) {
            sleep(50);
            idle();
        }

        telemetry.addLine("Alrighty...waiting for start");
        telemetry.addData("Imu calibration:", imu.getCalibrationStatus().toString());
        telemetry.update();

        // wait for start button.
        waitForStart();

        relicTrackables.activate();

        /* Let the team know wassup */
        telemetry.addLine("Ayyy, I'm running");
        telemetry.update();

        /* no encoder */
        runWithoutEncoder();

        /* Put the servo color arm down */
        gemServo.setPosition(xPosDown);
        sleep(500);

        /* Knock off the jewel */
        switch (teamColorPosition) {
            case "BlueRight":
                knockjewelBlue();
                break;
            case "BlueLeft":
                knockjewelBlue();
                break;
            case "RedRight":
                knockjewelRed();
                break;
            case "RedLeft":
                knockjewelRed();
                break;
        }

        /* Rotate so the phone can see the Vuforia Key */
        rotate(10, .2);

        /* Tells vuforia to look for relic templates, if it finds something, then it returns
        LEFT, RIGHT, CENTER and stores it into "vuMark", otherwise it only returns UNKNOWN */
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

        /* If "vuMark" is something other than UNKNOWN */
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            /* Send telemetry saying what vuforia sees */
            telemetry.addData("I see...", vuMark);
        }
        telemetry.update();

        /* Return to starting position */
        rotate(-10, .2);

        /* Wait a moment and let vuforia do its work and for the robot to realign properly */
        sleep(500);

        /* with encoder */
        resetRunToPosition();

        /* wait a moment */
        sleep(100);

        /* switch case to position proper distance and rotate to 45 */
        switch (teamColorPosition) {
            case "BlueRight":
                /* Get off balance board & rotate 180*/
                runToPositionWheels(2200, .7, "Forward");
                sleep(1500);
                runWithoutEncoder();
                rotate(177, .3);
                /* Rotate properly for vuMark */
                keyAlignment(teamColorPosition, vuMark);
                sleep(30);
                break;
            case "BlueLeft":
                break;
            case "RedRight":
                break;
            case "RedLeft":
                break;
        }
    }

    /***********************************************************************************************
     * These are all of the methods used in the Autonomous*
     ***********************************************************************************************/

    /* This moves the robot so it's ready to turn 45 towards the cryptobox based on
    the vuforia key reading*/
    public void keyAlignment(String teamColorPosition, RelicRecoveryVuMark vuMark) {
        runWithoutEncoder();
        switch (teamColorPosition) {
            case "BlueRight":
                vumarkSwitch(vuMark, 85, 50, 40, .5);
                break;
            case "BlueLeft":
                vumarkSwitch(vuMark, 85, 60, 50, .5);
                break;
            case "RedRight":
                vumarkSwitch(vuMark, -85, -60, -50, .5);
                break;
            case "RedLeft":
                vumarkSwitch(vuMark, -85, -60, -50, .5);
                break;

        }
    }

    /* This method is the switch case for vuMark alignment */
    public void vumarkSwitch(RelicRecoveryVuMark vuMark, int leftRotate, int centerRotate, int rightRotate, double power) {
        switch (vuMark) {
            case LEFT:
                rotate(leftRotate, power);
                sleep(500);
                resetRunToPosition();
                runToPositionWheels(400, .5, "Backward");
                sleep(1500);
                /* Place Glyph */
                placeGlyphOut();
                resetRunToPosition();
                runToPositionWheels(200, .5, "Forward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(500, .5, "Backward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(300, .5, "Forward");
                sleep(1500);
                placeTrayIn();
                sleep(100);
                break;
            case CENTER:
                rotate(centerRotate, power);
                sleep(500);
                resetRunToPosition();
                runToPositionWheels(400, .5, "Backward");
                sleep(1500);
                placeGlyphOut();
                runToPositionWheels(200, .5, "Forward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(500, .5, "Backward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(300, .5, "Forward");
                sleep(1500);
                placeTrayIn();
                sleep(100);
                break;
            case UNKNOWN:
                rotate(centerRotate, power);
                sleep(2000);
                resetRunToPosition();
                runToPositionWheels(400, .5, "Backward");
                sleep(2000);
                /* Place Glyph */
                placeGlyphOut();
                runToPositionWheels(200, .5, "Forward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(500, .5, "Backward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(300, .5, "Forward");
                sleep(1500);
                placeTrayIn();
                sleep(100);
                break;
            case RIGHT:
                rotate(rightRotate, power);
                sleep(2000);
                runToPositionWheels(400, .5, "Backward");
                sleep(2000);
                /* Place Glyph */
                placeGlyphOut();
                runToPositionWheels(200, .5, "Forward");
                sleep(1000);
                resetRunToPosition();
                runToPositionWheels(500, .5, "Backward");
                sleep(1000);
                resetRunToPosition();
                runToPositionWheels(300, .5, "Forward");
                sleep(1000);
                placeTrayIn();
                sleep(100);
                break;
        }

    }

    /* This flips out */
    public void placeGlyphOut() {
        trayMotor.setTargetPosition(trayOut);
        trayMotor.setPower(-.2);
        sleep(2000);
//        trayMotor.setTargetPosition(trayIn);
//        trayMotor.setPower(.2);
//        sleep(2000);
    }

    /* This flips in */
    public void placeTrayIn() {
        trayMotor.setTargetPosition(trayIn);
        trayMotor.setPower(.2);
        sleep(2000);

    }

    /* This resets the encoders on the wheels to RUN_WITHOUT_ENCODER */
    public void runWithoutEncoder() {
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(100);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /* This resets the encoders back to ZERO while still running with RUN_TO_POSITION */
    public void resetRunToPosition() {
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(100);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }


    /* This tells the wheels to run to a position for a certain number of ticks, at a certain power
    and in a certain direction */
    public void runToPositionWheels(int ticks, double power, String direction) {

        switch (direction) {
            case "Forward":
                frontRightMotor.setTargetPosition(ticks);
                frontLeftMotor.setTargetPosition(ticks);
                backRightMotor.setTargetPosition(ticks);
                backLeftMotor.setTargetPosition(ticks);
                break;
            case "Backward":
                frontRightMotor.setTargetPosition(-ticks);
                frontLeftMotor.setTargetPosition(-ticks);
                backRightMotor.setTargetPosition(-ticks);
                backLeftMotor.setTargetPosition(-ticks);
                break;
            case "Right":
                frontRightMotor.setTargetPosition(-ticks);
                frontLeftMotor.setTargetPosition(ticks);
                backRightMotor.setTargetPosition(ticks);
                backLeftMotor.setTargetPosition(-ticks);
                break;
            case "Left":
                frontRightMotor.setTargetPosition(ticks);
                frontLeftMotor.setTargetPosition(-ticks);
                backRightMotor.setTargetPosition(-ticks);
                backLeftMotor.setTargetPosition(ticks);
                break;
        }

        switch (direction) {
            case "Forward":
                setWheelPower(-power, power, -power, power);
                break;
            case "Backward":
                setWheelPower(power, -power, power, -power);
                break;
            case "Right":
                setWheelPower(-power, -power, power, power);
                break;
            case "Left":
                setWheelPower(power, power, -power, -power);
                break;
        }
    }

    /* This method moves the tray to position */
    public void setTrayPosition(String position) {
        switch (position) {
            case "Out":
                trayMotor.setTargetPosition(trayOut);
                trayMotor.setPower(-.4);
                break;
            case "In":
                trayMotor.setTargetPosition(trayIn);
                trayMotor.setPower(.4);
                break;
        }
    }

    /* This method moves the robot forward for time and power indicated*/
    public void movebytime(long time, double power, String direction) {

    /* This switch case is determined by the String direction indicated above */

        switch (direction) {
            case "Forward":
                setWheelPower(-power, power, -power, power);
                break;
            case "Backward":
                setWheelPower(power, -power, power, -power);
                break;
            case "Right":
                setWheelPower(-power, -power, power, power);
                break;
            case "Left":
                setWheelPower(power, power, -power, -power);
                break;
        }

    /* Sleep */
        sleep(time);


    /* Once the while loop above finishes turn off the wheels */
        wheelsOff();
    }


    /* This method simply sets all wheel motors to zero power */
    public void wheelsOff() {
        setWheelPower(0, 0, 0, 0);
    }

    /* This method powers each wheel to whichever power is desired */
    public void setWheelPower(double fl, double fr, double bl, double br) {

        /* Create power variables */
        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;

        double FrontLeftPower;
        double FrontRightPower;
        double BackLeftPower;
        double BackRightPower;

        FrontLeftPower = 0;
        FrontRightPower = 0;
        BackLeftPower = 0;
        BackRightPower = 0;

        /* Initialize the powers with the values input whenever this method is called */
        frontLeft = fl;
        frontRight = fr;
        backLeft = bl;
        backRight = br;

        /* set each wheel to the power indicated whenever this method is called */
        if (FrontLeftPower != frontLeft) {
            frontLeftMotor.setPower(-fl);
            FrontLeftPower = frontLeft;
        }
        if (FrontRightPower != frontRight) {
            frontRightMotor.setPower(fr);
            FrontRightPower = frontRight;
        }
        if (BackLeftPower != backLeft) {
            backLeftMotor.setPower(-bl);
            BackLeftPower = backLeft;
        }
        if (BackRightPower != backRight)
            backRightMotor.setPower(br);
        BackRightPower = backRight;
    }

    /* This method is tells the color sensor to read color, then rotate to knock off the blue
    jewel and then return the color sensor arm back up */
    public void knockjewelRed() {

        if (colorSensor.red() < colorSensor.blue()) {
            resetAngle();
            rotate(10, .3);
            wheelsOff();
            sleep(200);
            gemServo.setPosition(xPosUp);
            rotate(-10, .3);
        } else {
            resetAngle();
            rotate(-10, .3);
            wheelsOff();
            sleep(200);
            gemServo.setPosition(xPosUp);
            rotate(10, .3);
        }
    }

    /* This method is tells the color sensor to read color, then rotate to knock off the red
    jewel and then return the color sensor arm back up */
    public void knockjewelBlue() {

        if (colorSensor.red() > colorSensor.blue()) {
            resetAngle();
            rotate(10, .3);
            wheelsOff();
            sleep(200);
            gemServo.setPosition(xPosUp);
            rotate(-10, .3);
        } else {
            resetAngle();
            rotate(-10, .3);
            wheelsOff();
            sleep(200);
            gemServo.setPosition(xPosUp);
            rotate(10, .3);
        }

    }

    /**
     * Resets the cumulative angle tracking to zero.
     */
    private void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     *
     * @return Angle in degrees. + = left, - = right.
     */
    private double getAngle() {

        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    /**
     * Rotate left or right the number of degrees. Does not support turning more than 180 degrees.
     *
     * @param degrees Degrees to turn, + is left - is right
     */
    private void rotate(int degrees, double power) {
        double leftPower, rightPower;

        // restart imu movement tracking.
        resetAngle();

        // getAngle() returns + when rotating counter clockwise (left) and - when rotating
        // clockwise (right).

        if (degrees < 0) {   // turn right.
            leftPower = power;
            rightPower = -power;
        } else if (degrees > 0) {   // turn left.
            leftPower = -power;
            rightPower = power;
        } else return;

        // set power to rotate.
        frontLeftMotor.setPower(leftPower);
        backLeftMotor.setPower(leftPower);

        frontRightMotor.setPower(rightPower);
        backRightMotor.setPower(rightPower);

        // rotate until turn is completed.
        if (degrees < 0) {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {
            }

            while (opModeIsActive() && getAngle() > degrees) {
            }
        } else    // left turn.
            while (opModeIsActive() && getAngle() < degrees) {
            }

        // turn the motors off.
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);

        // wait for rotation to stop.
        sleep(1000);

        // reset angle tracking on new heading.
        resetAngle();
    }
}