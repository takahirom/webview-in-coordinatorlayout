package com.github.takahirom.webview_in_coodinator_layout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class LinearLayoutBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    public LinearLayoutBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final LinearLayout child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        final CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();

        final int topAndBottomOffset = ((AppBarLayout.Behavior) behavior).getTopAndBottomOffset();
        Log.d("LinearLayoutBehavior", "topAndBottomOffset:" + topAndBottomOffset);
        Log.d("LinearLayoutBehavior", "ViewCompat.getTranslationY(dependency):" + ViewCompat.getTranslationY(dependency));
        child.setTranslationY(-topAndBottomOffset);
        return true;
//        final float toY = child.getTranslationY() - dy - mLayoutHeight;
//        if (0 < toY) {
//            child.setTranslationY(mLayoutHeight);
//        } else if (-mLayoutHeight > toY) {
//            child.setTranslationY(0);
//        } else {
//            child.setTranslationY(mLayoutHeight + toY);
//        }
    }

}