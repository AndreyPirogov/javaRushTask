package com.space.controller;


import com.google.protobuf.MapEntry;
import com.space.model.Ship;
import com.space.model.ShipChekHelper;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.*;

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
    public ResponseEntity<List<Ship>> allShips(@RequestParam  Map<String, String> path){

        List<Ship> list = service.searchByParameters(path);

        if(list == null)return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(list,  HttpStatus.OK);
    }



    @RequestMapping(value = "/ships/count",method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Integer> countShips(@RequestParam  Map<String, String> path) {

        return new ResponseEntity<>(service.searchByParametersForCount(path), HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> findShip (@PathVariable("id") Long id){
        logger.debug("findShip " + id);
        if(ShipChekHelper.invalidID(id)){
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
        if(ShipChekHelper.noBodyOption(ship) ) return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(ship.getUsed() == null) ship.setUsed(false);
        if(ShipChekHelper.invalidCrewSize(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (ShipChekHelper.invalidPlanet(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        double resultSpeed =Math.round(ship.getSpeed() * 100) / 100.0;
        logger.debug("resultSpeed=" + resultSpeed);
        ship.setSpeed(resultSpeed);
        if(ShipChekHelper.invalidSpeed(ship))return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if(ShipChekHelper.invalidCrewSize(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (ShipChekHelper.invalidDate(ship)) return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

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
        if(ShipChekHelper.noBody(ship)) {
            return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
        }
        if(ship.getName() != null) {
            if(ShipChekHelper.invalidName(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(ship.getPlanet() != null) {
            if (ShipChekHelper.invalidPlanet(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(ship.getProdDate() != null){
            if (ShipChekHelper.invalidDate(ship)) return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(ship.getSpeed() != null) {
            double resultSpeed = Math.round(ship.getSpeed() * 100) / 100.0;
            logger.debug("resultSpeed=" + resultSpeed);
            ship.setSpeed(resultSpeed);
            if(ShipChekHelper.invalidSpeed(ship))return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        if(ship.getCrewSize() != null){
            if(ShipChekHelper.invalidCrewSize(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        ship.setId(id);
        Ship ship1 = service.update(ship);
        return new ResponseEntity<>(ship1, HttpStatus.OK);
    }




}

