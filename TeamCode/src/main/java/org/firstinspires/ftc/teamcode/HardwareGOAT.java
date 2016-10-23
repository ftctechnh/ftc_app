package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a K9 robot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Servo channel:  Servo to raise/lower arm: "arm"
 * Servo channel:  Servo to open/close claw: "claw"
 *
 * Note: the configuration of the servos is such that:
 *   As the arm servo approaches 0, the arm position moves up (away from the floor).
 *   As the claw servo approaches 0, the claw opens up (drops the game element).
 */
public class HardwareGOAT
{
    double driverShifterPosition;  //variable for position of driver shifter servo
    public double passShifterPosition;    //variable for position of passenger shifter servo
    double TailRotateSpeed;        //variable for speed of servos that pivot tail
    double DrivePower;             //variable for power level max of drive motors
    //double  Arm_dsPosition;         //variable for position of driver side arm flapper
    //double  Arm_psPosition;         //variable for position of passenger side arm flapper
    double  SweeperSpeed;           //variable for speed of servo for sweeper
    double  DumpPosition;           //variable for position of box dump servo
    double  dBrakePosition;         //variable for position of the driver and passenger front brakes
    double  pBrakePosition;
    double  ArmShifterPosition;
    double  ClimberArmPosition;
    double  KickerSpeed;

    float tail;     //variable for tail wheel speed
    float Arm;      //variable for driver and passenger arms extension speed
    float ArmPivot; //variable for speed of rotation pivot for arms

    DcMotor motorDriver1;   //Driver Side Motor 1
    DcMotor motorDriver2;   //Driver Side Motor 2
    DcMotor motorPass1;     //Passenger Side Motor 1
    DcMotor motorPass2;     //Passenger Side Motor 2
    DcMotor motorTail;      //Tail Drive Motor
    DcMotor motorArmPivot;  //Arms lead screw Pivot Motor
    DcMotor motorArm1;      //Arm 1 Extension Motor
    DcMotor motorArm2;      //Arm 2 Extension Motor
    Servo DriverShifter;//driver gearbox shifter    1
    Servo PassShifter;  //passenger gearbox shifter 2
    Servo TailRotate1;  //tail pivot servo 1        3
    Servo TailRotate2;  //tail pivot servo 2        4
    //Servo arm_ds;       //driver side flapper       5
    //Servo arm_ps;       //passenger side flapper    6
    Servo Sweeper;      //sweeper servo             7
    Servo Dump;         //box dump/rotate servo     8
    Servo dBrake;       //driver side brake         9
    Servo pBrake;       //passenger side brake      10
    Servo ArmShifter;   //arm gearbox shifter       11
    Servo ClimberArm;   //climber arm               12
    Servo Kicker;

    /* Local OpMode members. */
    HardwareMap hwMap  = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareGOAT() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // save reference to HW Map
        hwMap = ahwMap;

        //map motors here (names must be the same in the config file on the phone)
        motorDriver1 = hwMap.dcMotor.get("dm1");          //map motorDriver1 to dm1 on motor controller 1 port 1
        motorDriver2 = hwMap.dcMotor.get("dm2");          //map motorDriver2 to dm2 on motor controller 1 port 2
        motorDriver1.setDirection(DcMotor.Direction.REVERSE);   //sets direction of driver motors to reverse
        motorDriver2.setDirection(DcMotor.Direction.REVERSE);   //sets direction of driver motors to reverse
        motorPass1 = hwMap.dcMotor.get("pm1");            //map motorPass1 to pm1 on motor controller 2 port 1
        motorPass2 = hwMap.dcMotor.get("pm2");            //map motorPass2 to pm2 on motor controller 2 port 2
        motorTail = hwMap.dcMotor.get("tail");            //map motorTail to tail on motor controller 3 port 1
        motorArmPivot = hwMap.dcMotor.get("armp");        //map motorArmPivot to armp on motor controller 3 port 2
        motorArm1 = hwMap.dcMotor.get("arm1");            //map motorArm1 to arm1 on motor controller 4 port 1
        motorArm2 = hwMap.dcMotor.get("arm2");            //map motorArm2 to arm2 on motor controller 4 port 2

        // assign the starting position of the motors
        //all of the motors are set to 0 power level here so that they don't move before the match starts
        motorDriver1.setPower(0);
        motorDriver2.setPower(0);
        motorPass1.setPower(0);
        motorPass2.setPower(0);
        motorTail.setPower(0);
        motorArmPivot.setPower(0);
        motorArm1.setPower(0);
        motorArm2.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        motorDriver1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorDriver2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorPass1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorPass2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorTail.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorArmPivot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorArm1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorArm2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //map servos here (names must be the same in the config file on the phone)
        DriverShifter = hwMap.servo.get("dShift");        //map DriverShfiter to dShift on servo controller _ port _
        PassShifter = hwMap.servo.get("pShift");          //map PassengerShifter to pShift on servo controller _ port _
        TailRotate1 = hwMap.servo.get("tRotate1");        //map TailRotate1 to tRotate1 on servo controller _ port _
        TailRotate2 = hwMap.servo.get("tRotate2");        //map TailRotate2 to tRotate2 on servo controller _ port _
        //arm_ds = hwMap.servo.get("arm_ds");               //map arm_ds to arm_ds on servo controller _ port _
        //arm_ps = hwMap.servo.get("arm_ps");               //map arm_ps to arm_ps on servo controller _ port _
        Sweeper = hwMap.servo.get("sweeper");             //map Sweeper to sweeper on servo controller _ port _
        Dump = hwMap.servo.get("dump");                   //map Dump to dump on servo controller _ port _
        dBrake = hwMap.servo.get("dBrake");               //map dBrake to dBrake on servo controller _ port _
        pBrake = hwMap.servo.get("pBrake");               //map pBrake to pBrake on servo controller _ port _
        ArmShifter = hwMap.servo.get("aShift");           //map ArmShifter to aShift on servo controller _ port _
        ClimberArm = hwMap.servo.get("C_Arm");         //map ClimberArm to C_Arm on servo controller _ port _
        Kicker = hwMap.servo.get("Kicker");

        //assign the starting position of the servos and variables
        //all the servos variables are set to their start position here so that they are ready for the match to start
        driverShifterPosition = 0.6;    //the driver shifter is set to .6 so that it starts in high gear
        passShifterPosition = 0.6;      //the passenger shifter is set to .6 so that it starts in high gear
        TailRotateSpeed = .5;           //the tail rotate speed is set to .5 (neural) so that it will not go up or down
        DrivePower = 1;                 //drive power is set to 1 so that we start off the match at full speed
        ArmPivot = 0;                   //the speed of the arm pivot is set to 0 to keep it from moving at the beginning of the match
        Arm = 0;                        //the speed of the arms extension is set to 0 at the beginning of the match
        tail = 0;                       //the speed of the tail driver power variable is set to 0
        //Arm_dsPosition = .45;           //sets driver side flapper to be in  at the start of the match
        //Arm_psPosition = .55;           //sets passenger side flapper to be in at the start of the match
        SweeperSpeed = .5;              //sets the sweeper speed to be neutral at the start of the match
        DumpPosition = .5;              //sets the dump servo to the center position to keep the bucket vertical
        dBrakePosition = .92;             //sets the brakes/guards to be down to keep debris out from under the treads
        pBrakePosition = .92;
        ClimberArmPosition = 1;
        KickerSpeed = .5;

    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}
