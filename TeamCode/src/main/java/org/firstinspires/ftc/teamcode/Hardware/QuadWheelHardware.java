package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class QuadWheelHardware extends BaseHardware {
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public QuadWheelHardware(LinearOpMode opMode) {
        super(opMode);
    }

    @Override
    public void init() {
        frontLeft = hwMap.dcMotor.get("frontLeft");
        frontRight = hwMap.dcMotor.get("frontRight");
        backLeft = hwMap.dcMotor.get("backLeft");
        backRight = hwMap.dcMotor.get("backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        super.init();
    }
}
