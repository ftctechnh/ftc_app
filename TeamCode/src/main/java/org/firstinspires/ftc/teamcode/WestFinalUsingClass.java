package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.WestCoastRobot;


@TeleOp
//@Disabled
public class WestFinalUsingClass extends OpMode
{

    private WestCoastRobot robot = new WestCoastRobot();

    private boolean deployed = false;
    private boolean opened = true;
    private boolean dropped = false;

    private double sens = 0.6;

    private double rightPower,leftPower;

    private int liftPos1 = -585;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        robot.make(hardwareMap,"Motor1","Motor2","Motor3","Motor4","Lift_Motor","Grabber_Motor","Grab1","Grab2","Drop1","Drop2","Place");
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

        robot.initServo();

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if ((gamepad1.right_stick_y == 0) && (gamepad1.right_stick_x == 0)) {
            rightPower = Range.clip(gamepad1.left_stick_y + gamepad1.left_stick_x, -sens, sens);
            leftPower = Range.clip(gamepad1.left_stick_y - gamepad1.left_stick_x, -sens, sens);
        }else{
            rightPower = Range.clip(gamepad1.right_stick_y + gamepad1.right_stick_x,-0.4,0.4);
            leftPower = Range.clip(gamepad1.right_stick_y - gamepad1.right_stick_x,-0.4,0.4);
            }



        robot.setMotorPower(false,rightPower,-leftPower,rightPower,-leftPower);
        robot.grabber.setPower(0);

        if (gamepad1.right_bumper && ((sens+0.1) <= 1)){
            sens += 0.1;
        }
        if (gamepad1.left_bumper && ((sens-0.1) >= 0.1)){
            sens -= 0.1;
        }

        if (gamepad1.a){
            robot.liftToPosition(20);
        }
        if(gamepad1.b){
            robot.liftToPosition(liftPos1);
        }


        if(gamepad2.y){
            robot.dropBlock(!dropped);
            robot.assistBlock();
            dropped = !dropped;
        }

        while (gamepad2.left_bumper){
            robot.collectBlock(deployed);
        }
        while (gamepad2.right_bumper){
            robot.collectBlock(!deployed);
        }


        if (gamepad2.right_trigger == 1){
            robot.openServos(!opened);
            opened = !opened;
            try{
                Thread.sleep(200);
            }catch(InterruptedException exception){

            }
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
