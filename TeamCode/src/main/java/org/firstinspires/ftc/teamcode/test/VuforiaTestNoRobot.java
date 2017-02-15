package org.firstinspires.ftc.teamcode.test;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;

/**
 * Created by 292486 on 11/17/2016.
 */

/*
adb tcpip 555
adb connect [[192.168.0.0]]:5555

adb -s [[192.168.0.0]]:5555 usb

netsh wlan set hostednetwork mode=allow ssid=something key=xxxxxxxxxx
netsh wlan start hostednetwork
DIRECT-xx-YourRobotName
 */

@Autonomous
public class VuforiaTestNoRobot extends LinearOpMode {

    VuforiaLocalizer vuforia;
    private ElapsedTime timer;

    @Override
    public void runOpMode() throws InterruptedException {
        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        //params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "ARWfao3/////AAAAGShXvVW3Tk2+vd1uaNWPhiQakvDy25FBcrepFqO+jhVLsOEPsRlb061njMk5dQFc4Le7eTp32TTpn3eD2rXL3OeskF6Mj+9sY1KDzlUUtg8fLGhqsHZE96flU0bx4iKGX0PXv8k0C9XrKJzFi1c592kYK1O0+XEGLT7/7GAlsEBzMREFoHVvGavtleVRV0O78buXIDU/ihmuYcYFQZCTCdZ2o7+heZN8DJYpfBWSLSIFwHoVVf9QlchrJaYP8hKpN0sRL7aXmNybILOKZ23l/hbi4cvBueJUP44DRKaXvNvg8YHFUudAH1dmV0pzIgLtUrHWkUBW9AcVnjxR0FF9TPWrDAvYtcoHE66lyWyCYxHH\n";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;  //Teapot, buildings, feedback when found beacon

        vuforia = ClassFactory.createVuforiaLocalizer(params);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);

        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Legos");
        beacons.get(2).setName("Tools");
        beacons.get(3).setName("Gears");

        waitForStart();
        timer.reset();

        telemetry.addData("VUFORIA ", "done waiting");
        updateTelemetry(telemetry);
        //beacons.activate();
        beacons.activate();

        telemetry.addData("VUFORIA ", "beacons activated");
        updateTelemetry(telemetry);
            /*for(VuforiaTrackable beacon : beacons) {


                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beacon.getListener()).getPose();
                if (pose != null) {
                    VectorF translation = pose.getTranslation();
                    telemetry.addData("Translation [" + beacon.getName() + "] ", translation);
                    double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));
                    telemetry.addData("Team-Degrees", degreesToTurn);
                }

            }*/
            while(!moveToward(beacons.get(0), 10000))
            {

            }
                /*
            for(VuforiaTrackable beac : beacons){
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getPose();

                if(pose != null){
                    VectorF translation = pose.getTranslation();

                    telemetry.addData(beac.getName() + "-Translation", translation);

                    double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));  //0 and 1 for landscape

                    telemetry.addData(beac.getName() + "-Degrees", degreesToTurn);
                }
            }
            */
    }

    private static final double targetX = 100;
    private static final double targetY = 100;
    private static final double targetZ = -100;
    private static final double kp = .001;

    private boolean moveToward(VuforiaTrackable beacon, long time)
    {
        time += timer.time();
        double diffX, diffY, diffZ;
        double ddX=0, ddY=0, ddZ=0;
        double base = .1;
        OpenGLMatrix position = ((VuforiaTrackableDefaultListener) beacon.getListener()).getPose();
        if(position != null && position.getTranslation() != null) {
            //translation.get(0); //x 0; y 1; z 2
            VectorF translation = position.getTranslation();

            while ((Math.abs(translation.get(0)) > targetX || Math.abs(translation.get(1)) > targetY) || timer.time() < time) {
                if (translation != null) {
                    diffX = (targetX - translation.get(0)) * kp;
                    diffY = (targetY - translation.get(1)) * kp;
                    //diffZ = (targetZ - translation.get(2)) * kp;

                    if (diffX - ddX < 0) {
                        //Log.i("BEACON TRACKING " + beacon.getName(), " X-Decreasing TRUE");
                        telemetry.addData("BEACON TRACKING " + beacon.getName(), " X-Decreasing TRUE");
                    } else {
                        //Log.i("BEACON TRACKING " + beacon.getName(), " X-Decr FALSE");
                        telemetry.addData("BEACON TRACKING " + beacon.getName(), " FALSE");
                    }
                    if (diffY - ddY < 0) {
                        //Log.i("BEACON TRACKING " + beacon.getName(), " Y-Decreasing TRUE");
                        telemetry.addData("BEACON TRACKING " + beacon.getName(), " Y-Decreasing TRUE");
                    } else {
                        //Log.i("BEACON TRACKING " + beacon.getName(), " Y-Decr FALSE");
                        telemetry.addData("BEACON TRACKING " + beacon.getName(), " FALSE");
                    }
                    ddX = diffX;
                    ddY = diffY;

                    telemetry.addData(beacon.getName(), translation);
                    updateTelemetry(telemetry);
                }
                translation = position.getTranslation();
            }
            telemetry.addData("DONE! ", "Finished while loop");
            telemetry.update();
            return true;
        } else {
            telemetry.addData("DONE! ", "IF was FALSE");
            telemetry.update();
            return false;
        }
    }
}
