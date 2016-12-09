
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.aruco;

import org.opencv.core.Mat;

// C++: class Dictionary
//javadoc: Dictionary
public class Dictionary {

    protected final long nativeObj;
    protected Dictionary(long addr) { nativeObj = addr; }


    //
    // C++: static Ptr_Dictionary create(int nMarkers, int markerSize, Ptr_Dictionary baseDictionary)
    //

    //javadoc: Dictionary::create(nMarkers, markerSize, baseDictionary)
    public static Dictionary create_from(int nMarkers, int markerSize, Dictionary baseDictionary)
    {
        
        Dictionary retVal = new Dictionary(create_from_0(nMarkers, markerSize, baseDictionary.nativeObj));
        
        return retVal;
    }


    //
    // C++: static Ptr_Dictionary create(int nMarkers, int markerSize)
    //

    //javadoc: Dictionary::create(nMarkers, markerSize)
    public static Dictionary create(int nMarkers, int markerSize)
    {
        
        Dictionary retVal = new Dictionary(create_0(nMarkers, markerSize));
        
        return retVal;
    }


    //
    // C++: static Ptr_Dictionary get(int dict)
    //

    //javadoc: Dictionary::get(dict)
    public static Dictionary get(int dict)
    {
        
        Dictionary retVal = new Dictionary(get_0(dict));
        
        return retVal;
    }


    //
    // C++:  void drawMarker(int id, int sidePixels, Mat& _img, int borderBits = 1)
    //

    //javadoc: Dictionary::drawMarker(id, sidePixels, _img, borderBits)
    public  void drawMarker(int id, int sidePixels, Mat _img, int borderBits)
    {
        
        drawMarker_0(nativeObj, id, sidePixels, _img.nativeObj, borderBits);
        
        return;
    }

    //javadoc: Dictionary::drawMarker(id, sidePixels, _img)
    public  void drawMarker(int id, int sidePixels, Mat _img)
    {
        
        drawMarker_1(nativeObj, id, sidePixels, _img.nativeObj);
        
        return;
    }


    //
    // C++: Mat Dictionary::bytesList
    //

    //javadoc: Dictionary::get_bytesList()
    public  Mat get_bytesList()
    {
        
        Mat retVal = new Mat(get_bytesList_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: int Dictionary::markerSize
    //

    //javadoc: Dictionary::get_markerSize()
    public  int get_markerSize()
    {
        
        int retVal = get_markerSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: int Dictionary::maxCorrectionBits
    //

    //javadoc: Dictionary::get_maxCorrectionBits()
    public  int get_maxCorrectionBits()
    {
        
        int retVal = get_maxCorrectionBits_0(nativeObj);
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_Dictionary create(int nMarkers, int markerSize, Ptr_Dictionary baseDictionary)
    private static native long create_from_0(int nMarkers, int markerSize, long baseDictionary_nativeObj);

    // C++: static Ptr_Dictionary create(int nMarkers, int markerSize)
    private static native long create_0(int nMarkers, int markerSize);

    // C++: static Ptr_Dictionary get(int dict)
    private static native long get_0(int dict);

    // C++:  void drawMarker(int id, int sidePixels, Mat& _img, int borderBits = 1)
    private static native void drawMarker_0(long nativeObj, int id, int sidePixels, long _img_nativeObj, int borderBits);
    private static native void drawMarker_1(long nativeObj, int id, int sidePixels, long _img_nativeObj);

    // C++: Mat Dictionary::bytesList
    private static native long get_bytesList_0(long nativeObj);

    // C++: int Dictionary::markerSize
    private static native int get_markerSize_0(long nativeObj);

    // C++: int Dictionary::maxCorrectionBits
    private static native int get_maxCorrectionBits_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
