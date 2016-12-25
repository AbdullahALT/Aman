package com.amanapp.application.activities.elements;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Abdullah ALT on 8/22/2016.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;

    public DividerItemDecoration(Context context, int resId) {
        this.divider = ContextCompat.getDrawable(context, resId);
    }

//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//        if (parent.getChildAdapterPosition(view) != 0) {
////            outRect.top = divider.getIntrinsicHeight();
//        }
//    }

//    @Override
//    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
////        super.onDraw(c, parent, state);
//        int start = parent.getPaddingLeft();
//        int end = parent.getWidth() - parent.getPaddingRight();
//
//        for(int i = 0 ; i < parent.getChildCount() - 1; i++){
//            View child = parent.getChildAt(i);
//
//            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//
//            int top = child.getBottom() + params.bottomMargin;
//            int bottom = top + divider.getIntrinsicHeight();
//
//            divider.setBounds(start, top, end, bottom);
//            divider.draw(c);
//        }
//    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDrawOver(c, parent, state);
        int fileImageWidth = 56;
        int start = parent.getPaddingStart() + (int) (fileImageWidth * Resources.getSystem().getDisplayMetrics().density);
        int end = parent.getWidth() - parent.getPaddingEnd();

        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(start, top, end, bottom);
            divider.draw(c);
        }
    }
}
