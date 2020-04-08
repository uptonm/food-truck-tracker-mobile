package wit.edu.food_truck_tracker_mobile.ui.manage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ManageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ManageViewModel() {
        mText = new MutableLiveData<>("This is the manage fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
