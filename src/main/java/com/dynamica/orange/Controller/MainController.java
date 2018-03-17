package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Repo.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
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
    private Mailing mailing=new Mailing();

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


    @RequestMapping(value="/",method = RequestMethod.GET)
    public @ResponseBody String defaults(){
        return "index";
    }
    @RequestMapping(value = {"/deleteAll"},method = RequestMethod.POST)
    public String index(HttpServletRequest request){
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
        return "index";
    }

    @RequestMapping(value = {"/allcity"},method = RequestMethod.POST)
    public @ResponseBody ArrayList<City> getCities(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        return cityRepo.findAll();
        else
            return null;
    }

        @RequestMapping(value = {"/cityadd"},method = RequestMethod.POST)
        public @ResponseBody boolean addCity(@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null) {
                City city = new City(rus, kaz);
                cityRepo.save(city);
                return true;
            }
            else {
                return false;
            }
        }
        @RequestMapping(value = {"/deletecities"},method = RequestMethod.POST)
        public @ResponseBody boolean deleteCities(HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            cityRepo.deleteAll();
            return true;}
            return false;
        }

        @RequestMapping(value = {"/deletecity/{id}"},method = RequestMethod.POST)
        public @ResponseBody boolean deleteCity(@PathVariable("id") String id, HttpServletRequest request) throws NullPointerException{
            if(request.getSession().getAttribute("auth")!=null){
            City city=cityRepo.findById(id);
            cityRepo.delete(city);
            return true;}
            return false;
        }

    @RequestMapping(value = {"/allott"},method = RequestMethod.POST)
    public @ResponseBody ArrayList<OwnTimeType> getOtts(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){
        return ownTimeTypeRepo.findAll();}
        else{
            return null;
        }
    }
        @RequestMapping(value={"/ottAdd"}, method = RequestMethod.POST)
        public @ResponseBody boolean addOtt(@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            OwnTimeType ott=new OwnTimeType(rus,kaz);
            ownTimeTypeRepo.save(ott);
            return true;}
            return false;
        }
        @RequestMapping(value = {"/deleteotts"},method = RequestMethod.POST)
        public @ResponseBody boolean deleteOtts(HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            ownTimeTypeRepo.deleteAll();
            return true;}
            return false;
        }
        @RequestMapping(value = {"/deleteott/{id}"},method = RequestMethod.POST)
        public @ResponseBody boolean deleteOtt(@PathVariable("id") String id, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            OwnTimeType city=ownTimeTypeRepo.findById(id);
            ownTimeTypeRepo.delete(city);
            return true;}
            return false;
        }


    @RequestMapping(value = {"/allblood"},method = RequestMethod.POST)
    public @ResponseBody ArrayList<Blood> getBlood(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        return bloodRepo.findAll();
        else
            return null;
    }
        @RequestMapping(value={"/bloodAdd"}, method = RequestMethod.POST)
        public @ResponseBody boolean addBlood(@RequestParam String name, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null) {
                Blood blood = new Blood(name);
                bloodRepo.save(blood);
                return true;}
            return false;
        }
        @RequestMapping(value = {"/deletebloods"},method = RequestMethod.POST)
        public @ResponseBody boolean deleteBloods(HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            bloodRepo.deleteAll();
                return true;}
            return false;
        }
        @RequestMapping(value = {"/deleteblood/{id}"},method = RequestMethod.POST)
        public @ResponseBody boolean deleteBlood(@PathVariable("id") String id, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            Blood city=bloodRepo.findById(id);
            bloodRepo.delete(city);return true;}
            return false;
        }

    @RequestMapping(value = {"/allsertype"}, method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<ServiceType> getSerType(HttpServletRequest request) {
        if(request.getSession().getAttribute("auth")!=null)
            return sertypeRepo.findAll();
        else
            return null;
    }

        @RequestMapping(value = {"/sertypeAdd"}, method = RequestMethod.POST)
        public @ResponseBody boolean addSerType(@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request) {
            if(request.getSession().getAttribute("auth")!=null) {
                ServiceType serviceType = new ServiceType(rus, kaz);
                sertypeRepo.save(serviceType);
                return true;}
            return false;
        }

        @RequestMapping(value = {"/deletesertypes"}, method = RequestMethod.POST)
        public @ResponseBody boolean deleteSerTypes(HttpServletRequest request) {
            if(request.getSession().getAttribute("auth")!=null){
            sertypeRepo.deleteAll();return true;}
            return false;
        }
        @RequestMapping(value = {"/deletesertype/{id}"},method = RequestMethod.POST)
        public @ResponseBody boolean deleteSerType(@PathVariable("id") String id, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            ServiceType city=sertypeRepo.findById(id);
            sertypeRepo.delete(city);return true;}
            return false;
        }


    @RequestMapping(value={"/allmaps"}, method = RequestMethod.POST)
    public @ResponseBody  ArrayList<Map> allMaps(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){return mapRepo.findAll();}
        else{return null;}
    }

        @RequestMapping(value={"/deletemaps"}, method = RequestMethod.POST)
        public @ResponseBody boolean deleteMaps(HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            mapRepo.deleteAll();return true;}
            return false;
        }
        @RequestMapping(value = {"/deletemap/{id}"},method = RequestMethod.POST)
        public @ResponseBody boolean deleteMap(@PathVariable("id") String id, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            Map city=mapRepo.findById(id);
            mapRepo.delete(city);return true;}
            return false;
        }



    @RequestMapping(value = {"/addEdType"},method = RequestMethod.POST)
    public @ResponseBody boolean addEdType(@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){
        edTypeRepo.save(new EducationType(rus,kaz));return true;}
        return false;
    }
    @RequestMapping(value = {"/allEdTypes"}, method = RequestMethod.POST)
    public @ResponseBody ArrayList<EducationType> allEdTypes(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        return edTypeRepo.findAll();
        else
            return null;
    }
    @RequestMapping(value={"/deleteEdTypes"}, method = RequestMethod.POST)
    public @ResponseBody boolean deleteEdTypes(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){
            edTypeRepo.deleteAll();return true;}
        return false;
    }
    @RequestMapping(value = {"/deleteEdType/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean deleteEdType(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().    getAttribute("auth")!=null)
        {EducationType city=edTypeRepo.findById(id);
        edTypeRepo.delete(city);return true;}
        return false;
    }



    @RequestMapping(value = {"/addService/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean addService(@PathVariable("id") String id,@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){
        serviceRepo.save(new Service(id, rus, kaz));return true;}
        return false;
    }
    @RequestMapping(value = {"/allServices"}, method = RequestMethod.POST)
    public @ResponseBody ArrayList<Service> allServices(HttpServletRequest request){

        if(request.getSession().getAttribute("auth")!=null){return serviceRepo.findAll();}
        else return null;
    }
    @RequestMapping(value={"/allServicesById/{id}"}, method=RequestMethod.POST)
    public @ResponseBody ArrayList<Service> allServicesById(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        return serviceRepo.findByServtypeid(id);
        else return null;
    }
    @RequestMapping(value={"/deleteServices"}, method = RequestMethod.POST)
    public @ResponseBody boolean deleteServices(HttpServletRequest request) {
        if(request.getSession().getAttribute("auth")!=null){
        serviceRepo.deleteAll();return true;}
            return false;
    }
    @RequestMapping(value = {"/deleteService/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean deleteService(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){
        Service city=serviceRepo.findById(id);
        serviceRepo.delete(city);return true;}
        return false;
    }
    @RequestMapping(value={"/changePassword/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean changePas(@PathVariable("id") String id, @RequestParam String nPassword, @RequestParam String oPassword, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Client client = clientRepo.findById(id);
                if (client.getPassword().equals(oPassword)) {
                    client.setPassword(nPassword);
                    clientRepo.save(client);
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
    @RequestMapping(value = {"/addClient"},method = RequestMethod.POST)
    public @ResponseBody boolean addClient(@RequestParam String email,@RequestParam String password,  HttpServletRequest request) {
        Client client1=clientRepo.findByEmail(email);
        if(client1==null) {
            Client client = new Client(email);
            client.setPassword(password);
            Random ra = new Random();
            int num = ra.nextInt(1000);
            client.setAccesscode(num + "");
            try {
                mailing.Send("orangesuppkz@gmail.com", "orange12345", email, "Orange authentication", "Your Access Code: " + num);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            clientRepo.save(client);
            request.getSession().setAttribute("auth", client.getId());

            return true;
        }
        else return false;
    }
    @RequestMapping(value="/resend",method = RequestMethod.POST)
    public @ResponseBody boolean resend(@RequestParam String email, HttpServletRequest request){
        try{
            Client client=new Client(email);
            if (client != null) {

                Random ra = new Random();
                int num = ra.nextInt(1000);
                client.setAccesscode(num + "");
                mailing.Send("orangesuppkz@gmail.com", "orange12345", email, "Orange authentication", "Your Access Code: " + num);
                clientRepo.save(client);
                request.getSession().setAttribute("auth", client.getId());

                return true;
            }
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value = "/checkCode", method = RequestMethod.POST)
    public @ResponseBody String checkCode(@RequestParam String email, @RequestParam String code){
        try{
            Client client=clientRepo.findByEmail(email);
            logger.info(client.getAccesscode());
            if(client.getAccesscode().equals(code)){
                client.setActivated(true);
            }
            else{
                client.setActivated(false);
            }
            clientRepo.save(client);
            if(client.isActivated()){
                return client.getId();
            }
            else{
                return "Not activated";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    @RequestMapping(value = {"/deleteClients"},method = RequestMethod.POST)
    public @ResponseBody boolean deleteClients(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            clientRepo.deleteAll();
            patientRepo.deleteAll();
            doctorRepo.deleteAll();
            return true;}
        return false;
    }
    @RequestMapping(value={"/allClients"}, method = RequestMethod.POST)
    public @ResponseBody ArrayList<Client> allClients(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){
            return clientRepo.findAll();}
        else return null;
    }


    @RequestMapping(value={"/allPatients"}, method = RequestMethod.POST)
    public @ResponseBody ArrayList<Patient> allPatients(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)return patientRepo.findAll();
        else return null;
    }
    @RequestMapping(value={"/allDoctors"}, method = RequestMethod.POST)
    public @ResponseBody ArrayList<Doctor> allDoctors(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)return doctorRepo.findAll();
        else return null;
    }
    @RequestMapping(value={"/deleteAllDoctors"}, method= RequestMethod.POST)
    public @ResponseBody boolean deleteAllDoctors(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){
        doctorRepo.deleteAll();return true;}
        return false;
    }
    @RequestMapping(value={"/deleteAllPatients"}, method= RequestMethod.POST)
    public @ResponseBody boolean deleteAllPatients(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){

            patientRepo.deleteAll();return true;
        }
        return false;
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

    @RequestMapping(value="/getAllChats", method = RequestMethod.POST)
    public @ResponseBody List<Chat> getAllChat(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
            return chatRepo.findAll();
        else
            return null;
    }
    @RequestMapping(value="/deleteAllChats", method = RequestMethod.POST)
    public void deleteAllChat(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){
        chatRepo.deleteAll();}
    }

    @RequestMapping(value="/getAllOrders",method = RequestMethod.POST)
    public @ResponseBody List<Order> getAllOrders(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
            return orderRepo.findAll();
        else return null;
    }
    @RequestMapping(value="/deleteAllOrders",method=RequestMethod.POST)
    public void deleteAllOrders(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        orderRepo.deleteAll();
    }
    @RequestMapping(value = "/authClient", method = RequestMethod.POST)
    public @ResponseBody String authDoctor(@RequestParam String email,@RequestParam String password, HttpServletRequest request){
            Client client = clientRepo.findByEmail(email);
            if (client.getPassword().equals(password) && client.isActivated()) {
                request.getSession().setAttribute("auth", client.getId());
                try {
                    Doctor doctor = doctorRepo.findByClientid(client.getId());
                    if(doctor!=null)
                    return "doctor";
                    else return "patient";
                } catch (NullPointerException e) {
                    Patient patient = patientRepo.findByClientid(client.getId());
                    return "patient";
                }
            }
        return null;
    }
    @RequestMapping(value="/isAuth",method = RequestMethod.POST)
    public @ResponseBody boolean isAuth(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){
            return true;
        }
        else return false;
    }

}
