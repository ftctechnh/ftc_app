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
    private String[] titles = new String[] {"tuneValue", "title1", "title2", "title3", "title4", "title5", "title6", "title7", "title8"};
    private double[] values = new double[] {    0,        0,        0,        0,        0,        0,        0,        0,        0   };

    private Tuner tuner;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");


        tuner = new Tuner(titles, values, gamepad1, telemetry);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {

        tuner.tune();
        telemetry.addData("get(tuneValue)", tuner.get("tuneValue"));


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
            forward = Calculate.sensCurve(-gamepad1.left_stick_y, 2);
            turn = Calculate.sensCurve(-gamepad1.right_stick_x, 2);
        }



        telemetry.addData("forward",forward);
        telemetry.addData("turn",turn);
        telemetry.update();

    }



}
