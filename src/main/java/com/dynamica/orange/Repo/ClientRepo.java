package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by lordtemich on 12/7/17.
 */
public interface ClientRepo extends MongoRepository<Client,String>{
    public Client findById(String id);
    public Client findByPhone(String phone);
    public ArrayList<Client> findAll();
}
