package org.firstinspires.ftc.teamcode.VelocityVortex;

import android.util.Log;

import com.qualcomm.ftccommon.DbgLog;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by spmce on 11/14/2016.
 */
public class VelocityVortexAutonomous extends VelocityVortexAutoMeth {
    String messageForTel = null;
    private int timeCounter; // runs about 20 times a second
    double i = 0;
    //private int timeCounter2;
    /**
     * Construct the class.
     * The system calls this member when the class is instantiated.
     */
    public VelocityVortexAutonomous() {
        // Initialize base classes and class members.
        // All via self-construction.
    }

    /**
     * Init
     */
    public void init() {
        super.init();
    }

    /**
     * Run sensor telemetry in init loop
     */
    @Override
    public void init_loop() {
        //shanesTelemetry tele = new shanesTelemetry();
        //tele.sensorTele();
        allTele();
    }

    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     * The system calls this member once when the OpMode is enabled.
     */
    @Override
    public void start() {
        super.start();
        //resetDriveEncoders();
        //motorLeftDrive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        //motorRightDrive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    //private int state = 0;
    private int initLTime = 550;
    private int countThingy = 0;
    private double acc;
    /**
     * @param drIfBlue determines if blue or red autonomous
     */
    protected void autoLoop(boolean drIfBlue) {
        double drPower;
        double drAngle;
        boolean changeState;
        final double wallDistance = 150;

        switch (state) {
            case 0: // nothing - add reset encoders here
                messageForTel = "State 0 reached.";
                sLoaderStopper.setPosition(initLoaderStopper);
                state++;
                break;
            case 1: // drive until the light 1 hits the line
                drAngle = -Math.PI/4;
                drPower = maxSpeed;
                if (range.getDistance(DistanceUnit.MM) <= 3*wallDistance) {
                    drAngle = -.03;
                    /*if (countThingy > 800) {
                        drAngle = .03-Math.PI;
                        drPower = minSpeed;
                    }*/
                }
                messageForTel = "Finding line";
                changeState = findLine(drAngle, drPower, drIfBlue);
                //DbgLog.msg("ChangeState is %s", changeState);
                if (changeState) {
                    state++;
                    messageForTel = "Line found. Incremented state.";
                }
                countThingy++;
                break;
            case 2: // moves towards the beacon until it is 100 mm away
                drAngle = -Math.PI/2;
                double wd = wallDistance;
                if (!drIfBlue)
                    wd = wallDistance/1.5;
                changeState = untilDistance(drAngle, wd, drIfBlue);
                if (changeState) {
                    state++;
                }
                count = 0;
                break;
            case 3: // aligns with the lion
                drAngle = Math.PI;
                //alignLin(drAngle, drIfBlue);
                messageForTel = "Aligning to line";
                changeState = alignLine(drAngle, drIfBlue);
                if (changeState) {
                    state++;
                    /*try {
                        Thread.sleep(10); // .01 seconds
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }*/
                }
                break;
            case 4: // slowly moves to the button until button is pressed
                drAngle = -Math.PI/2;
                changeState = untilPressed(drAngle, drIfBlue);
                if (changeState) {
                    state++;
                }
                break;
            case 5: // presses the beacon button according to the color
                changeState = pressBeacon(drIfBlue);
                if (changeState) {
                    state++;
                }
                break;
            case 6: // resets the beacon button pressers
                resetBeacon();
                count = 0;
                state++;
                timeCounter = 0;
                break;
            case 7: // moves forward to align for launcher shot
                long num = 0;
                double turn = 0;
                if(drIfBlue) {
                    num = (long) (initLTime * Math.sqrt(2));
                    drAngle = Math.PI/4;
                    turn = .18;
                } else {
                    num = (long) (initLTime);
                    drAngle = Math.PI/2 + .11;
                    turn = .35;
                }
                //drPower = maxSpeed;
                drPower = topSpeed;
                drivePow(drAngle, drPower, drIfBlue,turn,false);
                try {
                    Thread.sleep(num);//.8 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                //if (timeCounter >= 15) {
                    state++;
                //}
                timeCounter++;
                break;
            case 8:
                timeCounter = 0;
                zeroDrive();
                state++;
                break;
            case 9:
                mLauncher.setPower(1);
                try {
                    Thread.sleep(800); // 1.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                //if (timeCounter >= 23) {
                    state++;
                //}
                timeCounter++;
                break;
            case 10:
                mLauncher.setPower(0);
                sLoaderStopper.setPosition(openLoaderStopper);
                timeCounter = 0;
                state++;
                break;
            case 11:
                mSweeper.setPower(.5);
                try {
                    Thread.sleep(2300); // 1.7 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                //if (timeCounter >= 33)
                    state++;
                //timeCounter++;
                break;
            case 12:
                timeCounter = 0;
                mSweeper.setPower(0);
                state++;
                break;
            case 13:
                mLauncher.setPower(1);
                try {
                    Thread.sleep(250); // .3 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                //if (timeCounter >= 5) {
                    state++;
                //}
                timeCounter++;
                break;
            case 14:
                timeCounter = 0;
                state++;
                break;
            case 15:
                mLauncher.setPower(0);
                try {
                    Thread.sleep(700); // 2.95 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                //if (timeCounter >= 13) {
                    state++;
                //}
                timeCounter++;
                break;
            case 16:
                timeCounter = 0;
                state++;
                break;
            case 17:
                mLauncher.setPower(1);
                try {
                    Thread.sleep(1050); // 2.95 seconds //I am commenting on your code to draw your attention to me... -Your secret admirer
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                //if (timeCounter >= 23) {
                    state++;
                //}
                timeCounter++;
                break;
            case 18:
                mLauncher.setPower(0);
                timeCounter = 0;
                state++;
                break;
            case 19: // moves forward to align for launcher shot
                drAngle = Math.PI/2;
                //drPower = maxSpeed;
                drPower = topSpeed;
                drivePow(drAngle, drPower, drIfBlue);
                try {
                    Thread.sleep(1100-initLTime);//.8 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                //if (timeCounter >= 15) {
                state++;
                //}
                timeCounter++;
                break;
            case 20:
                timeCounter = 0;
                zeroDrive();
                state = 28;
                break;
            case 26: // moves to the other beacon without sensing anything
                drAngle = 0;
                if(!drIfBlue)
                    drAngle = -.04;
                drPower = topSpeed;
                drivePow(drAngle, drPower, drIfBlue,-0.05,false);
                messageForTel = "before wait";
                try {
                    Thread.sleep(600);//.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                messageForTel = "after wait";
                //if (timeCounter >= 50) {
                    state++;
                //}
                timeCounter++;
                break;
            case 27:
                timeCounter = 0;
                state++;
                messageForTel = "wait done";
                i = .25;
                break;
            case 28: // continues program until the next beacon
                if(drIfBlue) {
                    drAngle = -.38;
                }
                else {
                    //drAngle = -0.15;
                    drAngle = -0.11;
                }
                drPower = i;
                changeState = findLineR(drAngle, drPower, drIfBlue);
                if (changeState) {
                    state++;
                }
                i = i + .01;
                if (i > maxSpeed)
                    i = maxSpeed;
                messageForTel = "normal until line";
                break;
            case 29: // moves towards the beacon until it is 100 mm away
                drAngle = -Math.PI/2;
                double num1=0;
                if(drIfBlue) {
                    drAngle += .24;
                    num1 = 1.5;
                }
                if(!drIfBlue) {
                    drAngle += .24;
                    num1 = .75;
                }
                changeState = untilDistance(drAngle,wallDistance/num1,drIfBlue);
                if (changeState) {
                    state++;
                }
                count = 0;
                break;
            case 30: //follows white line until robot reaches distance from beacon
                drAngle = Math.PI + 0.18;
                //alignLin(drAngle, drIfBlue);
                changeState = alignLine(drAngle, drIfBlue,.018);
                if (changeState)
                    state++;
                countThingy = 0;
                break;
            case 31: // slowly moves to the beacon
                countThingy++;
                acc = countThingy/1000;
                drAngle = -Math.PI/2;
                if(drIfBlue)
                    drAngle += .2;
                changeState = untilPressed(slowSpeed+acc,drAngle, drIfBlue);
                if (changeState) {
                    state++;
                }
                break;
            case 32: // presses the beacon button according to color
                changeState = pressBeacon(drIfBlue);
                if (changeState) {
                    state++;
                }
                break;
            case 33: // resets the beacon button pressers
                resetBeacon();
                state++;
                break;
            case 34: // moves to hit the ball
                /*drAngle = 47*Math.PI/64;
                drPower = 1;
                drivePow(drAngle, drPower, drIfBlue);
                try {
                    Thread.sleep(700); // 2.95 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }*/
                state++;
                break;
            case 35: // parks the robot
                //zeroDrive();
                state++;
                break;
            case 36:
                /*mLauncher.setPower(1);
                try {
                    Thread.sleep(2000); // 2.95 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }*/
                state++;
                break;
            case 37:
                drAngle = Math.PI/2;
                drPower = 1;
                int timeNum = 80;
                if(drIfBlue) {
                    timeNum = 500;
                }
                drivePow(drAngle, drPower, drIfBlue,.11);
                try {
                    Thread.sleep(timeNum); // 2.95 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 38:
                drAngle = 104*Math.PI/128;
                if (!drIfBlue)
                    drAngle = 96*Math.PI/128;
                drPower = 1;
                drivePow(drAngle, drPower, drIfBlue, .07, true);
                try {
                    Thread.sleep(2600); // 2.95 seconds `````````````
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                if (!drIfBlue) {
                    try {
                        Thread.sleep(200); // 2.95 seconds `````````````
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                countThingy = 0;
                state++;
                break;
            case 39:
                drAngle = 121*Math.PI/128;
                changeState = untilFarDistance(drAngle,1130,drIfBlue);
                if (changeState) {
                    state++;
                }
                if (countThingy > 90) {
                    state++;
                }
                countThingy++;
                break;
            case 40:
                zeroDrive();
                break;
            default:
                zeroDrive();
                break;
        }
        //shanesTelemetry tele = new shanesTelemetry();
        //tele.allTele(); // Update common telemetry
        allTele();
        telemetry.addData("25", "State: " + state);
        telemetry.addData("DbgMsg", messageForTel);
        telemetry.addData("timeCounter", timeCounter);
        //DbgLog.msg("State: %d", state);
    }
    void allTele() {
        motorTele();
        servoTele();
        sensorTele();
    }
    void motorTele() {
        telemetry.addData("fl", leftDrivePower);
        telemetry.addData("fr", rightDrivePower);
        telemetry.addData("bl", backLeftPower);
        telemetry.addData("br", backRightPower);
        telemetry.addData("sweeper", sweeperPower);
    }
    void servoTele() {
        telemetry.addData("rightBeacon", rightBeaconPosition);
        telemetry.addData("leftBeacon", leftBeaconPosition);
    }
    void sensorTele() {
        //telemetry.addData("touch", touch.isPressed());
        telemetry.addData("touch double", touch.getValue());
        telemetry.addData("light1", light1.getLightDetected());
        telemetry.addData("light2", light2.getLightDetected());
        telemetry.addData("color1 red", color1.red());
        telemetry.addData("color1 blue", color1.blue());
        telemetry.addData("color2 red", color2.red());
        telemetry.addData("color2 blue", color2.blue());
        telemetry.addData("gyro heading", gyro.getHeading());
        //telemetry.addData("gyro rotate",gyro.getRotationFraction());
        //telemetry.addData("gyro x",gyro.rawX());
        //telemetry.addData("gyro y",gyro.rawY());
        //telemetry.addData("gyro z",gyro.rawZ());
        telemetry.addData("range", range.getDistance(DistanceUnit.MM));
        telemetry.addData("optical distance", od.getLightDetected());
        telemetry.addData("optical distance", od.getLightDetected());
        telemetry.addData("", "");

        telemetry.addData("0 count thingy", countThingy);
        telemetry.addData("0 count ", count);
    }
}
