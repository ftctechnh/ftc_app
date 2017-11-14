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
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaNavigation;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * This OpMode illustrates the basics of using the Vuforia engine to determine
 * the identity of Vuforia VuMarks encountered on the field. The code is structured as
 * a LinearOpMode. It shares much structure with {@link ConceptVuforiaNavigation}; we do not here
 * duplicate the core Vuforia documentation found there, but rather instead focus on the
 * differences between the use of Vuforia for navigation vs VuMark identification.
 *
 * @see ConceptVuforiaNavigation
 * @see VuforiaLocalizer
 * @see VuforiaTrackableDefaultListener
 * see  ftc_app/doc/tutorial/FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained in {@link ConceptVuforiaNavigation}.
 */

@Autonomous(name="Blue1Auto", group ="Pushbot")
//@Disabled
public class Blue1Auto extends LinearOpMode {

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
         * If no camera monyitor is dyesired, yuse the parameterless constructor instead (commented out below).
         */

        robot.init(hardwareMap);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "ARjW6VD/////AAAAGbCMKpMCSEgSunPcA5cUQkuEKymuh9/mOQ5b+ngfYCdx3gPONkD3mscU39FUD7mRQRZSRZpjHZfohKwL2PYsVZrBcTlaY1JcJ9J5orZKqTxxy68irqEBuQkkfG72xEEPYuNq+yEJCNzYKhx3wFGqUV1H05Z1fFJa1ZiWfe4Tn9aO2Yf5AIkYCMz4K75LFU3ZM1wCgz9ubLhxZH2BWF9X0rhvnhZS2rnLHkxm+C+xzRbs2ZoGCOpDRb3Dy0iMG2y4Ve9/AApZQ+6sgSwlc9liA5jZ0QyT0dLqyfaoXwNxPqzBjhOj3FltEHxrWPdpOQm6B8BDC9Kv+BShnpi6g3yhf+msI3Qeqsns/nm6DrGF5zum";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
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

        while (!gamepad1.a) {

            if (gamepad1.right_bumper) {

                Dis = Dis + 50;
                sleep(500);
            }

            if (gamepad1.left_bumper) {
                Dis = Dis - 50;
                sleep(500);
            }
            telemetry.addData("Dis",Dis);
            telemetry.update();
        }

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


                robot.armleft.setPosition(0); //your mother
                sleep(300);
                robot.SideMotor.setPower(0.3);
                sleep(238);
                robot.SideMotor.setPower(0);
                sleep(100);
                robot.armleft.setPosition(0.75);
                sleep(300);
                encoderDrive(0.5, -48, -48, 1.0);  // (Power, Distance Left, Distance Right (INCHES), timeout)
                robot.armleft.setPosition(0);
                sleep(1000);     // pause for servos to move
                //encoderDrive(0.5, 48, -48, 0.3);  // (Power, Distance Left, Distance Right (INCHES), timeout)
                encoderDrive(0.5, -48, -48, 2.0);// (Power, Distance Left, Distance Right (INCHES), timeout)
                robot.SideMotor.setPower(-0.5);
                sleep(200);
                robot.SideMotor.setPower(0);
                encoderDrive(0.5, 20.0, -20.0, 1.0);

                if (vuMark == RelicRecoveryVuMark.RIGHT){
                    //encoderDrive(0.5, -11, 11, 0.4);
                    encoderDrive(1, 5, 5, 0.3);
                    break;
                }
                if (vuMark == RelicRecoveryVuMark.CENTER){
                    //encoderDrive(0.5, -11, 11, 0.4);
                    encoderDrive(1, 5, 5, 0.3);
                    robot.SideMotor.setPower(-1);
                    sleep(600);
                    robot.SideMotor.setPower(0);
                    encoderDrive(0.2, 10, 10, 5);
                    Dis = 0;
                    break;
                }
                if (vuMark == RelicRecoveryVuMark.LEFT){
                    //encoderDrive(0.5, 10, -10, 0.3);
                    encoderDrive(1, 5, 5, 0.3);
                    robot.SideMotor.setPower(-1);
                    sleep(750);
                    robot.SideMotor.setPower(0);
                    encoderDrive(0.2, 20, 20, 5);
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

