package com.space.repository;

import com.space.model.Ship;

import java.util.List;

public interface ShipDAO {

    List<Ship> allShip();
    Ship add(Ship ship);
    void delete(Ship ship);
    void update(Ship ship);
    Ship getById(Long id);
}
