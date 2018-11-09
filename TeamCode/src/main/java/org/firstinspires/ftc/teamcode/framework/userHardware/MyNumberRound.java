package org.firstinspires.ftc.teamcode.framework.userHardware;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Caitlyn on 9/30/2018.
 */

public class MyNumberRound {

    double value;
    int places;

    //The initializer
    public MyNumberRound(){
      value = 0.0;
      places = 1;
    }

    public double roundDouble(double value, int places) {
        //if (places < 0) throw new IllegalAccessException("places is wrong");
        if (places < 0) {
            places = 0;  // quick and dirty way to handle negative numbers
        }
        BigDecimal bd = new BigDecimal(Double.toString(value));
        // why is there a bd = ?
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
