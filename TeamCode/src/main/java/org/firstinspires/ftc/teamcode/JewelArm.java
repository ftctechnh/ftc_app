package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 11/28/2017.
 */

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class JewelArm {
    public Servo upDownServo;
    public Servo endServo;
    ColorSensor cs;
    private final double DOWN_POSITION = 0;
    private final double UP_POSITION = 0.035;
    private final double RIGHT_POSITION = 0.15;
    private final double LEFT_POSITION = 0.85;
    private final double MIDDLE_POSITION = 0.5;
    private final double TRY_AGAIN_POSITION = 0.35;
    private final double MIN_COLOR_DETECTION_THRESHOLD = 5;
    private Telemetry telemetry;

    public JewelArm(Servo upDownServo, Servo endServo, ColorSensor cs, Telemetry telemetry) {
        this.upDownServo = upDownServo;
        this.endServo = endServo;
        this.cs = cs;
        this.telemetry = telemetry;
    }
    public JewelArm (HardwareMap hardwareMap, Telemetry telemetry) {
        this.endServo = hardwareMap.servo.get("s3");
        this.upDownServo = hardwareMap.servo.get("s4");
        this.cs = hardwareMap.colorSensor.get("cs1");
        this.telemetry = telemetry;
    }

    public void down() {
        setUpDownPosition(DOWN_POSITION);
    }

    public void up() {
        setUpDownPosition(UP_POSITION);
    }

    public void right() {setEndPosition(RIGHT_POSITION);}

    public void left() {setEndPosition(LEFT_POSITION);}

    public void init() {
        up();
        cs.enableLed(true);
    }
    public void findJewel(Color allianceColor) {
        down();
        ElapsedTime time = new ElapsedTime();
        time.start();
        boolean tryAgain = false;
        while(cs.red() < MIN_COLOR_DETECTION_THRESHOLD && cs.blue() < MIN_COLOR_DETECTION_THRESHOLD) {
            if(time.getElapsedTime() > 3000) {
                tryAgain = true;
                break;
            }
        }
        if (tryAgain) {
            setEndPosition(TRY_AGAIN_POSITION);
            time.start();
            while (cs.red() < MIN_COLOR_DETECTION_THRESHOLD && cs.blue() < MIN_COLOR_DETECTION_THRESHOLD) {
                if(time.getElapsedTime() > 3000) {
                    return;
                }
            }
        }
        int red = 0, blue = 0;
        for (int i=0; i<5; i++) {
            if (cs.red() > cs.blue()) {
                red++;
            }
            else if (cs.blue()>cs.red()) {
                blue++;
            }
        }
        if(allianceColor == Color.RED) {
          if (red > blue) {
            right();
          }
          else if (blue > red) {
            left();
          }
        }
        if(allianceColor == Color.BLUE) {
          if (blue > red) {
            right();
          }
          else if (red > blue) {
            left();
          }
        }
        Systems.sleep(750);
        setEndPosition(MIDDLE_POSITION);
        up();
    }

    public void setUpDownPosition(double postion) {
        upDownServo.setPosition(postion);
        upDownServo.setPosition(postion);
    }
    public void setEndPosition(double position) {
        endServo.setPosition(position);
        endServo.setPosition(position);
    }
    private void addLine(String message) {
        telemetry.addLine(message);
        telemetry.update();
    }
}
