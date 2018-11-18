package org.firstinspires.ftc.teamcode.robotutil;

import android.widget.LinearLayout;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by antonlin on 9/9/18.
 */

public class Sweeper {
    private DcMotor sweeperMotor;
    LinearOpMode opMode;


    public Sweeper(LinearOpMode opMode) {
        this.opMode = opMode;
        sweeperMotor = opMode.hardwareMap.dcMotor.get("sweeper");
        sweeperMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        sweeperMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void setPower(double power){
        this.sweeperMotor.setPower(power);
    }

    public void intake(){
        setPower(1);
    }

    public void reverseIntake(){
        setPower(-1);
    }

    public void stop(){
        setPower(0);
    }

}
