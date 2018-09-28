package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TokenRepo extends MongoRepository<Token,String>{
    Token findById(String id);
    Token findByClientid(String clientid);
    List<Token> findByCurtimestartLessThan(long curtimestart);
    List<Token> findAll();
}
