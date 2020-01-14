package detectors;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

import detectors.FoundationPipeline.Foundation;
import detectors.FoundationPipeline.Pipeline;
import detectors.FoundationPipeline.SkyStone;
import detectors.FoundationPipeline.Stone;

public class OpenCvDetector extends StartStoppable {

	//Originally in RobotControllerActivity, but caused the camera shutter to make weird noises, so now it lives here
	static {
		OpenCVLoader.initDebug();
		//OR System.LoadLibrary("opencv_java3");
	}

	private List<Foundation> foundations = new ArrayList<>(); //detected foundations
	private List<Stone>      stones      = new ArrayList<>();
	private List<SkyStone>   skyStones   = new ArrayList<>();
	private ImageDetector    vuforia;


	public OpenCvDetector(OpMode opMode) {
		this.vuforia = new ImageDetector(opMode);
	}

	@Override
	public void loop() {
		//Log.d("GO TO MO","go");
		updateObjects();
	}

	@Override
	public void begin() {
		vuforia.start();
	}

	@Override
	public void end() {
		vuforia.stop();
	}

	/**
	 * hold the phone as you would use it to browse reddit
	 * x: 0 at the top, increases as you go down
	 * y: 0 at the right, increases as you go left
	 */
	private void updateObjects() {
		//get raw image
		//raw image for camera
		Bitmap image = vuforia.getImage();

		//raw to Mat
		//image converted to OpenCV Mat
		Mat matImage = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC1);
		Utils.bitmapToMat(image, matImage);

		//Opencv pipeline
		Pipeline.process(matImage);

		foundations.clear();
		foundations.addAll(Pipeline.foundations);
		stones.clear();
		stones.addAll(Pipeline.stones);
		skyStones.clear();
		skyStones.addAll(Pipeline.skyStones);
	}

	public Foundation[] getObjectsFoundations() {
		if (!activated) throw new IllegalStateException("Not activated");

		return foundations.toArray(new Foundation[0]);
	}

	public Stone[] getObjectsStones() {
		if (!activated) throw new IllegalStateException("Not activated");

		return stones.toArray(new Stone[0]);
	}

	public SkyStone[] getObjectsSkyStones() {
		if (!activated) throw new IllegalStateException("Not activated");

		return skyStones.toArray(new SkyStone[0]);
	}
}
