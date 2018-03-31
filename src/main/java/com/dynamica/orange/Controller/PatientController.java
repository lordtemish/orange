package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Form.*;
import com.dynamica.orange.Repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
    OrderRepo orderRepo;

    FileUploader fileUploader=new FileUploader();
    public static String path="/photo/";

    @RequestMapping(value = {"/addNew"}, method = RequestMethod.POST)
    public @ResponseBody Object addNew(@RequestHeader("token") String token,@RequestParam String name, @RequestParam String surname,   HttpServletRequest request){
           try {
               Token tok= tokenRepo.findById(token);
               if(tok!=null) {
                   Client client = clientRepo.findById(tok.getClientid());
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
    @RequestMapping(value = {"/addinfo"},method = RequestMethod.POST)
        public @ResponseBody Object addInfo(@RequestHeader("token") String token, @RequestParam String gender, @RequestParam String dateofbirth, @RequestParam int weight, @RequestParam int height, @RequestParam String bloodid, @RequestParam String chronic, @RequestParam String alergic, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
        Patient patient=patientRepo.findById(tok.getClientid());
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
               Patient patient = new Patient(tok.getClientid());
               Client client = clientRepo.findById(tok.getClientid());
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
            ClientWithPatientForm clientWithPatientForm = new ClientWithPatientForm(client, patient);
            return clientWithPatientForm;
        }
        else{
            return new StatusObject("noauth");
        }
    }
    @RequestMapping(value="/getPatientProfile", method = RequestMethod.POST)
    public @ResponseBody
    Object getPatientProfile(@RequestHeader("token") String token,@PathVariable("id") String id, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient=patientRepo.findById(id);
                Client client=clientRepo.findById(patient.getClientid());
                City workCity=cityRepo.findById(patient.getWorkAddress().getId());
                City homeCity=cityRepo.findById(patient.getHomeAddress().getId());
                String homeC="";
                String workC="";
                switch (client.getLang()){
                    case "R":
                        homeC=homeCity.getNameRus();
                        workC=workCity.getNameRus();
                        break;
                    default:
                        homeC=homeCity.getNameKaz();
                        workC=workCity.getNameKaz();
                        break;
                }
                String blood=bloodRepo.findById(patient.getBlood()).getName();
                return new PatientProfileForm(patient,client,workC,homeC, blood);
            }
            else{
                return new StatusObject("noauth");
            }
        }
        catch (Exception e){
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = "/getDoctorProfile",method = RequestMethod.POST)
    public @ResponseBody Object getDPF(@RequestHeader("token") String token, @RequestParam String doctorid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Doctor doctor=doctorRepo.findById(doctorid);
                Client client=clientRepo.findById(doctor.getClientid());
                ServiceType serviceType=serviceTypeRepo.findById(doctor.getServicetypeid());
                List<Service> services=new ArrayList<>();
                for(String i:doctor.getServices()){
                    services.add(serviceRepo.findById(i));
                }
                return new DoctorProfileForm(doctor,client,serviceType,services, cityRepo.findById(doctor.getWorkAddress().getCityid()),cityRepo.findById(doctor.getHomeAddress().getCityid()));
            }
            return new StatusObject("noauth");
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
                List<CommentForm> forms=new ArrayList<>();
                Doctor doctor=doctorRepo.findById(doctorid);
                for(Comment i:doctor.getComments()){
                    Patient patient=patientRepo.findById(i.getPatient_id());
                    CommentForm form=new CommentForm(patient,clientRepo.findById(patient.getClientid()),i);
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
    public @ResponseBody Object searchDoctorsByService(@RequestHeader("token") String token, @RequestParam String serviceid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Service service=serviceRepo.findById(serviceid);
                ArrayList<Doctor> doctors=doctorRepo.findByServicetypeid(service.getServtypeid());
                logger.info(doctors.size()+" "+service.getServtypeid());
                ArrayList<DoctorListForm> doctorListForms=new ArrayList<>();
                for(Doctor i:doctors){
                    if(i.getServices().contains(serviceid)){
                        ArrayList<Service> services=new ArrayList<>();
                        for(String j:i.getServices()){
                            services.add(serviceRepo.findById(j));
                        }
                        doctorListForms.add(new DoctorListForm(i,clientRepo.findById(i.getClientid()),serviceTypeRepo.findById(i.getServicetypeid()),services));
                    }
                    else
                        doctors.remove(i);
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
                List<DoctorListForm> doctorListForms=new ArrayList<>();
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                for(String i:patient.getFavs()){
                    Doctor doctor=doctorRepo.findById(i);
                    Client client=clientRepo.findById(doctor.getClientid());
                    ServiceType serviceType=serviceTypeRepo.findById(doctor.getServicetypeid());
                    ArrayList<Service> services=new ArrayList<>();
                    for(String jj:doctor.getServices()){
                        services.add(serviceRepo.findById(jj));
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
    @RequestMapping(value = {"/getMyDoctorsList/"}, method = RequestMethod.POST) //need to make new Form
    public @ResponseBody Object getMyDocsList(@RequestHeader("token") String token, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            List<DoctorListForm> list = new ArrayList<>();
            for (String i : patient.getMydocs()) {
                Doctor doctor=doctorRepo.findById(i);
                Client client=clientRepo.findById(doctor.getClientid());
                ServiceType serviceType=serviceTypeRepo.findById(doctor.getServicetypeid());
                ArrayList<Service> services=new ArrayList<>();
                for(String jj:doctor.getServices()){
                    services.add(serviceRepo.findById(jj));
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
            client.addEmail(mail);
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
            client.addPhone(phone);
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
            clientRepo.save(client);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/addAddress/{name}"}, method = RequestMethod.POST)
    public @ResponseBody Object addAddress(@RequestHeader("token") String token, @PathVariable("name") String name, @RequestParam String cityId, @RequestParam String address, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            switch (name) {
                case "work":
                    patient.setWorkAddress(new Address(cityId,address));
                    break;
                case "home":
                    patient.setHomeAddress(new Address(cityId,address));
                    break;
            }
            patientRepo.save(patient);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/deleteAddress/{name}"}, method = RequestMethod.POST)
    public @ResponseBody Object deleteAdress(@RequestHeader("token") String token, @PathVariable("name") String name, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            switch (name) {
                case "work":
                    patient.deleteWorkAddress();
                    break;
                case "home":
                    patient.deleteWorkAddress();
                    break;
            }
            patientRepo.save(patient);
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/addPhoto"}, method = RequestMethod.POST)
    public @ResponseBody Object addPhoto(@RequestHeader("token") String token, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "file is empty");
                return new StatusObject("emptyfile");
            } else {
                Client client = clientRepo.findById(tok.getClientid());
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                int i = 0;
                String url = "mainphoto-" + patient.getId();
                while (true) {
                    String s = fileUploader.upload(file, url + i);
                    logger.info("FILE: "+s);
                    if ("".equals(s)) {
                        i++;
                        continue;
                    } else {
                        url=s;
                        break;
                    }

                }
                client.addPhoto(url);
                clientRepo.save(client);
            }
            return new StatusObject("ok");
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/deletePhoto"}, method = RequestMethod.POST)
    public @ResponseBody Object delPhoto(@RequestHeader("token") String token, @RequestParam String url, RedirectAttributes redirectAttributes, HttpServletRequest request) {
       Token tok= tokenRepo.findById(token);
            if(tok!=null) {
                Client client = clientRepo.findById(tok.getClientid());
                client.deletePhoto(url);
                clientRepo.save(client);
                fileUploader.deletePhoto(url);
                return new StatusObject("ok");
            }
        return new StatusObject("noauth");
    }
    @RequestMapping(value={"/addFavouriteDoctor"}, method = RequestMethod.POST)
    public @ResponseBody Object addFavDoc(@RequestHeader("token") String token, @RequestParam String doctorid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient = patientRepo.findByClientid(tok.getClientid());
                patient.addFav(doctorid);
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
            Patient patient = patientRepo.findById(tok.getClientid());
            List<Doctor> list = new ArrayList<>();
            for (String i : patient.getFavs()) {
                list.add(doctorRepo.findById(i));
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
                Patient patient = patientRepo.findByClientid(tok.getClientid());
                patient.addDoc(doctorid);
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
    @RequestMapping(value="/deleteMyDoctor", method=RequestMethod.POST)
    public @ResponseBody Object delMyDoc(@RequestHeader("token") String token, @RequestParam String doctorid,HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient = patientRepo.findByClientid(tok.getClientid());
                boolean a = patient.deleteMyDoc(doctorid);
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
    @RequestMapping(value = {"/getMyDoctors"}, method = RequestMethod.POST) //need to make new Form
    public @ResponseBody Object getMyDocs(@RequestHeader("token") String token, HttpServletRequest request){
       Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Patient patient = patientRepo.findByClientid(tok.getClientid());
            List<Doctor> list = new ArrayList<>();
            for (String i : patient.getMydocs()) {
                list.add(doctorRepo.findById(i));
            }
            return list;
        }
        else return new StatusObject("noauth");
    }

    @RequestMapping(value="/openChat", method = RequestMethod.POST)
    public @ResponseBody Object openChat(@RequestHeader("token") String token,  @RequestParam String doctorid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
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
                    chatRepo.save(chat1);
                    chat = chatRepo.findOneByDoctoridAndPatientid(doctorid, id);
                }
                return new ChatIdForm(chat.getId());
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
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                List<Chat> chats = chatRepo.findByPatientid(patient.getId());
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
                Patient doctor = patientRepo.findByClientid(tok.getClientid());
                Chat chat = chatRepo.findById(chatid);
                Message message = new Message(doctor.getClientid(), "text", text);
                chat.addMessage(message);
                chat.setStatus("patient");
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
    @RequestMapping(value="/getMessages/}/{chatid}", method = RequestMethod.POST)
    public @ResponseBody Object getMessages(@RequestHeader("token") String token, @PathVariable("chatid") String chatid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient doctor = patientRepo.findByClientid(tok.getClientid());
                Chat chat = chatRepo.findById(chatid);
                if (!chat.getStatus().equals("patient")) {
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
    @RequestMapping(value="/sendFileMessage/{chatid}",method = RequestMethod.POST)
    public @ResponseBody Object sendFileMes(@RequestHeader("token") String token,@PathVariable("chatid") String chatid, @RequestParam String type, @RequestParam MultipartFile file, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient doctor = patientRepo.findByClientid(tok.getClientid());
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

    @RequestMapping(value="/addOrder",method = RequestMethod.POST)
    public @ResponseBody Object addOrder(@RequestHeader("token") String token, @RequestParam boolean atwork, @RequestParam String doctorid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = new Order(doctorid, id);
                order.setAtwork(atwork);
                orderRepo.save(order);
                return order.getId();
            }
            else return new StatusObject("noauth");
        }
        catch (Exception e){
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/setOrderInfoWorkplace/{orderid}", method=RequestMethod.POST)
    public @ResponseBody Object addOrderInfo(@RequestHeader("token") String token, @PathVariable("orderid") String orderid, @RequestParam String services, @RequestParam long chosetime, @RequestParam String periodTime, @RequestParam String text, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = orderRepo.findById(orderid);
                Doctor doctor=doctorRepo.findById(order.getDoctorid());
                order.addServices(services);
                order.setChoseTime(chosetime);
                order.setPeriodTime(periodTime);
                order.setTextMessage(text);
                order.setAtwork(true);
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
    @RequestMapping(value="/setOrderInfoHome/{orderid}", method=RequestMethod.POST)
    public @ResponseBody Object addOrderInfoHome(@RequestHeader("token") String token, @PathVariable("orderid") String orderid, @RequestParam String services, @RequestParam long chosetime,  @RequestParam String text, @RequestParam double period, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = orderRepo.findById(orderid);
                order.addServices(services);
                order.setChoseTime(chosetime);
                order.setTextMessage(text);
                order.setPeriodinhours(period);
                order.setAddress(patient.getHomeAddress());
                order.setAtwork(false);
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
    //order atHome
    @RequestMapping(value="/addOrderFile/{orderid}",method = RequestMethod.POST)
    public @ResponseBody Object addOrderFile(@RequestHeader("token") String token, @PathVariable("orderid") String orderid, @RequestParam String type, @RequestParam MultipartFile file, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
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
                    case "audio":
                        order.setAudioMessage(url);
                        break;
                    case "photo":
                        order.setPhotoMessage(url);
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
    @RequestMapping(value = "/addOrderAddress/{orderid}",method = RequestMethod.POST)
    public @ResponseBody Object addOrderAddress(@RequestHeader("token") String token, @PathVariable("orderid") String orderid, @RequestParam String cityid, @RequestParam String address, HttpServletRequest request){
        try{ // Set Normal cities and addresses
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
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
    @RequestMapping(value="/cancelOrder/{orderid}",method = RequestMethod.POST)
    public @ResponseBody Object cancelOrder(@RequestHeader("token") String token,@PathVariable("orderid") String orderid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                Order order = orderRepo.findById(orderid);
                order.setStatus("patientcancelled");
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
    public @ResponseBody Object finishOrder(@RequestHeader("token") String token, @PathVariable("orderid") String orderid,@RequestParam String comment,@RequestParam boolean impression,@RequestParam  int num, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Order order=orderRepo.findById(orderid);
                Doctor doctor=doctorRepo.findById(order.getDoctorid());
                order.setStatus("patientfinished");
                Comment comment1=new Comment(order.getPatientid(),comment);
                comment1.setImpression(impression);
                if(comment.length()>0) // comment won't save if text's length is 0
                doctor.addComment(comment1);
                if(num>=0) //Rate won't set if num is -1 or less
                doctor.setRate(new Rate(order.getPatientid(),num));
                doctorRepo.save(doctor);
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
    @RequestMapping(value="/getMyOrders",method = RequestMethod.POST)
    public @ResponseBody Object getMyOrders(@RequestHeader("token") String token,HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
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
    @RequestMapping(value="/getOrderInfo/{orderid}", method = RequestMethod.POST)
    public @ResponseBody Object getOrderInfo(@RequestHeader("token") String token, @PathVariable("orderid") String orderid, HttpServletRequest request){
        try{
           Token tok= tokenRepo.findById(token);
            if(tok!=null){
                Order order=orderRepo.findById(orderid);
                Doctor doctor=doctorRepo.findById(order.getDoctorid());
                Client clientd=clientRepo.findById(doctor.getClientid());
                Patient patient=patientRepo.findById(order.getPatientid());
                Client clientp  =clientRepo.findById(patient.getClientid());
                return new OrderInfoForm(order,doctor,clientd,patient,clientp);
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
                Patient patient=patientRepo.findByClientid(tok.getClientid());
                String id=patient.getId();
                ArrayList<Order> orders=orderRepo.findByPatientid(id);
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
    //ORDERS

}
