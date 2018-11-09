package org.firstinspires.ftc.teamcode.boggiewheel_base.hardware;

import org.firstinspires.ftc.teamcode.boggiewheel_base.hardware.devices.drive.DriveController;

public class HardwareDevices {

    private DriveController drive;

    public HardwareDevices(){
        drive = new DriveController();
    }

    public void setDriveY(double y){
        drive.setY(y);
    }

    public void setDriveZ(double z){
        drive.setZ(z);
    }

    public void setPower(double l, double r){
        drive.setPower(l,r);
    }

    public void updateDrive(){
        drive.update();
    }

    public void turnTo(double angle, double speed, double error, int period){
        drive.turnTo(angle, speed, error, period);
    }

    public void driveTo(double distance, double speed){
        drive.driveTo(distance, speed);
    }

    public void setPosition(int position, double power) {
        drive.setPosition(position, power);
    }

    public void stop(){
        drive.stop();
    }

}
