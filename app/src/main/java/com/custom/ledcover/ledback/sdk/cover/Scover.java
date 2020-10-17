package com.custom.ledcover.ledback.sdk.cover;

import android.content.Context;
import com.custom.ledcover.ledback.sdk.SsdkInterface;
import com.custom.ledcover.ledback.sdk.SsdkUnsupportedException;
import com.custom.ledcover.ledback.sdk.SsdkVendorCheck;

public final class Scover implements SsdkInterface {
    private Context mContext;

    public int getVersionCode() {
        return 16842752;
    }

    public String getVersionName() {
        return String.format("%d.%d.%d", new Object[]{1, 1, 0});
    }

    public void initialize(Context context) throws SsdkUnsupportedException, IllegalArgumentException {
        this.mContext = context;
        if (context == null) {
            throw new IllegalArgumentException("context may not be null!!");
        } else if (!SsdkVendorCheck.isSamsungDevice()) {
            throw new SsdkUnsupportedException("This is not Samsung device!!!", 0);
        } else if (!new ScoverManager(this.mContext).isSupportCover()) {
            throw new SsdkUnsupportedException("This device is not supported Scover!!!", 1);
        }
    }

    public boolean isFeatureEnabled(int i) {
        return new ScoverManager(this.mContext).isSupportTypeOfCover(i);
    }
}
