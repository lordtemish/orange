package com.dynamica.orange.Repo;

import com.dynamica.orange.Classes.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by lordtemich on 1/5/18.
 */
public interface OrderRepo extends MongoRepository<Order, String>{
    public Order findById(String id);
    public ArrayList<Order> findAll();
    public ArrayList<Order> findByStatusAndPatientid(String status, String patientid);
    public ArrayList<Order> findByStatusAndDoctorid(String status, String doctorid);
    public ArrayList<Order> findByPatientidAndDoctorid(String patientid, String doctorid);
    public ArrayList<Order> findByPatientid(String patientid);
    public ArrayList<Order> findByDoctorid(String doctorid);
    public ArrayList<Order> findByDoctoridAndAtwork(String doctorid, boolean atwork);
    public ArrayList<Order> findByDoctoridAndChoseTimeBetween(String doctorid, long min, long max);
    public ArrayList<Order> findByPatientidAndAtwork(String patient, boolean atwork);

}
