package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipChekHelper;
import com.space.repository.ShipDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
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
    public List<Ship> searchByParameters(Map<String, String> path) {
        StringBuilder str = new StringBuilder("select * from ship ");
        int pageNumber = 0;
        int pageSize = 3;
        try {
            str = question(path,str) == null ? new StringBuilder("") : question(path,str);
            if (path.containsKey("order")){
                str.append("order by ");
                switch (path.get("order")){
                    case "ID" : str.append("id");
                    break;
                    case "SPEED" : str.append("speed");
                    break;
                    case "DATE" : str.append("prodDate");
                    break;
                    case "RATING" : str.append("rating");
                    break;
                }
                str.append(" ");
            }

            if (path.containsKey("pageNumber")) pageNumber = Integer.parseInt(path.get("pageNumber"));
            if (path.containsKey("pageSize")) pageSize = Integer.parseInt(path.get("pageSize"));

        } catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }

        List<Ship> question = shipDAO.searchByParameters(str.toString().trim(),pageNumber, pageSize);



        return question;
    }

    @Override
    public int searchByParametersForCount(Map<String, String> path) {
        StringBuilder str = new StringBuilder("select * from ship ");
         str = question(path, str);

        return shipDAO.searchByParametersForCount(str.toString().trim());
    }

    private Double rating(Long prodData, Double speed, Boolean isUsed ){
        double k = isUsed ?  0.5 :  1;
        Date date = new Date(prodData);
        double result = ( 80.0 * speed * k)/(3019 - (date.getYear() + 1900) + 1.0) * 100;
        result = (double)Math.round(result);
        return result / 100;
    }

    private StringBuilder question(Map<String, String> path, StringBuilder stringBuilder) {
        Ship ship = new Ship();
        StringBuilder str = new StringBuilder(stringBuilder);
        str.append("where ");
        if (path.containsKey("name")) {
            ship.setName(path.get("name"));
            if (ShipChekHelper.invalidName(ship)) return null;
            str.append("name LIKE ").append("'%").append(path.get("name")).append("%' ");
        }
     /*   if (path.containsKey("planet")) {
                ship.setPlanet(path.get("planet"));
                if (ShipChekHelper.invalidPlanet(ship)) return null;
                str.append("planet LIKE ").append("'%").append(path.get("planet")).append("%' ");
            }
            if (path.containsKey("shipType")) str.append(path.get("shipType")).append(" ");
            if (path.containsKey("after")) {

                str.append(path.get("after")).append(" ");
            }
            if (path.containsKey("before")) {

                str.append(path.get("before")).append(" ");
            }
           if (path.containsKey("isUsed")) {
                str.append(path.get("isUsed")).append(" ");
            } else str.append("false");

            if (path.containsKey("minSpeed")) {
                ship.setSpeed(Double.parseDouble(path.get("miniSpeed")));
                if (ShipChekHelper.invalidSpeed(ship)) return null;
                str.append(path.get("minSpeed")).append(" ");
            }
            if (path.containsKey("maxSpeed")) {
                ship.setSpeed(Double.parseDouble(path.get("maxSpeed")));
                if (ShipChekHelper.invalidSpeed(ship)) return null;
                str.append(path.get("maxSpeed")).append(" ");
            }
            if (path.containsKey("minCrewSize")) {
                ship.setCrewSize(Integer.parseInt(path.get("minCrewSize")));
                if (ShipChekHelper.invalidCrewSize(ship)) return null;
                str.append(path.get("minCrewSize")).append(" ");
            }
            if (path.containsKey("maxCrewSize")) {
                ship.setCrewSize(Integer.parseInt(path.get("maxCrewSize")));
                if (ShipChekHelper.invalidCrewSize(ship)) return null;
                str.append(path.get("maxCrewSize")).append(" ");
            }
            if (path.containsKey("minRating")) {
                if (Integer.parseInt("minRating") <= 0) return null;
                str.append(path.get("minRating")).append(" ");
            }
            if (path.containsKey("maxRating")) {
                if (Integer.parseInt("maxRating") >= 1) return null;
                str.append(path.get("maxRating")).append(" ");
            }*/
            if (str.length() > 25) return str;
            else return stringBuilder;
        }

    }
