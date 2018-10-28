package org.firstinspires.ftc.teamcode;

public class CrazyAngle {

    public static float convertNebula(float inputAngle){
        float angle = reverseAngle(inputAngle);
        angle += 90;
        return normalizeAngle(angle);
    }

    public static float convertFootprint(float inputAngle) {
        float angle = reverseAngle(inputAngle);
        if (angle < 0)
            angle += 180;
        angle += 90;
        return normalizeAngle(angle);
    }

    public static float convertCrater(float inputAngle) {
        float angle = reverseAngle(inputAngle);
        if (angle > 90)
            angle -= 180;
        angle += 270;
        return normalizeAngle(angle);
    }

    public static float convertRover(float inputAngle) {
        float angle = reverseAngle(inputAngle);
       if (angle < 0)
           angle += 180;
       angle += 270;
        return normalizeAngle(angle);
    }

    public static float reverseAngle(float inputAngle) {
        return (inputAngle * -1.0f);
    }

    public static float normalizeAngle(float inputAngle) {
        return (inputAngle + 360.0f) % 360.0f;
    }
}
