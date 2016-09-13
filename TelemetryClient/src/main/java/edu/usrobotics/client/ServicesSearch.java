package edu.usrobotics.client;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

/**
 * Created by Max on 9/10/2016.
 */
public class ServicesSearch {
    /**
     * UUID used to find specific service supported by bluetooth device
     * https://www.bluetooth.org/en-us/specification/assigned-numbers/service-discovery
     * Find UUIDs for all types of bluetooth services.
     */
    /* To find push object service */
    private UUID OBEX_OBJECT_PUSH_PROFILE = new UUID(0x1105);
    /* To find file transfer service */
    private UUID OBEX_FILE_TRANSFER_PROFILE = new UUID(0x1106);
    /* To find hands free service */
    private UUID HANDS_FREE = new UUID(0x111E);
    /* Get URL attribute from bluetooth service */
    private int URL_ATTRIBUTE = 0X0100;

    String url;

    public RemoteDevice getBluetoothDevice (String name) {
        return new RemoteDeviceDiscovery().getDevice(name);
    }

    public String getBluetoothServiceURL (RemoteDevice device, String uuid) {

        UUID[] searchUuidSet = new UUID[]{new UUID(uuid, false)};
        //UUID[] searchUuidSet = new UUID[]{HANDS_FREE};

        final Object serviceSearchCompletedEvent = new Object();
        int[] attrIDs = new int[]{URL_ATTRIBUTE};

        try {
            DiscoveryListener listener = new DiscoveryListener() {

                public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                }

                public void inquiryCompleted(int discType) {
                }

                /* Find service URL of bluetooth device */
                public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                    for (ServiceRecord aServRecord : servRecord) {
                        String urlI = aServRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                        if (urlI == null) {
                            continue;
                        }

                        url = urlI;
                        try {
                            LocalDevice.getLocalDevice().getDiscoveryAgent().cancelServiceSearch(transID);
                        } catch (BluetoothStateException e) {
                            e.printStackTrace();
                        }
                    }
                }

                public void serviceSearchCompleted(int transID, int respCode) {
                    /* Notify thread when search completed */
                    synchronized (serviceSearchCompletedEvent) {
                        serviceSearchCompletedEvent.notifyAll();
                    }
                }
            };

            synchronized (serviceSearchCompletedEvent) {
                LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet, device, listener);
                serviceSearchCompletedEvent.wait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Return bluetooth devices detail */
        return url;
    }
}
