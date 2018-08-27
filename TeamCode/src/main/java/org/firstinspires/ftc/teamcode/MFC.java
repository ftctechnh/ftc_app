package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="MFC", group="Basic OP Mode")
@Disabled

public class MFC extends LinearOpMode {

  private DcMotor motor1 = null;
  private DcMotor motor2 = null;
  private DcMotor motor3 = null;
  private DcMotor motor4 = null;

  private static double[] ansAngleMag(double x, double y){
    double angle = 0;
    if (x<0 && y>0){
      angle+=90;
      x = Math.abs(x);
    } else if (x<0 && y<0){
      angle+=180;
    } else if (x>0 && y<0){
      angle+=270;
      y = Math.abs(y);
    } else if ((x == 0) && (y>0)){
      angle = 90;
    }
    angle += Math.atan(y/x)*180/Math.PI;
    double magnitude = Math.sqrt(x*x + y*y);
    double[] AngleMag = new double[2];
    AngleMag[0] = angle;
    AngleMag[1] = magnitude;
    return AngleMag;
  }

  public void runOpMode(){
    telemetry.addData(  "Status",   "Initialized");
    telemetry.update();

    motor1 = hardwareMap.get(DcMotor.class,                "motor1");
    motor2 = hardwareMap.get(DcMotor.class,                "motor2");
    motor3 = hardwareMap.get(DcMotor.class,                "motor3");
    motor4 = hardwareMap.get(DcMotor.class,                "motor4");
    waitForStart();

    while (opModeIsActive()){
      double power = 0.25;
      double left_y = -gamepad1.left_stick_y;
      double left_x = gamepad1.left_stick_x;
      double m1 = 0;
      double m2 = 0;
      double m3 = 0;
      double m4 = 0;
      if ((left_y != 0) || (left_x != 0)){
        double[] AngleMag = ansAngleMag(left_x, left_y);
        double angle = AngleMag[0];
        double magnitude = AngleMag[1];
        if (((337.5 <= angle) && (angle < 360)) || ((0 <= angle) && (angle <= 22.5))){
          //East-A 2:4+
          m1 = power*magnitude;
        } else if ((22.5 < angle) && (angle < 67.5)){
          //North-B East 1:3+ 2:4+
          m1 = 0;
        } else if ((67.5 <= angle) && (angle <= 112.5)){
          //North 1:3+
          m2 = power*magnitude;
        } else if ((112.5 < angle) && (angle < 157.5)){
          //North West 1:3+ 2:4-
          m2 = 0;
        } else if ((157.5 <= angle) && (angle <= 202.5)){
          //West 2:4-
          m3 = power*magnitude;
        } else if ((202.5 < angle) && (angle < 247.5)){
          //South West 1:3- 2:4-
          m3 = 0;
        } else if ((247.5 <= angle) && (angle <= 292.5)){
          //South 1:3-
          m4 = power*magnitude;
        } else if ((292.5 < angle) && (angle < 337.5)) {
          //South East AC- BD+2
          m4 = 0;
      }
      motor1.setPower(m1);
      motor2.setPower(m2);
      motor3.setPower(m3);
      motor4.setPower(m4);
      }
    }
  }
}
