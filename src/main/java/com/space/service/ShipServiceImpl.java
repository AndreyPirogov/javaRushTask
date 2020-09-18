package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("Service")
public class ShipServiceImpl implements ShipService {
    public static final Logger logger = Logger.getLogger(ShipServiceImpl.class);


    @Qualifier("jpaContact")
    @Autowired
    private ShipDAO shipDAO;

    @Override
    public List<Ship> allShip() {
        return shipDAO.allShip();
    }

    @Override
    public Ship add(Ship ship) {
        logger.debug("creat Ship");

        double rating = rating(ship.getProdDate().getTime(), ship.getSpeed(), ship.getUsed());
        logger.debug("rating = " + rating);
        ship.setRating(rating);

        return shipDAO.add(ship);
    }

    @Override
    public void delete(Ship ship) {
        shipDAO.delete(ship);
    }

    @Override
    public Ship update(Ship ship) {
        Ship update = shipDAO.getById(ship.getId());
        if(ship.getName() != null ){
            update.setName(ship.getName());
        }
        if(ship.getPlanet() != null){
            update.setPlanet(ship.getPlanet());
        }
        if(ship.getShipType() != null){
            update.setShipType(ship.getShipType());
        }
        if(ship.getProdDate() != null){
            update.setShipType(ship.getShipType());
        }
        if (ship.getUsed() != null){
            update.setUsed(ship.getUsed());
        }
        if(ship.getProdDate() != null){
            update.setProdDate(ship.getProdDate());
        }
        if(ship.getSpeed() != null){
            update.setSpeed(ship.getSpeed());
        }
        if(ship.getCrewSize() != null){
            update.setCrewSize(ship.getCrewSize());
        }


        update.setRating(rating(update.getProdDate().getTime(), update.getSpeed(), update.getUsed()));

            shipDAO.update(update);
            return update;
    }

    @Override
    public Ship getById(Long id) {
        return shipDAO.getById(id);
    }

    @Override
    public List<Ship> searchByParameters(Map<String, Object> path) {
        return null;
    }

    private Double rating(Long prodData, Double speed, Boolean isUsed ){
        double k = isUsed ?  0.5 :  1;
        Date date = new Date(prodData);
        double result = ( 80.0 * speed * k)/(3019 - (date.getYear() + 1900) + 1.0) * 100;
        result = (double)Math.round(result);
        return result / 100;
    }
}
