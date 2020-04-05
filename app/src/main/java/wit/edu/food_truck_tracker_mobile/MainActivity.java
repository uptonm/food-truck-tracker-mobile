package wit.edu.food_truck_tracker_mobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Locale;

import retrofit2.Callback;
import retrofit2.Response;
import wit.edu.food_truck_tracker_mobile.api.ApiClient;
import wit.edu.food_truck_tracker_mobile.api.TrackerApi;
import wit.edu.food_truck_tracker_mobile.models.Truck;
import wit.edu.food_truck_tracker_mobile.models.User;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 99;
    private AppBarConfiguration mAppBarConfiguration;

    private static String latitude = null;
    private static String longitude = null;
    private static final String RADIUS = "5";
    private static boolean called = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                SmartLocation.with(getApplicationContext()).location().config(LocationParams.NAVIGATION).start(new OnLocationUpdatedListener() {
////                    @Override
////                    public void onLocationUpdated(Location location) {
////                        if (location != null) {
////                            Log.d("TAG", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
////                        } else {
////                            Toast.makeText(MainActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                });
////            }
////        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_trucks, R.id.nav_liked, R.id.nav_create_truck, R.id.nav_login)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            getLocationUpdates();
        }

        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String jwt = prefs.getString("token", "");
        Log.d("TAG", "GOT TOKEN FROM STORAGE: " + jwt);
        if (jwt.length() > 0) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                fetchUser(jwt);
            }
        }
    }

    public void onClickLogin(View v) {
        Log.d("TAG", "Listener Worked");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.nav_login);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem logoutBtn = menu.findItem(R.id.action_logout);
        logoutBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("TAG", "Handle Log Out");
                handleLogout();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void getLocationUpdates() {
        SmartLocation.with(getApplicationContext()).location().config(LocationParams.NAVIGATION).start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                if (location != null) {
                    latitude = location.getLatitude() + "";
                    longitude = location.getLongitude() + "";
                    if (!called) {
                        called = true;
                        getTrucks();
                    }
                } else
                    Toast.makeText(MainActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTrucks() {
        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<List<Truck>> getTrucks = trackerApiService.searchTrucks(latitude, longitude, RADIUS);
        getTrucks.enqueue(new Callback<List<Truck>>() {
            @Override
            public void onResponse(Call<List<Truck>> call, Response<List<Truck>> response) {
                List<Truck> trucks = response.body();
                for (Truck truck : trucks) {
                    Log.d("TAG", truck.getName());

                    if (truck.getName().equals("Moyzilla")) {
//                       routeToLocation(
//                               latitude,
//                               longitude,
//                               truck.getLocation().getCoordinates()[0].toString(),
//                               truck.getLocation().getCoordinates()[1].toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Truck>> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });
    }

    private void wipeToken() {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs=getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit=prefs.edit();
        edit.remove("token");
        Log.i("TAG", "Removing JWT");
        edit.apply();
    }

    private void handleLogout() {
        NavigationView navView = findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);

        // Hide Login Button
        Button loginButton = navView.findViewById(R.id.nav_login_button);
        loginButton.setVisibility(View.VISIBLE);

        // Hide User Email and Name
        TextView username = navView.findViewById(R.id.username);
        username.setVisibility(View.GONE);

        TextView userEmail = navView.findViewById(R.id.user_email);
        userEmail.setVisibility(View.GONE);

        // Show Sample Avatar
        ImageView avatar = navView.findViewById(R.id.avatar);
        avatar.setVisibility(View.GONE);

        // Remove JWT
        wipeToken();
    }

    private void fetchUser(String jwt) {
        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<User> getUser = trackerApiService.getUser("Bearer " + jwt);
        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user != null) {
                    handleLoggedIn(user.getFirst() + " " + user.getLast(), user.getEmail());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });
    }

    private void handleLoggedIn(String fullName, String email) {
        NavigationView navView = findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);

        // Hide Login Button
        Button loginButton = navView.findViewById(R.id.nav_login_button);
        loginButton.setVisibility(View.GONE);

        // Show User Email and Name
        if (!fullName.equals("null null")) {
            TextView username = navView.findViewById(R.id.username);
            username.setText(fullName);
            username.setVisibility(View.VISIBLE);
        }

        TextView userEmail = navView.findViewById(R.id.user_email);
        userEmail.setText(email);
        userEmail.setVisibility(View.VISIBLE);

        // Show Sample Avatar
        ImageView avatar = navView.findViewById(R.id.avatar);
        avatar.setVisibility(View.VISIBLE);
    }
}