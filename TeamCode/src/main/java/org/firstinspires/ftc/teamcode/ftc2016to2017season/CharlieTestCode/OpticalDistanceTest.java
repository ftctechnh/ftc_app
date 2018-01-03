package org.firstinspires.ftc.teamcode.ftc2016to2017season.CharlieTestCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Steven on 1/5/2017.
 */
@Autonomous(name = "OpticalDistanceTest", group = "Pushbot")
//@Disabled
public class OpticalDistanceTest extends OpMode{
   // OpticalDistanceSensor ODS;
    OpticalDistanceSensor ODS2;
    //double baseline1;
    double baseline2;
    @Override
    public void init() {

       // ODS = hardwareMap.opticalDistanceSensor.get("ODSFront");
        ODS2 = hardwareMap.opticalDistanceSensor.get("ODSBack");

       // baseline1 = ODS.getRawLightDetected();
        baseline2 = ODS2.getRawLightDetected();
    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){

       // telemetry.addData("1 LightDetected", ODS.getLightDetected());
       // telemetry.addData("1 RawLightDetected",ODS.getRawLightDetected());
      //  telemetry.addData("1 RawLightDetectedMax",ODS.getRawLightDetectedMax());
        telemetry.addData("2 LightDetected", ODS2.getLightDetected());
        telemetry.addData("2 RawLightDetected",ODS2.getRawLightDetected());
        telemetry.addData("2 RawLightDetectedMax",ODS2.getRawLightDetectedMax());
        if (whiteLineDetected()) {
            telemetry.addData("Status: ", "White Line");
        }
        else{
            telemetry.addData("Status: ", "No White Line");
        }
        telemetry.update();
        //gray sheet's rawlight detected around: 0.7, 0.03
        //white line's rawlight detected around: 4.9, 0.1
    }

    public boolean whiteLineDetected(){
        if ((ODS2.getRawLightDetected()> baseline2*3)){
            return true;
        }
        return false;
    }
}
