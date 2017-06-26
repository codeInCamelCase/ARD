package in.ac.bits_pilani.goa.ard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ac.bits_pilani.goa.ard.R;
import in.ac.bits_pilani.goa.ard.utils.AHC;

import static in.ac.bits_pilani.goa.ard.utils.AHC.FDR_USERS_EMAIL;
import static in.ac.bits_pilani.goa.ard.utils.AHC.FDR_USERS_NAME;
import static in.ac.bits_pilani.goa.ard.utils.AHC.FDR_USERS_PHOTO_URL;

/**
 * AuthActivity authenticates user.
 * Currently implemented:
 *      1. Sign in with Google {@link #signInToGoogle()}
 * @author Rushikesh Jogdand
 */
public class AuthActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    /**
     * Request code for google sign in.
     */
    private static final int RC_GOOGLE_SIGN_IN = 17;

    /**
     * Google Sign In Button.
     */
    @BindView(R.id.btn_google_sign_in)
    Button googleSignInButton;

    /**
     * Tag for this activity.
     */
    private final String TAG = AHC.TAG + ".activities." + getClass().getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        googleSignInButton.setOnClickListener(this);
    }

    /**
     * Upon successful authentication, save relevant user info (name, email, photoUrl)
     * to firebase.
     */
    public void updateSelfInfo() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        final String uid = user.getUid();
        final String name = user.getDisplayName();
        final String email = user.getEmail();
        String photoUrl = null;
        try {
            // NullPointerException is caught in `catch`.
            //noinspection ConstantConditions
            photoUrl = user.getPhotoUrl().toString();
        } catch (final NullPointerException e) {
            Log.e(TAG, "updateSelfInfo: ", e);
        }
        final DatabaseReference userDb = FirebaseDatabase.getInstance().getReference(AHC.FDR_USERS).child(uid);
        if (name != null && name.length() > 0) {
            userDb.child(FDR_USERS_NAME).setValue(name);
        }
        if (email != null && email.length() > 0) {
            userDb.child(FDR_USERS_EMAIL).setValue(email);
        }
        if (photoUrl != null && photoUrl.length() > 0) {
            userDb.child(FDR_USERS_PHOTO_URL).setValue(photoUrl);
        }
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btn_google_sign_in) {
            googleSignInButton.setClickable(false);
            signInToGoogle();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: " + connectionResult);
        showGoogleSignInError();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            validateGoogleSignIn(data);
        }
    }

    // EVERYTHING RELATED TO GOOGLE SIGN IN

    /**
     * Send user to google sign in.
     */
    private void signInToGoogle() {
        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        final GoogleApiClient apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        final Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(googleSignInIntent, RC_GOOGLE_SIGN_IN);
    }

    /**
     * See if there is any error for google sign in.
     *
     * @param data {@link Intent} received from googleSignInIntent (See {@link #signInToGoogle()})
     */
    private void validateGoogleSignIn(final Intent data) {
        if (data == null) {
            Log.e(TAG, "validateGoogleSignIn: received null intent");
            showGoogleSignInError();
            return;
        }
        final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (!result.isSuccess()) {
            Log.e(TAG, "validateGoogleSignIn: Google sign in unsuccessful " + result.getStatus());
            showGoogleSignInError();
            return;
        }
        final GoogleSignInAccount account = result.getSignInAccount();
        if (account == null) {
            Log.e(TAG, "validateGoogleSignIn: got null googleSignInAccount" + result.getStatus());
            showGoogleSignInError();
            return;
        }
        authWithGoogle(account);
    }

    /**
     * Connect google account to firebase.
     *
     * @param account {@link GoogleSignInAccount}
     */
    private void authWithGoogle(final GoogleSignInAccount account) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        final OnCompleteListener<AuthResult> googleAuthListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "authWithGoogle-onComplete: task unsuccessful", task.getException());
                    showGoogleSignInError();
                    return;
                }
                updateSelfInfo();
                AuthActivity.this.finish();
            }
        };
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(googleAuthListener);
    }

    /**
     * Default response that user will get if anything in google sign in fails.
     */
    private void showGoogleSignInError() {
        Toast.makeText(getApplicationContext(),
                getString(R.string.error_google_sign_in_failed),
                Toast.LENGTH_SHORT
        ).show();
        googleSignInButton.setClickable(true);
    }
}
