/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class HardwareECRguy {
    /* Public OpMode members. */
    public DcMotor leftFront = null;
    public DcMotor rightFront = null;
    public DcMotor leftBack = null;
    public DcMotor rightBack = null;
    // public DcMotor  leftArm     = null;
    public Servo Dump    = null;
//    public Servo    rightClaw   = null;
    public DcMotor lift = null;
  //  public ColorSensor color;

    public static final double MID_SERVO       =  0.5 ;
    public static final double START_SERVO      = 1.0;
    public static final double LIFT_POWER = 0.5;
    public static final double REVERSE_LIFT_POWER = -1 * LIFT_POWER;


    //public static final double ARM_UP_POWER    =  0.45 ;
    // public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap = null;
    //private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareECRguy() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftFront = hwMap.get(DcMotor.class, "left_front");
        rightFront = hwMap.get(DcMotor.class, "right_front");
        //color = hwMap.colorSensor.get("color_sensor");
        leftBack = hwMap.get(DcMotor.class, "left_back");
        rightBack = hwMap.get(DcMotor.class, "right_back");
        Dump = hwMap.get(Servo.class,"Dump");
        lift = hwMap.get(DcMotor.class, "lift");
        // leftArm    = hwMap.get(DcMotor.class, "left_arm");
        leftFront.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightFront.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        leftBack.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightBack.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors

        // Set all motors to zero power
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        lift.setPower(0);
        //Dump.setPower(0); (does not work)
        // leftArm.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

      Dump.setPosition(START_SERVO);
      //claw.setPosition();
      //Claw position not yet determined

    }
    //servo functions
    void DumpIt(ElapsedTime runtime){
        Dump.setPosition(0.5);
    }

    //FUNCTIONS: This first group uses time
    void StopMoving(double Stoptime, ElapsedTime Runtime) {
        double end = Runtime.seconds() + Stoptime;
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        lift.setPower(0);
        while (end > Runtime.seconds()) {
        }
    }
    void StopLift(double Stoptime, ElapsedTime Runtime){
        lift.setPower(0);
    }

    void Forward(double Speed, double Stoptime, ElapsedTime Runtime) {
        double end = Runtime.seconds() + Stoptime;
        leftFront.setPower(Speed);
        rightFront.setPower(Speed);
        leftBack.setPower(Speed);
        rightBack.setPower(Speed);
        while (end > Runtime.seconds()) {
        }
    }

    void Backward(double Speed, double Stoptime, ElapsedTime Runtime) {
        double end = Runtime.seconds() + Stoptime;
        leftFront.setPower(-Speed);
        rightFront.setPower(-Speed);
        leftBack.setPower(-Speed);
        rightBack.setPower(-Speed);
        while (end > Runtime.seconds()) {
        }
    }

    void TurnLeft(double Speed, double Stoptime, ElapsedTime Runtime) {
        double end = Runtime.seconds() + Stoptime;
        leftFront.setPower(-Speed);
        rightFront.setPower(Speed);
        leftBack.setPower(-Speed);
        rightBack.setPower(Speed);
        while (end > Runtime.seconds()) {
        }

    }

    void TurnRight(double Speed, double Stoptime, ElapsedTime Runtime) {
        double end = Runtime.seconds() + Stoptime;
        leftFront.setPower(Speed);
        rightFront.setPower(-Speed);
        leftBack.setPower(Speed);
        rightBack.setPower(-Speed);
        while (end > Runtime.seconds()) {
        }
    }

    //attempting to use Encoders for distance rather than time

    void Forward_for_Distance(double distance, double speed) {
        //change mode of motors
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //put this in the op_mode to find position
        //int position = leftFront.getCurrentPosition();
        //telemetry.addData("Encoder Position", position);

        double radius = 2.0; //radius of the wheels
        double circ = 2 * radius * 3.1415;
        double clicks = 1440;   //one rotation is how many clicks of encoder?
        int rotations = (int) Math.round(distance/circ * clicks);

        leftFront.setTargetPosition(rotations);
        rightFront.setTargetPosition(rotations);
        leftBack.setTargetPosition(rotations);
        rightBack.setTargetPosition(rotations);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(speed);
        rightFront.setPower(speed);
        leftBack.setPower(speed);
        rightBack.setPower(speed);
        while (leftFront.isBusy()) {
            //empty on purpos

        }
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

    }

    void Backward_for_Distance(double distance, double speed) {
        this.Forward_for_Distance(-distance, -speed);
    }

    void Left_for_Distance(double degrees, double speed){
        //change mode of motors
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //put this in the op_mode to find position
        //int position = leftFront.getCurrentPosition();
        //telemetry.addData("Encoder Position", position);

        double clicks = 1440;
        double singledegree = 1440/360;
        double rot_force = 3.4;
        int rotations = (int) Math.round(degrees * singledegree*rot_force);

        leftFront.setTargetPosition(-rotations);
        rightFront.setTargetPosition(rotations);
        leftBack.setTargetPosition(-rotations);
        rightBack.setTargetPosition(rotations);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(-speed);
        rightFront.setPower(speed);
        leftBack.setPower(-speed);
        rightBack.setPower(speed);
        while (leftFront.isBusy()) {
            //empty on purpose
        }
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    void Right_for_Distance(double degrees, double speed){
        this.Left_for_Distance(-degrees, -speed);
    }

    void Lift_Down(double Speed, double Stoptime, ElapsedTime Runtime)

    {
        double end = Runtime.seconds() + Stoptime;

        lift.setPower(-Speed);
        while (end > Runtime.seconds()) {
        }
    }
    void Lift_Up(double Speed, double Stoptime, ElapsedTime Runtime)

    {
        double end = Runtime.seconds() + Stoptime;

        lift.setPower(Speed);
        while (end > Runtime.seconds()) {
        }
    }
 }

