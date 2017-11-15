/**
 * Created by spmce on 2/9/2016.
 */
package org.firstinspires.ftc.teamcode.ResQ;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides a single hardware access point between custom op-modes and the OpMode class for this Robot.
 * @author Shane McEnaney
 * @version 2016
 */
public class RobotHardware extends OpMode {

    protected double leftDrive,rightDrive,backLeftDrive,backRightDrive;

    protected boolean warningGenerated = false;
    protected String warningMessage;

    protected DcMotor left, right, backLeft, backRight;
    protected Servo hook, spinner;

    protected ArrayList<DcMotor> motor = new ArrayList<DcMotor>();
    protected ArrayList<Servo> servo = new ArrayList<Servo>();
    
    protected DcMotor[] motors = new DcMotor[8];
    protected Servo[] servos = new Servo[12];
    
    public RobotHardware() {
        mapDriveTrain();
        mapServos();
    }

    @Override
    public void init() {
        telemetry.addData("01", "Hi");
    }

    /*public void init_loop() {
        init();
        telemetry.addData("02","Hello");
    }*/

    @Override
    public void loop() {

    }

    boolean getWarningGenerated () {return warningGenerated;}

    String getWarningMessage () {return warningMessage;}

    void setWarningMessage (String opModeExceptionMessage) {
        if (warningGenerated)
            warningMessage += ", ";
        warningGenerated = true;
        warningMessage += opModeExceptionMessage;
    }

    private void mapDevice (HardwareDevice device) {
        try {
            device = hardwareMap.dcMotor.get(String.valueOf(device));
        } catch (Exception opModeException) {
            setWarningMessage(String.valueOf(device));
            //DbgLog.msg(opModeException.getLocalizedMessage());
        }
    }

    private void reverseDirection(DcMotor motor) {
        if (motor != null)
            motor.setDirection(DcMotor.Direction.REVERSE);
    }

    private void mapDriveTrain() {
        mapDevice(left);
        reverseDirection(right);
        mapDevice(right);
        mapDevice(backLeft);
        reverseDirection(backRight);
        mapDevice(backRight);
    }

    private void mapServos() {
        mapDevice(hook);
        mapDevice(spinner);
    }
    
    String getPower(DcMotor motor) {
        if (motor != null)
            return String.valueOf(motor.getPower());
        return null;
    }
    void setPower(DcMotor motor, double power) {
        if (motor != null)
            motor.setPower(power);
    }
    
    double getPosition(Servo servo) {
        if (servo != null)
            return servo.getPosition();
        return 0.5;
    }
    void setPosition(Servo servo, double position) {
        if (servo != null)
            servo.setPosition(position);
    }
    
    void setDriveTrain(double power) {
        if (left != null)
            left.setPower(power);
        if (right != null)
            right.setPower(power);
        if (backLeft != null)
            backLeft.setPower(power);
        if (backRight != null)
            backRight.setPower(power);
    }
    void setDriveTrain(double left ,double right) {
        if (this.left != null)
            this.left.setPower(left);
        if (this.right != null)
            this.right.setPower(right);
        if (backLeft != null)
            backLeft.setPower(left);
        if (backRight != null)
            backRight.setPower(right);
    }
    void setDriveTrain(double left ,double right, double backLeft, double backRight) {
        if (this.left != null)
            this.left.setPower(left);
        if (this.right != null)
            this.right.setPower(right);
        if (this.backLeft != null)
            this.backLeft.setPower(backLeft);
        if (this.backRight != null)
            this.backRight.setPower(backRight);
    }
}
