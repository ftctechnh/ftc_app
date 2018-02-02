package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;



@TeleOp
public class Synchronizing extends LinearOpMode {

    // our DC motor.
    DcMotor m1,m2;



    private double power = 0;
    private double power2 = 0;

    double init = 0.2;
    int old1 = 0;
    int old2 = 0;

    double factor = 1;

    public void runOpMode() {

        m1 = hardwareMap.get(DcMotor.class, "Motor1");
        m2 = hardwareMap.get(DcMotor.class,"Motor2");

        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // wait for start command.
        waitForStart();


        // display info to user.
        while(opModeIsActive()) {

            int e1 = m1.getCurrentPosition();
            int e2 = m2.getCurrentPosition();



            factor = (e2 - old2)/(e1 - old1);

            if ((factor * init) > 1){
                init = 1/factor;
            }

            String data = "vel1:" + e1 +"\nvel2"+ e2;

            telemetry.addData("v:",data);


            if (gamepad1.a){
                power = init;
                power2 = init*factor; //try lower values
            }
            if (gamepad1.x){
                power = 0;
                power2 = 0;
            }
            if (power != 0){
                telemetry.update();
            }

            m1.setPower(power);
            m2.setPower(power2);


            old1 = e1;
            old2 = e2;

        }
    }
}