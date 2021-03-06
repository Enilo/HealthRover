package se.healthrover.ui_activity_controller.car_selection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.conectivity.SqlHelper;
import se.healthrover.entities.Car;
import se.healthrover.entities.ObjectFactory;
import se.healthrover.ui_activity_controller.utilities.UserInterfaceUtilities;

public class CarSelect extends Activity{

    private Button infoButton;
    private Button connectToCarSelected;
    private Car healthRoverCar;
    private boolean carOnlineConnection;
    private CarManagement carManagement;
    private UserInterfaceUtilities uiHelper;
    private ListView carList;

    public CarSelect() {
        carManagement = ObjectFactory.getInstance().getCarManagement();
        uiHelper = ObjectFactory.getInstance().getInterfaceUtilities();
    }


    //Create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(ObjectFactory.getInstance().getExceptionHandler(this, healthRoverCar));
        carManagement.getCars().clear();
        initialize();

    }
    //Refresh the activity, used on back button press
    @Override
    protected void onRestart() {
        super.onRestart();
        Thread.setDefaultUncaughtExceptionHandler(ObjectFactory.getInstance().getExceptionHandler(this, healthRoverCar));
        carManagement.getCars().clear();
        initialize();
    }

    //Used to initialize the elements on the activity once it¨s loaded
    private void initialize(){
        checkForErrorMessage();
        //setting up by default everything to false and loading the car names into the listView and adapter
        carOnlineConnection = false;
        healthRoverCar = null;
        setContentView(R.layout.car_select);
        connectToCarSelected = findViewById(R.id.connectToCarButton);
        infoButton = findViewById(R.id.infoButton);
        carList = findViewById(R.id.smartCarList);
        carManagement.loadCarsIntoList(this);

        //Once a car is selected the name is retrieved and used to initialize the car object that is to be controlled
        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String carName = carList.getItemAtPosition(position).toString();
                healthRoverCar = carManagement.getCarByName(carName);
                uiHelper.showCustomToast(getApplicationContext(), getString(R.string.selected_car_message) + carName);
            }
        });

        //Connect to car button is pressed and we call the get status method in order to verify that the car is online
        connectToCarSelected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (healthRoverCar == null){
                    uiHelper.showCustomToast(getApplicationContext(), getString(R.string.select_car_prompt));
                }else {
                    carManagement.checkStatus(healthRoverCar, CarSelect.this);
                }
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiHelper.showCustomPopup(getApplicationContext(), R.layout.info_popup, v);
            }
        });
    }
    //Checks if the activity is loaded after a crash and prints it out if it exists
    private void checkForErrorMessage() {
        String errorMessage = getIntent().getStringExtra(getString(R.string.crash_error_intent));
        if (errorMessage!=null){
            uiHelper.showCustomToast(getApplicationContext(), errorMessage);
        }
    }



    //Using back button to exit application
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        carManagement.getCars().clear();
        finishAffinity();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        carManagement.getCars().clear();
        initialize();
    }
}
