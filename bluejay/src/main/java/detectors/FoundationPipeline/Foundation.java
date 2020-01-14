package detectors.FoundationPipeline;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Foundation {
    enum Type {
        BLUEFOUNDATION, REDFOUNDATION, UNKNOWNFOUNDATION
    }

    public int  x = 0;
    public int  y = 0;
    public Type t;
    Rect bounds;

    public Foundation(Rect bounds, Type t) {
        this.x = bounds.x + bounds.width / 2;
        this.y = bounds.y + bounds.height / 2;
        this.bounds = bounds;
        this.t = t;
    }

    public void draw(Mat canvas) {
        Scalar color = new Scalar(00, 255, 00);
        Scalar black = new Scalar(0, 0, 0);

        Imgproc.rectangle(canvas, bounds.tl(), bounds.br(), new Scalar(255, 0, 0), 4);
        Imgproc.putText(canvas, t.toString(), new Point(bounds.tl().x,bounds.tl().y+20), 0, 0.6, black, 7);
        Imgproc.putText(canvas, t.toString(), new Point(bounds.tl().x,bounds.tl().y+20), 0, 0.6, color, 2);

    }
    

    /**
     * @param d is a black detected
     * @param j is either blue or red or yellow detected
     */
    static Foundation createFoundation(Detected d, Detected j) {
        //combine Point arrays
        Point[] blackPoints = d.shape.toArray();
        Point[] colorPoints = j.shape.toArray();
        Point[] allTogetherNow = new Point[blackPoints.length+colorPoints.length];
        for(int i =0;i<blackPoints.length;i++) {
        	allTogetherNow[i]=blackPoints[i];
        }
        for(int i = blackPoints.length;i<allTogetherNow.length;i++) {
        	allTogetherNow[i]=colorPoints[i-blackPoints.length];
        }

        //draw Rectangle around them
        Rect foundationbound = Imgproc.boundingRect(new MatOfPoint(allTogetherNow));

        //add to Foundation List
        Type type = null;
        if (j.c == Detected.Color.BLUE) type = Type.BLUEFOUNDATION;
        if (j.c == Detected.Color.RED) type = Type.REDFOUNDATION;
        if (j.c == Detected.Color.YELLOW) type = Type.UNKNOWNFOUNDATION;
        return new Foundation(
                foundationbound,
                type
        );
            
    }


}
