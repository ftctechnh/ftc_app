package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="MyBot: Test", group="K9bot")
@Disabled

public class MyBotTest extends LinearOpMode {
    
    MyBot robot = new MyBot();
    
    @Override
    public void runOpMode() {
        
        int mode = 0;
        double left;
        double right;
        
        robot.init(hardwareMap);
        
        telemetry.addData("MyBotTest","Ready for testing...");
        telemetry.update();
        
        waitForStart();
        
        while(opModeIsActive()) {
            
            if (gamepad1.a) mode = 1;
            if (gamepad1.b) mode = 2;
            if (gamepad1.x) mode = 3;
            if (gamepad1.y) mode = 4;
            
            switch (mode) {
                
                case 1:
                    // Go full power for 1 second
                    robot.leftMotor.setPower(1);
                    robot.rightMotor.setPower(1);
                    wait(1000);
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0);
                break;
                    
                case 2:
                    // Go full power for half a second
                    robot.leftMotor.setPower(1);
                    robot.rightMotor.setPower(1);
                    wait(500);
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0);
                break;
                    
                case 3:
                    // Go half power for 1 second
                    robot.leftMotor.setPower(0.5);
                    robot.rightMotor.setPower(0.5);
                    wait(1000);
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0);
                break;
                    
                case 4:
                    // Go half power for half a second
                    robot.leftMotor.setPower(0.5);
                    robot.rightMotor.setPower(0.5);
                    wait(500);
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0);
                break;
                    
                default:
                    // Control with gamepad1's right and left sticks
                    left = -gamepad1.left_stick_y;
                    right = -gamepad1.right_stick_y;
                    robot.leftMotor.setPower(left);
                    robot.leftMotor.setPower(right);
                break;
                
            }
            
            mode = 0;

            robot.waitForTick(40);
        }
        
    }
    
    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            System.out.println("interrupted");
        }
    }
}
