package com.kooapps.wordsearch.model.event.main;

import androidx.annotation.NonNull;

import java.util.concurrent.CopyOnWriteArrayList;

public class EventDispatcher {

    @NonNull
    private EventListenerList mListeners = new EventListenerList();

    public <E extends Event> void addListener(@NonNull int name, @NonNull EventListener<E> listener) {
        mListeners.put(name, listener);
    }

    public <E extends Event> void removeListener(@NonNull int name, @NonNull EventListener<E> listener) {
        mListeners.remove(name, listener);
    }

    public <E extends Event> void dispatch(@NonNull E event) {
        CopyOnWriteArrayList<EventListener> listeners = mListeners.get(event.getId());
        if (listeners == null) {
            return;
        }

        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public void dispose() {
        mListeners.clear();
    }
}
