package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Dumper {
    LinearOpMode opMode;
    private Servo dumpServo;
    private final double RETRACT_POSITION = 0.6;
    private final double  DUMP_POSITION = 0.1;
    private Logger l = new Logger("DUMPER");


    public Dumper(LinearOpMode opMode) {
        this.opMode = opMode;
        this.dumpServo = opMode.hardwareMap.servo.get("dumpServo");
        this.dumpServo.setDirection(Servo.Direction.FORWARD);
        l.log("initialized dumper");

    }

    public void setDumpServo(double position) {
        this.dumpServo.setPosition(position);
        l.logData("setting dumper position",position);

    }

    public void dump(){
        setDumpServo(DUMP_POSITION);
        l.log("setting dumping position");
        l.lineBreak();

    }
    public void retract(){
        setDumpServo(RETRACT_POSITION);
        l.log("setting retract position position");
        l.lineBreak();
    }

}


