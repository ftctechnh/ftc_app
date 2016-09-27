package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by jacost63 on 9/27/2016.
 */
@Autonomous(name="AutonomousTestFire", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
public class AutonomousTestFire extends OpMode {
    private static final int ZERO_POWER = 0;
    private static final int MAX_FORWARD = 1;
    private static final int MAX_REVERSE = -1;

    DcMotor motorLeft = null;
    DcMotor motorRight = null;

    @Override
    public void init(){
        this.motorLeft = this.hardwareMap.dcMotor.get("motorLeft");
        this.motorRight = this.hardwareMap.dcMotor.get("motorRight");
        DcMotor.RunMode mode = DcMotor.RunMode.RUN_USING_ENCODER;
        this.motorLeft.setMode(mode);
        this.motorRight.setMode(mode);

        this.motorLeft.setDirection(DcMotor.Direction.FORWARD);
        this.motorRight.setDirection(DcMotor.Direction.REVERSE);

        AllStop();
    }

    @Override
    public void loop(){
        for(int i=0;i<4;i++){
            AllStop();
            MaxForward();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HardRight();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void Sleep(long sTime){
        long msSleep = System.currentTimeMillis()+sTime;
        while(msSleep < System.currentTimeMillis()){}
    }
    private void MaxForward(){
        this.motorRight.setPower(MAX_FORWARD);
        this.motorLeft.setPower(MAX_FORWARD);
    }
    private void MaxReverse(){
        this.motorRight.setPower(MAX_REVERSE);
        this.motorLeft.setPower(MAX_REVERSE);
    }
    private void HardRight(){
        this.motorRight.setPower(MAX_REVERSE);
        this.motorLeft.setPower(MAX_FORWARD);
    }
    private void HardLeft(){
        this.motorRight.setPower(MAX_FORWARD);
        this.motorLeft.setPower(MAX_REVERSE);
    }
    private void AllStop(){
        this.motorLeft.setPower(ZERO_POWER);
        this.motorRight.setPower(ZERO_POWER);
    }
}
