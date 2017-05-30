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


public class AboutMAC extends AppCompatActivity {

    @BindView(R.id.about_mac_text) TextView textView;
    @BindView(R.id.about_mac_image) ImageView imageView;
    @BindView(R.id.toolbar_about_mac) Toolbar toolbar;
    DatabaseReference databaseReference;
    ValueEventListener listener;
    SharedPreferences sharedPreferences;
    String html;
    String url;
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
        sharedPreferences=getSharedPreferences("aboutMAC", Context.MODE_PRIVATE);
        try{
            html=sharedPreferences.getString("html","");
            url=sharedPreferences.getString("imageUrl","");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
            } else {
                textView.setText(Html.fromHtml(html));
            }
            Glide.with(getApplicationContext()).load(url)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }catch (Exception e){
            Log.e(AHC.TAG,e.toString());
        }
        databaseReference= FirebaseDatabase.getInstance().getReference().child(AHC.FDR_ABOUT_MAC);
        listener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                html=dataSnapshot.child("html").getValue(String.class);
                url=dataSnapshot.child("imageUrl").getValue(String.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    textView.setText(Html.fromHtml(html));
                }
                Glide.with(getApplicationContext()).load(url)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("html",html);
                editor.putString("imageUrl",url);
                editor.apply();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(AHC.TAG,databaseError.toString());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(listener);
    }


}

