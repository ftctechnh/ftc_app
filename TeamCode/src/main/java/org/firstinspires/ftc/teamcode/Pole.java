package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by gstaats on 21/09/17.
 */


// Pole Class
public class Pole {
    //extends\retracts the pole
    private DcMotor far;
    //lifts\lowers the pole
    private DcMotor high;
    //extends the pole
    public void extend()
    {
        far.setPower(1);
    }
    //retracts the pole
    public void retract()
    {
        far.setPower(-1);
    }
    //lifts s the pole
    public void lift()
    {
        high.setPower(1);
    }
    //lowers the pole
    public void lower(){
        high.setPower(-1);
    }
    public void liftstop(){

        high.setPower(0);

    }
}
