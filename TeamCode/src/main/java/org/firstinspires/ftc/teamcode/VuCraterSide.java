    package org.firstinspires.ftc.teamcode;

    import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
    import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
    import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;
    import com.qualcomm.robotcore.hardware.GyroSensor;
    import com.qualcomm.robotcore.hardware.Servo;
    import com.qualcomm.robotcore.util.ElapsedTime;

    import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
    import org.firstinspires.ftc.teamcode.vision.MasterVision;
    import org.firstinspires.ftc.teamcode.vision.SampleRandomizedPositions;

    @Autonomous (name="VuCraterSide", group="Competition Autonomous")
    public class VuCraterSide extends LinearOpMode{
        MasterVision vision;
        SampleRandomizedPositions goldPosition;
        DcMotor liftMotor;
        DcMotor frontLeft;
        DcMotor rearLeft;
        DcMotor frontRight;
        DcMotor rearRight;
        Servo armServo;
        GyroSensor sensorGyro;
        ModernRoboticsI2cGyro mrGyro;

        private ElapsedTime runtime = new ElapsedTime();


        @Override
        public void runOpMode() throws InterruptedException {

            //declare motors
            liftMotor = hardwareMap.dcMotor.get("liftMotor");
            frontLeft = hardwareMap.dcMotor.get("frontLeft");
            rearLeft = hardwareMap.dcMotor.get("rearLeft");
            frontRight = hardwareMap.dcMotor.get("frontRight");
            rearRight = hardwareMap.dcMotor.get("rearRight");
            sensorGyro = hardwareMap.gyroSensor.get("gyro");
            armServo = hardwareMap.servo.get ("armServo");

            //declare motor directions
            frontLeft.setDirection(DcMotor.Direction.REVERSE);
            rearLeft.setDirection(DcMotor.Direction.REVERSE);
            frontRight.setDirection(DcMotor.Direction.FORWARD);
            rearRight.setDirection(DcMotor.Direction.FORWARD);
            liftMotor.setDirection(DcMotor.Direction.REVERSE);
            armServo.setDirection(Servo.Direction.FORWARD);


            mrGyro = (ModernRoboticsI2cGyro) sensorGyro;


            double turnSpeed = 0.2;
            int zAccumulated;
            int target = 0;

            telemetry.addData("Mode", "waiting");
            telemetry.update();


            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;// recommended camera direction
            parameters.vuforiaLicenseKey = "AbVGrK7/////AAABmV5qNYRo8EpalbdT9iVSnmNR6wynVnTYxdfuU0jrIQJY3/bNzMRAOB9ew/OVmuVwRluGP3sUUHaNIgpXOii6OX5JQHTGyOeDMkVtqPdvynUdw7hRhLL2a8L8nQzJdH4jrKTCB6hAykKflqR4dykoml54fOnuTuXzGgwydwHCkcwt3UnDy/kCMrmSSx/9hBW21N4m6vhqzM9cdhUAGvvQAJPEE7WjrfT14Z4onzZXM185HCLKIEXcaJx10MaGO/xHchVtbvMGB2zDzFJ57uG2+AJopJtI+Qh1anzqoPnolZMUwJHRBhQnxis+QGpoL1RiJ6HqTRQr5mAEuP3q4wX5I1WXydNah5JoLgekylpWKANr\n";

            vision = new MasterVision(parameters, hardwareMap, false, MasterVision.TFLiteAlgorithm.INFER_LEFT);
            vision.init();// enables the camera overlay. this will take a couple of seconds
            vision.enable();// enables the tracking algorithms. this might also take a little time

            waitForStart();

            vision.disable();// disables tracking algorithms. this will free up your phone's processing power for other jobs.

            goldPosition = vision.getTfLite().getLastKnownSampleOrder();

            while(opModeIsActive()){
                telemetry.addData("goldPosition was", goldPosition);// giving feedback










                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~LEFT CODE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                //~~                                                                                        ~~

                switch (goldPosition) { // using for things in the autonomous program
                    case LEFT: {
                        telemetry.addLine("going to the left");

                        //-------------------Place Marker Start--------------------------------------------------
                        armServo.setPosition(0.0);
                        sleep(400); //wait


                        //-------------------Block End--------------------------------------------------

                        //-----------------------------Lift Up(lower robot) Start-------------------------------------------
                        operateLift(1, 1, 5);
                        //Wait 1 second
                        sleep(500);
//-----------------------------Lift Up(lower robot) End---------------------------------------------


                        //----------------------Strafe off Lander Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-800);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(800);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(800);
                        frontLeft.setPower(0.6);
                        frontRight.setTargetPosition(-800);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Straight off Lander Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(600);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(600);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(600);
                        frontLeft.setPower(0.5);
                        frontRight.setTargetPosition(600);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Get in Position for Mineral Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(2000);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(-2000);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(-2000);
                        frontLeft.setPower(0.5);
                        frontRight.setTargetPosition(2000);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Straight to Knock Mineral Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(1800);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(1800);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(1800);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(1800);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Reverse-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-800);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(-800);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(-800);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(-800);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
                        //-------------------Block End--------------------------------------------------

                        //----------------------turn to Depot Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-1440);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(1440);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(-1440);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(1440);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------


                        //----------------------To Wall-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(1440);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(1440);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(1440);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(1440);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------turn to Depot Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-580);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(580);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(-580);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(580);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------


                        //----------------------Strafe into wall-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-2600);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(2600);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(2600);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(-2600);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------


                        //----------------------To Crater Block Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(4000);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(4000);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(4000);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(4000);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //-------------------Place Marker Start--------------------------------------------------
                        armServo.setPosition(0.7);
                        sleep(500); //wait



    //-------------------Block End--------------------------------------------------

    //----------------------To Crater Block Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-6000);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(-6000);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(-6000);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(-6000);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //-----------------------------Lift Down Start------------------------------------------------------
                        operateLift(0, 1, 5);
                        //Wait 1 second
                        sleep(500);
//-----------------------------Lift Down End--------------------------------------------------------

                        //----------------------WAIT-------------------------------------
                        sleep(25000);
                        //----------------------------------------------------------------

                    }
                    break;























                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~CENTER CODE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    //~~                                                                                          ~~
                    case CENTER: {
                        telemetry.addLine("going straight");
                        telemetry.addData("Mode", "running");
                        telemetry.update();

                        vision.shutdown();


                        //-------------------Place Marker Start--------------------------------------------------
                        armServo.setPosition(0.0);
                        sleep(400); //wait

                        //-----------------------------Lift Up(lower robot) Start-------------------------------------------
                        operateLift(1, 1, 5);
                        //Wait 1 second
                        sleep(500);
//-----------------------------Lift Up(lower robot) End---------------------------------------------


                        //-------------------Block End--------------------------------------------------

                        //----------------------Strafe off Lander Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-800);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(800);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(800);
                        frontLeft.setPower(0.6);
                        frontRight.setTargetPosition(-800);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Straight off Lander Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(1000);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(1000);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(1000);
                        frontLeft.setPower(0.5);
                        frontRight.setTargetPosition(1000);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Strafe Into Mineral Position-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(800);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(-800);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(-800);
                        frontLeft.setPower(0.5);
                        frontRight.setTargetPosition(800);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Straight Into Mineral-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(1100);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(1100);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(1100);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(1100);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Reverse-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-800);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(-800);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(-800);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(-800);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------turn 90 degrees-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-1360);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(1360);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(-1360);
                        frontLeft.setPower(0.5);
                        frontRight.setTargetPosition(1360);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Forward To Wall-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(2800);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(2800);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(2800);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(2800);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Turn 45 degrees-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-600);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(600);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(-600);
                        frontLeft.setPower(0.5);
                        frontRight.setTargetPosition(600);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Strafe To Wall-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-1800);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(1800);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(1800);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(-1800);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Follow Wall-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(4400);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(4400);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(4400);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(4400);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
                        //-------------------Block End--------------------------------------------------

                        //-------------------Place Marker Start--------------------------------------------------
                        armServo.setPosition(0.7);
                        sleep(400); //wait


                        //-------------------Block End--------------------------------------------------

                        //----------------------Follow Wall-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-6400);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(-6400);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(-6400);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(-6400);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
                        //-------------------Block End--------------------------------------------------

                        //-----------------------------Lift Down Start------------------------------------------------------
                        operateLift(0, 1, 5);
                        //Wait 1 second
                        sleep(500);
//-----------------------------Lift Down End--------------------------------------------------------

                        //----------------------WAIT-------------------------------------
                        sleep(25000);
                        //----------------------------------------------------------------


                    }
                    break;















                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~RIGHT CODE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    //~~                                                                                          ~~

                    case RIGHT: {
                        telemetry.addLine("going to the right");

                        vision.shutdown();


                        //-------------------Place Marker Start--------------------------------------------------
                        armServo.setPosition(0.0);
                        sleep(400); //wait


                        //-----------------------------Lift Up(lower robot) Start-------------------------------------------
                        operateLift(1, 1, 5);
                        //Wait 1 second
                        sleep(500);
//-----------------------------Lift Up(lower robot) End---------------------------------------------


                        //-------------------Block End--------------------------------------------------

                        //----------------------Strafe off Lander Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-1200);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(1200);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(1200);
                        frontLeft.setPower(0.6);
                        frontRight.setTargetPosition(-1200);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Straight off Lander Start-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(800);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(800);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(800);
                        frontLeft.setPower(0.5);
                        frontRight.setTargetPosition(800);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Strafe To Mineral-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-800);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(800);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(800);
                        frontLeft.setPower(0.5);
                        frontRight.setTargetPosition(-800);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Straight To Mineral-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(1100);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(1100);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(1100);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(1100);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Reverse-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-1000);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(-1000);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(-1000);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(-1000);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------turn 90 degrees-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-1360);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(1360);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(-1360);
                        frontLeft.setPower(0.5);
                        frontRight.setTargetPosition(1360);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Forward To Wall-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(5100);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(5100);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(5100);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(5100);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Turn 45 degrees-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-600);
                        rearLeft.setPower(0.5);
                        rearRight.setTargetPosition(600);
                        rearRight.setPower(0.5);
                        frontLeft.setTargetPosition(-600);
                        frontLeft.setPower(0.5);
                        frontRight.setTargetPosition(600);
                        frontRight.setPower(0.5);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Strafe To Wall-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-800);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(800);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(800);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(-800);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
    //-------------------Block End--------------------------------------------------

                        //----------------------Follow Wall-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(4400);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(4400);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(4400);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(4400);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
                        //-------------------Block End--------------------------------------------------

                        //-------------------Place Marker Start--------------------------------------------------
                        armServo.setPosition(0.7);
                        sleep(400); //wait


                        //-------------------Block End--------------------------------------------------

                        //----------------------Follow Wall-------------------------------------
                        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        rearLeft.setTargetPosition(-6400);
                        rearLeft.setPower(1);
                        rearRight.setTargetPosition(-6400);
                        rearRight.setPower(1);
                        frontLeft.setTargetPosition(-6400);
                        frontLeft.setPower(1);
                        frontRight.setTargetPosition(-6400);
                        frontRight.setPower(1);
                        while (rearLeft.isBusy() && opModeIsActive()) {
                        }
                        while (rearRight.isBusy() && opModeIsActive()) {
                        }
                        while (frontLeft.isBusy() && opModeIsActive()) {
                        }
                        while (frontRight.isBusy() && opModeIsActive()) {
                        }
                        rearLeft.setPower(0);
                        rearRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
                        //-------------------Block End--------------------------------------------------
//-----------------------------Lift Down Start------------------------------------------------------
                        operateLift(0, 1, 5);
                        //Wait 1 second
                        sleep(500);
//-----------------------------Lift Down End--------------------------------------------------------
                        //----------------------WAIT-------------------------------------
                        sleep(25000);
                        //----------------------------------------------------------------
                }
                        break;
































                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~UNKNOWN CODE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    //~~                                                                                          ~~

                    case UNKNOWN: {
                        telemetry.addLine("staying put");

                            vision.shutdown();


                            //-------------------Place Marker Start--------------------------------------------------
                            armServo.setPosition(0.0);
                            sleep(400); //wait

                            //-----------------------------Lift Up(lower robot) Start-------------------------------------------
                            operateLift(1, 1, 5);
                            //Wait 1 second
                            sleep(500);
//-----------------------------Lift Up(lower robot) End---------------------------------------------


                            //-------------------Block End--------------------------------------------------

                            //----------------------Strafe off Lander Start-------------------------------------
                            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearLeft.setTargetPosition(-800);
                            rearLeft.setPower(0.5);
                            rearRight.setTargetPosition(800);
                            rearRight.setPower(0.5);
                            frontLeft.setTargetPosition(800);
                            frontLeft.setPower(0.6);
                            frontRight.setTargetPosition(-800);
                            frontRight.setPower(0.5);
                            while (rearLeft.isBusy() && opModeIsActive()) {
                            }
                            while (rearRight.isBusy() && opModeIsActive()) {
                            }
                            while (frontLeft.isBusy() && opModeIsActive()) {
                            }
                            while (frontRight.isBusy() && opModeIsActive()) {
                            }
                            rearLeft.setPower(0);
                            rearRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                            //-------------------Block End--------------------------------------------------

                            //----------------------Straight off Lander Start-------------------------------------
                            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearLeft.setTargetPosition(1000);
                            rearLeft.setPower(0.5);
                            rearRight.setTargetPosition(1000);
                            rearRight.setPower(0.5);
                            frontLeft.setTargetPosition(1000);
                            frontLeft.setPower(0.5);
                            frontRight.setTargetPosition(1000);
                            frontRight.setPower(0.5);
                            while (rearLeft.isBusy() && opModeIsActive()) {
                            }
                            while (rearRight.isBusy() && opModeIsActive()) {
                            }
                            while (frontLeft.isBusy() && opModeIsActive()) {
                            }
                            while (frontRight.isBusy() && opModeIsActive()) {
                            }
                            rearLeft.setPower(0);
                            rearRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                            //-------------------Block End--------------------------------------------------

                            //----------------------Strafe Into Mineral Position-------------------------------------
                            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearLeft.setTargetPosition(800);
                            rearLeft.setPower(0.5);
                            rearRight.setTargetPosition(-800);
                            rearRight.setPower(0.5);
                            frontLeft.setTargetPosition(-800);
                            frontLeft.setPower(0.5);
                            frontRight.setTargetPosition(800);
                            frontRight.setPower(0.5);
                            while (rearLeft.isBusy() && opModeIsActive()) {
                            }
                            while (rearRight.isBusy() && opModeIsActive()) {
                            }
                            while (frontLeft.isBusy() && opModeIsActive()) {
                            }
                            while (frontRight.isBusy() && opModeIsActive()) {
                            }
                            rearLeft.setPower(0);
                            rearRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                            //-------------------Block End--------------------------------------------------

                            //----------------------Straight Into Mineral-------------------------------------
                            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearLeft.setTargetPosition(1100);
                            rearLeft.setPower(1);
                            rearRight.setTargetPosition(1100);
                            rearRight.setPower(1);
                            frontLeft.setTargetPosition(1100);
                            frontLeft.setPower(1);
                            frontRight.setTargetPosition(1100);
                            frontRight.setPower(1);
                            while (rearLeft.isBusy() && opModeIsActive()) {
                            }
                            while (rearRight.isBusy() && opModeIsActive()) {
                            }
                            while (frontLeft.isBusy() && opModeIsActive()) {
                            }
                            while (frontRight.isBusy() && opModeIsActive()) {
                            }
                            rearLeft.setPower(0);
                            rearRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                            //-------------------Block End--------------------------------------------------

                            //----------------------Reverse-------------------------------------
                            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearLeft.setTargetPosition(-800);
                            rearLeft.setPower(1);
                            rearRight.setTargetPosition(-800);
                            rearRight.setPower(1);
                            frontLeft.setTargetPosition(-800);
                            frontLeft.setPower(1);
                            frontRight.setTargetPosition(-800);
                            frontRight.setPower(1);
                            while (rearLeft.isBusy() && opModeIsActive()) {
                            }
                            while (rearRight.isBusy() && opModeIsActive()) {
                            }
                            while (frontLeft.isBusy() && opModeIsActive()) {
                            }
                            while (frontRight.isBusy() && opModeIsActive()) {
                            }
                            rearLeft.setPower(0);
                            rearRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                            //-------------------Block End--------------------------------------------------

                            //----------------------turn 90 degrees-------------------------------------
                            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearLeft.setTargetPosition(-1360);
                            rearLeft.setPower(0.5);
                            rearRight.setTargetPosition(1360);
                            rearRight.setPower(0.5);
                            frontLeft.setTargetPosition(-1360);
                            frontLeft.setPower(0.5);
                            frontRight.setTargetPosition(1360);
                            frontRight.setPower(0.5);
                            while (rearLeft.isBusy() && opModeIsActive()) {
                            }
                            while (rearRight.isBusy() && opModeIsActive()) {
                            }
                            while (frontLeft.isBusy() && opModeIsActive()) {
                            }
                            while (frontRight.isBusy() && opModeIsActive()) {
                            }
                            rearLeft.setPower(0);
                            rearRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                            //-------------------Block End--------------------------------------------------

                            //----------------------Forward To Wall-------------------------------------
                            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearLeft.setTargetPosition(2800);
                            rearLeft.setPower(1);
                            rearRight.setTargetPosition(2800);
                            rearRight.setPower(1);
                            frontLeft.setTargetPosition(2800);
                            frontLeft.setPower(1);
                            frontRight.setTargetPosition(2800);
                            frontRight.setPower(1);
                            while (rearLeft.isBusy() && opModeIsActive()) {
                            }
                            while (rearRight.isBusy() && opModeIsActive()) {
                            }
                            while (frontLeft.isBusy() && opModeIsActive()) {
                            }
                            while (frontRight.isBusy() && opModeIsActive()) {
                            }
                            rearLeft.setPower(0);
                            rearRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                            //-------------------Block End--------------------------------------------------

                            //----------------------Turn 45 degrees-------------------------------------
                            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearLeft.setTargetPosition(-600);
                            rearLeft.setPower(0.5);
                            rearRight.setTargetPosition(600);
                            rearRight.setPower(0.5);
                            frontLeft.setTargetPosition(-600);
                            frontLeft.setPower(0.5);
                            frontRight.setTargetPosition(600);
                            frontRight.setPower(0.5);
                            while (rearLeft.isBusy() && opModeIsActive()) {
                            }
                            while (rearRight.isBusy() && opModeIsActive()) {
                            }
                            while (frontLeft.isBusy() && opModeIsActive()) {
                            }
                            while (frontRight.isBusy() && opModeIsActive()) {
                            }
                            rearLeft.setPower(0);
                            rearRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                            //-------------------Block End--------------------------------------------------

                            //----------------------Strafe To Wall-------------------------------------
                            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearLeft.setTargetPosition(-1800);
                            rearLeft.setPower(1);
                            rearRight.setTargetPosition(1800);
                            rearRight.setPower(1);
                            frontLeft.setTargetPosition(1800);
                            frontLeft.setPower(1);
                            frontRight.setTargetPosition(-1800);
                            frontRight.setPower(1);
                            while (rearLeft.isBusy() && opModeIsActive()) {
                            }
                            while (rearRight.isBusy() && opModeIsActive()) {
                            }
                            while (frontLeft.isBusy() && opModeIsActive()) {
                            }
                            while (frontRight.isBusy() && opModeIsActive()) {
                            }
                            rearLeft.setPower(0);
                            rearRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                            //-------------------Block End--------------------------------------------------

                            //----------------------Follow Wall-------------------------------------
                            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearLeft.setTargetPosition(4400);
                            rearLeft.setPower(1);
                            rearRight.setTargetPosition(4400);
                            rearRight.setPower(1);
                            frontLeft.setTargetPosition(4400);
                            frontLeft.setPower(1);
                            frontRight.setTargetPosition(4400);
                            frontRight.setPower(1);
                            while (rearLeft.isBusy() && opModeIsActive()) {
                            }
                            while (rearRight.isBusy() && opModeIsActive()) {
                            }
                            while (frontLeft.isBusy() && opModeIsActive()) {
                            }
                            while (frontRight.isBusy() && opModeIsActive()) {
                            }
                            rearLeft.setPower(0);
                            rearRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                            //-------------------Block End--------------------------------------------------

                            //-------------------Place Marker Start--------------------------------------------------
                            armServo.setPosition(0.7);
                            sleep(400); //wait


                            //-------------------Block End--------------------------------------------------

                            //----------------------Follow Wall-------------------------------------
                            rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            rearLeft.setTargetPosition(-6400);
                            rearLeft.setPower(1);
                            rearRight.setTargetPosition(-6400);
                            rearRight.setPower(1);
                            frontLeft.setTargetPosition(-6400);
                            frontLeft.setPower(1);
                            frontRight.setTargetPosition(-6400);
                            frontRight.setPower(1);
                            while (rearLeft.isBusy() && opModeIsActive()) {
                            }
                            while (rearRight.isBusy() && opModeIsActive()) {
                            }
                            while (frontLeft.isBusy() && opModeIsActive()) {
                            }
                            while (frontRight.isBusy() && opModeIsActive()) {
                            }
                            rearLeft.setPower(0);
                            rearRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                            //-------------------Block End--------------------------------------------------
//-----------------------------Lift Down Start------------------------------------------------------
                        operateLift(0, 1, 5);
                        //Wait 1 second
                        sleep(500);
//-----------------------------Lift Down End--------------------------------------------------------
                            //----------------------WAIT-------------------------------------
                            sleep(25000);
                            //----------------------------------------------------------------






                        }
                        break;

                }

                telemetry.update();
            }

            vision.shutdown();
        }

        public void turn(int target) throws InterruptedException {
            turnAbsolute(target + mrGyro.getIntegratedZValue());
        }


        public void turnAbsolute(int target) throws InterruptedException {
            int zAccumulated = mrGyro.getIntegratedZValue();
            double turnSpeed = .18;

            while (Math.abs(zAccumulated - target) > 3) {
                if (zAccumulated > target) {
                    rearLeft.setPower(turnSpeed);
                    frontLeft.setPower(turnSpeed);
                    rearRight.setPower(-turnSpeed);
                    frontRight.setPower(-turnSpeed);
                }
                if (zAccumulated < target) {
                    rearLeft.setPower(-turnSpeed);
                    frontLeft.setPower(-turnSpeed);
                    rearRight.setPower(turnSpeed);
                    frontRight.setPower(turnSpeed);
                }

                waitOneFullHardwareCycle();

                zAccumulated = mrGyro.getIntegratedZValue();
                telemetry.addData("1. accu", String.format("%03d", zAccumulated));
            }
            rearLeft.setPower(0);
            rearRight.setPower(0);
            frontLeft.setPower(0);
            frontRight.setPower(0);

            telemetry.addData("1. accu", String.format("%03d", zAccumulated));

            waitOneFullHardwareCycle();

        }


        public void operateLift(int position, double speed, int timeoutS){
            if (opModeIsActive()) {
                if (position == 0) {
                    //Change This Number to Determine the Upper Position of the Lift
                    liftMotor.setTargetPosition(liftMotor.getCurrentPosition() - 7600
                    );
                    liftMotor.setPower(-speed);
                } else {
                    //Change This Number to Determine the Lower Position of the Lift
                    liftMotor.setTargetPosition(liftMotor.getCurrentPosition() + 7600
                    );
                    liftMotor.setPower(-speed);
                }
                liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                runtime.reset();

                while (liftMotor.isBusy() && opModeIsActive() &&
                        (runtime.seconds() < timeoutS)) {
                    // Display it for the driver.
                    telemetry.addData("Lift", "Running to %7d : %7d", liftMotor.getCurrentPosition(), liftMotor.getTargetPosition());
    //                telemetry.addData("Path2",  "Running at %7d :%7d",
    //                                            robot.leftDrive.getCurrentPosition(),
    //                                            robot.rightDrive.getCurrentPosition());
                    telemetry.update();
                }
                liftMotor.setPower(0);
                liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                ///CameraDevice.getInstance().setFlashTorchMode(true) ; //turns flash on

                //CameraDevice.getInstance().setFlashTorchMode(false ); //turns flash off
            }
        }
    }