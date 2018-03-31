package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Form.*;
import com.dynamica.orange.Repo.*;

import com.google.gson.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
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
    TokenRepo tokenRepo;
    @Autowired
    PatientRepo patientRepo;
    @Autowired
    DoctorRepo doctorRepo;
    @Autowired
    ServiceTypeRepo serviceTypeRepo;
    @Autowired
    ServiceRepo serviceRepo;
    @Autowired
    ChatRepo chatRepo;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    EducationTypeRepo educationTypeRepo;
    FileUploader fileUploader=new FileUploader();

    @RequestMapping(value = "/addDoctor",method = RequestMethod.POST)
    public  @ResponseBody Object addDoctor(@RequestHeader("token") String token,@RequestParam String name,@RequestParam String surname, @RequestParam String dad, @RequestParam String position, @RequestParam String info, @RequestParam String service_type_id, HttpServletRequest request){
        try {
            Token tok1=tokenRepo.findById(token);
            if(tok1!=null) {
                Doctor doctor = new Doctor(tok1.getClientid(), position, info);
                Client client = clientRepo.findById(tok1.getClientid());
                client.setName(name);
                client.setSurname(surname);
                client.setDadname(dad);
                doctor.setInfo(info);
                doctor.setServicetypeid(service_type_id);
                doctorRepo.save(doctor);
                clientRepo.save(client);
                return new StatusObject("ok");
            }
            else{
                return new StatusObject("noauth");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");}
    }
    @RequestMapping(value={"/getDoctorId"},method = RequestMethod.POST)
    public @ResponseBody Object getPatientId(@RequestHeader("token") String token){
        Token tok=tokenRepo.findById(token);
        if(tok!=null){
            try{
                Doctor patient=doctorRepo.findByClientid(tok.getClientid());
                return new DoctorIdForm(patient.getId());
            }
            catch (NullPointerException ee){
                return new StatusObject("nullpointerexception");
            }
            catch (Exception e){
                return new StatusObject("exception");
            }
        }
        else {
            return new StatusObject("noauth");
        }
    }
    @RequestMapping(value = {"/setAddress/{name}"}, method = RequestMethod.POST)
    public  @ResponseBody Object addAddress(@RequestHeader("token") String token, @PathVariable("name") String name, @RequestParam String cityId, @RequestParam String address, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            switch (name) {
                case "work":
                    doctor.setWorkAddress(new Address(cityId,address));
                    break;
                case "home":
                    doctor.setHomeAddress(new Address(cityId,address));
                    break;
            }
            doctorRepo.save(doctor);
            return new StatusObject("ok");}
            return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/setLang"}, method = RequestMethod.POST)
    public  @ResponseBody Object setLang(@RequestHeader("token") String token,@RequestParam String lang, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            Client client = clientRepo.findById(doctor.getClientid());
            client.setLang(lang.toUpperCase());
            clientRepo.save(client);
            return new StatusObject("ok");}
        return new StatusObject("noauth");
    }

    //addTimeSchedule
    @RequestMapping(value = "/addOwnService",method = RequestMethod.POST) //ottid change
    public @ResponseBody Object addOwnService(@RequestHeader("token") String token, @RequestParam String name, @RequestParam String ottid, @RequestParam String info, @RequestParam int price, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                doctor.addOwnService(new OwnService(name, ottid, info, price));
                doctorRepo.save(doctor);
                return new StatusObject("ok");}
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/getOwnServices", method = RequestMethod.POST)
    public @ResponseBody Object getOwnServices(@RequestHeader("token") String token, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            return doctor.getOwns();
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value="/deleteOwnService", method = RequestMethod.POST)
    public @ResponseBody Object deleteOwnService(@RequestHeader("token") String token, @RequestParam String ownserviceid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                for (OwnService i : doctor.getOwns()) {
                    if (i.getId().equals(ownserviceid)) {
                        doctor.getOwns().remove(i);
                        return new StatusObject("ok");
                    }
                }
                doctorRepo.save(doctor);
            }
            else
            return new StatusObject("noauth");

            return new StatusObject("noobject");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = "/setServiceTypeId/",method = RequestMethod.POST)
    public @ResponseBody Object setSerTypeId(@RequestHeader("token") String token, @RequestParam String service_type_id, HttpServletRequest request){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                ServiceType st = serviceTypeRepo.findById(service_type_id);
                if (st.equals(null)) {
                    return new StatusObject("noobject");
                } else {
                    Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                    doctor.setServicetypeid(service_type_id);
                    doctorRepo.save(doctor);
                    return new StatusObject("ok");
                }
            }
            return new StatusObject("noauth");
        }
        catch (NullPointerException e){
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/setServices",method = RequestMethod.POST)
    public @ResponseBody Object setServices(@RequestHeader("token") String token,@RequestParam String services, HttpServletRequest request){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                doctor.clearServices();
                String[] service = services.split(" ");
                for (int i = 0; i < service.length; i++) {
                    doctor.addService(service[i]);
                }
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
        return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }

    @RequestMapping(value="/servicesId", method = RequestMethod.POST)
    public @ResponseBody Object servicesId(@RequestHeader("token") String token, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            return doctor.getServices();
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value="/getServices/", method = RequestMethod.POST)
    public @ResponseBody Object getServices(@RequestHeader("token") String token, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                List<Service> services=new ArrayList<>();
                for(String i:doctor.getServices()){
                    services.add(serviceRepo.findById(i));
                }
                return services;
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/addMail",method = RequestMethod.POST)
    public @ResponseBody Object addMail(@RequestHeader("token") String token, @RequestParam String mail){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            Client client=clientRepo.findById(doctor.getClientid());
            client.addEmail(mail);
            clientRepo.save(client);
            return new StatusObject("ok");
                }
                return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/deleteMail",method = RequestMethod.POST)
    public @ResponseBody Object deleteMail(@RequestHeader("token") String token, @RequestParam String mail){
        try{Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                Client client = clientRepo.findById(doctor.getClientid());
                boolean aa = client.deleteMail(mail);
                clientRepo.save(client);
                if (aa) {
                    return new StatusObject("ok");
                } else {
                    return new StatusObject("nomail");
                }
            }else{
                return new StatusObject("noauth");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }

    @RequestMapping(value="/addPhone",method = RequestMethod.POST)
    public @ResponseBody Object addPhone(@RequestHeader("token") String token,@RequestParam String phone){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                Client client = clientRepo.findById(doctor.getClientid());
                client.addPhone(phone);
                clientRepo.save(client);
                return new StatusObject("ok");
            }
            else{
                return new StatusObject("noauth");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/deletePhone",method = RequestMethod.POST)
    public @ResponseBody Object deletePhone(@RequestHeader("token") String token, @RequestParam String phone){
        try{Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client = clientRepo.findById(tok.getClientid());
                boolean aa = client.deletePhone(phone);
                clientRepo.save(client);
                if (aa) {
                    return new StatusObject("ok");
                } else {
                    return new StatusObject("nophone");
                }
            }
            else{
                return new StatusObject("noauth");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/addCertificate",method = RequestMethod.POST)
    public @ResponseBody Object addCertificate(@RequestHeader("token") String token, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            try {
                String url = "doctorcertificate-" + doctor.getId();
                int i = 0;
                while (true) {
                    String s = fileUploader.upload(file, url + i);
                    if (s.equals("")) {
                        i++;
                        continue;
                    } else {
                        url = s;
                        break;
                    }
                }
                doctor.addCertificate(url);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            } catch (NullPointerException e) {
                e.printStackTrace();
                return new StatusObject("exception");
            }
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value="/deleteCertificate/",method = RequestMethod.POST)
    public @ResponseBody Object deleteCertificate(@RequestHeader("token") String token, @RequestParam String url, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            boolean a = false;
            a = doctor.deleteCertificate(url);
            try {
                fileUploader.deletePhoto(url);
            }catch (Exception e){}
            doctorRepo.save(doctor);
            if (a) {
                return new StatusObject("ok");
            } else {
                return new StatusObject("nocert");
            }
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value = "/addEducation",method = RequestMethod.POST)
    public @ResponseBody Object addEducation(@RequestHeader("token") String token, @RequestParam String ed_type_id,@RequestParam String name, @RequestParam String speciality, @RequestParam String start, @RequestParam String stop, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Education education = new Education(ed_type_id, name, speciality, start, stop);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            doctor.addEducation(education);
            doctorRepo.save(doctor);
            return new StatusObject("ok");
        }
        return new StatusObject("ok");
    }
    @RequestMapping(value="/deleteEducation/{edid}",method =RequestMethod.POST)
    public @ResponseBody Object deleteEducation(@RequestHeader("token") String token, @PathVariable("edid") String edid, HttpServletRequest request){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                Education education = doctor.getEducationById(edid);
                for (String i : education.getUrls()) {
                    fileUploader.deletePhoto(i);
                }
                doctor.deleteEducation(education);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return new StatusObject("nullpointerexception");
        }
    }
    @RequestMapping(value="/addEducationCertificate/{edid}", method = RequestMethod.POST)
    public @ResponseBody Object addEdCert(@RequestHeader("token") String token, @PathVariable("edid") String certid, @RequestParam MultipartFile file,  RedirectAttributes redirectAttributes, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            try {
                Education ed = doctor.getEducationById(certid);
                String url = "educationphoto-" + ed.getId();
                int i = 0;
                while (true) {
                    String s = fileUploader.upload(file, url + i);
                    if (s.equals("")) {
                        i++;
                        continue;
                    } else {
                        url = s;
                        break;
                    }
                }
                ed.addUrl(url);
                doctor.setEducationById(certid, ed);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            } catch (NullPointerException e) {
                e.printStackTrace();
                return new StatusObject("nullpointerexception");
            }
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value="/deleteEducationCertificate/{certid}", method = RequestMethod.POST)
    public @ResponseBody Object deleteEdCert(@RequestHeader("token") String token, @PathVariable("certid") String certid, @RequestParam String url, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            try {
                Education ed = doctor.getEducationById(certid);
                if (ed.getUrls().contains(url)) {
                    ed.getUrls().remove(url);
                } else {
                    return new StatusObject("noauth");
                }
                doctorRepo.save(doctor);
                fileUploader.deletePhoto(url);
                return new StatusObject("ok");
            } catch (NullPointerException e) {
                e.printStackTrace();
                return new StatusObject("nullpointerexception");
            }
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value="/getEducationList",method = RequestMethod.POST)
    @ResponseBody Object getEducations(@RequestHeader("token") String token, HttpServletRequest request){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                List<EducationForm> educationForms=new ArrayList<>();
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                for(Education i:doctor.getEducations()){
                    educationForms.add(new EducationForm(i,educationTypeRepo.findById(i.getEd_type_id())));
                }
                return educationForms;
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value={"/addProfessionalAch"},method = RequestMethod.POST)
    public @ResponseBody Object addProfAch(@RequestHeader("token") String token, @RequestParam String info, HttpServletRequest request){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                doctor.addProfAch(info);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (NullPointerException e){
            return new StatusObject("nullpointerexception");
        }
    }
    @RequestMapping(value={"/deleteProfessionalAch"}, method = RequestMethod.POST)
    public @ResponseBody Object deleteProfAch(@RequestHeader("token") String token, @RequestParam int index, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                doctor.getProfachievments().remove(index);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (NullPointerException e){
            return new StatusObject("nullpointerexception");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value={"/updateProfessionalAch"},method = RequestMethod.POST)
    public @ResponseBody Object updateProfAch(@RequestHeader("token") String token, @RequestParam int index, @RequestParam String info, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                doctor.getProfachievments().set(index, info);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }

    @RequestMapping(value={"/addExtraInfo"},method = RequestMethod.POST)
    public @ResponseBody Object addExtraInfo(@RequestHeader("token") String token, @RequestParam String info, HttpServletRequest request){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                doctor.addExtraInfo(info);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (NullPointerException e){
            return new StatusObject("nullpointerexception");
        }
    }
    @RequestMapping(value={"/deleteExtraInfo"}, method = RequestMethod.POST)
    public @ResponseBody Object deleteExtraInfo(@RequestHeader("token") String token, @RequestParam int index,HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                doctor.getExtrainfo().remove(index);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (NullPointerException e){
            return new StatusObject("nullpointerexception");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value={"/updateExtraInfo"},method = RequestMethod.POST)
    public @ResponseBody Object updateExtraInfo(@RequestHeader("token") String token, @RequestParam int index, @RequestParam String info, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                doctor.getExtrainfo().set(index, info);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }



    @RequestMapping(value = {"/addPhoto"}, method = RequestMethod.POST)
    public @ResponseBody  Object addPhoto(@RequestHeader("token") String token, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request){
        try {
            Token tok = tokenRepo.findById(token);
            if (tok != null) {
                if (file.isEmpty()) {
                    redirectAttributes.addFlashAttribute("message", "file is empty");
                    return new StatusObject("emptyfile");
                } else {
                    log.info(file.getSize());
                    Doctor patient = doctorRepo.findByClientid(tok.getClientid());
                    Client client = clientRepo.findById(patient.getClientid());
                    int i = 0;
                    String url = "mainphoto-" + patient.getId();
                    while (true) {
                        String s = fileUploader.upload(file, url + i);
                        if ("".equals(s)) {
                            i++;
                            continue;
                        } else {
                            url = s;
                            break;
                        }
                    }
                    client.addPhoto(url);
                    log.info(url);
                    clientRepo.save(client);
                    return new StatusObject("ok");
                }

            }
            return new StatusObject("noauth");
        }
        catch (Exception e){
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = {"/deletePhoto"}, method = RequestMethod.POST)
    public @ResponseBody Object delPhoto(@RequestHeader("token") String token, @RequestParam String url, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Client client = clientRepo.findById(tok.getClientid());
            client.deletePhoto(url);
            clientRepo.save(client);
            fileUploader.deletePhoto(url);return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }

    @RequestMapping(value="/addExperience",method = RequestMethod.POST)
    public @ResponseBody Object addExperience(@RequestHeader("token") String token,@RequestParam String name, @RequestParam int years, @RequestParam String position, @RequestParam String startyear, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                Experience experience = new Experience(name, position, years, startyear);
                doctor.addExperience(experience);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/deleteExperience/{expid}",method = RequestMethod.POST)
    public @ResponseBody Object deleteExperience(@RequestHeader("token") String token, @PathVariable("expid") String experienceid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                doctor.deleteExperiencebyId(experienceid);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/updateExperience/{expid}",method = RequestMethod.POST)
    public @ResponseBody Object updateExperience(@RequestHeader("token") String token, @PathVariable("expid") String experienceid, @RequestParam String name, @RequestParam int years, @RequestParam String position, @RequestParam String startyear, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                doctor.updateExperiencebyId(experienceid, name, position, years, startyear);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = "/setSchedule/{name}", method = RequestMethod.POST)
    public @ResponseBody Object addSchedule(@RequestHeader("token") String token, @PathVariable("name") String name, @RequestParam String sch, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                Schedule schedule = new Schedule();
                JsonElement jsonElement = new JsonParser().parse(sch);
                JsonObject object = jsonElement.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> set = object.entrySet();
                for (Map.Entry<String, JsonElement> i : set) {
                    switch (i.getKey()) {
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
                switch (name) {
                    case "work":
                        doctor.setWorkSchedule(schedule);
                        break;
                    case "home":
                        doctor.setHomeSchedule(schedule);
                        break;
                }
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }

    }
    @RequestMapping(value="/openChat", method = RequestMethod.POST)
    public @ResponseBody Object openChat(@RequestHeader("token") String token, @RequestParam String patientid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                String id=doctor.getId();
                Chat chat = chatRepo.findOneByDoctoridAndPatientid(doctor.getId(), patientid);
                Patient doctor1 = patientRepo.findById(patientid);
                if (doctor1 == null) {
                    return  null;
                }
                if (doctor == null) {
                    return  null;
                }
                if (chat == null) {
                    Chat chat1 = new Chat(doctor.getId(), patientid);
                    chatRepo.save(chat1);
                    chat = chatRepo.findOneByDoctoridAndPatientid(id, patientid);
                }
                return chat.getId();
            }
            else return new StatusObject("noauth");
        }
        catch (NullPointerException e){
            log.info(null);
            return new StatusObject("nullpointerexception");
        }
        catch (Exception e){
            return new StatusObject("exception");
        }
    }

    @RequestMapping(value="/getAllChats",method = RequestMethod.POST)
    public @ResponseBody Object getAllChats(@RequestHeader("token") String token, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                String id=doctor.getId();
            return chatRepo.findByDoctorid(id);
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value="/getAllChatsList", method = RequestMethod.POST)
    public @ResponseBody Object getAllChatsList(@RequestHeader("token") String token, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                String id=doctor.getId();
                List<Chat> chats = chatRepo.findByDoctorid(id);
                List<ChatListForm> forms = new ArrayList<>();
                for (Chat i : chats) {
                    forms.add(new ChatListForm(i, i.getLastMessage(), clientRepo.findById(i.getLastMessage().getClientid())));
                }
                return forms;
            }
            else return new StatusObject("noauth");
        }
        catch(Exception e){
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/sendTextMessage/{chatid}",method = RequestMethod.POST)
    public @ResponseBody Object sendTextMes(@RequestHeader("token") String token,@PathVariable("chatid") String chatid, @RequestParam String text, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                Chat chat = chatRepo.findById(chatid);
                Message message = new Message(doctor.getClientid(), "text", text);
                chat.addMessage(message);
                chat.setStatus("doctor");
                chat.unreadPlus();
                chatRepo.save(chat);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/sendFileMessage/{chatid}",method = RequestMethod.POST)
    public @ResponseBody Object sendFileMes(@RequestHeader("token") String token,@PathVariable("chatid") String chatid, @RequestParam String type, @RequestParam MultipartFile file, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());

                Chat chat = chatRepo.findById(chatid);
                Message message = new Message(doctor.getClientid(), type);
                chat.addMessage(message);
                message = chat.getMessages().get(chat.getMessages().size() - 1);
                chat.getMessages().remove(message);
                String url = "message-" + message.getId();
                int i = 0;
                while (true) {
                    String s = fileUploader.uploadMessageFile(file, url + i);
                    if (s.equals("")) {
                        i++;
                        continue;
                    } else {
                        url = s;
                        break;
                    }
                }
                message.setInfo(url);
                chat.getMessages().add(message);
                chatRepo.save(chat);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/getMessages/{chatid}", method = RequestMethod.POST)
    public @ResponseBody Object getMessages(@RequestHeader("token") String token, @PathVariable("chatid") String chatid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                Chat chat = chatRepo.findById(chatid);
                if (!chat.getStatus().equals("doctor")) {
                    chat.setUnread(0);
                }
                chatRepo.save(chat);
                return chat.getMessages();
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = "/getListOrders",method = RequestMethod.POST)
    public @ResponseBody Object getListOrders(@RequestHeader("token") String token, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor1=doctorRepo.findByClientid(tok.getClientid());
                String id=doctor1.getId();
                ArrayList<Order> orders=orderRepo.findByDoctorid(id);
                ArrayList<OrderListForm> orderListForms=new ArrayList<>();
                for(Order i:orders){
                    Doctor doctor=doctorRepo.findById(i.getDoctorid());
                    Client client=clientRepo.findById(doctor.getClientid());
                    ServiceType serviceType=serviceTypeRepo.findById(doctor.getServicetypeid());
                    ArrayList<Service> services=new ArrayList<>();
                    for(String j:doctor.getServices()){
                        services.add(serviceRepo.findById(j));
                    }
                    ArrayList<EducationForm> educationForms=new ArrayList<>();
                    for(Education j:doctor.getEducations()){
                        educationForms.add(new EducationForm(j, educationTypeRepo.findById(j.getEd_type_id())));
                    }
                    orderListForms.add(new OrderListForm(i,doctor,client,serviceType,services,educationForms));
                }
                return orderListForms;
            }
            return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/getMyOrders", method = RequestMethod.POST)
    public @ResponseBody Object getMyOrders(@RequestHeader("token") String token, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                String id=doctor.getId();
                return orderRepo.findByDoctorid(id);}
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/cancelOrder/{orderid}",method = RequestMethod.POST)
    public @ResponseBody Object cancelOrder(@RequestHeader("token") String token,@PathVariable("orderid") String orderid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Order order = orderRepo.findById(orderid);
                order.setStatus("doctorcancelled");
                orderRepo.save(order);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/acceptOrder/{orderid}", method = RequestMethod.POST)
    public @ResponseBody Object acceptOrder(@RequestHeader("token") String token,@PathVariable("orderid") String orderid, HttpServletRequest request) {
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null) {
                Order order = orderRepo.findById(orderid);
                order.setStatus("doctoraccepted");
                orderRepo.save(order);
                return new StatusObject("ok");
            }
            else{
                return new StatusObject("noauth");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = "/setTextAnswers/{orderid}",method = RequestMethod.POST)
    public @ResponseBody Object setTextAnswers(@RequestHeader("token") String token, @PathVariable("orderid") String orderid, @RequestParam String diagnos, @RequestParam String healing, @RequestParam String comment, HttpServletRequest request){
        try{
                Token tok= tokenRepo.findById(token);
                if(tok!=null) {
                    Order order = orderRepo.findById(orderid);
                    order.setDiagnosAnswer(diagnos);
                    order.setHealingAnswer(healing);
                    order.setTextAnswer(comment);
                    order.setStatus("doctoranswered");
                    orderRepo.save(order);
                    return new StatusObject("ok");
                }
                else{
                    return new StatusObject("noauth");
                }
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/setAudioHealingAnswer/{orderid}", method = RequestMethod.POST)
    public @ResponseBody Object setAudioHealingAnswer(@RequestHeader("token") String token, @PathVariable("orderid") String orderid, @RequestParam MultipartFile file, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Order order = orderRepo.findById(orderid);
                String url = "ordermessage-" + order.getId();
                int i = 0;
                while (true) {
                    String s = fileUploader.uploadOrderFile(file, url + i);
                    if (s.equals("")) {
                        i++;
                        continue;
                    } else {
                        url = s;
                        break;
                    }
                }
                order.setAudiohealing(url);
                orderRepo.save(order);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/addOrderFileComment/{orderid}",method=RequestMethod.POST)
    public @ResponseBody Object addOrderFileComment(@RequestHeader("token") String token, @PathVariable("orderid") String orderid, @RequestParam String type,@RequestParam MultipartFile file, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Order order = orderRepo.findById(orderid);
                String url = "ordermessage-" + order.getId();
                int i = 0;
                while (true) {
                    String s = fileUploader.uploadOrderFile(file, url + i);
                    if (s.equals("")) {
                        i++;
                        continue;
                    } else {
                        url = s;
                        break;
                    }
                }
                switch (type) {
                    case "photo":
                        order.setPhotoAnswer(url);
                        break;
                    case "audio":
                        order.setAudioAnswer(url);
                        break;
                }
                orderRepo.save(order);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/testRequest",method = RequestMethod.POST)
    public @ResponseBody Object test(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null)
        log.info("all is good");
        return new StatusObject("ok");
    }
    @RequestMapping(value="/getDoctorInfo/{id}")
    public @ResponseBody Object getDoctorInfo(@RequestHeader("token") String token,@PathVariable("id") String id, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Doctor doctor = doctorRepo.findById(id);
            Client client = clientRepo.findById(doctor.getClientid());
            return new ClientWithDoctorForm(client, doctor);
        }
        return new StatusObject("noauth");
    }
}
