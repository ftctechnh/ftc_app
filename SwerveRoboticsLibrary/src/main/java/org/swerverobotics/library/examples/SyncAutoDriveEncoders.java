package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.*;

/**
 * This simple OpMode illustrates how to drive autonomously a certain distance using encoders.
 *
 * The OpMode works with both legacy and modern motor controllers. It expects two motors,
 * named "motorLeft" and "motorRight".
 */
@Autonomous(name="Auto Drive (Sync)", group="Swerve Examples")
// @Disabled
public class SyncAutoDriveEncoders extends SynchronousOpMode
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    // The number of encoder ticks per motor shaft revolution. 1440 is correct
    // for HiTechnic motors. Andy Mark motors are 1120 ticks per revolution.
    // http://www.cougarrobot.com/index.php?option=com_content&view=article&id=331%3Aandymark-neverest-motor-notes&catid=92%3Aftc-hardware&Itemid=140
    final int encRotation = 1440;

    DcMotor motorRight;
    DcMotor motorLeft;

    //----------------------------------------------------------------------------------------------
    // Main loop
    //----------------------------------------------------------------------------------------------

    @Override protected void main() throws InterruptedException
        {
        this.composeDashboard();

        this.motorLeft = this.hardwareMap.dcMotor.get("motorLeft");
        this.motorRight = this.hardwareMap.dcMotor.get("motorRight");

        this.motorLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        // Reset the encoders to zero.
        //
        // Note: we can do this, or not do this. The rest of the code is based only on *increments*
        // to the positions, so we could live with whatever the encoders happen to presently read
        // just fine. That said, it's a little easier to interpret telemetry if we start them off
        // at zero, so we do that. But try commenting these lines out, and observe that the code
        // continues to work just fine, even as you run the OpMode multiple times.
        this.motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        // Drive forward a while. The parameters here are arbitrary; they're just for illustration
        driveWithEncoders(4.7, 1.0);
        }

    /** Drive (forward) the indicated number of motor shaft revolutions using the indicated power */
    void driveWithEncoders(double revolutions, double power) throws InterruptedException
        {
        // How far are we to move, in ticks instead of revolutions?
        int denc = (int)Math.round(revolutions * encRotation);

        // Tell the motors where we are going
        this.motorLeft.setTargetPosition(this.motorLeft.getCurrentPosition() + denc);
        this.motorRight.setTargetPosition(this.motorRight.getCurrentPosition() + denc);

        // Give them the power level we want them to move at
        this.motorLeft.setPower(power);
        this.motorRight.setPower(power);

        // Set them a-going
        this.motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        // Wait until they are done
        while (this.motorLeft.isBusy() || this.motorRight.isBusy())
            {
            telemetry.update();
            this.idle();
            }

        // Now that we've arrived, kill the motors so they don't just sit there buzzing
        this.motorLeft.setPower(0);
        this.motorRight.setPower(0);

        // Always leave the screen looking pretty
        telemetry.updateNow();
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    void composeDashboard()
        {
        telemetry.addLine(
                telemetry.item("left: ", new IFunc<Object>() { public Object value() { return motorLeft.getCurrentPosition(); }}),
                telemetry.item("target: ",   new IFunc<Object>() { public Object value() { return motorLeft.getTargetPosition(); }}),
                telemetry.item("mode: ",     new IFunc<Object>() { public Object value() { return format(motorLeft.getMode()); }})
            );

        telemetry.addLine(
                telemetry.item("right: ", new IFunc<Object>() { public Object value() { return motorRight.getCurrentPosition(); }}),
                telemetry.item("target: ",    new IFunc<Object>() { public Object value() { return motorRight.getTargetPosition(); }}),
                telemetry.item("mode: ",      new IFunc<Object>() { public Object value() { return format(motorRight.getMode()); }})
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
