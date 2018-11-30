package teamcode.examples;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

public class Helper {
    // http://www.revrobotics.com/content/docs/Encoder-Guide.pdf
    public static final int REV_CORE_HEX_MOTOR_TICKS_PER_ROTATION = 288;

    // http://www.revrobotics.com/content/docs/Encoder-Guide.pdf
    public static final int REV_HD_HEX_20_MOTOR_TICKS_PER_ROTATION = 560;

    // http://www.revrobotics.com/content/docs/Encoder-Guide.pdf
    public static final int REV_HD_HEX_40_MOTOR_TICKS_PER_ROTATION = 1120;

    // http://www.revrobotics.com/content/docs/Encoder-Guide.pdf
    public static final int REV_HD_HEX_60_MOTOR_TICKS_PER_ROTATION = 1680;

    // distance from 537.6494 x 930.5470
    public static final double CAMERA_DISTANCE = 930.55;

    public static double getCentimetersFromPixels(double pixels) {
        return ((pixels - 300) / -100) * 30.48; // centimeters
    }
}
