package be.kdg.teame.kandoe.profile;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ProfileScreenTest {

    @Rule
    public ActivityTestRule<ProfileActivity> mActivityRule = new ActivityTestRule<>(ProfileActivity.class);

    @Before
    public void setUp() {
        mActivityRule.getActivity().getPreferences(Context.MODE_PRIVATE).edit().clear().apply();
    }

    @Test
    public void testAuthenticatedRetrieveUserdata() {


    }
}
