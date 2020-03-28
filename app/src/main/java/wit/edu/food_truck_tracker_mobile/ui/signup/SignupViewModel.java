package wit.edu.food_truck_tracker_mobile.ui.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignupViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SignupViewModel() {
        mText = new MutableLiveData<>("This is the login fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}