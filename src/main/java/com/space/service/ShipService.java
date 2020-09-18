package com.space.service;

import com.space.model.Ship;

import java.util.List;
import java.util.Map;

public interface ShipService {

    List<Ship> allShip();
    Ship add(Ship ship);
    void delete(Ship ship);
    Ship update(Ship ship);
    Ship getById(Long id);
    List<Ship> searchByParameters(Map<String, Object> path);

}
