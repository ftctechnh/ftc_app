    import com.qualcomm.robotcore.eventloop.opmode.OpMode;
    import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;

    /**
     * Created by jeppe on 16-10-2017.
     */
    @TeleOp(name = "Jeppe test")
    class RelicRecoveryTeleOp extends OpMode {

        DcMotor rightMotor;
        DcMotor leftMotor;
        DcMotor liftMotor;
        DcMotor armMotor;

        double rightPower;
        double leftPower;
        double liftPower;
        double armPower;


        @Override
        public void init() {
            hardwareInit();
        }

        @Override
        public void loop() {
            rightPower = -gamepad1.right_stick_y;
            leftPower = -gamepad1.right_stick_y;
            armPower = -gamepad2.left_stick_y;
            liftPower = +gamepad1.right_trigger - gamepad1.left_trigger;
            rightMotor.setPower(rightPower);
            leftMotor.setPower(leftPower);
            liftMotor.setPower(liftPower);
            armMotor.setPower(armPower);
        }

        private void hardwareInit() {
            rightMotor = hardwareMap.dcMotor.get("rm");
            rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            leftMotor = hardwareMap.dcMotor.get("lm");
            liftMotor = hardwareMap.dcMotor.get("lift");
            liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            armMotor = hardwareMap.dcMotor.get("arm");
        }
    }
