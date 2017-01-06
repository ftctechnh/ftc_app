package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by ssuri on 10/19/16.
 *
 */
public class Hardware7908Robot
{
    private DcMotor lf, lb, rf, rb;
    private ColorSensor cs1, cs2;

    public Hardware7908Robot(HardwareMap map)
    {
        lf = map.dcMotor.get("lf");
        lb = map.dcMotor.get("lb");
        rf = map.dcMotor.get("rf");
        rb = map.dcMotor.get("rb");
        cs1 = null;
        cs2 = null;
    }

    public void drive(double pow)
    {
        drive(pow, pow);
    }

    public void drive(double left, double right)
    {
        lf.setPower(left);
        lb.setPower(left);
        rf.setPower(right);
        rb.setPower(right);
    }

    public void swingTurn(double pow, boolean left)
    {
        lf.setPower(pow * (left ? 1 : -1));
        lb.setPower(pow * (left ? 1 : -1));
        rf.setPower(pow * (left ? -1 : 1));
        rb.setPower(pow * (left ? -1 : 1));
    }

    public void pointTurn(double pow, boolean left)
    {
        lf.setPower(pow * (left ? 1 : 0));
        lb.setPower(pow * (left ? 1 : 0));
        rf.setPower(pow * (left ? 0 : 1));
        rb.setPower(pow * (left ? 0 : 1));
    }

    public void lineFollow(LineFollowData data)
    {
    }

    public static class LineFollowData
    {
        public int colorSensorNum;
        private int integral, differential;
        private float kP, kI, kD;

        public LineFollowData(int colorSensorNum, float kP, float kI, float kD)
        {
            this.colorSensorNum = colorSensorNum;
        }
    }
}
