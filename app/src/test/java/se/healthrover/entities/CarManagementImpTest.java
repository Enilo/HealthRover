package se.healthrover.entities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;
import se.healthrover.conectivity.HealthRoverWebService;

public class CarManagementImpTest {


    private CarManagement carManagementSpy;
    private HealthRoverWebService healthRoverCar;

    @Before
    public void setUp(){
        CarManagement carManagement = new CarManagementImp(healthRoverCar);
        carManagementSpy = Mockito.spy(carManagement);
    }



    @Test
    public void checkIfRequestIsSendTest() {
        carManagementSpy.moveCar(HealthRoverCar.HEALTH_ROVER_CAR1,0,0,null);
        Mockito.verify(carManagementSpy, Mockito.times(1)).moveCar(HealthRoverCar.HEALTH_ROVER_CAR1,0,0,null);
    }


}
