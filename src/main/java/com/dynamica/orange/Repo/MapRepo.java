package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.Map;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by lordtemich on 12/7/17.
 */
public interface MapRepo extends MongoRepository<Map, String>{
    Map findById(String id);
    ArrayList<Map> findAll();
}
