package com.kooapps.wordsearch.model.event.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventListenerList {

    private final HashMap<Integer, CopyOnWriteArrayList<EventListener>> mListeners = new HashMap<>();

    public synchronized void put(@NonNull int name, @NonNull EventListener listener) {
        CopyOnWriteArrayList<EventListener> list = mListeners.get(name);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
            mListeners.put(name, list);
        }

        if (list.contains(listener)) {
            return;
        }

        list.add(listener);
    }

    @Nullable
    public synchronized CopyOnWriteArrayList<EventListener> get(@NonNull int name) {
        CopyOnWriteArrayList<EventListener> list = mListeners.get(name);
        if (list == null) {
            return null;
        }
        return list;
    }

    public synchronized void remove(@NonNull int name, @NonNull EventListener listener) {
        CopyOnWriteArrayList<EventListener> list = mListeners.get(name);
        if (list == null) {
            return;
        }

        list.remove(listener);
    }

    public synchronized void clear() {
        mListeners.clear();
    }
}
