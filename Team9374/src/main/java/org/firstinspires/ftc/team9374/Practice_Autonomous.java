package org.firstinspires.ftc.team8745;
//1120
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
/**
 * Created by some guy "named" Nintendo8 on 10/30/2016.
 */
@Autonomous(name="8k Autonomous 1")

public class Practice_Autonomous extends OpMode {
    private static final int TICS_PER_REV =1120;
    private double WHEEL_DIAMETER = 4;
   private DcMotor leftFRONT;
   private DcMotor rightFRONT;
   private DcMotor leftBACK;
   private DcMotor rightBACK;
   private DcMotor shooterLeft;
    private DcMotor shooterRight;


    private int ticsForInches(double x){
      return (int)((x*TICS_PER_REV)/(Math.PI*WHEEL_DIAMETER));
    }




    // 4 Inches
    public void init() {
        //Front Motors
        leftFRONT = hardwareMap.dcMotor.get("motor-left");
        rightFRONT = hardwareMap.dcMotor.get("motor-right");

        //Back Motors
        leftBACK = hardwareMap.dcMotor.get("motor-leftBACK");
        rightBACK = hardwareMap.dcMotor.get("motor-rightBACK");

        rightFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBACK.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBACK.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //Encoder
        rightFRONT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBACK.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFRONT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBACK.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //setting direction
        rightFRONT.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBACK.setDirection(DcMotorSimple.Direction.REVERSE);
        //The next two lines are obsolete
        leftFRONT.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBACK.setDirection(DcMotorSimple.Direction.FORWARD);

    }

    @Override
    public void loop() {
    if (leftFRONT.getMode()!=DcMotor.RunMode.RUN_TO_POSITION){

        rightFRONT.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBACK.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFRONT.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBACK.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = ticsForInches(60);
        rightFRONT.setTargetPosition(ticks);
        rightBACK.setTargetPosition(ticks);
        leftFRONT.setTargetPosition(ticks);
        leftBACK.setTargetPosition(ticks);

        rightFRONT.setPower(.5);
        rightBACK.setPower(.5);
        leftFRONT.setPower(.5);
        leftBACK.setPower(.5);


    }

telemetry.addData("ticks",rightFRONT.getCurrentPosition());
        telemetry.addData("target",rightFRONT.getTargetPosition());
        telemetry.update();

    }


}
