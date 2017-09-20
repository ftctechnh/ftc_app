package org.firstinspires.ftc.teamcode.VelocityVortex;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by spmce on 11/18/2016.
 */
//@Autonomous(name = "Auto Blue" , group = "Autonomous") //Register
public class AutoBlue extends VelocityVortexHardware {

    /**
     * Construct the class.
     * The system calls this member when the class is instantiated.
     */
    public AutoBlue() {
        // Initialize base classes and class members.
        // All via self-construction.
    } // AutoBlue

    /**
     * Init
     */
    public void init() {
        super.init();
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

    /**
     * This class member remembers which state is currently active.  When the
     * start method is called, the state will be initialized (0).  When the loop
     * starts, the state will change from initialize to state_1.  When state_1
     * actions are complete, the state will change to state_2.  This implements
     * a state machine for the loop method.
     */
    private int state = 0;
    private double drPower;

    /**
     * Implement a state machine that controls the robot during auto-operation.
     * The state machine uses a class member and encoder input to transition
     * between states.
     * <p/>
     * The system calls this member repeatedly while the OpMode is running.
     */
    //@Override
    public void loop() {
        super.loop();
        OmniWheelDrive drive = new OmniWheelDrive();
        double[] power;
        switch (state) {
            case 0: // nothing
                state++;
                break;
            // Drive until
            case 1: // drive until the light 1 hits the line
                drPower = 1;
                power = drive.drive(Math.PI / 4,drPower,false);
                powerDrive(power);
                if (light1.getLightDetected() > 0.35) {
                    zeroDrive();
                    state++;
                }
                break;
            case 2: // moves towards the beacon until it is 100 mm away
                drPower = .25;
                power = drive.drive(Math.PI / 2,drPower, false);
                powerDrive(power);
                if (range.getDistance(DistanceUnit.MM) <= 100) {
                    zeroDrive();
                    state++;
                }
                break;
            case 3: // aligns with the lion
                drPower = .22;
                power = drive.drive(Math.PI,drPower,true);
                powerDrive(power);
//                boolean part1 = false;
//                boolean part2 = false;
                /*if (od.getLightDetected() > 0.5) {
                    mFL.setPower(0);
                    mFR.setPower(0);
                    part1 = true;
                }
                if (light2.getLightDetected() > .35) {
                    mBL.setPower(0);
                    mBR.setPower(0);
                    part2 = true;
                }
                if (part1 && part2) {
                    state++;
                }*/
                if (light2.getLightDetected() > .38) {
                    zeroDrive();
                    state++;
                }
                break;
            case 4: // slowly moves to the button
                drPower = .22;
                power = drive.drive(Math.PI / 2,drPower, false);
                powerDrive(power);
                if (touch.isPressed()) {
                   zeroDrive();
                    state++;
                }
                break;
            case 5: //presses the beacon button according to the color
                if (color1.blue() > 3 || color2.blue() >3) {
                    if (color1.blue() > 3)
                        sLeftBeacon.setPosition(.96);
                    else
                        sRightBeacon.setPosition(0);
                }
                // completes case if the colors are the same
                if (color1.blue() > 3 && color2.blue() > 3)
                    state++;
                break;
            case 6: // resets the beacon button pressers
                sLeftBeacon.setPosition(initLeftBeacon);
                sRightBeacon.setPosition(initRightBeacon);
                state++;
                break;
            case 7: // moves to the other beacon without sensing anything
                drPower = 1;
                power = drive.drive(.01,drPower, true);
               powerDrive(power);
                try {
                    Thread.sleep(200);//.2 seconds
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 8: // continues program until the next beacon
                drPower = 1;
                power = drive.drive(.01,drPower, true);
                powerDrive(power);
                if (light1.getLightDetected() > 0.35) {
                    zeroDrive();
                    state++;
                }
                break;
            case 9: // moves towards the beacon until it is 100 mm away
               drPower = .25;
                power = drive.drive(Math.PI / 2,drPower, false);
                powerDrive(power);
                if (range.getDistance(DistanceUnit.MM) <= 100) {
                    state++;
                }
                break;
            case 10: //follows white line until robot reaches distance from beacon
                drPower = .22;
                power = drive.drive(Math.PI,drPower,true);
               powerDrive(power);
                //boolean part1 = false;
                //boolean part2= false;
                /*if (od.getLightDetected() > 0.5) {
                    mFL.setPower(0);
                    mFR.setPower(0);
                    part1 = true;
                }
                if (light2.getLightDetected() > .35) {
                    mBL.setPower(0);
                    mBR.setPower(0);
                    part2 = true;
                }
                if (part1 && part2) {
                    state++;
                }*/
                if (light2.getLightDetected() > .38) {
                    zeroDrive();
                    state++;
                }
                break;
            case 11: // slowly moves to the beacon
                drPower = .22;
                power = drive.drive(Math.PI / 2,drPower, false);
                powerDrive(power);
                if (touch.isPressed()) {
                   zeroDrive();
                    state++;
                }
                break;
            case 12: // presses the beacon button according to color
                if (color1.blue() > 3)
                    sLeftBeacon.setPosition(.96);
                else
                    sRightBeacon.setPosition(0);
                if (color1.blue() > 3 && color2.blue() > 3)
                    state++;
                break;
            case 13: // resets the beacon button pressers
                sLeftBeacon.setPosition(initLeftBeacon);
                sRightBeacon.setPosition(initRightBeacon);
                state++;
                break;
            case 14: // moves to hit the ball
                drPower = 1;
                power = drive.drive(47*Math.PI / 64,drPower,true);
                powerDrive(power);
                try {
                    Thread.sleep(2950);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                state++;
                break;
            case 15: // parks the robot
                zeroDrive();
            default:
                break;
        }
        shanesTelemetry tele = new shanesTelemetry();
        tele.allTele(); // Update common telemetry
        motorTele();
        sensorTele();
        sensorTele();
        telemetry.addData("25", "State: " + state);

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
    }
    private void powerDrive(double[] power) {
        leftDrivePower  = power[0];
        rightDrivePower = power[1];
        backRightPower  = power[2];
        backLeftPower   = power[3];
        telemetry.addData("fl", leftDrivePower);
        telemetry.addData("fr",rightDrivePower);
        telemetry.addData("bl", backLeftPower);
        telemetry.addData("br", backRightPower);
        mFL.setPower(leftDrivePower);
        mFR.setPower(rightDrivePower);
        mBR.setPower(backRightPower);
        mBL.setPower(backLeftPower);
    }
    private void zeroDrive() {
        leftDrivePower  = 0;
        rightDrivePower = 0;
        backRightPower  = 0;
        backLeftPower   = 0;
        telemetry.addData("fl", leftDrivePower);
        telemetry.addData("fr",rightDrivePower);
        telemetry.addData("bl", backLeftPower);
        telemetry.addData("br", backRightPower);
        mFL.setPower(leftDrivePower);
        mFR.setPower(rightDrivePower);
        mBR.setPower(backRightPower);
        mBL.setPower(backLeftPower);
    }
}

