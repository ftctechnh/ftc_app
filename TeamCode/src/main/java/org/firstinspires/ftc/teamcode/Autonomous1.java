
//Autonomous Code

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.io.InterruptedIOException;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous1 extends LinearOpMode {
    DcMotor lift;

    @Override
    public void runOpMode() throws InterruptedException {
        lift = hardwareMap.dcMotor.get("lift");

        while(opModeIsActive()){
            lift.setPower(1);
        }

    }

}
