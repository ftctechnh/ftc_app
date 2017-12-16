package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jxfio on 12/15/2017.
 */

public class DriverNoEncoder extends Driver {
    public DcMotor leftMotor;
    public DcMotor rightMotor;

    public DriverNoEncoder(DcMotor left, DcMotor right){
        leftMotor = left;
        rightMotor = right;
    }
    //by distance we mean time
    public void forward(double distance, double power) {
        ElapsedTime runtime = new ElapsedTime();
        while (runtime.milliseconds() < distance) {
            leftMotor.setPower(power);
            rightMotor.setPower(-power);
        }
    }
    //by degrees we mean time
    public void turn(double degrees, double power) {
        ElapsedTime runtime = new ElapsedTime();
        while (runtime.milliseconds() < degrees) {
            leftMotor.setPower(power);
            rightMotor.setPower(power);
        }
    }

}
