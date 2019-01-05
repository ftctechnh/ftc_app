/* Copyright (c) 2017 FIRST. All rights reserved.javascript:void(0);
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

//import android.support.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a POV Game style Teleop for a PushBot
 * In this mode the left stick moves the robot FWD and back, the Right stick turns left and right.
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="ForgetUEncoders", group="Linear OpMode")
//@Disabled
public class ForgetUEncoders extends LinearOpMode {

    /* Declare OpMode members. */
//    HardwarePushbot robot           = new HardwarePushbot();   // Use a Pushbot's hardware
//                                                               // could also use HardwarePushbotMatrix class.
//    double          clawOffset      = 0;                       // Servo mid position
//    final double    CLAW_SPEED      = 0.02 ;                   // sets rate to move servo

    //@above default servo provided in sdk

    private boolean prodbot = false;
    //Last year we made two robots, a base model and the final result.
    // I think this boolean checked if we were using the base and then switched controls to just the four wheels.
    // May have to refer to this later.


    private DcMotor getNewMotor(String motorName) { //these could be made generic using type notation
        try {
            return (hardwareMap.get(DcMotor.class, motorName));
        } catch (Exception e) {
            telemetry.addLine("MOTOR: " + motorName + "   offline");
            telemetry.update();
            return (null);
        }
    }
    private CRServo getNewServo(String Name) { //these could be made generic using type notation
        try {
            return (hardwareMap.get(CRServo.class, Name));
        } catch (Exception e) {
            telemetry.addLine("SERVO: " + Name +"   offline");
            telemetry.update();
            return (null);
        }
    }



    //Speed Factors for Fast/Slow Mode
    private double speedFactor = 1.0; //default full speed
    private ElapsedTime runtime = new ElapsedTime();
    private double currentLoopTime = 0.0;
    private double lastLoopTime = 0.0;
    int encode = 0;
    int encode2 = 0;
    //Hanging Mechanism
    private DcMotor elevator = null;

    //Arm Motors
    private DcMotor ma = null;
    private DcMotor rla = null;
    private DcMotor lla = null;

    //Driving Motors
    private DcMotor lf = null; //"lf" stands for left front and so on.
    private DcMotor rf = null;
    private DcMotor lb = null;
    private DcMotor rb = null;

    //Servos
    private CRServo spinner = null;
    private CRServo wrist = null;

    //other variables
    private static boolean JOYSTICK_SCALING = true;
    private static boolean tele = false; //show telemetry

    // We may use a color sensor for the item knockoff in Autonomous


    //Our software coach from last year helped us with this method that uses trigonometry to operate mecanum wheels
    private void mecanumMove(double lsx, double lsy, double rsx) {

        if (JOYSTICK_SCALING) {                 //I think Joystick_scaling is a boolean on whether the joystick is being moved
            lsy = Math.pow(lsy, 1.0);

            lsx = Math.pow(lsx, 1.0);
            rsx = Math.pow(rsx, 1.0);           //"ls" is left stick which was used for robo Mecanum movement
            //"rs" is right stick used for robot rotation
        }

        double r = Math.sqrt(lsy * lsy + lsx * lsx);            //This double uses the pythagoren theorem to find
        // out the distance from the the joystick center
        double robotAngle = Math.atan2(-1 * lsy, lsx) - Math.PI / 4;
        double rightX = rsx;
        final double v1 = r * Math.cos(robotAngle) + rightX;    //Multiplies the scaling of the joystick to give different speeds based on joystick movement
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        if (lf != null)
            lf.setPower(v1);
        if (rf != null)
            rf.setPower(v2);
        if (lb != null)
            lb.setPower(v3);
        if (rb != null)
            rb.setPower(v4);
    }
    private void probing(double rotatingArm, double bendingArm,int encode,int encode2){
        encode+=rotatingArm*3;
        if (lla != null) {
            lla.setTargetPosition(encode);
            lla.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            lla.setPower(0.5);
            while (lla.isBusy()){
                
            }
            lla.setPower(0.3);
        }
        if (rla != null){
            rla.setTargetPosition(encode);
            rla.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rla.setPower(0.5);
            while (rla.isBusy()){

            }
            rla.setPower(0);
        }
//            rla.setPower(rotatingArm);
//            if (JOYSTICK_SCALING) {                 //I think Joystick_scaling is a boolean on whether the joystick is being moved
//                rotatingArm =           //"ls" is left stick which was used for robo Mecanum movement
//                //"rs" is right stick used for robot rotation
//            }
        // I was wondering what would happen if the above was nonexistant
        encode2+=bendingArm*3;
        if (ma != null) {
            ma.setTargetPosition(encode2);
            ma.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            ma.setPower(0.5);
            while (ma.isBusy()){

            }
            ma.setPower(0);
        }
//        if (ma != null)
//            ma.setPower(bendingArm);




    }

    private void elevation(double p){
        if (JOYSTICK_SCALING) {                 //I think Joystick_scaling is a boolean on whether the joystick is being moved
            p = Math.pow(p, 1.0);

        }
        final double r = p;

        if (elevator != null){
            elevator.setPower(r);
        }
    }
    @Override
    public void runOpMode() throws InterruptedException {

        if (prodbot) {
            telemetry.addData("Rover", "prodbot");
        } else {
            telemetry.addData("Rover", "Hal9000");
        }
//        double left;
//        double right;
//        double drive;
//        double turn;
//        double max;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        //robot.init(hardwareMap);

        //Above line is commented out because Hardware map is used for accessories such as attachment sensors/servos/motors

        // Send telemetry message to signify robot waiting;
        //telemetry.addData("Say", "Hello Driver");

        //initialize required driving motors
        lf = getNewMotor("lf");
        rf = getNewMotor("rf");
        lb = getNewMotor("lb");
        rb = getNewMotor("rb");

        //initialize Arm
        ma = getNewMotor("ma");
        lla = getNewMotor("lla");
        rla = getNewMotor("rla");

        spinner = getNewServo("spinner");
        wrist = getNewServo("wrist");
//         if (spinner != null)
//             spinner.setPosition(0.0);
        //initialize Lifting Mechanism
        elevator = getNewMotor("elevator");



        telemetry.addData("Status", "Initialized");                 //Telemetry is the messages displayed on phone
        telemetry.update();

        if (lf != null)
            lf.setDirection(DcMotor.Direction.REVERSE); //is REVERSE
        if (rf != null)
            rf.setDirection(DcMotor.Direction.FORWARD); //is REVERSE
        if (lb != null)
            lb.setDirection(DcMotor.Direction.REVERSE); //is FORWARD
        if (rb != null)
            rb.setDirection(DcMotor.Direction.FORWARD); //is FORWARD


        if (ma != null) {
            ma.setDirection(DcMotor.Direction.FORWARD); //is FORWARD
            ma.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            ma.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if (lla != null) {
            lla.setDirection(DcMotor.Direction.FORWARD); //is FORWARD
            lla.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lla.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if (rla != null) {
            rla.setDirection(DcMotor.Direction.REVERSE); //is FORWARD
            rla.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rla.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if (elevator != null)
            elevator.setDirection(DcMotor.Direction.FORWARD); //is FORWARD





        if (spinner != null)
            spinner.setDirection(CRServo.Direction.FORWARD); //is FORWARD
        if (wrist != null)
            wrist.setDirection(CRServo.Direction.FORWARD); //is FORWARD

        // Wait for the game to start (driver presses PLAY)


//                 _____________
//                /             \
//               /               \
//              /                 \
//             |                   |
//             |       GO!!!       |
//             |                   |
//              \                 /
//               \               /
//                ---------------


        waitForStart();
        runtime.reset();    //resets runtime()

        currentLoopTime = runtime.time();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            lastLoopTime = currentLoopTime;
            currentLoopTime = runtime.time();

            //gamepad1
            double forwardBack = gamepad1.left_stick_y;
            double leftRight = gamepad1.left_stick_x;
            double Rotate = gamepad1.right_stick_x;
            boolean slowMode = (gamepad1.left_bumper);
//            boolean lowerBallArm = (gamepad1.dpad_down);
//            boolean raiseBallArm = (gamepad1.dpad_up);
            double Elevating = gamepad1.right_stick_y;

            //gamepad2
            double liftla = gamepad2.left_stick_x * 0.5;
            double middleArm = gamepad2.left_stick_y*0.5;
            // boolean extendAB = gamepad2.dpad_right;
            // boolean retractAB = gamepad2.dpad_left;
            boolean slowArmMode = gamepad2.left_bumper;
//             boolean spinningDirectionToggle = gamepad2.right_bumper;
//             boolean spinningDirectionToggle2 = gamepad2.left_bumper;
            double spinning = gamepad2.right_trigger;
            double reverseSpinning = gamepad2.left_trigger;
            double wristTurn = gamepad1.right_trigger;
            double reverseWristTurn = gamepad1.left_trigger;

//            boolean PoleDescend = gamepad1.y;
//            boolean PoleAscend = gamepad1.a;

//            double raiseLowerLift = gamepad2.left_stick_y;
//            double leftClamp = gamepad2.left_trigger;
//            double rightClamp = gamepad2.right_trigger;
//            boolean leftRelease = gamepad2.left_bumper;
//            boolean rightRelease = gamepad2.right_bumper;
//            boolean extendRelicArm = gamepad2.dpad_right;
//            boolean retractRelicArm = gamepad2.dpad_left;
//            boolean raiseRelicArm = gamepad2.dpad_up;
//            boolean lowerRelicArm = gamepad2.dpad_down;
//            boolean raiseRelic = gamepad2.y;//gamepad2.right_stick_y>0.2;
//            boolean lowerRelic = gamepad2.a;//gamepad2.right_stick_y<-0.2;

            // spinner = Range.clip(spinner, -10,10);
            if (slowMode) {
                speedFactor = 0.5;
            } else {
                speedFactor = 1.0;
            }

            double lsy = forwardBack * speedFactor;
            double lsx = leftRight * speedFactor;
            double rsx = Rotate;
            if (speedFactor < 1.0) {
                rsx *= 0.5;
            }
            if (slowArmMode) {
                liftla *= 0.5;
                middleArm *= 0.5;
            }
//             if (spinningDirectionToggle){
//                 spinning = -10;
//             }   else if (spinningDirectionToggle2) {
//                 spinning = 10;

//             } else {
//                 spinning = spinning;
//             }
            double letsSpin = 0.5;
            if (spinning>0 && reverseSpinning<0.2){
                letsSpin = spinning;
                spinner.setPower(letsSpin);
            } else if (spinning<0.2 && reverseSpinning>0){
                letsSpin = -reverseSpinning;
                spinner.setPower(letsSpin);

            } else {
                letsSpin = 0;
                spinner.setPower(0);
            }
            double wristTurnPower = 0.5;
            if (wristTurn>0 && reverseWristTurn<0.2){
                wristTurnPower = wristTurn;
                wrist.setPower(wristTurnPower);
            } else if (wristTurn<0.2 && reverseWristTurn>0){
                wristTurnPower = -reverseWristTurn;
                wrist.setPower(wristTurnPower);

            } else {
                wristTurnPower = 0;
                wrist.setPower(0);
            }
            telemetry.addLine(letsSpin +"");
            updateTelemetry(telemetry);

            // spinner.setPower(letsSpin);
            mecanumMove(lsx, lsy, rsx);
            probing(liftla,middleArm,encode,encode2);
            elevation(Elevating);



            // Use gamepad buttons to move arm up (Y) and down (A)
            if (tele) telemetry.addData("Status", "Run Time: " + runtime.toString());
            if (tele) telemetry.update();

            // Pace this loop so jaw action is reasonable speed.
            sleep(50);
        }
    }
}