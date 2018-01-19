package com.pmberjaya.indotiki.interfaces.deposit;

import com.pmberjaya.indotiki.callbacks.deposit.DepositCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface DepositLogInterface {
    void onSuccessGetDepositLog(DepositCallback depositCallback);
    void onErrorGetDepositLog(APIErrorCallback apiErrorCallback);
}
