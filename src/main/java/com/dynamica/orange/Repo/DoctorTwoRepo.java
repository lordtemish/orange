package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by lordtemich on 12/23/17.
 */
public interface DoctorTwoRepo extends MongoRepository<Doctor, String> {
    Doctor findById(String id);
    Doctor findByClientid(String clientid);
    ArrayList<Doctor> findAll();
}
