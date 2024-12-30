package com.furkan.clashofwords;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.furkan.clashofwords.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        super.setContentView(binding.getRoot());

        // Passing each menu ID as a set of IDs because each
        // menu should be considered as top-level destinations.
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_shop, R.id.navigation_friends, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // ProfileFragment açıldığında BottomNavigationView'i gizle
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.profileFragment ||
                    destination.getId() == R.id.profilePicture ||
                    destination.getId() == R.id.questionStartFragment ||
                    destination.getId() == R.id.gamePlayFragment ||
                    destination.getId() == R.id.resultFragment) {
                navView.setVisibility(View.GONE); // Alt menüyü gizle
            } else {
                navView.setVisibility(View.VISIBLE); // Diğer fragment'larda göster
            }
        });

    }
}
