package com.pmberjaya.indotiki.callbacks.main;

import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.models.main.AppVersionData;
import com.pmberjaya.indotiki.models.main.MaintenanceData;

import java.util.List;

/**
 * Created by edwin on 10/08/2016.
 */
public class VersionAndMaintenanceCallback extends BaseCallback {
    private MaintenanceData data_maintenance;
    List<AppVersionData> data_version;
    public MaintenanceData getData_maintenance() {
        return data_maintenance;
    }

    public void setData_maintenance(MaintenanceData data_maintenance) {
        this.data_maintenance = data_maintenance;
    }


    public List<AppVersionData> getData_version() {
        return data_version;
    }

    public void setData_version(List<AppVersionData> data_version) {
        this.data_version = data_version;
    }
}
