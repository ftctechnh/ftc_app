package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by thund on 11/11/2017.
 */

public class PengwinFin {
    Servo fin;

    public PengwinFin(HardwareMap hardwareMap){
        fin = hardwareMap.servo.get("fin");
    }
}
