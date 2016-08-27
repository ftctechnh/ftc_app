package com.qualcomm.ftcrobotcontroller.opmodes.FreshClasses;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Naisan on 4/16/2016.
 */
public class FreshServos {
    Servo S_rackRight,
            S_rackLeft,
            S_bucket;

        public FreshServos(Servo S_rackRight, Servo S_rackLeft, Servo S_bucket) {
            this.S_rackRight = S_rackRight;
            this.S_rackLeft = S_rackLeft;
            this.S_bucket = S_bucket;
        }
}
