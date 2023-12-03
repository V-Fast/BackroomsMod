package org.vfast.backrooms.util.accessor;

public interface IPlayerSanityAccessor {
    default void modifySanity(int amount) {}

    default int getSanity() {
        return 0;
    }
}
