
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp

public class TankDrive extends OpMode {
    /* Declare OpMode members. */
    private DcMotor m1,m2;
    //m3,m4;

    
    private ElapsedTime runtime = new ElapsedTime();
    private double leftPower;
    private double rightPower;

    @Override
    public void init() {
        telemetry.addData("Status:", "Initialized");

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        telemetry.addData("Status:","Waiting for start");
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        telemetry.addData("Status:","Started");
        
        
        

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        m1 = hardwareMap.get(DcMotor.class, "Motor1");
        m2 = hardwareMap.get(DcMotor.class, "Motor2");
        //m3 = hardwareMap.get(DcMotor.class,"Motor3");
        //m4 = hardwareMap.get(DcMotor.class,"Motor4");
        rightPower = Range.clip(gamepad1.right_stick_y,-0.5,0.5);
        leftPower = Range.clip(gamepad1.left_stick_y,-0.5,0.5);
        
        double trigger = gamepad1.right_trigger;
        double trigger2 = gamepad1.left_trigger;
        double arm_power = 0;
        if (trigger > 0){
            arm_power = Range.clip(trigger,-0.2,0.2);
        }
        else if (trigger2 >0){
            arm_power = Range.clip(-trigger2,-0.2,0.2);
        }

        if (gamepad1.a){
            m1.setPower(10);
        }
        if (gamepad1.b){
            m2.setPower(10);
        }
        //if (gamepad1.x){
            //m3.setPower(10);

        //if (gamepad1.y){
            //m4.setPower(10);


        
        
        m1.setPower(-rightPower);
        m2.setPower(-leftPower);
        //m3.setPower(leftPower);
        //m4.setPower(leftPower);

        
        telemetry.addData("Status",runtime.toString());


    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}
