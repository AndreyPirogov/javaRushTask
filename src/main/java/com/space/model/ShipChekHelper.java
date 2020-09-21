package com.space.model;

import com.space.controller.ShipController;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ShipChekHelper {
    public static final Logger logger = Logger.getLogger(ShipController.class);

    public static synchronized boolean invalidName(Ship ship){
        if(ship.getName().length() > 50){
            logger.debug("name > 50");
            return true;
        }
        if(ship.getName().trim().isEmpty()){
            logger.debug("name  isEmpty");
            return true;
        }
        return false;
    }
    public static synchronized  boolean invalidPlanet( Ship ship){
        if( ship.getPlanet().length() > 50){
            logger.debug(" planet > 50");
            return true;
        }
        if(ship.getPlanet().trim().isEmpty()){
            logger.debug("planet isEmpty");
            return true;
        }
        return false;
    }


    public static synchronized  boolean invalidDate( Ship ship){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(ship.getProdDate());
        Calendar calendarBefore = new GregorianCalendar(2800, Calendar.JANUARY, 1);
        Calendar calendarAfter = new GregorianCalendar(3019, Calendar.DECEMBER, 31);
        if (calendar.before(calendarBefore) || calendar.after(calendarAfter)){
            logger.debug("badDate");
            return  true;
        }
        return false;
    }
    public static synchronized boolean invalidSpeed( Ship ship){
        if (ship.getSpeed()  < 0.01 || ship.getSpeed() > 0.99){
            logger.debug(" bad speed ");
            return  true;
        }
        return false;
    }

    public static synchronized  boolean invalidCrewSize( Ship ship){
        if (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999){
            logger.debug(" bad speed or bad crewSize");
            return  true;
        }
        return false;
    }

    public static synchronized boolean noBodyOption( Ship ship){
        if(ship.getName() == null || ship.getPlanet() == null || ship.getShipType() == null ||
                ship.getProdDate() == null || ship.getSpeed() == null || ship.getCrewSize() == null  ){
            logger.debug("is null");
            return  true;
        }
        return false;
    }
    public static synchronized boolean noBody( Ship ship){
        if(ship.getName() == null && ship.getPlanet() == null && ship.getShipType() == null &&
                ship.getProdDate() == null && ship.getSpeed() == null && ship.getCrewSize() == null  ){
            logger.debug("is null");
            return  true;
        }
        return false;
    }
    public static synchronized  boolean invalidID(Long id){
        return id == null || id <= 0;
    }
}
