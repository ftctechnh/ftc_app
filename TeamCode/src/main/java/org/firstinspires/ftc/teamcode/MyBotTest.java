package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="MyBot: Test", group="K9bot")
@Disabled

public class MyBotTest extends LinearOpMode {
    
    MyBot robot = new MyBot();
    double armPosition = robot.ARM_HOME;
    double clawPosition = robot.CLAW_HOME;
    final double CLAW_SPEED = 0.01;
    final double ARM_SPEED = 0.01;
    
    @Override
    public void runOpMode() {
        
        double mode = 0;
        double left;
        double right;
        
        robot.init(hardwareMap);
        
        telemetry.addData("Ready for testing...");
        telemetry.update();
        
        waitForStart();
        
        while(opModeIsActive()) {
            
            if (gamepad1.a) mode = 1;
            if (gamepad1.b) mode = 2;
            if (gamepad1.x) mode = 3;
            if (gamepad1.y) mode = 4;
            
            switch (mode) {
                
                case 1:
                    robot.leftMotor.setPower(1);
                    robot.rightMotor.setPower(1);
                    sleep(1000);
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0);
                break;
                    
                case 2:
                    robot.leftMotor.setPower(1);
                    robot.rightMotor.setPower(1);
                    sleep(500);
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0);
                break;
                    
                case 3:
                    robot.leftMotor.setPower(0.5);
                    robot.rightMotor.setPower(0.5);
                    sleep(1000);
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0);
                break;
                    
                case 4:
                    robot.leftMotor.setPower(0.5);
                    robot.rightMotor.setPower(0.5);
                    sleep(500);
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0);
                break;
                    
                default:
                    left = -gamepad1.left_stick_y;
                    right = -gamepad1.right_stick_y;
                    robot.leftMotor.setPower(left);
                    robot.leftMotor.setPower(right);
                break;
                
            }
            
            mode = 0;
            
        }
        
    }
}
