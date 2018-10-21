package org.firstinspires.ftc.teamcode;

public class CrazyAngle {
    public float convertNebula(float inputAngle){
        if (inputAngle>75) {
            inputAngle -= 180;
        }
        inputAngle += 15;
        if (inputAngle<0){
            inputAngle+=360;
        }
        return inputAngle;
    }

    public float convertFootprint(float inputAngle) {
        if (inputAngle > -15) {
            inputAngle -= 180;
        }
        inputAngle += 15;
        if (inputAngle < 0) {
            inputAngle += 360;
        }
        return inputAngle;
    }

    public float convertCrater(float inputAngle) {
        if (inputAngle < 75) {
            inputAngle += 180;
        }
        inputAngle += 15;
        return inputAngle;
    }

    public float convertRover(float inputAngle) {
        if (inputAngle < -15) {
            inputAngle += 180;
        }
        inputAngle += 15;
        return inputAngle;
    }
}
