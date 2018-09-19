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

package org.firstinspires.ftc.teamcode.Autonomus.Restul;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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
import org.firstinspires.ftc.teamcode.HardwareRobot;
import java.lang.String;

import static org.firstinspires.ftc.teamcode.HardwareRobot.COLOR_POS_DOWN;

@Autonomous(name="Functions Ana", group="TEST")
@Disabled
public class Autonomus_Functions_Ana extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime     runtime = new ElapsedTime();

    HardwareRobot robot = new HardwareRobot();
    /** VUFORIA DECLARATION **/
    OpenGLMatrix lastLocation = null;

    VuforiaLocalizer vuforia;
    public VuforiaTrackable relicTemplate;
    public static int relicCode = 0;
    /** VUFORIA DECLARATION END **/
    /** DEPLASARE DECLARATI **/
    public static int deplasare = 0;
    public static int deplasare_left = 0;
    public static int deplasare_center = 0;
    public static int deplasare_right = 0;
    static final double     DRIVE_SPEED             = 0.2;
    static final double     TURN_SPEED = 0.15;
    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // ANDYMARK_TICKS_PER_REV
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP ???????? or 2.0
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference ????????
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     COUNTE_PER_CM = COUNTS_PER_INCH / 2.54;
    public static int MIN_RED = 0;
    public static int MIN_BLUE = 0;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);
        telemetry.update();

        /** VUFORIA START INIT **/
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AddjBXL/////AAAAmcN61ZHW80IvtUwvfesWZa5JrV9AQn+mphNUco4vRSptOi8UXRpia2gnoLyZrCakLsIEUTD6Z84YWrKm3hjsUcsq8XuiTCxroeAOz4ExDes3eBcnsXsEWud++ymX1jCUgGt4sBHuRh7J0BZ+mj4ATIsXcBHf/SlWjmkKavc0vSqfwR6owMJPBzs0tv49k//jc6JJh2pKREB6YGUBUjlTsroX1qGvxxLHLTTHog1tmBe7cvsa+jQAGtn7kItK/quRF9DQqDGo9dc3UlPUbhwX5O9V4cdOt0r45C62g6Buj47mxVzzz5XurgeGYF1dMhLyl4toN5mCi03wUb+L1/X1pBGPNWwD3guQzUy7pGPjQlYw";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();
        toString();
        relicTrackables.activate();
        /** VUFORIA END INIT **/

        /**
         ******** START*******
         ******** OP**********
         ******** MODE********
         */
        while(opModeIsActive())
        {
            /** VUFORIA START OPMODE **/
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                telemetry.addData("VuMark", "%s visible", vuMark);

                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                //telemetry.addData("Pose", format(pose));
                if (vuMark == RelicRecoveryVuMark.LEFT)
                    relicCode = 1;
                if (vuMark == RelicRecoveryVuMark.CENTER)
                    relicCode = 2;
                if (vuMark == RelicRecoveryVuMark.RIGHT)
                    relicCode = 3;

                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                }
            }
            else {
                relicCode = 0;
                telemetry.addData("VuMark", "not visible");
            }
            telemetry.addData("Relic Code", relicCode);
            telemetry.update();
            /** VUFORIA END OPMODE **/
            /** RELIC CODE **/
            if (relicCode == 1)
                deplasare = deplasare_left;
            else if (relicCode == 2)
                deplasare = deplasare_center;
            else
                deplasare = deplasare_right;
            ///moveForward(distance, timeout);
            ///moveBackward(distance, timeout);
            ///turnAbsolute(grade);
            ///jewels_move(int TEAM)
        }
    }

    /**
     ******** END*********
     ******** OP**********
     ******** MODE********
     */

    ///FUNCTII
    public void turnAbsolute(int target)throws InterruptedException {

        int zAccumulated = robot.modernRoboticsI2cGyro.getIntegratedZValue();

        while (Math.abs(zAccumulated - target) > 3 && opModeIsActive() )
        {
            if (zAccumulated > target)
            {
                robot.FrontLeftMotor.setPower(TURN_SPEED);
                robot.BackLeftMotor.setPower(TURN_SPEED);
                robot.FrontRightMotor.setPower(-TURN_SPEED);
                robot.BackRightMotor.setPower(-TURN_SPEED);
            }
            else if (zAccumulated < target)
            {
                robot.FrontLeftMotor.setPower(-TURN_SPEED);
                robot.BackLeftMotor.setPower(-TURN_SPEED);
                robot.FrontRightMotor.setPower(TURN_SPEED);
                robot.BackRightMotor.setPower(TURN_SPEED);
            }

            zAccumulated = robot.modernRoboticsI2cGyro.getIntegratedZValue();
            telemetry.addData("integratedZ", zAccumulated);
        }

        robot.FrontLeftMotor.setPower(0);
        robot.BackLeftMotor.setPower(0);
        robot.FrontRightMotor.setPower(0);
        robot.BackRightMotor.setPower(0);

        telemetry.addData("integratedZ", zAccumulated);
        telemetry.update();
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

    public void moveForward(int distance, int timeout) {
        encoderDrive(DRIVE_SPEED,  0, distance, 0,  timeout);
    }

    public void moveBackward(int distance, int timeout) {
        moveForward(-distance, timeout);
    }

    public void encoderDrive(double speed, double x, double y, double z, double timeoutS) {
        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;

        double FRONT_LEFT_POWER   =   - y + z;
        double FRONT_RIGHT_POWER  =   + y + z;
        double BACK_LEFT_POWER    =   - y + z;
        double BACK_RIGHT_POWER   =   + y + z;

        /*
        double FRONT_LEFT_POWER   =   - x - y + z;
        double FRONT_RIGHT_POWER  =   - x + y + z;
        double BACK_LEFT_POWER    =     x - y + z;
        double BACK_RIGHT_POWER   =     x + y + z;
        */
        if (opModeIsActive()) {

            newFrontLeftTarget = robot.FrontLeftMotor.getCurrentPosition() + (int)(FRONT_LEFT_POWER * COUNTE_PER_CM);
            newFrontRightTarget = robot.FrontRightMotor.getCurrentPosition() + (int)(FRONT_RIGHT_POWER * COUNTE_PER_CM);
            newBackLeftTarget = robot.FrontLeftMotor.getCurrentPosition() + (int)(BACK_LEFT_POWER * COUNTE_PER_CM);
            newBackRightTarget = robot.FrontRightMotor.getCurrentPosition() + (int)(BACK_RIGHT_POWER * COUNTE_PER_CM);

            robot.FrontLeftMotor.setTargetPosition(newFrontLeftTarget);
            robot.FrontRightMotor.setTargetPosition(newFrontRightTarget);
            robot.BackLeftMotor.setTargetPosition(newBackLeftTarget);
            robot.BackRightMotor.setTargetPosition(newBackRightTarget);

            robot.FrontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.FrontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.BackLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.BackRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            robot.FrontLeftMotor.setPower(Math.abs(speed));
            robot.FrontRightMotor.setPower(Math.abs(speed));
            robot.BackLeftMotor.setPower(Math.abs(speed));
            robot.BackRightMotor.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.FrontLeftMotor.isBusy() && robot.FrontRightMotor.isBusy()
                            && robot.BackLeftMotor.isBusy() && robot.BackRightMotor.isBusy())) {

                telemetry.addData("Path1" , "TO FL: " + newFrontLeftTarget);
                telemetry.addData("Path1" , "TO FR: " + newFrontRightTarget);
                telemetry.addData("Path1" , "TO BL: " + newBackLeftTarget);
                telemetry.addData("Path1" , "TO BR: " + newBackRightTarget);
                telemetry.update();
                telemetry.addData("Path0" , "FL: " + robot.FrontLeftMotor.getCurrentPosition());
                telemetry.addData("Path0" , "FR: " + robot.FrontRightMotor.getCurrentPosition());
                telemetry.addData("Path0" , "BL: " + robot.BackLeftMotor.getCurrentPosition());
                telemetry.addData("Path0" , "BR: " + robot.BackRightMotor.getCurrentPosition());
                telemetry.update();
            }
            robot.FrontLeftMotor.setPower(DRIVE_SPEED / 2);
            robot.FrontRightMotor.setPower(DRIVE_SPEED / 2);
            robot.BackLeftMotor.setPower(DRIVE_SPEED / 2);
            robot.BackRightMotor.setPower(DRIVE_SPEED / 2);

            robot.FrontLeftMotor.setPower(DRIVE_SPEED / 4);
            robot.FrontRightMotor.setPower(DRIVE_SPEED / 4);
            robot.BackLeftMotor.setPower(DRIVE_SPEED / 4);
            robot.BackRightMotor.setPower(DRIVE_SPEED / 4);

            robot.FrontLeftMotor.setPower(0);
            robot.FrontRightMotor.setPower(0);
            robot.BackLeftMotor.setPower(0);
            robot.BackRightMotor.setPower(0);

            robot.FrontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.FrontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.BackLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.BackRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            sleep(500);
        }
    }

    public int get_color(int TEAM) {
        int answer = 0;
        ///1 dreapta
        ///-1 stanga
        if(TEAM == 1) ///RED
        {
            if(robot.colorSensor.red() > MIN_RED)
                answer = 1;
            else if( robot.colorSensor.blue() > MIN_BLUE)
                answer = -1;
        }
        else if(TEAM == 0) ///BLUE
        {
            if(robot.colorSensor.red() > MIN_RED)
                answer = -1;
            else if(robot.colorSensor.blue() > MIN_BLUE)
                answer = 1;
        }
        return answer;
    }

    public void jewels_move(int TEAM)throws InterruptedException {
        robot.Color_Hand.setPosition(COLOR_POS_DOWN);
        idle();
        sleep(500);
        int dir = get_color(TEAM) * 30;
        sleep(500);
        turnAbsolute(dir);
        sleep(500);
        turnAbsolute(0);
        sleep(500);
    }
}

