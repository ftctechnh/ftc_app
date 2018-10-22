package org.firstinspires.ftc.teamcode.components;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.systems.BaseSystems.System;


public abstract class LinearSlide extends System {
    private DcMotor linearslidemeasureMotor;
    private OpticalDistanceSensor heightsensor;
    private DigitalChannel bottomSwitch;
    private DigitalChannel topSwitch;
    public ElapsedTime runtime = new ElapsedTime();


    public LinearSlide(OpMode opMode, String systemName) {
        super(opMode, systemName);
        linearslidemeasureMotor = map.dcMotor.get("linearslideMotor");
        heightsensor = map.opticalDistanceSensor.get("heightSensor");
        linearslidemeasureMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomSwitch = map.digitalChannel.get("bottomswitch");
        topSwitch = map.digitalChannel.get("topswitch");
    }
    //instead of using the distance sensor
    //might use 2 magnetic limit switches.
    public void runToTop() {
        // checking if heightsensor is 100, setting power to 0 if so
        if (heightsensor == 100 || topSwitch.getState()== true){
            linearslidemeasureMotor.setPower(0);
        }
        else{
            linearslidemeasureMotor.setPower(1);
        }
    }

    public void runToBottom() {
        if (heightsensor == 100.0 || topSwitch.getState()== true){
            linearslidemeasureMotor.setPower(-1);
        }
        else{
            linearslidemeasureMotor.setPower(1);
        }
    }


}

