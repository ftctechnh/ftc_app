package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.locomotion.DriveInfo;
import org.firstinspires.ftc.teamcode.robot.locomotion.Meccanum;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Derek on 10/31/2017.
 *
 * Base Op mode that all other op modes extend.
 *
 * @see OpMode
 *
 *
 * todo: finish Javadoc
 *
 */

@TeleOp(name="RelicBaseOp", group="Base")
public class RelicBaseOp extends OpMode {

    private static Robot robot;
    private static final int DEBUG = 0;
    protected DriveInfo driveInfo;

    /*
            B = hardwareMap.dcMotor.get("frontLeft");
            C = hardwareMap.dcMotor.get("frontRight");
            A = hardwareMap.dcMotor.get("backLeft");
            D = hardwareMap.dcMotor.get("backRight");
     */
    @Override
    public void init() {
        robot = new Robot(hardwareMap,new Meccanum(Collections.unmodifiableMap(new HashMap<String,DcMotor>() {
            {
                put("A",hardwareMap.dcMotor.get("A"));
                put("B",hardwareMap.dcMotor.get("B"));
                put("C",hardwareMap.dcMotor.get("C"));
                put("D",hardwareMap.dcMotor.get("D"));
            }
        })));
    }

    @Override
    public void loop() {
        robot.update(driveInfo);
    }
}
