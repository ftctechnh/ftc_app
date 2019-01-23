package org.firstinspires.ftc.teamcode;



//import com.qualcomm.ftcrobotcontroller.CameraPreview;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.widget.FrameLayout;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.internal.CameraPreview;
import org.firstinspires.ftc.robotcore.external.Consumer;


/**
 *
 */
@TeleOp(name="CameraRover", group="MonsieurMallah")
public class CameraRover extends OpMode implements Consumer<CameraPreview> {

    // Elapsed time since the opmode started.


    // Motors connected to the hub.
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;
    private DcMotor extender;
    private DcMotor shoulder;

    // Camera Stuff
    public Camera camera;
    public CameraPreview preview;

    private Object imageLock = new Object();
    //private ElapsedTime imageTime = new ElapsedTime();
    private ElapsedTime imageSampleTime = new ElapsedTime();
    private ElapsedTime imageCompressTime = new ElapsedTime();
    private YuvImage imageSample = null;
    private int imageSampleCount = 0;

    private int cameraWidth;
    private int cameraHeight;

    private String data; // probably unnecessary

    // Hack stuff.
    private boolean useMotors = false;
    private boolean useEncoders = false;
    private boolean useArm = false;
    private boolean useCamera = true;
    private boolean hackTimeouts = true;

    // Preview callback lambda
    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera)
        {
            // meter the sampling
            synchronized(imageLock) {
                if (imageSampleTime.milliseconds() < 1000) {
                    return;
                }
            }

            // extract the image from the camera.
            YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, cameraWidth, cameraHeight, null);

            // UI thread and event thread will access this at same time.
            synchronized(imageLock) {
                imageSample = yuvImage;
                imageSampleCount++;
                imageSampleTime.reset();
            }
        }
    };


    /**
     * Code to run ONCE when the driver hits INITh6
     */
    @Override
    public void init() {

        // Initialize the motors.
        if (useMotors) {
            motorBackLeft = hardwareMap.get(DcMotor.class, "motor0");
            motorBackRight = hardwareMap.get(DcMotor.class, "motor1");
            motorFrontLeft = hardwareMap.get(DcMotor.class, "motor2");
            motorFrontRight = hardwareMap.get(DcMotor.class, "motor3");

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
            motorBackRight.setDirection(DcMotor.Direction.REVERSE);
            motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
            motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

            if (useEncoders) {
                motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }

        if (useArm) {
            shoulder = hardwareMap.get(DcMotor.class, "motor4");
            extender = hardwareMap.get(DcMotor.class, "motor5");
        }

        if (useCamera) {
            camera_init();
        }

        if (hackTimeouts) {
            this.msStuckDetectInit = 30000;
            this.msStuckDetectInitLoop = 30000;
            this.msStuckDetectStart = 30000;
            this.msStuckDetectLoop = 30000;
            this.msStuckDetectStop = 30000;
        }
    }


    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    private void camera_init() {
        // TODO: there is no way to get camera from hardware map?
        camera = ((FtcRobotControllerActivity)hardwareMap.appContext).camera;
        camera.setPreviewCallback(previewCallback);

        Camera.Parameters parameters = camera.getParameters();
        data = parameters.flatten(); // TODO: what is this doing? no one reads from data.
        cameraWidth = parameters.getPreviewSize().width;
        cameraHeight = parameters.getPreviewSize().height;

        // Build a little window on the app to display the image.
        ((FtcRobotControllerActivity) hardwareMap.appContext).initPreview(camera, this, previewCallback);
    }

    @Override
    public void accept(CameraPreview p) {
        this.preview = p;
    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        if (useMotors) {
            // Control the wheel motors.
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double driveNormal = -gamepad1.left_stick_y;
            double driveStrafe = -gamepad1.left_stick_x;
            if (Math.abs(driveNormal) < 0.1)
                driveNormal = 0.0; // Prevent the output from saying "-0.0".
            if (Math.abs(driveStrafe) < 0.1)
                driveStrafe = 0.0; // Prevent the output from saying "-0.0".

            double turn = gamepad1.right_stick_x;

            double leftBackPower = Range.clip(driveNormal + turn + driveStrafe, -0.8, 0.8);
            double rightBackPower = Range.clip(driveNormal - turn - driveStrafe, -0.8, 0.8);
            double leftFrontPower = Range.clip(driveNormal + turn - driveStrafe, -0.8, 0.8);
            double rightFrontPower = Range.clip(driveNormal - turn + driveStrafe, -0.8, 0.8);

            double halfLeftBackPower = Range.clip(driveNormal + turn + driveStrafe, -0.25, 0.25);
            double halfRightBackPower = Range.clip(driveNormal - turn - driveStrafe, -0.25, 0.25);
            double halfLeftFrontPower = Range.clip(driveNormal + turn - driveStrafe, -0.25, 0.25);
            double halfRightFrontPower = Range.clip(driveNormal - turn + driveStrafe, -0.25, 0.25);

            boolean halfSpeed = gamepad1.left_bumper && gamepad1.right_bumper;


            if (halfSpeed) {
                motorBackLeft.setPower(halfLeftBackPower);
                motorBackRight.setPower(halfRightBackPower);
                motorFrontLeft.setPower(halfLeftFrontPower);
                motorFrontRight.setPower(halfRightFrontPower);
            } else {
                motorBackLeft.setPower(leftBackPower);
                motorBackRight.setPower(rightBackPower);
                motorFrontLeft.setPower(leftFrontPower);
                motorFrontRight.setPower(rightFrontPower);
            }
        }

        if (useArm) {
            boolean pullUp = gamepad1.dpad_down;
            boolean pullOut = gamepad1.dpad_up;
            double pullPower = 0.0;
            if (pullUp) {
                pullPower = 1.0;
            } else if (pullOut) {
                pullPower = -1.0;
            }
            shoulder.setPower(pullPower);

            // Control the extender.
            boolean extendOut = gamepad1.y;
            boolean extendIn = gamepad1.a;
            double extendPower = 0.0;
            if (extendOut) {
                extendPower = 1.0;
            } else if (extendIn) {
                extendPower = -1.0;
            }
            extender.setPower(extendPower);
        }

        if (useCamera) {
            camera_loop();
        }
    }



    private static int red(int pixel) {
        return (pixel >> 16) & 0xff;
    }

    private static int green(int pixel) {
        return (pixel >> 8) & 0xff;
    }

    private static int blue(int pixel) {
            return pixel & 0xff;
        }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    private static int highestColor(int red, int green, int blue) {
        int[] color = {red,green,blue};
        int value = 0;
        for (int i = 1; i < 3; i++) {
            if (color[value] < color[i]) {
                value = i;
            }
        }
        return value;
    }


    private Bitmap convertImage(YuvImage yuvImage) {
        // TODO: use different params to get different subrects
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, cameraWidth, cameraHeight), 0, out);
        byte[] imageBytes = out.toByteArray();

        Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return image;
    }

    private void camera_loop() {

        YuvImage yuvImage = null;
        synchronized (imageLock) {
            if (imageSample != null) {
                yuvImage = imageSample;
                imageSample = null;
            }
        }

        if (yuvImage != null) {
            int redValue = 0;
            int blueValue = 0;
            int greenValue = 0;
            Bitmap image = convertImage(yuvImage);
            for (int x = 0; x < cameraWidth; x++) {
                for (int y = 0; y < cameraHeight; y++) {
                    int pixel = image.getPixel(x, y);
                    redValue += red(pixel);
                    blueValue += blue(pixel);
                    greenValue += green(pixel);
                }
            }
            int color = highestColor(redValue, greenValue, blueValue);
            String colorString = "";
            switch (color) {
                case 0:
                    colorString = "RED";
                    break;
                case 1:
                    colorString = "GREEN";
                    break;
                case 2:
                    colorString = "BLUE";
            }
            telemetry.addData("Color:", "Color detected is: " + colorString);
        }
        telemetry.addData("Looped","Looped " + Integer.toString(imageSampleCount) + " times");
        Log.d("DEBUG:",data);
    }
}