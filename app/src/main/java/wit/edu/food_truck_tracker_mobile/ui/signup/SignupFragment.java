package wit.edu.food_truck_tracker_mobile.ui.signup;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wit.edu.food_truck_tracker_mobile.R;
import wit.edu.food_truck_tracker_mobile.api.ApiClient;
import wit.edu.food_truck_tracker_mobile.api.TrackerApi;
import wit.edu.food_truck_tracker_mobile.models.AuthRequestBody;
import wit.edu.food_truck_tracker_mobile.models.AuthTokenResponse;
import wit.edu.food_truck_tracker_mobile.models.SignupRequestResponse;

public class SignupFragment extends Fragment {

    private static final int REQUEST_LOCATION = 99;
    private SignupViewModel signupViewModel;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        signupViewModel =
                new ViewModelProvider(this).get(SignupViewModel.class);
        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        final EditText emailInput = root.findViewById(R.id.signup_email);
        final EditText passwordInput = root.findViewById(R.id.signup_password);
        final EditText passwordConfirmInput = root.findViewById(R.id.signup_password_confirm);

        Button loginBtn = root.findViewById(R.id.signup_button);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "LOGIN CLICKED!!!");
                if (passwordInput.getText().toString().equals(passwordConfirmInput.getText().toString())) {
                    handleSignup(emailInput.getText().toString(), passwordInput.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Passwords must match", Toast.LENGTH_SHORT).show();
                }
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

    private GestureDetector setupGestures() {
        return new GestureDetector(getActivity(),
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                        else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.i("TAG", "Left to Right");
                            switchToLogin();
                        }
                    } catch (Exception e) {
                        // nothing
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void switchToLogin() {
        Log.d("TAG", "Listener Worked");
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_login);
    }

    private void handleSignup(String email, String password) {
        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<SignupRequestResponse> login = trackerApiService.signup(new AuthRequestBody(email, password));
        login.enqueue(new Callback<SignupRequestResponse>() {
            @Override
            public void onResponse(Call<SignupRequestResponse> call, Response<SignupRequestResponse> response) {
                if (response.code() == 200) {
                    SignupRequestResponse signupResponse = response.body();
                    Log.d("TAG", signupResponse.getUser().toString());
                    Toast.makeText(getContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Signup Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignupRequestResponse> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });
    }
}