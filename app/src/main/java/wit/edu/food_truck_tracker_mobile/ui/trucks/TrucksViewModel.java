package wit.edu.food_truck_tracker_mobile.ui.trucks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrucksViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TrucksViewModel() {
        mText = new MutableLiveData<>("This is the value");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String str) {
        mText.postValue(str);
    }
}