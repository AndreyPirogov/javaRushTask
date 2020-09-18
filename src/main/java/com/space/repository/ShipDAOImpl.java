package com.space.repository;

import com.space.model.Ship;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
@Transactional
@Repository
@Service("jpaContact")
public class ShipDAOImpl implements ShipDAO {

    public static final Logger logger = Logger.getLogger(ShipDAOImpl.class);

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<Ship> allShip() {
        logger.debug("allShip");
        TypedQuery<Ship> typedQuery = em.createNamedQuery("Ship.findAll", Ship.class);
        return typedQuery.getResultList();
    }

    @Override
    public Ship add(Ship ship) {
        em.persist(ship);
        logger.debug("Ship add with id " + ship.getId());
        return ship;
    }

    @Override
    public void delete(Ship ship) {
        Ship mergeShip = em.merge(ship);
        em.remove(mergeShip);
        logger.debug("Ship with id: "+ ship.getId() + " deleted successfully");
    }

    @Override
    public void update(Ship ship) {
        em.merge(ship);

    }

    @Override
    public Ship getById(Long id) {
       logger.debug("getById " + id);
        TypedQuery<Ship> typedQuery = em.createNamedQuery("Contact.findByid", Ship.class);
        typedQuery.setParameter("id", id);
        Ship ship;
        try {
            ship = typedQuery.getSingleResult();
        } catch (NoResultException e){
            ship = null;
        }

        return ship;
    }

    @Override
    public List searchByParameters(String string) {
        logger.debug("searchByParameters: " + string);
        return em.createNativeQuery(string, Ship.class).getResultList();
    }
}
