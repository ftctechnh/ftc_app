package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "7518Teleop", group = "7518")
public class Team7518Teleop extends LinearOpMode{

    private DcMotor  leftFront, rightFront, leftRear, rightRear, absoluteRight, absoluteLeft, yAxis, rotationMotor;
    private Servo colorSensorServo;
    DigitalChannel upperLimitSwitch;
    DigitalChannel lowerLimitSwitch;
    DeviceInterfaceModule cdi;

    @Override
    public void runOpMode() throws InterruptedException
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //Declare hardwareMap here, example below
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        absoluteRight=hardwareMap.dcMotor.get("absoluteRight");
        absoluteLeft=hardwareMap.dcMotor.get("absoluteLeft");
        yAxis=hardwareMap.dcMotor.get("yAxis");
        rotationMotor=hardwareMap.dcMotor.get("rotationMotor");
        colorSensorServo=hardwareMap.servo.get("colorSensorServo");
        upperLimitSwitch=hardwareMap.digitalChannel.get("upperLimitSwitch");
        lowerLimitSwitch=hardwareMap.digitalChannel.get("lowerLimitSwitch");
        cdi=hardwareMap.deviceInterfaceModule.get("Device Interface Module 1");

        //Include any code to run only once here
        waitForStart();


        while(opModeIsActive())
        {
                colorSensorServo.setPosition(1); //lock color sensor arm so it doesn't fall over as it is not in use during TeleOp



                        //Include commands to run on controller presses here

                        //Mecanum Drive
                        double h = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
                        double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;

                        final double leftFrontPower = h * Math.sin(robotAngle) - gamepad1.right_stick_x;
                        final double rightFrontPower = h * Math.cos(robotAngle) + gamepad1.right_stick_x;
                        final double leftRearPower = h * Math.cos(robotAngle) - gamepad1.right_stick_x;
                        final double rightRearPower = h * Math.sin(robotAngle) + gamepad1.right_stick_x;

                        leftFront.setPower(leftFrontPower);
                        rightFront.setPower(-rightFrontPower);
                        leftRear.setPower(leftRearPower);
                        rightRear.setPower(-rightRearPower);

                        //green wheels
                        if (gamepad1.left_trigger>0){
                            absoluteLeft.setPower(gamepad1.left_trigger);
                            absoluteRight.setPower(-gamepad1.left_trigger);
                        }//end if
                        else if (gamepad1.right_trigger>0){
                            absoluteLeft.setPower(-gamepad1.right_trigger);
                            absoluteRight.setPower(gamepad1.right_trigger);
                        }//end else if
                        else{
                            absoluteLeft.setPower(0);
                            absoluteRight.setPower(0);
                        }//end else
           



                        //rotation motor
                        if (gamepad1.dpad_up)
                            rotationMotor.setPower(.25);
                        else if (gamepad1.dpad_down)
                            rotationMotor.setPower(-.25);
                        else
                            rotationMotor.setPower(0);


            telemetry.addData("bottom", upperLimitSwitch.getState()); //get limit switch state (upper)
            for (int i = 0; i < 8; i++){
                telemetry.addData("Digital " + i, cdi.getDigitalChannelState(i));
            }
          boolean up = upperLimitSwitch.getState();
          boolean down = lowerLimitSwitch.getState();

                        //y-axis motor. We need to add another condition for the if statement: read the limit switch
                        if ((gamepad1.y) && !up)  //and the upper limit switch is not pressed. this way if the upper limit switch is pressed we can't go up any more
                            yAxis.setPower(.75);
                        else if ((gamepad1.x) && !down) //and the lower limit switch is not pressed. this way if the lower limit switch is pressed we can't go down any more
                            yAxis.setPower(-.75);
                        else
                            yAxis.setPower(0);


                        telemetry.update();
                        idle();

        }//end while(opModeIsActive)


    }//end runOpMode
}//end class
