package org.firstinspires.ftc.teamcode.Core.Utility;


import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


/**
 * Utility class used for simplifying Vuforai code for Relic Recovery
 */
public class UtilVuforia
{
    // You're not going to guess this :)
    @SuppressWarnings("FieldCanBeLocal")
    private final String _KEY = "AVL5SH7/////AAAAGQHMTrmZFkVaqLgyIFXlUot8Bj4E0gdKiux+X0Pw/1vkCO02" +
                                "YQNTTWDYnCV2DpnMYwq1E4S745dGDU0oUTRAD8XmI4WPL5MpPRCjvPPUaixm+D4u" +
                                "UrjpGH7+P8v+ZqqSKINM3NayE6lzg96ZrdLhGVnchvIpFsanlkm6hQfQeUSompiO" +
                                "XU8wZdbz+Ryc5WM4nDdI1R7VdmWZGejnMx4OgoqzAcOJbP7tO/FXdsoWFNUZGcO2" +
                                "Az0nnaAX9W5OdDy+78grYk+R9kdYu6eBNwZ/G01zlkiJM24slrCR7wlfiWtH+ywO" +
                                "ZpWYRZTGIrsrMLIk3USWLRRO/Yf0wKzyu1nHiqBuRQeVlZgm35T19WQk55FO";


    @SuppressWarnings("FieldCanBeLocal")
    private final String _MARK_ASSETS = "RelicVuMark";


    @SuppressWarnings("FieldCanBeLocal")
    private VuforiaLocalizer _vuforia;
    private VuforiaTrackables _trackables;
    private VuforiaTrackable _template;
    @SuppressWarnings("FieldCanBeLocal")
    private RelicRecoveryVuMark _vuMark;


    /**
     * Initializes Vuforia for use
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
     * Activates Vuforia
     */
    public void activate()
    {
        _trackables.activate();
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
}