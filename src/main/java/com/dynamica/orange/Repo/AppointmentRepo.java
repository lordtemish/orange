package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRepo extends MongoRepository<Appointment,String> {
    public Appointment findById(String id);
    public List<Appointment> findByDoctorid(String doctorid);
    public Appointment findByOrderid(String orderid);
    public List<Appointment> findByDoctoridAndDayBetween(String doctorid, long time1, long time2);
    public List<Appointment> findAll();
}
