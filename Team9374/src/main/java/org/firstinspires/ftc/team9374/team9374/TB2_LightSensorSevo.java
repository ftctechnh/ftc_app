package org.firstinspires.ftc.team9374.team9374;


 import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
 import com.qualcomm.robotcore.eventloop.opmode.Disabled;
 import com.qualcomm.robotcore.eventloop.opmode.OpMode;
 import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
 import com.qualcomm.robotcore.hardware.ColorSensor;
 import com.qualcomm.robotcore.hardware.DcMotor;
 import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
 import com.qualcomm.robotcore.hardware.DcMotorSimple;
 import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
 import com.qualcomm.robotcore.hardware.Servo;
 import com.qualcomm.robotcore.util.ElapsedTime;
 import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Tank Drive with Color sensor", group="Noramal_Opmode")
@Disabled
/*
This Tank drive uses the color sensor to move the servo!
 */

public class TB2_LightSensorSevo extends OpMode{

    DcMotor left;
    DcMotor right;
    ColorSensor sensor;
    Servo slapper;
    public ElapsedTime runTime = new ElapsedTime();

    public void init()  {
        left = hardwareMap.dcMotor.get("Motor-left");
        right = hardwareMap.dcMotor.get("Motor-right");
        sensor = hardwareMap.colorSensor.get("Sensor");
        slapper = hardwareMap.servo.get("Servo");
        slapper.setPosition(.5);

        runTime.reset();
    }

 @Override
  public void loop() {
     //All Driving code//

     float leftDC = gamepad1.left_stick_y;
     float rightDC =  gamepad1.right_stick_y;
     double rTrigger = gamepad1.right_trigger;
     double lTrigger = gamepad1.left_trigger;


     //left.setDirection(DcMotorSimple.Direction.REVERSE);//Or .FORWARD
     left.setPower(leftDC); //Flips the Motor direction
     right.setPower(rightDC);

     //End of Driving code//
     //All Servo Code//

     rTrigger = Range.clip(rTrigger,0.5,1); //Controlls right side
     lTrigger = lTrigger/-5 + 0.5;          //Controlls left side

     if (rTrigger > 0.5){
         slapper.setPosition(rTrigger);
     } else if (lTrigger > 0){
         slapper.setPosition(lTrigger);
     } else {
         slapper.setPosition(0.5);
     }

     telemetry.addData("Right Trigger",rTrigger);
     telemetry.addData("Left Trigger", lTrigger);
     telemetry.addData("Sensor Blue", sensor.blue());
     telemetry.addData("Sensor Red", sensor.red());
     telemetry.addData("Time sence start", runTime);



  }
}
