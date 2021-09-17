package com.example.petowner;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainFragment.OnFragmentItemSelectedListener{

    TextView verifyEmailMsg;
    Button verifyEmailbtn, btnlogout;
    FirebaseAuth auth;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private String profileID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //Load default fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer,new MainFragment());
        fragmentTransaction.commit();


        // For Action Bar
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


        auth = FirebaseAuth.getInstance();

        //Button for Main Fragment Right now
        verifyEmailMsg = findViewById(R.id.verifyEmailMsg);
        verifyEmailbtn = findViewById(R.id.verifyEmailbtn);

        if(!auth.getCurrentUser().isEmailVerified()){
            verifyEmailbtn.setVisibility(View.VISIBLE);
            verifyEmailMsg.setVisibility(View.VISIBLE);
        }

        //Load default fragment(MainFragment)
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer,new MainFragment());
        fragmentTransaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.home){
            //Load main fragment
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer,new MainFragment());
            fragmentTransaction.commit();


        }
        if(item.getItemId() == R.id.profile){
            //profile fragment
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer,new ProfileFragment());
            fragmentTransaction.commit();


        }
        if(item.getItemId() == R.id.notification){
            //notifications fragment
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer,new NotificationFragment());
            fragmentTransaction.commit();
        }

        if(item.getItemId() == R.id.search){
            // search fragment
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer,new SearchFragment());
            fragmentTransaction.commit();
        }

        if(item.getItemId() == R.id.message){
            //message fragment
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer,new MessageFragment());
            fragmentTransaction.commit();

        }
        if(item.getItemId() == R.id.payment){
            //payment fragment
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer,new PaymentFragment());
            fragmentTransaction.commit();

        }
        if(item.getItemId() == R.id.logout){
            //logout fragment
            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginActivity);
            finish();

        }

        drawerLayout.closeDrawer(GravityCompat.START); //For closing the drawer
        return true;
    }

    @Override
    public void onEmailverifyBtnSelected() {
        //This verifyEmail in the MainFragment
        verifyEmailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send verification message
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                        verifyEmailbtn.setVisibility(View.GONE);
                        verifyEmailMsg.setVisibility(View.GONE);
                    }
                });
            }
        });

        // user will get redirected
        Bundle intent = getIntent().getExtras();
        if (intent != null){
            String profileID = intent.getString("currentUserId");
            /* adding it to shared preferences as it is the only way we can transfer our
               data from a activity to a fragment on that same activity. */
            getSharedPreferences("CurrentUserId", MODE_PRIVATE).edit().putString("currentUserId", profileID).apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.profile , new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.search , new SearchFragment()).commit();
        }
    }

    @Override
    public void onEmailverifyMsgSelected() {
      // It's just a text/msg in MainFragment

    }

    /* Search Fragment */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.fragment_search_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportFragmentManager().beginTransaction().replace(R.id.search,new SearchFragment()).commit();
    }
}