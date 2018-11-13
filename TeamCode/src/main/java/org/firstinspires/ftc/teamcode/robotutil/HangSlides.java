package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class HangSlides {
    private DcMotor[] hangMotors;
    LinearOpMode opMode;
//    private int[] encoderStartPosition;

    private static final int TOTAL_TICKS = (int) (12 * Values.ticksPerInch);

    public HangSlides(LinearOpMode opMode, DcMotor hangTop, DcMotor hangBottom) {
        this.opMode = opMode;
        this.hangMotors = new DcMotor[]{hangTop, hangBottom};

        Motors.dontUseEncoders(hangMotors);
        Motors.setBrake(hangMotors);

//        this.encoderStartPosition = new int[]{
//                hangMotors[0].getCurrentPosition(),
//                hangMotors[1].getCurrentPosition()
//        };
    }

    public void setPower(double power) {
        for (DcMotor motor : hangMotors) {
            motor.setPower(power);
        }
    }

//    public void up(double power, double timeoutS) {
//        int ticksToMoveTop = TOTAL_TICKS - (hangMotors[0].getCurrentPosition() - encoderStartPosition[0]);
//        int ticksToMoveBot = TOTAL_TICKS - (hangMotors[1].getCurrentPosition() - encoderStartPosition[1]);
//        int ticksToMove = Math.max(ticksToMoveTop, ticksToMoveBot);
//
//        move(power, ticksToMove, timeoutS);
//    }
//
//    public void down(double power, double timeoutS) {
//        int ticksToMoveTop = TOTAL_TICKS - (hangMotors[0].getCurrentPosition() - encoderStartPosition[0]);
//        int ticksToMoveBot = TOTAL_TICKS - (hangMotors[1].getCurrentPosition() - encoderStartPosition[1]);
//        int ticksToMove = Math.max(ticksToMoveTop, ticksToMoveBot);
//
//        move(power, -1 * ticksToMove, timeoutS);
//    }
//
//    private void move(double power, int ticks, double timeoutS) {
//        int topTarget = hangMotors[0].getCurrentPosition() + ticks;
//        int botTarget = hangMotors[1].getCurrentPosition() + ticks;
//
//        hangMotors[0].setTargetPosition(topTarget);
//        hangMotors[1].setTargetPosition(botTarget);
//        Motors.runToPosition(hangMotors);
//
//        double startTime = System.currentTimeMillis();
//        while(opMode.opModeIsActive() && (System.currentTimeMillis() - startTime) / 1000 < timeoutS) {
//            if(!hangMotors[0].isBusy()) {
//                hangMotors[1].setPower(0);
//            }
//            if(!hangMotors[0].isBusy()) {
//                hangMotors[1].setPower(0);
//            }
//        }
//
//        setPower(0);
//        Motors.useEncoders(hangMotors);
//    }
}
