package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.EducationType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by lordtemich on 12/7/17.
 */
public interface EducationTypeRepo extends MongoRepository<EducationType,String> {
    EducationType findById(String id);
    EducationType findByNameRus(String nameRus);
    EducationType findByNameKaz(String nameKaz);
    ArrayList<EducationType> findAll();
}
