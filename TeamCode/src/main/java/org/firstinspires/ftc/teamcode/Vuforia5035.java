package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;



/*
 * This OpMode was written for the Vuforia Basics video. This demonstrates basic principles of
 * using Vuforia in FTC.
 */
@Autonomous(name = "Vuforia5035")
public class Vuforia5035 extends LinearOpMode
{

        VuforiaLocalizer vuforia;

        @Override public void runOpMode() throws InterruptedException {
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
            parameters.vuforiaLicenseKey = "AfXb48r/////AAAAGeD2f/Vqr091sIpDI7RLaYYXDM4Ao03klW8aOZpnKhW7owlW94atv0FpmrIMSu8f15XxGzIXZa9xjWrEw+Cqnea2mZE/FuHbD6WUGnU1Mwyy8CzejVRQV0dTu2Y/KuS9nxcCMcMDKnH3OZjFZYJLPgJ3TqqgL47MkEszN/iS8LKg82rPhB81mh3t5c7ZohzPRNDhvrgUOQHruNu+7YcjilNMbtqBGutFkNxJ5qSbA1WajcXwIrgMwvQFDMnr3O1kqo5Mks4lYReyNvczQ4I7TZuRtqox4SzZf9hJN7EfuOGVwRX8YdOTyMMOnekK7lJSNbdydaTQA3ye0eLxO90kOX1zOhexEzGO9WPFiG3hN/s4";
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
            parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;


            this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
            Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);


            VuforiaTrackables Beacons = this.vuforia.loadTrackablesFromAsset("FTC_2016-17");

            VuforiaTrackable WheelTarget = Beacons.get(0);
            WheelTarget.setName("wheeltarget");  // wheels

            VuforiaTrackable ToolTarget  = Beacons.get(1);
            ToolTarget.setName("tooltarget");  // tools

            VuforiaTrackable LegosTarget  = Beacons.get(2);
            LegosTarget.setName("legostarget");  // tools

            VuforiaTrackable GearsTarget  = Beacons.get(3);
            GearsTarget.setName("gearstarget");  // tools



            /**
             * We use units of mm here because that's the recommended units of measurement for the
             * size values specified in the XML for the ImageTarget trackables in data sets. E.g.:
             *      <ImageTarget name="stones" size="247 173"/>
             * You don't *have to* use mm here, but the units here and the units used in the XML
             * target configuration files *must* correspond for the math to work out correctly.
             */
            float mmPerInch        = 25.4f;
            float mmBotWidth       = 18 * mmPerInch;            // ... or whatever is right for your robot
            float mmFTCFieldWidth  = (12*12 - 2) * mmPerInch;   // the FTC field is ~11'10" center-to-center of the glass panels

            waitForStart();

            /** Start tracking the data sets we care about. */
            Beacons.activate();

            while (opModeIsActive()) {

                for (VuforiaTrackable trackable : Beacons) {
                    /**
                     * getUpdatedRobotLocation() will return null if no new information is available since
                     * the last time that call was made, or if the trackable is not currently visible.
                     * getRobotLocation() will return null if the trackable is not currently visible.
                     */
                    telemetry.addData(trackable.getName(), ((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible() ? "Visible" : "Not Visible");    //

                    OpenGLMatrix beaconPose = ((VuforiaTrackableDefaultListener)trackable.getListener()).getPose();
                    if (beaconPose != null) {
                        VectorF translation = beaconPose.getTranslation();

                        telemetry.addData(trackable.getName() + "-Translation", translation);

                        double heading = Math.toDegrees(Math.atan2(beaconPose.get(0, 1), beaconPose.get(0, 0)));
                        telemetry.addData(trackable.getName() + "-heading", heading);

                        double degreesToTurn = Math.toDegrees(Math.atan2(-translation.get(1), -translation.get(2)));

                        telemetry.addData(trackable.getName() + "-Degrees", degreesToTurn);
                    }
                }
                telemetry.update();
                idle();
            }
        }
}
