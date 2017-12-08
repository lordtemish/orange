package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Patient;
import com.dynamica.orange.Form.ClientWithPatientForm;
import com.dynamica.orange.Repo.ClientRepo;
import com.dynamica.orange.Repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

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
    @RequestMapping(value = {"/addinfo/{id}"},method = RequestMethod.POST)
    public String addInfo(@PathVariable("id") String id, @RequestParam String gender, @RequestParam String dateofbirth, @RequestParam int weight, @RequestParam int height, @RequestParam String bloodid, @RequestParam String chronic, @RequestParam String alergic){
        Patient patient=patientRepo.findById(id);
        patient.setGender(gender.toUpperCase());
        patient.setDate(dateofbirth);
        patient.setWeight(weight);
        patient.setHeight(height);
        patient.setBlood(bloodid);
        patient.setChronic(chronic);
        patient.setAlergic(alergic);
        patientRepo.save(patient);
        return "index";
    }
    @RequestMapping(value={"/getPatientInfo/{id}"}, method = RequestMethod.POST)
    public @ResponseBody ClientWithPatientForm getPatientInfo(@PathVariable("id") String id){
        Patient patient=patientRepo.findById(id);
        Client client=clientRepo.findById(patient.getClientid());
        ClientWithPatientForm clientWithPatientForm=new ClientWithPatientForm(client,patient);
        return clientWithPatientForm;
    }
    @RequestMapping(value = {"/addMail/{id}"},method = RequestMethod.POST)
    public String addMail(@PathVariable("id") String id,@RequestParam String mail){
        Patient patient=patientRepo.findById(id);
        Client client=clientRepo.findById(patient.getClientid());
        client.addEmail(mail);
        clientRepo.save(client);
        return "index";
    }
}
