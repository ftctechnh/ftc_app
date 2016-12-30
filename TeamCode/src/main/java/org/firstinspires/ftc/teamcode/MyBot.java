package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class MyBot {
    
    public MyBot() {    }
    
    // defining motors 
    public DcMotor leftMotor;
    public DcMotor rightMotor;
    
    private ElapsedTime period  = new ElapsedTime();
    
    // assignment of the motors
    
    public void init(HardwareMap hMap) {
        
        leftMotor = hMap.dcMotor.get("left");
        rightMotor = hMap.dcMotor.get("right");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        
    }
    
    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}