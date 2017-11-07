package org.firstinspires.ftc.teamcode.color;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.configuration.LynxI2cDeviceConfiguration;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;



/**
 * Created by Kit Caldwell on 9/26/2017.
 */

@TeleOp
public class colorTest extends OpMode{

    LynxI2cColorRangeSensor colorRange;
    I2cAddr ColorNumber = I2cAddr.create7bit(0x39);

    public void init(){
        colorRange = (LynxI2cColorRangeSensor)hardwareMap.i2cDevice.get("sensorColorRange");
        ColorNumber = (new I2cAddr(0x39));
    }

    public void loop(){
        telemetry.addData("Distance", getDistance(DistanceUnit.CM));
        telemetry.addData("Light Detected", getLightDetected());
        telemetry.addData("Raw Light Detected", getRawLightDetected());
        telemetry.addData("Raw Light Max", getRawLightDetectedMax());
        telemetry.update();
    }

    public LynxI2cColorRangeSensor
    getDistance (DistanceUnit unit){
        return colorRange;
    }

    public LynxI2cColorRangeSensor getLightDetected () {
        return colorRange;
    }

    public LynxI2cColorRangeSensor
    getRawLightDetected () {
        return colorRange;
    }

    public LynxI2cColorRangeSensor
    getRawLightDetectedMax () {
        return colorRange;
    }

    public String status () {
        return null;
    }

}