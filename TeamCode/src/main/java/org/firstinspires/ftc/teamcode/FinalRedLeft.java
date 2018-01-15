
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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

@Autonomous(name = "Red Left", group = "Best")
//@Disabled
public class FinalRedLeft extends LinearOpMode {
    /* Declare all devices since hardware class isn't working */
    DcMotor                 frontLeftMotor;
    DcMotor                 backLeftMotor;
    DcMotor                 frontRightMotor;
    DcMotor                 backRightMotor;
    DcMotor                 verticalArmMotor;
    DcMotor                 clawMotor;
    ColorSensor             colorSensor;
    Servo                   gemServo;
    BNO055IMU               imu;
    ModernRoboticsI2cRangeSensor sideRangeSensor;
    ModernRoboticsI2cRangeSensor frontRangeSensor;

    Orientation             lastAngles = new Orientation();
    double globalAngle;
    double xPosUp = 0;
    double xPosDown = .55;
    static double clawClose = .25;
    static double clawOpen = -.15;
    static double clawStill = 0;
    static double clawUp = 900;
    static double clawDown = 10;
    static double closest = 110;
    static double farthest  = 143;
    static double centery = 130;
    static double forwardy = 20;
    static double backwardy = 30;

    OpenGLMatrix lastLocation = null;

    /*{@link #vuforia} is the variable we will use to store our instance of the Vuforia localization engine.*/
    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() throws InterruptedException
    {


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
        clawMotor = hardwareMap.dcMotor.get("CM");
        gemServo = hardwareMap.servo.get("gemservo");
        colorSensor = hardwareMap.colorSensor.get("colorsensor");
        sideRangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "SRS");
        frontRangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "FRS");

         /* Initialize the vertical arm encoder */
        verticalArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(100);
        verticalArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    /* Reverse the direction of the front right and back right motors */
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

    /* When robot is off remove the brake on the wheel motors */
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        verticalArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    /* Init the range sensor */
        sideRangeSensor.initialize();
        frontRangeSensor.initialize();

    /* Set parameters for the gyro (imu)*/
        BNO055IMU.Parameters imuparameters = new BNO055IMU.Parameters();

        imuparameters.mode                = BNO055IMU.SensorMode.IMU;
        imuparameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imuparameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuparameters.loggingEnabled      = false;

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
        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addLine("Um...waiting for start");
        telemetry.addData("Imu calibration?", imu.getCalibrationStatus().toString());
        telemetry.update();

        // wait for start button.
        waitForStart();

        relicTrackables.activate();

        /* Let the team know wassup */
        telemetry.addLine("Ayyy, I'm running");
        telemetry.update();
       /* Power the claw to have a grip on the block */
        clawMotor.setPower(clawClose);
        sleep(500);
        clawMotor.setPower(0);

        /* Move the claw to position up */
        clawPowerPositionDirection(1, clawUp, "Up");

        /* Put the servo color arm down */
        gemServo.setPosition(xPosDown);
        sleep(1500);

        /* Knock of the Red jewel */
        knockjewelRed();

        /* Rotate so the phone can see the Vuforia Key */
        rotate(10,.2);

        /* Tells vuforia to look for relic templates, if it finds something, then it returns
        LEFT, RIGHT, CENTER and stores it into "vuMark", otherwise it only returns UNKNOWN */
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

        /* If "vuMark" is something other than UNKNOWN */
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            /* Send telemetry saying what vuforia sees */
            telemetry.addData("Ayyy I see", vuMark);
        }
        telemetry.update();

        /* Return to starting position */
        rotate(-10, .2);

        /* Wait a moment and let vuforia do its work and for the robot to realign properly */
        sleep(500);

        //////////////////* Begin the variance *\\\\\\\\\\\\\\\\\\\\\\\\\\\

        /* Move backwards into the triangle */
        movebytime(1560, .3, "Backward");

        /* This is really meant to accomplish  a 90 degree rotation, however, it is set to 87 to
        account for the slight slippage after powering off the wheels */
        rotate(-87, .2);

        /* Wait a moment to stop moving */
        sleep(700);

        /////////////////* This is the Blue Right Case *\\\\\\\\\\\\\\\\\\\\\\\

        /* Switch case based on what vuMark we see */
        switch (vuMark){
            case LEFT:
                crabDirectionGtLtStop("Right", "GT", farthest);
                break;
            case RIGHT:
                crabDirectionGtLtStop("Left", "LT", closest);
                break;
            case CENTER:
                if(getDistanceCM() > centery){
                    crabDirectionGtLtStop("Left", "LT", centery);
                }
                else{
                    crabDirectionGtLtStop("Right", "GT", centery);
                }
                break;
            case UNKNOWN:
                if(getDistanceCM() > centery){
                    crabDirectionGtLtStop("Left", "LT", centery);
                }
                else{
                    crabDirectionGtLtStop("Right", "GT", centery);
                }
                break;
        }

        /* Wait a moment */
        sleep(700);

        /* Rotate 180 */
        rotate(180, .2);

        crabFrontBack(forwardy,"Forward");

//        /* Move forward slightly so the block is in the space */
//        movebytime(500, .2, "Forward");

        ///////////////////* End the variance *\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

       /* Open up the claw to release the block */
        clawMotor.setPower(clawOpen);

        /* Let the claw */
        sleep(500);

        /* Stop the claw */
        clawMotor.setPower(clawStill);

        /* Wait a moment */
        sleep(200);

        crabFrontBack(backwardy, "Backward");
    }


    /***********************************************************************************************
     * These are all of the methods used in the Autonomous*
     ***********************************************************************************************/

    public void crabDirectionGtLtStop(String direction, String gtlt, double stop) {
        double power = .3;

        if (direction == "Right") {
            if (gtlt == "GT") {

                setWheelPower(power, power, -power, -power);

                while (opModeIsActive() && getDistanceCM() > stop) {
                    telemetry.addData("SRS Distance:", getDistanceCM());
                    telemetry.update();
                }
                wheelsOff();
            } else {
                while (opModeIsActive() && getDistanceCM() < stop) {
                    telemetry.addData("SRS Distance:", getDistanceCM());
                    telemetry.update();
                }
                wheelsOff();
            }
        } else {
         /* Left strafe */
            if (gtlt == "GT") {
                setWheelPower(-power, -power, power, power);

                while (opModeIsActive() && getDistanceCM() > stop) {
                    telemetry.addData("SRS Distance:", getDistanceCM());
                    telemetry.update();
                }
                wheelsOff();
            } else {
                while (opModeIsActive() && getDistanceCM() < stop) {
                    telemetry.addData("SRS Distance:", getDistanceCM());
                    telemetry.update();
                }
                wheelsOff();
            }
        }
    }

    public double getFrontDistanceCM(){
        double firstDistance;

        firstDistance = frontRangeSensor.cmUltrasonic();

        return firstDistance;

    }

    public double getDistanceCM(){
        double firstDistance;

        firstDistance = sideRangeSensor.cmUltrasonic();

        if(firstDistance > 160){
            return 160;
        }
        else if(firstDistance < 95){
            return 95;
        }
        else{
            return firstDistance;
        }
    }


    public void crabFrontBack(double stop, String direction){
        double power = .3;

        switch (direction){
            case"Forward":
                setWheelPower(power, -power, power, -power);
                while(opModeIsActive() && getFrontDistanceCM() < stop){
                }
                break;
            case"Backward":
                setWheelPower(-power, power, -power, power);
                while(opModeIsActive() && getFrontDistanceCM() > stop){
                }
                break;
        }
        wheelsOff();
    }

    public void crabLeft(double stop){
        double power = .3;
        setWheelPower(-power, -power, power, power);

        while(opModeIsActive() && getDistanceCM() > stop){
            telemetry.addData("SRS Distance:", getDistanceCM());
            telemetry.update();
        }
        wheelsOff();
    }

    public void crabRight(double stop){
        double power = .3;
        setWheelPower(power, power, -power, -power);

        while(opModeIsActive() && getDistanceCM() < stop){
            telemetry.addData("SRS Distance:", getDistanceCM());
            telemetry.update();
        }
        wheelsOff();
    }

    public void crabCenter(){
        double power = .3;

        if(getDistanceCM() > 130) {
            crabLeft(centery);
        }
        else{
            crabRight(centery);
        }
        wheelsOff();
    }


    /* This method uses moves the claw to a certain tick using encoders */
    public void clawPowerPositionDirection(double power, double position, String direction) {

        switch(direction){
            case "Up":
                verticalArmMotor.setPower(power);
                while(opModeIsActive() && verticalArmMotor.getCurrentPosition() < position){
                    telemetry.addData("position", verticalArmMotor.getCurrentPosition());
                    telemetry.update();
                }
                break;
            case "Down":
                verticalArmMotor.setPower(power);
                while(opModeIsActive() && verticalArmMotor.getCurrentPosition() > position){
                }
                break;
        }
        verticalArmMotor.setPower(0.0);
    }

    /* This method moves the robot forward for time and power indicated*/
    public void movebytime (long time, double power, String direction) {

    /* This switch case is determined by the String direction indicated above */

        switch (direction) {
            case "Forward":
                setWheelPower(power, -power, power, -power);
                break;
            case "Backward":
                setWheelPower(-power, power, -power, power);
                break;
            case "Right":
                setWheelPower(power, power, -power, -power);
                break;
            case "Left":
                setWheelPower(-power, -power, power, power);
                break;
        }

    /* Sleep */
        sleep(time);


    /* Once the while loop above finishes turn off the wheels */
        wheelsOff();
    }


    /* This method simply sets all wheel motors to zero power */
    public void wheelsOff() {
        setWheelPower(0,0,0,0);
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
        frontLeft   =   fl;
        frontRight  =   fr;
        backLeft    =   bl;
        backRight   =   br;

        /* set each wheel to the power indicated whenever this method is called */
        if ( FrontLeftPower != frontLeft) {
            frontLeftMotor.setPower(-fl);
            FrontLeftPower = frontLeft;
        }
        if ( FrontRightPower != frontRight) {
            frontRightMotor.setPower(fr);
            FrontRightPower = frontRight;
        }
        if ( BackLeftPower != backLeft) {
            backLeftMotor.setPower(-bl);
            BackLeftPower = backLeft;
        }
        if ( BackRightPower != backRight)
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
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(-10,.3);
        } else {
            resetAngle();
            rotate(-10, .3);
            wheelsOff();
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(10,.3);
        }
    }

    /* This method is tells the color sensor to read color, then rotate to knock off the red
    jewel and then return the color sensor arm back up */
    public void knockjewelBlue(){

        if (colorSensor.red() > colorSensor.blue()) {
            resetAngle();
            rotate(10, .3);
            wheelsOff();
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(-10,.3);
        } else {
            resetAngle();
            rotate(-10, .3);
            wheelsOff();
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(10,.3);
        }

    }

    /**
     * Resets the cumulative angle tracking to zero.
     */
    private void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     * @return Angle in degrees. + = left, - = right.
     */
    private double getAngle()
    {

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
     * See if we are moving in a straight line and if not return a power correction value.
     * @return Power adjustment, + is adjust left - is adjust right.
     */
    private double checkDirection()
    {
        // The gain value determines how sensitive the correction is to direction changes.
        // You will have to experiment with your robot to get small smooth direction changes
        // to stay on a straight line.
        double correction, angle, gain = .10;

        angle = getAngle();

        if (angle == 0)
            correction = 0;             // no adjustment.
        else
            correction = -angle;        // reverse sign of angle for correction.

        correction = correction * gain;

        return correction;
    }

//    /**
//     * Rotate left or right the number of degrees. Does not support turning more than 180 degrees.
//     * @param degrees Degrees to turn, + is left - is right
//     */

    /* This method is used to have the robot rotate to a desired heading that is defined throughout the code*/
    private void RotateTo(int targetHeading) throws InterruptedException {
        /* declare values and define constant values*/
        int robotHeading = 0;
        int headingError;
        double r = 0;
        int opModeLoopCount = 0;

        headingError = 1;

        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && (headingError != 0)) {
            // get the heading info.
            // the Modern Robotics' gyro sensor keeps
            // track of the current heading for the Z axis only.
            robotHeading = gyroGetHeading();
            // adjust heading to match unit circle
            //       Modern Robotics gyro heading increases CW
            //       unit circle increases CCW
            if (robotHeading != 0) {
                robotHeading = 360 - robotHeading;
            }

            // if heading not desired heading rotate left or right until they match
            headingError = targetHeading - robotHeading;

            if (headingError != 0) {
                if (headingError < -180) {
                    headingError = headingError + 360;
                } else if (headingError > 180) {
                    headingError = headingError - 360;
                }
                // avoid overflow to motors
                //    headingError is -180 to 180
                //    divide by 180 to make -1 to 1
                r = (double) headingError / 180.0;

                // ensure minimal power to move robot
                if ((r < .07) && (r > 0)) {
                    r = .07;
                } else if ((r > -.07) && (r < 0)) {
                    r = -.07;
                }

                // Set power on each wheel
                frontLeftMotor.setPower(r);
                frontRightMotor.setPower(r);
                backLeftMotor.setPower(r);
                backRightMotor.setPower(r);
            } else {
                wheelsOff();
            }
            opModeLoopCount = opModeLoopCount + 1;
        }
    }

    private int gyroGetHeading(){
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return Math.round(angles.firstAngle);
    }

    private void rotate(int degrees, double power)
    {
        double  leftPower, rightPower;

        // restart imu movement tracking.
        resetAngle();

        // getAngle() returns + when rotating counter clockwise (left) and - when rotating
        // clockwise (right).

        if (degrees < 0)
        {   // turn right.
            leftPower = -power;
            rightPower = power;
        }
        else if (degrees > 0)
        {   // turn left.
            leftPower = power;
            rightPower = -power;
        }
        else return;

        // set power to rotate.
        frontLeftMotor.setPower(leftPower);
        backLeftMotor.setPower(leftPower);

        frontRightMotor.setPower(rightPower);
        backRightMotor.setPower(rightPower);

        // rotate until turn is completed.
        if (degrees < 0)
        {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {}

            while (opModeIsActive() && getAngle() > degrees) {}
        }
        else    // left turn.
            while (opModeIsActive() && getAngle() < degrees) {}

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