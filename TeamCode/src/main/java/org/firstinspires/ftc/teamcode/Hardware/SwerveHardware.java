package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;

public class SwerveHardware extends QuadWheelHardware {

    String[] directions = {"left_front", "right_front", "left_back", "right_back"};
    public CRServo frontLeftServo;
    public CRServo backLeftServo;
    public CRServo frontRightServo;
    public CRServo backRightServo;
    public CRServo[] servoArr = {frontLeftServo, frontRightServo, backLeftServo, backRightServo};

    public AnalogInput frontLeftEncoder;
    public AnalogInput backLeftEncoder;
    public AnalogInput frontRightEncoder;
    public AnalogInput backRightEncoder;
    public AnalogInput[] encoderArr = {frontLeftEncoder, frontRightEncoder, backLeftEncoder, backRightEncoder};

    public double maxVoltages[] = {3.262, 3.266, 3.268, 3.263};
    public double zeroVoltages[] = {2.656, 2.882, 1.456, 3.258};


    public SwerveHardware(LinearOpMode opMode) {super(opMode);}

    public void init() {
        for (int i = 0; i < directions.length; i++) {
            servoArr[i] = hwMap.crservo.get(directions[i] + "_servo");
            encoderArr[i] = hwMap.analogInput.get(directions[i] + "_encoder");
        }
    }
}
