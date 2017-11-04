package org.firstinspires.ftc.teamcode.subsystems;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware9330;
import java.util.HashMap;

public class ColorDistance9330 {

    private Hardware9330 hwMap = null;
    public ColorDistance9330(Hardware9330 robotMap) { hwMap = robotMap; }

    public HashMap getInfo() {
        HashMap hm = new HashMap();
        hm.put("Distance (cm)",  (hwMap.ds.getDistance(DistanceUnit.CM)));
        hm.put("Alpha", new Integer(hwMap.platformCS.alpha()));
        hm.put("Red", new Integer(hwMap.platformCS.red()));
        hm.put("Green",new Integer( hwMap.platformCS.green()));
        hm.put("Blue", new Integer(hwMap.platformCS.blue()));
            return hm;
    }

}

