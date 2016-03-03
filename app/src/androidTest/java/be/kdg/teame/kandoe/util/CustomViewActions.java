package be.kdg.teame.kandoe.util;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.action.Swiper;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.view.View;
import android.view.ViewConfiguration;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Contains ViewActions which aren't included in the default espresso libraries.
 */
public class CustomViewActions {

    public static ViewAction swipeForTextToBeVisible(final String text, final long millis, final View navigationView) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "swipe for a specific view with text <" + text + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withText(text);

                do {
                    boolean returnAfterOneMoreSwipe = false; //not sure why I have to do one more swipe but it works

                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child) && child.getVisibility() == View.VISIBLE) {
                            returnAfterOneMoreSwipe = true;
                        }
                    }

                    CoordinatesProvider coordinatesProvider = new CoordinatesProvider() {
                        public float[] calculateCoordinates(View view) {
                            float[] xy = GeneralLocation.BOTTOM_CENTER.calculateCoordinates(view);
                            xy[0] += 0.0F * (float)view.getWidth();
                            xy[1] += -0.083F * (float)view.getHeight();
                            return xy;
                        }
                    };

                    //Below code is c/p from perform in android/support/test/espresso/action/GeneralSwipeAction.class
                    float[] startCoordinates = coordinatesProvider.calculateCoordinates(navigationView);
                    float[] endCoordinates = GeneralLocation.TOP_CENTER.calculateCoordinates(navigationView);
                    float[] precision = Press.FINGER.describePrecision();
                    Swiper.Status status = Swiper.Status.FAILURE;

                    for(int tries = 0; tries < 3 && status != Swiper.Status.SUCCESS; ++tries) {
                        try {
                            status = Swipe.SLOW.sendSwipe(uiController, startCoordinates, endCoordinates, precision);
                        } catch (RuntimeException var9) {
                            throw (new PerformException.Builder()).withActionDescription(this.getDescription()).withViewDescription(HumanReadables.describe(navigationView)).withCause(var9).build();
                        }

                        int duration = ViewConfiguration.getPressedStateDuration();
                        if(duration > 0) {
                            uiController.loopMainThreadForAtLeast((long)duration);
                        }
                    }
                    //End c/p from android/support/test/espresso/action/GeneralSwipeAction.class

                    if (returnAfterOneMoreSwipe) {
                        return;
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }
}
