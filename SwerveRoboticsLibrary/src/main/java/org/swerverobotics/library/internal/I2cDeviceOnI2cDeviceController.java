package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import java.util.concurrent.locks.Lock;

/**
 * @deprecated Code that previously used I2cDeviceOnI2cDeviceController should use I2cDeviceImpl instead
 */
@Deprecated
public final class I2cDeviceOnI2cDeviceController extends I2cDeviceImpl
    {
    public I2cDeviceOnI2cDeviceController(I2cController controller, int port)
        {
        super(controller, port);
        }
    }
