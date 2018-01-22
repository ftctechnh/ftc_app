package org.firstinspires.ftc.team11248.CompSci_Education;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.team11248.Hardware.HolonomicDriver_11248;

/**
 * Created by Tony_Air on 12/18/17.
 */

public class BoiBot extends HolonomicDriver_11248{

    Servo left, right;

    double lu = 0;
    double ld = 0;
    double ru = 0;
    double rd = 0;

    boolean rightUp = true;
    boolean leftUp = true;

    public BoiBot(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap.dcMotor.get("FrontLeft"),
                hardwareMap.dcMotor.get("FrontRight"),
                hardwareMap.dcMotor.get("BackLeft"),
                hardwareMap.dcMotor.get("BackRight"),
                telemetry);

        left = hardwareMap.servo.get("servo1");
        right = hardwareMap.servo.get("servo2");

    }

    public void init() {
        left.setPosition(lu);
        right.setPosition(ru);
    }

    public void toggle_left(){
      left.setPosition( leftUp?ld:lu);
      leftUp = !leftUp;
    }

    public void toggle_right(){
        right.setPosition( rightUp?rd:ru);
        rightUp = !rightUp;
    }
}
