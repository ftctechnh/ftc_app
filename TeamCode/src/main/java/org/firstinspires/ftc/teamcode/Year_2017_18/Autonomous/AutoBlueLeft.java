package org.firstinspires.ftc.teamcode.Year_2017_18.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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
import org.firstinspires.ftc.teamcode.Year_2017_18.Robot.RobotCommands;

import static com.sun.tools.javac.util.Constants.format;

// Motor: NeveRest 40 each tick is 0.01682996064 of an inch.
//Motor: NeveRest 40 each 1120 ticks is 18.8495559168 in.
// Servos: Deluxe HItec HS-485HB

/*
 *                 README: Commands
 *--------------------------------------------------
 * //For driving.
 * myRobot.drive(leftSpeed, rightSpeed, timeInMilliseconds);
 *
 * //For the main claws.
 * myRobot.pulley(Speed, timeInMilliseconds);
 * myRobot.claw_grab();
 * myRobot.claw_release();
 *
 * //For the sensor claw.
 * myRobot.sensor_left();
 * myRobot.sensor_right();
 */

@Autonomous(name = "AutoBlueLeft", group = "AutoMode")

public class AutoBlueLeft extends LinearOpMode {
    RobotCommands myRobot = new RobotCommands();
    public VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() throws InterruptedException {
        myRobot.setHardwareMap(hardwareMap);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AYopJqz/////AAAAGd7dq6/IAUxmkk2YPfuDDJIScTBeUahXwWW5cAm30qYh06uJbRTEnM8XQZqYPwsQ8r+kp4FkQIU8LqTpk3C+FN8F1W7b/LKOCPXqa33IVgjfo6jzH7fi3mtIhfmtAFv3Tl01wDI2iKamifnWinRD829j20xg6hRGb2xpdNz1c2YiOuQJ+wGPW/orzwsvGObXwz1LOxY9YcSI8ZbWf8QtFoez/iy0sI4ESLDeVysCqBYb3mYLY917Z5rOnppu/9pN1ytcgL4Qv/Qb+wX0oHcxwnUACGjEMQV0AxYY9ENrO+5VuxDTEQYhN/CSo6aQjfwEQrOe2GkAsntyriI/RsKQ+kocIhkGXFKQ3A9A0Ots1Y4K";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTrackables.activate();

        telemetry.addData("Status", "The robot has successfully initialized! ");
        telemetry.update();

        waitForStart();
        telemetry.addData("Status", "The robot has started running!");
        telemetry.update();

        myRobot.claw_release();  //Keeps a firm grip on the glyph.
        sleep(100);
        myRobot.claw_grab();
        sleep(100);
        myRobot.pulley(1.0f, 500);
        sleep(100);
        myRobot.color_rotate(0.6); //Sets the Rotation of the color arm to the center.
        sleep(100);
        myRobot.sensor_left(); //Raise the sensor arm down.
        sleep(4000);

        if (myRobot.hardware.colorSensor.blue() > myRobot.hardware.colorSensor.red()) {           //What to do if the jewel is blue.
            telemetry.addData("Color Sensor", "sees blue!");
            telemetry.update();
            myRobot.color_rotate(1);
            sleep(300);
            myRobot.sensor_middle();
            sleep(1000);
            myRobot.color_rotate(0.75);
            sleep(100);
            myRobot.sensor_right();
            sleep(5000);
        } else if (myRobot.hardware.colorSensor.red() > myRobot.hardware.colorSensor.blue()) {      //What to do if the jewel is red.
            telemetry.addData("Color Sensor", "sees red!");
            telemetry.update();
            myRobot.color_rotate(0);
            sleep(300);
            myRobot.sensor_middle();
            sleep(1000);
            myRobot.color_rotate(0.75);
            sleep(100);
            myRobot.sensor_right();
            sleep(5000);
        }
        while (opModeIsActive())
        {
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose(); // Get Positional value to use later
            telemetry.addData("Pose", format(pose));
            if (pose != null)
            {
                VectorF trans = pose.getTranslation();
                Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
            }
            sleep(2000);
            telemetry.update();
            if (vuMark == RelicRecoveryVuMark.LEFT)
            { // Test to see if Image is the "LEFT" image and display value.
                telemetry.addData("VuMark is", "Left");
                telemetry.update();
                myRobot.drive(0.15, 0.1, 3000);
                sleep(100);
                myRobot.claw_release();
                sleep(300);
                myRobot.drive(-.26, -.21, 330);
                sleep(30000);
            } else if (vuMark == RelicRecoveryVuMark.CENTER)
            { // Test to see if Image is the "CENTER" image and display values
                telemetry.addData("VuMark is", "Center");
                telemetry.update();
                myRobot.drive(0.2, 0.1, 3000);
                sleep(100);
                myRobot.claw_release();
                sleep(300);
                myRobot.drive(-.26, -.21, 330);
                sleep(30000);
            } else if (vuMark == RelicRecoveryVuMark.RIGHT)
            { // Test to see if Image is the "RIGHT" image and display values.
                telemetry.addData("VuMark is", "Right");
                telemetry.update();
                myRobot.drive(0.25, 0.1, 3000);
                sleep(100);
                myRobot.claw_release();
                sleep(300);
                myRobot.drive(-.26, -.21, 330);
                sleep(30000);
            } else {
                telemetry.addData("VuMark is", "Not Visible");
                telemetry.update();
                myRobot.drive(0.15, 0.1, 3000);
                sleep(100);
                myRobot.claw_release();
                sleep(300);
                myRobot.drive(-.26, -.21, 330);
                sleep(30000);
            }
        }
        telemetry.update();
    }
    String format(OpenGLMatrix transformationMatrix)
    {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}