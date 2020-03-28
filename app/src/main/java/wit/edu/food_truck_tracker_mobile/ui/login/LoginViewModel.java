package wit.edu.food_truck_tracker_mobile.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LoginViewModel() {
        mText = new MutableLiveData<>("This is the login fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}