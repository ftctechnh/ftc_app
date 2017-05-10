package org.firstinspires.ftc.teamcode.Libs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.teamcode.HardwareProfiles.HardwareTestPlatform;

import java.util.List;

/**
 * Created by caseyzandbergen on 12/7/16.
 */

public class DriveMecanum {
    public double myCurrentMotorPosition;
    public double myTargetPosition;
    public double LF, RF, LR, RR;
    public double robotX;          // The robot's X position from VuforiaLib
    public double robotY;  // The robot's Y position from VuforiaLib
    public double robotBearing;    //Bearing to, i.e. the bearing you need to stear toward
    public String procedure;
    public double initZ;
    public double currentZint;
    public double odsThreshold = .5;   //Threshold at which the ODS sensor acquires the whie line
    public double ods = 0;             //Value returned from the Optical Distance Sensor
    public double zCorrection = 0;
    private List<VuforiaTrackable> myTrackables;
    private HardwareTestPlatform robot = null;
    private LinearOpMode opMode = null;
    private ElapsedTime runtime = new ElapsedTime();
    private VuforiaLib myVuforia = null;
    private List<Double> vuforiaTracking;
    private DataLogger Dl;
    private double heading;
    private double power;
    private double colorRightRed = 0;   //Value from color sensor
    private double colorRightBlue = 0;  //Value from color sensor
    private double colorLeftRed = 0;    //Value from color sensor
    private double colorLeftBlue = 0;   //Value from color sensor
    private double radians = 0;
    private double changeSpeed = 0;
    private double motorCorrectCoefficient = .05;
    private boolean tel = true;

    public DriveMecanum(HardwareTestPlatform myRobot, LinearOpMode myOpMode, VuforiaLib thisVuforia,
                        List<VuforiaTrackable> trackableList, DataLogger myDl) {
        robot = myRobot;
        opMode = myOpMode;
        myVuforia = thisVuforia;
        myTrackables = trackableList;
        Dl = myDl;

    }

    public void motorsHalt() {
        robot.motorLF.setPower(0);
        robot.motorRF.setPower(0);
        robot.motorLR.setPower(0);
        robot.motorRR.setPower(0);
    }

    /**
     * Set the power level of the motors.
     */
    public void setPower(double LF, double LR, double RF, double RR) {
        robot.motorLF.setPower(LF);
        robot.motorRF.setPower(RF);
        robot.motorLR.setPower(LR);
        robot.motorRR.setPower(RR);
    }

    /**
     * Pivot the robot to a new heading. 0 is straight ahead, 1 to 179 is to the left -1 to -179 is
     * to the right.
     */
    public void pivotLeft(double power, double heading) {
        procedure = "Pivot";
        initZ = robot.mrGyro.getIntegratedZValue();
        currentZint = robot.mrGyro.getIntegratedZValue();

        while (currentZint < heading && opMode.opModeIsActive()) {
            /**
             * We are pivoting left so reverse power to the left motor
             */
            LR = -power;
            LF = -power;
            RR = power;
            RF = power;

            setPower(LF, LR, RF, RR);

            currentZint = robot.mrGyro.getIntegratedZValue();
            if (tel) {
                telemetry();
                logData();
            }
            opMode.idle();
            }
        motorsHalt();
    }

    public void pivotRight(double power, double heading) {
        procedure = "Pivot";
        initZ = robot.mrGyro.getIntegratedZValue();
        currentZint = robot.mrGyro.getIntegratedZValue();

        while (Math.abs(currentZint) < heading && opMode.opModeIsActive()) {
            /**
             * We are pivoting left so reverse power to the left motor
             */
            LR = power;
            LF = power;
            RR = -power;
            RF = -power;

            setPower(LF, LR, RF, RR);

            currentZint = robot.mrGyro.getIntegratedZValue();
            if (tel) {
                telemetry();
                logData();
            }
            opMode.idle();
        }
        motorsHalt();
    }

    /**
     * In our testing, on our field, a .7 value indicates you are centered on
     * the line.  You should use the practice field and test opMode to confirm
     * this is valid for your actual competition field.
     * The value from the ODS sensor indicates you are no longer centered on the
     * line and the robotBearing indicates the bot is to the left of the line
     * so we will increase power on the LR and decrease power on the RR to
     * get centered back on the line
     */
    public void followLineX(double x, double power, double heading) {
        procedure = "followLineX";
        ods = robot.ods.getLightDetected();
        radians = getRadians(heading);

        while (robotX > x && opMode.opModeIsActive()) {
            ods = robot.ods.getLightDetected();

            getVuforiaLocation();

            LF = calcLF(radians, power);
            RF = calcRF(radians, power);
            LR = calcLR(radians, power);
            RR = calcRR(radians, power);

            if (ods < .5 && robotBearing > 0 && robotBearing < 180) {
                LF = power + motorCorrectCoefficient;
                LR = power + motorCorrectCoefficient;
                RF = power - motorCorrectCoefficient;
                RR = power - motorCorrectCoefficient;

                opMode.telemetry.addData("DRIFTED LEFT", String.valueOf(robotBearing));
            }

            /**
             * The robot has drifted right, correct the course
             */
            if (ods < .5 && robotBearing > 180 && robotBearing < 359.9) {
                LF = power - motorCorrectCoefficient;
                LR = power - motorCorrectCoefficient;
                RF = power + motorCorrectCoefficient;
                RR = power + motorCorrectCoefficient;

                opMode.telemetry.addData("DRIFTED RIGHT", "");
            }

            setPower(LF, LR, RF, RR);

            if (tel) {
                telemetry();
                logData();
            }

            opMode.idle();
        }
        motorsHalt();
    }

    public void acquireBeaconX(double x, double power, double heading) {
        procedure = "acquireBeaconX";
        initZ = robot.mrGyro.getIntegratedZValue();
        radians = getRadians(heading);

        getVuforiaLocation();

        while (robot.touchSensor.getValue() == 0
                && robotX > x
                && opMode.opModeIsActive()) {

            ods = robot.ods.getLightDetected();
            currentZint = robot.mrGyro.getIntegratedZValue();

            getVuforiaLocation();

            LF = calcLF(radians, power);
            RF = calcRF(radians, power);
            LR = calcLR(radians, power);
            RR = calcRR(radians, power);

            currentZint = robot.mrGyro.getIntegratedZValue();

            if (ods < .9 && robotBearing > 0 && robotBearing < 180) {
                LF = power + power * .1;
                LR = power + power * .1;
                RF = power + power * .1;
                RR = power + power * .1;

                opMode.telemetry.addData("DRIFTED LEFT", String.valueOf(robotBearing));
            }

            /**
             * The robot has drifted right, correct the course
             */
            if (ods < .9 && robotBearing > 180 && robotBearing < 359.9) {
                LF = power + power * .1;
                LR = power + power * .1;
                RF = power + power * .1;
                RR = power + power * .1;

                opMode.telemetry.addData("DRIFTED RIGHT", "");
            }

            /**
             * The robot is centered on the line, drive straight.
             */
            if (ods > .9) {
                LF = power;
                RF = power;
                LR = power;
                RR = power;

                opMode.telemetry.addData("NO DRIFT", "");
            }

            setPower(LF, LR, RF, RR);


            if (tel) {
                telemetry();
                logData();
            }

            opMode.idle();
        }
        motorsHalt();
    }

    public void acquireBeaconY(double y, double power, double heading) {
        procedure = "acquireBeaconX";
        initZ = robot.mrGyro.getIntegratedZValue();
        radians = getRadians(heading);

        getVuforiaLocation();

        while (robot.touchSensor.getValue() == 0
                && robotX > y
                && opMode.opModeIsActive()) {

            ods = robot.ods.getLightDetected();
            currentZint = robot.mrGyro.getIntegratedZValue();

            getVuforiaLocation();

            LF = calcLF(radians, power);
            RF = calcRF(radians, power);
            LR = calcLR(radians, power);
            RR = calcRR(radians, power);

            currentZint = robot.mrGyro.getIntegratedZValue();

            if (ods < .9 && robotBearing > 0 && robotBearing < 180) {
                LF = power + power * .1;
                LR = power + power * .1;
                RF = power + power * .1;
                RR = power + power * .1;

                opMode.telemetry.addData("DRIFTED LEFT", String.valueOf(robotBearing));
            }

            /**
             * The robot has drifted right, correct the course
             */
            if (ods < .9 && robotBearing > 180 && robotBearing < 359.9) {
                LF = power + power * .1;
                LR = power + power * .1;
                RF = power + power * .1;
                RR = power + power * .1;

                opMode.telemetry.addData("DRIFTED RIGHT", "");
            }

            /**
             * The robot is centered on the line, drive straight.
             */
            if (ods > .9) {
                LF = power;
                RF = power;
                LR = power;
                RR = power;

                opMode.telemetry.addData("NO DRIFT", "");
            }

            setPower(LF, LR, RF, RR);


            if (tel) {
                telemetry();
                logData();
            }

            opMode.idle();
        }
        motorsHalt();
    }

    /**
     * Translate on a heading and stop when the ODS sensor detects the white line.
     */
    public void translateOdsStop(double odsThreshold, double power, double heading) {
        procedure = "translateOdsStop";
        initZ = robot.mrGyro.getIntegratedZValue();
        ods = robot.ods.getLightDetected();

        radians = getRadians(heading);

        while (opMode.opModeIsActive() && ods < odsThreshold) {
            ods = robot.ods.getLightDetected();
            currentZint = robot.mrGyro.getIntegratedZValue();

            LF = calcLF(radians, power);
            RF = calcRF(radians, power);
            LR = calcLR(radians, power);
            RR = calcRR(radians, power);

            if (currentZint != initZ) {  //Robot has drifted off course
                zCorrection = Math.abs(initZ - currentZint);

                courseCorrect();
            } else {
                zCorrection = 0;
            }

            setPower(LF, LR, RF, RR);

            if (tel) {
                telemetry();
                logData();
            }

            opMode.idle();
        }

        motorsHalt();

    }

    /**
     * Translate on a heading the distance specified in MM.
     */
    public void translateDistance(double mm, double power, double heading) {
        procedure = "translateDistance";
        initZ = robot.mrGyro.getIntegratedZValue();
        myCurrentMotorPosition = robot.motorLF.getCurrentPosition();
        myTargetPosition = myCurrentMotorPosition + (int) (mm * robot.COUNTS_PER_MM);

        radians = getRadians(heading);

        while (opMode.opModeIsActive() && myTargetPosition > myCurrentMotorPosition) {
            LF = calcLF(radians, power);
            RF = calcRF(radians, power);
            LR = calcLR(radians, power);
            RR = calcRR(radians, power);

            currentZint = robot.mrGyro.getIntegratedZValue();

            if (currentZint != initZ) {  //Robot has drifted off course
                zCorrection = Math.abs(initZ - currentZint);

                courseCorrect();
            } else {
                zCorrection = 0;
            }

            setPower(LF, LR, RF, RR);

            myCurrentMotorPosition = robot.motorLF.getCurrentPosition();

            if (tel) {
                telemetry();
                logData();
            }

            opMode.idle();
        }

        motorsHalt();

    }

    /**
     * Translate on a heading for a defined period of time.
     */
    public void translateTime(double timeOut, double power, double heading) {
        double timeOutTime;
        procedure = "translateTime";
        initZ = robot.mrGyro.getIntegratedZValue();
        radians = getRadians(heading);

        timeOutTime = runtime.time() + timeOut;

        while (opMode.opModeIsActive() && runtime.time() < timeOutTime) {
            LF = calcLF(radians, power);
            RF = calcRF(radians, power);
            LR = calcLR(radians, power);
            RR = calcRR(radians, power);

            currentZint = robot.mrGyro.getIntegratedZValue();

            if (currentZint != initZ) {  //Robot has drifted off course
                zCorrection = Math.abs(initZ - currentZint);

                courseCorrect();
            } else {
                zCorrection = 0;
            }

            setPower(LF, LR, RF, RR);

            myCurrentMotorPosition = robot.motorLF.getCurrentPosition();

            if (tel) {
                telemetry();
                logData();
            }

            opMode.idle();
        }

        motorsHalt();

    }

    /**
     * Translate on a heading and stop when we reach the specified X axis value.
     */
    private void translateVuforiaNavXNeg(double x, double power, double heading) {
        procedure = "translateVuforiaNavXNeg";
        initZ = robot.mrGyro.getIntegratedZValue();
        radians = getRadians(heading);

        getVuforiaLocation();

        while (opMode.opModeIsActive() && robotX > x) {
            LF = calcLF(radians, power);
            RF = calcRF(radians, power);
            LR = calcLR(radians, power);
            RR = calcRR(radians, power);

            currentZint = robot.mrGyro.getIntegratedZValue();

            if (currentZint != initZ) {  //Robot has drifted off course
                zCorrection = Math.abs(initZ - currentZint);

                courseCorrect();
            } else {
                zCorrection = 0;
            }

            setPower(LF, LR, RF, RR);

            getVuforiaLocation();

            if (tel) {
                telemetry();
                logData();
            }

            opMode.idle();
        }

        motorsHalt();

    }


    /**
     * Translate on a heading and stop when we reach the specified X axis value.
     */
    private void translateVuforiaNavXPos(double x, double power, double heading) {
        procedure = "translateVuforiaNavXNeg";
        initZ = robot.mrGyro.getIntegratedZValue();
        radians = getRadians(heading);

        getVuforiaLocation();

        while (opMode.opModeIsActive() && robotX < x) {
            LF = calcLF(radians, power);
            RF = calcRF(radians, power);
            LR = calcLR(radians, power);
            RR = calcRR(radians, power);

            currentZint = robot.mrGyro.getIntegratedZValue();

            if (currentZint != initZ) {  //Robot has drifted off course
                zCorrection = Math.abs(initZ - currentZint);

                courseCorrect();
            } else {
                zCorrection = 0;
            }

            setPower(LF, LR, RF, RR);

            getVuforiaLocation();

            if (tel) {
                telemetry();
                logData();
            }

            opMode.idle();
        }

        motorsHalt();

    }

    /**
     * Translate on a heading and stop when we reach the specified Y axis value.
     */
    private void translateVuforiaNavY(double y, double power, double heading) {
        procedure = "translateVuforiaNavY";
        initZ = robot.mrGyro.getIntegratedZValue();
        radians = getRadians(heading);

        getVuforiaLocation();

        while (opMode.opModeIsActive() && robotY < y) {
            LF = calcLF(radians, power);
            RF = calcRF(radians, power);
            LR = calcLR(radians, power);
            RR = calcRR(radians, power);

            currentZint = robot.mrGyro.getIntegratedZValue();

            if (currentZint != initZ) {  //Robot has drifted off course
                zCorrection = Math.abs(initZ - currentZint);

                courseCorrect();
            } else {
                zCorrection = 0;
            }

            setPower(LF, LR, RF, RR);

            getVuforiaLocation();

            if (tel) {
                telemetry();
                logData();
            }

            opMode.idle();
        }

        motorsHalt();

    }

    /**
     * Calculate the wheel speeds.
     *
     * @return wheel speed
     */
    public double calcLF(double radians, double power) {
        LF = power * Math.sin(radians + (Math.PI / 4)) + changeSpeed;

        if (LF > 1 || LF < -1) {
            LF = 0;
        }

        return LF;
    }

    public double calcRF(double radians, double power) {
        RF = power * Math.cos(radians + (Math.PI / 4)) - changeSpeed;

        if (RF > 1 || RF < -1) {
            RF = 0;
        }

        return RF;
    }

    public double calcLR(double radians, double power) {
        LR = power * Math.cos(radians + (Math.PI / 4)) + changeSpeed;

        if (LR > 1 || LR < -1) {
            LR = 0;
        }

        return LR;
    }

    public double calcRR(double radians, double power) {
        RR = power * Math.sin(radians + (Math.PI / 4)) - changeSpeed;

        if (RR > 1 || RR < -1) {
            RR = 0;
        }

        return RR;
    }

    /**
     * Change power to the motors to correct for heading drift as indicated by the gyro.
     */
    public void courseCorrect() {
        if (currentZint > initZ) {  //Robot has drifted left
            LF = LF + motorCorrectCoefficient;
            LR = LR + motorCorrectCoefficient;
            RF = RF - motorCorrectCoefficient;
            RR = RR - motorCorrectCoefficient;
        }

        if (currentZint < initZ) {  //Robot has drifted right
            LF = LF - motorCorrectCoefficient;
            LR = LR - motorCorrectCoefficient;
            RF = RF + motorCorrectCoefficient;
            RR = RR + motorCorrectCoefficient;
        }
    }

    private void getVuforiaLocation() {
        vuforiaTracking = myVuforia.getLocation(myTrackables);
        robotX = vuforiaTracking.get(0);
        robotY = vuforiaTracking.get(1);
        robotBearing = vuforiaTracking.get(2);

    }

    public double getRadians(double heading) {
        radians = heading * (Math.PI / 180);

        return radians;
    }

    /**
     * Transmit telemetry.
     */
    private void telemetry() {

        opMode.telemetry.addData("Procedure", String.valueOf(procedure));
        opMode.telemetry.addData("Heading", String.valueOf(heading));
        opMode.telemetry.addData("robotX", String.valueOf((int) robotX));
        opMode.telemetry.addData("robotY", String.valueOf((int) robotY));
        opMode.telemetry.addData("robotBearing", String.valueOf((int) robotBearing));
        opMode.telemetry.addData("Current Z Int", String.valueOf(currentZint));
        opMode.telemetry.addData("Z Correction", String.valueOf(zCorrection));
        opMode.telemetry.addData("touchSensor", String.valueOf(robot.touchSensor.getValue()));
        opMode.telemetry.addData("ODS", String.valueOf(ods));
        opMode.telemetry.addData("Target Encoder Position", String.valueOf(myTargetPosition));
        opMode.telemetry.addData("Current Encoder Position", String.valueOf(robot.motorLF.getCurrentPosition()));
        opMode.telemetry.addData("LF", String.valueOf(LF));
        opMode.telemetry.addData("RF", String.valueOf(RF));
        opMode.telemetry.addData("LR", String.valueOf(LR));
        opMode.telemetry.addData("RR", String.valueOf(RR));
        opMode.telemetry.update();
    }

    /**
     * Log data to the file on the phone.
     */
    private void logData() {

        Dl.addField(String.valueOf(runtime.time()));
        Dl.addField(String.valueOf(""));
        Dl.addField(String.valueOf(""));
        Dl.addField(String.valueOf(procedure));
        Dl.addField(String.valueOf(""));
        Dl.addField(String.valueOf(heading));
        Dl.addField(String.valueOf((int) robotX));
        Dl.addField(String.valueOf((int) robotY));
        Dl.addField(String.valueOf(""));
        Dl.addField(String.valueOf(""));
        Dl.addField(String.valueOf((int) robotBearing));
        Dl.addField(String.valueOf(initZ));
        Dl.addField(String.valueOf(currentZint));
        Dl.addField(String.valueOf(zCorrection));
        Dl.addField(String.valueOf(robot.touchSensor.getValue()));
        Dl.addField(String.valueOf(ods));
        Dl.addField(String.valueOf(colorRightRed));
        Dl.addField(String.valueOf(colorRightBlue));
        Dl.addField(String.valueOf(colorLeftRed));
        Dl.addField(String.valueOf(colorLeftBlue));
        Dl.addField(String.valueOf(myTargetPosition));
        Dl.addField(String.valueOf(robot.motorLF.getCurrentPosition()));
        Dl.addField(String.valueOf(LF));
        Dl.addField(String.valueOf(RF));
        Dl.addField(String.valueOf(LR));
        Dl.addField(String.valueOf(RR));
        Dl.newLine();
    }
}
