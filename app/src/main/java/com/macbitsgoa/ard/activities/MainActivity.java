package com.macbitsgoa.ard.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.macbitsgoa.ard.R;
import com.macbitsgoa.ard.fragments.BaseFragment;
import com.macbitsgoa.ard.fragments.ChatFragment;
import com.macbitsgoa.ard.fragments.ForumFragment;
import com.macbitsgoa.ard.fragments.HomeFragment;
import com.macbitsgoa.ard.interfaces.ChatFragmentListener;
import com.macbitsgoa.ard.interfaces.ForumFragmentListener;
import com.macbitsgoa.ard.keys.AuthActivityKeys;
import com.macbitsgoa.ard.services.MessagingService;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main activity of app.
 *
 * @author Vikramaditya Kukreja
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        ForumFragmentListener,
        ChatFragmentListener {

    /**
     * Bottom navigation view.
     */
    @BindView(R.id.bottom_nav_activity_main)
    BottomNavigationView bottomNavigationView;

    /**
     * MainActivity Sections.
     */
    enum MaSection {
        FAQ, HOME, CHAT;
    }

    /**
     * Key {@link MainActivity.MaSection}
     * Value : Access order (lower is more recent).
     */
    HashMap<MaSection, Integer> sectionsHistory = new HashMap<>();

    private static MaSection currentSection = MaSection.HOME;

    /**
     * Fragment manager used to handle the 3 fragments.
     */
    private FragmentManager fragmentManager;

    /**
     * ForumFragment object.
     */
    private ForumFragment forumFragment;

    /**
     * HomeFragment object.
     */
    private HomeFragment homeFragment;

    /**
     * ChatFragment object.
     */
    private ChatFragment chatFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        //Check if authorised
        if (!auth(getIntent())) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        startService(new Intent(this, MessagingService.class));
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    /**
     * Start {@link AuthActivity} if Firebase user object is null.
     * This also closes the current {@link MainActivity} before launching Auth.
     *
     * @param intent Intent object. Should not be null. See <b>MainActivityTest</b>.
     * @return boolean true if auth is successful, false otherwise.
     */
    public boolean auth(@NonNull final Intent intent) {
        return !intent.getBooleanExtra(AuthActivityKeys.USE_DEFAULT, true)
                || FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    /**
     * All initialisation are done here.
     */
    private void init() {
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        forumFragment = ForumFragment.newInstance(getString(R.string.bottom_nav_forum_activity_main));
        homeFragment = HomeFragment.newInstance(null);
        chatFragment = ChatFragment.newInstance(getString(R.string.bottom_nav_chat_activity_main));

        launchFragment(currentSection);
        final int menuId;
        if (currentSection == MaSection.FAQ) menuId = R.id.bottom_nav_forum;
        else if (currentSection == MaSection.HOME) menuId = R.id.bottom_nav_home;
        else menuId = R.id.bottom_nav_chat;
        bottomNavigationView.setSelectedItemId(menuId);
        bottomNavigationView.getMenu().findItem(menuId).setChecked(true);
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.bottom_nav_forum) {
            launchFragment(MaSection.FAQ);
        } else if (id == R.id.bottom_nav_home) {
            launchFragment(MaSection.HOME);
            homeFragment.scrollToTop();
        } else {
            launchFragment(MaSection.CHAT);
        }
        return true;
    }

    private void launchFragment(@NonNull MaSection section) {
        for (Map.Entry<MaSection, Integer> entry : sectionsHistory.entrySet()) {
            entry.setValue(entry.getValue() + 1);
        }
        sectionsHistory.put(currentSection, 0);

        currentSection = section;
        sectionsHistory.remove(currentSection);

        BaseFragment baseFragment;
        if (currentSection == MaSection.FAQ) baseFragment = forumFragment;
        else if (currentSection == MaSection.HOME) baseFragment = homeFragment;
        else baseFragment = chatFragment;
        fragmentManager.beginTransaction().replace(R.id.frame_content_main, baseFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (sectionsHistory.isEmpty()) {
            // Home should be last section before exit.
            if (currentSection == MaSection.HOME) {
                finish();
                return;
            } else {
                sectionsHistory.put(MaSection.HOME, 0);
            }
        }

        Map.Entry<MaSection, Integer> e = sectionsHistory.entrySet().iterator().next();
        int minVal = e.getValue();
        MaSection lastSection = e.getKey();

        for (Map.Entry<MaSection, Integer> entry : sectionsHistory.entrySet()) {
            if (entry.getValue() < minVal) {
                minVal = entry.getValue();
                lastSection = entry.getKey();
            }
        }

        MaSection staleSection = currentSection;

        launchFragment(lastSection);
        final int menuId;
        if (currentSection == MaSection.FAQ) menuId = R.id.bottom_nav_forum;
        else if (currentSection == MaSection.HOME) menuId = R.id.bottom_nav_home;
        else menuId = R.id.bottom_nav_chat;
        bottomNavigationView.setSelectedItemId(menuId);
        bottomNavigationView.getMenu().findItem(menuId).setChecked(true);

        sectionsHistory.remove(staleSection);
    }

    @Override
    public void updateChatFragment() {

    }

    @Override
    public void updateForumFragment() {

    }
}
