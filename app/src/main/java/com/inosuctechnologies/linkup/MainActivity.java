package com.inosuctechnologies.linkup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.inosuctechnologies.linkup.ui.LanguageActivity;
import com.inosuctechnologies.linkup.ui.fragments.BuzzFragment;
import com.inosuctechnologies.linkup.ui.fragments.CategoryFragment;
import com.inosuctechnologies.linkup.ui.fragments.HomeFragment;
import com.inosuctechnologies.linkup.ui.fragments.InfoFragment;
import com.inosuctechnologies.linkup.ui.fragments.ProductsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        if (! isTablet){
            // desired action
        } else {
            // same action
        }

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, LanguageActivity.class));
                break;
            case R.id.action_login:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_products) {
            fragment = new ProductsFragment();
            setTitle(R.string.my_products);
        } else if (id == R.id.nav_business) {
            fragment = new BuzzFragment();
            setTitle(R.string.business_type);
        } else if (id == R.id.nav_categories) {
            fragment = new CategoryFragment();
            setTitle(R.string.categories);
        }
//        else if (id == R.id.nav_login) {
//            fragment = new LoginFragment();
//            setTitle(R.string.log_in);
//        }
        else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND).setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, "I use LinkUP for Android to link up " +
                            "with buyers and sellers of products and services.\n" +
                            " Try it: https://linkup.ist.co.zw/mobile/feed.php/");
            startActivity(Intent.createChooser(shareIntent, "Share link using"));
        } else if (id == R.id.nav_info) {
            fragment = new InfoFragment();
            setTitle(R.string.about);
        }
        drawer.closeDrawer(GravityCompat.START);
        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
        return true;
    }
}