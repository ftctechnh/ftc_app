package org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by pston on 11/12/2017
 */

public class BlockLift {

    DcMotor liftMotor;
    Servo leftGrab;
    Servo rightGrab;

    public BlockLift(DcMotor liftMotor, Servo leftGrab, Servo rightGrab) {

        this.liftMotor = liftMotor;
        this.leftGrab = leftGrab;
        this.rightGrab = rightGrab;

    }

}
