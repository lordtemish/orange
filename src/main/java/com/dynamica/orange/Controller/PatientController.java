package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.FileUploader;
import com.dynamica.orange.Classes.Patient;
import com.dynamica.orange.Form.ClientWithPatientForm;
import com.dynamica.orange.Repo.ClientRepo;
import com.dynamica.orange.Repo.PatientRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;

/**
 * Created by lordtemich on 10/27/17.
 */
@Controller
@RequestMapping(value = {"/patient"})
public class PatientController {
    private Logger logger = LoggerFactory.getLogger(PatientController.class);
    @Autowired
    ServletContext context;
    @Autowired
    ClientRepo clientRepo;
    @Autowired
    PatientRepo patientRepo;

    FileUploader fileUploader=new FileUploader();
    public static String path="/photo/";

    @RequestMapping(value = {"/addNew/{id}"}, method = RequestMethod.POST)
    public String addNew(@PathVariable("id") String id,@RequestParam String name, @RequestParam String surname, @RequestParam String email, @RequestParam String password){
        Patient patient=new Patient(id);
        Client client=clientRepo.findById(id);
        client.setName(name);client.setSurname(surname);client.setEmail(email);client.setPassword(password);
        clientRepo.save(client);
        patientRepo.save(patient);
        return "index";
    }
    @RequestMapping(value = {"/setLang/{id}"}, method = RequestMethod.POST)
    public String setLang(@PathVariable("id") String id, @RequestParam String lang){
        Patient patient=patientRepo.findById(id);
        Client client=clientRepo.findById(patient.getClientid());
        client.setLang(lang);
        clientRepo.save(client);
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
    @RequestMapping(value = {"/deleteMail/{id}"},method = RequestMethod.POST)
    public String deleteMail(@PathVariable("id") String id,@RequestParam String mail){
        Patient patient=patientRepo.findById(id);
        Client client=clientRepo.findById(patient.getClientid());
        client.deleteMail(mail);
        clientRepo.save(client);
        return "index";
    }
    @RequestMapping(value = {"/addPhone/{id}"},method = RequestMethod.POST)
    public String addPhone(@PathVariable("id") String id,@RequestParam String phone){
        Patient patient=patientRepo.findById(id);
        Client client=clientRepo.findById(patient.getClientid());
        client.addPhone(phone);
        clientRepo.save(client);
        return "index";
    }
    @RequestMapping(value = {"/deletePhone/{id}"},method = RequestMethod.POST)
    public String deletePhone(@PathVariable("id") String id,@RequestParam String phone){
        Patient patient=patientRepo.findById(id);
        Client client=clientRepo.findById(patient.getClientid());
        client.deletePhone(phone);
        clientRepo.save(client);
        return "index";
    }
    @RequestMapping(value = {"/setPush/{id}"},method = RequestMethod.POST)
    public String setPush(@PathVariable("id") String id,@RequestParam boolean push){
        Patient patient=patientRepo.findById(id);
        Client client=clientRepo.findById(patient.getClientid());
        client.setPush(push);
        clientRepo.save(client);
        return "index";
    }
    @RequestMapping(value = {"/setPubl/{id}"},method = RequestMethod.POST)
    public String setPubl(@PathVariable("id") String id,@RequestParam boolean publ){
        Patient patient=patientRepo.findById(id);
        Client client=clientRepo.findById(patient.getClientid());
        client.setPubl(publ);
        clientRepo.save(client);
        return "index";
    }
    @RequestMapping(value = {"/addAddress/{name}/{id}"}, method = RequestMethod.POST)
    public String addAddress(@PathVariable("id") String id, @PathVariable("name") String name, @RequestParam String cityId, @RequestParam String address){
        Patient patient=patientRepo.findById(id);
        switch (name){
            case "work":
                patient.setWorkadr(address);
                patient.setWorkcity(cityId);
                break;
            case "home":
                patient.setHomeadr(address);
                patient.setHomecity(cityId);
                break;
        }
        patientRepo.save(patient);
        return "index";
    }
    @RequestMapping(value = {"/deleteAddress/{name}/{id}"}, method = RequestMethod.POST)
    public String deleteAdress(@PathVariable("id") String id, @PathVariable("name") String name){
        Patient patient=patientRepo.findById(id);
        switch (name){
            case "work":
                patient.setWorkcity(null);
                patient.setWorkadr(null);
                break;
            case "home":
                patient.setHomecity(null);
                patient.setHomeadr(null);
                break;
        }
        patientRepo.save(patient);
        return "index";
    }
    @RequestMapping(value = {"/addPhoto/{id}"}, method = RequestMethod.POST)
    public String addPhoto(@PathVariable("id") String id, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes){
        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "file is empty");

        }
        else{
            Patient patient=patientRepo.findById(id);
            Client client=clientRepo.findById(patient.getClientid());
            int i=0;
            String url="mainphoto-"+patient.getId();
            while(true){
                String s=fileUploader.upload(file, url+i);
                if("".equals(s)) {
                    i++;
                    continue;
                }
                else{
                    url=s;
                    break;
                }
            }
            client.addPhoto(url);
            clientRepo.save(client);
        }
        return "index";
    }
    @RequestMapping(value = {"/deletePhoto/{id}"}, method = RequestMethod.POST)
    public String delPhoto(@PathVariable("id") String id, @RequestParam String url, RedirectAttributes redirectAttributes) {
        Patient patient=patientRepo.findById(id);
        Client client=clientRepo.findById(patient.getClientid());
        client.deletePhoto(url);
        clientRepo.save(client);
        fileUploader.deletePhoto(url);
        return "index";
    }
}
