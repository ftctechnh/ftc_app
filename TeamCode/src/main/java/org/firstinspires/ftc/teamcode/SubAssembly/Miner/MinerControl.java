package org.firstinspires.ftc.teamcode.SubAssembly.Miner;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.Utilities.EnumWrapper;
import org.firstinspires.ftc.teamcode.Utilities.ServoControl;

import java.util.EnumMap;

/* Sub Assembly Class
 */
public class MinerControl {
    /* Constants */
    final double LINEAR_SPEED = 1.0;
    final double INTAKE_SPEED = 1.0;

    /* Declare private class object */
    private LinearOpMode opmode = null;     /* local copy of opmode class */

    private DcMotor LinearMinerM;
    private DcMotor IntakeM;
    private Servo MinerLeftS;
    private Servo MinerRightS;
    private Servo DeployerS;

    private EnumMap<Setpoints, Double> MapDepServo;

    public ServoControl<Setpoints, EnumMap<Setpoints, Double>> DeployerServo;

    public enum Setpoints implements EnumWrapper<Setpoints> {
        Undump, Middump, Dump;
    }

    /* Declare public class object */
    public TouchSensor MinerButtonI;
    public TouchSensor MinerButtonO;

    /* Subassembly constructor */
    public MinerControl() {
    }

    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;

        opMode.telemetry.addLine("Lift Control" + " initialize");
        opMode.telemetry.update();

        /* Set local copies from opmode class */
        opmode = opMode;
        hwMap = opMode.hardwareMap;

        MapDepServo = new EnumMap<Setpoints, Double>(Setpoints.class);


        /* Assign setpoint values */
        MapDepServo.put(Setpoints.Undump, 0.15);
        MapDepServo.put(Setpoints.Middump, 0.65);
        MapDepServo.put(Setpoints.Dump, 1.0);

        /* Map hardware devices */
        LinearMinerM = hwMap.dcMotor.get("LinearMinerM");
        IntakeM = hwMap.dcMotor.get("IntakeM");
        MinerLeftS = hwMap.servo.get("MinerLeftS");
        MinerRightS = hwMap.servo.get("MinerRightS");
        MinerButtonI = hwMap.touchSensor.get("MinerButtonI");
        MinerButtonO = hwMap.touchSensor.get("MinerButtonO");
        DeployerS = hwMap.servo.get("DeployerS");

        LinearMinerM.setPower(0);
        IntakeM.setPower(0);
        DeployerS.setPosition(1);

        DeployerServo = new ServoControl(DeployerS, MapDepServo, Setpoints.Undump, true);
    }

    public void Extend() {
        LinearMinerM.setPower(-LINEAR_SPEED);
    }

    public void Retract() {
        LinearMinerM.setPower(LINEAR_SPEED);
    }

    public void MinerStop() {
        LinearMinerM.setPower(0);
    }

    public void Intake() {
        IntakeM.setPower(INTAKE_SPEED);
    }

    public void Stoptake() {
        IntakeM.setPower(0);
    }

    public void Untake() {
        IntakeM.setPower(-INTAKE_SPEED);
    }

    /*public void Dump() {
        DeployerS.setPosition(1);
    }*/

    /*public void Middump() {
        DeployerS.setPosition(0.6);
    }*/

    /*public void Undump() {
        DeployerS.setPosition(0.12);
    }*/

    public void IntakeRaise() {
        MinerLeftS.setPosition(0.5);
        MinerRightS.setPosition(0.5);
    }

    public void IntakeLower() {
        MinerLeftS.setPosition(1);
        MinerRightS.setPosition(0);
    }

    public void deployUp() {
        DeployerServo.nextSetpoint(true);
    }

    public void deployDown() {
        DeployerServo.prevSetpoint(true);
    }
}