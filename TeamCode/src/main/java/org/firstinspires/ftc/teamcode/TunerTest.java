package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;



@TeleOp(name = "TunerTest", group = "Joystick Opmode")
public class TunerTest extends OpMode {

    private double forward = 0;
    private double turn = 0;
    private double tuneValue = 0;

    //make sure to match these names when getting values
    private String[] titles = new String[] {"tuneValue", "DriveExponent", "TurnExponent"};
    private double[] values = new double[] {    0,              2,              2};

    private Tuner tuner;

    @Override
    public void init() {


        tuner = new Tuner(titles, values, gamepad1, telemetry);

    }

    @Override
    public void loop() {

        tuner.tune();


        if (gamepad1.dpad_up){
            forward = 0.9;
            turn = 0;
        }else if (gamepad1.dpad_down){
            forward = -0.9;
            turn = 0;}
        else if (gamepad1.dpad_right){
            forward = 0;
            turn = 0.9;
        }else if (gamepad1.dpad_left){
            forward = 0;
            turn = -0.9;
        }else{
            forward = Calculate.sensCurve(-gamepad1.left_stick_y, tuner.get("DriveExponent"));
            turn = Calculate.sensCurve(-gamepad1.right_stick_x, tuner.get("TurnExponent"));
        }



        telemetry.addData("forward",forward);
        telemetry.addData("turn",turn);
        telemetry.update();

    }



}
