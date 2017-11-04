package org.firstinspires.ftc.teamcode.TestCode.CoreTest;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "Core Test" , group = "Prototypes")
@Disabled
@SuppressWarnings("unused")
public class OpCoreTest extends LinearOpMode
{
    private TestBase _base = new TestBase();

    @Override
    public void runOpMode() throws InterruptedException
    {
        _base.init(hardwareMap , this);

        waitForStart();

        while(opModeIsActive())
        {
            _base.component.setPower(-gamepad1.left_stick_y);
        }
    }
}
