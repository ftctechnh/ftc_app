package org.firstinspires.ftc.teamcode.roverRuckus;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp (name = "BasicTeleOp", group = "usable")
public class BasicTeleOp extends OmniMode {
    //
    public void runOpMode(){
        //
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        //
        configureMotors();
        //
        waitForStartify();
        //
        Double rightPower = .5;
        Double leftPower = .5;
        //
        while (opModeIsActive()){
            //
            left.setPower(-gamepad1.left_stick_y * leftPower);
            right.setPower(-gamepad1.right_stick_y * rightPower);
            //
            if (gamepad1.right_trigger > 0){
                rightPower = .2;
                telemetry.addData("Trigger", "Right");
            } else if (gamepad1.right_bumper){
                rightPower = .05;
                telemetry.addData("Bumper", "Right");
            } else{
                rightPower = .5;
                telemetry.addData("No action", "Right");
            }
            telemetry.addData("rightPower", rightPower);
            telemetry.addData("Right Speed", right.getPower());
            //
            if (gamepad1.left_trigger > 0){
                leftPower = .2;
                telemetry.addData("Trigger", "Left");
            }else if (gamepad1.left_bumper){
                leftPower = .05;
                telemetry.addData("Bumper", "Left");
            }else {
                leftPower = .5;
                telemetry.addData("No action", "Left");
            }
            telemetry.addData("leftPower", leftPower);
            telemetry.addData("Left Speed", left.getPower());
            telemetry.update();
        }
    }
}
