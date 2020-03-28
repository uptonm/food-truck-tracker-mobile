package wit.edu.food_truck_tracker_mobile.ui.create_truck;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateTruckViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreateTruckViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}