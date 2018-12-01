package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@Config
public class CameraFlipper {
    public static double DOWN = 1;
    public static double UP = 0.55;

    public Servo servo;

    public CameraFlipper (Servo servo) {
        this.servo = servo;
    }

    public void flipUp() {
        servo.setPosition(UP);
    }

    public void flipDown() {
        servo.setPosition(DOWN);
    }
}
