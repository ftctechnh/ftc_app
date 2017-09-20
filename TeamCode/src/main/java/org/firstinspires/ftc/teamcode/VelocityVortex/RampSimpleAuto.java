package org.firstinspires.ftc.teamcode.VelocityVortex;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by spmce on 2/4/2017.
 */
public class RampSimpleAuto extends VelocityVortexAutoMeth{
    private int state = 0;
    //private AutoLaunch launch = new AutoLaunch();
    private boolean isFinished;
    int num = 0;

    public void init() {
        super.init();
        mFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //mSweeper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //mLauncher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void start() {
        mFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //mSweeper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //mLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void autoLoop(boolean ifBlue) {
        switch (state) {
            case 0:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 1:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 2:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 3:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 4:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 5:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 6:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 7:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 8:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 9:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 10:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 11:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 12:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 13:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 14:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 15:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state = 20;
                break;
            case 16:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 17:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 18:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 19:
                try {
                    Thread.sleep(1000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 20:
                driveForward(0.5);
                if (mFL.getCurrentPosition() > 1280) {
                    state++;
                }
                break;
            case 21:
                zeroDrive();
                //sLoaderStopper.setPosition(0.5);
                state++;
                break;
            case 22:
                isFinished = shooter();
                if (isFinished) {
                    state++;
                }
                break;
            case 23:
                drivePow(3*Math.PI/16,1,ifBlue);
                try {
                    Thread.sleep(3150); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            default:
                zeroDrive();
                telemetry.addData("default", "default state");
                break;
        }
        telemetry.addData("state",state);
        robotTele();
    }
    public void stop() {
        robotTele();
        zeroDrive();
    }
    void robotTele() {
        telemetry.addData("fl encoder", mFL.getCurrentPosition());
        telemetry.addData("fr encoder", mFR.getCurrentPosition());
        telemetry.addData("bl encoder", mBL.getCurrentPosition());
        telemetry.addData("br encoder", mBR.getCurrentPosition());
    }
}
