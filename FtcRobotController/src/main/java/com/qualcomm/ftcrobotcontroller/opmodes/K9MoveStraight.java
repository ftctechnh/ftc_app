
package com.qualcomm.ftcrobotcontroller.opmodes;

        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.DcMotorController;

public class K9MoveStraight extends OpMode{
    DcMotor DC_left;
    DcMotor DC_right;


    @Override
    public void init () {
        DC_left = hardwareMap.dcMotor.get("DC_left");
        DC_right = hardwareMap.dcMotor.get("DC_right");

    }

    @Override
    public void loop () {
        DC_left.setPower(0.5);
        DC_right.setPower(0.5);

    }

}