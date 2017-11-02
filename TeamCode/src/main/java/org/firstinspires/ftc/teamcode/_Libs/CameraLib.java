package org.firstinspires.ftc.teamcode._Libs;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.nio.IntBuffer;
import java.util.List;

/**
 * Library of utility classes that support acquiring images from the phone's camera
 * Created by phanau on 12/30/15.
 */
public class CameraLib {

    // functional utility class that converts image in NV21 format to RGB
    static class NV21toRGB {

        static Bitmap convert(byte[] data, int imageWidth, int imageHeight) {

            // the bitmap we want to fill with the image
            Bitmap bitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
            int numPixels = imageWidth * imageHeight;

            // the buffer we fill up which we then fill the bitmap with
            IntBuffer intBuffer = IntBuffer.allocate(imageWidth * imageHeight);
            // If you're reusing a buffer, next line imperative to refill from the start,
            // if not good practice
            intBuffer.position(0);

            // Set the alpha for the image: 0 is transparent, 255 fully opaque
            final byte alpha = (byte) 255;

            // Get each pixel, one at a time
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    // Get the Y value, stored in the first block of data
                    // The logical "AND 0xff" is needed to deal with the signed issue
                    int Y = data[y * imageWidth + x] & 0xff;

                    // Get U and V values, stored after Y values, one per 2x2 block
                    // of pixels, interleaved. Prepare them as floats with correct range
                    // ready for calculation later.
                    int xby2 = x / 2;
                    int yby2 = y / 2;

                    // change this to V for NV12/420SP
                    float U = (float) (data[numPixels + 2 * xby2 + yby2 * imageWidth] & 0xff) - 128.0f;

                    // change this to U for NV12/420SP
                    float V = (float) (data[numPixels + 2 * xby2 + 1 + yby2 * imageWidth] & 0xff) - 128.0f;

                    // Do the YUV -> RGB conversion
                    float Yf = 1.164f * ((float) Y) - 16.0f;
                    int R = (int) (Yf + 1.596f * V);
                    int G = (int) (Yf - 0.813f * V - 0.391f * U);
                    int B = (int) (Yf + 2.018f * U);

                    // Clip rgb values to 0-255
                    R = R < 0 ? 0 : R > 255 ? 255 : R;
                    G = G < 0 ? 0 : G > 255 ? 255 : G;
                    B = B < 0 ? 0 : B > 255 ? 255 : B;

                    // Put that pixel in the buffer
                    intBuffer.put((alpha << 24) + (R << 16) + (G << 8) + B);
                }
            }

            // Get buffer ready to be read
            intBuffer.flip();

            // Push the pixel information from the buffer onto the bitmap.
            bitmap.copyPixelsFromBuffer(intBuffer);

            return bitmap;
        }
    }


    // a simple utility class that provides a few more operations on an RGB pixel encoded as an int
    // by extending the Android Color class that does most of what we need.
    public static class Pixel extends Color {
        static float[] mHSV = new float[3];     // scratch storage - allocated once at start-up

        public static String toString(int rgb) {
            return "pixel("+Color.red(rgb)+","+green(rgb)+","+blue(rgb)+")";
        }

        // return hue of given RGB pixel, discretized to 6 principal colors:
        // Red,Yellow,Green,Cyan,Blue,Magenta (1..6) or, if saturation < threshold, White(0)
        public static int hue(int pix) {
            float[] hsv = RGBtoHSV(pix, mHSV);
            if (hsv[1] < 0.20) {
                return (int)(hsv[2] * 7 + 7);       // return Value discretized to 8 levels (7..14)
            }
            int iHue = ((int)hsv[0]+30)/60;         // round to nearest 60 degrees of hue
            if (iHue == 6)
                iHue = 0;                           // red is either 0 or 360 degrees of hue
            return iHue+1;          // return discretized hue RYGCBM (1..6)
        }

        // return integer grayscale value (0:255) of a given RGB pixel
        public static int value(int pix){
            float[] hsv = RGBtoHSV(pix, mHSV);
            return (int)(hsv[2]*255);
        }

        // return "dominant color" of an RGB pixel if it has one (or white if it doesn't)
        public static int dominantColor(int pix) {
            final float n = 1.5F;    // dominance factor threshold
            int domClr = 0;     // default is white (i.e. shades of gray)
            if (red(pix)>n*green(pix) && red(pix)>n*blue(pix))
                domClr = 1;     // red
            else
            if (green(pix)>n*red(pix) && green(pix)>n*blue(pix))
                domClr = 3;     // green
            else
            if (blue(pix)>n*red(pix) && blue(pix)>n*green(pix))
                domClr = 5;     // blue
            else
            if (blue(pix)>n*red(pix) && green(pix)>n*red(pix))
                domClr = 4;     // cyan
            else
            if (blue(pix)>n*green(pix) && red(pix)>n*green(pix))
                domClr = 6;     // magenta
            else
            if (red(pix)>n*blue(pix) && green(pix)>n*blue(pix))
                domClr = 2;     // yellow
            // if it has no discernible hue, encode its gray level 0-7
            if (domClr == 0) {
                float value = red(pix)*0.2f + green(pix)*0.7f + blue(pix)*0.1f; // 0..255
                domClr = (int)(value / 32 + 7);  // return Value discretized to 8 levels (7..14)
            }
            return domClr;
        }

        // return string name of given color index
        public static String colorName(int i) {
            String c[] = {"w", "r", "y", "g", "c", "b", "m", "0","1","2","3","4","5","6","7"};
            return c[i];
        }

        public static float[] RGBtoHSV(int pix, float[] hsv) {
            // caller passes in return storage (hsv) to avoid lots of allocations
            // Convert RGB components to HSV.
            // hsv[0] is Hue [0 .. 360)
            // hsv[1] is Saturation [0...1]
            // hsv[2] is Value [0...1]
            colorToHSV(pix, hsv);
            return hsv;     // (also return arg2 for convenience)
        }

        public static int HSVtoRGB(float[] hsv) {
            return HSVToColor(hsv);
        }
    }

    // filter interface ---
    // client can use this to collapse the full set of recognized hues and brightnesses to a smaller set.
    // e.g. if you want both blue and cyan to be coded as "blue".
    public interface Filter {
        public int map(int i);
    }

    // a simple wrapper around the data returned by the camera callback
    // assumes the data is in NV21 format (for now)
    // see http://www.fourcc.org/yuv.php#NV21 for descriptions of various formats
    public static class CameraImage {
        Camera.Size mSize;      // size of the camera that took this image
        byte[] mData;           // data from Camera preview in NV21 format
        Bitmap mBitmap;         // mData converted to RGB

        public CameraImage(final byte[] imageData, Camera c) {
            mData = imageData;      // reference to (readonly) image data
            Camera.Parameters camParms = c.getParameters();
            mSize = camParms.getPictureSize();
            mBitmap = NV21toRGB.convert(mData, mSize.width, mSize.height);
        }

        public Camera.Size cameraSize() {
            return mSize;
        }

        public int dataSize() { return mData.length; }

        public String dataToString(int count) {
            String s = "";
            for (int i=0; i<count; i++) {
                int b = mData[i];
                if (b < 0)      // treat data as unsigned
                    b += 256;
                s += String.valueOf(b)+",";
            }
            return s;
        }

        // return the pixel at (x,y) as ARGB packed in an int
        // adjust for new orientation of camera, now assumed to be in landscape mode, lens facing front,
        // with the USB plug on the TOP (previously bottom) of the phone, so physical camera scanlines now go
        // x: right-to-left  y: bottom-to-top
        // but we will reverse that so our virtual scanlines still go (as before)
        // x: left-to-right  y: top-to-bottom
        public int getPixel(int x, int y) {
            return mBitmap.getPixel(mSize.width-1-x, mSize.height-1-y);
        }

        // return a string representation of the dominant colors along the given scanline
        public String scanlineDomColor(int y, int bandWidth) {
            return scanlineDomColor(y, bandWidth, null);
        }
        public String scanlineDomColor(int y, int bandWidth, Filter f) {
            // scan the given horizontal line of the image for red, green, and blue strips and report
            // dominant pixel color of each bandWidth-pixel band
            Histogram hist = new Histogram(15);
            String sDom = "";               // string describing the line of pixels ito "dominant color"
            for (int x=0; x<cameraSize().width; x++) {
                int pix = getPixel(x, y);                    // get the pixel
                int domClr = CameraLib.Pixel.dominantColor(pix);    // get the dominant color of the pixel
                if (f != null)                                 // if a mapping filter was provided
                    domClr = f.map(domClr);                    // use it to map values
                hist.add(domClr);                            // add a sample to Histogram for this band
                if (x%bandWidth == (bandWidth-1)) {
                    sDom += CameraLib.Pixel.colorName(hist.maxBin());  // add symbol string for most popular color in this band
                    hist.clear();                                // ... and restart the histogram for the next band
                }
            }
            return sDom;
        }

        // return a string representation of the dominant colors along the given scanline
        public String scanlineHue(int y, int bandWidth) {
            return scanlineHue(y, bandWidth, null);
        }
        public String scanlineHue(int y, int bandWidth, Filter f) {
            // scan the given horizontal line of the image for red, green, and blue strips and report
            // dominant pixel hue of each bandWidth-pixel band
            Histogram hist = new Histogram(15);
            String sDom = "";               // string describing the line of pixels ito "dominant color"
            for (int x=0; x<cameraSize().width; x++) {
                int pix = getPixel(x, y);                        // get the pixel
                int domClr = CameraLib.Pixel.hue(pix);           // get the Hue of the pixel
                if (f != null)                                 // if a mapping filter was provided
                    domClr = f.map(domClr);                    // use it to map values
                hist.add(domClr);                                // add the sample to Histogram for this band
                if (x%bandWidth == (bandWidth-1)) {
                    sDom += CameraLib.Pixel.colorName(hist.maxBin());  // add symbol string for most popular color in this band
                    hist.clear();                                // ... and restart the histogram for the next band
                }
            }
            return sDom;
        }

        // return a string representation of the most popular dominant color in each column-band of the image
        public String columnDom(int bandWidth) {
            return columnDom(bandWidth, null);
        }
        public String columnDom(int bandWidth, Filter f) {
            // scan the given horizontal line of the image for red, green, and blue strips and report
            // dominant pixel hue of each bandWidth-pixel band
            Histogram hist = new Histogram(15);
            String sDom = "";               // string describing the line of pixels ito "dominant color"
            for (int x=0; x<cameraSize().width; x++) {
                for (int y=0; y<cameraSize().height; y++) {
                    int pix = getPixel(x, y);                        // get the pixel
                    int domClr = CameraLib.Pixel.dominantColor(pix);           // get the dominant color of the pixel
                    if (f != null)                                   // if a mapping filter was provided
                        domClr = f.map(domClr);                      // use it to map values
                    hist.add(domClr);                                // add the sample to Histogram for this band
                }
                if (x%bandWidth == (bandWidth-1)) {
                    sDom += CameraLib.Pixel.colorName(hist.maxBin());  // add symbol string for most popular color in this band
                    hist.clear();                                    // ... and restart the histogram for the next band
                }
            }
            return sDom;
        }

        // return a string representation of the most popular hue in each column-band of the image
        public String columnHue(int bandWidth) {
            return columnHue(bandWidth, null);
        }
        public String columnHue(int bandWidth, Filter f) {
            // scan the given horizontal line of the image for red, green, and blue strips and report
            // dominant pixel hue of each bandWidth-pixel band
            Histogram hist = new Histogram(15);
            String sDom = "";               // string describing the line of pixels ito "dominant color"
            for (int x=0; x<cameraSize().width; x++) {
                for (int y=0; y<cameraSize().height; y++) {
                    int pix = getPixel(x, y);                        // get the pixel
                    int hue = CameraLib.Pixel.hue(pix);              // get the Hue of the pixel
                    if (f != null)                                   // if a mapping filter was provided
                        hue = f.map(hue);                            // use it to map values
                    hist.add(hue);                                   // add the sample to Histogram for this band
                }
                if (x%bandWidth == (bandWidth-1)) {
                    sDom += CameraLib.Pixel.colorName(hist.maxBin());  // add symbol string for most popular color in this band
                    hist.clear();                                    // ... and restart the histogram for the next band
                }
            }
            return sDom;
        }

        // find the dominant color of pixels in a given rectangle of the image
        public int rectHue(Rect r) {
            return rectHue(r, null);
        }
        public int rectHue(Rect r, Filter f) {
            Histogram hist = new Histogram(15);
            for (int x = r.left; x < r.right; x++) {
                for (int y = r.bottom; y < r.top; y++) {
                    int pix = getPixel(x, y);                       // get the pixel
                    int hue = CameraLib.Pixel.hue(pix);             // get the Hue of the pixel
                    if (f != null)                                  // if a mapping filter was provided
                        hue = f.map(hue);                           // use it to map values
                    hist.add(hue);                                  // add the sample to Histogram for this rectangle
                }
            }
            return hist.maxBin();
        }

        // compute the centroid of pixels of a given color in the given rectangle of the image
        public Point colorCentroid(Rect r, int color) {
            return colorCentroid(r, color, null);
        }
        public Point colorCentroid(Rect r, int color, Filter f) {
            int sumX = 0;
            int sumY = 0;
            int nPix = 0;
            for (int x = r.left; x < r.right; x++) {
                for (int y = r.bottom; y < r.top; y++) {
                    int pix = getPixel(x, y);                        // get the pixel
                    int hue = CameraLib.Pixel.hue(pix);
                    if (f != null)                                   // if a mapping filter was provided
                        hue = f.map(hue);                            // use it to map values
                    if (color == hue) {          // if the Hue of this pixel is what we're looking for ...
                        sumX += x;
                        sumY += y;
                        nPix++;
                    }
                }
            }
            if (nPix == 0)
                nPix++;
            int centX = sumX / nPix;
            int centY = sumY / nPix;
            return new Point(centX, centY);
        }

        // return the given scanline as a Scanline object (an array of byte grayscale values)
        public Scanline scanlineGray(int y) {
            Scanline gray = new Scanline(cameraSize().width);
            for (int x=0; x<cameraSize().width; x++) {
                int pix = getPixel(x, y);
                int value = CameraLib.Pixel.value(pix);            // grayscale value 0:255
                gray.setPixel(x, value);
            }
            return gray;
        }
    }

    // class providing operations on a grayscale byte scanline
    public static class Scanline {
        int[] mData;           // array of byte grayscale pixels
        public Scanline(int width){
            mData = new int[width];
        }
        public Scanline(Scanline s) {
            mData = s.mData.clone();
        }
        public void setPixel(int x, int value) {
            mData[x] = value;
        }
        public int getPixel(int x) {
            return mData[x];
        }
        public void threshold(int value) {
            for (int x=0; x<mData.length; x++)
                mData[x] = (mData[x] > value) ? (byte)1 : (byte)0;
        }
        public int min() {
            int min = 255;
            for (int x=0; x<mData.length; x++)
                if (mData[x] < min)
                    min = mData[x];
            return min;
        }
        public int max() {
            int max = 0;
            for (int x=0; x<mData.length; x++)
                if (mData[x] > max)
                    max = mData[x];
            return max;
        }
        public int centroid() {
            // this call is destructive -- clone this object's data if you want to use it again after this call
            // threshold operation creates array of 0/1 - cut value is average of min and max intensities
            threshold((min()+max())/2);

            // compute center of mass of the array
            int c = 0;
            int n = 0;
            for (int x=0; x<mData.length; x++) {
                c += x * getPixel(x);     // position-weighted sum of non-zero entries
                n += getPixel(x);         // number of non-zero entries
            }
            return (n>0) ? c/n : -1;
        }
        public float fCentroid() {
            int c = centroid();
            return (c==-1) ? -1 : (float)c/(float)mData.length;
        }
    }

    // utility class that constructs a histogram of given data and reports various stats on it
    public static class Histogram {
        int[] mHist;
        public Histogram(int bins) {
            mHist = new int[bins];
            clear();
        }
        public void clear() {
            for (int i=0; i<mHist.length; i++)
                mHist[i] = 0;
        }
        public void add(int sample) {
            if (sample >= 0 && sample < mHist.length)
                mHist[sample]++;
        }
        public int maxBin() {
            int mx = 0;
            for (int i=1; i<mHist.length; i++)
                if (mHist[i] > mHist[mx])
                    mx = i;
            return mx;
        }
    }

    // utility class that wraps up all the data and logic needed to acquire image frames from the camera
    public static class CameraAcquireFrames {
        Camera mCamera;
        SurfaceTexture mDummyTexture;
        CameraImage mPreviewImage;
        int mFrameCount;
        boolean mNewFrame;

        public CameraAcquireFrames() {
            mCamera = null;
            mPreviewImage = null;
            mDummyTexture = null;
            mFrameCount = 0;
            mNewFrame = false;
        }

        Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
            public void onPreviewFrame(byte[] imageData, Camera camera) {
                // process the frame and save results in member variables
                // ...
                mPreviewImage = new CameraImage(imageData, camera);
                mNewFrame = true;
                mFrameCount++;
            }
        };

        public boolean init(int iCamSize) {
            try {
                if (mCamera == null)        // make sure we don't already have it ...
                    mCamera = Camera.open();
                Camera.Parameters parameters = mCamera.getParameters();
                List<Integer> previewFormats = parameters.getSupportedPreviewFormats();
                parameters.setPreviewFormat(ImageFormat.NV21);       // choices are NV21(17) and YV12(842094169)
                List<Camera.Size> previewSizes = parameters.getSupportedPictureSizes();
                int iSize = previewSizes.size()-iCamSize;  // use ith-smallest size (i>0): e.g. (1)160x120, (2)176x144, (3)320x240 entry
                parameters.setPreviewSize(previewSizes.get(iSize).width, previewSizes.get(iSize).height);
                parameters.setPictureSize(previewSizes.get(iSize).width, previewSizes.get(iSize).height);
                mCamera.setParameters(parameters);
                mDummyTexture = new SurfaceTexture(1); // make a target texture with an arbitrary texture id
                mCamera.setPreviewTexture(mDummyTexture);
                mCamera.setPreviewCallback(mPreviewCallback);
                mCamera.startPreview();
            }
            catch (Exception e) {
                if (mCamera != null)
                    mCamera.release();
                mCamera = null;
                return false;
            }
            return true;
        }

        public CameraImage loop() {
            if (mNewFrame) {
                mNewFrame = false;
                // start another frame acquisition
                try {
                    mCamera.setPreviewCallback(mPreviewCallback);
                    mCamera.startPreview();
                }
                catch (Exception e) {
                    return null;
                }
            }
            return mPreviewImage;       // may be null if no data ready yet
        }

        public int frameCount() {
            return mFrameCount;
        }

        public void stop() {
            if (mCamera != null) {          // guard against extra stop() calls ...
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
            }
            mCamera = null;         // delete the Camera object now
            mDummyTexture = null;
            mPreviewImage = null;
        }

    }

}
