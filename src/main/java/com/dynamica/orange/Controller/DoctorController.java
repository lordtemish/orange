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
    CityRepo cityRepo;
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
    @Autowired
    OwnTimeTypeRepo ownTimeTypeRepo;
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
                client.onReqested();
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
    @RequestMapping(value = {"/setAddress"}, method = RequestMethod.POST)
    public  @ResponseBody Object addAddress(@RequestHeader("token") String token, @RequestParam String name, @RequestParam String cityId, @RequestParam String address, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
    @RequestMapping(value = {"/setAddressWithCompany"}, method = RequestMethod.POST)
    public  @ResponseBody Object addAddressWC(@RequestHeader("token") String token, @RequestParam String name, @RequestParam String cityId, @RequestParam String address,@RequestParam String company, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            Client client=clientRepo.findById(tok.getClientid());
            client.onReqested();
            clientRepo.save(client);
            Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            Address address1=new Address(cityId,address);
            address1.setName(company);
            switch (name) {
                case "work":
                    doctor.setWorkAddress(address1);
                    break;
                case "home":
                    doctor.setHomeAddress(address1);
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
            client.onReqested();
            clientRepo.save(client);
            return new StatusObject("ok");}
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/setPubl"}, method = RequestMethod.POST)
    public  @ResponseBody Object setPubl(@RequestHeader("token") String token,@RequestParam boolean publ, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            Client client = clientRepo.findById(tok.getClientid());
            client.setPubl(publ);
            client.onReqested();
            clientRepo.save(client);
            return new StatusObject("ok");}
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/setPush"}, method = RequestMethod.POST)
    public  @ResponseBody Object setPush(@RequestHeader("token") String token,@RequestParam boolean push, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            Client client = clientRepo.findById(tok.getClientid());
            client.setPush(push);
            client.onReqested();
            clientRepo.save(client);
            return new StatusObject("ok");}
        return new StatusObject("noauth");
    }
    @RequestMapping(value = "/setHomeplaceOwnService",method = RequestMethod.POST) //ottid change
    public @ResponseBody Object setHomeplaceOwnService(@RequestHeader("token") String token, @RequestParam String name,@RequestParam int price, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                for(OwnService i : doctor.getOwns()){
                    if(i.getHomeplace()){
                        doctor.deleteOwnService(i);
                        break;
                    }
                }
                doctor.addOwnService(new OwnService(name,price));
                doctorRepo.save(doctor);
                return new StatusObject("ok");}
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    //addTimeSchedule
    @RequestMapping(value = "/addOwnService",method = RequestMethod.POST) //ottid change
    public @ResponseBody Object addOwnService(@RequestHeader("token") String token, @RequestParam String name, @RequestParam String ottid, @RequestParam String info, @RequestParam int price, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                for(OwnService i:doctor.getOwns()){
                    i.setOwn_time_type_id(ownTimeTypeRepo.findById(i.getOwn_time_type_id()+""));
                    log.info(i.getId());
                }
            return doctor.getOwns();
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value="/deleteOwnService", method = RequestMethod.POST)
    public @ResponseBody Object deleteOwnService(@RequestHeader("token") String token, @RequestParam String ownserviceid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                /*for (OwnService i : doctor.getOwns()) {
                    if (i.getId().equals(ownserviceid)) {
                        doctor.deleteOwnService(i);
                        return new StatusObject("ok");
                    }
                }*/
                Boolean deleted=doctor.deleteOwnServiceById(ownserviceid);
                doctorRepo.save(doctor);
             //   log.info(deleted);
                if(deleted){
                    return new StatusObject("ok");
                }
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                List<Service> services=new ArrayList<>();
                for(IDObject i:doctor.getServices()){
                    services.add(serviceRepo.findById(i.getId()));
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
            client.onReqested();
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
                client.onReqested();
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
                client.onReqested();
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
                client.onReqested();
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
    public @ResponseBody Object addCertificate(@RequestHeader("token") String token, @RequestParam List<String> files, RedirectAttributes redirectAttributes, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            try {
               /* String url = "doctorcertificate-" + doctor.getId();
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
                }*/
                for(String i:files) {
                    FileObjectForm fileObjectForm = new FileObjectForm(i);
                    doctor.addCertificate(fileObjectForm);
                }
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
    public @ResponseBody Object deleteCertificate(@RequestHeader("token") String token, @RequestParam String id, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            boolean a = false;
            a = doctor.deleteCertificate(id);
            doctorRepo.save(doctor);
            if (a) {
                return new StatusObject("ok");
            } else {
                return new StatusObject("nocert");
            }
        }
        else return new StatusObject("noauth");
    }

    @RequestMapping(value = "/addEducationWithCertificate",method = RequestMethod.POST)
    public @ResponseBody Object addEducationWithCertificate(@RequestHeader("token") String token, @RequestParam String ed_type_id, @RequestParam String name, @RequestParam String speciality, @RequestParam String start, @RequestParam String stop,@RequestParam List<String> files, HttpServletRequest request){
        Token tok=tokenRepo.findById(token);
        try {
            if (tok != null) {
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Education education = new Education(ed_type_id, name, speciality, start, stop);
                Doctor doctor = doctorRepo.findByClientid(tok.getClientid());
                doctor.addEducation(education);
              /*  String url = "educationphoto-" + education.getId();
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
                }*/

                for(String i:files) {
                    FileObjectForm fileObjectForm = new FileObjectForm(i);
                    education.addUrl(fileObjectForm);
                }
                doctor.setEducationById(education.getId(),education);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            return new StatusObject("noauth");
        }
        catch (Exception e){
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = "/addEducation",method = RequestMethod.POST)
    public @ResponseBody Object addEducation(@RequestHeader("token") String token, @RequestParam String ed_type_id,@RequestParam String name, @RequestParam String speciality, @RequestParam String start, @RequestParam String stop, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
            Education education = new Education(ed_type_id, name, speciality, start, stop);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            doctor.addEducation(education);
            doctorRepo.save(doctor);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value="/deleteEducation",method =RequestMethod.POST)
    public @ResponseBody Object deleteEducation(@RequestHeader("token") String token, @RequestParam String edid, HttpServletRequest request){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                Education education = doctor.getEducationById(edid);
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
    @RequestMapping(value="/addEducationCertificate", method = RequestMethod.POST)
    public @ResponseBody Object addEdCert(@RequestHeader("token") String token, @RequestParam String certid, @RequestParam List<String> files,  RedirectAttributes redirectAttributes, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            try {
                Education ed = doctor.getEducationById(certid);
            /*    String url = "educationphoto-" + ed.getId();
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
                }*/
                for(String i:files) {
                    FileObjectForm fileObjectForm = new FileObjectForm(i);
                    ed.addUrl(fileObjectForm);
                }
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
    @RequestMapping(value="/deleteEducationCertificate", method = RequestMethod.POST)
    public @ResponseBody Object deleteEdCert(@RequestHeader("token") String token, @RequestParam String certid, @RequestParam String id, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            try {
                Education ed = doctor.getEducationById(certid);
                boolean deleted=ed.deleteUrl(id);
                doctorRepo.save(doctor);
                if(deleted)
                return new StatusObject("ok");
                else return new StatusObject("notdeleted");
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                List<EducationForm> educationForms=new ArrayList<>();
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                for(Education i:doctor.getEducations()){
                    educationForms.add(new EducationForm(i,educationTypeRepo.findById(i.getEd_type_id()+"")));
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
    @RequestMapping(value={"/getProfessionalAch"},method = RequestMethod.POST)
    public @ResponseBody Object getProfAch(@RequestHeader("token") String token){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            List<Object> aa=new ArrayList<>();
            for(TextObject i:doctor.getProfachievments()){
                aa.add(new TextObject(i.getText()));
            }
                return aa;
            }
            else return new StatusObject("noauth");
        }
        catch (NullPointerException e){
            return new StatusObject("nullpointerexception");
        }
    }
    @RequestMapping(value={"/addProfessionalAch"},method = RequestMethod.POST)
    public @ResponseBody Object addProfAch(@RequestHeader("token") String token, @RequestParam String info, HttpServletRequest request){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                boolean a=doctor.deleteProfAchs(index);
                doctorRepo.save(doctor);
                if(a)
                return new StatusObject("ok");
                else return new StatusObject("notdeleted");
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                boolean a = doctor.updateProfAch(index, info);
                doctorRepo.save(doctor);
                if(a)
                return new StatusObject("ok");
                else return new StatusObject("notdeleted");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value={"/getExtraInfo"},method = RequestMethod.POST)
    public @ResponseBody Object getExtraInfo(@RequestHeader("token") String token){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                List<Object> aa=new ArrayList<>();
                for(TextObject i:doctor.getExtrainfo()){
                    aa.add(i);
                }
                return aa;
            }
            else return new StatusObject("noauth");
        }
        catch (NullPointerException e){
            return new StatusObject("nullpointerexception");
        }
    }
    @RequestMapping(value={"/addExtraInfo"},method = RequestMethod.POST)
    public @ResponseBody Object addExtraInfo(@RequestHeader("token") String token, @RequestParam String info, HttpServletRequest request){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                boolean a=doctor.deleteExtraInfo(index);
                doctorRepo.save(doctor);
                if(a)
                return new StatusObject("ok");
                else return new StatusObject("notdeleted");
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                boolean a = doctor.updateExtraInfo(index,info);
                doctorRepo.save(doctor);
                if(a)
                return new StatusObject("ok");
                else return new StatusObject("notdeleted");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }



    @RequestMapping(value = {"/addPhoto"}, method = RequestMethod.POST)
    public @ResponseBody  Object addPhoto(@RequestHeader("token") String token, @RequestParam String file, RedirectAttributes redirectAttributes, HttpServletRequest request){
        try {
            Token tok = tokenRepo.findById(token);
            if (tok != null) {

                if (file.isEmpty()) {
                    redirectAttributes.addFlashAttribute("message", "file is empty");
                    return new StatusObject("emptyfile");
                } else {
                    //log.info(file.getSize());
                    Doctor patient = doctorRepo.findByClientid(tok.getClientid());
                    Client client = clientRepo.findById(patient.getClientid());
                 /*   int i = 0;
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
                    }*/
                 FileObjectForm fileObjectForm=new FileObjectForm(file);
                    client.addPhoto(fileObjectForm);
                    client.onReqested();
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
    public @ResponseBody Object delPhoto(@RequestHeader("token") String token,  RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Client client = clientRepo.findById(tok.getClientid());
            boolean deleted=true;
            client.deletePhoto();
            client.onReqested();
            clientRepo.save(client);
            if(deleted)
            return new StatusObject("ok");
            else
                return new StatusObject("notdeleted");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value="/getExperience",method=RequestMethod.POST)
    public @ResponseBody Object getExperience(@RequestHeader("token") String token){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                return doctor.getExperiences();
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/addExperience",method = RequestMethod.POST)
    public @ResponseBody Object addExperience(@RequestHeader("token") String token,@RequestParam String name, @RequestParam int years, @RequestParam String position, @RequestParam String startyear, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
    @RequestMapping(value="/deleteExperience",method = RequestMethod.POST)
    public @ResponseBody Object deleteExperience(@RequestHeader("token") String token, @RequestParam String experienceid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
    @RequestMapping(value="/updateExperience",method = RequestMethod.POST)
    public @ResponseBody Object updateExperience(@RequestHeader("token") String token, @RequestParam String experienceid, @RequestParam String name, @RequestParam int years, @RequestParam String position, @RequestParam String startyear, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
    @RequestMapping(value = "/setSchedule", method = RequestMethod.POST)
    public @ResponseBody Object addSchedule(@RequestHeader("token") String token, @RequestParam String name, @RequestParam String sch, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
                    chat1.setStatus("doctor");
                    chatRepo.save(chat1);
                    chat = chatRepo.findOneByDoctoridAndPatientid(id, patientid);
                }
                return new IDObject(chat.getId());
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
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
            if(tok!=null){    Client client11=clientRepo.findById(tok.getClientid());
                client11.onReqested();
                clientRepo.save(client11);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                String id=doctor.getId();
                List<Chat> chats = chatRepo.findByDoctorid(id);
                List<ChatListForm> forms = new ArrayList<>();
                for (Chat i : chats) {
                    Client oppClient;
                    try {
                        oppClient = clientRepo.findById(patientRepo.findById(i.getPatientid()).getClientid());
                    }
                    catch (NullPointerException e){
                        continue;
                    }
                    Client client=clientRepo.findById(i.getLastMessage().getClientid()+"");
                    Patient patient=null;
                    MessageForm form=null;
                    if(client!=null) {
                        patient=patientRepo.findByClientid(client.getId());
                        if (patient != null) {
                            form = new MessageForm(i.getLastMessage(), client, patient);
                            form.setMymessage(false);
                        } else {
                            form = new MessageForm(i.getLastMessage(), client, doctor);
                            form.setMymessage(true);
                        }
                    }
                    else{
                        patient=patientRepo.findById(i.getPatientid());
                    }
                    forms.add(new ChatListForm(i,form,patient,oppClient));
                }
                return forms;
            }
            else return new StatusObject("noauth");
        }
        catch(Exception e){
            e.printStackTrace();
            return new StatusObject("exception"+" "+e.getMessage());
        }
    }
    @RequestMapping(value="/sendTextMessage",method = RequestMethod.POST)
    public @ResponseBody Object sendTextMes(@RequestHeader("token") String token,@RequestParam String chatid, @RequestParam String text, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
    @RequestMapping(value="/sendFileMessage",method = RequestMethod.POST)
    public @ResponseBody Object sendFileMes(@RequestHeader("token") String token,@RequestParam String chatid, @RequestParam String type, @RequestParam String file, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());

                Chat chat = chatRepo.findById(chatid);
                Message message = new Message(doctor.getClientid(), type);
                chat.addMessage(message);
                message = chat.getMessages().get(chat.getMessages().size() - 1);
                chat.getMessages().remove(message);
                chat.setStatus("doctor");
                chat.unreadPlus();
               /* String url = "message-" + message.getId();
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
                }*/
               FileObjectForm fileObjectForm=new FileObjectForm(file);
                message.setInfo(fileObjectForm);
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
    @RequestMapping(value="/getMessages", method = RequestMethod.POST)
    public @ResponseBody Object getMessages(@RequestHeader("token") String token, @RequestParam String chatid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client11=clientRepo.findById(tok.getClientid());
                client11.onReqested();
                clientRepo.save(client11);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                Chat chat = chatRepo.findById(chatid);
                if (!chat.getStatus().equals("doctor")) {
                    chat.setUnread(0);
                }
                chatRepo.save(chat);
                List<Message> list=chat.getMessages();
                List<MessageForm> formList=new ArrayList<>();
                for(Message i:list){
                    Patient patient=patientRepo.findByClientid(i.getClientid()+"");
                    Client client=clientRepo.findById(i.getClientid()+"");
                    if(patient!=null){
                        MessageForm form=new MessageForm(i,client,patient);
                        form.setMymessage(false);
                        formList.add(form);
                    }
                    else{
                        MessageForm form= new MessageForm(i,client,doctor);
                        form.setMymessage(true);
                        formList.add(form);
                    }
                }
                return formList;
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }

    @RequestMapping(value="deleteMyPatient",method = RequestMethod.POST)
    public @ResponseBody Object deleteMyPatients(@RequestHeader("token") String token,@RequestParam String patientid, HttpServletRequest request) {
        try{

            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                boolean a=doctor.deleteMyPatient(patientid);
                doctorRepo.save(doctor);
                if(a)
                return new StatusObject("ok");
                else return new StatusObject("notdeleted");
            }
            else {
                return new StatusObject("noauth");

            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception") ;}
    }
    @RequestMapping(value = "/getMyRequestsList",method = RequestMethod.POST)
    public @ResponseBody Object getMyReqestsList(@RequestHeader("token") String token,HttpServletRequest request) {
            try{
                Token tok=tokenRepo.findById(token);
                if(tok!=null){
                        DoctorRequestForm form;
                        Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                        List<MyPatientListForm> reqs=new ArrayList<>();
                        List<MyPatientListForm> phones=new ArrayList<>();
                        for(Object i:doctor.getMyPatients()){
                            myPatientForm pForm=(myPatientForm) i;
                            Client PClient=clientRepo.findById(patientRepo.findById(pForm.getId()).getClientid());
                            MyPatientListForm listForm=new MyPatientListForm();
                            listForm.setPatientid(pForm.getId());
                            listForm.setName(PClient.getSurname());listForm.setSurname(PClient.getSurname());
                            if(PClient.getPhotourl().size()>0) {
                                listForm.setPhoto(PClient.getPhotourl().get(PClient.getPhotourl().size()-1));
                            }
                            if((new Date().getTime())-PClient.getLastOnline()<=600000) {
                                listForm.setOnline(true);
                            }
                            else{
                                listForm.setOnline(false);
                            }
                            if(!pForm.isAccepted()){
                                reqs.add(listForm);
                            }
                            else if(pForm.isPhone()){
                                phones.add(listForm);
                            }
                        }
                        form=new DoctorRequestForm(reqs,phones);
                        return form;
                }
                else {
                    return new StatusObject("noauth");
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return new StatusObject("exception");
            }
    }
    @RequestMapping(value="/answerRequest",method = RequestMethod.POST)
    public @ResponseBody Object answerRequest(@RequestHeader("token") String token,@RequestParam String patientid,@RequestParam boolean answer,HttpServletRequest request) {
        try {
            Token tok = tokenRepo.findById(token);
            if (tok != null) {
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                int l=0;
                for(Object i:doctor.getMyPatients()) {
                    myPatientForm pForm = (myPatientForm) i;
                    if(pForm.getId().equals(patientid)){
                        pForm.setAccepted(answer);
                        doctor.setPatientById(patientid,pForm);
                        if(!answer)
                        {
                            doctor.deleteMyPatient(patientid);
                        }
                        else {
                            doctorRepo.save(doctor);
                        }
                        return new StatusObject("ok");
                    }

                }
                return new StatusObject("noclient");
            }
            else {
                return new StatusObject("noauth");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/answerPhoneRequest",method = RequestMethod.POST)
    public @ResponseBody Object answerPhoneRequest(@RequestHeader("token") String token,@RequestParam String patientid, @RequestParam boolean answer, HttpServletRequest request) {
        try {
            Token tok = tokenRepo.findById(token);
            if (tok != null) {
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                int l=0;
                for(Object i:doctor.getMyPatients()) {
                    myPatientForm pForm = (myPatientForm) i;
                    if(pForm.getId().equals(patientid)){
                        pForm.setPhonedoctor(answer);
                        doctor.setPatientById(patientid,pForm);
                        doctorRepo.save(doctor);
                        return new StatusObject("ok");
                    }
                }
                return new StatusObject("noclient");
            }
            else {
                return new StatusObject("noauth");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/getMyPatientsList",method = RequestMethod.POST)
    public @ResponseBody Object getMyPatients(@RequestHeader("token") String token,HttpServletRequest request) {
        try{

            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                List<MyPatientListForm> forms=new ArrayList<>();
                for(Object i:doctor.getMyPatients()){
                    myPatientForm form=(myPatientForm) i;
                    if(!form.isAccepted()){
                        continue;
                    }
                    MyPatientListForm listForm=new MyPatientListForm();
                    listForm.setPatientid(form.getId());
                    Client PClient=clientRepo.findById(patientRepo.findById(form.getId()).getClientid());
                    listForm.setName(PClient.getSurname());listForm.setSurname(PClient.getSurname());
                    if(PClient.getPhotourl().size()>0) {
                        listForm.setPhoto(PClient.getPhotourl().get(PClient.getPhotourl().size()-1));
                    }
                    if((new Date().getTime())-PClient.getLastOnline()<=600000) {
                        listForm.setOnline(true);
                    }
                    else{
                        listForm.setOnline(false);
                    }
                    List<Order> orders=orderRepo.findByPatientidAndDoctorid(form.getId(),doctor.getId());
                    int home=0, work=0;
                    for(Order ii:orders){
                        if(ii.isAtwork()){
                            work++;
                        }
                        else{
                            home++;
                        }
                    }
                    listForm.setCalls(home);
                    listForm.setCommings(work);
                    forms.add(listForm);
                }
                return forms;
            }
            else {
                return new StatusObject("noauth");

            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception") ;}
    }
    @RequestMapping(value = "/getListOrders",method = RequestMethod.POST)
    public @ResponseBody Object getListOrders(@RequestHeader("token") String token, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client11=clientRepo.findById(tok.getClientid());
                client11.onReqested();
                clientRepo.save(client11);
                Doctor doctor1=doctorRepo.findByClientid(tok.getClientid());
                String id=doctor1.getId();
                ArrayList<Order> orders=orderRepo.findByDoctorid(id);
                ArrayList<OrderListForm> orderListForms=new ArrayList<>();
                for(Order i:orders){
                    Patient patient=patientRepo.findById(i.getPatientid());
                    Client client=clientRepo.findById(patient.getClientid());
                    orderListForms.add(new OrderListForm(i,patient,client));
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
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
    @RequestMapping(value="/cancelOrder",method = RequestMethod.POST)
    public @ResponseBody Object cancelOrder(@RequestHeader("token") String token,@RequestParam String orderid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
    @RequestMapping(value="/acceptOrder", method = RequestMethod.POST)
    public @ResponseBody Object acceptOrder(@RequestHeader("token") String token,@RequestParam String orderid, HttpServletRequest request) {
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null) {    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
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
    @RequestMapping(value = "/setTextAnswers",method = RequestMethod.POST)
    public @ResponseBody Object setTextAnswers(@RequestHeader("token") String token, @RequestParam String orderid, @RequestParam String diagnos, @RequestParam String healing, @RequestParam String comment, HttpServletRequest request){
        try{
                Token tok= tokenRepo.findById(token);
                if(tok!=null) {    Client client=clientRepo.findById(tok.getClientid());
                    client.onReqested();
                    clientRepo.save(client);
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
    @RequestMapping(value="/setAudioHealingAnswer", method = RequestMethod.POST)
    public @ResponseBody Object setAudioHealingAnswer(@RequestHeader("token") String token, @RequestParam String orderid, @RequestParam String file, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Order order = orderRepo.findById(orderid);
              /*  String url = "ordermessage-" + order.getId();
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
                }*/
              FileObjectForm fileObjectForm=new FileObjectForm(file);
                order.setAudiohealing(fileObjectForm);
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
    @RequestMapping(value="/addOrderFileComment",method=RequestMethod.POST)
    public @ResponseBody Object addOrderFileComment(@RequestHeader("token") String token, @RequestParam String orderid, @RequestParam String type,@RequestParam String file, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Order order = orderRepo.findById(orderid);
              /*  String url = "ordermessage-" + order.getId();
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
                }*/
                FileObjectForm fileObjectForm=new FileObjectForm(file);
                switch (type) {
                    case "photo":
                        order.setPhotoAnswer(fileObjectForm);
                        break;
                    case "audio":
                        order.setAudioAnswer(fileObjectForm);
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
    @RequestMapping(value="/finishOrder/{orderid}",method = RequestMethod.POST)
    public @ResponseBody Object finishOrder(@RequestHeader("token") String token, @PathVariable("orderid") String orderid,@RequestParam String diagnosis,@RequestParam  String healing, @RequestParam String comment, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Order order=orderRepo.findById(orderid);
                Patient patient=patientRepo.findById(order.getPatientid());
                order.setStatus("doctorfinished");
                Comment comment1=new Comment(order.getDoctorid(),comment);
                order.setDiagnosAnswer(diagnosis);
                order.setHealingAnswer(healing);
                order.setDoctorComment(comment1);
                orderRepo.save(order);
                return new StatusObject("ok");
            }
            return new StatusObject("noauth");
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
    @RequestMapping(value="/getDoctorInfo")
    public @ResponseBody Object getDoctorInfo(@RequestHeader("token") String token, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Doctor doctor = doctorRepo.findByClientid(tok.getClientid());
            Client client = clientRepo.findById(tok.getClientid());

                client.onReqested();
                clientRepo.save(client);
            return new ClientWithDoctorForm(client, doctor);
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value="/getDoctorProfile")
    public @ResponseBody Object getDoctorProfile(@RequestHeader("token") String token, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            Doctor doctor = doctorRepo.findByClientid(tok.getClientid());
            Client client = clientRepo.findById(tok.getClientid());

            client.onReqested();
            clientRepo.save(client);
            ServiceType serviceType=serviceTypeRepo.findById(doctor.getServicetypeid()+"");
            List<Service> services=new ArrayList<>();
            for(IDObject i:doctor.getServices())
                services.add(serviceRepo.findById(i.getId()));
            Address workAd=doctor.getWorkAddress();
            City WCity;
            Address homeAd=doctor.getHomeAddress();
            City HCity;
            try{
                WCity=cityRepo.findById(workAd.getCityid()+"");
                WCity.getNameRus();
            }
            catch (NullPointerException e){
                WCity=null;
            }
            try{
                HCity=cityRepo.findById(homeAd.getCityid()+"");
                HCity.getNameRus();
            }
            catch (NullPointerException e){
                HCity=null;
            }
            return new DoctorProfileForm(doctor,client,serviceType,services, WCity, HCity);
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value="/getDoctorInfoById")
    public @ResponseBody Object getDoctorInfoById(@RequestHeader("token") String token,@RequestParam String doctorid, HttpServletRequest request){
        try {
            Token tok = tokenRepo.findById(token);
            if (tok != null) {
                Doctor doctor = doctorRepo.findById(doctorid);
                Client client = clientRepo.findById(doctor.getClientid());
                client.onReqested();
                clientRepo.save(client);
                return new ClientWithDoctorForm(client, doctor);
            }
            return new StatusObject("noauth");
        }
        catch (Exception e){
            return new StatusObject("exception");
        }
    }
}
