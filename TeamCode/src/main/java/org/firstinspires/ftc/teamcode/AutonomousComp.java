
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous
public class AutonomousComp extends LinearOpMode {

    private DcMotor m1,m2,m3,m4;
    boolean finished = false;
    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        m1 = hardwareMap.get(DcMotor.class,"Motor1");
        m2 = hardwareMap.get(DcMotor.class,"Motor2");
        m3 = hardwareMap.get(DcMotor.class,"Motor3");
        m4 = hardwareMap.get(DcMotor.class,"Motor4");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (!finished){
                m1.setPower(0.5);
                m2.setPower(-0.5);
                m3.setPower(0.5);
                m4.setPower(-0.5);

                sleep(1000);

                m1.setPower(-0.5);
                m2.setPower(0.5);
                m3.setPower(-0.5);
                m4.setPower(0.5);

                sleep(1000);

                m1.setPower(0);
                m2.setPower(0);
                m3.setPower(0);
                m4.setPower(0);

                finished = true;
            }
        }
    }
}
