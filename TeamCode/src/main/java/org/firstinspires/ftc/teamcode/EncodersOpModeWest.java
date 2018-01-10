//Author: Jose

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp
//@Disabled
public class EncodersOpModeWest extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor m1,m2;
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


        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        m2.setTargetPosition(targetPos);
        m1.setTargetPosition(targetPos);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            m2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            m1.setPower(power);
            m2.setPower(power);


            while (m2.isBusy() && m1.isBusy()){
                int posMotor2 = m2.getCurrentPosition();
                int posMotor3 = m2.getCurrentPosition();


                String data = "Motor 2 Position:" + posMotor2 + "\nMotor 1 Position:" + posMotor3 + "\nTime:" + runtime;
                telemetry.addData("Data:",data);
                telemetry.update();

            }

            m1.setPower(0);
            m2.setPower(0);


            m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            idle();

        }
    }

}
