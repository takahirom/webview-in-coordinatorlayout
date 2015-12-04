package com.github.takahirom.webview_in_coodinator_layout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

public class ScrollBehavior extends CoordinatorLayout.Behavior<View> {
    private int mLayoutHeight;
    private ViewOffsetHelper mViewOffsetHelper;

    public ScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        // We depend on any AppBarLayouts
        return dependency instanceof AppBarLayout || dependency instanceof LinearLayout;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);

        if (mViewOffsetHelper == null) {
            mViewOffsetHelper = new ViewOffsetHelper(child);
        }
        mViewOffsetHelper.onViewLayout();

        // Now offset us correctly to be in the correct position. This is important for things
        // like activity transitions which rely on accurate positioning after the first layout.
        final List<View> dependencies = parent.getDependencies(child);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof LinearLayout) {
                mLayoutHeight = view.getHeight();
                mViewOffsetHelper.setTopAndBottomOffset(mLayoutHeight);
            }
//            if (updateOffset(parent, child, view)) {
//                // If we updated the offset, break out of the loop now
//                break;
//            }
        }

        Log.d("ScrollBehavior", "child.getTop():" + child.getTop());
        Log.d("ScrollBehavior", "child.getBottom():" + child.getBottom());
        return true;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child,
                                          View dependency) {
        updateOffset(parent, child, dependency);
        return false;
    }

    private boolean updateOffset(CoordinatorLayout parent, View child, View dependency) {
        final CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {
            float topAndBottomOffset = ((AppBarLayout.Behavior) behavior).getTopAndBottomOffset();
            mViewOffsetHelper.setTopAndBottomOffset((int) topAndBottomOffset);
        } else {
            float translationY = dependency.getTranslationY();
            Log.d("ScrollBehavior", "translationY:" + translationY);
            mViewOffsetHelper.setTopAndBottomOffset((int) translationY);
        }
        return false;
    }
}