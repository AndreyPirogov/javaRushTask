package com.space.service;

import com.space.model.Ship;

import java.util.List;

public interface ShipService {

    List<Ship> allShip();
    Ship add(Ship ship);
    void delete(Ship ship);
    Ship update(Ship ship);
    Ship getById(Long id);

}
