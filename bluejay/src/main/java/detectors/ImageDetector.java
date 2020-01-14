package detectors;

import android.content.Context;
import android.graphics.Bitmap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.*;

import java.util.ArrayList;
import java.util.List;

import localizers.Localizer;
import localizers.PoseOrientation;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

public class ImageDetector extends StartStoppable implements Localizer{
	private static final String VUFORIA_KEY =
			"<--Key Here-->";
	//phone attributes
	private static final boolean PHONE_IS_PORTRAIT = false;
	// Everything is in mm
	private static final float mmPerInch = 25.4f;
	private static final float mmTargetHeight = (6) * mmPerInch;          // the height of the center of the target
	// Constant for Stone Target
	private static final float stoneZ = 2.00f * mmPerInch;
	// Constants for the center support targets
	private static final float bridgeZ = 6.42f * mmPerInch;
	private static final float bridgeY = 23 * mmPerInch;
	private static final float bridgeX = 5.18f * mmPerInch;
	private static final float bridgeRotY = 59;                                 // Units are degrees
	// image above the floor
	private static final float bridgeRotZ = 180;
	// Constants for perimeter targets
	private static final float halfField = 72 * mmPerInch;
	private static final float quadField = 36 * mmPerInch;
	VuforiaTrackables allTrackables;
	private VuforiaLocalizer vuforia = null;

	public ImageDetector(OpMode opMode) {
		this(opMode, false);
	}
	
	public ImageDetector(OpMode opMode, boolean useDisplay) {
		
		if (isVuforiaInitialized()) return;
		
		setupVuforia(useDisplay, opMode.hardwareMap.appContext);
		
		allTrackables = vuforia.loadTrackablesFromAsset("Skystone");
		
		setupTrackables(allTrackables);
		
		setupPhone();
	}
	
	/**
	 * ensures that the static vuforia is made
	 **/
	private void setupVuforia(boolean useDisplay, Context appContext) {
		int cameraMonitorViewId = appContext.getResources().getIdentifier("cameraMonitorViewId", "id",
				appContext.getPackageName());
		
		//Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
		VuforiaLocalizer.Parameters parameters;
		if (useDisplay) parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
		else parameters = new VuforiaLocalizer.Parameters();
		
		parameters.vuforiaLicenseKey = VUFORIA_KEY;
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
		
		//  Instantiate the Vuforia engine
		VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(parameters);
		
		//not necessary, you can ignore it
		Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
		
		this.vuforia = vuforia;

		vuforia.setFrameQueueCapacity(1);
	}
	
	private void setupTrackables(VuforiaTrackables targetsSkyStone) {
		// Load the data sets for the trackable objects. These particular data
		// sets are stored in the 'assets' part of our application.
		
		VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
		stoneTarget.setName("Stone Target");
		VuforiaTrackable blueRearBridge = targetsSkyStone.get(1);
		blueRearBridge.setName("Blue Rear Bridge");
		VuforiaTrackable redRearBridge = targetsSkyStone.get(2);
		redRearBridge.setName("Red Rear Bridge");
		VuforiaTrackable redFrontBridge = targetsSkyStone.get(3);
		redFrontBridge.setName("Red Front Bridge");
		VuforiaTrackable blueFrontBridge = targetsSkyStone.get(4);
		blueFrontBridge.setName("Blue Front Bridge");
		VuforiaTrackable red1 = targetsSkyStone.get(5);
		red1.setName("Red Perimeter 1");
		VuforiaTrackable red2 = targetsSkyStone.get(6);
		red2.setName("Red Perimeter 2");
		VuforiaTrackable front1 = targetsSkyStone.get(7);
		front1.setName("Front Perimeter 1");
		VuforiaTrackable front2 = targetsSkyStone.get(8);
		front2.setName("Front Perimeter 2");
		VuforiaTrackable blue1 = targetsSkyStone.get(9);
		blue1.setName("Blue Perimeter 1");
		VuforiaTrackable blue2 = targetsSkyStone.get(10);
		blue2.setName("Blue Perimeter 2");
		VuforiaTrackable rear1 = targetsSkyStone.get(11);
		rear1.setName("Rear Perimeter 1");
		VuforiaTrackable rear2 = targetsSkyStone.get(12);
		rear2.setName("Rear Perimeter 2");
		
		stoneTarget.setLocation(OpenGLMatrix
				                        .translation(0, 0, stoneZ)
				                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0,
						                        -90)));
		
		//Set the position of the bridge support targets with relation to origin (center of field)
		blueFrontBridge.setLocation(OpenGLMatrix
				                            .translation(-bridgeX, bridgeY, bridgeZ)
				                            .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0,
						                            bridgeRotY, bridgeRotZ)));
		
		blueRearBridge.setLocation(OpenGLMatrix
				                           .translation(-bridgeX, bridgeY, bridgeZ)
				                           .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0,
						                           -bridgeRotY, bridgeRotZ)));
		
		redFrontBridge.setLocation(OpenGLMatrix
				                           .translation(-bridgeX, -bridgeY, bridgeZ)
				                           .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0,
						                           -bridgeRotY, 0)));
		
		redRearBridge.setLocation(OpenGLMatrix
				                          .translation(bridgeX, -bridgeY, bridgeZ)
				                          .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0,
						                          bridgeRotY, 0)));
		
		//Set the position of the perimeter targets with relation to origin (center of field)
		red1.setLocation(OpenGLMatrix
				                 .translation(quadField, -halfField, mmTargetHeight)
				                 .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));
		
		red2.setLocation(OpenGLMatrix
				                 .translation(-quadField, -halfField, mmTargetHeight)
				                 .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));
		
		front1.setLocation(OpenGLMatrix
				                   .translation(-halfField, -quadField, mmTargetHeight)
				                   .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));
		
		front2.setLocation(OpenGLMatrix
				                   .translation(-halfField, quadField, mmTargetHeight)
				                   .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));
		
		blue1.setLocation(OpenGLMatrix
				                  .translation(-quadField, halfField, mmTargetHeight)
				                  .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));
		
		blue2.setLocation(OpenGLMatrix
				                  .translation(quadField, halfField, mmTargetHeight)
				                  .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));
		
		rear1.setLocation(OpenGLMatrix
				                  .translation(halfField, quadField, mmTargetHeight)
				                  .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));
		
		rear2.setLocation(OpenGLMatrix
				                  .translation(halfField, -quadField, mmTargetHeight)
				                  .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));
	}
	
	private void setupPhone() {
		// We need to rotate the camera around its long axis to bring the correct camera forward.
		//if (CAMERA_CHOICE == BACK) { We always use the back camera
		float phoneYRotate = -90;
		float phoneXRotate = 0;
		
		// Rotate the phone vertical about the X axis if it's in portrait mode
		if (PHONE_IS_PORTRAIT) {
			phoneXRotate = 90;
		}
		
		// Next, translate the camera lens to where it is on the robot.
		// In this example, it is centered (left to right), but forward of the middle of the robot, and above ground
		// level.
		final float CAMERA_FORWARD_DISPLACEMENT = 4.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot
		// center
		final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;   // eg: Camera is 8 Inches above ground
		final float CAMERA_LEFT_DISPLACEMENT = 0;     // eg: Camera is ON the robot's center line
		
		float phoneZRotate = 0;
		OpenGLMatrix robotFromCamera = OpenGLMatrix.translation(
				CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT
		).multiplied(Orientation.getRotationMatrix(
				EXTRINSIC, YZX, DEGREES,
				phoneYRotate, phoneZRotate, phoneXRotate
		));
		
		for (VuforiaTrackable trackable : allTrackables) {
			((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, BACK);
		}
	}
	
	public boolean isVuforiaInitialized() {
		return vuforia != null;
	}
	
	public VuforiaLocalizer getVuforiaLocalizer() {
		return vuforia;
	}
	
	public void begin() {
		allTrackables.activate();
	}
	
	public void end() {
		allTrackables.deactivate();
	}

	@Override
	public void loop() {
		calculatePosition();
	}

	/**
	 * Gets the most recent position, if available.
	 */
	public PoseOrientation getPosition() {
		if (!activated) throw new IllegalStateException("Not activated");
		OpenGLMatrix matrix = calculatePosition();
		if (matrix == null) return null;
		return toPoseOrientation(matrix);
	}
	
	OpenGLMatrix calculatePosition() {
		List<OpenGLMatrix> newLocations = new ArrayList<>();
		for (VuforiaTrackable trackable : allTrackables) {
			VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener) trackable.getListener();
			if (listener.isVisible()) {
				
				if (trackable.getName().equals("Stone Target")) {
					//we cannot depend on this for coordinates
					continue;
				}
				OpenGLMatrix robotLocationTransform = listener.getRobotLocation();
				//get, instead of get updated.
				//store in list.
				if (robotLocationTransform != null) {
					newLocations.add(robotLocationTransform);
				}
			}
		}
		//take average, or null if none.
		if (newLocations.isEmpty()) return null;
		OpenGLMatrix sum = newLocations.get(0);
		for (int i = 1; i < newLocations.size(); i++) {
			sum.add(newLocations.get(i));
		}
		sum.multiply(1f / newLocations.size());
		return sum;
	}

	private PoseOrientation toPoseOrientation(OpenGLMatrix matrix) {
		VectorF translation = matrix.getTranslation();
		Orientation rotation = Orientation.getOrientation(matrix, EXTRINSIC, XYZ, DEGREES);
		return new PoseOrientation(translation.get(0), translation.get(1), rotation.thirdAngle);
	}

	/**
	 * @return next image if available.
	 */
	public Bitmap getImage() {
		try {

			VuforiaLocalizer.CloseableFrame closeableFrame = vuforia.getFrameQueue().take();
			try {
				for (int i = 0; i < closeableFrame.getNumImages(); i++) {
					Image image = closeableFrame.getImage(i);
					
					if (image.getFormat() == PIXEL_FORMAT.RGB565) {
						Bitmap bm = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.RGB_565);
						bm.copyPixelsFromBuffer(image.getPixels());
						return bm;
					}
				}
			} finally {
				closeableFrame.close();
			}
		} catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
		return null;
	}
}
