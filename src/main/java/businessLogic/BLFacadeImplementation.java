package businessLogic;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dataAccess.HibernateDataAccess;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import domain.Driver;
import jakarta.enterprise.context.ApplicationScoped;


@WebService(endpointInterface = "businessLogic.BLFacade")
@ApplicationScoped
public class BLFacadeImplementation implements BLFacade {
	
	HibernateDataAccess dbManager; 

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		dbManager = new HibernateDataAccess();
	}
	
    public BLFacadeImplementation(HibernateDataAccess da)  {
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		dbManager = da;		
	}
    
    /**
     * {@inheritDoc}
     */
    @WebMethod 
    public List<String> getDepartCities(){
		return dbManager.getDepartCities();
    }
    
    /**
     * {@inheritDoc}
     */
	@WebMethod 
	public List<String> getDestinationCities(String from){
		return dbManager.getArrivalCities(from);
	}

	/**
	 * {@inheritDoc}
	 */
   @WebMethod
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
		return dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
   };
	
   /**
    * {@inheritDoc}
    */
	@WebMethod 
	public List<Ride> getRides(String from, String to, Date date){
		return dbManager.getRides(from, to, date);
	}

    
	/**
	 * {@inheritDoc}
	 */
	@WebMethod 
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		return dbManager.getThisMonthDatesWithRides(from, to, date);
	}
	
    @WebMethod	
	 public void initializeBD(){

	}
    @WebMethod
    public Driver getDriver(String email) {
        return dbManager.getDriver(email);
    }

    @WebMethod
    public void createDriver(String email, String name, String password) {
        dbManager.createDriver(email, name, password);
    }
    

    @WebMethod
    public List<Ride> getRidesByDriver(Driver driver) {
    	String driverMail = driver.getEmail();
        return dbManager.getRidesByDriver(driverMail);
    }
    
    @WebMethod
    public List<Ride> getCheaperRides(double money) {
        return dbManager.getCheaperRides(money);
    }
}