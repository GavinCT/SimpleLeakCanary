package com.czt.simpleleakcanary.library;

import android.util.Log;

import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

/**
 * Created by chentong on 12/5/15.
 * mail:chentong.think@gmail.com
 */
public class RefWatcher {

    private final Set<String> retainedKeys = new CopyOnWriteArraySet<>();
    private final Executor watchExecutor = new AndroidWatchExecutor();
    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();

    public void watch(Object object) {
        String key = UUID.randomUUID().toString();
        retainedKeys.add(key);

        final KeyedWeakReference reference = new KeyedWeakReference(object, key, queue);

        watchExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ensureGone(reference);
            }
        });
    }

    private void ensureGone(KeyedWeakReference reference) {
        removeWeaklyReachableReferences();
        if (gone(reference)) {
            return;
        }

        GcTrigger.DEFAULT.runGc();
        removeWeaklyReachableReferences();

        if (!gone(reference)) {
            String name = reference.get().getClass().getName();
            Log.i("MemoryLeak", "class:" + name);
        }
    }

    private boolean gone(KeyedWeakReference reference) {
        return !retainedKeys.contains(reference.getKey());
    }

    private void removeWeaklyReachableReferences() {
        // WeakReferences are enqueued as soon as the object to which they point to becomes weakly
        // reachable. This is before finalization or garbage collection has actually happened.
        KeyedWeakReference ref;
        while ((ref = (KeyedWeakReference) queue.poll()) != null) {
            retainedKeys.remove(ref.getKey());
        }
    }
}
