package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by pramo on 11/15/2017.
 */

public class VerticalLiftMotor {

    private DcMotor liftMotor;

    public VerticalLiftMotor(DcMotor motor) {

        this.liftMotor = motor;

    }

    public void Lift(boolean gp1LeftBumper, boolean gp2LeftBumper
            , double gp1Trigger, double gp2Trigger) {

        if (gp1LeftBumper|| gp2LeftBumper) {
            liftMotor.setPower(1);
        } else if (gp1Trigger != 0 || gp2Trigger != 0) {
            liftMotor.setPower(-1);
        } else {
            liftMotor.setPower(0);
        }
    }

}
