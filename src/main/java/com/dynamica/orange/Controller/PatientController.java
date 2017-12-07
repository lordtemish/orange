package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Patient;
import com.dynamica.orange.Repo.ClientRepo;
import com.dynamica.orange.Repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by lordtemich on 10/27/17.
 */
@Controller
@RequestMapping(value = {"/patient"})
public class PatientController {
    @Autowired
    ClientRepo clientRepo;
    @Autowired
    PatientRepo patientRepo;

    @RequestMapping(value = {"/addNew/{id}"}, method = RequestMethod.POST)
    public String addNew(@PathVariable("id") String id,@RequestParam String name, @RequestParam String surname, @RequestParam String email, @RequestParam String password){
        Patient patient=new Patient(id);
        Client client=clientRepo.findById(id);
        client.setName(name);client.setSurname(surname);client.setEmail(email);client.setPassword(password);
        clientRepo.save(client);
        patientRepo.save(patient);
        return "index";
    }

}
