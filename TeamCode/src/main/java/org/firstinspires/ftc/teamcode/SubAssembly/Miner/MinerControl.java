package org.firstinspires.ftc.teamcode.SubAssembly.Miner;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

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
    }

    public void Extend() {
        LinearMinerM.setPower(LINEAR_SPEED);
    }

    public void Retract() {
        LinearMinerM.setPower(-LINEAR_SPEED);
    }

    public void MinerStop() {
        LinearMinerM.setPower(0);
    }

    public void Intake() {
        IntakeM.setPower(INTAKE_SPEED);
    }

    public void Stoptake()  {
        IntakeM.setPower(0);
    }

    public void Untake() {
        IntakeM.setPower(-INTAKE_SPEED);
    }

    public void Dump() {
        DeployerS.setPosition(0);
    }

    public void Undump() {
        DeployerS.setPosition(1);
    }

    public void IntakeRaise() {
        MinerLeftS.setPosition(0);
        MinerRightS.setPosition(0);
    }

    public void IntakeLower() {
        MinerLeftS.setPosition(1);
        MinerRightS.setPosition(1);
    }
}