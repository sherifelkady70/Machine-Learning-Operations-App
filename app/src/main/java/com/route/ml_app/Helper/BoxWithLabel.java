package com.route.ml_app.Helper;

import android.graphics.Rect;

public class BoxWithLabel {
     Rect rect;
     String label;

    public BoxWithLabel(Rect rect, String label) {
        this.rect = rect;
        this.label = label;
    }
}
