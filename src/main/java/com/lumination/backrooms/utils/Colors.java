package com.lumination.backrooms.utils;

public class Colors {
  public static final int white = 16777215;

  public static int getColorFromRGB(int r, int g, int b) {
    return ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
  }
}
