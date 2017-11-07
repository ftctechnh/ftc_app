package RicksCode.Bill_VV;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by jmgu3 on 11/7/2016.
 */
public class Sweeper {

    public double sweepSpeed;
    public DcMotor motor;

    public void init(HardwareMap hardwareMap) {
        motor = hardwareMap.dcMotor.get("sweeper");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        sweepSpeed = .5;
    }

    public void sweepIn()
    {
        motor.setPower(sweepSpeed);
    }

    public void sweepOut()
    {
        motor.setPower(-sweepSpeed);
    }

    public void stop()
    {
        motor.setPower(0);
    }
}
