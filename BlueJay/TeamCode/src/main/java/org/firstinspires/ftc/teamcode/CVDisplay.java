package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import detectors.FoundationPipeline.Pipeline;

/*

    If you're using this library, THANKS! I spent a lot of time on it.

    However, stuff isn't as well-documented as I like...still working on that

    So if you have questions, email me at xchenbox@gmail.com and I will get back to you in about a day (usually)

    Enjoy!

    Below is the code to display to the RC; thanks DogeCV! I tried easyOpenCV, but it was lagging and stuttering. (??)
	If it crashes after about a minute, it's probably becaugit reflog expire --expire=now --all && git gc --prune=now --aggressivese OpenCV is using too much native memory. My solution
	is to call System.gc() whenever it reaches 70% (works on my g4 play) , but if someone knows more please contact me.
 */

@TeleOp(name = "CV Simulator", group = "Auto")
public class CVDisplay extends OpMode {

    private DogeCVDetector detector = new DogeCVDetector() {
        @Override
        public Mat process(Mat rgba) {

        	/*
        	    Here you can specify which elements are being detected.
        	    At the moment, SkyStones are super-reliable, Individual stones can be detected if you set up
        	    the camera right, and Foundations...need work.

        	 */
            Pipeline.doFoundations=false;
            Pipeline.doStones=false;
            Pipeline.doSkyStones=true;


            Mat m = Pipeline.process(rgba);

            telemetry.update();

            Imgproc.resize(m, m, new Size(640*1.3, 480*1.3 ));
            return m;
        }
        @Override
        public void useDefaults() {}
    };

    @Override
    public void init() {
        telemetry.setAutoClear(true);
        // Set up detector
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance()); // Initialize it with the app context and camera
        detector.enable();
    }
    /*
     * Code to run REPEATEDLY when the driver hits INIT
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    /*
     * Code to run REPEATEDLY when the driver hits PLAY
     */
    @Override
    public void loop() {
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        if(detector != null) detector.disable(); //Make sure to run this on stop!
    }

    public static void clean(){
    	System.gc();
    }
}
