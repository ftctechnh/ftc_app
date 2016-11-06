package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.Servo;
import static org.firstinspires.ftc.teamcode.ComponentsInit.ComponentMap.*;

public class ServoControl {

    public ServoControl(Servo...servos){
        this.servos=servos;
    }
    public void open(){
        servos[S_TOP_LEFT].setPosition(0.5);
        servos[S_TOP_RIGHT].setPosition(0.5);
    }

    public void close(){
        servos[S_TOP_LEFT].setPosition(0.0);
        servos[S_TOP_RIGHT].setPosition(1.0);
    }
    Servo[] servos;
}
