package in.ac.bits_pilani.goa.ard.activities;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.ac.bits_pilani.goa.ard.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertArrayEquals;

/**
 * UI tests for AuthActivity.
 * @author Rushikesh Jogdand
 */
@RunWith(AndroidJUnit4.class)
public class AuthActivityTest {
    @Rule
    public IntentsTestRule<AuthActivity> activityTestRule =
            new IntentsTestRule<>(AuthActivity.class);

    @Test
    public void testClassName() throws Exception {
        final String[] expected = new String[]{
                "AuthActivity",
        };
        assertArrayEquals("Class name error", expected,
                new String[]{activityTestRule.getActivity().getClass().getSimpleName()});
    }

    @Test
    public void testParentActivityName() throws Exception {
        final String[] expected = new String[]{
                "AppCompatActivity",
        };
        assertArrayEquals("Parent class is wrong", expected,
                new String[]{activityTestRule.getActivity()
                        .getClass().getSuperclass().getSimpleName()});
    }

    @Test
    public void testGoogleSignInExists() {
        onView(withId(R.id.btn_google_sign_in)).check(matches(isDisplayed()));
    }

    @Test
    public void testGoogleAccountChooserLaunches() {
        intending(anyIntent()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        onView(withId(R.id.btn_google_sign_in)).perform(click());
        intended(hasComponent("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"), times(1));
    }
}
