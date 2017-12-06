package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by lordtemich on 10/27/17.
 */
public interface CityRepo extends MongoRepository<City,String>{
    public City findById(String id);
    public City findByNameRus(String nameRus);
    public City findByNameKaz(String nameKaz);
    public ArrayList<City> findAll();
}
