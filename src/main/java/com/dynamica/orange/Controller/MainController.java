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
import sun.applet.Main;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lordtemich on 10/27/17.
 */
@Controller
@RequestMapping(value= {"/"})
public class MainController {
    private Logger logger = LoggerFactory.getLogger(MainController.class);

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
    @RequestMapping(value = {"/"},method = RequestMethod.GET)
    public String index(HttpServletRequest request){
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
        public String addCity(@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null) {
                City city = new City(rus, kaz);
                cityRepo.save(city);
                return "index";
            }
            else {
                return null;
            }
        }
        @RequestMapping(value = {"/deletecities"},method = RequestMethod.POST)
        public String deleteCities(HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            cityRepo.deleteAll();}
            return "index";
        }

        @RequestMapping(value = {"/deletecity/{id}"},method = RequestMethod.POST)
        public String deleteCity(@PathVariable("id") String id, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            City city=cityRepo.findById(id);
            cityRepo.delete(city);}
            return "index";
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
        public String addOtt(@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            OwnTimeType ott=new OwnTimeType(rus,kaz);
            ownTimeTypeRepo.save(ott);}
            return "index";
        }
        @RequestMapping(value = {"/deleteotts"},method = RequestMethod.POST)
        public String deleteOtts(HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            ownTimeTypeRepo.deleteAll();}
            return "index";
        }
        @RequestMapping(value = {"/deleteott/{id}"},method = RequestMethod.POST)
        public String deleteOtt(@PathVariable("id") String id, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            OwnTimeType city=ownTimeTypeRepo.findById(id);
            ownTimeTypeRepo.delete(city);}
            return "index";
        }


    @RequestMapping(value = {"/allblood"},method = RequestMethod.POST)
    public @ResponseBody ArrayList<Blood> getBlood(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        return bloodRepo.findAll();
        else
            return null;
    }
        @RequestMapping(value={"/bloodAdd"}, method = RequestMethod.POST)
        public String addBlood(@RequestParam String name, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null) {
                Blood blood = new Blood(name);
                bloodRepo.save(blood);
            }
            return "index";
        }
        @RequestMapping(value = {"/deletebloods"},method = RequestMethod.POST)
        public String deleteBloods(HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null)
            bloodRepo.deleteAll();
            return "index";
        }
        @RequestMapping(value = {"/deleteblood/{id}"},method = RequestMethod.POST)
        public String deleteBlood(@PathVariable("id") String id, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            Blood city=bloodRepo.findById(id);
            bloodRepo.delete(city);}
            return "index";
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
        public String addSerType(@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request) {
            if(request.getSession().getAttribute("auth")!=null) {
                ServiceType serviceType = new ServiceType(rus, kaz);
                sertypeRepo.save(serviceType);
            }
            return "index";
        }

        @RequestMapping(value = {"/deletesertypes"}, method = RequestMethod.POST)
        public String deleteSerTypes(HttpServletRequest request) {
            if(request.getSession().getAttribute("auth")!=null)
            sertypeRepo.deleteAll();
            return "index";
        }
        @RequestMapping(value = {"/deletesertype/{id}"},method = RequestMethod.POST)
        public String deleteSerType(@PathVariable("id") String id, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            ServiceType city=sertypeRepo.findById(id);
            sertypeRepo.delete(city);}
            return "index";
        }


    @RequestMapping(value={"/allmaps"}, method = RequestMethod.POST)
    public @ResponseBody  ArrayList<Map> allMaps(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){return mapRepo.findAll();}
        else{return null;}
    }

        @RequestMapping(value={"/deletemaps"}, method = RequestMethod.POST)
        public String deleteMaps(HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null)
            mapRepo.deleteAll();
            return "index";
        }
        @RequestMapping(value = {"/deletemap/{id}"},method = RequestMethod.POST)
        public String deleteMap(@PathVariable("id") String id, HttpServletRequest request){
            if(request.getSession().getAttribute("auth")!=null){
            Map city=mapRepo.findById(id);
            mapRepo.delete(city);}
            return "index";
        }



    @RequestMapping(value = {"/addEdType"},method = RequestMethod.POST)
    public String addEdType(@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        edTypeRepo.save(new EducationType(rus,kaz));
        return "index";
    }
    @RequestMapping(value = {"/allEdTypes"}, method = RequestMethod.POST)
    public @ResponseBody ArrayList<EducationType> allEdTypes(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        return edTypeRepo.findAll();
        else
            return null;
    }
    @RequestMapping(value={"/deleteEdTypes"}, method = RequestMethod.POST)
    public String deleteEdTypes(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
            edTypeRepo.deleteAll();
        return "index";
    }
    @RequestMapping(value = {"/deleteEdType/{id}"},method = RequestMethod.POST)
    public String deleteEdType(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        {EducationType city=edTypeRepo.findById(id);
        edTypeRepo.delete(city);}
        return "index";
    }



    @RequestMapping(value = {"/addService/{id}"},method = RequestMethod.POST)
    public String addService(@PathVariable("id") String id,@RequestParam String rus, @RequestParam String kaz, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        serviceRepo.save(new Service(id, rus, kaz));
        return "index";
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
    public String deleteServices(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        serviceRepo.deleteAll();
        return "index";
    }
    @RequestMapping(value = {"/deleteService/{id}"},method = RequestMethod.POST)
    public String deleteService(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){
        Service city=serviceRepo.findById(id);
        serviceRepo.delete(city);}
        return "index";
    }
    @RequestMapping(value={"/changePassword/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean changePas(@PathVariable("id") String id, @RequestParam String nPassword, @RequestParam String oPassword){
        try{
            Client client=clientRepo.findById(id);
            if(client.getPassword().equals(oPassword)){
                client.setPassword(nPassword);
                clientRepo.save(client);
                return true;
            }
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value = {"/addClient"},method = RequestMethod.POST)
    public String addClient(@RequestParam String phone, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        {Client client=new Client(phone);
        clientRepo.save(client);}
        return "index";
    }
    @RequestMapping(value = {"/deleteClients"},method = RequestMethod.POST)
    public String deleteClients(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            clientRepo.deleteAll();
        }
        return "index";
    }
    @RequestMapping(value={"/allClients"}, method = RequestMethod.POST)
    public @ResponseBody ArrayList<Client> allClients(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null){return clientRepo.findAll();}
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
    public String deleteAllDoctors(HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        doctorRepo.deleteAll();
        return "index";
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
    public @ResponseBody String authDoctor(@RequestParam String phone,@RequestParam String password, HttpServletRequest request){
            Client client = clientRepo.findByPhone(phone);
            if (client.getPassword().equals(password)) {
                request.getSession().setAttribute("auth", client.getId());
                try {
                    Doctor doctor = doctorRepo.findByClientid(client.getId());
                    return "doctor";
                } catch (NullPointerException e) {
                    Patient patient = patientRepo.findByClientid(client.getId());
                    return "patient";
                }
            }
        return null;
    }

}
