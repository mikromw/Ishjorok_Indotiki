package com.pmberjaya.indotiki.interfaces.deposit;

import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.callbacks.deposit.DepositCallback;
import com.pmberjaya.indotiki.callbacks.deposit.DepositNewConfirmationCallback;

/**
 * Created by edwin on 04/12/2017.
 */

public interface DepositNewConfirmationInterface {
    void onSuccess(DepositNewConfirmationCallback depositNewConfirmationCallback);
    void onError(APIErrorCallback apiErrorCallback);
}
