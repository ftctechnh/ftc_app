package org.overlake.ftc.team_7330.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.Autonomous;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.IFunc;

/**
 * This simple OpMode illustrates how to drive autonomously a certain distance using encoders.
 *
 * The OpMode works with both legacy and modern motor controllers. It expects two motors,
 * named "motorLeft" and "motorRight".
 */
@Autonomous(name="Auto Drive (Sync)", group="Swerve Examples")
public class SyncAutoDriveEncoders extends SynchronousOpMode
{
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    // The number of encoder ticks per motor shaft revolution. 1440 is correct
    // for HiTechnic motors. Andy Mark motors are 1120 ticks per revolution.
    // http://www.cougarrobot.com/index.php?option=com_content&view=article&id=331%3Aandymark-neverest-motor-notes&catid=92%3Aftc-hardware&Itemid=140
    final int encRotation = 1440;

    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;

    //----------------------------------------------------------------------------------------------
    // Main loop
    //----------------------------------------------------------------------------------------------

    @Override protected void main() throws InterruptedException
    {
        this.composeDashboard();

        this.motorFrontRight = this.hardwareMap.dcMotor.get("motorFrontRight");
        this.motorFrontLeft = this.hardwareMap.dcMotor.get("motorFrontLeft");
        this.motorBackRight = this.hardwareMap.dcMotor.get("motorBackRight");
        this.motorBackLeft = this.hardwareMap.dcMotor.get("motorBackLeft");


        this.motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        // Reset the encoders to zero.
        //
        // Note: we can do this, or not do this. The rest of the code is based only on *increments*
        // to the positions, so we could live with whatever the encoders happen to presently read
        // just fine. That said, it's a little easier to interpret telemetry if we start them off
        // at zero, so we do that. But try commenting these lines out, and observe that the code
        // continues to work just fine, even as you run the OpMode multiple times.
        this.motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        // Drive forward a while. The parameters here are arbitrary; they're just for illustration
        driveWithEncoders(4.7, 1.0);
    }

    /** Drive (forward) the indicated number of motor shaft revolutions using the indicated power */
    void driveWithEncoders(double revolutions, double power) throws InterruptedException
    {
        // How far are we to move, in ticks instead of revolutions?
        int denc = (int)Math.round(revolutions * encRotation);

        // Tell the motors where we are going
        this.motorFrontLeft.setTargetPosition(this.motorFrontLeft.getCurrentPosition() + denc);
        this.motorFrontRight.setTargetPosition(this.motorFrontRight.getCurrentPosition() + denc);
        this.motorBackLeft.setTargetPosition(this.motorBackLeft.getCurrentPosition() + denc);
        this.motorBackRight.setTargetPosition(this.motorBackRight.getCurrentPosition() + denc);


        // Give them the power level we want them to move at
        this.motorFrontLeft.setPower(power);
        this.motorFrontRight.setPower(power);
        this.motorBackLeft.setPower(power);
        this.motorBackRight.setPower(power);

        // Set them a-going
        this.motorFrontLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorFrontRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorBackLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorBackRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        // Wait until they are done
        while (this.motorFrontLeft.isBusy() || this.motorFrontRight.isBusy()||this.motorBackRight.isBusy()|| this.motorBackLeft.isBusy())
            {
            telemetry.update();
            this.idle();
            }

        // Now that we've arrived, kill the motors so they don't just sit there buzzing
        this.motorFrontLeft.setPower(0);
        this.motorFrontRight.setPower(0);
        this.motorBackLeft.setPower(0);
        this.motorBackRight.setPower(0);

        // Always leave the screen looking pretty
        telemetry.updateNow();
    }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    void composeDashboard()
        {
        telemetry.addLine(
                telemetry.item("FL: ", new IFunc<Object>() { public Object value() { return motorFrontLeft.getCurrentPosition(); }}),
                telemetry.item("FL target: ",   new IFunc<Object>() { public Object value() { return motorFrontLeft.getTargetPosition(); }}),
                telemetry.item("FL mode: ",     new IFunc<Object>() { public Object value() { return format(motorFrontLeft.getMode()); }}),
                telemetry.item("FL power: ",     new IFunc<Object>() { public Object value() { return motorFrontLeft.getPowerFloat(); }})
            );

        telemetry.addLine(
                telemetry.item("FR: ", new IFunc<Object>() { public Object value() { return motorFrontRight.getCurrentPosition(); }}),
                telemetry.item("FR target: ",    new IFunc<Object>() { public Object value() { return motorFrontRight.getTargetPosition(); }}),
                telemetry.item("FR mode: ",      new IFunc<Object>() { public Object value() { return format(motorFrontRight.getMode()); }}),
                telemetry.item("FR power: ",     new IFunc<Object>() { public Object value() { return motorFrontRight.getPowerFloat(); }})
            );
        telemetry.addLine(
                telemetry.item("BL: ", new IFunc<Object>() { public Object value() { return motorBackLeft.getCurrentPosition(); }}),
                telemetry.item("BL target: ",    new IFunc<Object>() { public Object value() { return motorBackLeft.getTargetPosition(); }}),
                telemetry.item("BL mode: ",      new IFunc<Object>() { public Object value() { return format(motorBackLeft.getMode()); }}),
                telemetry.item("BL power: ",     new IFunc<Object>() { public Object value() { return motorBackLeft.getPowerFloat(); }})
        );
        telemetry.addLine(
                telemetry.item("BR: ", new IFunc<Object>() { public Object value() { return motorBackRight.getCurrentPosition(); }}),
                telemetry.item("BR target: ",    new IFunc<Object>() { public Object value() { return motorBackRight.getTargetPosition(); }}),
                telemetry.item("BR mode: ",      new IFunc<Object>() { public Object value() { return format(motorBackRight.getMode()); }}),
                telemetry.item("BR power: ",     new IFunc<Object>() { public Object value() { return motorBackRight.getPowerFloat(); }})
        );
        }

    String format(DcMotorController.RunMode mode)
        {
        switch (mode)
            {
            default:
            case RUN_WITHOUT_ENCODERS: return "run";
            case RESET_ENCODERS:     return "reset";
            case RUN_TO_POSITION:    return "runToPos";
            case RUN_USING_ENCODERS: return "runEnc";
            }
        }
}
