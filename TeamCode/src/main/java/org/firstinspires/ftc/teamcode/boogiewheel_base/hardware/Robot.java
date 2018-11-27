package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

import android.telecom.Call;

import org.firstinspires.ftc.teamcode.framework.AbstractOpMode;
import org.firstinspires.ftc.teamcode.framework.AbstractRobot;

import java.util.concurrent.Callable;

public class Robot extends AbstractRobot{

    private HardwareDevices hardware;
    private boolean mineralGateOpen = false;

    public Robot(){
        hardware = new HardwareDevices();
    }

    //Drive Methods
    public void setDriveY(double y){
        hardware.drive.setY(y);
    }

    public void setDriveZ(double z){
        hardware.drive.setZ(z);
    }

    public void setDrivePower(double l, double r){
        hardware.drive.setPower(l, r);
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

    public void driveTo(double distance, double speed, int angle){
        hardware.drive.driveTo(distance, speed, angle);
    }

    public synchronized int[] recordPathWithHeading(int numSamples, int timeInterval) {
        return hardware.drive.recordPathWithHeading(numSamples, timeInterval);
    }

    public synchronized void runPathWithHeading(int[] values, int timeInterval, double speed) {
        hardware.drive.runPathWithHeading(values, timeInterval, speed);
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
    public Callable beginIntakingCallable(){
        return ()->{
            hardware.intake.beginIntaking();
            return true;
        };
    }

    public Callable finishIntakingCallable(){
        return ()->{
          hardware.intake.finishIntaking();
          return true;
        };
    }

    public Callable reverseIntakeCallable(){
        return ()->{
            hardware.intake.reverseIntake();
            return true;
        };
    }

    //Mineral Lift Methods
    public Callable moveMineralLiftToCollectPositionCallable(){
        return ()->{
            moveMineralLiftToCollectPosition();
            return true;
        };
    }

    public void moveMineralLiftToCollectPosition() {
        hardware.mineralLift.moveToCollectPosition();
    }

    public Callable moveMineralLiftToDumpPositionCallable(){
        return ()->{
            moveMineralLiftToDumpPosition();
            return true;
        };
    }

    public void moveMineralLiftToDumpPosition(){
        hardware.mineralLift.moveToDumpPosition();
    }

    public Callable openMineralGateCallable() {
        return ()->{
            openMineralGate();
            return true;
        };
    }

    public void openMineralGate() {
        hardware.mineralLift.openGate();
    }

    public Callable closeMineralGateCallable() {
        return ()->{
            closeMineralGate();
            return true;
        };
    }

    public void closeMineralGate() {
        hardware.mineralLift.closeGate();
    }

    public Callable toggleMineralGateCallable(){
        return ()-> {
            if (mineralGateOpen) {
                closeMineralGate();
                mineralGateOpen = false;
            } else {
                openMineralGate();
                mineralGateOpen = true;
            }
            return true;
        };
    }

    public Callable robotLiftUp (){
        return ()->{
            hardware.robotLift.robotLiftUp();
            return true;};

    }

    public Callable robotLiftDown (){
        return ()->{
            hardware.robotLift.robotLiftDown();
            return true;
        };

    }

    public Callable robotLiftStop () {
        return () -> {
            hardware.robotLift.robotLiftStop();
            return true;
        };

    }


}



