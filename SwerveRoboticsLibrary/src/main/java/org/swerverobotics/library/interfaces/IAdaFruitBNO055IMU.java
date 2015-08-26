package org.swerverobotics.library.interfaces;

/**
 * Interface to Adafruit 9-DOF Absolute Orientation IMU Fusion Breakout - BNO055 
 * 
 * Undoubtedly this needs further refinement and expansion. All in due time.
 * 
 * @see <a href="http://www.adafruit.com/products/2472">http://www.adafruit.com/products/2472</a>
 */
public interface IAdaFruitBNO055IMU
    {
    void initialize(BNO055_OPERATION_MODE mode) throws InterruptedException;
    void setExternalCrystalUse(boolean useExternalCrystal) throws InterruptedException;
    
    byte getSystemStatus() throws InterruptedException;
    byte getSystemError() throws InterruptedException;

    /**
     * Constants are as in Table 3-5 (p21) of the BNO055 specification.
     */
    enum BNO055_OPERATION_MODE
        {
        CONFIG(0X00),
        ACCONLY(0X01),
        MAGONLY(0X02),
        GYRONLY(0X03),
        ACCMAG(0X04),
        ACCGYRO(0X05),
        MAGGYRO(0X06),
        AMG(0X07),
        IMU(0X08),
        COMPASS(0X09),
        M4G(0X0A),
        NDOF_FMC_OFF(0X0B),
        NDOF(0X0C);
        //------------------------------------------------------------------------------------------
        private byte value;
        private BNO055_OPERATION_MODE(int value)
            {
            this.value = (byte)value;
            }
        }    
    }
