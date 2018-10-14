package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Shreyas on 9/8/18.
 */
@Autonomous
public class FTC_Control_Tutorial extends LinearOpMode {

    private DcMotor Motor1;
    @Override
    public void runOpMode() throws InterruptedException {
        Motor1 = hardwareMap.dcMotor.get("Motor1");
        waitForStart();
        Motor1.setPower(1);
        sleep(5000);
        Motor1.setPower(0);

    }
}
