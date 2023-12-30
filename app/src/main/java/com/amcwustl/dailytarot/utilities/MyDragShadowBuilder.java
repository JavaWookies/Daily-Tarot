package com.amcwustl.dailytarot.utilities;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;

public class MyDragShadowBuilder extends View.DragShadowBuilder {

    // The drag shadow image, defined as a drawable object.
    private final Drawable shadow;
    private int width;
    private int height;

    // Constructor.
    public MyDragShadowBuilder(View view, Drawable cardDrawable, int width, int height) {
        super(view);

        this.shadow = cardDrawable;
        this.width = width;
        this.height = height;

        shadow.setBounds(0, 0, width, height);
    }

    // Define a callback that sends the drag shadow dimensions and touch point back to the system.
    @Override
    public void onProvideShadowMetrics(Point size, Point touch) {
        size.set(width, height);
        touch.set(width / 2, height / 2);
    }

    // Define a callback that draws the drag shadow in a Canvas that the system constructs.
    @Override
    public void onDrawShadow(Canvas canvas) {
        // Draw the Drawable (card image) on the Canvas passed in from the system.
        shadow.draw(canvas);
    }
}
