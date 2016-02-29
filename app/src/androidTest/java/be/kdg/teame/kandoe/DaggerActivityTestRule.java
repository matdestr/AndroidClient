package be.kdg.teame.kandoe;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

/**
 * Custom Activity test rule which can be used for test dependency injection using Dagger
 * */
public class DaggerActivityTestRule<T extends Activity> extends ActivityTestRule<T> {
    private OnBeforeActivityLaunchedListener<T> mListener;
    private AfterActivityLaunchedListener<T> afterActivityLaunchedListener;

    public DaggerActivityTestRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity,
                                  @NonNull AfterActivityLaunchedListener<T> listener) {
        super(activityClass, initialTouchMode, launchActivity);
        this.afterActivityLaunchedListener = listener;
    }

    public DaggerActivityTestRule(Class<T> activityClass, @NonNull OnBeforeActivityLaunchedListener<T> listener) {
        this(activityClass, false, listener);
    }

    public DaggerActivityTestRule(Class<T> activityClass, boolean initialTouchMode,
                                  @NonNull OnBeforeActivityLaunchedListener<T> listener) {
        this(activityClass, initialTouchMode, true, listener);
    }

    public DaggerActivityTestRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity,
                                  @NonNull OnBeforeActivityLaunchedListener<T> listener) {
        super(activityClass, initialTouchMode, launchActivity);
        this.mListener = listener;
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();

        if (mListener != null)
            mListener.beforeActivityLaunched(
                    (Application) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext(),
                    getActivity());
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        if (afterActivityLaunchedListener == null)
            return;

        afterActivityLaunchedListener.afterActivityLaunched(
                (Application) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext(),
                getActivity()
        );
    }

    public interface OnBeforeActivityLaunchedListener<T> {
        void beforeActivityLaunched(@NonNull Application application, @NonNull T activity);
    }

    public interface AfterActivityLaunchedListener<T> {
        void afterActivityLaunched(@NonNull Application application, @NonNull T activity);
    }
}
