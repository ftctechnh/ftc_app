
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.photo;



// C++: class TonemapMantiuk
//javadoc: TonemapMantiuk
public class TonemapMantiuk extends Tonemap {

    protected TonemapMantiuk(long addr) { super(addr); }


    //
    // C++:  float getScale()
    //

    //javadoc: TonemapMantiuk::getScale()
    public  float getScale()
    {
        
        float retVal = getScale_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void setScale(float scale)
    //

    //javadoc: TonemapMantiuk::setScale(scale)
    public  void setScale(float scale)
    {
        
        setScale_0(nativeObj, scale);
        
        return;
    }


    //
    // C++:  float getSaturation()
    //

    //javadoc: TonemapMantiuk::getSaturation()
    public  float getSaturation()
    {
        
        float retVal = getSaturation_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void setSaturation(float saturation)
    //

    //javadoc: TonemapMantiuk::setSaturation(saturation)
    public  void setSaturation(float saturation)
    {
        
        setSaturation_0(nativeObj, saturation);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  float getScale()
    private static native float getScale_0(long nativeObj);

    // C++:  void setScale(float scale)
    private static native void setScale_0(long nativeObj, float scale);

    // C++:  float getSaturation()
    private static native float getSaturation_0(long nativeObj);

    // C++:  void setSaturation(float saturation)
    private static native void setSaturation_0(long nativeObj, float saturation);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
