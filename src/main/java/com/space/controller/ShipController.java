package com.space.controller;


import com.space.model.Ship;
import com.space.service.ShipService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
@RequestMapping(value = "/rest")
public class ShipController {

    public static final Logger logger = Logger.getLogger(ShipController.class);


    private ShipService service;

    @Autowired
    public void setService(ShipService service) {
        this.service = service;
    }



    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/ships",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<Ship>> allShips() {
        List<Ship> list = service.allShip();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @RequestMapping(value = "/ships/count",method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Integer> countShips() {
        List<Ship> list = service.allShip();

        return new ResponseEntity<>(list.size(), HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> findShip (@PathVariable("id") Long id){
        logger.debug("findShip " + id);
        if(id == null || id <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Ship ship = service.getById(id);
        if(ship == null){
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") Long id){
        logger.debug("deleteShip " + id);
        if(id == null || id <= 0)return new ResponseEntity<>((HttpStatus.BAD_REQUEST));


        Ship ship = service.getById(id);
        if(ship == null)return new ResponseEntity<>((HttpStatus.NOT_FOUND));

        service.delete(ship);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @RequestMapping(value = "/ships", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> addShip(@RequestBody Ship ship){
        if(noBodyOption(ship) ) return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(ship.getUsed() == null) ship.setUsed(false);
        if(invalidName(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (invalidPlanet(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        double resultSpeed =Math.round(ship.getSpeed() * 100) / 100.0;
        logger.debug("resultSpeed=" + resultSpeed);
        ship.setSpeed(resultSpeed);
        if(invalidSpeed(ship))return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if(invalidCrewSize(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (invalidDate(ship)) return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship shipResult = service.add(ship);

        return  new ResponseEntity<>(shipResult, HttpStatus.OK);

    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship>  update (@RequestBody Ship ship, @PathVariable("id") Long id){
        if(id <=0){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(service.getById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(noBody(ship)) {
            return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
        }
        if(ship.getName() != null) {
            if(invalidName(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(ship.getPlanet() != null) {
            if (invalidPlanet(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(ship.getProdDate() != null){
            if (invalidDate(ship)) return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(ship.getSpeed() != null) {
            double resultSpeed = Math.round(ship.getSpeed() * 100) / 100.0;
            logger.debug("resultSpeed=" + resultSpeed);
            ship.setSpeed(resultSpeed);
            if(invalidSpeed(ship))return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        if(ship.getCrewSize() != null){
            if(invalidCrewSize(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        ship.setId(id);
        Ship ship1 = service.update(ship);
        return new ResponseEntity<>(ship1, HttpStatus.OK);
    }


    private boolean invalidName(@RequestBody Ship ship){
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
    private boolean invalidPlanet(@RequestBody Ship ship){
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


    private boolean invalidDate(@RequestBody Ship ship){
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
    private boolean invalidSpeed(@RequestBody Ship ship){
        if (ship.getSpeed()  < 0.01 || ship.getSpeed() > 0.99){
            logger.debug(" bad speed ");
            return  true;
        }
        return false;
    }

    private boolean invalidCrewSize(@RequestBody Ship ship){
        if (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999){
            logger.debug(" bad speed or bad crewSize");
            return  true;
        }
        return false;
    }

    private  boolean noBodyOption(@RequestBody Ship ship){
        if(ship.getName() == null || ship.getPlanet() == null || ship.getShipType() == null ||
                ship.getProdDate() == null || ship.getSpeed() == null || ship.getCrewSize() == null  ){
            logger.debug("is null");
            return  true;
        }
        return false;
    }
    private  boolean noBody(@RequestBody Ship ship){
        if(ship.getName() == null && ship.getPlanet() == null && ship.getShipType() == null &&
                ship.getProdDate() == null && ship.getSpeed() == null && ship.getCrewSize() == null  ){
            logger.debug("is null");
            return  true;
        }
        return false;
    }

}

