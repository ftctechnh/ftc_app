package detectors.FoundationPipeline;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class gap {
	public int   y;
	public int   x1;
	public int   x2;
	public int   width;
	public Point center;
	
	public void draw (Mat canvas) {
		Scalar color = new Scalar(100,225,255);
	
		Imgproc.line(canvas, new Point(x1,y), new Point(x2,y), color, 3);
		Imgproc.line(canvas, new Point(x1,y+10), new Point(x1,y-10), color, 3);
		Imgproc.line(canvas, new Point(x2,y+10), new Point(x2,y-10), color, 3);
	}
	
	public gap(int y, int x1, int x2) {
		this.y=y;
		this.x1=x1;
		this.x2=x2;
		
		width = Math.abs(x2-x1);
		center = new Point((x2+x1)/2,y);
	}
}
