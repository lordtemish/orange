package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.Service;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by lordtemich on 12/7/17.
 */
public interface ServiceRepo extends MongoRepository<Service, String>{
    Service findById(String id);
    ArrayList<Service> findByServtypeid(String servtypeid);
    ArrayList<Service> findAll();
}
