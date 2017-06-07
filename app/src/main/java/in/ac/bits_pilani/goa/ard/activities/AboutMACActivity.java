package in.ac.bits_pilani.goa.ard.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    @BindView(R.id.about_mac_desc)
    TextView textView_about_mac;

    /**
     * About Mac Image.
     */
    @BindView(R.id.about_mac_image)
    ImageView imageView_about_mac;

    /**
     * About Mac Toolbar.
     */
    @BindView(R.id.toolbar_about_mac)
    Toolbar toolbar_about_mac;

    /**
     * Database Reference.
     */
    private DatabaseReference databaseReference;

    /**
     * Valueeventlistener for databasereference.
     */
    private ValueEventListener listener;

    /**
     * Sharedpreferences.
     */
    private SharedPreferences sharedPreferences;

    /**
     * String text to be displayed.
     */
    private String text;

    /**
     * String url for imageurl.
     */
    private String url;

    /**
     * Tag for about mac activity.
     */
    private final String TAG = AHC.TAG + ".activities." + getClass().getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_mac);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_about_mac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences(AHC.USER_PREFERENCES, Context.MODE_PRIVATE);
        text = sharedPreferences.getString(AHC.TEXT_ABOUT_MAC, getString(R.string.empty_about_mac));
        url = sharedPreferences.getString(AHC.IMAGEURL_ABOUT_MAC, getString(R.string.empty_about_mac));
        if (getString(R.string.empty_about_mac).equals(text)) {
            textView_about_mac.setText(getString(R.string.initial_text_about_mac));
        } else {
            textView_about_mac.setText(text);
            Glide.with(getApplicationContext()).load(url)
                    .thumbnail(AHC.GLIDE_THUMBNAIL_VALUE)
                    .into(imageView_about_mac);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child(AHC.FDR_ABOUT_MAC);
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                text = dataSnapshot.child(AHC.TEXT_ABOUT_MAC).getValue(String.class);
                url = dataSnapshot.child(AHC.IMAGEURL_ABOUT_MAC).getValue(String.class);
                textView_about_mac.setText(text);
                Glide.with(getApplicationContext()).load(url)
                        .thumbnail(AHC.GLIDE_THUMBNAIL_VALUE)
                        .into(imageView_about_mac);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(AHC.TEXT_ABOUT_MAC, text);
                editor.putString(AHC.IMAGEURL_ABOUT_MAC, url);
                editor.apply();
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(listener);
    }

}
