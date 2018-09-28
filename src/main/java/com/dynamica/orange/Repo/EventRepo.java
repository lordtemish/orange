package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepo extends MongoRepository<Event, String> {
    public Event findEventById(String id);
    public List<Event> findByDoctorid(String doctorid);
    public List<Event> findAll();
}
