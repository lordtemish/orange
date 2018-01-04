package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by lordtemich on 1/3/18.
 */
public interface ChatRepo extends MongoRepository<Chat,String> {
    public Chat findById(String id);
    public Chat findOneByDoctoridAndPatientid(String doctorid, String patientid);
    public ArrayList<Chat> findByDoctorid(String doctorid);
    public ArrayList<Chat> findByPatientid(String patientid);
    public ArrayList<Chat> findAll();
}
