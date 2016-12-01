package org.firstinspires.ftc.teamcode;
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
 * Created by Peter on 11/6/2016.
 */
public class fieldNavigator
{
    // Variables to be used for later
    private VuforiaLocalizer vuforiaLocalizer;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables visionTargets;
    private VuforiaTrackable wheelsTarget, toolsTarget, legosTarget, gearsTarget;
    private VuforiaTrackableDefaultListener wheelsListener, toolsListener, legosListener, gearsListener;
    private OpenGLMatrix lastKnownLocation = null;
    private OpenGLMatrix phoneLocation,latestLocation;
    private OmniDriveBot robot;

    public fieldNavigator(OmniDriveBot obj)
    {
        robot = obj;
    }

    private double currentX, currentY, currentDeg; //currentX and currentY are in inches

    public static final String VUFORIA_KEY = "AepnoMf/////AAAAGWsPSj5vh0WQpMc0OEApBsgbZVwduMSeEZFjXMlBPW7WiZRgwGXsOTLiGMxL4qjU0MYpZitHxs4E/nOUHseMX+SW0oopu6BnWL3cAqFIptSrdMpy4y6yB3N6l+FPcGFZxzadvRoiOfAuYIu5QMHSeulfQ1XApDhBQ79lNUXv9LZ7bngBI3BEYVB+slmTGHKhRW2NI5fUtF+rLRiou4ZcNir2eZh0OxEW4zAnTnciVB2R28yyHkYz8xJtACm+4heWLdpw/zf66LRpvTGLwkASci7ZkGJp4NrG5Of4C0b3+iq/EeEmX2PiY5lq2fkUE0dejdztmkFWYBW7c/Y+bIYGER/3gt6I8UhAB78cR7p2mOaY"; //Key used for Vuforia.

    public void setupVuforia() {
        // Setup parameters to create localizer
        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        // These are the vision targets that we want to use
        // The string needs to be the name of the appropriate .xml file in the assets folder
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");

        // Setup the target to be tracked
        wheelsTarget = visionTargets.get(0); // 0 corresponds to the wheels target
        wheelsTarget.setName("Wheels Target");
        wheelsTarget.setLocation(createMatrix(0, 1981, 32, 90, 0, 90));

        toolsTarget = visionTargets.get(1);
        toolsTarget.setName("Tools Target");
        toolsTarget.setLocation(createMatrix(914, 0, 32, 90, 0, 180));

        legosTarget = visionTargets.get(2);
        legosTarget.setName("Legos Target");
        legosTarget.setLocation(createMatrix(0, 914, 32, 90, 0, 90));

        gearsTarget = visionTargets.get(3);
        gearsTarget.setName("Gears Target");
        gearsTarget.setLocation(createMatrix(1981, 0, 32, 90, 0, 180));
        // Set phone location on robot
        phoneLocation = createMatrix(0, 0, 0, 90, 0, 0);

        // Setup listener and inform it of phone information
        wheelsListener = (VuforiaTrackableDefaultListener) wheelsTarget.getListener();
        wheelsListener.setPhoneInformation(phoneLocation, parameters.cameraDirection);

        toolsListener = (VuforiaTrackableDefaultListener) toolsTarget.getListener();
        toolsListener.setPhoneInformation(phoneLocation, parameters.cameraDirection);

        legosListener = (VuforiaTrackableDefaultListener) legosTarget.getListener();
        legosListener.setPhoneInformation(phoneLocation, parameters.cameraDirection);

        gearsListener = (VuforiaTrackableDefaultListener) gearsTarget.getListener();
        gearsListener.setPhoneInformation(phoneLocation, parameters.cameraDirection);
        visionTargets.activate();
    }

    public void visionTrack()
    {
        latestLocation = null;
            // Ask the listener for the latest information on where the robot is
        if (wheelsListener.isVisible())
        {
            latestLocation = wheelsListener.getUpdatedRobotLocation();
        }
        else if (toolsListener.isVisible())
        {
            latestLocation = toolsListener.getUpdatedRobotLocation();
        }
        else if (legosListener.isVisible())
        {
            latestLocation = legosListener.getUpdatedRobotLocation();
        }
        else if (gearsListener.isVisible())
        {
            latestLocation = gearsListener.getUpdatedRobotLocation();
        }
        if (latestLocation != null)
        {
            lastKnownLocation = latestLocation;
            updateRobotLocation();
        }
    }
    // Creates a matrix for determining the locations and orientations of objects
    // Units are millimeters for x, y, and z, and degrees for u, v, and w
    public OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w) {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }

    public double getXLocation(OpenGLMatrix matrix) //returns x value
    {
        float[] robotLocationArray = matrix.getData();
        return robotLocationArray[12];
    }

    public double getYLocation(OpenGLMatrix matrix) //Returns y value
    {
        float[] robotLocationArray = matrix.getData();
        return robotLocationArray[13];
    }

    public double convertMMToIn(double mm) {
        return mm * 0.0393701;
    }

    public double returnAngle(OpenGLMatrix robotLocationMatrix) //return robot's angle according to phone in degrees, -180 - 180
    {
        Orientation rot = Orientation.getOrientation(robotLocationMatrix, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);
        return (rot.thirdAngle * 57.2958);
    }

    public void moveToPosition(double wantedX_in, double wantedY_in, double wantedDeg)
    {

        /*double robotDeg = (-1 * currentDeg) + 90;

        double difference = (wantedX_in - currentX); //change in x

        if (difference < 0)
        { //this means that current x is greater than wanted x, so move robot in -90 deg
            //robot.driveStraight(difference, -robotDeg - 90);
        }
        else if (difference > 0)
        {
           // robot.driveStraight(difference -90 - robotDeg);
        }

        difference = wantedY_in - currentY; //change in y

        if (difference < 0) // Means current y is greater than wanted y, so the robot is above, so move down , 0 deg,
        {
         //   robot.driveStraight(difference, 180 - robotDeg);
        }
        else if (difference > 0) //move 180 deg
        {
       //     robot.driveStraight(difference, 180 - robotDeg);
        }
        //spin, positive values spin counter c, negative values for wanted deg does clock wise
        */
        robot.spin((wantedDeg - currentDeg));
    }

    public void updateRobotLocation()
    {
        currentX = convertMMToIn(getXLocation(lastKnownLocation));
        currentY = convertMMToIn(getYLocation(lastKnownLocation));
        currentDeg = returnAngle(lastKnownLocation);
    }

    public void setRobotLocation(float xIn, float yIn, float angle)
    {
        lastKnownLocation = createMatrix(inToMM(xIn), inToMM(yIn), 0, 90, 0, angle);
        updateRobotLocation();
    }


    public static float inToMM(float in)
    {
        return (float)(in * 25.4);
    }

    public boolean isDetectingTarget()
    {
        if(lastKnownLocation == null)
            return true;

        return false;
    }

    public double returnCurrentX()
    {
        return currentX;
    }

    public double returnCurrentY()
    {
        return currentY;
    }

    public double returnCurrentAngle()
    {
        return currentDeg;
    }
}
