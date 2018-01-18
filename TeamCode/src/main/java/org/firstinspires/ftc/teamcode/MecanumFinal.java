
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;


@TeleOp

public class MecanumFinal extends LinearOpMode {

    private DcMotor m1, m2,m3,m4,lift,grab1,grab2;

    private double rightPower = 0;
    private double leftPower = 0;

    private boolean opened = false;



    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        m1 = hardwareMap.get(DcMotor.class, "Motor1");
        m2 = hardwareMap.get(DcMotor.class, "Motor2");
        m3 = hardwareMap.get(DcMotor.class,"Motor3");
        m4 = hardwareMap.get(DcMotor.class,"Motor4");
        lift = hardwareMap.get(DcMotor.class,"Lift_Motor");
        grab1 = hardwareMap.get(DcMotor.class,"Grab1");
        grab2 = hardwareMap.get(DcMotor.class,"Grab2");

        grab1.setDirection(DcMotor.Direction.REVERSE);

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        grab1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        grab2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        grab1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        grab2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

            rightPower = Range.clip(gamepad1.left_stick_y - gamepad1.left_stick_x,-0.5,0.5);
            leftPower = Range.clip(gamepad1.left_stick_y + gamepad1.left_stick_x,-0.5,0.5);



            double turn = Range.clip(gamepad1.right_stick_y,-0.5,0.5);

            if (turn > 0){
                m1.setPower(-turn);
                m2.setPower(turn);
                m3.setPower(turn);
                m4.setPower(-turn);
            }
            else if(turn < 0){
                m1.setPower(turn);
                m2.setPower(-turn);
                m3.setPower(-turn);
                m4.setPower(turn);
            }
            else{
                m1.setPower(-rightPower);
                m2.setPower(-rightPower);
                m3.setPower(leftPower);
                m4.setPower(leftPower);
            }

            if (gamepad1.a){
                liftToPosition(0);
            }
            if (gamepad1.b){
                liftToPosition(-1069);
            }
            if (gamepad1.x){
                liftToPosition(-2616);
            }
            if (gamepad1.y){
                liftToPosition(-4837);
            }

            if ((gamepad1.right_trigger > 0) && (!opened)){
                grabber(true);
                opened = true;;
            }
            if ((gamepad1.right_trigger > 0) && opened){
                grabber(false);
                opened = false;
            }





        }
    }

    private void liftToPosition(int encoder_pos){
        lift.setTargetPosition(encoder_pos);

        double power = 0.0;
        int currentPos = lift.getCurrentPosition();

        if (currentPos > encoder_pos){
            power = -0.5;
        }
        else{
            power = 0.5;
        }
        while (lift.isBusy()){
            lift.setPower(power);
            telemetry.addData("Lift:","Moving...");
            telemetry.update();
        }

        lift.setPower(0);


    }

    private void grabber(boolean open){
        double power = 0.0;
        if (open){
            grab1.setTargetPosition(300);
            grab2.setTargetPosition(300);

            power = 0.5;
        }
        else{
            grab1.setTargetPosition(0);
            grab2.setTargetPosition(0);

            power = -0.5;
        }

        while (grab1.isBusy() && grab2.isBusy()){
            grab1.setPower(power);
            grab2.setPower(power);
        }

        grab1.setPower(0);
        grab2.setPower(0);
    }
}
