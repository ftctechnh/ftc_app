package org.firstinspires.ftc.teamcode.subsystems;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware9330;
import java.util.HashMap;
import java.util.Locale;

public class ColorDistance9330 {

    private Hardware9330 hwMap = null;
    public ColorDistance9330(Hardware9330 robotMap) { hwMap = robotMap; }

    public HashMap getInfo() {
        HashMap hm = new HashMap();
        hm.put("Distance (cm)", new String(String.format(Locale.US, "%.02f", hwMap.ds.getDistance(DistanceUnit.CM))));
        hm.put("Alpha", new Integer(hwMap.cs.alpha()));
        hm.put("Red", new Integer(hwMap.cs.red()));
        hm.put("Green",new Integer( hwMap.cs.green()));
        hm.put("Blue", new Integer(hwMap.cs.blue()));
            return hm;
    }

}

