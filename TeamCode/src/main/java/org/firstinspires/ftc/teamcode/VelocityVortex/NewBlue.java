package org.firstinspires.ftc.teamcode.VelocityVortex;

/**
 * Created by spmce on 12/10/2016.
 */
//@Autonomous (name = "NewBlue", group = "Autonomous")
public class NewBlue extends VelocityVortexHardware {

    //@Override
    public void init() {
        super.init();
    }

    //@Override
    public void loop() {
        super.loop();
        OmniWheelDrive drive = new OmniWheelDrive();
        double wheels[] = drive.drive(Math.PI/2,1);
        leftDrivePower  = wheels[0];
        rightDrivePower = wheels[1];
        backRightPower  = wheels[2];
        backLeftPower   = wheels[3];
        mFL.setPower(leftDrivePower);
        mFR.setPower(rightDrivePower);
        mBR.setPower(backRightPower);
        mBL.setPower(backLeftPower);
        try {
            Thread.sleep(3000); // 2.95 seconds
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        mFL.setPower(0);
        mFR.setPower(0);
        mBR.setPower(0);
        mBL.setPower(0);
    }

}
