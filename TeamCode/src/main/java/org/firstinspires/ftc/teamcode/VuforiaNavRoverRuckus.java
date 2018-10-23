package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

import java.util.ArrayList;
import java.util.List;

@TeleOP(name="Vuforia Nav", group="Linear OpMode")
//@Disabled
public class VuforiaNav extends LinearOpMode  {

    //Vuforia Key to use the program
    private static final String VUFORIA_KEY = "AYF223v/////AAABmfNmdY7wQEWOtnHRqbf9/zkjwk4qXoKsKyftQI0oXarKVtaL18uhqXwgwqN4hqE11yd1YTTJtd3mUCoO+vUS6NqXLQE6oZ8GYw/EEAqbnjxrChXmAyNVa8Oz4slaFSarcPIlZH8NFXWkJbCgJm/7mtMfT6yYd0q2uwkjaFVQ+2V7mjXkbip19xPaXNfEDPpv36/g8wfMOU4RMKmhJEu2cVKPqgBQ/iHWu3FdS+ehsjRcsRftnMbnBOPT6FtZmNfh+JhJg15QAVzpobc/ER5ai1Fl/Wl1HDQgxxZUdzdckWZRJ49E1zdBz2ghhDcRU3J/qvsVaSmyGi5rvgJUyFMngBzo6GJJMYpgy3Es5PBt1VOJ"

    //measurements

    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;  //width
    private static final float mmTargetHeight   = (6) * mmPerInch;  //height

    //front or back camera
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;

    private OpenGLMatrix lastLocation = null;
    private boolean targetVisible = false;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    VuforiaLocalizer vuforia;

    @override public void runOpMode() {

        parameters.vuforiaLicenseKey = VUFORIA_KEY ;


    }



}