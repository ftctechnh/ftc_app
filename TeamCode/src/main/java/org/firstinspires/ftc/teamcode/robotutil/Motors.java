package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Motors {

    private Motor[] motors;

    public Motors(Motor[] motors) {
        this.motors = motors;
    }

    public Motor[] getMotors() {
        return motors;
    }

    public int[] getCurrentPositions() {
        int[] currentPositions = new int[motors.length];
        for (int i = 0; i < motors.length; i++) {
            currentPositions[i] = motors[i].getCurrentPosition();
        }
        return currentPositions;
    }

    public void setPowers(double... powers) {
        if (powers.length == motors.length) {
            for (int i = 0; i < motors.length; i++) {
                motors[i].setPower(powers[i]);
            }
        }
    }

    public void setAllTargets(int ticks) {
        for (Motor motor : motors) {
            motor.setTarget(ticks);
        }
    }

    public void setTargets(int... ticks) {
        if (ticks.length == motors.length) {
            for (int i = 0; i < motors.length; i++) {
                motors[i].setTarget(ticks[i]);
            }
        }

    }

    public void updateErrors() {
        for (Motor motor : motors) {
            motor.updateError();
        }
    }

    public void addPID(double kp, double ki, double kd, double minError) {
        for (Motor motor : motors) {
            motor.addPID(kp, ki, kd);
            motor.setMinError(minError);
        }
    }

    public boolean withinMinError() {
        for (Motor motor : motors) {
            if (motor.withinMinError()) {
                return true;
            }
        }
        return false;
    }

    public double[] getPIDOutput() {
        double[] outputs = new double[motors.length];
        for (int i = 0; i < motors.length; i++) {
            outputs[i] = motors[i].getPIDOutput();
        }
        return outputs;
    }

    public void useEncoders() {
        for (Motor motor : this.motors) {
            motor.getMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void withoutEncoders() {
        for (Motor motor : this.motors) {
            motor.getMotor().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void runToPosition(){
        for (Motor motor : this.motors) {
            motor.getMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public void setBrake() {
        for (Motor motor : this.motors) {
            motor.getMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    public void setCoast() {
        for (Motor motor : this.motors) {
            motor.getMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public void resetEncoders() {
        for (Motor motor : this.motors) {
            motor.getMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.getMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

}
