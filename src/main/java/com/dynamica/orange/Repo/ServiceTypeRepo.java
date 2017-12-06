package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.ServiceType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by lordtemich on 12/7/17.
 */
public interface ServiceTypeRepo extends MongoRepository<ServiceType, String> {
    ServiceType findById(String id);

    ServiceType findByNameRus(String nameRus);

    ServiceType findByNameKaz(String nameKaz);

    ArrayList<ServiceType> findAll();
}
