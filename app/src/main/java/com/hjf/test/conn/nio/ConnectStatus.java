package com.hjf.test.conn.nio;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({

})
@Retention(RetentionPolicy.SOURCE)
public @interface ConnectStatus {

    int CONNECT_PRE = 1;
    int CONNECT = 2;
    int CONNECT_OK = 3;

}
