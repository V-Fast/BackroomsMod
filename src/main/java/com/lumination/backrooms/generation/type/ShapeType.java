package com.lumination.backrooms.generation.type;

import jdk.jfr.Experimental;

public enum ShapeType {
    /** Makes a box structure */
    BOX,

    /** Makes a rectangle structure */
    RECTANGLE,

    /** Generates a circle structure
     * @apiNote In the work
     */
    @Experimental
    CIRCLE
}
