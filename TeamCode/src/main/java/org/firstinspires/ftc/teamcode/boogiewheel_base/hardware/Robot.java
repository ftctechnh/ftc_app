package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

import org.firstinspires.ftc.teamcode.framework.userHardware.paths.Path;
import org.firstinspires.ftc.teamcode.framework.util.AbstractRobot;

import java.util.concurrent.Callable;

public class Robot extends AbstractRobot {

    private HardwareDevices hardware;

    //Robot Methods
    public Robot() {
        hardware = new HardwareDevices();
    }

    public void updateAll() {
        hardware.drive.update();
        hardware.intake.update();
        hardware.mineralLift.update();
        hardware.robotLift.update();
    }

    public void stop() {
        hardware.stop();
    }

    //Drive Methods
    public void setDriveY(double y) {
        hardware.drive.setY(y);
    }

    public void setDriveZ(double z) {
        hardware.drive.setZ(z);
    }

    public void driveUpdate() {
        hardware.drive.updateYZDrive();
    }

    public void setDrivePower(double l, double r) {
        hardware.drive.setPower(l, r);
    }

    public Callable toggleDriveInvertedCallable() {
        return () -> {
            hardware.drive.toggleInverted();
            return true;
        };
    }

    public void turnTo(double angle, double speed, double error, int period) {
        hardware.drive.turnTo(angle, speed, error, period);
    }

    public void runDrivePath(Path path) {
        hardware.drive.runDrivePath(path);
    }

    public int[][] recordPath(int numSamples, int timeInterval) {
        return hardware.drive.recordPath(numSamples, timeInterval);
    }

    public void runPath(int[] left, int[] right, int timeInterval) {
        hardware.drive.runPath(left, right, timeInterval);
    }

    public void driveTo(double distance, double speed) {
        hardware.drive.driveTo(distance, speed);
    }

    public void driveTo(double distance, double speed, int angle) {
        hardware.drive.driveTo(distance, speed, angle);
    }

    public int[] recordPathWithHeading(int numSamples, int timeInterval) {
        return hardware.drive.recordPathWithHeading(numSamples, timeInterval);
    }

    public void runPathWithHeading(int[] values, int timeInterval, double speed) {
        hardware.drive.runPathWithHeading(values, timeInterval, speed);
    }

    public void setPosition(int position, double power) {
        hardware.drive.setPosition(position, power);
    }

    public boolean isGyroCalibrated() {
        return hardware.drive.isGyroCalibrated();
    }

    public Callable autonReleaseWheelsSequenceCallable() {
        return () -> {
            hardware.drive.autonReleaseWheelsSequence();
            return true;
        };
    }

    public void autonReleaseWheelsSequence() {
        hardware.drive.autonReleaseWheelsSequence();
    }

    public void autonDriveToWallSequence() {
        hardware.drive.autonDriveToWallSequence();
    }

    public Callable autonDriveToWallSequenceCallable() {
        return () -> {
            hardware.drive.autonDriveToWallSequence();
            return true;
        };
    }

    public double getHeading() {
        return hardware.drive.getHeading();
    }

    //Intake Methods
    public Callable autonIntakeSequenceCallable() {
        return () -> {
            hardware.intake.autonIntakeSequence();
            return true;
        };
    }

    public Callable beginIntakingCallable() {
        return () -> {
            beginIntaking();
            return true;
        };
    }

    public void beginIntaking() {
        hardware.intake.beginIntaking();
    }

    public Callable finishIntakingCallable() {
        return () -> {
            finishIntaking();
            return true;
        };
    }

    public void finishIntaking() {
        hardware.intake.finishIntaking();
    }

    public Callable reverseIntakeCallable() {
        return () -> {
            reverseIntake();
            return true;
        };
    }

    public void reverseIntake() {
        hardware.intake.reverseIntake();
    }

    public Callable liftIntakeCallable() {
        return () -> {
            liftIntake();
            return true;
        };
    }

    public void liftIntake() {
        hardware.intake.liftIntake();
    }

    public Callable lowerIntakeCallable() {
        return () -> {
            lowerIntake();
            return true;
        };
    }

    public void lowerIntake() {
        hardware.intake.lowerIntake();
    }

    //Mineral Lift Methods
    public Callable moveMineralLiftToCollectPositionCallable() {
        return () -> {
            moveMineralLiftToCollectPosition();
            return true;
        };
    }

    public void moveMineralLiftToCollectPosition() {
        hardware.mineralLift.closeGate();
        hardware.mineralLift.moveToCollectPosition();
        hardware.drive.setInverted(false);
    }

    public Callable moveMineralLiftToDumpPositionCallable() {
        return () -> {
            moveMineralLiftToDumpPosition();
            return true;
        };
    }

    public void moveMineralLiftToDumpPosition() {
        hardware.mineralLift.moveToDumpPosition();
        hardware.drive.setInverted(true);
    }

    public Callable openMineralGateCallable() {
        return () -> {
            openMineralGate();
            return true;
        };
    }

    public void openMineralGate() {
        hardware.mineralLift.openGate();
    }

    public Callable closeMineralGateCallable() {
        return () -> {
            closeMineralGate();
            return true;
        };
    }

    public void closeMineralGate() {
        hardware.mineralLift.closeGate();
    }

    public Callable toggleMineralGateCallable() {
        return () -> {
            hardware.mineralLift.toggleGate();
            return true;
        };
    }

    public Callable autonLowerMineralLiftSequenceCallable() {
        return () -> {
            autonLowerMineralLiftSequence();
            return true;
        };
    }

    public void autonLowerMineralLiftSequence() {
        hardware.mineralLift.autonLowerLiftSequence();
    }

    //robot lift methods
    public Callable robotLiftUpCallable() {
        return () -> {
            hardware.robotLift.robotLiftUp();
            return true;
        };

    }

    public Callable robotLiftDownCallable() {
        return () -> {
            hardware.robotLift.robotLiftDown();
            return true;
        };
    }

    public Callable robotLiftStopCallable() {
        return () -> {
            hardware.robotLift.robotLiftStop();
            return true;
        };
    }

    public Callable moveRobotLiftToTopCallable() {
        return () -> {
            hardware.robotLift.raiseLift();
            return true;
        };
    }

    public Callable moveRobotLiftToBottomCallable() {
        return () -> {
            moveRobotLiftToBottom();
            return true;
        };
    }

    public void moveRobotLiftToBottom() {
        hardware.robotLift.lowerLift();
    }

    public Callable dropMarkerCallable() {
        return () -> {
            hardware.drive.dropTeamMarker();
            return true;
        };
    }
}