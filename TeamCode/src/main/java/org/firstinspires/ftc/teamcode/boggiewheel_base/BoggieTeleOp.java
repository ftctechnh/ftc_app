package org.firstinspires.ftc.teamcode.boggiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.boggiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.AbstractTeleop;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

@TeleOp(name="boggiewheel_teleop", group="New")
//@Disabled

public class BoggieTeleOp extends AbstractTeleop {

    private Robot robot;

    private SlewDcMotor intakeMotor;

    @Override
    public void Init() {
        robot = new Robot();
        intakeMotor = new SlewDcMotor(hardwareMap.dcMotor.get("intake"));
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void Loop() {
        robot.updateDrive();
        telemetry.addData("Loop");
        telemetry.update();
    }

    @Override
    public void lsy_change(double lsy) {
        robot.setDriveY(lsy);
        //robot.setPower(lsy,gamepad1.right_stick_y);
        telemetry.addData("Y");
    }

    @Override
    public void rsx_change(double rsx) {
        robot.setDriveZ(rsx);
        telemetry.addData("X");
    }

    @Override
    public void a_down(){
        intakeMotor.setPower(1);
    }

    @Override
    public void x_down(){
        intakeMotor.setPower(-1);
    }

    @Override
    public void b_down(){
        intakeMotor.setPower(0);
    }

    /*@Override
    public void rsy_change(double rsy){
        robot.setPower(gamepad1.left_stick_y,rsy);
    }
*/

    @Override
    public void Stop(){
        robot.stop();
    }
}
