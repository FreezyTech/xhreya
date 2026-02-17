package com.example.xhreya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeDashboardActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard);

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        // Hamburger icon
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Optional: change header text dynamically
        View headerView = navigationView.getHeaderView(0);
        TextView headerTitle = headerView.findViewById(R.id.headerTitle);
        TextView headerSubtitle = headerView.findViewById(R.id.headerSubtitle);
        headerTitle.setText("Freezy Subedi");
        headerSubtitle.setText("Stay Safe Always");

        // Handle drawer item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_sos) {
                startActivity(new Intent(this, HiddenSOSActivity.class));
            } else if (id == R.id.nav_contacts) {
                startActivity(new Intent(this, EmergencyContactsActivity.class));
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    // Main buttons
    public void openContacts(View v) {
        startActivity(new Intent(this, EmergencyContactsActivity.class));
    }

    public void openSOS(View v) {
        startActivity(new Intent(this, HiddenSOSActivity.class));
    }

    public void openSettings(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}