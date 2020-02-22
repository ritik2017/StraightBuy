package in.straightbuy.www.straightbuy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;

    public static int k=1;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                    if(k == 1){
                        k++;
                        Intent intent = new Intent(MainActivity.this , ProductPageActivity.class);
                        startActivity(intent);
                    }
                    else {
                        finish();
                    }

            } else {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                                .build(),
                        RC_SIGN_IN);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        k=1;

        mFirebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                startActivity(intent);
                Toast.makeText(this, "SIGNING YOU IN", Toast.LENGTH_SHORT).show();

            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "SIGN IN CANCELLED", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
}
