package com.gnzlt.ucotren;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gnzlt.ucotren.about.AboutFragment;
import com.gnzlt.ucotren.databinding.ActivityMainBinding;
import com.gnzlt.ucotren.estimation.EstimationFrameFragment;
import com.gnzlt.ucotren.model.ParseLocation;
import com.gnzlt.ucotren.news.NewsFragment;
import com.gnzlt.ucotren.schedule.ScheduleFrameFragment;
import com.gnzlt.ucotren.tickets.TicketsFragment;
import com.gnzlt.ucotren.util.Constants;
import com.gnzlt.ucotren.util.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.analytics.FirebaseAnalytics;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener {

    private static final String EXTRA_SHOW_NEWS = "EXTRA_SHOW_NEWS";

    private FirebaseAnalytics mFirebaseAnalytics;
    private GoogleApiClient mGoogleApiClient;
    private FragmentManager mFragmentManager;
    private ActivityMainBinding mBinding;

    private ActionBarDrawerToggle mDrawerToggle;


    public static Intent getNewIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_SHOW_NEWS, true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.UCOmoveTheme);
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);

        setupView();
        setupGoogleApiClient();
        setupGooglePlayRate();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, toolbar,
                R.string.util_navigation_drawer_open, R.string.util_navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(mDrawerToggle);
        mBinding.navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

        showHome();
    }

    private void setupGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            checkLocationPermissions();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                        }
                    })
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void checkLocationPermissions() {
        if (Utils.hasLocationPermissions(this)) {
            registerLastLocation();
        } else {
            Utils.requestLocationPermissions(this);
        }
    }

    private void setupGooglePlayRate() {
        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setShowLaterButton(true)
                .setDebug(false)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);
    }

    private void showHome() {
        boolean showNews = getIntent().getBooleanExtra(EXTRA_SHOW_NEWS, false);
        MenuItem homeMenuItem = showNews ?
                mBinding.navigationView.getMenu().findItem(R.id.nav_news) :
                mBinding.navigationView.getMenu().findItem(R.id.nav_train);
        Fragment homeFragment = showNews ?
                NewsFragment.newInstance() :
                EstimationFrameFragment.newInstance(Constants.TRANSPORT_TYPE_TRAIN);
        mFragmentManager.beginTransaction()
                .add(R.id.frame_content, homeFragment)
                .addToBackStack(String.valueOf(homeMenuItem.getItemId()))
                .commit();
        mBinding.navigationView.setCheckedItem(homeMenuItem.getItemId());
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment;
        String eventType;

        switch (menuItem.getItemId()) {
            case R.id.nav_train:
                fragment = EstimationFrameFragment.newInstance(Constants.TRANSPORT_TYPE_TRAIN);
                eventType = Constants.VALUE_TRAIN;
                break;
            case R.id.nav_bus:
                fragment = EstimationFrameFragment.newInstance(Constants.TRANSPORT_TYPE_BUS);
                eventType = Constants.VALUE_BUS;
                break;
            case R.id.nav_schedule:
                fragment = ScheduleFrameFragment.newInstance();
                eventType = Constants.VALUE_SCHEDULE;
                break;
            case R.id.nav_news:
                fragment = NewsFragment.newInstance();
                eventType = Constants.VALUE_NEWS;
                break;
            case R.id.nav_tickets:
                fragment = TicketsFragment.newInstance();
                eventType = Constants.VALUE_TICKETS;
                break;
            case R.id.nav_about:
                fragment = AboutFragment.newInstance();
                eventType = Constants.VALUE_ABOUT;
                break;
            default:
                fragment = EstimationFrameFragment.newInstance(Constants.TRANSPORT_TYPE_TRAIN);
                eventType = Constants.VALUE_TRAIN;
        }

        if (!getLastBackStackEntry().getName().equals(String.valueOf(menuItem.getItemId()))) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.frame_content, fragment, String.valueOf(menuItem.getItemId()))
                    .addToBackStack(String.valueOf(menuItem.getItemId()))
                    .commit();

            menuItem.setChecked(true);
        }

        mBinding.drawerLayout.closeDrawers();
        registerMenuEvent(eventType);
    }

    private void registerMenuEvent(String eventType) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(FirebaseAnalytics.Param.CONTENT_TYPE, eventType);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @SuppressWarnings("MissingPermission")
    private void registerLastLocation() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (lastLocation != null) {
            new ParseLocation(lastLocation).saveInBackground();
        }
    }

    private FragmentManager.BackStackEntry getLastBackStackEntry() {
        int lastBackStackEntryCount = mFragmentManager.getBackStackEntryCount() - 1;
        return mFragmentManager.getBackStackEntryAt(lastBackStackEntryCount);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (mFragmentManager.getBackStackEntryCount() == 1) {
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onBackStackChanged() {
        MenuItem lastMenuItem = mBinding.navigationView.getMenu().findItem(
                Integer.parseInt(getLastBackStackEntry().getName()));
        lastMenuItem.setChecked(true);
        setTitle(lastMenuItem.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utils.PERMISSIONS_REQUEST:
                if ((grantResults.length > 0) &&
                        (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    registerLastLocation();
                }
                break;
            default:
                break;
        }
    }
}
