package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.OwnTimeType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by lordtemich on 10/28/17.
 */
public interface OwnTimeTypeRepo extends MongoRepository<OwnTimeType,String>{
    public OwnTimeType findById(String id);
    public OwnTimeType findByNamerus(String namerus);
    public OwnTimeType findByNamekaz(String namekaz);
    public ArrayList<OwnTimeType> findAll();
}
