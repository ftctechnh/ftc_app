package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp
//@Disabled
public class WestFinal2 extends OpMode
{

    private DcMotor m1, m2,grabber,lift;
    private Servo grab1,grab2,drop1,drop2;

    private double rightPower = 0;
    private double leftPower = 0;

    private int deployedPos = 111;
    private int liftPos1 = -150;
    private int liftPos2 = -432;
    private int placeBlockPos = -20;

    private double grab1Max = 0.28;
    private double grab2Min = 0.65;
    private double drop1Min =0.5 ;
    private double drop2Max = 0.5;
    private double interval = 0.01;

    private boolean deployed = true;
    private boolean opened = false;
    private double sens = 0.5;

    private boolean dropped = false;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        m1 = hardwareMap.get(DcMotor.class, "Motor1");
        m2 = hardwareMap.get(DcMotor.class, "Motor2");
        grabber = hardwareMap.get(DcMotor.class, "Grabber_Motor");
        lift = hardwareMap.get(DcMotor.class, "Lift_Motor");

        grabber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        grabber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        grab1 = hardwareMap.get(Servo.class, "Grab1");
        grab2 = hardwareMap.get(Servo.class, "Grab2");
        drop1 = hardwareMap.get(Servo.class, "Drop1");
        drop2 = hardwareMap.get(Servo.class, "Drop2");

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop(){
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        moveMotor(grabber,deployedPos);
        grab1.setPosition(0);
        grab2.setPosition(1);
        drop1.setPosition(1);
        drop2.setPosition(0);

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        rightPower = Range.clip(gamepad1.left_stick_y - gamepad1.left_stick_x,-sens,sens);
        leftPower = Range.clip(gamepad1.left_stick_y + gamepad1.left_stick_x,-sens,sens);

        m1.setPower(rightPower);
        m2.setPower(-leftPower);

        if (gamepad1.right_bumper && ((sens+0.1) <= 1)){
            sens += 0.1;
        }
        if (gamepad1.left_bumper && ((sens-0.1) >= 0.1)){
            sens -= 0.2;
        }

        if (gamepad1.a){
            moveMotor(lift,0);
        }
        if(gamepad1.b){
            moveMotor(lift,liftPos1);
        }
        if(gamepad1.x){
            moveMotor(lift,liftPos2);
        }
        if(gamepad1.y){
            dropBlock(dropped);
            dropped = !dropped;
        }

        if ((gamepad1.left_trigger > 0) && deployed){
            moveMotor(grabber,placeBlockPos);
            deployed = false;
        }
        if ((gamepad1.left_trigger > 0) && !deployed){
            moveMotor(grabber,deployedPos);
            deployed = true;
        }

        if (gamepad1.right_trigger > 0){
            openServos(opened);
            opened = !opened;
        }




    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    private void moveMotor(DcMotor motor,int new_encoder){
        int current = motor.getCurrentPosition();
        double power = 0;
        if (current < new_encoder){
            power = -0.2;
        }
        else{
            power = 0.2;
        }
        motor.setTargetPosition(new_encoder);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (motor.isBusy()){
            motor.setPower(power);
        }
        motor.setPower(0);
    }

    private void openServos(boolean open){
        if (open){
            double current1 = grab1Max;
            double current2 = grab2Min;
            while ((current1 > interval) || (current2 < 1)){
                current1 = (current1 > 0)? current1 - interval:current1;
                current2 = (current2 < 1)? current2 + interval:current2;
                grab1.setPosition(current1);
                grab2.setPosition(current2);
            }
        }
        else{
            double current1 = interval;
            double current2 = 1;

            while ((current1 < grab1Max) || (current2 > grab2Min)){
                current1 = (current1 < grab1Max)? current1 + interval:current1;
                current2 = (current2 > grab2Min)? current2 - interval:current2;
                grab1.setPosition(current1);
                grab2.setPosition(current2);
            }
        }
    }

    private void dropBlock(boolean drop){
        if (drop){
            double current1 = 1;
            double current2 = 0;
            while ((current1 > drop1Min) || (current2 < drop2Max)){
                current1 = (current1 > drop1Min)? current1 - interval:current1;
                current2 = (current2 < drop2Max)? current2 + interval:current2;
                drop1.setPosition(current1);
                drop2.setPosition(current2);
            }
        }
        else{
            double current1 = drop1Min;
            double current2 = drop2Max;
            while ((current1 < 1) || (current2 > 0)){
                current1 = (current1 < 1)? current1 + interval:current1;
                current2 = (current2 > 0)? current2 - interval:current2;
                drop1.setPosition(current1);
                drop2.setPosition(current2);
            }
        }
    }

}
