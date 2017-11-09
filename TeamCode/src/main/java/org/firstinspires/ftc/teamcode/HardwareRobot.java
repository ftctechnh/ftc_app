package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareRobot {

    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;
    final static double COUNTS_PER_MOTOR_REV = 105;
    final static double DRIVE_GEAR_REDUCTION = 2.91;
    final static double WHEEL_DIAMETER_INCHES = 4.0;

    public final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    public HardwareRobot()
    {

    }


    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {

        hwMap = ahwMap;

        // Define and Initialize Motors
        leftDrive = hwMap.get(DcMotor.class, "motorLeft");
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive = hwMap.get(DcMotor.class, "motorRight");


        // Define and Initialize Motors
        //leftDrive = hwMap.get(DcMotor.class, "motorLeft");
        //rightDrive = hwMap.get(DcMotor.class, "motorRight"); 
     


        leftDrive.setPower(0);
        rightDrive.setPower(0);

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encoderToPosition() {
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
