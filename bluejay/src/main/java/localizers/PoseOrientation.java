package localizers;

/**
 * Units in mm and degrees.
 */
public class PoseOrientation {
	public double x;
	public double y;
	public double rot;
	
	public PoseOrientation(double x, double y, double rot) {
		this.x = x;
		this.y = y;
		this.rot = rot;
	}
}
