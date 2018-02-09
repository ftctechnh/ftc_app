package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.WestCoastRobot;


@TeleOp
//@Disabled
public class WestFinalUsingClass extends OpMode
{

    private WestCoastRobot robot = new WestCoastRobot("Motor1","Motor2","Motor3","Motor4","Lift_Motor","Grabber_Motor",
            "Grab1", "Grab2","Drop1","Drop2","Place","Color_Sensor");

    private boolean deployed = true;
    private boolean opened = true;
    private double sens = 0.5;

    private boolean dropped = false;

    private double rightPower,leftPower;

    private int liftPos1 = -441;
    private int liftPos2 = -735;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        robot.setUpEncoders();


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

        robot.collectBlock(true);
        robot.initServo();

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        rightPower = Range.clip(gamepad1.left_stick_y - gamepad1.left_stick_x,-sens,sens);
        leftPower = Range.clip(gamepad1.left_stick_y + gamepad1.left_stick_x,-sens,sens);



        robot.setMotorPower(false,rightPower,-leftPower,rightPower,-leftPower);
        robot.lift.setPower(0);
        robot.grabber.setPower(0);

        if (gamepad1.right_bumper && ((sens+0.1) <= 1)){
            sens += 0.1;
        }
        if (gamepad1.left_bumper && ((sens-0.1) >= 0.1)){
            sens -= 0.2;
        }

        if (gamepad1.a){
            robot.liftToPosition(0);
        }
        if(gamepad1.b){
            robot.liftToPosition(liftPos1);
        }
        if(gamepad1.x){
            robot.liftToPosition(liftPos2);
        }

        if(gamepad1.y){
            robot.dropBlock(!dropped);
            dropped = !dropped;
        }

        if (gamepad1.left_trigger == 1){
            robot.collectBlock(!deployed);
            deployed = !deployed;
        }

        if (gamepad1.right_trigger == 1){
            robot.openServos(!opened);
            opened = !opened;
        }

        if (gamepad1.dpad_up){
            robot.placeFirstBlock();
        }




    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }



}
