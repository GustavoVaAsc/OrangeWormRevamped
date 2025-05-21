package unam.fi.ai.orangewormrevamped;
import unam.fi.ai.orangewormrevamped.appobjects.*;
import unam.fi.ai.orangewormrevamped.appobjects.decisiontree.*;
import unam.fi.ai.orangewormrevamped.ui.calculatetime.CalculateTransferFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import unam.fi.ai.orangewormrevamped.databinding.ActivityMainBinding;
import unam.fi.ai.orangewormrevamped.ui.subwaymap.MetroMapActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private boolean isCalculateTransferFragmentOpen = false;
    private static final String CALCULATE_TRANSFER_TAG = "calculate_transfer_fragment";


    public boolean isCalculateTransferFragmentOpen() {
        Fragment f = getSupportFragmentManager().findFragmentByTag(CALCULATE_TRANSFER_TAG);
        return f != null && f.isVisible();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserManager.current_user = new User("Temporal","12345678");
        UserManager.current_user.loadGraph(this);
        UserManager.current_user.createGraph();
        UserManager.current_user.loadUserRoutes(this);
        DecisionTree dt = new DecisionTree();
        List<RouteInstance> to_train = dt.convertRoutesToInstances(UserManager.current_user.getSavedRoutes());
        Trainer trainer = new Trainer();
        trainer.invokeTrainer(to_train,dt);

        //dt.predictCurrentRoute(dt,);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCalculateTransferFragmentOpen) {
                    isCalculateTransferFragmentOpen = true;

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                                    android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .replace(R.id.nav_host_fragment_content_main, new CalculateTransferFragment(), CALCULATE_TRANSFER_TAG)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });



        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_subwaymap)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            FragmentManager fm = getSupportFragmentManager();
            Fragment calculateFragment = fm.findFragmentByTag(CALCULATE_TRANSFER_TAG);

            if (calculateFragment != null) {
                fm.popBackStack(CALCULATE_TRANSFER_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                isCalculateTransferFragmentOpen = false;
            }

            if (id == R.id.nav_calculatetime) {
                if (!isCalculateTransferFragmentOpen) {
                    isCalculateTransferFragmentOpen = true;
                    fm.beginTransaction()
                            .replace(R.id.nav_host_fragment_content_main, new CalculateTransferFragment(), CALCULATE_TRANSFER_TAG)
                            .addToBackStack(CALCULATE_TRANSFER_TAG)
                            .commit();
                }
                binding.drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.nav_subwaymap) {
                Intent intent = new Intent(MainActivity.this, MetroMapActivity.class);
                startActivity(intent);
                binding.drawerLayout.closeDrawers();
                return true;
            } else {
                binding.drawerLayout.closeDrawers();
                return NavigationUI.onNavDestinationSelected(item,
                        Navigation.findNavController(this, R.id.nav_host_fragment_content_main))
                        || super.onOptionsItemSelected(item);
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setCalculateTransferFragmentOpen(boolean isOpen) {
        this.isCalculateTransferFragmentOpen = isOpen;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment f = getSupportFragmentManager().findFragmentByTag(CALCULATE_TRANSFER_TAG);
        isCalculateTransferFragmentOpen = f != null;
    }

    private void clearActiveFragmentScreen() {
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0) {
            Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
            if (currentFragment instanceof unam.fi.ai.orangewormrevamped.ui.home.HomeFragment) {
                ((unam.fi.ai.orangewormrevamped.ui.home.HomeFragment) currentFragment).clearScreen();
            } else if (currentFragment instanceof unam.fi.ai.orangewormrevamped.ui.gallery.GalleryFragment) {
                ((unam.fi.ai.orangewormrevamped.ui.gallery.GalleryFragment) currentFragment).clearScreen();
            }
        }
    }

}