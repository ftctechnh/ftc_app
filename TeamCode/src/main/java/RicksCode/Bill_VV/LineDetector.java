package RicksCode.Bill_VV;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by jmgu3 on 11/25/2016.
 */

public class LineDetector {

    LightSensor left;
    LightSensor middle;
    LightSensor right;

    public void init (HardwareMap hardwareMap)
    {
        left = hardwareMap.lightSensor.get("left");
        middle = hardwareMap.lightSensor.get("middle");
        right = hardwareMap.lightSensor.get("right");
        left.enableLed(true);
        middle.enableLed(true);
        right.enableLed(true);
    }



    public boolean lineIsFound()
    {
        if(left.getLightDetected() > .5 )
            return true;
        else if(middle.getLightDetected() > .5)
            return  true;
        else if(right.getLightDetected() > .5)
            return true;
        else
            return false;
    }

    public boolean lineIsFoundInMiddle()
    {
        if(middle.getLightDetected() > .5)
            return true;
        else
            return false;
    }


}
