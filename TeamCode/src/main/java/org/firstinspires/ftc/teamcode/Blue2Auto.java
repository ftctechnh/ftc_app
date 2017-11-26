
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;



@Autonomous(name="Blue2Auto", group ="Pushbot")
//@Disabled
public class Blue2Auto extends LinearOpMode {

    HardwareDRive         robot   = new HardwareDRive();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    int Dis=700;

    public static final String TAG = "Vuforia VuMark Sample";

    OpenGLMatrix lastLocation = null;

    public static RelicRecoveryVuMark vuMark = null;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    VuforiaLocalizer vuforia;

    @Override public void runOpMode() {

        /*
         * To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */

        robot.init(hardwareMap);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "ARjW6VD/////AAAAGbCMKpMCSEgSunPcA5cUQkuEKymuh9/mOQ5b+ngfYCdx3gPONkD3mscU39FUD7mRQRZSRZpjHZfohKwL2PYsVZrBcTlaY1JcJ9J5orZKqTxxy68irqEBuQkkfG72xEEPYuNq+yEJCNzYKhx3wFGqUV1H05Z1fFJa1ZiWfe4Tn9aO2Yf5AIkYCMz4K75LFU3ZM1wCgz9ubLhxZH2BWF9X0rhvnhZS2rnLHkxm+C+xzRbs2ZoGCOpDRb3Dy0iMG2y4Ve9/AApZQ+6sgSwlc9liA5jZ0QyT0dLqyfaoXwNxPqzBjhOj3FltEHxrWPdpOQm6B8BDC9Kv+BShnpi6g3yhf+msI3Qeqsns/nm6DrGF5zum";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary



        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.FLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.FRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.BLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.BRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        idle();

        robot.FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.FLMotor.getCurrentPosition(),
                robot.FRMotor.getCurrentPosition(),
                robot.FLMotor.getCurrentPosition(),
                robot.FRMotor.getCurrentPosition());
        telemetry.update();

        waitForStart();

        relicTrackables.activate();

        while (opModeIsActive()) {

            /**
             * See if any of the instances of {@link relicTemplate} are currently visible.
             * {@link RelicRecoveryVuMark} is an enum which can have the following values:
             * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
             * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
             */
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "%s visible", vuMark);

                /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. */
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                telemetry.addData("Pose", format(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                }



//close the claw first thing after start
                robot.clawleft.setPosition(0.5);
                robot.clawright.setPosition(0);
                robot.arm.setPower(-0.2);
                sleep(500);
                robot.arm.setPower(0);
                //actual auto start


                if (vuMark == RelicRecoveryVuMark.RIGHT){
                    encoderDrive(0.5,-15,-15,2.5);
                    robot.SideMotor.setPower(1);
                    sleep(1100);
                    robot.SideMotor.setPower(0);
                    encoderDrive(1, -5, -5, 0.5);
                    sleep(100);
                    //open claw
                    robot.clawleft.setPosition(0);
                    robot.clawright.setPosition(0.5);
                    sleep(300);
                    encoderDrive(0.2,10,10,2);
                    robot.clawleft.setPosition(0.5);
                    robot.clawright.setPosition(0);
                    encoderDrive(0.5,-10,-10,2);
                    //open claw
                /*robot.clawleft.setPosition(0.1);
                robot.clawright.setPosition(0.4);
                sleep(300);
                encoderDrive(0.5,5,5,5);//drive back
                robot.SideMotor.setPower(0.5);//drive to the right
                sleep(600);
                robot.SideMotor.setPower(0);
                encoderDrive(0.5,13,13,10);
                encoderDrive(0.5, 9, -9, 5);//turn 90 degrees
                */
                    break;
                }
                if (vuMark == RelicRecoveryVuMark.CENTER){
                    encoderDrive(0.5,-15,-15,2.5);
                    robot.SideMotor.setPower(1);
                    sleep(720);
                    robot.SideMotor.setPower(0);
                    encoderDrive(1, -5, -5, 0.5);
                    sleep(100);
                    //open claw
                    robot.clawleft.setPosition(0);
                    robot.clawright.setPosition(0.5);
                    sleep(300);
                    encoderDrive(0.2,10,10,2);
                    robot.clawleft.setPosition(0.5);
                    robot.clawright.setPosition(0);
                    encoderDrive(0.5,-10,-10,2);

                /*
                encoderDrive(0.5,5,5,5);//drive back
                robot.SideMotor.setPower(0.5);//drive to the right
                sleep(500);
                robot.SideMotor.setPower(0);
                encoderDrive(0.5,15,15,10);
                encoderDrive(0.5, 8.3, -8.3, 5);//turn 90 degrees
                */
                    break;
                }
                if (vuMark == RelicRecoveryVuMark.LEFT){
                    encoderDrive(0.5,-15,-15,2.5);
                    robot.SideMotor.setPower(1);
                    sleep(280);
                    robot.SideMotor.setPower(0);
                    encoderDrive(1, -5, -5, 0.5);
                    sleep(100);
                    //open claw
                    robot.clawleft.setPosition(0);
                    robot.clawright.setPosition(0.5);
                    sleep(300);
                    encoderDrive(0.2,10,10,2);
                    robot.clawleft.setPosition(0.5);
                    robot.clawright.setPosition(0);
                    encoderDrive(0.8,-10,-10,2);

                /*
                encoderDrive(0.5,5,5,5);//drive back
                robot.SideMotor.setPower(0.5);//drive to the right
                sleep(1400);
                robot.SideMotor.setPower(0);
                encoderDrive(0.5,15,15,10);
                encoderDrive(0.5, 8.3, -8.3, 5);//turn 90 degrees
*/
                    break;
                }
            }
            else {
                telemetry.addData("VuMark", "not visible");
            }

            telemetry.update();
        }
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

    public static RelicRecoveryVuMark vuMark() {
        return vuMark;
    }


    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        leftInches *= -1;
        rightInches *= -1;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.FLMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.FRMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newLeftTarget = robot.BLMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.BRMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.FLMotor.setTargetPosition(newLeftTarget);
            robot.FRMotor.setTargetPosition(newRightTarget);
            robot.BLMotor.setTargetPosition(newLeftTarget);
            robot.BRMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.BLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.BRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.FLMotor.setPower(Math.abs(speed));
            robot.FRMotor.setPower(Math.abs(speed*0.7));
            robot.BLMotor.setPower(Math.abs(speed));
            robot.BRMotor.setPower(Math.abs(speed*0.7));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.FLMotor.isBusy() && robot.FRMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.FLMotor.getCurrentPosition(),
                        robot.FRMotor.getCurrentPosition(),
                        robot.BLMotor.getCurrentPosition(),
                        robot.BRMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.FLMotor.setPower(0);
            robot.FRMotor.setPower(0);
            robot.BLMotor.setPower(0);
            robot.BRMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //  sleep(250);   // optional pause after each move
        }
    }


}

