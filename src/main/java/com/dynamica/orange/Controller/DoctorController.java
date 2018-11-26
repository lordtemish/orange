package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Form.*;
import com.dynamica.orange.Repo.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.cert.Certificate;
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
    AppointmentRepo appointmentRepo;
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
    BloodRepo bloodRepo;
    @Autowired
    OwnTimeTypeRepo ownTimeTypeRepo;
    @Autowired
    EventRepo eventRepo;
    FileUploader fileUploader=new FileUploader();

    @RequestMapping(value = "/addEvent",method = RequestMethod.POST)
    public @ResponseBody Object addEvent(@RequestHeader("token") String token,@RequestParam long chosenTime,@RequestParam boolean atwork, @RequestParam double notifyPeriod, @RequestParam String info, @RequestParam String name, @RequestParam List<String> patients ){
        try {
            Token tok=tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                List<Event> events=eventRepo.findByDoctorid(doctor.getId());
                Event event=new Event(doctor.getId(),events.size(),chosenTime,atwork,notifyPeriod,info,name);
                event.setPatientidsString(patients);
                eventRepo.save(event);

                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = "/getEvents",method = RequestMethod.POST)
    public @ResponseBody Object getEvents(@RequestHeader("token") String token, @RequestParam long time){
        try {
            Token tok=tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());

                Calendar calendar=Calendar.getInstance();
                calendar.setTimeInMillis(time);
                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),calendar.getMinimum(Calendar.HOUR_OF_DAY),0);
                long minimum=calendar.getTimeInMillis();
                long maximum=minimum+(24*60*60*1000);
                List<Event> events=eventRepo.findByDoctoridAndChosenTimeBetween(doctor.getId(), minimum, maximum);
                List<Order> orders=orderRepo.findByDoctoridAndChoseTimeBetween(doctor.getId(),minimum,maximum);


                List<Object> orders2=new ArrayList<>();
                List<Object> events2=new ArrayList<>();
                List<Integer> days=new ArrayList<>();

                List<EventListForm> eventListForms=new ArrayList<>();
                for(Event i:events){
                    List<Object> listForms=new ArrayList<>();
                    for(Object j:i.getPatientids()){
                        String jj=j+"";
                        Patient patient=patientRepo.findById(jj);
                        Client clientp=clientRepo.findById(patient.getClientid());
                        if(patient!=null && clientp!=null) {
                            PatientListForm listForm = new PatientListForm(patient, client);
                            listForms.add(listForm);
                        }
                    }
                    i.setPatientids(listForms);
                    eventListForms.add(new EventListForm(i,i.getChosenTime()));
                }
                Collections.sort(eventListForms);
                events2.addAll(eventListForms);
                List<OrderListForm> orderListForms=new ArrayList<>();
                for(Order i:orders){
                    Patient patient=patientRepo.findById(i.getPatientid());
                    Client client2=clientRepo.findById(patient.getClientid());
                    OrderListForm listForm=new OrderListForm(i,patient,client2);
                    Appointment appointment=appointmentRepo.findByOrderid(i.getId());
                    if(appointment!=null)
                        listForm.setAppointment(appointment);
                    ArrayList<OwnService> ownServices = new ArrayList<>();
                    for (Object j : i.getOwnServices()) {
                        for (OwnService jj : doctor.getOwns()) {
                            log.info(jj.getId().length() + " " + (j + "").length() + "   " + (j + "").equals(jj.getId() + ""));
                            if ((j + "").equals(jj.getId())) {
                                ownServices.add(jj);
                                break;
                            }
                        }
                    }
                    listForm.setOwnServices(ownServices);
                    listForm.setPriceBy();
                    orderListForms.add(listForm);
                }
                Collections.sort(orderListForms);
                orders2.addAll(orderListForms);


                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.getMinimum(Calendar.DAY_OF_MONTH),calendar.getMinimum(Calendar.HOUR_OF_DAY),0);
                minimum=calendar.getTimeInMillis();
                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.getActualMaximum(Calendar.DAY_OF_MONTH),calendar.getActualMaximum(Calendar.HOUR_OF_DAY),calendar.getActualMaximum(Calendar.MINUTE));
                maximum=calendar.getTimeInMillis();
                log.info(minimum+"  "+ maximum+"   ");
                HashSet<Integer> daysInt=new HashSet<>();
                List<Event> events1=eventRepo.findByDoctoridAndChosenTimeBetween(doctor.getId(),minimum,maximum);
                List<Order> orders1=orderRepo.findByDoctoridAndChoseTimeBetween(doctor.getId(),minimum,maximum);
                for(Event i:events1){
                        calendar.setTimeInMillis(i.getChosenTime());
                        daysInt.add(calendar.get(Calendar.DAY_OF_MONTH));
                }
                for(Order i :orders1){
                    calendar.setTimeInMillis(i.getChoseTime());
                    daysInt.add(calendar.get(Calendar.DAY_OF_MONTH));
                }
                days.addAll(daysInt);
                EventWithOrdersForm eventWithOrdersForm=new EventWithOrdersForm(events2,orders2,days);
                return eventWithOrdersForm;

            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }

    @RequestMapping(value = "/addDoctor",method = RequestMethod.POST)
    public  @ResponseBody Object addDoctor(@RequestHeader("token") String token,@RequestParam String name,@RequestParam String surname, @RequestParam String dad, @RequestParam String position, @RequestParam String info, @RequestParam String service_type_id, HttpServletRequest request){
        try {
            Token tok1=tokenRepo.findById(token);
            if(tok1!=null) {
                Doctor doctor=doctorRepo.findByClientid(tok1.getClientid());
                if(doctor==null) {
                    doctor = new Doctor(tok1.getClientid(), position, info);
                }
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
 /*   @RequestMapping(value = {"/setAddress"}, method = RequestMethod.POST)
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
    }*/
    @RequestMapping(value = {"/addAddress"}, method = RequestMethod.POST)
    public  @ResponseBody Object addAddressWithLocation(@RequestHeader("token") String token,  @RequestParam String cityId, @RequestParam String address,@RequestParam double longitude, @RequestParam double latitude,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            Client client=clientRepo.findById(tok.getClientid());
            client.onReqested();
            clientRepo.save(client);
            Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                    Address address1=new Address(cityId,address);
                    address1.setLocation(new com.dynamica.orange.Classes.Map(latitude,longitude));

             boolean added =doctor.addAddress(address1);
             if(added) {
                 doctorRepo.save(doctor);
                 return new StatusObject("ok");
             }
             else{
                 return new StatusObject("addresslimit");
             }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/deleteAddress"}, method = RequestMethod.POST)
    public  @ResponseBody Object deleteAddress(@RequestHeader("token") String token, @RequestParam int index){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            Client client = clientRepo.findById(tok.getClientid());
            client.onReqested();
            clientRepo.save(client);
            Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            boolean deleted=doctor.deleteAddress(index);
            if(deleted) {
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else{
                return new StatusObject("indexoutofrange");
            }
        }
        else {
            return new StatusObject("noauth");
        }
        }
        @RequestMapping(value = {"/addAddressWithCompany"}, method = RequestMethod.POST)
    public  @ResponseBody Object addAddressWC(@RequestHeader("token") String token, @RequestParam String cityId, @RequestParam String address,@RequestParam String company,@RequestParam double longitude, @RequestParam double latitude, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            Client client=clientRepo.findById(tok.getClientid());
            client.onReqested();
            clientRepo.save(client);
            Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            Address address1=new Address(cityId,address);
            address1.setLocation(new com.dynamica.orange.Classes.Map(latitude,longitude));
            address1.setName(company);
            boolean added =doctor.addAddress(address1);
            if(added) {
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else{
                return new StatusObject("addresslimit");
            }
        }
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
                doctor.addOwnService(new OwnService(name,price, 0));
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
    public @ResponseBody Object addOwnService(@RequestHeader("token") String token,@RequestParam int callPrice, @RequestParam String name, @RequestParam String ottid, @RequestParam String info, @RequestParam int price, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                doctor.addOwnService(new OwnService(name, ottid, info, price,callPrice));
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
                ArrayList<String> strings=new ArrayList<>();
                strings.add(mail);
                client.setPhones(strings);
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
                ArrayList<String> strings=new ArrayList<>();
                strings.add(phone);
                client.setPhones(strings);
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
    public @ResponseBody Object addCertificate(@RequestHeader("token") String token, @RequestParam List<String> files, RedirectAttributes redirectAttributes, HttpServletRequest request) throws IOException {
        Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            try {

                for(String i:files) {
                    int l=0;
                    String url="";
                    byte[] byteFile=fileUploader.decodeFileFromBase64(i);
                    while(true) {
                        //String res=fileUploader.uploadText(i, "certificate"+doctor.getId()+l);
                        String res=fileUploader.savePhoto(byteFile,"certificate"+doctor.getId()+l+".jpeg");
                        if(!res.equals("")){
                            i=res;
                            url=res;
                            break;
                        }
                        else if(res.equals("exception")){
                            break;
                        }
                        else{
                            l++;
                        }
                    }
                    i=fileUploader.getLhost()+"?url="+url;
                    FileObjectForm fileObjectForm = new FileObjectForm(i);
                    fileObjectForm.setUrl(url);
                    fileObjectForm.setType("photo");
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
    @RequestMapping(value="/deleteComments/",method = RequestMethod.POST)
    public @ResponseBody Object deleteCOmments(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            Client client=clientRepo.findById(tok.getClientid());
            client.onReqested();
            clientRepo.save(client);
            Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
            boolean a =true;
            doctor.setComments(new ArrayList<>());
            doctorRepo.save(doctor);
            if (a) {
                return new StatusObject("ok");
            } else {
                return new StatusObject("nocert");
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


                for(String i:files) {
                    int l=0;
                    while(true) {
                        byte[] bytePhoto=fileUploader.decodeFileFromBase64(i);
                        String res=fileUploader.savePhoto(bytePhoto, "certificate"+doctor.getId()+l+".jpeg");
                        if(!res.equals("")){
                            i=res;
                            break;
                        }
                        else if(res.equals("exception")){
                            break;
                        }
                        else{
                            l++;
                        }
                    }
                    String url=i;
                    i=fileUploader.getLhost()+"?url="+i;
                    FileObjectForm fileObjectForm = new FileObjectForm(i);
                    fileObjectForm.setType("photo");
                    fileObjectForm.setUrl(url);
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
                for(Object f:education.getUrls()){
                    FileObjectForm objectForm=(FileObjectForm) f;
                    fileUploader.deleteFile(objectForm.getUrl());
                }
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

                for(String i:files) {
                    int l=0;
                    while(true) {
                        byte[] bytePhoto=fileUploader.decodeFileFromBase64(i);
                        String res=fileUploader.savePhoto(bytePhoto, "certificate"+doctor.getId()+l+".jpeg");
                        if(!res.equals("")){
                            i=res;
                            break;
                        }
                        else if(res.equals("exception")){
                            break;
                        }
                        else{
                            l++;
                        }
                    }
                    String url=i;
                    i=fileUploader.getLhost()+"?url="+i;
                    FileObjectForm fileObjectForm = new FileObjectForm(i);
                    fileObjectForm.setUrl(url);
                    fileObjectForm.setType("photo");
                    ed.addUrl(fileObjectForm);
                }
                doctor.setEducationById(certid, ed);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            } catch (NullPointerException e) {
                e.printStackTrace();
                return new StatusObject("nullpointerexception");
            }catch (Exception e){
                return "exception";
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
                for(Education i:doctor.getEducations()){/*
                    ArrayList<Object> urls=i.getUrls();
                    ArrayList<Object> files=new ArrayList<>();
                    for(Object j:urls){
                        FileObjectForm jj=(FileObjectForm)j;
                        FileObjectForm jfile=fileUploader.changeToFile(jj);
                        files.add(jfile);
                    }
                    i.setUrls(files);*/
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

                    int l=0;
                    byte[] byteFile=fileUploader.decodeFileFromBase64(file);
                    while(true) {
                        String res=fileUploader.savePhoto(byteFile,"photo"+patient.getId()+l+".jpeg");
                        if(!res.equals("")){
                            file=res;
                            break;
                        }
                        else if(res.equals("exception")){
                            break;
                        }
                        else{
                            l++;
                        }
                    }
                    String file1=fileUploader.getLhost()+"?url="+file;
                    FileObjectForm fileObjectForm=new FileObjectForm(file1);
                    fileObjectForm.setType("photo");
                    fileObjectForm.setUrl(file);
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
            String url=((FileObjectForm)client.getPhotourl().get(0)).getUrl();
            fileUploader.deleteFile(url);
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
    public List<ScheduleForm> getScheduleForms(JsonArray array){
        List<ScheduleForm> scheduleForms=new ArrayList<>();
        for(int i=0;i<array.size();i++){
            JsonObject element=array.get(i).getAsJsonObject();
            int from=element.get("from").getAsInt();
            int to=element.get("to").getAsInt();
            for(int j=from; j<to;j++){
                scheduleForms.add(new ScheduleForm(j,j+1));
            }
        }
        return scheduleForms;
    }
    @RequestMapping(value = "/removeSchedule", method = RequestMethod.POST)
    public @ResponseBody Object remSch(@RequestHeader("token") String token) {
        Token tok = tokenRepo.findById(token);
        if (tok != null) {
            Doctor doctor = doctorRepo.findByClientid(tok.getClientid());
            doctor.setHomeSchedule(new Schedule());
            doctor.setWorkSchedule(new Schedule());
            return new StatusObject("ok");
        } else {
            return new StatusObject("noauth");
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
                Set<Map.Entry<String, JsonElement>> set  = object.entrySet();
                for (Map.Entry<String, JsonElement> i : set) {
                    switch (i.getKey()) {
                        case "sunday":
                            JsonArray array=i.getValue().getAsJsonArray();
                            List<ScheduleForm> scheduleForms=getScheduleForms(array);
                            schedule.setSunday(scheduleForms);
                            break;
                        case "monday":
                            List<Object> list=new ArrayList<>();
                            schedule.setMonday(getScheduleForms(i.getValue().getAsJsonArray()));
                            break;
                        case "tuesday":
                            schedule.setTuesday(getScheduleForms(i.getValue().getAsJsonArray()));
                            break;
                        case "wednesday":
                            schedule.setWednesday(getScheduleForms(i.getValue().getAsJsonArray()));
                            break;
                        case "thursday":
                            schedule.setThursday(getScheduleForms(i.getValue().getAsJsonArray()));
                            break;
                        case "friday":
                            schedule.setFriday(getScheduleForms(i.getValue().getAsJsonArray()));
                            break;
                        case "saturday":
                            schedule.setSaturday(getScheduleForms(i.getValue().getAsJsonArray()));
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
    @RequestMapping(value="/answerChatRequest", method = RequestMethod.POST)
    public @ResponseBody Object answerChatRequest(@RequestHeader("token") String token, @RequestParam String chatid,@RequestParam boolean accept, HttpServletRequest request){
        try {
            Token tok = tokenRepo.findById(token);
            if (tok != null) {
                Chat chat=chatRepo.findById(chatid);
                if(chat==null){
                    return new StatusObject("nochatrequests");
                }
                ChatRequest chatRequest=null;
                if(accept){
                    chatRequest=ChatRequest.DOCTOR_ACCEPTED;
                }
                else
                    chatRequest=ChatRequest.DOCTOR_DECLINE;
                chat.setChatRequest(chatRequest);
                chatRepo.save(chat);
                return new StatusObject("ok");
            }
            return new StatusObject("noauth");
        }
        catch (Exception e){
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
                    chat1.setChatRequest(ChatRequest.DOCTOR_ACCEPTED);
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
            if(tok!=null){
                //ChatListWholeForm wholeForm=new ChatListWholeForm();
                Client client11=clientRepo.findById(tok.getClientid());
                client11.onReqested();
                clientRepo.save(client11);
                /*if(client11.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm)client11.getPhotourl().get(0));
                    wholeForm.setMyphoto(fileObjectForm);
                }*/
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                String id=doctor.getId();
                List<Chat> chats = chatRepo.findByDoctorid(id);
                List<ChatListForm> forms = new ArrayList<>();
                for (Chat i : chats) {
                    Client oppClient;
                    try {
                        oppClient = clientRepo.findById(patientRepo.findById(i.getPatientid()).getClientid());/*
                        ArrayList<Object> photos=new ArrayList<>();
                        if(oppClient.getPhotourl().size()>0){
                            FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) oppClient.getPhotourl().get(oppClient.getPhotourl().size()-1));
                            fileObjectForm.setId(((FileObjectForm) oppClient.getPhotourl().get(oppClient.getPhotourl().size()-1)).getId());
                            photos.add(fileObjectForm);
                        }
                        oppClient.setPhotourl(photos);*/
                    }
                    catch (NullPointerException e){
                        continue;
                    }
                    Patient patient = patientRepo.findById(i.getPatientid());
                    MessageForm form = null;
                    if(i.getMessages().size()>0) {
                        Message message = i.getLastMessage();
                        if (message.getType().equals("audio"))
                        {
                            FileObjectForm fileObjectForm = fileUploader.changeToFile((FileObjectForm) message.getInfo());
                            message.setInfo(fileObjectForm);
                        }
                        Client client = clientRepo.findById(message.getClientid() + "");

                        if (client != null) {
                            patient = patientRepo.findByClientid(client.getId());
                            if (patient != null) {

                                form = new MessageForm(message, client, patient);
                                form.setMymessage(false);
                            } else {
                                form = new MessageForm(message, client, doctor);
                                form.setMymessage(true);
                            }
                        } else {
                            patient = patientRepo.findById(i.getPatientid());
                        }
                    }
                        forms.add(new ChatListForm(i, form, patient, oppClient));

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
                if(chat.getStatus().equals("patient")){
                    chat.setUnread(0);
                }
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
                if(chat.getStatus().equals("patient")){
                    chat.setUnread(0);
                }
                chat.setStatus("doctor");
                chat.unreadPlus();

                int l=0;
                byte[] byteFile=null;
                if(type.equals("photo")) byteFile=fileUploader.decodeFileFromBase64(file);
                String url="";
                while (true) {
                    String res="";
                    if(type.equals("audio")) {
                        res=fileUploader.uploadText(file, "chat" + message.getId() + l);
                        url=res;
                    }
                    else {

                        res = fileUploader.savePhoto(byteFile, "chat" + chat.getId() + l + ".jpeg");
                        url=res+"";
                        res=fileUploader.getLhost()+"?url="+url;
                    }
                    if(!res.equals("")){
                        file=res;
                        break;
                    }
                    else if(res.equals("exception")){
                        break;
                    }
                    else{
                        l++;
                    }
                }
               FileObjectForm fileObjectForm=new FileObjectForm(file);
                fileObjectForm.setType(type);
                fileObjectForm.setUrl(url);
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
    @RequestMapping(value="/deleteMessage", method = RequestMethod.POST)
    public @ResponseBody Object deleteMessage(@RequestHeader("token") String token, @RequestParam String chatid,@RequestParam String messageid, HttpServletRequest request) {
        try {
            Token tok = tokenRepo.findById(token);
            if(tok!=null) {
                Client cliqqent = clientRepo.findById(tok.getClientid());
                cliqqent.onReqested();
                clientRepo.save(cliqqent);
                Doctor patient = doctorRepo.findByClientid(tok.getClientid());
                Chat chat = chatRepo.findById(chatid);
                List<Message> messages=new ArrayList<>();
                messages.addAll(chat.getMessages());
                boolean deleted=false;
                for(Message i:messages){
                    if(i.getId().equals(messageid)){
                        if(!i.getType().equals("text")){
                            fileUploader.deleteFile(((FileObjectForm)i.getInfo()).getUrl());
                        }
                        messages.remove(i);
                        deleted=true;
                        break;
                    }
                }
                if(deleted){
                    chat.setMessages(messages);
                    chatRepo.save(chat);
                    return new StatusObject("ok");
                }
                else
                    return new StatusObject("noobject");
            }
            else {
                return new StatusObject("noauth");
            }
        } catch (Exception e) {
            return new StatusObject("exeption");
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

                    if(i.getType().equals("audio")){
                        FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm)i.getInfo());
                        i.setInfo(fileObjectForm);
                    }

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
                            listForm.setName(PClient.getName());listForm.setSurname(PClient.getSurname());/*
                            if(PClient.getPhotourl().size()>0) {
                                FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) PClient.getPhotourl().get(PClient.getPhotourl().size()-1));
                                listForm.setPhoto(fileObjectForm);
                            }*/
                            if((new Date().getTime())-PClient.getLastOnline()<=600000) {
                                listForm.setOnline(true);
                            }
                            else{
                                listForm.setOnline(false);
                            }
                            if(!pForm.isAccepted()){
                                reqs.add(listForm);
                            }
                            else if(pForm.isPhone() && !pForm.phonefinished){
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
                        pForm.phonefinished=true;
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
                    listForm.setName(PClient.getSurname());listForm.setSurname(PClient.getSurname());/*
                    if(PClient.getPhotourl().size()>0) {
                        FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) PClient.getPhotourl().get(PClient.getPhotourl().size()-1));
                        listForm.setPhoto(fileObjectForm);
                    }*/
                    if(client.getPhotourl().size()>0)
                        listForm.setPhoto((FileObjectForm)client.getPhotourl().get(0));
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
    @RequestMapping(value="/getOrderInfo", method = RequestMethod.POST)
    public @ResponseBody Object getOrderInfo(@RequestHeader("token") String token, @RequestParam String orderid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Order order=orderRepo.findById(orderid);
                Doctor doctor=doctorRepo.findById(order.getDoctorid());
                Client clientd=clientRepo.findById(doctor.getClientid());
                Patient patient=patientRepo.findById(order.getPatientid());
                Client clientp  =clientRepo.findById(patient.getClientid());
                ArrayList<Object> services1 = new ArrayList<>();
                for (Object j : order.getServices()) {
                    services1.add(serviceRepo.findById(j+""));
                }/*
                ArrayList<Object> fileObjectForms=new ArrayList<>(), secondObjectForms=new ArrayList<>();
                if(clientd.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) clientd.getPhotourl().get(clientd.getPhotourl().size()-1));
                    fileObjectForms.add(fileObjectForm);
                    clientd.setPhotourl(fileObjectForms);
                }
                if(clientp.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) clientp.getPhotourl().get(clientp.getPhotourl().size()-1));
                    secondObjectForms.add(fileObjectForm);
                    clientd.setPhotourl(fileObjectForms);
                }*//*
                for(Education education:doctor.getEducations()){
                    ArrayList<Object> files=new ArrayList<>();
                    for(Object j:education.getUrls()){
                        FileObjectForm file1=(FileObjectForm) j;
                        FileObjectForm file=fileUploader.changeToFile(file1);
                        file.setId(file1.getId());
                        files.add(file);
                    }
                    education.setUrls(files);
                    doctor.setEducationById(education.getId(),education);
                }*/
                if(order.getAudiohealing()!=null){
                    FileObjectForm fileObjectForm=(FileObjectForm)order.getAudiohealing();
                    FileObjectForm ss=fileUploader.changeToFile(fileObjectForm);
                    order.setAudiohealing(ss);
                }
                if(order.getAudioMessage()!=null){
                    FileObjectForm fileObjectForm=(FileObjectForm)order.getAudioMessage();
                    FileObjectForm ss=fileUploader.changeToFile(fileObjectForm);
                    order.setAudioMessage(ss);
                }

                order.setServices(services1);
                List<Object> services=new ArrayList<>();
                for(Object i: order.getOwnServices()){
                    for (OwnService jj : doctor.getOwns()) {
                        log.info(jj.getId().length() + " " + (i + "").length() + "   " + (i + "").equals(jj.getId() + ""));
                        if ((i + "").equals(jj.getId())) {
                            services.add(jj);
                            break;
                        }

                    }
                }
                List<CommentForm> forms=new ArrayList<>();
                for(Comment j:doctor.getComments()){
                    if(j.getId().equals(order.getId())){
                        forms.add(new CommentForm(patient,clientp,j));
                    }
                }

                order.setOwnServices(services);
                OrderInfoForm infoForm= new OrderInfoForm(order,doctor,clientd,patient,clientp);
                if(forms.size()>0)
                infoForm.setCommentForms(forms.get(0));
                return infoForm;
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
            if(tok!=null){    Client client11=clientRepo.findById(tok.getClientid());
                client11.onReqested();
                clientRepo.save(client11);
                Doctor doctor1=doctorRepo.findByClientid(tok.getClientid());
                String id=doctor1.getId();
                ArrayList<Order> orders=orderRepo.findByDoctorid(id);
                ArrayList<OrderListForm> orderListForms=new ArrayList<>();
                for(Order i:orders){
                    Patient patient=patientRepo.findById(i.getPatientid());
                    Client client=clientRepo.findById(patient.getClientid());/*
                    ArrayList<Object> photos=new ArrayList<>();
                    if(client.getPhotourl().size()>0){
                        FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                        photos.add(fileObjectForm);
                    }
                    client.setPhotourl(photos);*/
                    ArrayList<Service> services = new ArrayList<>();
                    for (Object j : i.getServices()) {
                        services.add(serviceRepo.findById(j+""));
                    }
                    ArrayList<OwnService> ownServices = new ArrayList<>();
                    for (Object j : i.getOwnServices()) {
                        for (OwnService jj : doctor1.getOwns()) {
                            log.info(jj.getId().length() + " " + (j + "").length() + "   " + (j + "").equals(jj.getId() + ""));
                            if ((j + "").equals(jj.getId())) {
                                ownServices.add(jj);
                                break;
                            }

                        }
                    }
                    List<CommentForm> forms=new ArrayList<>();
                    for(Comment j:doctor1.getComments()){
                        if(j.getId().equals(i.getId())){
                            forms.add(new CommentForm(patient,client,j));
                        }
                    }

                    OrderListForm orderListForm=new OrderListForm(i,patient,client);
                    if(forms.size()>0)
                    orderListForm.setCommentForms(forms.get(0));
                    orderListForm.setOwnServices(ownServices);
                    orderListForm.setServices(services);
                    orderListForm.setPriceBy();
                    orderListForms.add(orderListForm);
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
    public @ResponseBody Object cancelOrder(@RequestHeader("token") String token,@RequestParam String orderid,@RequestParam String text, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Order order = orderRepo.findById(orderid);
                order.setStatus("doctorcancelled");
                order.setTextAnswer(text);
                Appointment appointment=appointmentRepo.findByOrderid(orderid);
                if(appointment!=null){
                    appointmentRepo.delete(appointment);
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

                Appointment appointment=appointmentRepo.findByOrderid(orderid);
                appointment.setAccepted(true);
                appointmentRepo.save(appointment);
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

                int l=0;
                while(true) {
                    String res=fileUploader.uploadText(file, "order"+order.getId()+l);
                    if(!res.equals("")){
                        file=res;
                        break;
                    }
                    else if(res.equals("exception")){
                        break;
                    }
                    else{
                        l++;
                    }
                }
              FileObjectForm fileObjectForm=new FileObjectForm(file);
                fileObjectForm.setType("audio");
                fileObjectForm.setUrl(file);
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
                int l=0;byte[] byteFile=null;
                if(type.equals("photo")) byteFile=fileUploader.decodeFileFromBase64(file);
                String url="";
                while (true) {
                    String res="";
                    if(type.equals("audio")) {
                        res=fileUploader.uploadText(file, "order" + order.getId() + l);
                        url=res;
                    }
                    else {

                        res = fileUploader.savePhoto(byteFile, "order" + order.getId() + l + ".jpeg");
                        url=res+"";
                        res=fileUploader.getLhost()+"?url="+res;
                    }
                    if(!res.equals("")){
                        file=res;
                        break;
                    }
                    else if(res.equals("exception")){
                        break;
                    }
                    else{
                        l++;
                    }
                }
                FileObjectForm fileObjectForm=new FileObjectForm(file);
                fileObjectForm.setUrl(url);
                fileObjectForm.setType(type);
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
    @RequestMapping(value="/finishOrder",method = RequestMethod.POST)
    public @ResponseBody Object finishOrder(@RequestHeader("token") String token, @RequestParam String orderid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){    Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Order order=orderRepo.findById(orderid);
                Patient patient=patientRepo.findById(order.getPatientid());
                order.setStatus("doctorfinished");
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
            Client client = clientRepo.findById(tok.getClientid());/*
            ArrayList<Object> photos=new ArrayList<>();
                if(client.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                    photos.add(fileObjectForm);
                }
                client.setPhotourl(photos);*/
                client.onReqested();
                clientRepo.save(client);
            return new ClientWithDoctorForm(client, doctor);
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value="/getPatientProfile", method = RequestMethod.POST) // need put objects
    public @ResponseBody
    Object getPatientProfile(@RequestHeader("token") String token,@RequestParam String patientid, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient=patientRepo.findById(patientid);
                Client client=clientRepo.findById(patient.getClientid());
                Doctor doctor=doctorRepo.findByClientid(tok.getClientid());
                Client client1=clientRepo.findById(tok.getClientid());
                client1.onReqested();
                clientRepo.save(client1);
                ArrayList<Object> photos=new ArrayList<>();/*
                if(client.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                    photos.add(fileObjectForm);
                }
                client.setPhotourl(photos);*/

                City simple=new City("","");
                City workCity=simple; City homeCity=simple;

                try {
                    if(patient.getWorkAddress()!=null) {
                        workCity = cityRepo.findById(patient.getWorkAddress().getCityid()+"");
                    }
                    if(patient.getHomeAddress()!=null) {
                        homeCity = cityRepo.findById(patient.getHomeAddress().getCityid()+"");
                    }
                    if(workCity==null){
                        workCity=simple;
                    }
                    if(homeCity==null){
                        homeCity=simple;
                    }
                }
                catch (NullPointerException eee){
                }
                String homeC;
                String workC;
                String l;
                if(client!=null) {
                    if (client.getLang() != null)
                        l = client.getLang();
                    else
                        l = "R";
                }
                else{
                    l="R";
                }
                log.info(l);
                if(l.equals("R")) {
                    homeC = homeCity.getNameRus();
                    workC = workCity.getNameRus();
                }
                else{
                    homeC = homeCity.getNameKaz();
                    workC = workCity.getNameKaz();
                }

                log.info(homeC+" "+workC+" "+workCity);
                String bloodid=patient.getBlood();
                String blood="";
                if(bloodid!=null) {
                    blood = bloodRepo.findById(patient.getBlood()).getName();
                }
                PatientProfileForm form=new PatientProfileForm(patient,client,workC,homeC, blood);
                form.setMyPatient(patient.docContains(doctor.getId()));
                return form;
            }
            else{
                return new StatusObject("noauth");
            }
        }
        catch (Exception e){
            log.info(e.getMessage());
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }

    @RequestMapping(value="/getDoctorProfile")
    public @ResponseBody Object getDoctorProfile(@RequestHeader("token") String token, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            Doctor doctor = doctorRepo.findByClientid(tok.getClientid());
            Client client = clientRepo.findById(tok.getClientid());

            client.onReqested();
            clientRepo.save(client);/*
            ArrayList<Object> photos=new ArrayList<>();
            if(client.getPhotourl().size()>0){
                FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                photos.add(fileObjectForm);
            }
            client.setPhotourl(photos);*//*
            for(Education education:doctor.getEducations()){
                ArrayList<Object> files=new ArrayList<>();
                for(Object j:education.getUrls()){
                    FileObjectForm file1=(FileObjectForm) j;
                    FileObjectForm file=fileUploader.changeToFile(file1);
                    file.setId(file1.getId());
                    file.setId(file1.getId());
                    files.add(file);
                }
                education.setUrls(files);
                doctor.setEducationById(education.getId(),education);
            }*//*
            ArrayList<Object> files=new ArrayList<>();
            for (Object c:doctor.getCertificates()){
                FileObjectForm f=(FileObjectForm) c;
                FileObjectForm file=fileUploader.changeToFile(f);
                file.setId(f.getId());
                files.add(file);
            }
            doctor.setCertificates(files);*/
            ServiceType serviceType=serviceTypeRepo.findById(doctor.getServicetypeid()+"");
            List<Service> services=new ArrayList<>();
            for(IDObject i:doctor.getServices())
                services.add(serviceRepo.findById(i.getId()));
            List<City> cities=new ArrayList<>();
            for(Address ii:doctor.getAddresses()) {
                Address workAd = ii;
                City WCity;
                try {
                    WCity = cityRepo.findById(workAd.getCityid() + "");
                    WCity.getNameRus();
                } catch (NullPointerException e) {
                    WCity = null;
                }
                cities.add(WCity);
            }

            ArrayList<Comment> comments=doctor.getComments();
            for(Comment i:comments){
                Patient patient=patientRepo.findById(i.getPatient_id());
                Client clientp=clientRepo.findById(patient.getClientid());
                i.setPatient(new PatientListForm(patient,clientp));
            }
            doctor.setComments(comments);
            DoctorProfileForm profileForm=new DoctorProfileForm(doctor,client,serviceType,services);
            profileForm.setCities(cities);
            return profileForm;
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
                clientRepo.save(client);/*
                ArrayList<Object> photos=new ArrayList<>();
                if(client.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                    photos.add(fileObjectForm);
                }
                client.setPhotourl(photos);*/
                return new ClientWithDoctorForm(client, doctor);
            }
            return new StatusObject("noauth");
        }
        catch (Exception e){
            return new StatusObject("exception");
        }
    }
}
