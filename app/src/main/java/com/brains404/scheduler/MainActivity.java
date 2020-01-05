package com.brains404.scheduler;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.brains404.scheduler.ui.settings.SettingsActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;


import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    GoogleSignInClient mGoogleSignInClient;
    final int RC_SIGN_IN=404;
    GoogleSignInAccount acct;
    SignInButton signInButton;
    TextView email ;
    TextView username ;
    ImageView profileImage;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("PrefsTheme", MODE_PRIVATE);

        boolean useDarkTheme = preferences.getBoolean("darkTheme", false);
        if (useDarkTheme) {
            setTheme(R.style.DarkAppTheme_NoActionBar);
        }

        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Access to Navigation View drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        signInButton = headerLayout.findViewById(R.id.sign_in_button);
         email = headerLayout.findViewById(R.id.tv_user_email);
         username = headerLayout.findViewById(R.id.tv_username);
         profileImage = headerLayout.findViewById(R.id.iv_profile);
        // Sign in Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                 acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if (acct == null) {

                    signIn();
                }else {

                    signOut();
                }
            }
        });

        Menu menu = navigationView.getMenu();

        MenuItem tools = menu.findItem(R.id.more_drawer_title);
        SpannableString s = new SpannableString(tools.getTitle());
        // change Title Drawer depends n Current Theme(Light,Dark)

        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearanceTitleDrawer), 0, s.length(), 0);

        tools.setTitle(s);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                //TODO Add id nav_item
                R.id.nav_time_table,
                R.id.nav_tasks)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }
    // Switch between Fragments
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.switch_fragment) {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingIntent);
            return true;
            //TODO Switch between Task fragment and timeTableFragment
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Rotation", "Landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("Rotation", "Portrait");

        }
    }
    // Google Sign in methods
    @Override
    protected void onStart() {
        super.onStart();
         acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {

            updateUI(acct);
        }else{
            updateUI(null);
        }

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("test", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public void updateUI(GoogleSignInAccount acct){
        if (acct!=null) {
           // Toast.makeText(getApplicationContext(),getResources().getString(R.string.google_account_connected),Toast.LENGTH_LONG).show();
            setGooglePlusButtonText(signInButton,getResources().getString(R.string.google_sign_out));


            email.setText(acct.getEmail());

            username.setText(String.format("%s %s", acct.getGivenName(), acct.getFamilyName()));



             // Log.d("img",acct.getPhotoUrl()+"");
            //Draw profileImage
           Picasso.with(this).load(acct.getPhotoUrl()).fit().placeholder(R.drawable.logo).into(profileImage);

        }else{
          //  Toast.makeText(getApplicationContext(),getResources().getString(R.string.google_account_disconnected),Toast.LENGTH_LONG).show();
            setGooglePlusButtonText(signInButton,getResources().getString(R.string.google_sign_in));

            username.setText(getResources().getString(R.string.nav_header_title));
            email.setText(getResources().getString(R.string.nav_header_subtitle));
           profileImage.setImageResource(R.drawable.logo);

        }
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and alter text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }





}
