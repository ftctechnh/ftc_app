package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Naman on 8/12/2016.
 */

public class BasicTeleOp extends TeleOpHelper{

    public BasicTeleOp() {}


    @Override
    public void loop() {

        driveControl("normal");
        basicTel();

    }
}
