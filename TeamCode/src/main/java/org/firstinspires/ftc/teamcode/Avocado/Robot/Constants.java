package org.firstinspires.ftc.teamcode.Avocado.Robot;

import org.firstinspires.ftc.teamcode.Salsa.Vision.CameraCropAngle;

/**
 * Created by adityamavalankar on 11/4/18.
 */

public abstract class Constants {

    /**
     * The constants program is meant to make things consistent throughout our programs, without much fiddling with the code.
     * The benefit of having constant names is that one configuration file works with everything!
     */
    public final String LEFT_FRONT_NAME = "leftFront";
    public final String RIGHT_FRONT_NAME = "rightFront";
    public final String LEFT_BACK_NAME = "leftBack";
    public final String RIGHT_BACK_NAME = "rightBack";
    public final String MINERAL_SHOOTER_NAME = "mineralShooter";
    public final String CRATER_SLIDES_NAME = "craterSlides";
    public final String INTAKE_MOTOR_NAME = "intakeMotor";
    public final String LIFT_SLIDES_NAME = "liftSlides";


    //names for the sensors
    public final String GYRO_NAME = "imu";
    public final String WEBCAM_FRONT_NAME = "webcamFront";
    public final String LEFT_COLOR_NAME = "left_line";
    public final String RIGHT_COLOR_NAME = "right_line";
    public final String GROUND_DISTANCE_SENSOR_NAME = "groundDistance";

    //names for servos
    public final String LEFT_MINERAL_NAME = "leftMineral";
    public final String RIGHT_MINERAL_NAME = "rightMineral";
    public final String MINERAL_FEEDER_NAME = "mineralFeeder";
    public final String DEPOSITER_ROTATE_NAME = "depositerRotate";
    public final String DEPOSITER_DUMP_NAME = "depositerDump";
    public final String INTAKE_LIFTER_NAME = "intakeLifter";
    public final String MARKER_DEPOSITER_NAME = "maerkerDepositer";

    //Vuforia license key, DO NOT TOUCH!
    public final String VUFORIA_KEY = "AffveYv/////AAAAGQ5VbB9zQUgjlHWrneVac2MnNgfMDlq6EwI3tyURgRK6C" +
            "HargOTFidfzKod6GLQwGD4m9MPLkR+0NfUrnY8+o8FqAKcQbrAsjk8ONdkWYTPZDfoBRgDLNWRuB7LU1MOp9KqAWpXB" +
            "JjvH5JCKF/Hxz+beHfVqdWQ0BVZdgGMXG4yEzLN5AI+4NIkQeLvI7Cwz5pIlksoH+rb/e6+YExoWZbQWhDTiRiemlWjvDM" +
            "1z2a0kteGDz0wTyHz48IkV4M0YsSQIFKwu3YB2a1vkB9FiRfMrBI+CyInjgNoO8V0EEOtRc6Vqsf3XbF3fGXricZUhl7RIl5" +
            "M/IkFOgeAZ4ML+JcrjTqfZb2Yh3JNx1me524cK";
    public final CameraCropAngle CAMERA_AIM_DIRECTION = CameraCropAngle.RIGHT;



    //constant numbers meant for autonomous with encoders
    public final double TICKS_PER_ROTATION = 1120;

    public final double WHEEL_DIAMETER_CM = 10.16;
    public final double WHEEL_DIAMETER_IN = 4;
    public final double WHEEL_CIRCUMFERENCE_CM = (Math.PI * WHEEL_DIAMETER_CM);
    public final double WHEEL_CIRCUMFERENCE_IN = (Math.PI * WHEEL_DIAMETER_IN);

    public final double TICKS_PER_CM = (TICKS_PER_ROTATION)/(WHEEL_CIRCUMFERENCE_CM);
    public final double TICKS_PER_IN = (TICKS_PER_ROTATION)/(WHEEL_CIRCUMFERENCE_IN);

    public final double ENC_DRIVE_TIME_MULTIPLIER = 1.5;
    public final int NEVEREST_40_RPM = 160;

    public final int LIFT_SLIDES_REVERSE_CONSTANT = 1;

}
