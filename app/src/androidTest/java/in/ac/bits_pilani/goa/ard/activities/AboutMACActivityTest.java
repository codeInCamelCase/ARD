package in.ac.bits_pilani.goa.ard.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import in.ac.bits_pilani.goa.ard.R;
import in.ac.bits_pilani.goa.ard.utils.AHC;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Aayush on 1/6/17.
 * Test for AboutMAC activity.
 */

@RunWith(AndroidJUnit4.class)
public class AboutMACActivityTest {

    @Mock
    private Context context;

    @Rule
    public ActivityTestRule<AboutMACActivity> activityTestRule =
            new ActivityTestRule<AboutMACActivity>(AboutMACActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    InstrumentationRegistry.getTargetContext();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    return intent;
                }
            };


    @Before
    public void init() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void ensureIntentDataIsDisplayed() throws Exception {
        AboutMACActivity activity = activityTestRule.getActivity();
        assertNotNull(activity);
    }

    @Test
    public void testClassName() throws Exception {
        final String[] expected = new String[]{
                "AboutMACActivity",
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
    public void testImageView() throws Exception {
        ImageView imageView = (ImageView) activityTestRule.getActivity().findViewById(R.id.about_mac_image);
        assertNotNull(imageView);
    }

    @Test
    public void testTextView() throws Exception {
        TextView textView = (TextView) activityTestRule.getActivity().findViewById(R.id.about_mac_text);
        assertNotNull(textView);
    }

    @Test
    public void testToolbar() throws Exception {
        Toolbar toolbar = (Toolbar) activityTestRule.getActivity().findViewById(R.id.toolbar_about_mac);
        assertNotNull(toolbar);
    }

    @Test
    public void onStart() throws Exception {
        DatabaseReference databasereference = FirebaseDatabase.getInstance().getReference().child(AHC.FDR_ABOUT_MAC);
        assertNotNull(databasereference);
        ContextWrapper contextWrapper = new ContextWrapper(context);
        SharedPreferences sharedPreferences = contextWrapper.getSharedPreferences("aboutMAC", Context.MODE_PRIVATE);
        String html = sharedPreferences.getString(AHC.HTML_ABOUT_MAC, "");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            assertNotNull(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            assertNotNull(Html.fromHtml(html));
        }
    }

}
