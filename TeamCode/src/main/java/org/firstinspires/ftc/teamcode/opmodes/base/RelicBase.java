package org.firstinspires.ftc.teamcode.opmodes.base;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.general.Updatable;
import org.firstinspires.ftc.teamcode.robot.peripherals.Peripheral;
import org.firstinspires.ftc.teamcode.robot.peripherals.SingleServoGripper;

import java.util.LinkedList;
import java.util.List;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

/**
 * Created by Derek on 2/6/2018.
 */


public class RelicBase extends OpMode {
    List<Peripheral> peripherals = new LinkedList<>();
    List<Updatable> updatables = new LinkedList<>();

    SingleServoGripper gripper;
    DcMotor A,B,C,D,arm;


    @Override
    public void init() {

        B = hardwareMap.dcMotor.get("frontLeft");
        C = hardwareMap.dcMotor.get("frontRight");
        A = hardwareMap.dcMotor.get("backLeft");
        D = hardwareMap.dcMotor.get("backRight");
        arm = hardwareMap.dcMotor.get("arm");
        Servo gripperServo = hardwareMap.servo.get("claw");

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        C.setDirection(REVERSE);
        D.setDirection(REVERSE);

        gripper = new SingleServoGripper("Main Arm Gripper",gripperServo);
        gripper.getClampPositions().OPEN.setPosition(1);
        gripper.getClampPositions().CLOSED.setPosition(0);
        gripper.getClampPositions().CENTER.setPosition(0.5);

        peripherals.add(gripper);

        for (Peripheral peripheral : peripherals) {
            telemetry.addLine(new StringBuilder(peripheral.getName())
                    .append(" tested ")
                    .append(peripheral.test())
                    .toString()
            );
        }

    }

    public void update() {
        for(Updatable updatable : updatables) {
            updatable.update();
        }
    }

    @Override
    public void loop() {
        update();
    }

    //private methods
    private void addPeripheral(Peripheral peripheral) {
        peripherals.add(peripheral);
        addUpdatable(peripheral);
    }

    private void addUpdatable(Updatable updatable) {
        updatables.add(updatable);
    }
}
