package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.AbstractAuton;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

@Autonomous(name="BoogieWheel Auton", group="New")
//@Disabled

public class BoogieAuton extends AbstractAuton {

    Robot robot;
    double speed = 1, error = 3;
    int period = 100;

    private SlewDcMotor intakeMotor;

    @Override
    public void Init() {
        robot = new Robot();
        intakeMotor = new SlewDcMotor(hardwareMap.dcMotor.get("intake"));
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void Run() {
        //depotSideRightMineral();
        craterSideRightMineral();
    }

    public void craterSideRightMineral(){
        robot.turnTo(-25,speed,error,period);

        intakeMotor.setPower(1);

        robot.driveTo(24,speed);

        intakeMotor.setPower(0);

        robot.driveTo(-8,speed);

        robot.turnTo(90,speed,error,period);

        robot.driveTo(54,speed);

        robot.turnTo(135,speed,error,period);

        robot.driveTo(45,speed);

        robot.driveTo(-65,speed);

        robot.driveTo(-10,0.7);
    }

    public void depotSideRightMineral(){
        robot.turnTo(-25,speed,error,period);

        robot.driveTo(32,speed);

        robot.turnTo(25,speed,error,period);

        robot.driveTo(34,speed);

        robot.turnTo(45,speed,error,period);

        robot.driveTo(-68,speed);

        robot.driveTo(-10,0.5);
    }

    public void aroundField(){
        robot.driveTo(65,speed);

        robot.turnTo(-45,speed,error,period);

        robot.driveTo(78,speed);

        robot.turnTo(-90,speed,error,period);

        robot.driveTo(62,speed);
    }

    @Override
    public void Stop() {
        robot.stop();
    }
}
