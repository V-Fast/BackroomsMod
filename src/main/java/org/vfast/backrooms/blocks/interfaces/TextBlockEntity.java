package org.vfast.backrooms.blocks.interfaces;

public interface TextBlockEntity {
    boolean setFrontText(String frontText);
    boolean setBackText(String frontText);

    String getFrontText();
    String getBackText();

    void updateText(String text, boolean isFrontText);

    int maxTextWidth();
}
