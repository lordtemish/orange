package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by lordtemich on 12/7/17.
 */
public interface PatientRepo extends MongoRepository<Patient,String> {
    public Patient findById(String id);
    public Patient findByClientid(String clientid);
    public ArrayList<Patient> findAll();
}
