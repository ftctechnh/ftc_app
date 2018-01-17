package org.firstinspires.ftc.teamcode.robot.locomotion;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.math.vector.Vec3;

import java.util.Map;

/**
 * Created by Derek on 12/7/2017.
 */

public class Meccanum implements DriveMode {
    DcMotor A,B,C,D;

    public Meccanum(DcMotor A, DcMotor B, DcMotor C, DcMotor D) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
    }

    public Meccanum(Map<String,DcMotor> map) {
        for (String s : map.keySet()) {
            switch (s) {
                case "A":
                    this.A = map.get(s);
                    break;

                case "B":
                    this.B = map.get(s);
                    break;

                case "C":
                    this.C = map.get(s);
                    break;

                case "D":
                    this.D = map.get(s);
                    break;

            }
        }
    }

    @Override
    public void update(DriveInfo driveInfo) {

    }

    @Override
    public type getType() {
        return type.MECCANUM;
    }
}
