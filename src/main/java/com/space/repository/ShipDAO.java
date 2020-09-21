package com.space.repository;

import com.space.model.Ship;

import java.util.List;
import java.util.Map;

public interface ShipDAO {

    List<Ship> allShip();
    Ship add(Ship ship);
    void delete(Ship ship);
    void update(Ship ship);
    Ship getById(Long id);
    List<Ship> searchByParameters(String string,int page, int maxResult);
    int searchByParametersForCount(String string);
}
