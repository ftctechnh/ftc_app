package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class HangSlides {
    LinearOpMode opMode;
    private MotorGroup hangMotors;
    private DcMotor topMotor, bottomMotor;

    public HangSlides(LinearOpMode opMode) {
        this.opMode = opMode;
        this.topMotor = opMode.hardwareMap.dcMotor.get("hangTop");
        this.bottomMotor = opMode.hardwareMap.dcMotor.get("hangBottom");
        this.topMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.bottomMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        this.hangMotors = new MotorGroup(new DcMotor[]{topMotor, bottomMotor});
        hangMotors.useEncoders();
        hangMotors.setBrake();

    }

    public void setPower(double power) {
        hangMotors.setPower(power);
    }

    public void moveSlides(Direction direction,double power,double inches,double timeoutS) {
        hangMotors.resetEncoders();
        hangMotors.useEncoders();

        int topTarget,bottomTarget;
        switch (direction) {
            case UP:
                topTarget = topMotor.getCurrentPosition() + (int) (inches * Values.HANG_TICKS_PER_INCH);
                bottomTarget = bottomMotor.getCurrentPosition() + (int) (inches * Values.HANG_TICKS_PER_INCH);
                power = Math.abs(power);
                break;
            case DOWN:
                topTarget = topMotor.getCurrentPosition() - (int) (inches * Values.HANG_TICKS_PER_INCH);
                bottomTarget = bottomMotor.getCurrentPosition() - (int) (inches * Values.HANG_TICKS_PER_INCH);
                power = -Math.abs(power);
                break;

            default:
               topTarget = topMotor.getCurrentPosition();
               bottomTarget = bottomMotor.getCurrentPosition();

        }

        topMotor.setTargetPosition(topTarget);
        bottomMotor.setTargetPosition(bottomTarget);
        hangMotors.runToPosition();

        hangMotors.setPower(power);

        double startTime = System.currentTimeMillis();
        while (topMotor.isBusy() && bottomMotor.isBusy() &&
                (System.currentTimeMillis() - startTime) / 1000 < timeoutS) {

        }

        hangMotors.setPower(0);


    }
}
