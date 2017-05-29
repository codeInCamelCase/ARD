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
 * NavigationDrawerListener Class
 * reflects changes from firebase regarding nav drawer header
 *
 * @author rushi
 */

public class NavigationDrawerListener implements ValueEventListener {
    private final Context context;
    private final TextView navDrawerTitle;
    private final TextView navDrawerSubtitle;
    private final ImageView navDrawerImage;
    private final String TAG;

    public NavigationDrawerListener(
            Context context,
            TextView navDrawerTitle,
            TextView navDrawerSubtitle,
            ImageView navDrawerImage,
            String TAG) {
        this.context = context;
        this.navDrawerTitle = navDrawerTitle;
        this.navDrawerSubtitle = navDrawerSubtitle;
        this.navDrawerImage = navDrawerImage;
        this.TAG = TAG + " NavigationDrawerListener";
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.child(AHC.FDR_NAV_DRAWER_TITLE).exists()) {
            String navDrawerTitleText = dataSnapshot
                    .child(AHC.FDR_NAV_DRAWER_TITLE)
                    .getValue(String.class);
            if (!Objects.equals(navDrawerTitleText, MainActivity.navDrawerTitleText)) {
                Log.d(TAG, "onDataChange: navDrawerTitle");
                navDrawerTitle.setText(navDrawerTitleText);
                MainActivity.navDrawerTitleText = navDrawerTitleText;
            }
        }
        if (dataSnapshot.child(AHC.FDR_NAV_DRAWER_SUBTITLE).exists()) {
            String navDrawerSubtitleText = dataSnapshot
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
            ArrayList<String> navDrawerImageList = new ArrayList<>();
            for (DataSnapshot childSnapshot : dataSnapshot
                    .child(AHC.FDR_NAV_DRAWER_IMAGE_LIST)
                    .getChildren()
                    ) {
                navDrawerImageList.add(childSnapshot.getValue(String.class));
            }
            if (!Objects.equals(navDrawerImageList, MainActivity.navDrawerImageList)) {
                Log.d(TAG, "onDataChange: navDrawerImageList");
                Random rand = new Random();
                String navDrawerImageURL = navDrawerImageList
                        .get(rand.nextInt(navDrawerImageList.size()));
                RequestOptions navDrawerImageOptions = new RequestOptions()
                        .placeholder(context.getDrawable(R.drawable.nav_drawer_default_image));
                try {
                    Glide.with(context)
                            .load(navDrawerImageURL)
                            .transition(DrawableTransitionOptions.withCrossFade()
                                    .crossFade(MainActivity.navDrawerImgAnimDur)
                            )
                            .apply(navDrawerImageOptions)
                            .into(navDrawerImage);
                } catch (Exception e) {
                    navDrawerImage.setImageDrawable(context.getDrawable(R.drawable.nav_drawer_default_image));
                    Log.e(TAG, e.toString());
                }
                MainActivity.navDrawerImageURL = navDrawerImageURL;
                MainActivity.navDrawerImageList = navDrawerImageList;
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e(TAG, databaseError.toString());
    }
}
