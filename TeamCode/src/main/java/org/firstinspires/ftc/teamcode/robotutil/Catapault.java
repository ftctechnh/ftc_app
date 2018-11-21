package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utils.Utils;

/**
 * Created by antonlin on 9/9/18.
 */

public class Catapault {
    private DcMotor catapaultMotor;
    MotorGroup motors;
    LinearOpMode opMode;
    Logger l = new Logger("CATAPTULT");

    public Catapault(LinearOpMode opMode, String hardwareName) {
        this.opMode = opMode;
        catapaultMotor = opMode.hardwareMap.dcMotor.get(hardwareName);
        catapaultMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        catapaultMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors = new MotorGroup(new DcMotor[]{catapaultMotor});
    }

    public void setPower(double power){
        this.catapaultMotor.setPower(power);
    }


    public void flick(double power, int time){
        l.log("flick()");
        l.logData("power",power);
        l.logData("time",time);
        l.log("starting flick");
        catapaultMotor.setPower(power);
        Utils.waitFor(time);
        catapaultMotor.setPower(0);
        l.log("stopped flick");

    }

    public void flick(Direction direction,double power,double ticks,double timeoutS) {
        l.log("starting flick");
        l.logData("power",power);
        l.logData("ticks",ticks);
        motors.useEncoders();
        motors.resetEncoders();
        int target;
        switch (direction) {
            case UP:
                target = catapaultMotor.getCurrentPosition() + (int) (ticks);
                power = Math.abs(power);
                break;
            case DOWN:
                target = catapaultMotor.getCurrentPosition() - (int) (ticks);
                power = -Math.abs(power);
                break;
            default:
                target = catapaultMotor.getCurrentPosition();
        }
        l.logData("target",target);
        catapaultMotor.setTargetPosition(target);
        motors.runToPosition();
        catapaultMotor.setPower(power);

        double startTime = System.currentTimeMillis();
        while (catapaultMotor.isBusy()  &&
                (System.currentTimeMillis() - startTime) / 1000 < timeoutS) {
            l.log("in flick loop");
            l.logData("pos",catapaultMotor.getCurrentPosition());

        }
        catapaultMotor.setPower(0);
        l.logData("pos",catapaultMotor.getCurrentPosition());

        l.log("stopped");
    }
}
