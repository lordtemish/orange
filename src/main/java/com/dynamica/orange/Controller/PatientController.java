package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Form.ClientWithPatientForm;
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
    ServletContext context;
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

    @RequestMapping(value = {"/addNew/{id}"}, method = RequestMethod.POST)
    public String addNew(@PathVariable("id") String id,@RequestParam String name, @RequestParam String surname, @RequestParam String email, @RequestParam String password, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = new Patient(id);
            Client client = clientRepo.findById(id);
            client.setName(name);
            client.setSurname(surname);
            client.setEmail(email);
            client.setPassword(password);
            clientRepo.save(client);
            patientRepo.save(patient);
        }
        return "index";
    }
    @RequestMapping(value = {"/setLang/{id}"}, method = RequestMethod.POST)
    public String setLang(@PathVariable("id") String id, @RequestParam String lang, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            Client client = clientRepo.findById(patient.getClientid());
            client.setLang(lang);
            clientRepo.save(client);
        }
        return "index";
    }
    @RequestMapping(value = {"/addinfo/{id}"},method = RequestMethod.POST)
    public String addInfo(@PathVariable("id") String id, @RequestParam String gender, @RequestParam String dateofbirth, @RequestParam int weight, @RequestParam int height, @RequestParam String bloodid, @RequestParam String chronic, @RequestParam String alergic, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
        Patient patient=patientRepo.findById(id);
        patient.setGender(gender.toUpperCase());
        patient.setDate(dateofbirth);
        patient.setWeight(weight);
        patient.setHeight(height);
        patient.setBlood(bloodid);
        patient.setChronic(chronic);
        patient.setAlergic(alergic);
        patientRepo.save(patient);}
        return "index";
    }
    @RequestMapping(value={"/getPatientInfo/{id}"}, method = RequestMethod.POST) //addNewForms
    public @ResponseBody ClientWithPatientForm getPatientInfo(@PathVariable("id") String id,HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            Client client = clientRepo.findById(patient.getClientid());
            ClientWithPatientForm clientWithPatientForm = new ClientWithPatientForm(client, patient);
            return clientWithPatientForm;
        }
        else{
            return null;
        }
    }
    @RequestMapping(value = {"/addMail/{id}"},method = RequestMethod.POST)
    public String addMail(@PathVariable("id") String id,@RequestParam String mail, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            Client client = clientRepo.findById(patient.getClientid());
            client.addEmail(mail);
            clientRepo.save(client);
        }
        return "index";
    }
    @RequestMapping(value = {"/deleteMail/{id}"},method = RequestMethod.POST)
    public String deleteMail(@PathVariable("id") String id,@RequestParam String mail,HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            Client client = clientRepo.findById(patient.getClientid());
            client.deleteMail(mail);
            clientRepo.save(client);
        }
        return "index";
    }
    @RequestMapping(value = {"/addPhone/{id}"},method = RequestMethod.POST)
    public String addPhone(@PathVariable("id") String id,@RequestParam String phone,HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            Client client = clientRepo.findById(patient.getClientid());
            client.addPhone(phone);
            clientRepo.save(client);
        }
        return "index";
    }
    @RequestMapping(value = {"/deletePhone/{id}"},method = RequestMethod.POST)
    public String deletePhone(@PathVariable("id") String id,@RequestParam String phone, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            Client client = clientRepo.findById(patient.getClientid());
            client.deletePhone(phone);
            clientRepo.save(client);
        }
        return "index";
    }
    @RequestMapping(value = {"/setPush/{id}"},method = RequestMethod.POST)
    public String setPush(@PathVariable("id") String id,@RequestParam boolean push, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            Client client = clientRepo.findById(patient.getClientid());
            client.setPush(push);
            clientRepo.save(client);
        }
        return "index";
    }
    @RequestMapping(value = {"/setPubl/{id}"},method = RequestMethod.POST)
    public String setPubl(@PathVariable("id") String id,@RequestParam boolean publ, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            Client client = clientRepo.findById(patient.getClientid());
            client.setPubl(publ);
            clientRepo.save(client);
        }
        return "index";
    }
    @RequestMapping(value = {"/addAddress/{name}/{id}"}, method = RequestMethod.POST)
    public String addAddress(@PathVariable("id") String id, @PathVariable("name") String name, @RequestParam String cityId, @RequestParam String address, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            switch (name) {
                case "work":
                    patient.setWorkadr(address);
                    patient.setWorkcity(cityId);
                    break;
                case "home":
                    patient.setHomeadr(address);
                    patient.setHomecity(cityId);
                    break;
            }
            patientRepo.save(patient);
        }
        return "index";
    }
    @RequestMapping(value = {"/deleteAddress/{name}/{id}"}, method = RequestMethod.POST)
    public String deleteAdress(@PathVariable("id") String id, @PathVariable("name") String name, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            switch (name) {
                case "work":
                    patient.setWorkcity(null);
                    patient.setWorkadr(null);
                    break;
                case "home":
                    patient.setHomecity(null);
                    patient.setHomeadr(null);
                    break;
            }
            patientRepo.save(patient);
        }
        return "index";
    }
    @RequestMapping(value = {"/addPhoto/{id}"}, method = RequestMethod.POST)
    public String addPhoto(@PathVariable("id") String id, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "file is empty");
            } else {
                Patient patient = patientRepo.findById(id);
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
                clientRepo.save(client);
            }
        }
        return "index";
    }
    @RequestMapping(value = {"/deletePhoto/{id}"}, method = RequestMethod.POST)
    public String delPhoto(@PathVariable("id") String id, @RequestParam String url, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            Client client = clientRepo.findById(patient.getClientid());
            client.deletePhoto(url);
            clientRepo.save(client);
            fileUploader.deletePhoto(url);
        }
        return "index";
    }
    @RequestMapping(value={"/addFavouriteDoctor/{id}"}, method = RequestMethod.POST)
    public @ResponseBody boolean addFavDoc(@PathVariable("id") String id, @RequestParam String doctorid, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient patient = patientRepo.findById(id);
                patient.addFav(doctorid);
                patientRepo.save(patient);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/deleteFavouriteDoctor/{id}", method=RequestMethod.POST)
    public @ResponseBody boolean delFavDoc(@PathVariable("id") String id, @RequestParam String doctorid,HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient patient = patientRepo.findById(id);
                boolean a = patient.deleteFav(doctorid);
                patientRepo.save(patient);
                return a;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value = {"/getFavouriteDoctors/{id}"}, method = RequestMethod.POST) //need to make new Form
    public @ResponseBody List<Doctor> getFavDocs(@PathVariable("id") String id,HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            List<Doctor> list = new ArrayList<>();
            for (String i : patient.getFavs()) {
                list.add(doctorRepo.findById(i));
            }
            return list;
        }
        else return null;
    }
    @RequestMapping(value={"/addMyDoctor/{id}"}, method = RequestMethod.POST)
    public @ResponseBody boolean addMyDoc(@PathVariable("id") String id, @RequestParam String doctorid,HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient patient = patientRepo.findById(id);
                patient.addDoc(doctorid);
                patientRepo.save(patient);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/deleteMyDoctor/{id}", method=RequestMethod.POST)
    public @ResponseBody boolean delMyDoc(@PathVariable("id") String id, @RequestParam String doctorid,HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient patient = patientRepo.findById(id);
                boolean a = patient.deleteMyDoc(doctorid);
                patientRepo.save(patient);
                return a;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value = {"/getMyDoctors/{id}"}, method = RequestMethod.POST) //need to make new Form
    public @ResponseBody List<Doctor> getMyDocs(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Patient patient = patientRepo.findById(id);
            List<Doctor> list = new ArrayList<>();
            for (String i : patient.getMydocs()) {
                list.add(doctorRepo.findById(i));
            }
            return list;
        }
        else return null;
    }

    @RequestMapping(value="/openChat/{id}", method = RequestMethod.POST)
    public @ResponseBody String openChat(@PathVariable("id") String id, @RequestParam String doctorid, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient doctor = patientRepo.findById(id);
                Chat chat = chatRepo.findOneByDoctoridAndPatientid(doctorid, id);
                Doctor doctor1 = doctorRepo.findById(doctorid);
                if (doctor1 == null) {
                    return null;
                }
                if (doctor == null) {
                    return null;
                }
                if (chat == null) {
                    Chat chat1 = new Chat(doctorid, id);
                    chatRepo.save(chat1);
                    chat = chatRepo.findOneByDoctoridAndPatientid(doctorid, id);
                }
                return chat.getId();
            }
            else return null;
        }
        catch (NullPointerException e){
            logger.info(null);
            return null;
        }
        catch (Exception e){
            return "";
        }
    }
    @RequestMapping(value="/getAllChats/{id}",method = RequestMethod.POST)
    public @ResponseBody List<Chat> getAllChats(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {return chatRepo.findByPatientid(id);}
        else return null;
    }
    @RequestMapping(value="/sendTextMessage/{id}/{chatid}",method = RequestMethod.POST)
    public @ResponseBody boolean sendTextMes(@PathVariable("id") String id,@PathVariable("chatid") String chatid, @RequestParam String text, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient doctor = patientRepo.findById(id);
                Chat chat = chatRepo.findById(chatid);
                Message message = new Message(doctor.getClientid(), "text", text);
                chat.addMessage(message);
                chat.setStatus("patient");
                chat.unreadPlus();
                chatRepo.save(chat);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="getMessages/{id}/{chatid}", method = RequestMethod.POST)
    public @ResponseBody List<Message> getMessages(@PathVariable("id") String id, @PathVariable("chatid") String chatid, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient doctor = patientRepo.findById(id);
                Chat chat = chatRepo.findById(chatid);
                if (!chat.getStatus().equals("patient")) {
                    chat.setUnread(0);
                }
                chatRepo.save(chat);
                return chat.getMessages();
            }
            else return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping(value="/sendFileMessage/{id}/{chatid}",method = RequestMethod.POST)
    public @ResponseBody boolean sendFileMes(@PathVariable("id") String id,@PathVariable("chatid") String chatid, @RequestParam String type, @RequestParam MultipartFile file, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient doctor = patientRepo.findById(id);
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
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value="/addOrder/{id}",method = RequestMethod.POST)
    public @ResponseBody String addOrder(@PathVariable("id") String id, @RequestParam boolean atwork, @RequestParam String doctorid, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Order order = new Order(doctorid, id);
                order.setAtwork(atwork);
                orderRepo.save(order);
                return order.getId();
            }
            else return null;
        }
        catch (Exception e){
            return null;
        }
    }
    @RequestMapping(value="/addOrderInfoWorkplace/{id}/{orderid}", method=RequestMethod.POST)
    public @ResponseBody boolean addOrderInfo(@PathVariable("id") String id, @PathVariable("orderid") String orderid, @RequestParam String services, @RequestParam long chosetime, @RequestParam String periodTime, @RequestParam String text, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient patient = patientRepo.findById(id);
                Order order = orderRepo.findById(orderid);
                order.addServices(services);
                order.setChoseTime(chosetime);
                order.setPeriodTime(periodTime);
                order.setTextMessage(text);
                orderRepo.save(order);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/addOrderInfoHome/{id}/{orderid}", method=RequestMethod.POST)
    public @ResponseBody boolean addOrderInfoHome(@PathVariable("id") String id, @PathVariable("orderid") String orderid, @RequestParam String services, @RequestParam long chosetime,  @RequestParam String text, @RequestParam double period, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient patient = patientRepo.findById(id);
                Order order = orderRepo.findById(orderid);
                order.addServices(services);
                order.setChoseTime(chosetime);
                order.setTextMessage(text);
                order.setPeriodinhours(period);
                orderRepo.save(order);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //order atHome
    @RequestMapping(value="/addOrderFile/{id}/{orderid}",method = RequestMethod.POST)
    public @ResponseBody boolean addOrderFile(@PathVariable("id") String id, @PathVariable("orderid") String orderid, @RequestParam String type, @RequestParam MultipartFile file, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient patient = patientRepo.findById(id);
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
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value = "/addOrderAddress/{id}/{orderid}",method = RequestMethod.POST)
    public @ResponseBody boolean addOrderAddress(@PathVariable("id") String id, @PathVariable("orderid") String orderid, /*@RequestParam String cityid,*/ @RequestParam String address, HttpServletRequest request){
        try{ // Set Normal cities and addresses
            if(request.getSession().getAttribute("auth")!=null) {
                Patient patient = patientRepo.findById(id);
                Order order = orderRepo.findById(orderid);
                order.setAddress(address);
                orderRepo.save(order);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/cancelOrder/{id}/{orderid}",method = RequestMethod.POST)
    public @ResponseBody boolean cancelOrder(@PathVariable("id") String id, @PathVariable("orderid") String orderid, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Patient patient = patientRepo.findById(id);
                Order order = orderRepo.findById(orderid);
                order.setStatus("cancelled");
                orderRepo.save(order);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/getMyOrders/{id}",method = RequestMethod.POST)
    public @ResponseBody ArrayList<Order> getMyOrders(@PathVariable("id") String id,HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                return orderRepo.findByPatientid(id);
            }
            else return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    //ORDERS
}
