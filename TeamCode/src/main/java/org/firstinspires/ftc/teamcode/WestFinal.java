package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;



@TeleOp
//@Disabled
public class WestFinal extends OpMode
{

    private DcMotor m1, m2,grabber,lift;
    private Servo grab1,grab2,drop1,drop2,place;

    private double rightPower = 0;
    private double leftPower = 0;

    private int deployedPos = 111;
    private int liftPos1 = -441;
    private int liftPos2 = -735;
    private int placeBlockPos = -20;

    private double grab1Max = 0.5; //change
    private double grab2Min = 0.3; //change
    private double grab1Min = 0.25; //change
    private double grab2Max = 0.75; //change

    private double drop1Min = 0.5;
    private double drop2Max = 0.95;
    private double drop1Max = 1;
    private double drop2Min = 0.43;

    private double placeMin = 0.5;
    private double placeMax = 1;

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
        grabber.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        grab1 = hardwareMap.get(Servo.class, "Grab1");
        grab2 = hardwareMap.get(Servo.class, "Grab2");
        drop1 = hardwareMap.get(Servo.class, "Drop1");
        drop2 = hardwareMap.get(Servo.class, "Drop2");
        place = hardwareMap.get(Servo.class,"Place");

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
        grab1.setPosition(grab1Max);
        grab2.setPosition(grab1Min);
        drop1.setPosition(drop1Min);
        drop2.setPosition(drop2Max);

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop(){
        rightPower = Range.clip(gamepad1.left_stick_y - gamepad1.left_stick_x,-sens,sens);
        leftPower = Range.clip(gamepad1.left_stick_y + gamepad1.left_stick_x,-sens,sens);

        m1.setPower(rightPower);
        m2.setPower(-leftPower);

        lift.setPower(0);
        grabber.setPower(0);

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

        if (gamepad1.dpad_up){
            place.setPosition(placeMax);
            try {
                Thread.sleep(500);
            }catch(InterruptedException exceptin){

            }
            place.setPosition(placeMin);
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
        double power;
        if (current < new_encoder){
            power = 0.2;
        }
        else{
            power = -0.2;
        }
        motor.setTargetPosition(new_encoder);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (motor.isBusy()){
            motor.setPower(power);
        }
        motor.setPower(0);
    }

    public void openServos(boolean open){
        if (open){
            double current1 = grab1Max;
            double current2 = grab2Min;
            while ((current1 > grab1Min) || (current2 < grab2Max)){
                current1 = (current1 > grab1Min)? current1 - interval:current1;
                current2 = (current2 < grab2Max)? current2 + interval:current2;
                grab1.setPosition(current1);
                grab2.setPosition(current2);
            }
        }
        else{
            double current1 = grab1Min;
            double current2 = grab2Max;

            while ((current1 < grab1Max) || (current2 > grab2Max)){
                current1 = (current1 < grab1Max)? current1 + interval:current1;
                current2 = (current2 > grab2Max)? current2 - interval:current2;
                grab1.setPosition(current1);
                grab2.setPosition(current2);
            }
        }
    }

    public void dropBlock(boolean dropBlock){
        if (dropBlock){
            double current1 = drop1Max;
            double current2 = drop2Min;
            while ((current1 > drop1Min) || (current2 < drop2Max)){
                current1 =  (current1 > drop1Min)?current1 - interval:current1;
                current2 = (current2 < drop2Max)?current2 + interval:current2;
                drop1.setPosition(current1);
                drop2.setPosition(current2);
            }
        }
        else{
            double current1 = drop1Min;
            double current2 = drop2Max;
            while ((current1 < drop1Max) || (current2 > drop2Min)){
                current1 =  (current1 < drop2Max)?current1 + interval:current1;
                current2 = (current2 > drop2Max)?current2 - interval:current2;
                drop1.setPosition(current1);
                drop2.setPosition(current2);

            }
        }
    }
}
