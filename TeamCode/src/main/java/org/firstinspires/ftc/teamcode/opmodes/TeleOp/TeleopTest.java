package org.firstinspires.ftc.teamcode.opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public abstract class TeleopTest extends LinearOpMode {

    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor linearslidemeasureMotor;
    public DigitalChannel bottomSwitch;
    public DigitalChannel topSwitch;

    public void initializeSystems() {
        linearslidemeasureMotor = hardwareMap.dcMotor.get("linearslideMotor");
        //heightsensor = map.opticalDistanceSensor.get("heightSensor");
        linearslidemeasureMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomSwitch = hardwareMap.digitalChannel.get("bottomswitch");
        topSwitch = hardwareMap.digitalChannel.get("topswitch");
    }

    public void runOpmode() {

        initializeSystems();
        waitForStart();
        telemetry.addLine("Worked for first time");
        telemetry.update();
        runToTop();
    }
    public void runToTop() {
        // checking if heightsensor is 100, setting power to 0 if so
        if (/*heightsensor == 100 ||*/ topSwitch.getState()== true){
            linearslidemeasureMotor.setPower(0);
            telemetry.addLine("Worked for second time");
            telemetry.update();
        }
        else{
            linearslidemeasureMotor.setPower(1);
        }
    }

    public void runToBottom() {
        if (/*heightsensor == 100.0 ||*/ topSwitch.getState()== true){
            linearslidemeasureMotor.setPower(-1);
            telemetry.addLine("Worked for third time");
            telemetry.update();
        }
        else{
            linearslidemeasureMotor.setPower(1);
        }
    }
}