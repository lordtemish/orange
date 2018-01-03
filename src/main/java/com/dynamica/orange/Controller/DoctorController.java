package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Form.ClientWithDoctorForm;
import com.dynamica.orange.Repo.*;

import com.google.gson.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.print.Doc;
import java.util.*;
import java.util.Map;

/**
 * Created by lordtemich on 10/27/17.
 */
@Controller
@RequestMapping(value = {"/doctor"})
public class DoctorController {
    Logger log=Logger.getLogger(DoctorController.class);
    @Autowired
    ClientRepo clientRepo;
    @Autowired
    DoctorRepo doctorRepo;
    @Autowired
    ServiceTypeRepo serviceTypeRepo;
    @Autowired
    ChatRepo chatRepo;
    FileUploader fileUploader=new FileUploader();
    @RequestMapping(value = "/addDoctor/{id}",method = RequestMethod.POST)
    public String addDoctor(@PathVariable("id") String id, @RequestParam String name,@RequestParam String surname, @RequestParam String dad, @RequestParam String position, @RequestParam String info, @RequestParam String service_type_id, @RequestParam String password){
        Doctor doctor=new Doctor(id,position,info);
        Client client=clientRepo.findById(id);
        client.setName(name);
        client.setSurname(surname);
        client.setDadname(dad);
        client.setPassword(password);
        doctor.setInfo(info);
        doctor.setServicetypeid(service_type_id);
        doctorRepo.save(doctor);
        clientRepo.save(client);
        return "index";
    }
    @RequestMapping(value = {"/setLang/{id}"}, method = RequestMethod.POST)
    public String setLang(@PathVariable("id") String id, @RequestParam String lang){
        Doctor doctor=doctorRepo.findById(id);
        Client client=clientRepo.findById(doctor.getClientid());
        client.setLang(lang);
        clientRepo.save(client);
        return "index";
    }

    //addTimeSchedule
    @RequestMapping(value = "/addOwnService/{id}",method = RequestMethod.POST) //ottid change
    public @ResponseBody boolean addOwnService(@PathVariable("id") String id, @RequestParam String name, @RequestParam String ottid, @RequestParam String info, @RequestParam int price){
        try{
            Doctor doctor=doctorRepo.findById(id);
            doctor.addOwnService(new OwnService(name,ottid,info,price));
            doctorRepo.save(doctor);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/getOwnServices/{id}", method = RequestMethod.POST)
    public @ResponseBody List<OwnService> getOwnServices(@PathVariable("id") String id){
        Doctor doctor=doctorRepo.findById(id);
        return doctor.getOwns();
    }
    @RequestMapping(value="/deleteOwnService/{id}", method = RequestMethod.POST)
    public @ResponseBody boolean deleteOwnService(@PathVariable("id") String id, @RequestParam String ownserviceid){
        try{
            Doctor doctor=doctorRepo.findById(id);
            for(OwnService i:doctor.getOwns()){
                if(i.getId().equals(ownserviceid)){
                    doctor.getOwns().remove(i);
                    return true;
                }
            }
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value = "/setServiceTypeId/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean setSerTypeId(@PathVariable("id")  String id, @RequestParam String service_type_id){
        try {
            ServiceType st = serviceTypeRepo.findById(service_type_id);
            if (st.equals(null)) {
                return false;
            } else {
                Doctor doctor = doctorRepo.findById(id);
                doctor.setServicetypeid(service_type_id);
                doctorRepo.save(doctor);
                return true;
            }
        }
        catch (NullPointerException e){
            return false;
        }
    }
    @RequestMapping(value="/setServices/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean setServices(@PathVariable("id") String id, @RequestParam String services){
        try {
            Doctor doctor = doctorRepo.findById(id);
            doctor.clearServices();
            String[] service=services.split(" ");
            for(int i=0;i<service.length;i++){
                doctor.addService(service[i]);
            }
            doctorRepo.save(doctor);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/servicesId/{id}", method = RequestMethod.POST)
    public @ResponseBody List<String> servicesId(@PathVariable("id") String id){
        Doctor doctor=doctorRepo.findById(id);
        return doctor.getServices();
    }
    @RequestMapping(value="/addCertificate/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean addCertificate(@PathVariable("id") String id, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes){
        Doctor doctor=doctorRepo.findById(id);
        try{
            String url="doctorcertificate-"+doctor.getId();
            int i=0;
            while(true){
                String s=fileUploader.upload(file, url+i);
                if(s.equals("")){
                    i++;
                    continue;
                }
                else{
                    url=s;
                    break;
                }
            }
            doctor.addCertificate(url);
            doctorRepo.save(doctor);
            return true;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/deleteCertificate/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean deleteCertificate(@PathVariable("id") String id, @RequestParam String url){
        Doctor doctor=doctorRepo.findById(id);
        boolean a=false;
        a=doctor.deleteCertificate(url);
        fileUploader.deletePhoto(url);
        doctorRepo.save(doctor);
        return a;
    }
    @RequestMapping(value = "/addEducation/{id}",method = RequestMethod.POST)
    public String addEducation(@PathVariable("id") String id, @RequestParam String ed_type_id,@RequestParam String name, @RequestParam String speciality, @RequestParam String start, @RequestParam String stop){
        Education education=new Education(ed_type_id,name,speciality,start,stop);
        Doctor doctor=doctorRepo.findById(id);
        doctor.addEducation(education);
        doctorRepo.save(doctor);
        return "index";
    }
    @RequestMapping(value="/deleteEducation/{id}/{edid}",method =RequestMethod.POST)
    public @ResponseBody boolean deleteEducation(@PathVariable("id") String id, @PathVariable("edid") String edid){
        try {
            Doctor doctor = doctorRepo.findById(id);
            Education education = doctor.getEducationById(edid);
            for (String i : education.getUrls()) {
                fileUploader.deletePhoto(i);
            }
            doctor.deleteEducation(education);
            doctorRepo.save(doctor);
            return true;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/addEducationCertificate/{id}/{edid}", method = RequestMethod.POST)
    public @ResponseBody boolean addEdCert(@PathVariable("id") String id, @PathVariable("edid") String certid, @RequestParam MultipartFile file,  RedirectAttributes redirectAttributes){
        Doctor doctor=doctorRepo.findById(id);
        try{
            Education ed=doctor.getEducationById(certid);
            String url="educationphoto-"+ed.getId();
            int i=0;
            while(true){
                String s=fileUploader.upload(file, url+i);
                if(s.equals("")){
                    i++;
                    continue;
                }
                else{
                    url=s;
                    break;
                }
            }
            ed.addUrl(url);
            doctor.setEducationById(certid,ed);
            doctorRepo.save(doctor);
            return true;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/deleteEducationCertificate/{id}/{certid}", method = RequestMethod.POST)
    public @ResponseBody boolean deleteEdCert(@PathVariable("id") String id, @PathVariable("certid") String certid, @RequestParam String url){
        Doctor doctor=doctorRepo.findById(id);
        try{
            Education ed=doctor.getEducationById(certid);
            if(ed.getUrls().contains(url)){
                ed.getUrls().remove(url);
            }
            else{
                return false;
            }
            doctorRepo.save(doctor);
            fileUploader.deletePhoto(url);
            return true;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value={"/addProfessionalAch/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean addProfAch(@PathVariable("id") String id, @RequestParam String info){
        try {
            Doctor doctor = doctorRepo.findById(id);
            doctor.addProfAch(info);
            doctorRepo.save(doctor);
            return true;
        }
        catch (NullPointerException e){
            return false;
        }
    }
    @RequestMapping(value={"/deleteProfessionalAch/{id}"}, method = RequestMethod.POST)
    public @ResponseBody boolean deleteProfAch(@PathVariable("id") String id, @RequestParam int index){
        try{
            Doctor doctor=doctorRepo.findById(id);
            doctor.getProfachievments().remove(index);
            doctorRepo.save(doctor);
            return true;
        }
        catch (NullPointerException e){
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value={"/updateProfessionalAch/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean updateProfAch(@PathVariable("id") String id, @RequestParam int index, @RequestParam String info){
        try{
            Doctor doctor=doctorRepo.findById(id);
            doctor.getProfachievments().set(index,info);
            doctorRepo.save(doctor);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value={"/addExtraInfo/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean addExtraInfo(@PathVariable("id") String id, @RequestParam String info){
        try {
            Doctor doctor = doctorRepo.findById(id);
            doctor.addExtraInfo(info);
            doctorRepo.save(doctor);
            return true;
        }
        catch (NullPointerException e){
            return false;
        }
    }
    @RequestMapping(value={"/deleteExtraInfo/{id}"}, method = RequestMethod.POST)
    public @ResponseBody boolean deleteExtraInfo(@PathVariable("id") String id, @RequestParam int index){
        try{
            Doctor doctor=doctorRepo.findById(id);
            doctor.getExtrainfo().remove(index);
            doctorRepo.save(doctor);
            return true;
        }
        catch (NullPointerException e){
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value={"/updateExtraInfo/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean updateExtraInfo(@PathVariable("id") String id, @RequestParam int index, @RequestParam String info){
        try{
            Doctor doctor=doctorRepo.findById(id);
            doctor.getExtrainfo().set(index,info);
            doctorRepo.save(doctor);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    @RequestMapping(value = {"/addPhoto/{id}"}, method = RequestMethod.POST)
    public String addPhoto(@PathVariable("id") String id, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes){
        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "file is empty");

        }
        else{
            Doctor patient=doctorRepo.findById(id);
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
        Doctor patient=doctorRepo.findById(id);
        Client client=clientRepo.findById(patient.getClientid());
        client.deletePhoto(url);
        clientRepo.save(client);
        fileUploader.deletePhoto(url);
        return "index";
    }

    @RequestMapping(value="/addExperience/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean addExperience(@PathVariable("id") String id, @RequestParam String name, @RequestParam int years, @RequestParam String position, @RequestParam String startyear){
        try{
            Doctor doctor=doctorRepo.findById(id);
            Experience experience=new Experience(name, position, years,startyear);
            doctor.addExperience(experience);
            doctorRepo.save(doctor);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/deleteExperience/{id}/{expid}",method = RequestMethod.POST)
    public @ResponseBody boolean deleteExperience(@PathVariable("id") String id, @PathVariable("expid") String experienceid){
        try{
            Doctor doctor=doctorRepo.findById(id);
            doctor.deleteExperiencebyId(experienceid);
            doctorRepo.save(doctor);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/updateExperience/{id}/{expid}",method = RequestMethod.POST)
    public @ResponseBody boolean updateExperience(@PathVariable("id") String id, @PathVariable("expid") String experienceid, @RequestParam String name, @RequestParam int years, @RequestParam String position, @RequestParam String startyear){
        try{
            Doctor doctor=doctorRepo.findById(id);
            doctor.updateExperiencebyId(experienceid,name,position,years,startyear);
            doctorRepo.save(doctor);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value = "/setSchedule/{id}/{name}", method = RequestMethod.POST)
    public @ResponseBody boolean addSchedule(@PathVariable("id") String id, @PathVariable("name") String name, @RequestParam String sch){
        try{
            Doctor doctor=doctorRepo.findById(id);
            Schedule schedule=new Schedule();
            JsonElement jsonElement=new JsonParser().parse(sch);
            JsonObject object=jsonElement.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> set=object.entrySet();
            for(Map.Entry<String, JsonElement> i:set){
                switch (i.getKey()){
                    case "sunday":
                        schedule.setSunday(i.getValue().getAsString());
                        break;
                    case "monday":
                        schedule.setMonday(i.getValue().getAsString());
                        break;
                    case "tuesday":
                        schedule.setTuesday(i.getValue().getAsString());
                        break;
                    case "wednesday":
                        schedule.setWednesday(i.getValue().getAsString());
                        break;
                    case "thursday":
                        schedule.setThursday(i.getValue().getAsString());
                        break;
                    case "friday":
                        schedule.setFriday(i.getValue().getAsString());
                        break;
                    case "saturday":
                        schedule.setSaturday(i.getValue().getAsString());
                        break;
                }
            }
            switch (name){
                case "work":
                    doctor.setWorkSchedule(schedule);
                    break;
                case "home":
                    doctor.setHomeSchedule(schedule);
                    break;
            }
            doctorRepo.save(doctor);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value="/getDoctorInfo/{id}")
    public @ResponseBody
    ClientWithDoctorForm getDoctorInfo(@PathVariable("id") String id){
        Doctor doctor=doctorRepo.findById(id);
        Client client=clientRepo.findById(doctor.getClientid());
        return new ClientWithDoctorForm(client,doctor);
    }
}
