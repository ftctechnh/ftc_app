package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guberti on 6/6/2017.
 */

public class LogTick {
    double gamepad1X1;
    double gamepad1Y1;
    double gamepad1X2;
    double gamepad1Y2;
    double gyroReading;
    double compassReading;
    int frontLeftPos;
    int frontRightPos;
    int backLeftPos;
    int backRightPos;
    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;
    long tick;

    public LogTick(Gamepad g1, NullbotHardware hardware, DcMotor[] motorArr) {
        frontLeftPos = motorArr[0].getCurrentPosition();
        frontLeftPower = motorArr[0].getPower();
        frontRightPos = motorArr[1].getCurrentPosition();
        frontRightPower = motorArr[1].getPower();
        backLeftPos = motorArr[2].getCurrentPosition();
        backLeftPower = motorArr[2].getPower();
        backRightPos = motorArr[3].getCurrentPosition();
        backRightPower = motorArr[3].getPower();
        gyroReading = hardware.getGyroHeading();
        compassReading = hardware.getCompassHeading();
        gamepad1X1 = g1.left_stick_x;
        gamepad1Y1 = g1.left_stick_y;
        gamepad1X2 = g1.right_stick_x;
        gamepad1Y2 = g1.right_stick_y;
        tick = g1.timestamp;
    }

    public String toString() {
        JSONObject j = new JSONObject();
        try {
            j.put("gp1s1X", gamepad1X1);
            j.put("gp1s1Y", gamepad1Y1);
            j.put("gp1s2X", gamepad1X2);
            j.put("gp1s2Y", gamepad1Y2);
            j.put("gyro", gyroReading);
            j.put("compass", compassReading);
            j.put("fLP", frontLeftPos);
            j.put("fRP", frontRightPos);
            j.put("bLP", backLeftPos);
            j.put("bRP", backRightPos);
            j.put("fLE", frontLeftPower);
            j.put("fRE", frontRightPower);
            j.put("bLE", backLeftPower);
            j.put("bRE", backRightPower);
            j.put("tick", tick);
        } catch (JSONException e) {

        }
        return j.toString();
    }

}
