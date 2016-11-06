package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import static org.firstinspires.ftc.teamcode.ComponentsInit.ComponentMap.*;

public abstract class ComponentsInit extends OpMode {
        DcMotor mFL;
        DcMotor mFR;
        DcMotor mRL;
        DcMotor mRR;
        Servo sTL;
        Servo sTR;
        DcMotor[] motors={mFL,mFR,mRL,mRR};
        Servo[] servos={sTL,sTR};

        public static final class ComponentMap{
            final static int M_FRONT_LEFT=0;
            final static int M_FRONT_RIGHT=1;
            final static int M_REAR_LEFT=2;
            final static int M_REAR_RIGHT=3;
            final static int S_TOP_LEFT=0;
            final static int S_TOP_RIGHT=1;
        }
        float driveX(){
            return -gamepad1.left_stick_x;
        }
        float driveLY(){
            return -gamepad1.left_stick_y;
        }
        float driveR(){return gamepad1.right_stick_x;}
        float driveRY(){return gamepad1.right_stick_y;}
        void motorInit() {
            motors[M_FRONT_LEFT] = hardwareMap.dcMotor.get("fl");
            motors[M_FRONT_RIGHT] = hardwareMap.dcMotor.get("fr");
            motors[M_REAR_LEFT] = hardwareMap.dcMotor.get("rl");
            motors[M_REAR_RIGHT] = hardwareMap.dcMotor.get("rr");

            motors[M_FRONT_LEFT].setDirection(DcMotorSimple.Direction.REVERSE);
            motors[M_REAR_LEFT].setDirection(DcMotorSimple.Direction.REVERSE);
        }
        void servoInit(){
            servos[S_TOP_LEFT]=hardwareMap.servo.get("sl");
            servos[S_TOP_RIGHT]=hardwareMap.servo.get("sr");
        }
        Servo[] getServos(){
            return servos;
        }
        DcMotor[] getMotors(){
            return motors;
        }

}
