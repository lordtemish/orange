package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Form.FileObjectForm;
import com.dynamica.orange.Form.FireBaseForm;
import com.dynamica.orange.Form.NotificationForm;
import com.dynamica.orange.Repo.*;
import com.dynamica.orange.Service.OrangeService;
import com.google.gson.Gson;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

/**
 * Created by lordtemich on 10/27/17.
 */
@Controller
@RequestMapping(value= {"/"})
public class MainController {
    private Logger logger = LoggerFactory.getLogger(MainController.class);
    FileUploader fileUploader=new FileUploader();

    private Mailing mailing=new Mailing();
    private OrangeService service;
    @Autowired
    AppointmentRepo appointmentRepo;
    @Autowired
    TokenRepo tokenRepo;
    @Autowired
    CityRepo cityRepo;
    @Autowired
    OwnTimeTypeRepo ownTimeTypeRepo;
    @Autowired
    BloodRepo bloodRepo;
    @Autowired
    ServiceTypeRepo sertypeRepo;
    @Autowired
    MapRepo mapRepo;
    @Autowired
    EducationTypeRepo edTypeRepo;
    @Autowired
    ServiceRepo serviceRepo;
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

    @Autowired
    public void setProductService(OrangeService service) {
        this.service = service;
    }



    @RequestMapping(value="/",method = RequestMethod.GET)
    public @ResponseBody String defaults(){
        return "index";
    }
    @RequestMapping(value = "/testOrangeQueues",method = RequestMethod.POST)
    public @ResponseBody String def(/*@RequestParam String mes*/ @RequestParam List<String> files){
        return "";
    }


    @RequestMapping(value="/getAdminToken",method = RequestMethod.POST)
    public @ResponseBody Object createNewAdminToken(@RequestParam String password){
        if(password.equals("orangeadmin")){
            Token token=new Token();
            token.setAdmin(true);
            tokenRepo.save(token);
            return new TokenStatus("ok",token.getId());
        }
        else{
            return new StatusObject("no");
        }
    }
    @RequestMapping(value = {"/deleteAll"},method = RequestMethod.POST)
    public @ResponseBody Object index(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok=tokenRepo.findById(token);
        try {
            if (tok != null && tok.isAdmin()) {
                cityRepo.deleteAll();
                ownTimeTypeRepo.deleteAll();
                bloodRepo.deleteAll();
                sertypeRepo.deleteAll();
                serviceRepo.deleteAll();
                mapRepo.deleteAll();
                edTypeRepo.deleteAll();
                clientRepo.deleteAll();
                patientRepo.deleteAll();
                doctorRepo.deleteAll();
                chatRepo.deleteAll();
                orderRepo.deleteAll();
                tokenRepo.deleteAll();
                return new StatusObject("ok");
            }
            return new StatusObject("noauth");
        }
        catch (Exception e){
            return new StatusObject("exception");
        }
    }

    @RequestMapping(value = {"/allcity"},method = RequestMethod.POST)
    public @ResponseBody Object getCities(HttpServletRequest request){
        return cityRepo.findAll();
    }

        @RequestMapping(value = {"/cityadd"},method = RequestMethod.POST)
        public @ResponseBody Object addCity(@RequestHeader("token") String token,@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                if(tok.isAdmin()){
                City city = new City(rus, kaz);
                cityRepo.save(city);
                return new StatusObject("ok");}
            }
                return new StatusObject("noauth");

        }
        @RequestMapping(value = {"/deletecities"},method = RequestMethod.POST)
        public @ResponseBody Object deleteCities(@RequestHeader("token") String token,HttpServletRequest request){
            Token tok= tokenRepo.findById(token);
            if(tok!=null) {
                if (tok.isAdmin()) {
                    cityRepo.deleteAll();
                    return new StatusObject("ok");
                }
            }
            return new StatusObject("noauth");
        }

        @RequestMapping(value = {"/deletecity/{id}"},method = RequestMethod.POST)
        public @ResponseBody Object deleteCity(@RequestHeader("token") String token,@PathVariable("id") String id, HttpServletRequest request) throws NullPointerException{
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                if(tok.isAdmin()) {
                    City city = cityRepo.findById(id);
                    cityRepo.delete(city);
                }
                return new StatusObject("ok");}
            return new StatusObject("noauth");
        }

    @RequestMapping(value = {"/allott"},method = RequestMethod.POST)
    public @ResponseBody Object getOtts(HttpServletRequest request){
        return ownTimeTypeRepo.findAll();
    }
        @RequestMapping(value={"/ottAdd"}, method = RequestMethod.POST)
        public @ResponseBody Object addOtt(@RequestHeader("token") String token,@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
                if(tok.isAdmin()) {
            OwnTimeType ott=new OwnTimeType(rus,kaz);
            ownTimeTypeRepo.save(ott);
                return new StatusObject("ok");}}
            return new StatusObject("noauth");
        }
        @RequestMapping(value = {"/deleteotts"},method = RequestMethod.POST)
        public @ResponseBody Object deleteOtts(@RequestHeader("token") String token,HttpServletRequest request){
            Token tok= tokenRepo.findById(token);
            if(tok!=null) {
                if (tok.isAdmin()) {
                    ownTimeTypeRepo.deleteAll();
                    return new StatusObject("ok");
                }
            }
            return new StatusObject("noauth");
        }
        @RequestMapping(value = {"/deleteott/{id}"},method = RequestMethod.POST)
        public @ResponseBody Object deleteOtt(@RequestHeader("token") String token,@PathVariable("id") String id, HttpServletRequest request){
            Token tok= tokenRepo.findById(token);
            if(tok!=null) {
                if (tok.isAdmin()) {
                    OwnTimeType city = ownTimeTypeRepo.findById(id);
                    ownTimeTypeRepo.delete(city);
                    return new StatusObject("ok");
                }
            }
            return new StatusObject("noauth");
        }


    @RequestMapping(value = {"/allblood"},method = RequestMethod.POST)
    public @ResponseBody Object getBlood(HttpServletRequest request){
        return bloodRepo.findAll();

    }
        @RequestMapping(value={"/bloodAdd"}, method = RequestMethod.POST)
        public @ResponseBody Object addBlood(@RequestHeader("token") String token,@RequestParam String name, HttpServletRequest request){
            Token tok= tokenRepo.findById(token);
            if(tok!=null) {
                if (tok.isAdmin()) {
                    Blood blood = new Blood(name);
                    bloodRepo.save(blood);
                    return new StatusObject("ok");
                }
            }
            return new StatusObject("noauth");
        }
        @RequestMapping(value = {"/deletebloods"},method = RequestMethod.POST)
        public @ResponseBody Object deleteBloods(@RequestHeader("token") String token,HttpServletRequest request){
            Token tok= tokenRepo.findById(token);
            if(tok!=null) {
                if (tok.isAdmin()) {
                    bloodRepo.deleteAll();
                    return new StatusObject("ok");
                }
            }
            return new StatusObject("noauth");
        }
        @RequestMapping(value = {"/deleteblood/{id}"},method = RequestMethod.POST)
        public @ResponseBody Object deleteBlood(@RequestHeader("token") String token,@PathVariable("id") String id, HttpServletRequest request){
            Token tok= tokenRepo.findById(token);
            if(tok!=null) {
                if (tok.isAdmin()) {
                    Blood city = bloodRepo.findById(id);
                    bloodRepo.delete(city);
                    return new StatusObject("ok");
                }
            }
            return new StatusObject("noauth");
        }

    @RequestMapping(value = {"/allsertype"}, method = RequestMethod.POST)
    public
    @ResponseBody
    Object getSerType(HttpServletRequest request) {
        return sertypeRepo.findAll() ;
    }

        @RequestMapping(value = {"/sertypeAdd"}, method = RequestMethod.POST)
        public @ResponseBody Object addSerType(@RequestHeader("token") String token,@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request) {
            Token tok= tokenRepo.findById(token);
            if(tok!=null) {
                if (tok.isAdmin()) {
                    ServiceType serviceType = new ServiceType(rus, kaz);
                    sertypeRepo.save(serviceType);
                    return new StatusObject("ok");
                }
            }
            return new StatusObject("noauth");
        }

        @RequestMapping(value = {"/deletesertypes"}, method = RequestMethod.POST)
        public @ResponseBody Object deleteSerTypes(@RequestHeader("token") String token,HttpServletRequest request) {
            Token tok= tokenRepo.findById(token);
            if(tok!=null) {
                if (tok.isAdmin()) {
                    sertypeRepo.deleteAll();
                    return new StatusObject("ok");
                }
            }
            return new StatusObject("noauth");
        }
        @RequestMapping(value = {"/deletesertype/{id}"},method = RequestMethod.POST)
        public @ResponseBody Object deleteSerType(@RequestHeader("token") String token,@PathVariable("id") String id, HttpServletRequest request){
            Token tok= tokenRepo.findById(token);
            if(tok!=null) {
                if (tok.isAdmin()) {
                    ServiceType city = sertypeRepo.findById(id);
                    sertypeRepo.delete(city);
                    return new StatusObject("ok");
                }
            }
            return new StatusObject("noauth");
        }


    @RequestMapping(value={"/allmaps"}, method = RequestMethod.POST)
    public @ResponseBody  Object allMaps(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){return mapRepo.findAll();}
        else{         return new StatusObject("noauth");}
    }

        @RequestMapping(value={"/deletemaps"}, method = RequestMethod.POST)
        public @ResponseBody Object deleteMaps(@RequestHeader("token") String token,HttpServletRequest request){
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
            mapRepo.deleteAll(); return new StatusObject("ok");}
            return new StatusObject("noauth");
        }
        @RequestMapping(value = {"/deletemap/{id}"},method = RequestMethod.POST)
        public @ResponseBody Object deleteMap(@RequestHeader("token") String token,@PathVariable("id") String id, HttpServletRequest request){
            Token tok= tokenRepo.findById(token);
            if(tok!=null){
            Map city=mapRepo.findById(id);
            mapRepo.delete(city); return new StatusObject("ok");}
            return new StatusObject("noauth");
        }



    @RequestMapping(value = {"/addEdType"},method = RequestMethod.POST)
    public @ResponseBody Object addEdType(@RequestHeader("token") String token,@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                edTypeRepo.save(new EducationType(rus, kaz));
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/allEdTypes"}, method = RequestMethod.POST)
    public @ResponseBody Object allEdTypes(HttpServletRequest request){

        return edTypeRepo.findAll();

    }
    @RequestMapping(value={"/deleteCertificates"}, method = RequestMethod.POST)
    public @ResponseBody Object deleteCertificates(@RequestHeader("token") String token,HttpServletRequest request) {
        Token tok = tokenRepo.findById(token);
        if (tok != null) {
            if (tok.isAdmin()) {
                try {
                    List<Doctor> doctors=doctorRepo.findAll();
                    for(Doctor doctor:doctors){
                        ArrayList<Object> list=new ArrayList<>();
                        for(Object io:doctor.getCertificates()){
                            if(io.getClass().equals(FileObjectForm.class)){
                                list.add(io);
                            }
                        }
                        doctor.setCertificates(list);
                        doctorRepo.save(doctor);
                    }
                    return new StatusObject("ok");

                } catch (Exception e) {
                    return new StatusObject("exception");
                }
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value={"/deleteWrongFiles"}, method = RequestMethod.POST)
    public @ResponseBody Object deleteWrongFiles(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                try {
                    List<Client> clients = clientRepo.findAll();
                    for (Client client : clients) {
                        ArrayList<Object> photos = client.getPhotourl();
                        ArrayList<Object> nesw = new ArrayList<>();
                        for (int i = 0; i < photos.size(); i++) {
                            if (photos.get(i).getClass().equals(FileObjectForm.class)) {
                                nesw.add(photos.get(i));
                            } else {

                            }
                        }
                        client.setPhotourl(nesw);
                        clientRepo.save(client);
                    }
                    return "";
                } catch (Exception e) {
                    return new StatusObject("exception");
                }
            }
            else{
                return new StatusObject("noauth");
            }
        }
        else{
            return  new StatusObject("notoken");
        }
    }
    @RequestMapping(value={"/deleteEdTypes"}, method = RequestMethod.POST)
    public @ResponseBody Object deleteEdTypes(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                edTypeRepo.deleteAll();
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/deleteEdType/{id}"},method = RequestMethod.POST)
    public @ResponseBody Object deleteEdType(@RequestHeader("token") String token,@PathVariable("id") String id, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                EducationType city = edTypeRepo.findById(id);
                edTypeRepo.delete(city);
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }

    @RequestMapping(value = {"/firebaseNote"},method = RequestMethod.POST)
    public @ResponseBody Object fireBaseNote(@RequestParam String token, @RequestParam String info)
    {
        final String uri = "https://fcm.googleapis.com/fcm/send";
        try {
            String key="key=AAAAnSAD-bA:APA91bELzDMD46mf5OeGTl4uGenkxW2XoRFayqkoW5Sg23uWcef6D3nM24OIQnaxGeUyvL1pTQPpcW-rMI0lrbVHpoJwbSFiteU_d0UQdNPw_2UEfpbANphaCsZOBfn4FxaP3DcDwsOM";
           HttpResponse<JsonNode> future = Unirest.post(uri)
                    .header("Authorization", key)
                    .header("Content-Type", "application/json")
                    .body(info)
                    .asJson();
           return future.getBody();
        }
        catch (Exception e){

        }
        return "";
    }

    @RequestMapping(value = {"/addService/{id}"},method = RequestMethod.POST)
    public @ResponseBody Object addService(@RequestHeader("token") String token,@PathVariable("id") String id,@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                serviceRepo.save(new Service(id, rus, kaz));
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/allServices"}, method = RequestMethod.POST)
    public @ResponseBody Object allServices(HttpServletRequest request){

        return serviceRepo.findAll();
    }
    @RequestMapping(value = {"/allApps"}, method = RequestMethod.POST)
    public @ResponseBody List<Appointment> allApps(HttpServletRequest request){

        return appointmentRepo.findAll();
    }
    @RequestMapping(value={"/allServicesById"}, method=RequestMethod.POST)
    public @ResponseBody Object allServicesById(@RequestParam String id, HttpServletRequest request){

        return serviceRepo.findByServtypeid(id);

    }
    @RequestMapping(value={"/deleteServices"}, method = RequestMethod.POST)
    public @ResponseBody Object deleteServices(@RequestHeader("token") String token,HttpServletRequest request) {
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                serviceRepo.deleteAll();
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = {"/deleteService/{id}"},method = RequestMethod.POST)
    public @ResponseBody Object deleteService(@RequestHeader("token") String token,@PathVariable("id") String id, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                Service city = serviceRepo.findById(id);
                serviceRepo.delete(city);
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value={"/changePassword"},method = RequestMethod.POST)
    public @ResponseBody Object changePas(@RequestHeader("token"
    ) String token, @RequestParam String nPassword, @RequestParam String oPassword, HttpServletRequest request){
        try{
            Token tok= tokenRepo.findById(token);
        if(tok!=null) {
                Client client = clientRepo.findById(tok.getClientid());
                if (client.getPassword().equals(hashPass(oPassword))) {
                    client.setPassword(hashPass(nPassword));
                    clientRepo.save(client);
                    return new StatusObject("ok");}
            }return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
        return new StatusObject("exception");

        }
    }
    @RequestMapping(value = {"/addClientbyAdmin"},method = RequestMethod.POST)
    public @ResponseBody Object addClasdadient(@RequestHeader("token"
    ) String token,@RequestParam String email,@RequestParam String password,  HttpServletRequest request) throws NoSuchAlgorithmException {
        Token tok=tokenRepo.findById(token);
        if(tok!=null) {
            if(tok.isAdmin()) {
                Client client1 = clientRepo.findByEmail(email.toLowerCase());
                if (client1 == null) {
                    Client client = new Client(email);

                    password = hashPass(password);
                    client.setPassword(password);
                    client.setAccesscode("777");

                    clientRepo.save(client);
                    request.getSession().setAttribute("auth", client.getId());

                    return new StatusObject("ok");
                } else return new StatusObject("noclient");
            }
            return new StatusObject("notadmin");
        }
        else{
             return new StatusObject("noauth");
        }
    }
    @RequestMapping(value = {"/addClient"},method = RequestMethod.POST)
    public @ResponseBody Object addClient(@RequestParam String email,@RequestParam String password,  HttpServletRequest request) throws NoSuchAlgorithmException {
        Client client1=clientRepo.findByEmail(email.toLowerCase());
        if(client1==null) {
            Client client = new Client(email.toLowerCase());
            password=hashPass(password);
            client.setPassword(password);
            Random ra = new Random();
            int num1 = ra.nextInt(1000);
            String num=num1+"";
            if(num.length()<3){
                num+="0"+num;
                }
                if(num.length()<2){
                num+="0"+num;
                }
            client.setAccesscode(num + "");
            try {
                mailing.Send("orangesuppkz@gmail.com", "orange12345", email.toLowerCase(), "Orange authentication", "Your Access Code: " + num);
            } catch (Exception e) {
                e.printStackTrace();
            return new StatusObject("MailSendException");
            }
            clientRepo.save(client);
            request.getSession().setAttribute("auth", client.getId());

            return new StatusObject("ok");
        }
        else{
            if(!client1.isActivated()){
                resend(email,password);
                return new StatusObject("ok");
            }
            return new StatusObject("noclient");
        }
    }
    public String hashPass(String s) throws NoSuchAlgorithmException{
        String myHash = DigestUtils.md2Hex(s);
        return myHash;
    }
    public Object resend(String email, String password) {
        try {
            Client client = clientRepo.findByEmail(email.toLowerCase());

                Random ra = new Random();
                int num = ra.nextInt(1000);
                client.setAccesscode(num + "");
                password = hashPass(password);
                client.setPassword(password);
                mailing.Send("orangesuppkz@gmail.com", "orange12345", email, "Orange authentication", "Your Access Code: " + num);
                clientRepo.save(client);
                return new StatusObject("ok");
            }
        catch (NullPointerException e) {
            e.printStackTrace();
            return new StatusObject("null");
        }
         catch (Exception e) {
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value="/resend",method = RequestMethod.POST)
    public @ResponseBody Object resend(@RequestParam String email, HttpServletRequest request){
        try{
            Client client = clientRepo.findByEmail(email.toLowerCase());
            if (client != null) {

                Random ra = new Random();
                int num = ra.nextInt(1000);
                client.setAccesscode(num + "");
                mailing.Send("orangesuppkz@gmail.com", "orange12345", email, "Orange authentication", "Your Access Code: " + num);
                clientRepo.save(client);
                request.getSession().setAttribute("auth", client.getId());
                return new StatusObject("ok");}
            return new StatusObject("noclient");
        }
        catch (Exception e){
            e.printStackTrace();
        return new StatusObject("exception");
        }
    }

    @RequestMapping(value = "/checkCode", method = RequestMethod.POST)
    public @ResponseBody Object checkCode(@RequestParam String email, @RequestParam String code, HttpServletRequest request){
        try{
            Client client=clientRepo.findByEmail(email.toLowerCase());
            Token token=null;
            String ip="";
            if (request != null) {
                ip = request.getHeader("X-FORWARDED-FOR");
                if (ip == null || "".equals(ip)) {
                    ip = request.getRemoteAddr();
                }
            }
            logger.info(client.getAccesscode());
            if(client.getAccesscode().equals(code)){
                client.setActivated(true);
                token=new Token(client.getId(), ip);
                tokenRepo.save(token);
            }
            else{
                client.setActivated(false);
            }
            clientRepo.save(client);
            if(client.isActivated()){
                return new TokenStatus("activated",token.getId());
            }
            else{
            return new StatusObject("notactivated");
            }
        }
        catch (Exception e){
            e.printStackTrace();return new StatusObject("exception");
        }
    }
    @RequestMapping(value = {"/deleteClient"},method = RequestMethod.POST)
    public @ResponseBody Object deleteClient(@RequestHeader("token") String token,@RequestParam String id, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        try {
            if (tok != null) {
                if (tok.isAdmin()) {
                    Client client = clientRepo.findById(id);
                    clientRepo.delete(client);
                    try {
                        Doctor doctor = doctorRepo.findByClientid(client.getId());
                        doctor.getId();
                        doctorRepo.delete(doctor);
                    } catch (NullPointerException e) {
                        Patient patient = patientRepo.findByClientid(client.getId());
                        patient.getId();
                        patientRepo.delete(patient);
                    }
                    return new StatusObject("ok");
                }
            }
            return new StatusObject("noauth");
        }
        catch (Exception e){
            e.printStackTrace();
            return new StatusObject("exception");
        }
    }
    @RequestMapping(value = {"/deleteClients"},method = RequestMethod.POST)
    public @ResponseBody Object deleteClients(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                clientRepo.deleteAll();
                patientRepo.deleteAll();
                doctorRepo.deleteAll();
                tokenRepo.deleteAll();
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value={"/allClients"}, method = RequestMethod.POST)
    public @ResponseBody Object allClients(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                return clientRepo.findAll();
            }
        }
        return new StatusObject("noauth");
    }

    @RequestMapping(value={"/allTokens"}, method = RequestMethod.POST)
    public @ResponseBody Object allTokens(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                return tokenRepo.findAll();
            }
        }

            return new StatusObject("noauth");
    }
    @RequestMapping(value={"/allPatients"}, method = RequestMethod.POST)
    public @ResponseBody Object allPatients(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                return patientRepo.findAll();
            }
        }

            return new StatusObject("noauth");
    }
    @RequestMapping(value={"/allDoctors"}, method = RequestMethod.POST)
    public @ResponseBody Object allDoctors(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                return doctorRepo.findAll();
            }
        }

            return new StatusObject("noauth");
    }
    @RequestMapping(value={"/deleteAllDoctors"}, method= RequestMethod.POST)
    public @ResponseBody Object deleteAllDoctors(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                doctorRepo.deleteAll();
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value={"/deleteDoctor/{id}"}, method= RequestMethod.POST)
    public @ResponseBody Object deleteDoctor(@RequestHeader("token") String token,@PathVariable("id") String id, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                Doctor patient=doctorRepo.findById(id);
                doctorRepo.delete(patient);
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value={"/deleteAllPatients"}, method= RequestMethod.POST)
    public @ResponseBody Object deleteAllPatients(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                patientRepo.deleteAll();
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value={"/deletePatient/{id}"}, method= RequestMethod.POST)
    public @ResponseBody Object deletePatient(@RequestHeader("token") String token,@PathVariable("id") String id, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                Patient patient=patientRepo.findById(id);
                patientRepo.delete(patient);
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = "/getFile/",method = RequestMethod.POST)
    public StreamingResponseBody getFile(@RequestParam String url, HttpServletResponse response, HttpServletRequest request) throws IOException{
        try{

            File file=new File(url);

            response.setContentType("text/html;charset="+Files.probeContentType(file.toPath()));
            response.setHeader("Content-Disposition", "attachment; filename=\""+file.getName()+"\"");
            InputStream inputStream = new FileInputStream(file);
            logger.info(file.getName()+"    "+url+"   "+Files.probeContentType(file.toPath()));
            return outputStream -> {
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    outputStream.write(data, 0, nRead);
                }
                inputStream.close();
            };
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping(value="/addFile", method = RequestMethod.POST)
    public @ResponseBody Object fileadd(@RequestHeader("token") String token,@RequestParam String file, @RequestParam String name, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                int l=0;
                while(true) {
                    String res=fileUploader.uploadText(file, name+l);
                    if(!res.equals("")){
                        return new FileObjectForm(res);
                    }
                    else if(res.equals("exception")){
                        break;
                    }
                    else{
                        l++;
                    }
                }
            }
        }

        return new StatusObject("noauth");
    }
    @RequestMapping(value = "/getFileEncoded", method = RequestMethod.POST)
    public   @ResponseBody Object getFIleEncoded(@RequestParam String url,HttpServletResponse response, HttpServletRequest request) throws IOException{
        String s = fileUploader.getFileText(url);
        if(s.equals("")){
            return new StatusObject("nofile");
        }
        FileObjectForm fileObjectForm=new FileObjectForm(s);
        return fileObjectForm;
    }
    @RequestMapping(value = "/getFileasBase", method = RequestMethod.POST)
    public   @ResponseBody Object getImage(@RequestParam String url,HttpServletResponse response, HttpServletRequest request) throws IOException{

        return new TextObject(fileUploader.encodeFileToBase64Binary(url));
        /*
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(image.length);
        return new HttpEntity<byte[]>(image, headers);
        */
    }


    @RequestMapping(value="/getAllChats", method = RequestMethod.POST)
    public @ResponseBody Object getAllChat(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                return chatRepo.findAll();
            }
        }

            return new StatusObject("noauth");
    }
    @RequestMapping(value="/deleteAllChats", method = RequestMethod.POST)
    public Object deleteAllChat(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                chatRepo.deleteAll();
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }

    @RequestMapping(value = "/Image/", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(@RequestParam String url) throws IOException {
        File file=new File(url);
        byte[] image =Files.readAllBytes(file.toPath());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
    @RequestMapping(value="/getAllOrders",method = RequestMethod.POST)
    public @ResponseBody Object getAllOrders(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                return orderRepo.findAll();
            }
        }

            return new StatusObject("noauth");
    }
    @RequestMapping(value="/deleteAllOrders",method=RequestMethod.POST)
    public @ResponseBody Object deleteAllOrders(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                orderRepo.deleteAll();
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value="/deleteOrderById",method=RequestMethod.POST)
    public @ResponseBody Object deleteOrderById(@RequestHeader("token") String token,@RequestParam String id, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                Order order=orderRepo.findById(id);
                orderRepo.delete(order);
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value="/deleteOrdersWithoutServices",   method=RequestMethod.POST)
    public @ResponseBody Object deleteOrdersById(@RequestHeader("token") String token, HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                List<Order> orders=orderRepo.findAll();
                for(Order i:orders){
                    if(i.getServices().size()==0){
                        orderRepo.delete(i);
                    }
                }
                return new StatusObject("ok");
            }
        }
        return new StatusObject("noauth");
    }
    @RequestMapping(value = "/authClient", method = RequestMethod.POST)
    public @ResponseBody Object authDoctor(@RequestParam String email,@RequestParam String password,@RequestParam String deviceToken, HttpServletRequest request) throws NoSuchAlgorithmException{
        try {
            Client client = clientRepo.findByEmail(email.toLowerCase());
            String ip="";
            if (request != null) {
                ip = request.getHeader("X-FORWARDED-FOR");
                if (ip == null || "".equals(ip)) {
                    ip = request.getRemoteAddr();
                }
            }
            Token token = new Token(client.getId(), ip);
            token.setIosToken(deviceToken);
            tokenRepo.save(token);
            logger.info(password);
            password = hashPass(password);
            logger.info((client.getPassword().equals(password) && client.isActivated()) + "");
            for(Token token1:tokenRepo.findByClientid(client.getId())){
                if(!token1.getId().equals(token.getId())){
                    tokenRepo.delete(token1);
                }
            }
            if (client.getPassword().equals(password) && client.isActivated()) {
                try {
                    Doctor doctor = doctorRepo.findByClientid(client.getId());

                    if (doctor != null)
                        return new TokenStatus("doctor", token.getId());
                    else return new TokenStatus("patient", token.getId());
                } catch (NullPointerException e) {
                    Patient patient = patientRepo.findByClientid(client.getId());
                    if (patient != null) {
                        return new TokenStatus("patient", token.getId());
                    }
                }
            } else {
                return new StatusObject("noclient");
            }
            return new StatusObject("noclient");
        }
        catch (NullPointerException e){
            return new StatusObject("null");
        }
    }
    @RequestMapping(value="/isAuth",method = RequestMethod.POST)
    public @ResponseBody Object isAuth(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            return new StatusObject(true);}
        return new StatusObject("noauth");
    }

    @RequestMapping(value = "/testFireBase",method = RequestMethod.POST)
    public @ResponseBody Object testFire(@RequestHeader("token") String token){
        final String uri = "http://localhost:8050/firebaseNote";
        Token tok=tokenRepo.findById(token);
        String to=tok.getIosToken();
        FireBaseForm fireBaseForm=new FireBaseForm(to,new Doctor(),new NotificationForm("Welcome to ORANGE","ТЕстим сам фаирбэйз на работу","default"));
        Future<HttpResponse<JsonNode>> future = Unirest.post(uri)
                .header("accept", "application/json")
                .field("token", token)
                .field("info", new Gson().toJson(fireBaseForm))
                .asJsonAsync(new Callback<JsonNode>() {

                    public void failed(UnirestException e) {
                        System.out.println("The request has failed");
                    }

                    public void completed(HttpResponse<JsonNode> response) {
                        int code = response.getStatus();
                       Headers headers = response.getHeaders();
                        JsonNode body = response.getBody();
                        InputStream rawBody = response.getRawBody();
                    }

                    public void cancelled() {
                        System.out.println("The request has been cancelled");
                    }

                });
        try{
            HttpResponse<JsonNode> nodes=future.get();
            return nodes.getBody().toString();
        }
        catch (Exception e){


        }
        return new StatusObject("ok");
    }
}
