package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Jeremy on 10/6/2016.
 * Using template created by Fromen8923 on Github
 * https://github.com/SwerveRoboticsTeams/ftc_app/blob/Tutorials/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Vuforia.java#L3
 *
 */

@TeleOp(name = "TestVuforia", group = "Concept")
public class VuforiaTest extends LinearOpMode
{
    // Variables to be used for later
    VuforiaLocalizer vuforiaLocalizer;
    VuforiaLocalizer.Parameters parameters;
    VuforiaTrackables visionTargets;
    VuforiaTrackable wheelsTarget, toolsTarget, legosTarget, gearsTarget;
    VuforiaTrackableDefaultListener wheelsListener, toolsListener, legosListener, gearsListener;

    OpenGLMatrix lastKnownLocation;
    OpenGLMatrix phoneLocation;

    public static final String VUFORIA_KEY = "AepnoMf/////AAAAGWsPSj5vh0WQpMc0OEApBsgbZVwduMSeEZFjXMlBPW7WiZRgwGXsOTLiGMxL4qjU0MYpZitHxs4E/nOUHseMX+SW0oopu6BnWL3cAqFIptSrdMpy4y6yB3N6l+FPcGFZxzadvRoiOfAuYIu5QMHSeulfQ1XApDhBQ79lNUXv9LZ7bngBI3BEYVB+slmTGHKhRW2NI5fUtF+rLRiou4ZcNir2eZh0OxEW4zAnTnciVB2R28yyHkYz8xJtACm+4heWLdpw/zf66LRpvTGLwkASci7ZkGJp4NrG5Of4C0b3+iq/EeEmX2PiY5lq2fkUE0dejdztmkFWYBW7c/Y+bIYGER/3gt6I8UhAB78cR7p2mOaY"; //Key used for Vuforia.

    public void runOpMode() throws InterruptedException
    {
        setupVuforia();

        // We don't know where the robot is, so set it to the origin
        // If we don't include this, it would be null, which would cause errors later on
        lastKnownLocation = createMatrix(0, 0, 0, 0, 0, 0);

        waitForStart();

        // Start tracking the targets
        visionTargets.activate();

        while(opModeIsActive())
        {
            // Ask the listener for the latest information on where the robot is
            if (wheelsListener.isVisible()) {
                OpenGLMatrix latestLocation = wheelsListener.getUpdatedRobotLocation();

                // The listener will sometimes return null, so we check for that to prevent errors
                if (latestLocation != null)
                    lastKnownLocation = latestLocation;

                // Send information about whether the target is visible, and where the robot is
                telemetry.addData("Tracking " + target.getName(), listener.isVisible());
                telemetry.addData("Last Known Location", formatMatrix(lastKnownLocation));

                // Send telemetry and idle to let hardware catch up
                telemetry.update();
                idle();
            }
            else if (toolsListener.isVisible())
            {

            }
            else if (legosListener.isVisible())
            {

            }
            else if (gearsListener.isVisible())
            {

            }
            else
            {

            }

        }
    }

    public void setupVuforia()
    {
        // Setup parameters to create localizer
        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        // These are the vision targets that we want to use
        // The string needs to be the name of the appropriate .xml file in the assets folder
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");

        // Setup the target to be tracked
        wheelsTarget = visionTargets.get(0); // 0 corresponds to the wheels target
        wheelsTarget.setName("Wheels Target");
        wheelsTarget.setLocation(createMatrix(0, 1981, 0, 90, 0, 90));

        toolsTarget = visionTargets.get(1);
        toolsTarget.setName("Tools Target");
        toolsTarget.setLocation(createMatrix(914, 0, 0, 90, 0, 90));

        legosTarget = visionTargets.get(1);
        legosTarget.setName("Legos Target");
        legosTarget.setLocation(createMatrix(0, 914, 0, 90, 0, 90));

        gearsTarget = visionTargets.get(1);
        gearsTarget.setName("Gears Target");
        gearsTarget.setLocation(createMatrix(1981, 0, 0, 90, 0, 90));
        // Set phone location on robot
        phoneLocation = createMatrix(0, 225, 0, 90, 0, 0);

        // Setup listener and inform it of phone information
        wheelsListener = (VuforiaTrackableDefaultListener) wheelsTarget.getListener();
        wheelsListener.setPhoneInformation(phoneLocation, parameters.cameraDirection);

        toolsListener = (VuforiaTrackableDefaultListener) toolsTarget.getListener();
        toolsListener.setPhoneInformation(phoneLocation, parameters.cameraDirection);

        legosListener = (VuforiaTrackableDefaultListener) legosTarget.getListener();
        legosListener.setPhoneInformation(phoneLocation, parameters.cameraDirection);

        gearsListener = (VuforiaTrackableDefaultListener) gearsTarget.getListener();
        gearsListener.setPhoneInformation(phoneLocation, parameters.cameraDirection);
    }

    // Creates a matrix for determining the locations and orientations of objects
    // Units are millimeters for x, y, and z, and degrees for u, v, and w
    public OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w)
    {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }

    // Formats a matrix into a readable string
    public String formatMatrix(OpenGLMatrix matrix)
    {
        return matrix.formatAsTransform();
    }
}
