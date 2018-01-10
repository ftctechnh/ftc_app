//Author: Jose

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp
//@Disabled
public class EncodersOpModeMecanum extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor m1,m2,m3,m4;
    private ElapsedTime runtime = new ElapsedTime();

    private double power = 0.5;
    private int targetPos = 400;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        m1 = hardwareMap.get(DcMotor.class, "Motor1");
        m2 = hardwareMap.get(DcMotor.class,"Motor2");
        m3 = hardwareMap.get(DcMotor.class,"Motor3");
        m4 = hardwareMap.get(DcMotor.class,"Motor4");



        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        m2.setTargetPosition(targetPos);
        m3.setTargetPosition(targetPos);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            m2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m3.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            m1.setPower(-power);
            m3.setPower(-power);
            m3.setPower(power);
            m4.setPower(power);

            while (m2.isBusy() && m3.isBusy()){
                int posMotor2 = m2.getCurrentPosition();
                int posMotor3 = m3.getCurrentPosition();


                String data = "Motor 2 Position:" + posMotor2 + "\nMotor 3 Position:" + posMotor3 + "\nTime:" + runtime;
                telemetry.addData("Data:",data);
                telemetry.update();
            }

            m1.setPower(0);
            m2.setPower(0);
            m3.setPower(0);
            m4.setPower(0);

            m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            idle();

        }
    }

}
