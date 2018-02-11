package org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by pston on 12/14/2017
 */

public class  RelicGrab {

    private DcMotor relicLift;
    private Servo relicTilt;
    private Servo relicClamp;

    private double tiltPosition; // Stored: 0.539, Upper Bound: 0.531, Lower Bound: 0.59
    private double clampPosition; // Stored: 0, Upper Bound: 0.55, Lower Bound: 0

    public RelicGrab(DcMotor relicLift, Servo relicTilt, Servo relicClamp) {
        this.relicLift = relicLift;
        this.relicTilt = relicTilt;
        this.relicClamp = relicClamp;

        this.relicLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        tiltPosition = 0.36;
        clampPosition = 0.40;
    }

    public void relicLift(boolean leftBumper, float leftTrigger) {
        if (leftBumper) {
            relicLift.setPower(0.5);
        } else if (leftTrigger > 0) {
            relicLift.setPower(-0.5);
        } else {
            relicLift.setPower(0);
        }
    }


    public void setTilt(boolean y, boolean a) {
        if (a && tiltPosition <= 0.44) {
            tiltPosition += 0.01;
        } else if (y && tiltPosition >= 0.36) {
            tiltPosition -= 0.01;
        }
        relicTilt.setPosition(tiltPosition);
    }

    public void setClamp(boolean rightBumper, float rightTrigger) {
        if (rightBumper && clampPosition <= 1) {
            clampPosition += 0.008;
        } else if (rightTrigger > 0 && clampPosition >= 0.4) {
            clampPosition -= 0.008;
        }
        relicClamp.setPosition(clampPosition);
    }

    public void relicGrab(boolean leftBumper, float leftTrigger, boolean dpadUp, boolean dpadDown, boolean y, boolean a, boolean rightBumper, float rightTrigger) {
        relicLift(leftBumper, leftTrigger);
        setTilt(y, a);
        setClamp(rightBumper, rightTrigger);
    }

    public void servoInfo(Telemetry telemetry) {
        telemetry.addData("Tilt Servo Position", tiltPosition);
        telemetry.addData("Clamp Servo Position", clampPosition);
    }

}
