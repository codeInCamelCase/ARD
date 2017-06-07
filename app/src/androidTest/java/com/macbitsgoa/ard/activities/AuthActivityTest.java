package com.macbitsgoa.ard.activities;

import android.app.Instrumentation;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * UI tests for AuthActivity.
 * @author Rushikesh Jogdand
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class AuthActivityTest {

    @Rule
    public IntentsTestRule<AuthActivity> mTestRule =
            new IntentsTestRule<>(AuthActivity.class);

    @Test
    public void testGoogleAccountChooserLaunches() throws Exception {
        intending(anyIntent()).respondWith(new Instrumentation.ActivityResult(RESULT_CANCELED, null));
        mTestRule.getActivity().googleSignInButton.callOnClick();
//        onView(withId(R.id.btn_content_auth_google)).perform(click());
        intended(hasComponent("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"));
//        String errMsg = mTestRule.getActivity().getString(R.string.error_google_sign_in_failed);
//        onView(withText(errMsg))
//                .inRoot(withDecorView(not(is(mTestRule.getActivity().getWindow().getDecorView()))))
//                .check(matches(isDisplayed()));
    }

//    not required
    @Ignore
    @Test
    public void testOnActivityResultNullResponse() throws Exception {
        intending(anyIntent()).respondWith(new Instrumentation.ActivityResult(RESULT_OK, null));
//        onView(withId(R.id.btn_content_auth_google)).perform(click());
        mTestRule.getActivity().googleSignInButton.callOnClick();
        intended(hasComponent("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"));
        //Cannot check for multiple toasts in one activity test as mentioned here
        //https://stackoverflow.com/a/38379219/5262677
        /*onView(withText(R.string.error_google_sign_in_failed))
                .inRoot(withDecorView(not(is(mTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
                */
    }
/*
    public class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    //means this window isn't contained by any other windows.
                }
            }
            return false;
        }
    }
*/
}
