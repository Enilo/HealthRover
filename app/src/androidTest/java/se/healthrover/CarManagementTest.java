package se.healthrover;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.github.javafaker.Faker;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import se.healthrover.car_service.CarManagementImp;
import se.healthrover.conectivity.SqlHelper;
import se.healthrover.entities.Car;
import se.healthrover.entities.ObjectFactory;
import se.healthrover.ui_activity_controller.car_selection.CarSelect;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class CarManagementTest {

    private Activity activity;
    private List<Car> dataBase;
    private List<Car> carListTestCars;
    private SqlHelper sqlHelper;
    private CarManagementImp management;


    @Rule
    public ActivityTestRule<CarSelect> carSelectActivityTestRule = new ActivityTestRule<>(CarSelect.class);


    @Before
    public void setUp() {
        activity = carSelectActivityTestRule.getActivity();
        management = new CarManagementImp();
        carListTestCars = new ArrayList<>();
        dataBase = new ArrayList<>();
        sqlHelper = ObjectFactory.getInstance().getSqlHelper(activity);
        dataBase = sqlHelper.getSavedCars();
        sqlHelper.deleteTableContent();
        for (int i = 0; i < 10; i++){
            Car car = new Car("http://" + new Faker().internet().url(), new Faker().name().username());
            carListTestCars.add(car);
            management.addCar(car);
        }
        for (int i = 0; i < carListTestCars.size(); i++){
            sqlHelper.insertData(carListTestCars.get(i));
        }

    }

    @Test
    public void updateCarNameTest(){
        Car car = carListTestCars.get(0);
        management.updateCarName(car,"test new name", activity);
        car.setName("test new name");
        assertEquals(car, sqlHelper.getCarByName("test new name"));
    }

    @Test
    public void loadCarsIntoListTest(){
        management.addCar(carListTestCars.get(0));
        management.loadCarsIntoList(activity);

        assertEquals(carListTestCars.get(0), management.getCars().get(0));
    }


    @Test
    public void getCarNamesTest(){
        String[] result = management.getCarNames();
        String[] carNames = new String[carListTestCars.size()];
        for (int i = 0; i < carNames.length; i++){
            carNames[i] = carListTestCars.get(i).getName();
        }
        assertArrayEquals(carNames, result);
    }

    @Test
    public void getCarByUrlTest(){
        Car car = carListTestCars.get(0);
        Car result = management.getCarByURL(car.getURL());
        assertEquals(car, result);

    }

    @Test
    public void getCarByNameTest(){
        Car car = carListTestCars.get(0);
        String carName = car.getName();
        Car result = management.getCarByName(carName);
        assertEquals(carName, result.getName());
        assertEquals(car, result);
    }
    @Test
    public void getCarsTest(){
        List<Car> cars = management.getCars();
        assertEquals(carListTestCars, cars);
    }

    @Test
    public void addCarTest(){
        Car car = new Car("http://" + new Faker().internet().url(), new Faker().name().username());
        carListTestCars.add(car);
        int startSize = management.getCars().size();
        management.addCar(car);
        assertEquals(startSize + 1 , management.getCars().size());
        assertEquals(carListTestCars, management.getCars());
    }

    @Test
    public void removeCarTest(){
        Car car = management.getCars().get(0);
        carListTestCars.remove(car);
        int startSize = management.getCars().size();
        management.removeCar(car);
        assertEquals(startSize - 1 , management.getCars().size());
        assertEquals(carListTestCars, management.getCars());
    }
    @Test
    public void setCarListTest(){
        for (int i = 1; i < management.getCars().size(); i++){
            management.getCars().remove(i);
        }
        assertNotEquals(carListTestCars, management.getCars());
        management.setCars(carListTestCars);
        assertEquals(carListTestCars, management.getCars());
    }

    @After
    public void tearDown()  {
        sqlHelper.deleteTableContent();
        if (dataBase != null){
            for (int i = 0; i < dataBase.size(); i++){
                sqlHelper.insertData(dataBase.get(i));
            }
            assertEquals(dataBase, sqlHelper.getSavedCars());
        }
        dataBase = null;
        carListTestCars = null;
        activity = null;
        management.setCars(new ArrayList<Car>());
    }
}
