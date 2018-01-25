/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;


import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
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
 * Utility class used for simplifying Vuforia code for Relic Recovery- provides for easy and
 * simplified retrieval of data.
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class Vuforia
{
    // You're not going to guess this :)
    private final String _KEY = "AVL5SH7/////AAAAGQHMTrmZFkVaqLgyIFXlUot8Bj4E0gdKiux+X0Pw/1vkCO02" +
                                "YQNTTWDYnCV2DpnMYwq1E4S745dGDU0oUTRAD8XmI4WPL5MpPRCjvPPUaixm+D4u" +
                                "UrjpGH7+P8v+ZqqSKINM3NayE6lzg96ZrdLhGVnchvIpFsanlkm6hQfQeUSompiO" +
                                "XU8wZdbz+Ryc5WM4nDdI1R7VdmWZGejnMx4OgoqzAcOJbP7tO/FXdsoWFNUZGcO2" +
                                "Az0nnaAX9W5OdDy+78grYk+R9kdYu6eBNwZ/G01zlkiJM24slrCR7wlfiWtH+ywO" +
                                "ZpWYRZTGIrsrMLIk3USWLRRO/Yf0wKzyu1nHiqBuRQeVlZgm35T19WQk55FO";

    private final String _MARK_ASSETS = "RelicVuMark";

    static final double DEFAULT_TRANS = -100;
    static final double DEFAULT_ROTATE = 370;

    private VuforiaLocalizer _vuforia;
    private VuforiaTrackables _trackables;
    private VuforiaTrackable _template;
    private RelicRecoveryVuMark _vuMark;
    private OpenGLMatrix _pose;


    /**
     * Initializes Vuforia for use. Call this first.
     *
     * @param HW The OpMode's HardwareMap object- in your opmode, just put in "hardwareMap".
     *
     * @param CAM_DIR Direction of the camera to use. Typically, the back camera produces better
     *                image quality.
     */
    public void init(final HardwareMap HW , final VuforiaLocalizer.CameraDirection CAM_DIR)
    {
        int cameraViewId = HW.appContext.getResources().getIdentifier("cameraMonitorViewId" , "id" ,
                           HW.appContext.getPackageName());

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(cameraViewId);

        params.vuforiaLicenseKey = _KEY;
        params.cameraDirection = CAM_DIR;

        _vuforia = ClassFactory.createVuforiaLocalizer(params);

        _trackables = _vuforia.loadTrackablesFromAsset(_MARK_ASSETS);
        _template = _trackables.get(0);
    }


    /**
     * Activates Vuforia. Call this directly after initialization.
     */
    public void activate()
    {
        _trackables.activate();
    }


    /**
     * Deactivates Vuforia
     */
    public void deactivate()
    {
        _trackables.deactivate();
    }


    /**
     * Returns the current marker seen.
     *
     * @return Enumeration
     */
    public RelicRecoveryVuMark currentMarker()
    {
        _vuMark = RelicRecoveryVuMark.from(_template);

        return _vuMark;
    }


    /**
     * Reads markers, sets the pose. Pose is null if nothing is found
     */
    public void readMarker()
    {
        _pose =  ((VuforiaTrackableDefaultListener)_template.getListener()).getPose();
    }


    /**
     * @return Returns the angle from the Vuforia marker (forward to backward pitch).
     */
    public double xAngle()
    {
        if(_pose != null)
        {
            return Orientation.getOrientation(_pose , AxesReference.EXTRINSIC , AxesOrder.XYZ ,
                                                AngleUnit.DEGREES).firstAngle;
        }

        return DEFAULT_ROTATE;
    }


    /**
     * @return Returns the angle from the Vuforia marker (side to side pitch).
     */
    public double yAngle()
    {
        if(_pose != null)
        {
            return Orientation.getOrientation(_pose , AxesReference.EXTRINSIC , AxesOrder.XYZ ,
                    AngleUnit.DEGREES).secondAngle;
        }

        return DEFAULT_ROTATE;
    }


    /**
     * @return Returns the angle from the Vuforia marker (side to side rotation).
     */
    public double zAngle()
    {
        if(_pose != null)
        {
            return Orientation.getOrientation(_pose , AxesReference.EXTRINSIC , AxesOrder.XYZ ,
                    AngleUnit.DEGREES).thirdAngle;
        }

        return DEFAULT_ROTATE;
    }


    /**
     * @return Returns the distance from the Vuforia marker (left to right direction).
     */
    public double xTrans()
    {
        if(_pose != null)
        {
            return _pose.getTranslation().get(0);
        }

        return DEFAULT_TRANS;
    }


    /**
     * @return Returns the distance from the Vuforia marker (up to down direction).
     */
    public double yTrans()
    {
        if(_pose != null)
        {
            return _pose.getTranslation().get(1);
        }

        return DEFAULT_TRANS;
    }


    /**
     * @return Returns the distance from the Vuforia marker (forward to backward direction).
     */
    public double zTrans()
    {
        if(_pose != null)
        {
            return _pose.getTranslation().get(2);
        }

        return DEFAULT_TRANS;
    }
}