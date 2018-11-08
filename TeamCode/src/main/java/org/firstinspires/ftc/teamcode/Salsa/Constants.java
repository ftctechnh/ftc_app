package org.firstinspires.ftc.teamcode.Salsa;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by adityamavalankar on 11/4/18.
 */

public class Constants {

    //these are the names for the drivetrain motors in the HardwareMap()
    //This allows you to modify them without severe repercussions, and it keeps nomenclature consistent among the various programs
    //If you modify the name here, you must update on the driver station/robot controller
    public final String LEFT_FRONT_NAME = "leftFront";
    public final String RIGHT_FRONT_NAME = "rightFront";
    public final String LEFT_BACK_NAME = "leftBack";
    public final String RIGHT_BACK_NAME = "rightBack";


    //names for the sensors
    public final String GYRO_NAME = "imu";
    public final String WEBCAM_FRONT_NAME = "webcam_front";
    public final String LEFT_COLOR_NAME = "left_line";
    public final String RIGHT_COLOR_NAME = "right_line";

    //Vuforia license key, DO NOT TOUCH!
    public final String VUFORIA_KEY = "AffveYv/////AAAAGQ5VbB9zQUgjlHWrneVac2MnNgfMDlq6EwI3tyURgRK6CHargOTFidfzKod6GLQwGD4m9MPLkR+0NfUrnY8+o8FqAKcQbrAsjk8ONdkWYTPZDfoBRgDLNWRuB7LU1MOp9KqAWpXBJjvH5JCKF/Hxz+beHfVqdWQ0BVZdgGMXG4yEzLN5AI+4NIkQeLvI7Cwz5pIlksoH+rb/e6+YExoWZbQWhDTiRiemlWjvDM1z2a0kteGDz0wTyHz48IkV4M0YsSQIFKwu3YB2a1vkB9FiRfMrBI+CyInjgNoO8V0EEOtRc6Vqsf3XbF3fGXricZUhl7RIl5M/IkFOgeAZ4ML+JcrjTqfZb2Yh3JNx1me524cK";


    //constant numbers meant for autonomous with encoders
    public final double TICKS_PER_ROTATION = 1120;

    public final double WHEEL_DIAMETER_CM = 10.16;
    public final double WHEEL_DIAMETER_IN = 4;
    public final double WHEEL_CIRCUMFERENCE_CM = (Math.PI * WHEEL_DIAMETER_CM);
    public final double WHEEL_CIRCUMFERENCE_IN = (Math.PI * WHEEL_DIAMETER_IN);

    public final double TICKS_PER_CM = (TICKS_PER_ROTATION)/(WHEEL_CIRCUMFERENCE_CM);
    public final double TICKS_PER_IN = (TICKS_PER_ROTATION)/(WHEEL_CIRCUMFERENCE_IN);

}
