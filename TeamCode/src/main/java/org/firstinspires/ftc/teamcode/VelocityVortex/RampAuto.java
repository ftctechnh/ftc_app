package org.firstinspires.ftc.teamcode.VelocityVortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by spmce on 2/4/2017.
 */
public class RampAuto extends VelocityVortexAutoMeth {
    public void autoLoop(boolean drIfBlue) {
        double drPower;
        double drAngle;
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
                    Thread.sleep(2000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 14:
                try {
                    Thread.sleep(2000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 15:
                try {
                    Thread.sleep(2000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 16:
                try {
                    Thread.sleep(2000); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 17: // moves forward to align for launcher shot
                drAngle = Math.PI/2;
                drPower = topSpeed;
                drivePow(drAngle, drPower, drIfBlue);
                try {
                    Thread.sleep(550);//.8 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 18:
                zeroDrive();
                state++;
                break;
            case 19:
                mLauncher.setPower(1);
                try {
                    Thread.sleep(800); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 20:
                mLauncher.setPower(0);
                sLoaderStopper.setPosition(0.5);
                state++;
                break;
            case 21:
                mSweeper.setPower(.5);
                try {
                    Thread.sleep(2300); // 1.7 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 22:
                mSweeper.setPower(0);
                state++;
                break;
            case 23:
                mLauncher.setPower(1);
                try {
                    Thread.sleep(250); // .3 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 24:
                state++;
                break;
            case 25:
                mLauncher.setPower(0);
                try {
                    Thread.sleep(700); // 2.95 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 26:
                state++;
                break;
            case 27:
                mLauncher.setPower(1);
                try {
                    Thread.sleep(1050); // 2.95 seconds //I am commenting on your code to draw your attention to me... -Your secret admirer
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 28:
                mLauncher.setPower(0);
                state++;
                break;
            case 29:
                drivePow(0.03 ,topSpeed,drIfBlue);
                try {
                    Thread.sleep(1550); // 2.95 seconds //I am commenting on your code to draw your attention to me... -Your secret admirer
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 30:
                zeroDrive();
                state++;
                break;
            default:
                zeroDrive();
                telemetry.addData("default", "default state");
                break;
        }
        telemetry.addData("state",state);
    }
}
