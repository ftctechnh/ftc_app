package org.firstinspires.ftc.teamcode.VelocityVortex;

/**
 * Created by spmce on 11/20/2016.
 */
//@TeleOp (name = "changeI2cAddress", group = "TeleOp")
public class changeI2cAddress extends VelocityVortexHardware {

    @Override
    public void init() {
        super.init();
        //color1.getI2cAddress();
        //color2.getI2cAddress();
    }
    public void start() {
        color1.getI2cAddress().toString();
        color2.getI2cAddress().toString();
        color1.toString();
        color2.toString();


        color1.getConnectionInfo();
        color2.getConnectionInfo();
    }
}
