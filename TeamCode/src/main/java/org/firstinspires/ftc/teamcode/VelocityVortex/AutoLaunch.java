package org.firstinspires.ftc.teamcode.VelocityVortex;

/**
 * Created by spmce on 12/16/2016.
 */
public class AutoLaunch extends VelocityVortexAutoMeth {
    public boolean shooter() {
        switch (state) {
            case 0:
                mLauncher.setPower(1);
                try {
                    Thread.sleep(800); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 1:
                mLauncher.setPower(0);
                state++;
                break;
            case 2:
                mSweeper.setPower(.5);
                try {
                    Thread.sleep(1700); // 1.7 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 3:
                mSweeper.setPower(0);
                state++;
                break;
            case 4:
                mLauncher.setPower(1);
                try {
                    Thread.sleep(200); // .3 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 5:
                state++;
                break;
            case 6:
                mLauncher.setPower(0);
                try {
                    Thread.sleep(800); // 2.95 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 7:
                state++;
                break;
            case 8:
                mLauncher.setPower(1);
                try {
                    Thread.sleep(1200); // 2.95 seconds //I am commenting on your code to draw your attention to me... -Your secret admirer
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 9:
                mLauncher.setPower(0);
                return true;
            default:
                mLauncher.setPower(0);
                mSweeper.setPower(0);
                break;

        }
        return false;
    }
}
