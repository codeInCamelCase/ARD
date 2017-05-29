package in.ac.bits_pilani.goa.ard.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ac.bits_pilani.goa.ard.R;
import in.ac.bits_pilani.goa.ard.interfaces.NavigationDrawerListener;
import in.ac.bits_pilani.goa.ard.utils.AHC;

/**
 * Main activity of app.
 * @author Vikramaditya Kukreja
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Toolbar for MainActivity.
     */
    @BindView(R.id.toolbar_activity_main)
    Toolbar toolbar;

    /**
     *  Fab button.
     */
    @BindView( R.id.fab)
    FloatingActionButton fab;

    /**
     * DrawerLayout for nav drawer.
     */
    @BindView( R.id.drawer_layout)
    DrawerLayout drawer;

    /**
     * Navigation view in drawer.
     */
    @BindView( R.id.nav_view)
    NavigationView navigationView;

    /**
     * Tag for this activity.
     */
    private final String TAG = AHC.TAG + ".activities." + getClass().getSimpleName();

    /**
     * Variables related to navigation drawer
     */
    public static String navDrawerTitleText = null;
    public static String navDrawerSubtitleText = null;
    public static ArrayList<String> navDrawerImageList = null;
    public static String navDrawerImageURL = null;
    private DatabaseReference navDrawerDBRef;
    private NavigationDrawerListener navDrawerListener;
    public static final int navDrawerImgAnimDur = 50;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navDrawerDBRef = FirebaseDatabase.getInstance().getReference(AHC.FDR_NAV_DRAWER);

        View headerView = navigationView.getHeaderView(0);
        ImageView navDrawerImage = ButterKnife.findById(headerView, R.id.nav_drawer_image);
        TextView navDrawerTitle = ButterKnife.findById(headerView, R.id.nav_drawer_title);
        TextView navDrawerSubtitle = ButterKnife.findById(headerView, R.id.nav_drawer_subtitle);

        if (navDrawerTitleText != null) {
            navDrawerTitle.setText(navDrawerTitleText);
        }

        if (navDrawerSubtitleText != null) {
            navDrawerSubtitle.setText(navDrawerSubtitleText);
        }

        if (navDrawerImageURL != null) {
            RequestOptions navDrawerImageOptions = new RequestOptions()
                    .placeholder(this.getDrawable(R.drawable.nav_drawer_default_image));
            try {
                Glide.with(this)
                        .load(navDrawerImageURL)
                        .transition(DrawableTransitionOptions.withCrossFade()
                                .crossFade(navDrawerImgAnimDur)
                        )
                        .apply(navDrawerImageOptions)
                        .into(navDrawerImage);
            } catch (Exception e) {
                navDrawerImage.setImageDrawable(this.getDrawable(R.drawable.nav_drawer_default_image));
                Log.e(TAG, e.toString());
            }
        }

        navDrawerListener = new NavigationDrawerListener(
                this,
                navDrawerTitle,
                navDrawerSubtitle,
                navDrawerImage,
                TAG);
        navDrawerDBRef.addValueEventListener(navDrawerListener);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onStop() {
        navDrawerDBRef.removeEventListener(navDrawerListener);
        super.onStop();
    }
}
