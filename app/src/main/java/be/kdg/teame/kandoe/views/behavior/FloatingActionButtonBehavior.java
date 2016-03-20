package be.kdg.teame.kandoe.views.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * This class implements custom behavior for a {@link FloatingActionButton}.
 * When scrolling down, the {@link FloatingActionButton} disappears.
 *
 * @see <a href="http://androidessence.com/hide-the-floatingactionbutton-when-scrolling-a-recyclerview/">Android Essence</a>
 * */
public class FloatingActionButtonBehavior extends FloatingActionButton.Behavior {

    /**
     * Default constructor needed otherwise the application will crash
     */
    public FloatingActionButtonBehavior(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public void onNestedScroll(
            CoordinatorLayout coordinatorLayout,
            FloatingActionButton child,
            View target,
            int dxConsumed,
            int dyConsumed,
            int dxUnconsumed,
            int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.hide();
        } else if (dyConsumed < 0 && child.getVisibility() == View.GONE) {
            child.show();
        }
    }

    @Override
    public boolean onStartNestedScroll(
            CoordinatorLayout coordinatorLayout,
            FloatingActionButton child,
            View directTargetChild,
            View target,
            int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
