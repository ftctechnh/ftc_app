package org.firstinspires.ftc.teamcode.VelocityVortex;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by spmce on 11/28/2016.
 */
public class VelocityVortexAutoMeth extends VelocityVortexHardware {

    protected final static double topSpeed = Math.sqrt(2);
    protected final static double maxSpeed = 1;
    protected final static double highSpeed = 0.75;
    protected final static double halfSpeed = 0.5;
    protected final static double slowSpeed = 0.4;
    protected final static double quarterSpeed = 0.35;
    protected final static double minSpeed = 0.31;
    private final static double light1value = 0.38;
    private final static double light2value = 0.39;
    private String messageForTel = null;
    protected int count;

    OmniWheelDrive drive = new OmniWheelDrive();
    private double[] power;
    protected int state = 0;

    //@Override
    public void init() {
        super.init();
    }
    protected void drivePow(double ang, double pow, boolean ifBlue) {
        dPow(ang, pow, ifBlue, 0, 0);
    }
    protected void drivePow(double ang, double pow, boolean ifBlue,double turn, boolean ifBlueTurn) {
        if(ifBlueTurn)
            dPow(ang,pow,ifBlue,turn,0);
        else
            dPow(ang,pow,ifBlue,0,turn);
    }
    protected void drivePow(double ang, double pow, boolean ifBlue, double turn) {
        dPow(ang,pow,ifBlue,turn,turn);
    }
    protected void dPow(double ang, double pow, boolean ifBlue, double blueTurn, double redTurn) {
        double angle = ang;
        double turn;
        if (!ifBlue) {
            angle = redAngle(ang);
            turn = .01 + redTurn;
        } else {
            turn = -.011 + blueTurn;
        }
        power = drive.drive(angle,pow,turn);
        powerDrive(power);
    }
    protected boolean findLineR(double ang, double pow, boolean ifBlue) {
        double angle = ang;
        /*double value;
        if (ifBlue) {
            value = light1value;
        } else {
            angle = redAngle(ang);
            value = light2value;
        }*/
        double rotate = 0;
        if (!ifBlue) {
            angle = redAngle(ang);
            rotate = 0.03;
        }
        power = drive.drive(angle,pow,rotate);
        powerDrive(power);
        if (light1.getLightDetected() > light1value) {
            zeroDrive();
            return true;
        }
        return false;
    }
    protected boolean findLine(double ang, double pow, boolean ifBlue) {
        double angle = ang;
        /*double value;
        if (ifBlue) {
            value = light1value;
        } else {
            angle = redAngle(ang);
            value = light2value;
        }*/
        double rotate = 0;
        if (!ifBlue) {
            angle = redAngle(ang);
            rotate = .03;
        }
        power = drive.drive(angle,pow,rotate);
        powerDrive(power);
        if (light1.getLightDetected() > light1value) {
            zeroDrive();
            return true;
        }
        return false;
    }
    protected boolean untilDistance(double ang, double mmDistance, boolean ifBlue) {
        double angle = ang;
        double turn = 0;
        if (!ifBlue) {
            angle = redAngle(ang);
        } else {
            angle += .3;
            turn = .012;
        }
        power = drive.drive(angle,slowSpeed,turn);
        powerDrive(power);
        if (range.getDistance(DistanceUnit.MM) <= mmDistance) {
            zeroDrive();
            return true;
        }
        return false;
    }
    protected boolean untilFarDistance(double ang, double mmDistance, boolean ifBlue) {
        double angle = ang;
        double turn = 0;
        if (!ifBlue) {
            angle = redAngle(ang);
        } else {
            angle += .3;
            turn = .012;
        }
        power = drive.drive(angle,slowSpeed,turn);
        powerDrive(power);
        if (range.getDistance(DistanceUnit.MM) >= mmDistance) {
            zeroDrive();
            return true;
        }
        return false;
    }
    protected boolean alignLine(double ang, boolean ifBlue, double turn) {
        return aLine(ang, ifBlue, turn);
    }
    protected boolean alignLine(double ang, boolean ifBlue) {
        return aLine(ang, ifBlue, 0);
    }
    protected boolean aLine(double ang, boolean ifBlue, double turn) {
        double angle = ang;
        if (!ifBlue)
            angle = redAngle(ang);
        if (count > 50 && count < 100)
            angle = -angle;
        power = drive.drive(angle,minSpeed,turn);
        powerDrive(power);
        /*boolean part1 = false;
        boolean part2 = false;
        if (od.getLightDetected() > 0.5) {
            mFL.setPower(0);
            mFR.setPower(0);
            part1 = true;
        }
        if (light2.getLightDetected() > light2value) {
            mBL.setPower(0);
            mBR.setPower(0);
            part2 = true;
        }
        if (part1 && part2) {
            return true;
        }*/
        if (light2.getLightDetected() > light2value) {
            zeroDrive();
            return true;
        }
        return false;
    }
    protected boolean untilPressed(double pow, double ang, boolean ifBlue) {
        return uPressed(pow,ang,ifBlue);
    }
    protected boolean untilPressed(double ang, boolean ifBlue) {
        return uPressed(slowSpeed,ang,ifBlue);
    }
    protected boolean uPressed(double pow, double ang, boolean ifBlue) {
        double angle = ang;
        if (!ifBlue)
            angle = redAngle(ang);
        power = drive.drive(angle, pow);
        powerDrive(power);
        if (touch.isPressed()) {
            zeroDrive();
            return true;
        }
        return false;
    }
    protected boolean pressBeacon(boolean ifBlue) {
        if (color1.blue() == 255 && color2.blue() == 255) {
            count++;
            return count > 280;
        } else if (color2.blue() == 255) {
            if (ifBlue) {
                return color1BlueBeacon();
            } else {
                return color1RedBeacon();
            }
        } else if (color1.blue() == 255) {
            if (ifBlue) {
                return color2BlueBeacon();
            } else {
                return color2RedBeacon();
            }
        } else if (ifBlue) {
            return blueBeacon();
        }
        return redBeacon();
    }
    protected void resetBeacon() {
        sLeftBeacon.setPosition(initLeftBeacon);
        sRightBeacon.setPosition(initRightBeacon);
    }
    protected void powerDrive(double[] power) {
        leftDrivePower  = power[0];
        rightDrivePower = power[1];
        backRightPower  = power[2];
        backLeftPower   = power[3];
        mFL.setPower(leftDrivePower);
        mFR.setPower(rightDrivePower);
        mBR.setPower(backRightPower);
        mBL.setPower(backLeftPower);
    }
    protected void zeroDrive() {
        leftDrivePower  = 0;
        rightDrivePower = 0;
        backRightPower  = 0;
        backLeftPower   = 0;
        mFL.setPower(leftDrivePower);
        mFR.setPower(rightDrivePower);
        mBR.setPower(backRightPower);
        mBL.setPower(backLeftPower);
    }
    private double redAngle(double angle) {
        if (angle < 0)
            return -Math.PI - angle;
        return Math.PI - angle;
    }
    private boolean blueBeacon() {
        if (color1.blue() > 1)
            sLeftBeacon.setPosition(.96);
        else if (color1.red() > 1)
            sRightBeacon.setPosition(0);
        boolean ifTrue = false;
        if (color1.blue() > 1 && color2.blue() > 1)
            ifTrue = true;
        count++;
        if (count > 280)
            ifTrue = true;
        // completes case if the colors are the same
        return ifTrue;
    }
    private boolean redBeacon() {
        if (color1.red() > 1)
            sLeftBeacon.setPosition(.96);
        else if (color1.blue() > 1)
            sRightBeacon.setPosition(0);
        boolean ifTrue = false;
        if (color1.red() > 1 && color2.red() > 1)
            ifTrue = true;
        count++;
        if (count > 280)
            ifTrue = true;
        // completes case if the colors are the same
        return ifTrue;
    }
    private boolean color1BlueBeacon() {
        if (color1.blue() > 1)
            sLeftBeacon.setPosition(.96);
        else if (color1.red() > 1)
            sRightBeacon.setPosition(0);
        count++;
        return count > 280;
    }
    private boolean color1RedBeacon() {
        if (color1.red() > 1)
            sLeftBeacon.setPosition(.96);
        else if (color1.blue() > 1)
            sRightBeacon.setPosition(0);
        count++;
        return count > 280;
    }
    private boolean color2BlueBeacon() {
        if (color2.red() > 1)
            sLeftBeacon.setPosition(.96);
        else if (color2.blue() > 1)
            sRightBeacon.setPosition(0);
        count++;
        return count > 280;
    }
    private boolean color2RedBeacon() {
        if (color2.blue() > 1)
            sLeftBeacon.setPosition(.96);
        else if (color2.red() > 1)
            sRightBeacon.setPosition(0);
        count++;
        return count > 280;
    }

    //*******************************************

    void driveForward(double pow) {
        powerDrive(drive.drive(Math.PI/2,pow));
    }

    boolean untilLight(boolean ifLight1) {
        return true;
    }
    //******************************************

    protected boolean shooter() {
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
                sLoaderStopper.setPosition(openLoaderStopper);
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
