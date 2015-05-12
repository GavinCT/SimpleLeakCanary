package com.czt.simpleleakcanary.library;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * Created by chentong on 12/5/15.
 * mail:chentong02@meituan.com
 */
public class KeyedWeakReference extends WeakReference<Object> {
    private String key;
    public KeyedWeakReference(Object r, String key, ReferenceQueue<Object> q) {
        super(r, q);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
