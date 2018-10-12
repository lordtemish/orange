package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Classes.Map;
import com.dynamica.orange.Form.*;
import com.dynamica.orange.Repo.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.concurrent.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by lordtemich on 10/27/17.
 */
@Controller
@RequestMapping(value = {"/patient"})
public class PatientController {
    private Logger logger = LoggerFactory.getLogger(PatientController.class);
    @Autowired
    TokenRepo tokenRepo;
    @Autowired
    EducationTypeRepo educationTypeRepo;
    @Autowired
    ServiceTypeRepo serviceTypeRepo;
    @Autowired
    ServiceRepo serviceRepo;
    @Autowired
    ServletContext context;
    @Autowired
    CityRepo cityRepo;
    @Autowired
    BloodRepo bloodRepo;
    @Autowired
    ClientRepo clientRepo;
    @Autowired
    PatientRepo patientRepo;
    @Autowired
    DoctorRepo doctorRepo;
    @Autowired
    ChatRepo chatRepo;
    @Autowired
    AppointmentRepo appointmentRepo;
    @Autowired
    OrderRepo orderRepo;

    FileUploader fileUploader=new FileUploader();
    public static String path="/photo/";

    @RequestMapping(value = {"/addNew"}, method = RequestMethod.POST)
    public @ResponseBody Object addNew(@RequestHeader("token") String token,@RequestParam String name, @RequestParam String surname,   HttpServletRequest request){
           try {
               Token tok= tokenRepo.findById(token);
               if(tok!=null) {
                   Client client = clientRepo.findById(tok.getClientid());
                   client.onReqested();
                   Patient patient = new Patient(tok.getClientid());

                   client.setName(name);
                   client.setSurname(surname);
                   clientRepo.save(client);
                   patientRepo.save(patient);
                   return new StatusObject("ok");
               }
               else return new StatusObject("noauth");
           }
           catch (Exception e){
               e.printStackTrace();
               return new StatusObject("exception");
           }
    }
    @RequestMapping(value = {"/setLang"}, method = RequestMethod.POST)
    public @ResponseBody Object setLang(@RequestHeader("token") String token, @RequestParam String lang, HttpServletRequest request) {
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Client client = clientRepo.findById(tok.getClientid());
                client.onReqested();
            client.setLang(lang.toUpperCase());
            clientRepo.save(client);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }

    @RequestMapping(value={"/getPatientId"},method = RequestMethod.POST)
    public @ResponseBody Object getPatientId(@RequestHeader("token") String token){
        Token tok=tokenRepo.findById(token);
        if(tok!=null){
            try{
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                return new PatientIdForm(patient.getId());
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
    @RequestMapping(value = {"/changeFIO"},method = RequestMethod.POST)
    public @ResponseBody Object changeFIO(@RequestHeader("token") String token, @RequestParam String name, @RequestParam String surname, @RequestParam String dad, HttpServletRequest request) {
        try {
            Token tok = tokenRepo.findById(token);
            if (tok != null) {
                Client client = clientRepo.findById(tok.getClientid());
                client.onReqested();
                client.setName(name);
                client.setSurname(surname);
                client.setDadname(dad);
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
            @RequestMapping(value = {"/addinfo"},method = RequestMethod.POST)
        public @ResponseBody Object addInfo(@RequestHeader("token") String token, @RequestParam String gender, @RequestParam String dateofbirth, @RequestParam int weight, @RequestParam int height, @RequestParam String bloodid, @RequestParam String chronic, @RequestParam String alergic, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
        Patient patient=patientRepo.findByClientid(tok.getClientid());
        patient.setGender(gender.toUpperCase());
        patient.setDate(dateofbirth);
        patient.setWeight(weight);
        patient.setHeight(height);
        patient.setBlood(bloodid);
        patient.setChronic(chronic);
        patient.setAlergic(alergic);
        patientRepo.save(patient);
            return new StatusObject("ok");}

        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/addFullInfoClient"},method = RequestMethod.POST)
    public @ResponseBody Object addFullInfo(@RequestHeader("token") String token,@RequestParam String name, @RequestParam String surname,  @RequestParam String gender, @RequestParam String dateofbirth, @RequestParam int weight, @RequestParam int height, @RequestParam String bloodid, @RequestParam String chronic, @RequestParam String alergic, HttpServletRequest request) {
       try {
           Token tok= tokenRepo.findById(token);
           if(tok!=null) {
               Patient patient=patientRepo.findByClientid(tok.getClientid());
               if(patient==null){
                    patient= new Patient(tok.getClientid());
               }
               Client client = clientRepo.findById(tok.getClientid());
               client.onReqested();
               patient.setGender(gender.toUpperCase());
               patient.setDate(dateofbirth);
               patient.setWeight(weight);
               patient.setHeight(height);
               patient.setBlood(bloodid);
               patient.setChronic(chronic);
               patient.setAlergic(alergic);
               client.setName(name);
               client.setSurname(surname);
               clientRepo.save(client);
               patientRepo.save(patient);
               return new StatusObject("ok");
           }
           else{
               return new StatusObject("noauth");
           }
       }
       catch (Exception e) {
           e.printStackTrace();
           return new StatusObject("exception");
        }
    }
    @RequestMapping(value={"/getPatientInfo"}, method = RequestMethod.POST) //addNewForms
    public @ResponseBody Object getPatisentInfo(@RequestHeader("token") String token,@PathVariable("id") String id,HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Patient patient = patientRepo.findById(id);
            Client client = clientRepo.findById(patient.getClientid());
                client.onReqested();

                clientRepo.save(client);
                ArrayList<Object> photos=new ArrayList<>();
                /*if(client.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                    photos.add(fileObjectForm);
                }
                client.setPhotourl(photos);*/
                ClientWithPatientForm clientWithPatientForm = new ClientWithPatientForm(client, patient);
            return clientWithPatientForm;
        }
        else{
            return new StatusObject("noauth");
        }
    }
    @RequestMapping(value="/getPatientProfile", method = RequestMethod.POST) // need put objects
    public @ResponseBody
    Object getPatientProfile(@RequestHeader("token") String token, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
              /*  ArrayList<Object> photos=new ArrayList<>();
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
                logger.info(l);
                 if(l.equals("R")) {
                     homeC = homeCity.getNameRus();
                     workC = workCity.getNameRus();
                 }
                  else{
                            homeC = homeCity.getNameKaz();
                            workC = workCity.getNameKaz();
                    }

                logger.info(homeC+" "+workC+" "+workCity);
                 String bloodid=patient.getBlood();
                 String blood="";
                 if(bloodid!=null) {
                     blood = bloodRepo.findById(patient.getBlood()).getName();
                 }
                 PatientProfileForm form=new PatientProfileForm(patient,client,workC,homeC, blood);
                return form;
            }
            else{
                return new StatusObject("noauth");
            }
        }
        catch (Exception e){
            logger.info(e.getMessage());
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }

    @RequestMapping(value = "/getDoctorProfile",method = RequestMethod.POST)
    public @ResponseBody Object getDPF(@RequestHeader("token") String token, @RequestParam String doctorid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                Client client1=clientRepo.findById(tok.getClientid());
                Doctor doctor=doctorRepo.findById(doctorid);
                Client client=clientRepo.findById(doctor.getClientid());
                client1.onReqested();
                clientRepo.save(client1);

               /* ArrayList<Object> photos=new ArrayList<>();
                if(client.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                    photos.add(fileObjectForm);
                }
                client.setPhotourl(photos);*/
               /*
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
                }
*/
                ServiceType serviceType=serviceTypeRepo.findById(doctor.getServicetypeid()+"");
                List<Service> services=new ArrayList<>();
                for(IDObject i:doctor.getServices()){
                    services.add(serviceRepo.findById(i.getId()));
                }
                City cityH;
                City cityW;
                if(doctor.getHomeAddress()==null) {
                    cityH=new City("","");
                }
                else {
                    cityH = cityRepo.findById(doctor.getHomeAddress().getCityid()+"");
                }
                if(doctor.getWorkAddress()==null) {
                    cityW=new City("","");
                }
                else{
                    cityW = cityRepo.findById(doctor.getWorkAddress().getCityid()+"");
                }
                if(cityH==null){
                    cityH=new City("","");
                }
                if(cityW==null){
                    cityW=new City("","");
                }
                doctor.setServicetypeid(serviceType);
                myPatientForm patientForm=doctor.getPatientbyId(patient.getId());
                if(patientForm==null){
                    patientForm=new myPatientForm(false,false,null);
                }
                /*ArrayList<Object> files=new ArrayList<>();
                for (Object c:doctor.getCertificates()){
                    FileObjectForm f=(FileObjectForm) c;
                    FileObjectForm file=fileUploader.changeToFile(f);
                    files.add(file);
                }
                doctor.setCertificates(files);*/
                ArrayList<CommentForm> commentForms= new ArrayList<>();
                for(Comment j:doctor.getComments()) {
                    Patient patient1=patientRepo.findById(j.getPatient_id());
                    Client client2=clientRepo.findById(patient1.getClientid());
                    CommentForm commentForm=new CommentForm(patient1,client2,j);
                    commentForms.add(commentForm);
                }
                DoctorProfileForm form=new DoctorProfileForm(doctor,client,serviceType,services, cityH,cityW);
                form.setShowPhones(patientForm.isPhonedoctor());
                form.setCommentForms(commentForms);
                List<Order> orders=orderRepo.findByPatientidAndDoctorid(patient.getId(),doctor.getId());
                int home=0, work=0;
                for(Order ii:orders){
                    if(ii.isAtwork()){
                        work++;
                    }
                    else{
                        home++;
                    }
                }
                form.setCalls(home);
                form.setCommings(work);
                form.setMyDoctor(patient.docContains(doctorid));
                form.setFavouriteDoctor(patient.favContains(doctorid));

                ArrayList<Comment> comments=doctor.getComments();
                for(Comment i:comments){
                    Patient patient2=patientRepo.findById(i.getPatient_id());
                    Client clientp=clientRepo.findById(patient2.getClientid());
                    i.setPatient(new PatientListForm(patient2,clientp));
                }
                doctor.setComments(comments);
                return form;
            }
            return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value={"/getScheduleByDay"},method = RequestMethod.POST)
    public @ResponseBody Object getSchedulebyDay(@RequestHeader("token") String token,@RequestParam String doctorid, @RequestParam long time){
        try {
            Token tok = tokenRepo.findById(token);
            if (tok != null) {
                Doctor doctor=doctorRepo.findById(doctorid);
                DoctorScheduleForm scheduleForm=new DoctorScheduleForm();
                Schedule work=doctor.getWorkSchedule();
                Schedule home=doctor.getHomeSchedule();

                Calendar calendar=Calendar.getInstance();
                calendar.setTime(new Date(time));
                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
                int dow=calendar.get(Calendar.DAY_OF_WEEK);
                long start=calendar.getTimeInMillis();
                long stop=start+(24*60*60*1000);
                List<ScheduleForm> works=new ArrayList<>();
                List<ScheduleForm> homes=new ArrayList<>();

                List<Appointment> appointments=appointmentRepo.findByDoctoridAndDayBetween(doctor.getId(),start,stop);
                List<Appointment> appointmentWorks=new ArrayList<>();
                List<Appointment> appointmentHomes=new ArrayList<>();
                for(Appointment aa:appointments){
                    if(aa.isOrder()) {
                        Order order = orderRepo.findById(aa.getOrderid());
                        if(order.isAtwork()){
                            appointmentWorks.add(aa);
                        }
                        else{
                            appointmentHomes.add(aa);
                        }
                    }
                }
                if(work!=null){
                    List<ScheduleForm> forms=getScheduleForms(dow,work);
                    if(forms!=null){
                        for(int i=0;i<forms.size();i++){
                            ScheduleForm form=forms.get(i);
                            for(Appointment aa:appointmentWorks){
                                if(form.getFrom()>=aa.getStart() && (form.getTo()<=aa.getStop())){
                                    form.setEmpty(false);
                                    break;
                                }
                            }
                            works.add(form);
                        }
                    }
                }
                if(home!=null){
                    List<ScheduleForm> forms=getScheduleForms(dow,home);
                    if(forms!=null){
                        for(int i=0;i<forms.size();i++){
                            ScheduleForm form=forms.get(i);
                            for(Appointment aa:appointmentHomes){
                                if(form.getFrom()>=aa.getStart() && (form.getTo()<=aa.getStop()) && aa.isAccepted()){
                                    form.setEmpty(false);
                                    break;
                                }
                            }
                            homes.add(form);
                        }
                    }
                }
                scheduleForm.setHome(homes);
                scheduleForm.setWork(works);

                return scheduleForm;
            } else {
                return new StatusObject("noauth");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    private List<ScheduleForm> getScheduleForms(int dow, Schedule schedule){
        switch (dow){
            case Calendar.SUNDAY:
                return schedule.getSunday();
            case Calendar.MONDAY:
                return schedule.getMonday();
            case Calendar.TUESDAY:
                return schedule.getTuesday();
            case Calendar.WEDNESDAY:
                return schedule.getWednesday();
            case Calendar.THURSDAY:
                return schedule.getThursday();
            case Calendar.FRIDAY:
                return schedule.getFriday();
            case Calendar.SATURDAY:
                return schedule.getSaturday();
                default:
                    return null;

        }
    }
    @RequestMapping(value={"/getProfessionalAch"},method = RequestMethod.POST)
    public @ResponseBody Object getProfAch(@RequestHeader("token") String token, @RequestParam String doctorid){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findById(doctorid);
                List<Object> aa=new ArrayList<>();
                for(TextObject i:doctor.getProfachievments()){
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
    @RequestMapping(value={"/getExtraInfo"},method = RequestMethod.POST)
    public @ResponseBody Object getExtraInfo(@RequestHeader("token") String token, @RequestParam String doctorid){
        try {
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Doctor doctor=doctorRepo.findById(doctorid);
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
    @RequestMapping(value="/getExperience",method=RequestMethod.POST)
    public @ResponseBody Object getExperience(@RequestHeader("token") String token,@RequestParam String doctorid){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
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
    @RequestMapping(value="/getComments/",method = RequestMethod.POST)
    public @ResponseBody Object getComments(@RequestHeader("token") String token, @RequestParam String doctorid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                List<CommentForm> forms=new ArrayList<>();
                Doctor doctor=doctorRepo.findById(doctorid);
                for(Comment i:doctor.getComments()){
                    Patient patient=patientRepo.findById(i.getPatient_id());
                    Client client1=clientRepo.findById(patient.getClientid());
                    ArrayList<Object> photos=new ArrayList<>();
                    /*
                    if(client1.getPhotourl().size()>0){
                        FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client1.getPhotourl().get(client1.getPhotourl().size()-1));
                        photos.add(fileObjectForm);
                    }
                    client1.setPhotourl(photos);*/
                    CommentForm form=new CommentForm(patient,client1,i);
                    forms.add(form);
                }
                return forms;
            }
            else{
                return new StatusObject("noauth");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/searchDoctorsByService/", method = RequestMethod.POST)
    public @ResponseBody Object searchDoctorsByService(@RequestHeader("token") String token, @RequestParam String serviceid,@RequestParam String cityId, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Service service=serviceRepo.findById(serviceid);
                ArrayList<Doctor> doctors=doctorRepo.findByServicetypeid(service.getServtypeid());
                logger.info(doctors.size()+" "+service.getServtypeid());
                ArrayList<DoctorListForm> doctorListForms=new ArrayList<>();
                for(Doctor i:doctors){
                    if(i.getWorkAddress()==null){
                        Address s=new Address();
                        s.setCityid("");
                        i.setWorkAddress(s);
                    }
                    if(i.getHomeAddress()==null){
                        Address s=new Address();
                        s.setCityid("");
                        i.setHomeAddress(s);
                    }
                    if(i.getServicesList().contains(serviceid) && (i.getHomeAddress().getCityid().equals(cityId) || i.getWorkAddress().getCityid().equals(cityId))){
                        ArrayList<Service> services=new ArrayList<>();
                        for(IDObject j:i.getServices()){
                            services.add(serviceRepo.findById(j.getId()));
                        }
                        Client client1= clientRepo.findById(i.getClientid());
                        /*
                        ArrayList<Object> photos=new ArrayList<>();
                        if(client1.getPhotourl().size()>0){
                            FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client1.getPhotourl().get(client1.getPhotourl().size()-1));
                            photos.add(fileObjectForm);
                        }
                        client1.setPhotourl(photos);*/
                        doctorListForms.add(
                                new DoctorListForm(
                                        i,
                                       client1,
                                        serviceTypeRepo.findById(i.getServicetypeid()+""),
                                        services)
                        );
                    }

                    else
                        continue;

                }
                return doctorListForms;
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
        @RequestMapping(value="/searchDoctorsByServiceType/", method = RequestMethod.POST)
    public @ResponseBody Object searchDoctorsByServiceType(@RequestHeader("token") String token, @RequestParam String servicetypeid,@RequestParam String cityId, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                ArrayList<Doctor> doctors=doctorRepo.findByServicetypeid(servicetypeid);
                logger.info(doctors.size()+" "+servicetypeid);
                ArrayList<DoctorListForm> doctorListForms=new ArrayList<>();
                for(Doctor i:doctors){
                    if(i.getWorkAddress()==null){
                        Address s=new Address();
                        s.setCityid("");
                        i.setWorkAddress(s);
                    }
                    if(i.getHomeAddress()==null){
                        Address s=new Address();
                        s.setCityid("");
                        i.setHomeAddress(s);
                    }
                    if(i.getWorkAddress().getCityid().equals(cityId) || i.getHomeAddress().getCityid().equals(cityId)){
                        ArrayList<Service> services=new ArrayList<>();
                        for(IDObject j:i.getServices()){
                            services.add(serviceRepo.findById(j.getId()));
                        }
                        Client client1= clientRepo.findById(i.getClientid());/*
                        ArrayList<Object> photos=new ArrayList<>();
                        if(client1.getPhotourl().size()>0){
                            FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client1.getPhotourl().get(client1.getPhotourl().size()-1));
                            photos.add(fileObjectForm);
                        }
                        client1.setPhotourl(photos);*/
                        doctorListForms.add(
                                new DoctorListForm(
                                        i,
                                        client1,
                                        serviceTypeRepo.findById(i.getServicetypeid()+""),
                                        services));
                    }
                    else
                        continue;
                }
                return doctorListForms;
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
    @RequestMapping(value="/searchDoctorsByServiceTypeWithText/", method = RequestMethod.POST)
    public @ResponseBody Object searchDoctorsByServiceType1(@RequestHeader("token") String token, @RequestParam String servicetypeid, @RequestParam String text, @RequestParam String cityId, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client11=clientRepo.findById(tok.getClientid());
                client11.onReqested();
                clientRepo.save(client11);
                ArrayList<Doctor> doctors=doctorRepo.findByServicetypeid(servicetypeid);
                logger.info(doctors.size()+" "+servicetypeid);
                ArrayList<DoctorListForm> doctorListForms=new ArrayList<>();
                for(Doctor i:doctors){
                    if(i.getWorkAddress()==null){
                        Address s=new Address();
                        s.setCityid("");
                        i.setWorkAddress(s);
                    }
                    if(i.getHomeAddress()==null){
                        Address s=new Address();
                        s.setCityid("");
                        i.setHomeAddress(s);
                    }
                    Client client=clientRepo.findById(i.getClientid());
                    if(((client.getName()!=null && client.getName().toLowerCase().contains(text.toLowerCase())) || (client.getSurname()!=null && client.getSurname().toLowerCase().contains(text.toLowerCase()))) && (i.getHomeAddress().getCityid().equals(cityId) || i.getWorkAddress().getCityid().equals(cityId))){
                        ArrayList<Service> services=new ArrayList<>();
                        for(IDObject j:i.getServices()){
                            services.add(serviceRepo.findById(j.getId()));
                        }
                        Client client1= clientRepo.findById(i.getClientid());/*
                        ArrayList<Object> photos=new ArrayList<>();
                        if(client1.getPhotourl().size()>0){
                            FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client1.getPhotourl().get(client1.getPhotourl().size()-1));
                            photos.add(fileObjectForm);
                        }
                        client1.setPhotourl(photos);*/
                        doctorListForms.add(
                                new DoctorListForm(
                                        i,
                                        client1,
                                        serviceTypeRepo.findById(i.getServicetypeid()+""),
                                        services));
                    }
                    else
                        continue;
                }
                return doctorListForms;
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
    @RequestMapping(value="/searchDoctorsByServiceWithText/", method = RequestMethod.POST)
    public @ResponseBody Object searchDoctorsByService1(@RequestHeader("token") String token, @RequestParam String serviceid, @RequestParam String text,@RequestParam String cityId, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client11=clientRepo.findById(tok.getClientid());
                client11.onReqested();
                clientRepo.save(client11);
                Service service=serviceRepo.findById(serviceid);
                ArrayList<Doctor> doctors=doctorRepo.findByServicetypeid(service.getServtypeid());
                logger.info(doctors.size()+" "+service.getServtypeid());
                ArrayList<DoctorListForm> doctorListForms=new ArrayList<>();
                for(Doctor i:doctors){
                    if(i.getWorkAddress()==null){
                        Address s=new Address();
                        s.setCityid("");
                        i.setWorkAddress(s);
                    }
                    if(i.getHomeAddress()==null){
                        Address s=new Address();
                        s.setCityid("");
                        i.setHomeAddress(s);
                    }
                    if(i.getServicesList().contains(serviceid) && (i.getWorkAddress().getCityid().equals(cityId) || i.getHomeAddress().getCityid().equals(cityId))) {
                        Client client = clientRepo.findById(i.getClientid());
                        if ((client.getName()!=null && client.getName().toLowerCase().contains(text.toLowerCase())) || (client.getSurname()!=null && client.getSurname().toLowerCase().contains(text.toLowerCase()))) {
                            ArrayList<Service> services = new ArrayList<>();
                            for (IDObject j : i.getServices()) {
                                services.add(serviceRepo.findById(j.getId()));
                            }
                            Client client1= clientRepo.findById(i.getClientid());
                            ArrayList<Object> photos=new ArrayList<>();/*
                            if(client1.getPhotourl().size()>0){
                                FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client1.getPhotourl().get(client1.getPhotourl().size()-1));
                                photos.add(fileObjectForm);
                            }
                            client1.setPhotourl(photos);*/
                            doctorListForms.add(
                                    new DoctorListForm(
                                            i,
                                            client1,
                                            serviceTypeRepo.findById(i.getServicetypeid()+""),
                                            services)
                            );
                        }
                    }
                    else
                        continue;
                }
                return doctorListForms;
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
    @RequestMapping(value = "/addComment/{doctorid}",method = RequestMethod.POST)
    public @ResponseBody Object addComment(@RequestHeader("token") String token, @PathVariable("doctorid") String doctorid, @RequestParam String message, boolean impression, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                Doctor doctor=doctorRepo.findById(doctorid);
                Comment comment=new Comment(patient.getId(),message);
                comment.setImpression(impression);
                doctor.addComment(comment);
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
    @RequestMapping(value="/getFavouriteDoctorsList/", method = RequestMethod.POST)
    public @ResponseBody Object getFavouriteDL(@RequestHeader("token") String token, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client clien11t=clientRepo.findById(tok.getClientid());
                clien11t.onReqested();
                clientRepo.save(clien11t);
                List<DoctorListForm> doctorListForms=new ArrayList<>();
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                for(IDObject i:patient.getFavs()){
                    Doctor doctor=doctorRepo.findById(i.getId());
                    Client client=clientRepo.findById(doctor.getClientid());
                    ArrayList<Object> photos=new ArrayList<>();/*
                    if(client.getPhotourl().size()>0){
                        FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                        fileObjectForm.setId(((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1)).getId());
                        photos.add(fileObjectForm);
                    }
                    client.setPhotourl(photos);*/
                    ServiceType serviceType=serviceTypeRepo.findById(doctor.getServicetypeid()+"");
                    ArrayList<Service> services=new ArrayList<>();
                    for(IDObject jj:doctor.getServices()){
                        services.add(serviceRepo.findById(jj.getId()));
                    }
                    DoctorListForm listForm=new DoctorListForm(doctor,client,serviceType,services);
                    doctorListForms.add(listForm);
                }
                return doctorListForms;
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = {"/getMyRequestsList"}, method = RequestMethod.POST) //need to make new Form
    public @ResponseBody Object getMyReqsList(@RequestHeader("token") String token, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            Patient patient=patientRepo.findByClientid(tok.getClientid());
            List<PatientRequestForm> requestForms=new ArrayList<>();
            for(IDObject i:patient.getMydocs()){
                Doctor doctor=doctorRepo.findById(i.getId());
                Client client=clientRepo.findById(doctor.getClientid());/*
                ArrayList<Object> photos=new ArrayList<>();
                if(client.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                    fileObjectForm.setId(((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1)).getId());
                    photos.add(fileObjectForm);
                }
                client.setPhotourl(photos);*/
                ServiceType serviceType=serviceTypeRepo.findById(doctor.getServicetypeid()+"");
                ArrayList<Service> services=new ArrayList<>();
                for(String ii:doctor.getServicesList()){
                    Service service=new Service();
                }
                myPatientForm form=doctor.getPatientbyId(patient.getId());
                DoctorListForm doctorProfileForm=new DoctorListForm(doctor,client,serviceType,services);

                PatientRequestForm requestForm=new PatientRequestForm(doctorProfileForm,"","");
                if(form==null){
                    requestForm=new PatientRequestForm(doctorProfileForm,"accept","cancelled");
                }
                else{
                    if(!form.isPhonedoctor() && form.phonefinished){
                        requestForm=new PatientRequestForm(doctorProfileForm,"phone","cancelled");
                        requestForms.add(requestForm);
                    }
                    else if(form.isPhonedoctor()){
                        requestForm=new PatientRequestForm(doctorProfileForm,"phone","accepted");
                        requestForms.add(requestForm);
                    }
                    else if(form.isPhone() && !form.isPhonedoctor()){
                        requestForm=new PatientRequestForm(doctorProfileForm,"phone","inprocess");
                        requestForms.add(requestForm);
                    }

                    else if(!form.isAccepted()){
                        requestForm=new PatientRequestForm(doctorProfileForm,"accept","inprocess");
                        requestForms.add(requestForm);
                    }
                    else if(form.isAccepted()){
                        requestForm=new PatientRequestForm(doctorProfileForm,"accept","accepted");
                        requestForms.add(requestForm);
                    }
                }
            }
            return requestForms;
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/getMyDoctorsList"}, method = RequestMethod.POST) //need to make new Form
    public @ResponseBody Object getMyDocsList(@RequestHeader("token") String token, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client cl11ient=clientRepo.findById(tok.getClientid());
                cl11ient.onReqested();
                clientRepo.save(cl11ient);

            Patient patient = patientRepo.findByClientid(tok.getClientid());
            List<DoctorListForm> list = new ArrayList<>();

            for (IDObject i : patient.getMydocs()) {
                Doctor doctor=doctorRepo.findById(i.getId());
                Client client=clientRepo.findById(doctor.getClientid());


/*                ArrayList<Object> photos=new ArrayList<>();
                if(client.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                    fileObjectForm.setId(((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1)).getId());
                    photos.add(fileObjectForm);
                }
                client.setPhotourl(photos);*/
                boolean a=false;
                myPatientForm form=doctor.getPatientbyId(patient.getId());
                if(form==null){
                    a=true;
                }
                else{
                    if(!form.isAccepted()){
                        a=true;
                    }
                }
                if(a) continue;
                ServiceType serviceType=serviceTypeRepo.findById(doctor.getServicetypeid()+"");
                ArrayList<Service> services=new ArrayList<>();
                for(IDObject jj:doctor.getServices()){
                    services.add(serviceRepo.findById(jj.getId()));
                }
                list.add(new DoctorListForm(doctor,client,serviceType,services));
            }
            return list;
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value="/addRate/{doctorid}", method = RequestMethod.POST)
    public @ResponseBody Object addRate(@RequestHeader("token") String token,@PathVariable("doctorid") String doctorid, @RequestParam int num, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                Rate rate=new Rate(patient.getId(), num);
                Doctor doctor=doctorRepo.findById(doctorid);
                doctor.setRate(rate);
                doctorRepo.save(doctor);
                return new StatusObject("ok");
            }
            else
                return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = {"/addMail"},method = RequestMethod.POST)
    public @ResponseBody Object addMail(@RequestHeader("token") String token,@RequestParam String mail, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client = clientRepo.findById(tok.getClientid());
            ArrayList<String> strings=new ArrayList<>();
            strings.add(mail);
            client.setMails(strings);
            client.onReqested();
            clientRepo.save(client);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/deleteMail"},method = RequestMethod.POST)
    public @ResponseBody Object deleteMail(@RequestHeader("token") String token,@RequestParam String mail,HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client = clientRepo.findById(tok.getClientid());
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            client.deleteMail(mail);
            client.onReqested();
            clientRepo.save(client);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/addPhone/"},method = RequestMethod.POST)
    public @ResponseBody Object addPhone(@RequestHeader("token") String token,@RequestParam String phone,HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client = clientRepo.findById(tok.getClientid());
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            ArrayList<String> strings=new ArrayList<>();
            strings.add(phone);
            client.setPhones(strings);
            client.onReqested();
            clientRepo.save(client);return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/deletePhone"},method = RequestMethod.POST)
    public @ResponseBody Object deletePhone(@RequestHeader("token") String token,@RequestParam String phone, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client = clientRepo.findById(tok.getClientid());
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            client.deletePhone(phone);
            client.onReqested();
            clientRepo.save(client);return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/setPush"},method = RequestMethod.POST)
    public @ResponseBody Object setPush(@RequestHeader("token") String token,@RequestParam boolean push, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client = clientRepo.findById(tok.getClientid());
            client.setPush(push);
            client.onReqested();
            clientRepo.save(client);return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/setPubl"},method = RequestMethod.POST)
    public @ResponseBody Object setPubl(@RequestHeader("token") String token,@RequestParam boolean publ, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client = clientRepo.findById(tok.getClientid());
            client.setPubl(publ);
            client.onReqested();
            clientRepo.save(client);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
  /*  @RequestMapping(value = {"/addAddress"}, method = RequestMethod.POST)
    public @ResponseBody Object addAddress(@RequestHeader("token") String token, @RequestParam String cityId, @RequestParam String address, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);

            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            patient.addAddress(new Address(cityId,address));
            patientRepo.save(patient);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }*/
  /*  @RequestMapping(value = {"/changeAddress"}, method = RequestMethod.POST)
    public @ResponseBody Object changeAddress(@RequestHeader("token") String token,@RequestParam String addressid, @RequestParam String cityId, @RequestParam String address, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);

        if(tok!=null){
            Client client=clientRepo.findById(tok.getClientid());
            client.onReqested();
            clientRepo.save(client);
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            Address add=new Address(cityId,address);
            boolean changed=patient.changeAddressById(addressid,add);
            if(changed) {
                patientRepo.save(patient);
                return new StatusObject("ok");
            }
            else
                return new StatusObject("noaddressid");
        }
        return new StatusObject("noauth");
    }*/
    @RequestMapping(value = {"/changeAddress"}, method = RequestMethod.POST)
    public @ResponseBody Object changeAddressWithLocation(@RequestHeader("token") String token,@RequestParam String addressid, @RequestParam String cityId, @RequestParam String address,@RequestParam double longitude, @RequestParam double latitude, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);g

        if(tok!=null){
            Client client=clientRepo.findById(tok.getClientid());
            client.onReqested();
            clientRepo.save(client);
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            Address add=new Address(cityId,address);
            add.setLocation(new Map(latitude,longitude));
            boolean changed=patient.changeAddressById(addressid,add);
            if(changed) {
                patientRepo.save(patient);
                return new StatusObject("ok");
            }
            else
                return new StatusObject("noaddressid");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/addAddress"}, method = RequestMethod.POST)
    public @ResponseBody Object addAddressWithLocation(@RequestHeader("token") String token, @RequestParam String cityId, @RequestParam String address,@RequestParam double longitude, @RequestParam double latitude, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);

        if(tok!=null){
            Client client=clientRepo.findById(tok.getClientid());
            client.onReqested();
            clientRepo.save(client);
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            Address addr=new Address(cityId,address);
            addr.setLocation(new Map(latitude,longitude));
            patient.addAddress(addr);
            patientRepo.save(patient);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/addAddressWithCompany"}, method = RequestMethod.POST)
    public @ResponseBody Object addAddressWC(@RequestHeader("token") String token, @RequestParam String cityId, @RequestParam String address,@RequestParam String company, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            Client client=clientRepo.findById(tok.getClientid());
            client.onReqested();
            clientRepo.save(client);
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            Address ad=new Address(cityId,address);
            ad.setName(company);
            patient.addAddress(ad);
            patientRepo.save(patient);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/deleteAddress"}, method = RequestMethod.POST)
    public @ResponseBody Object deleteAdress(@RequestHeader("token") String token, @RequestParam String id, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            boolean deleted=patient.deleteAddressById(id);
            if(deleted) {
                patientRepo.save(patient);
                return new StatusObject("ok");
            }
            else{
                return new StatusObject("noaddressid");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/addPhoto"}, method = RequestMethod.POST)
    public @ResponseBody Object addPhoto(@RequestHeader("token") String token, @RequestParam String file, RedirectAttributes redirectAttributes, HttpServletRequest request) throws IOException {
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "file is empty");
                return new StatusObject("emptyfile");
            } else {
                Client client = clientRepo.findById(tok.getClientid());
                Patient patient=patientRepo.findByClientid(tok.getClientid());

                byte[] byteFile=fileUploader.decodeFileFromBase64(file);
                int l=0;
                while(true) {
                    //String res=fileUploader.uploadText(file, "photo"+patient.getId()+l);
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
                fileObjectForm.setUrl(file);
                fileObjectForm.setType("photo");
                if(client.getPhotourl().size()>0){
                    fileUploader.deleteFile(((FileObjectForm)client.getPhotourl().get(0)).getUrl());
                }
                client.addPhoto(fileObjectForm);
                clientRepo.save(client);
            }
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/deletePhoto"}, method = RequestMethod.POST)
    public @ResponseBody Object delPhoto(@RequestHeader("token") String token, RedirectAttributes redirectAttributes, HttpServletRequest request) {
       Token tok= tokenRepo.findById(token);
            if(tok!=null) {

                Client client = clientRepo.findById(tok.getClientid());
                boolean deleted=true;
                if(client.getPhotourl().size()>0)
                fileUploader.deleteFile(((FileObjectForm) client.getPhotourl().get(0)).getUrl());
                client.deletePhoto();
                client.onReqested();
                clientRepo.save(client);
                if(deleted)
                return new StatusObject("ok");
                else return new StatusObject("notdeleted");
            }
        return new StatusObject("noauth");
    }  /*@RequestMapping(value = {"/deletePhotos"}, method = RequestMethod.POST)
    public @ResponseBody Object delPhotos(@RequestHeader("token") String token, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            Client client = clientRepo.findById(tok.getClientid());
            client.setPhotourl(new ArrayList<>());
            client.onReqested();
            clientRepo.save(client);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }*/
    @RequestMapping(value={"/addFavouriteDoctor"}, method = RequestMethod.POST)
    public @ResponseBody Object addFavDoc(@RequestHeader("token") String token, @RequestParam String doctorid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient = patientRepo.findByClientid(tok.getClientid());
                patient.addFav(doctorid);
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                patientRepo.save(patient);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/deleteFavouriteDoctor", method=RequestMethod.POST)
    public @ResponseBody Object delFavDoc(@RequestHeader("token") String token, @RequestParam String doctorid,HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient = patientRepo.findByClientid(tok.getClientid());
                boolean a = patient.deleteFav(doctorid);
                patientRepo.save(patient);
                return a;
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = {"/getFavouriteDoctors"}, method = RequestMethod.POST) //need to make new Form
    public @ResponseBody Object getFavDocs(@RequestHeader("token") String token,HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);

            Patient patient = patientRepo.findById(tok.getClientid());
            List<Doctor> list = new ArrayList<>();
            for (IDObject i : patient.getFavs()) {
                list.add(doctorRepo.findById(i.getId()));
            }
            return list;
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value={"/addMyDoctor"}, method = RequestMethod.POST)
    public @ResponseBody Object addMyDoc(@RequestHeader("token") String token, @RequestParam String doctorid,HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient = patientRepo.findByClientid(tok.getClientid());
                Doctor doctor=doctorRepo.findById(doctorid);
                doctor.getId();
                boolean a=patient.addDoc(doctorid);
                if(a) {
                    patientRepo.save(patient);
                    myPatientForm patientForm=new myPatientForm(false, false, patient.getId());
                    doctor.addMyPatient(patientForm);
                    doctorRepo.save(doctor);
                    return new StatusObject("ok");
                }
                else{
                    myPatientForm form=doctor.getPatientbyId(patient.getId());
                    if(form==null){
                        myPatientForm patientForm=new myPatientForm(false, false, patient.getId());
                        doctor.addMyPatient(patientForm);
                        doctorRepo.save(doctor);
                        return new StatusObject("ok request resent");
                    }
                    else {
                        return new StatusObject("doctorwasinside");
                    }
                }
            }
            else return new StatusObject("noauth");
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return new StatusObject("nullpointer");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value={"/sendPhoneRequest"}, method = RequestMethod.POST)
    public @ResponseBody Object sendPhone(@RequestHeader("token") String token, @RequestParam String doctorid,HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient = patientRepo.findByClientid(tok.getClientid());
                boolean bb=patient.getMyDocs().contains(doctorid);
                if(bb) {
                    Doctor doctor = doctorRepo.findById(doctorid);
                    myPatientForm patientForm = doctor.getPatientbyId(patient.getId());
                    patientForm.setPhone(true);
                    doctor.setPatientById(patient.getId(),patientForm);
                    doctorRepo.save(doctor);
                    return new StatusObject("ok");

                }
                else{
                    return new StatusObject("nodoctorinside");
                }
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/deleteMyDoctor", method=RequestMethod.POST)
    public @ResponseBody Object delMyDoc(@RequestHeader("token") String token, @RequestParam String doctorid,HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient = patientRepo.findByClientid(tok.getClientid());
                boolean a = patient.deleteMyDoc(doctorid);
                patientRepo.save(patient);
                Doctor doctor=doctorRepo.findById(doctorid);
                if(doctor!=null){
                    doctor.deleteMyPatient(patient.getId());
                    doctorRepo.save(doctor);
                }
                if(a){
                    return new StatusObject("ok");
                }
                else{
                    return new StatusObject("notdeleted");
                }
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = {"/getMyDoctors"}, method = RequestMethod.POST) //need to make new Form
    public @ResponseBody Object getMyDocs(@RequestHeader("token") String token, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);/*
                ArrayList<Object> photos=new ArrayList<>();
                if(client.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                    fileObjectForm.setId(((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1)).getId());
                    photos.add(fileObjectForm);
                }
                client.setPhotourl(photos);*/
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            List<Doctor> list = new ArrayList<>();
            for (IDObject i : patient.getMydocs()) {
                list.add(doctorRepo.findById(i.getId()  ));
            }
            return list;
        }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value="/sendChatRequest",method=RequestMethod.POST)
    public @ResponseBody Object sendChatRequest(@RequestHeader("token") String token,@RequestParam String doctorid){
        try {
            Token tok = tokenRepo.findById(token);
            if (tok != null) {
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient doctor = patientRepo.findByClientid(tok.getClientid());
                String id=doctor.getId();
                Chat chat = chatRepo.findOneByDoctoridAndPatientid(doctorid, id);
                Doctor doctor1 = doctorRepo.findById(doctorid);
                if (doctor1 == null) {
                    return new StatusObject("nodoctor");
                }
                if (doctor == null) {
                    return new StatusObject("nopatient");
                }
                if (chat == null) {
                    Chat chat1 = new Chat(doctorid, id);
                    chat1.setStatus("patient");
                    chat1.setChatRequest(ChatRequest.CLIENT_SENT);
                    chatRepo.save(chat1);
                    chat = chatRepo.findOneByDoctoridAndPatientid(doctorid, id);
                    return new StatusObject("sent");
                }
                else {
                    ChatRequest chatRequest=chat.getChatRequest();
                    switch (chatRequest){
                        case CLIENT_SENT:
                            chat.setChatRequest(ChatRequest.CLIENT_RESENT);
                            chatRepo.save(chat);
                            return new StatusObject("resent");
                        case CLIENT_RESENT:
                            return new StatusObject("resent");
                        case DOCTOR_DECLINE:
                            chat.setChatRequest(ChatRequest.CLIENT_RESENT);
                            chatRepo.save(chat);
                            return new StatusObject("resent");
                        case DOCTOR_ACCEPTED:
                            return new StatusObject("open");
                    }
                }
                return new ChatIdForm(chat.getId(), null);
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/openChat", method = RequestMethod.POST)
    public @ResponseBody Object openChat(@RequestHeader("token") String token,  @RequestParam String doctorid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient doctor = patientRepo.findByClientid(tok.getClientid());
                String id=doctor.getId();
                Chat chat = chatRepo.findOneByDoctoridAndPatientid(doctorid, id);
                Doctor doctor1 = doctorRepo.findById(doctorid);
                if (doctor1 == null) {
                    return new StatusObject("nodoctor");
                }
                if (doctor == null) {
                    return new StatusObject("nopatient");
                }
                if (chat == null) {
                    Chat chat1 = new Chat(doctorid, id);
                    chat1.setStatus("patient");
                    chat1.setChatRequest(ChatRequest.CLIENT_SENT);
                    chatRepo.save(chat1);
                    chat = chatRepo.findOneByDoctoridAndPatientid(doctorid, id);
                }
                FileObjectForm fileObjectForm=null;
              /*  if(client.getPhotourl().size()>0){
                    fileObjectForm=fileUploader.changeToFile((FileObjectForm)client.getPhotourl().get(0));
                }*/
                return new ChatIdForm(chat.getId(),fileObjectForm);
            }
            else return new StatusObject("noauth");
        }
        catch (NullPointerException e){
            logger.info(null);
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
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                return chatRepo.findByPatientid(patient.getId());
            }
        else return new StatusObject("noauth");
    }
    @RequestMapping(value="/getAllChatsList", method = RequestMethod.POST)
    public @ResponseBody Object getAllChatsList(@RequestHeader("token") String token, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
              //  ChatListWholeForm wholeForm=new ChatListWholeForm();
                Client client11=clientRepo.findById(tok.getClientid());
                client11.onReqested();
                clientRepo.save(client11);
                /*if(client11.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm)client11.getPhotourl().get(0));
                    wholeForm.setMyphoto(fileObjectForm);
                }*/
                Patient patient;
                try {
                    patient = patientRepo.findByClientid(tok.getClientid());
                }
                catch (NullPointerException e){
                    patient=new Patient();
                }
                List<Chat> chats = chatRepo.findByPatientid(patient.getId());
                List<ChatListForm> forms = new ArrayList<>();
                for (Chat i : chats) {
                    Client oppClient;
                    try {
                        oppClient = clientRepo.findById(doctorRepo.findById(i.getDoctorid()).getClientid());
                        /*
                        ArrayList<Object> photos=new ArrayList<>();
                        if(oppClient.getPhotourl().size()>0){
                            FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) oppClient.getPhotourl().get(oppClient.getPhotourl().size()-1));
                            fileObjectForm.setId(((FileObjectForm) oppClient.getPhotourl().get(oppClient.getPhotourl().size()-1)).getId());
                            photos.add(fileObjectForm);
                        }
                        oppClient.setPhotourl(photos);
                        */
                    }
                    catch (NullPointerException e){
                        continue;
                    }
                    MessageForm form = null;
                    Client client;
                    String serviceInfo = "";
                    Doctor doctor = doctorRepo.findById(i.getDoctorid());
                    if(i.getMessages().size()>0) {
                        Message message = i.getLastMessage();

                        if (message.getType().equals("audio") )
                        {
                            FileObjectForm objectForm=(FileObjectForm) message.getInfo();
                            FileObjectForm fileObjectForm = fileUploader.changeToFile(objectForm);
                            fileObjectForm.setId(objectForm.getId());
                            message.setInfo(fileObjectForm);
                        }

                        client = clientRepo.findById(message.getClientid() + "");




                        if (client != null) {
                            Doctor doctor1 = doctorRepo.findByClientid(client.getId());

                            if (doctor1 != null) {
                                form = new MessageForm(message, client, doctor);
                                form.setMymessage(false);
                            } else {
                                form = new MessageForm(message, client, patient);
                                form.setMymessage(true);
                            }

                        }
                    }
                    client=clientRepo.findById(patient.getClientid());
                    if(doctor.getServicetypeid()!=null){
                        ServiceType serviceType=serviceTypeRepo.findById(doctor.getServicetypeid()+"");
                        try {
                            client.getLang() ;

                        }
                        catch (NullPointerException e){
                            client.setLang("R");
                        }
                        switch (client.getLang()) {
                            case "K":
                                serviceInfo += serviceType.getNameKaz();
                                for(IDObject ii:doctor.getServices()){
                                    Service i3=serviceRepo.findById(ii.getId());
                                    if(i3!=null && i3.getInfoKaz()!=null)
                                        serviceInfo+=", "+i3.getInfoKaz();
                                    else{
                                        serviceInfo+=", "+null;
                                    }
                                }
                                break;
                            default:
                                serviceInfo += serviceType.getNameRus();
                                for(IDObject ii:doctor.getServices()){
                                    Service i3=serviceRepo.findById(ii.getId());
                                    if(i3!=null && i3.getInfoRus()!=null)
                                        serviceInfo+=", "+i3.getInfoRus();
                                    else{
                                        serviceInfo+=", "+null;
                                    }
                                }
                        }
                    }
                    forms.add(new ChatListForm(i, form, doctor, oppClient,serviceInfo));
                }
              /*  wholeForm.setResults(forms);
                return wholeForm;*/
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
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient doctor = patientRepo.findByClientid(tok.getClientid());
                Chat chat = chatRepo.findById(chatid);
                if(chat.getChatRequest().equals(ChatRequest.DOCTOR_ACCEPTED)) {
                    Message message = new Message(doctor.getClientid(), "text", text);
                    chat.addMessage(message);
                    if(chat.getStatus().equals("doctor")){
                        chat.setUnread(0);
                    }
                    chat.setStatus("patient");
                    chat.unreadPlus();
                    chatRepo.save(chat);
                    return new StatusObject("ok");
                }
                else{
                    return new StatusObject("nopermission");
                }
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
                Patient patient = patientRepo.findByClientid(tok.getClientid());
                Chat chat = chatRepo.findById(chatid);
                List<Message> messages=new ArrayList<>();
                messages.addAll(chat.getMessages());
                boolean deleted=false;
                for(Message i:messages){
                    if(i.getId().equals(messageid)){
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
            if(tok!=null){    Client cliqqent=clientRepo.findById(tok.getClientid());
                cliqqent.onReqested();
                clientRepo.save(cliqqent);
                Patient patient = patientRepo.findByClientid(tok.getClientid());
                Chat chat = chatRepo.findById(chatid);
                if (!chat.getStatus().equals("patient")) {
                    chat.setUnread(0);
                }
                chatRepo.save(chat);
                List<Message> list=chat.getMessages();
                List<MessageForm> formList=new ArrayList<>();
                for(Message i:list){
                    Doctor doctor=doctorRepo.findByClientid(i.getClientid()+"");
                    Client client=clientRepo.findById(i.getClientid()+"");/*
                    ArrayList<Object> photos=new ArrayList<>();
                    if(client.getPhotourl().size()>0){
                        FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                        fileObjectForm.setId(((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1)).getId());
                        photos.add(fileObjectForm);
                    }
                    client.setPhotourl(photos);*/

                    if(i.getType().equals("audio")){
                        FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm)i.getInfo());
                        i.setInfo(fileObjectForm);
                    }

                    if(doctor!=null){
                        MessageForm form= new MessageForm(i,client,doctor);
                        form.setMymessage(false);
                        formList.add(form);
                    }
                    else{
                        MessageForm form=new MessageForm(i,client,patient);
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
    @RequestMapping(value="/sendFileMessage",method = RequestMethod.POST)
    public @ResponseBody Object sendFileMes(@RequestHeader("token") String token,@RequestParam String chatid, @RequestParam String type, @RequestParam String file, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient doctor = patientRepo.findByClientid(tok.getClientid());
                Chat chat = chatRepo.findById(chatid);
                if(chat.getChatRequest().equals(ChatRequest.DOCTOR_ACCEPTED)) {
                    Message message = new Message(doctor.getClientid(), type);
                    chat.addMessage(message);
                    message = chat.getMessages().get(chat.getMessages().size() - 1);
                    chat.getMessages().remove(message);
                    if(chat.getStatus().equals("patient")){
                        chat.setUnread(0);
                    }
                    chat.setStatus("patient");
                    chat.unreadPlus();

                    int l = 0;
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
                        }
                        if (!res.equals("")) {
                            file = res;
                            break;
                        } else if (res.equals("exception")) {
                            break;
                        } else {
                            l++;
                        }
                    }
                    if(type.equals("photo"))
                    file=fileUploader.getLhost()+"?url="+file;
                    FileObjectForm fileObjectForm = new FileObjectForm(file);
                    fileObjectForm.setType(type);
                    fileObjectForm.setUrl(url);
                    message.setInfo(fileObjectForm);
                    chat.getMessages().add(message);
                    chatRepo.save(chat);
                    return new StatusObject("ok");
                }
                else{
                    return new StatusObject("nopermission");
                }
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }

    @RequestMapping(value="/addOrder",method = RequestMethod.POST)
    public @ResponseBody Object addOrder(@RequestHeader("token") String token, @RequestParam boolean atwork, @RequestParam String doctorid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = new Order(doctorid, id);
                order.setAtwork(atwork);
                order.setStatus("patientstarted");
                orderRepo.save(order);
                return new IDObject(order.getId());//FORM need
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/setOrderInfoWorkplace", method=RequestMethod.POST)
    public @ResponseBody Object addOrderInfo(@RequestHeader("token") String token, @RequestParam String orderid, @RequestParam long chosetime, @RequestParam String periodTime, @RequestParam List<String> services, @RequestParam String text, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = orderRepo.findById(orderid);
                Doctor doctor=doctorRepo.findById(order.getDoctorid());
                order.setChoseTime(chosetime);
                order.setCreatedTime(new Date().getTime());
                JsonElement obel=new JsonParser().parse(periodTime);
                JsonObject object=obel.getAsJsonObject();
                ScheduleForm scheduleForm=new ScheduleForm(object.get("from").getAsInt(),object.get("to").getAsInt());
                scheduleForm.setEmpty(false);
                order.setPeriodTime(scheduleForm);
                order.setTextMessage(text);
                order.setServicess(services);
                order.setAtwork(true);
                order.setStatus("patientcreated");
                orderRepo.save(order);

                Appointment appointment=new Appointment(chosetime,scheduleForm.getFrom(),scheduleForm.getTo(),doctor.getId());
                appointment.setOrder(true);
                appointment.setOrderid(orderid);
                appointmentRepo.save(appointment);

                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/setOrderInfoHome", method=RequestMethod.POST)
    public @ResponseBody Object addOrderInfoHome(@RequestHeader("token") String token, @RequestParam String orderid,  @RequestParam long chosetime,  @RequestParam String text, @RequestParam String periodTime,
                                                @RequestParam List<String> services, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = orderRepo.findById(orderid);
                Doctor doctor=doctorRepo.findById(order.getDoctorid());
                order.setChoseTime(chosetime);
                order.setCreatedTime(new Date().getTime());
                order.setTextMessage(text);
                JsonElement obel=new JsonParser().parse(periodTime);
                JsonObject object=obel.getAsJsonObject();
                ScheduleForm scheduleForm=new ScheduleForm(object.get("from").getAsInt(),object.get("to").getAsInt());
                scheduleForm.setEmpty(false);
                order.setPeriodTime(scheduleForm);
                order.setServicess(services);
                List<Object> ows=new ArrayList<>();
                ows.add(doctor.getHomePlaceOwn());
                order.setOwnServices(ows);
                order.setAddress(patient.getHomeAddress());
                order.setAtwork(false);
                order.setStatus("patientcreated");
                orderRepo.save(order);
                Appointment appointment=new Appointment(chosetime,scheduleForm.getFrom(),scheduleForm.getTo(),doctor.getId());
                appointment.setOrder(true);
                appointment.setOrderid(orderid);
                appointmentRepo.save(appointment);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }


    //

    @RequestMapping(value="/setOrderInfoWorkplaceWithOwnService", method=RequestMethod.POST)
    public @ResponseBody Object addOrderInfoOwnService(@RequestHeader("token") String token, @RequestParam String orderid, @RequestParam long chosetime,@RequestParam List<String> ownServices, @RequestParam String periodTime, @RequestParam String text
            , HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){

                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = orderRepo.findById(orderid);
                Doctor doctor=doctorRepo.findById(order.getDoctorid());
                order.setChoseTime(chosetime);
                order.setCreatedTime(new Date().getTime());
                JsonElement obel=new JsonParser().parse(periodTime);
                JsonObject object=obel.getAsJsonObject();
                ScheduleForm scheduleForm=new ScheduleForm(object.get("from").getAsInt(),object.get("to").getAsInt());
                scheduleForm.setEmpty(false);
                order.setPeriodTime(scheduleForm);
                order.setTextMessage(text);
                order.setAtwork(true);
                order.setOwnServicess(ownServices);
                order.setStatus("patientcreated");
                orderRepo.save(order);

                Appointment appointment=new Appointment(chosetime,scheduleForm.getFrom(),scheduleForm.getTo(),doctor.getId());
                appointment.setOrder(true);
                appointment.setOrderid(orderid);
                appointmentRepo.save(appointment);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/setOrderInfoHomeWithOwnService", method=RequestMethod.POST)
    public @ResponseBody Object addOrderInfoHomewithOwn(@RequestHeader("token") String token, @RequestParam String orderid,@RequestParam List<String> ownServices,  @RequestParam long chosetime,  @RequestParam String text,  @RequestParam String periodTime, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = orderRepo.findById(orderid);
                Doctor doctor=doctorRepo.findById(order.getDoctorid());
                order.setChoseTime(chosetime);
                order.setCreatedTime(new Date().getTime());
                order.setTextMessage(text);
                order.setOwnServicess(ownServices);
                order.addOwnService(doctor.getHomePlaceOwn());
                JsonElement obel=new JsonParser().parse(periodTime);
                JsonObject object=obel.getAsJsonObject();
                ScheduleForm scheduleForm=new ScheduleForm(object.get("from").getAsInt(),object.get("to").getAsInt());
                scheduleForm.setEmpty(false);
                order.setPeriodTime(scheduleForm);
                order.setAddress(patient.getHomeAddress());
                order.setAtwork(false);
                order.setStatus("patientcreated");
                orderRepo.save(order);
                Appointment appointment=new Appointment(chosetime,scheduleForm.getFrom(),scheduleForm.getTo(),doctor.getId());
                appointment.setOrder(true);
                appointment.setOrderid(orderid);
                appointmentRepo.save(appointment);
                return new StatusObject("ok");
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    //order atHome
    @RequestMapping(value="/addOrderFile",method = RequestMethod.POST)
    public @ResponseBody Object addOrderFile(@RequestHeader("token") String token, @RequestParam String orderid, @RequestParam String type, @RequestParam String file, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = orderRepo.findById(orderid);

                byte[] byteFile=null;
                if(type.equals("photo")) byteFile=fileUploader.decodeFileFromBase64(file);
                int l=0;
                String url="";
                while(true) {
                    String res;
                    if(type.equals("audio")) {
                        res = fileUploader.uploadText(file, "order" + order.getId() + l);
                        url=res;
                    }
                    else{
                        res=fileUploader.savePhoto(byteFile,"order"+orderid+l+".jpeg");
                        url=res;
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
                if(type.equals("photo")){
                    file=fileUploader.getLhost()+"?url="+file;
                }
               FileObjectForm fileObjectForm=new FileObjectForm(file);
                fileObjectForm.setUrl(url);
                fileObjectForm.setType(type);
                switch (type) {
                    case "audio":
                        order.setAudioMessage(fileObjectForm);
                        break;
                    case "photo":
                        order.setPhotoMessage(fileObjectForm);
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




    @RequestMapping(value = "/addOrderAddress",method = RequestMethod.POST)
    public @ResponseBody Object addOrderAddress(@RequestHeader("token") String token, @RequestParam String orderid, @RequestParam String cityid, @RequestParam String address, HttpServletRequest request){
        try{ // Set Normal cities and addresses
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = orderRepo.findById(orderid);
                order.setAddress(new Address(cityid,address));
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
    @RequestMapping(value="/cancelOrder",method = RequestMethod.POST)
    public @ResponseBody Object cancelOrder(@RequestHeader("token") String token,@RequestParam String orderid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = orderRepo.findById(orderid);
                order.setStatus("patientcancelled");
                orderRepo.save(order);
                Appointment appointment=appointmentRepo.findByOrderid(orderid);
                if(appointment!=null){
                    appointmentRepo.delete(appointment);
                }
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
    public @ResponseBody Object finishOrder(@RequestHeader("token") String token, @RequestParam String orderid,@RequestParam String comment,@RequestParam boolean impression,@RequestParam  int num, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                Order order=orderRepo.findById(orderid);
                Doctor doctor=doctorRepo.findById(order.getDoctorid());
                order.setStatus("patientfinished");
                Comment comment1=new Comment(order.getPatientid(),comment);
                comment1.setId(orderid);
                comment1.setImpression(impression);
                comment1.setRate(num);
                if(comment.length()>0) // comment won't save if text's length is 0
                doctor.addComment(comment1);
                if(num>=0) //Rate won't set if num is -1 or less
                doctor.setRate(new Rate(order.getPatientid(),num));
                doctorRepo.save(doctor);
                orderRepo.save(order);
                Appointment appointment=appointmentRepo.findByOrderid(orderid);
                if(appointment!=null){
                    appointmentRepo.delete(appointment);
                }
                return new StatusObject("ok");
            }
            return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/getMyOrders",method = RequestMethod.POST)
    public @ResponseBody Object getMyOrders(@RequestHeader("token") String token,HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Client client=clientRepo.findById(tok.getClientid());
                client.onReqested();
                clientRepo.save(client);
                /*
                ArrayList<Object> photos=new ArrayList<>();
                if(client.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                    fileObjectForm.setId(((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1)).getId());
                    photos.add(fileObjectForm);
                }
                client.setPhotourl(photos);*/
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                return orderRepo.findByPatientid(id);
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
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
                /*
                ArrayList<Object> photos=new ArrayList<>();
                if(clientd.getPhotourl().size()>0){
                    FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) clientd.getPhotourl().get(clientd.getPhotourl().size()-1));
                    fileObjectForm.setId(((FileObjectForm) clientd.getPhotourl().get(clientd.getPhotourl().size()-1)).getId());
                    photos.add(fileObjectForm);
                }
                clientd.setPhotourl(photos);
*//*
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

                Patient patient=patientRepo.findById(order.getPatientid());
                Client clientp  =clientRepo.findById(patient.getClientid());

                ArrayList<Object> services1 = new ArrayList<>();
                for (Object j : order.getServices()) {
                    services1.add(serviceRepo.findById(j+""));
                }
                order.setServices(services1);

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

                List<Object> services=new ArrayList<>();
                for(Object i: order.getOwnServices()){
                    for (OwnService jj : doctor.getOwns()) {
                        logger.info(jj.getId().length() + " " + (i + "").length() + "   " + (i + "").equals(jj.getId() + ""));
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
            if(tok!=null){
                Client clientqq=clientRepo.findById(tok.getClientid());
                clientqq.onReqested();
                clientRepo.save(clientqq);
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                ArrayList<Order> orders=orderRepo.findByPatientid(id);
                ArrayList<OrderListForm> orderListForms=new ArrayList<>();
                for(Order i:orders) {
                    Doctor doctor = doctorRepo.findById(i.getDoctorid());
                    if (doctor == null) {
                       continue;
                    }
                    else {
                        Client client = clientRepo.findById(doctor.getClientid());/*
                        ArrayList<Object> photos=new ArrayList<>();
                        if(client.getPhotourl().size()>0){
                            FileObjectForm fileObjectForm=fileUploader.changeToFile((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1));
                            fileObjectForm.setId(((FileObjectForm) client.getPhotourl().get(client.getPhotourl().size()-1)).getId());
                            photos.add(fileObjectForm);
                        }
                        client.setPhotourl(photos);*/
                    ServiceType serviceType = serviceTypeRepo.findById(doctor.getServicetypeid() + "");
                    ArrayList<Service> services = new ArrayList<>();
                    for (Object j : i.getServices()) {
                            services.add(serviceRepo.findById(j+""));
                    }
                    ArrayList<EducationForm> educationForms = new ArrayList<>();
                    for (Education j : doctor.getEducations()) {
                        educationForms.add(new EducationForm(j, educationTypeRepo.findById(j.getEd_type_id() + "")));
                    }
                    ArrayList<OwnService> ownServices = new ArrayList<>();
                    for (Object j : i.getOwnServices()) {
                        for (OwnService jj : doctor.getOwns()) {
                            logger.info(jj.getId().length() + " " + (j + "").length() + "   " + (j + "").equals(jj.getId() + ""));
                            if ((j + "").equals(jj.getId())) {
                                ownServices.add(jj);
                                break;
                            }

                        }
                    }
                    List<CommentForm> forms=new ArrayList<>();
                    for(Comment j:doctor.getComments()){
                        if(j.getId().equals(i.getId())){
                            forms.add(new CommentForm(patient,clientqq,j));
                        }
                    }
                    OrderListForm form=new OrderListForm(i, doctor, client, serviceType, services, ownServices);
                    if(forms.size()>0) form.setCommentForms(forms.get(0));
                    List<Order> orderList=orderRepo.findByDoctoridAndAtwork(doctor.getId(),false);
                    List<Order> orderList1=orderRepo.findByDoctoridAndAtwork(doctor.getId(),true);
                    form.setCalls(orderList.size());
                    form.setCommings(orderList1.size());
                    form.setAtwork(i.isAtwork());
                    orderListForms.add(form);
                }
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
    //ORDERS
}