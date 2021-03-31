package com.kooapps.wordsearch.model.event.main;

import androidx.annotation.NonNull;

public final class EagerEventDispatcher {

    private static EventDispatcher DISPATCHER = new EventDispatcher();

    public static <E extends Event> void addListener(@NonNull int name, @NonNull EventListener<E> listener) {
        DISPATCHER.addListener(name, listener);
    }

    public static <E extends Event> void removeListener(@NonNull int name, @NonNull EventListener listener) {
        DISPATCHER.removeListener(name, listener);
    }

    public static <E extends Event> void dispatch(@NonNull E event) {
        DISPATCHER.dispatch(event);
    }
}
