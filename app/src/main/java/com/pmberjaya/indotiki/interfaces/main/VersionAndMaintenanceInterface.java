package com.pmberjaya.indotiki.interfaces.main;

import com.pmberjaya.indotiki.callbacks.main.VersionAndMaintenanceCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface VersionAndMaintenanceInterface {
    void onSuccessVersionAndMaintenance(VersionAndMaintenanceCallback data);
    void onErrorVersionAndMaintenance(APIErrorCallback data);
}
