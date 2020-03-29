package wit.edu.food_truck_tracker_mobile.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wit.edu.food_truck_tracker_mobile.R;
import wit.edu.food_truck_tracker_mobile.api.ApiClient;
import wit.edu.food_truck_tracker_mobile.api.TrackerApi;
import wit.edu.food_truck_tracker_mobile.models.AuthRequestBody;
import wit.edu.food_truck_tracker_mobile.models.AuthTokenResponse;
import wit.edu.food_truck_tracker_mobile.models.User;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText emailInput = root.findViewById(R.id.login_email);
        final EditText passwordInput = root.findViewById(R.id.login_password);

        Button loginBtn = root.findViewById(R.id.login_button);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin(emailInput.getText().toString(), passwordInput.getText().toString());
            }
        });


        final GestureDetector gesture = setupGestures();

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private GestureDetector setupGestures() {
        return new GestureDetector(getActivity(),
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    Log.i("TAG", "onFling has been called!");
                    final int SWIPE_MIN_DISTANCE = 120;
                    final int SWIPE_MAX_OFF_PATH = 250;
                    final int SWIPE_THRESHOLD_VELOCITY = 200;
                    try {
                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                            return false;
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.i("TAG", "Right to Left");
                            switchToSignup();
                        }
                    } catch (Exception e) {
                        // nothing
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void switchToSignup() {
        Log.d("TAG", "Listener Worked");
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_signup);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveToken(String jwt) {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs= Objects.requireNonNull(getActivity()).getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit=prefs.edit();
        edit.putString("token", jwt);
        Log.i("Login", jwt);
        edit.apply();
    }

    private String getToken() {
        SharedPreferences prefs=this.getActivity().getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        return prefs.getString("token","");
    }

    private void handleLogin(String email, String password) {
        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<AuthTokenResponse> login = trackerApiService.login(new AuthRequestBody(email, password));
        login.enqueue(new Callback<AuthTokenResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                if (response.code() == 200) {
                    AuthTokenResponse token = response.body();
                    Log.d("TAG", token.getJwtToken());
                    saveToken(token.getJwtToken());
                    Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "Token saved: " + getToken());
                    handlePostLogin(token.getJwtToken());
                } else {
                    Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                Log.d("TAG", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handlePostLogin(String token) {
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_trucks);

        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<User> getUser = trackerApiService.getUser("Bearer " + token);
        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User userResponse = response.body();
                handleUpdateView(userResponse.getFirst() + " " + userResponse.getLast(), userResponse.getEmail());
                Log.d("TAG", userResponse.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void handleUpdateView(String fullName, String email) {
        NavigationView navView = getActivity().findViewById(R.id.nav_view);
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