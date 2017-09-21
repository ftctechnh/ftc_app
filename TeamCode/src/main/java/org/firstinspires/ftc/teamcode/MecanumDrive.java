package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by vineelavandanapu on 9/12/17.
 */

@TeleOp(name="Mecanum Drive", group="Pushbot")
public class MecanumDrive extends OpMode{

    TestAutonHardware robot       = new TestAutonHardware(); //initializing the robot

    @Override
    public void init() {
        robot.init(hardwareMap);

        telemetry.addData("Say", "Hello Driver");
    }

    @Override
    public void loop() {

        double leftX, rightX, leftY, rightY;
        boolean speedslow;

        leftX = gamepad1.left_stick_x;
        rightX = gamepad1.right_stick_x;
        leftY = gamepad1.left_stick_y;
        rightY = gamepad1.right_stick_y;

        speedslow = gamepad1.left_bumper || gamepad1.right_bumper;

        double v_lf;
        double v_lr;
        double v_rf;
        double v_rr;

        double desiredSpeed = Math.sqrt(leftX*leftX + leftY*leftY);
        double direction;
        double rotationSpeed = rightX;

        if(!(rightX ==0 && rightY == 0 && !speedslow)){
            desiredSpeed = desiredSpeed*0.4;
        }

        if (leftX != 0){
            if(leftY == 0){
                if(leftX > 0) direction = -Math.PI/2;
                else direction = Math.PI/2;
            } else {
                direction = Math.atan(leftY / leftX) + Math.PI / 2;
            }
        }else{
            if(leftY > 0){
                direction = 0;
            }else if(leftY < 0){
                direction = -Math.PI;
            }else {
                direction = 0;
            }
        }

        if (!(leftX == 0 && leftY == 0 && !speedslow)) {
            rotationSpeed = rotationSpeed*0.4;

        }

        v_lf = -(desiredSpeed * Math.sin(direction + Math.PI/4) + rotationSpeed);
        v_rf = (desiredSpeed * Math.cos(direction + Math.PI/4) - rotationSpeed);
        v_lr = -(desiredSpeed * Math.cos(direction + Math.PI/4) + rotationSpeed);
        v_rr = (desiredSpeed * Math.sin(direction + Math.PI/4) - rotationSpeed);

        robot.MotorFrontLeft.setPower(v_lf);
        robot.MotorFrontRight.setPower(v_rf);
        robot.MotorRearLeft.setPower(v_lr);
        robot.MotorRearRight.setPower(v_rr);

        telemetry.addData("Front Left", v_lf);
        telemetry.addData("Right Front", v_rf);
        telemetry.addData("Left Rear", v_lr);
        telemetry.addData("Right Rear", v_rr);

    }
}
