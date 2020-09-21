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
    private String SELECT = "select * from ship";


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
        StringBuilder str;
        int pageNumber = 0;
        int pageSize = 3;
        try {
            str = question(path) == null ? new StringBuilder(" ") : question(path);
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
            } else str.append("order by id ");

            if (path.containsKey("pageNumber")) pageNumber = Integer.parseInt(path.get("pageNumber"));
            if (path.containsKey("pageSize")) pageSize = Integer.parseInt(path.get("pageSize"));

        } catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }


        List<Ship> question = shipDAO.searchByParameters(SELECT + str.toString(), pageNumber, pageSize);

        return question;
    }

    @Override
    public int searchByParametersForCount(Map<String, String> path) {
        StringBuilder str = question(path) == null ? new StringBuilder(" ") : question(path);

        return shipDAO.searchByParametersForCount(SELECT + str.toString());
    }

    private Double rating(Long prodData, Double speed, Boolean isUsed ){
        double k = isUsed ?  0.5 :  1;
        Date date = new Date(prodData);
        double result = ( 80.0 * speed * k)/(3019 - (date.getYear() + 1900) + 1.0) * 100;
        result = (double)Math.round(result);
        return result / 100;
    }

    private StringBuilder question(Map<String, String> path) {
        Ship ship = new Ship();
        StringBuilder str = new StringBuilder();
        java.sql.Date date = new java.sql.Date(1119);
        boolean chek = false;
        str.append(" where ");

        if (path.containsKey("name")) {
            ship.setName(path.get("name"));
            if (ShipChekHelper.invalidName(ship)) return null;
            str.append("name LIKE ").append("'%").append(path.get("name")).append("%' ");
            chek = true;
        }

        if (path.containsKey("planet")) {
                if(chek) str.append("and ");
                ship.setPlanet(path.get("planet"));
                if (ShipChekHelper.invalidPlanet(ship)) return null;
                str.append("planet LIKE ").append("'%").append(path.get("planet")).append("%' ");
                chek = true;
            }
            if (path.containsKey("shipType")) {
                if(chek) str.append("and ");
                str.append("shipType ='");
                str.append(path.get("shipType")).append("' ");
                chek = true;
            }
          if (path.containsKey("after")) {
                if(chek) str.append("and ");
                long l = Long.parseLong(path.get("after"));
                str.append("prodDate >='");
                date.setTime(l);
                str.append(date).append("' ");
                chek = true;
            }
            if (path.containsKey("before")) {
                if(chek) str.append("and ");
                long l = Long.parseLong(path.get("before"));
                str.append("prodDate <='");
                date.setTime(l);
                str.append(date).append("' ");
                chek = true;
            }
           if (path.containsKey("isUsed")) {
               if(chek) str.append("and ");
               str.append("isUsed ='");
               str.append(path.get("isUsed")).append("' ");
               chek = true;
            }

          if (path.containsKey("minSpeed")) {
                if(chek) str.append("and ");
                ship.setSpeed(Double.parseDouble(path.get("minSpeed")));
                if (ShipChekHelper.invalidSpeed(ship)) return null;
                str.append("speed >='");
                str.append(path.get("minSpeed")).append("' ");
                chek = true;
            }
            if (path.containsKey("maxSpeed")) {
                if(chek) str.append("and ");
                ship.setSpeed(Double.parseDouble(path.get("maxSpeed")));
                if (ShipChekHelper.invalidSpeed(ship)) return null;
                str.append("speed <='");
                str.append(path.get("maxSpeed")).append("' ");
                chek = true;
            }
              if (path.containsKey("minCrewSize")) {
                if(chek) str.append("and ");
                ship.setCrewSize(Integer.parseInt(path.get("minCrewSize")));
                if (ShipChekHelper.invalidCrewSize(ship)) return null;
                str.append("crewSize >='");
                str.append(path.get("minCrewSize")).append("' ");
                chek = true;
            }
            if (path.containsKey("maxCrewSize")) {
                if(chek) str.append("and ");
                ship.setCrewSize(Integer.parseInt(path.get("maxCrewSize")));
                if (ShipChekHelper.invalidCrewSize(ship)) return null;
                str.append("crewSize <='");
                str.append(path.get("maxCrewSize")).append("' ");
                chek = true;
            }
            if (path.containsKey("minRating")) {
                if(chek) str.append("and ");
                if (Double.parseDouble(path.get("minRating")) <= 0) return null;
                str.append("rating >='");
                str.append(path.get("minRating")).append("' ");
                chek = true;
            }
            if (path.containsKey("maxRating")) {
                if(chek) str.append("and ");
                if (Double.parseDouble(path.get("maxRating")) <= 0) return null;
                str.append("rating <='");
                str.append(path.get("maxRating")).append("' ");
                chek = true;
            }

            if (chek) return str;
            else return null;
        }

    }
