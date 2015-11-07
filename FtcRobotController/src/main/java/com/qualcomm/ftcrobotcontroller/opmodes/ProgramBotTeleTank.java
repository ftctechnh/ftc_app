package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by sam on 17-Oct-15.
 */
public class ProgramBotTeleTank extends OpMode {
    RobotMethodLibraries rml = new RobotMethodLibraries();
    private double Lthrottle;
    private double Rthrottle;
    @Override
    public void init() {
        rml.Init_Motors(RobotMethodLibraries.Preset.Tom);
    }
    @Override
    public void loop() {
            Rthrottle = gamepad1.right_stick_y;
            Lthrottle = gamepad1.left_stick_y;
            Lthrottle = Range.clip(Lthrottle, -1, 1);
            Rthrottle = Range.clip(Rthrottle, -1, 1);
            rml.BR.setPower(-Rthrottle);
            rml.FR.setPower(-Rthrottle);
            rml.FL.setPower(-Lthrottle);
            rml.BL.setPower(-Lthrottle);
    }
}
