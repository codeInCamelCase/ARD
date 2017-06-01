package in.ac.bits_pilani.goa.ard.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ac.bits_pilani.goa.ard.R;
import in.ac.bits_pilani.goa.ard.utils.AHC;

/**
 * AboutMac class.
 * @author Aayush
 * @version 1.0.
 */

public class AboutMACActivity extends AppCompatActivity {

    /**
     * About Mac Text.
     */
    @BindView(R.id.about_mac_text)
    TextView textView;

    /**
     * About Mac Image.
     */
    @BindView(R.id.about_mac_image)
    ImageView imageView;

    /**
     * About Mac Toolbar.
     */
    @BindView(R.id.toolbar_about_mac)
    Toolbar toolbar;

    /**
     * Database Reference.
     */
    DatabaseReference databaseReference;

    /**
     * Vale event listener.
     */
    ValueEventListener listener;

    /**
     * Sharedpreferences.
     */
    SharedPreferences sharedPreferences;

    /**
     * String html.
     */
    String html;

    /**
     * String url.
     */
    String url;

    /**
     * Tag for about mac activity.
     */
    private final String TAG = AHC.SHORT_TAG + ".activities." + getClass().getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_mac);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("aboutMAC", Context.MODE_PRIVATE);
        html = sharedPreferences.getString(AHC.HTML_ABOUT_MAC, "");
        url = sharedPreferences.getString(AHC.IMAGEURL_ABOUT_MAC, "");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(html));
        }
        Glide.with(getApplicationContext()).load(url)
                .thumbnail(AHC.MAGICNUMBER_ABOUT_MAC)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(AHC.FDR_ABOUT_MAC);
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                html = dataSnapshot.child(AHC.HTML_ABOUT_MAC).getValue(String.class);
                url = dataSnapshot.child(AHC.IMAGEURL_ABOUT_MAC).getValue(String.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    textView.setText(Html.fromHtml(html));
                }
                Glide.with(getApplicationContext()).load(url)
                        .thumbnail(AHC.MAGICNUMBER_ABOUT_MAC)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(AHC.HTML_ABOUT_MAC, html);
                editor.putString(AHC.IMAGEURL_ABOUT_MAC, url);
                editor.apply();
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(listener);
    }

}
