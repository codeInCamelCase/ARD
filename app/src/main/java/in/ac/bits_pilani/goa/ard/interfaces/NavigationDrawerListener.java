package in.ac.bits_pilani.goa.ard.interfaces;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import in.ac.bits_pilani.goa.ard.R;
import in.ac.bits_pilani.goa.ard.activities.MainActivity;
import in.ac.bits_pilani.goa.ard.utils.AHC;

/**
 * NavigationDrawerListener Class.
 * reflects changes from firebase regarding nav drawer header
 *
 * @author Rushikesh Jogdand
 */

public class NavigationDrawerListener implements ValueEventListener {

    /**
     * Context required for glide image loading.
     */
    private final Context context;

    /**
     * Textview corresponding to nav drawer title.
     */
    private final TextView navDrawerTitle;

    /**
     * Textview corresponding to nav drawer subtitle.
     */
    private final TextView navDrawerSubtitle;

    /**
     * ImageView corresponding to nav drawer header background.
     */
    private final ImageView navDrawerImage;

    /**
     * TAG for methods in this class.
     */
    private final String TAG;

    /**
     * Constructor with all fields required.
     */
    public NavigationDrawerListener(
            final Context context,
            final TextView navDrawerTitle,
            final TextView navDrawerSubtitle,
            final ImageView navDrawerImage,
            final String tag) {
        this.context = context;
        this.navDrawerTitle = navDrawerTitle;
        this.navDrawerSubtitle = navDrawerSubtitle;
        this.navDrawerImage = navDrawerImage;
        this.TAG = tag + " NavigationDrawerListener";
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        if (dataSnapshot.child(AHC.FDR_NAV_DRAWER_TITLE).exists()) {
            final String navDrawerTitleText = dataSnapshot
                    .child(AHC.FDR_NAV_DRAWER_TITLE)
                    .getValue(String.class);

            if (!Objects.equals(navDrawerTitleText, MainActivity.navDrawerTitleText)) {
                Log.d(TAG, "onDataChange: navDrawerTitle");
                navDrawerTitle.setText(navDrawerTitleText);
                MainActivity.navDrawerTitleText = navDrawerTitleText;
            }
        }
        if (dataSnapshot.child(AHC.FDR_NAV_DRAWER_SUBTITLE).exists()) {
            final String navDrawerSubtitleText = dataSnapshot
                    .child(AHC.FDR_NAV_DRAWER_SUBTITLE)
                    .getValue(String.class);

            if (!Objects.equals(navDrawerSubtitleText, MainActivity.navDrawerSubtitleText)) {
                Log.d(TAG, "onDataChange: navDrawerSubtitle");
                navDrawerSubtitle.setText(navDrawerSubtitleText);
                MainActivity.navDrawerSubtitleText = navDrawerSubtitleText;
            }
        }
        if (dataSnapshot.child(AHC.FDR_NAV_DRAWER_IMAGE_LIST).exists()
                && dataSnapshot.child(AHC.FDR_NAV_DRAWER_IMAGE_LIST).getChildrenCount() > 0) {
            final ArrayList<String> navDrawerImageList = new ArrayList<>();
            for (final DataSnapshot childSnapshot
                    : dataSnapshot.child(AHC.FDR_NAV_DRAWER_IMAGE_LIST).getChildren())
                navDrawerImageList.add(childSnapshot.getValue(String.class));

            if (!Objects.equals(navDrawerImageList, MainActivity.navDrawerImageList)) {
                Log.d(TAG, "onDataChange: navDrawerImageList");
                final Random rand = new Random();
                final String navDrawerImageURL = navDrawerImageList
                        .get(rand.nextInt(navDrawerImageList.size()));
                final RequestOptions navDrawerImageOptions = new RequestOptions()
                        .placeholder(context.getDrawable(R.drawable.nav_drawer_default_image));

                Glide.with(context)
                        .load(navDrawerImageURL)
                        .transition(DrawableTransitionOptions.withCrossFade()
                                .crossFade(MainActivity.navDrawerAnimationDuration)
                        )
                        .apply(navDrawerImageOptions)
                        .into(navDrawerImage);

                MainActivity.navDrawerImageURL = navDrawerImageURL;
                MainActivity.navDrawerImageList = navDrawerImageList;
            }
        }
    }

    @Override
    public void onCancelled(final DatabaseError databaseError) {
        Log.e(TAG, databaseError.toString());
    }
}
