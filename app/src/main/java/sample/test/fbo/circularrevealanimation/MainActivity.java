package sample.test.fbo.circularrevealanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private final static int ANIMATION_DURATION = 400;
    private ToggleButton mActionButton;
    private View mRevealedToolBar;
    private ImageButton mArrowButton;
    private boolean mIsHidden = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // main toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // trigger circular reveal animation
        mActionButton = (ToggleButton) findViewById(R.id.actionButton);
        mActionButton.setOnClickListener(this);

        // toolbar to reveal
        mRevealedToolBar = findViewById(R.id.revealedToolBar);
        mRevealedToolBar.setVisibility(View.INVISIBLE);

        // button in revealed toolbar to dismiss it
        mArrowButton = (ImageButton) findViewById(R.id.toolbar_arrow);
        mArrowButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onClick(final View view) {

        if (view == mActionButton || view == mArrowButton) {


            // compute started X and Y co-ordinates for the animation + radius
            int x = mRevealedToolBar.getLeft();
            int y = mRevealedToolBar.getBottom();
            int startRadius = 0;
            int endRadius = Math.max(mRevealedToolBar.getWidth(), mRevealedToolBar.getHeight());
            int reverseStartRadius = endRadius;
            int reverseEndRadius = startRadius;



            if (mIsHidden) {

                // show secondary toolbar
                // performing circular reveal when icon will be tapped
                Animator animator = ViewAnimationUtils.createCircularReveal(mRevealedToolBar, x, y, startRadius, endRadius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(ANIMATION_DURATION);

                // to show the layout when icon is tapped
                mRevealedToolBar.setVisibility(View.VISIBLE);
                animator.start();
                mIsHidden = false;


            } else {

                // dismiss secondary toolbar
                // performing circular reveal for reverse animation
                Animator animate = ViewAnimationUtils.createCircularReveal(mRevealedToolBar, x, y, reverseStartRadius, reverseEndRadius);
                animate.setInterpolator(new AccelerateDecelerateInterpolator());
                animate.setDuration(ANIMATION_DURATION);

                // to hide layout on animation end
                animate.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealedToolBar.setVisibility(View.INVISIBLE);
                        mIsHidden = true;
                    }
                });

                mRevealedToolBar.setVisibility(View.VISIBLE);
                animate.start();
            }
        }
    }
}
