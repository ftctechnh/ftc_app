package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

import android.telecom.Call;

import org.firstinspires.ftc.teamcode.framework.AbstractOpMode;
import org.firstinspires.ftc.teamcode.framework.AbstractRobot;

import java.util.concurrent.Callable;

public class Robot extends AbstractRobot{

    private HardwareDevices hardware;

    public Robot(){
        hardware = new HardwareDevices();
    }

    //Drive Methods
    public Callable setDriveY(double y){
        return ()->{
            hardware.drive.setY(y);
            return true;
        };
    }

    public Callable setDriveZ(double z){
        return ()-> {
            hardware.drive.setZ(z);
            return true;
        };
    }

    public Callable setDrivePower(double l, double r){
        return ()-> {
            hardware.drive.setPower(l, r);
            return true;
        };
    }

    public void updateDrive(){
        hardware.drive.update();
    }

    public void turnTo(double angle, double speed, double error, int period){
        hardware.drive.turnTo(angle, speed, error, period);
    }

    public int[][] recordPath(int numSamples,int timeInterval) {
        return hardware.drive.recordPath(numSamples, timeInterval);
    }

    public void runPath(int[] left, int[] right, int timeInterval) {
        hardware.drive.runPath(left, right, timeInterval);
    }

    public void driveTo(double distance, double speed){
        hardware.drive.driveTo(distance, speed);
    }

    public void setPosition(int position, double power) {
        hardware.drive.setPosition(position, power);
    }

    public boolean isGyroCalibrated() {
        return hardware.drive.isGyroCalibrated();
    }

    public double getHeading(){
        return hardware.drive.getHeading();
    }

    public void stop(){
        hardware.stop();
    }

    //Intake Methods
    public Callable beginIntaking(){
        return ()->{
            hardware.intake.beginIntaking();
            return true;
        };
    }

    public Callable finishIntaking(){
        return ()->{
          hardware.intake.finishIntaking();
          return true;
        };
    }

    //Mineral Lift Methods
    public Callable moveMineralLiftToCollectPosition(){
        return ()->{
            hardware.mineralLift.moveToCollectPosition();
            return true;
        };
    }
    
    public Callable moveMineralLiftToDumpPosition(){
        return ()->{
            hardware.mineralLift.moveToDumpPosition();
            return true;
        };
    }
}
