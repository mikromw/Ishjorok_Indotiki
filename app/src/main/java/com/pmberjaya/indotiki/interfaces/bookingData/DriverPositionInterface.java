package com.pmberjaya.indotiki.interfaces.bookingData;

import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.models.bookingNew.DriverPositionListData.DriverPositionListModel;

/**
 * Created by edwin on 21/08/2017.
 */

public interface DriverPositionInterface {
    void onSuccessGetDriverPosition(BaseGenericCallback<DriverPositionListModel> driverPositionBookingCallback);
    void onErrorGetDriverPosition(APIErrorCallback apiErrorCallback);
}
