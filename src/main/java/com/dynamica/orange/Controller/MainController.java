package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Repo.*;
import com.dynamica.orange.Service.OrangeService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public @ResponseBody String def(@RequestParam String mes){
        service.sendOrangeMessage(mes);
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
    @RequestMapping(value = {"/addClient"},method = RequestMethod.POST)
    public @ResponseBody Object addClient(@RequestParam String email,@RequestParam String password,  HttpServletRequest request) throws NoSuchAlgorithmException {
        Client client1=clientRepo.findByEmail(email);
        if(client1==null) {
            Client client = new Client(email);
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
                mailing.Send("orangesuppkz@gmail.com", "orange12345", email, "Orange authentication", "Your Access Code: " + num);
            } catch (Exception e) {
                e.printStackTrace();
            return new StatusObject("MailSendException");
            }
            clientRepo.save(client);
            request.getSession().setAttribute("auth", client.getId());

            return new StatusObject("ok");
        }
        else return new StatusObject("noclient");
    }
    public String hashPass(String s) throws NoSuchAlgorithmException{
        String myHash = DigestUtils.md2Hex(s);
        return myHash;
    }
    @RequestMapping(value="/resend",method = RequestMethod.POST)
    public @ResponseBody Object resend(@RequestParam String email, HttpServletRequest request){
        try{
            Client client=new Client(email);
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
    public @ResponseBody Object checkCode(@RequestParam String email, @RequestParam String code){
        try{
            Client client=clientRepo.findByEmail(email);
            Token token=null;
            logger.info(client.getAccesscode());
            if(client.getAccesscode().equals(code)){
                client.setActivated(true);
                token=new Token(client.getId());
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

    @RequestMapping(value = {"/deleteClients"},method = RequestMethod.POST)
    public @ResponseBody Object deleteClients(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null) {
            if (tok.isAdmin()) {
                clientRepo.deleteAll();
                patientRepo.deleteAll();
                doctorRepo.deleteAll();
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

    @RequestMapping(value = "/authClient", method = RequestMethod.POST)
    public @ResponseBody Object authDoctor(@RequestParam String email,@RequestParam String password, HttpServletRequest request) throws NoSuchAlgorithmException{
            Client client = clientRepo.findByEmail(email);
            Token token=tokenRepo.findByClientid(client.getId());
            logger.info(password);
            password=hashPass(password);
            logger.info((client.getPassword().equals(password)&& client.isActivated())+"");
            if (client.getPassword().equals(password) && client.isActivated()) {
                try {
                    Doctor doctor = doctorRepo.findByClientid(client.getId());

                    if(doctor!=null)
                        return new TokenStatus("doctor",token.getId());
                    else return  new TokenStatus("patient",token.getId());
                } catch (NullPointerException e) {
                    Patient patient = patientRepo.findByClientid(client.getId());
                    return new TokenStatus("patient",token.getId());
                }
            }
            else {
                return new StatusObject("noclient");
            }

    }
    @RequestMapping(value="/isAuth",method = RequestMethod.POST)
    public @ResponseBody Object isAuth(@RequestHeader("token") String token,HttpServletRequest request){
        Token tok= tokenRepo.findById(token);
        if(tok!=null){
            return new StatusObject(true);}
        return new StatusObject("noauth");
    }

}
