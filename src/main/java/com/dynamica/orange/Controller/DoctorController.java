package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Form.ChatListForm;
import com.dynamica.orange.Form.ClientWithDoctorForm;
import com.dynamica.orange.Form.EducationForm;
import com.dynamica.orange.Form.OrderListForm;
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
    @RequestMapping(value = "/addDoctor/{id}",method = RequestMethod.POST)
    public  @ResponseBody boolean addDoctor(@PathVariable("id") String id, @RequestParam String name,@RequestParam String surname, @RequestParam String dad, @RequestParam String position, @RequestParam String info, @RequestParam String service_type_id, @RequestParam String password, HttpServletRequest request){
        try {
            Doctor doctor = new Doctor(id, position, info);
            Client client = clientRepo.findById(id);
            client.setName(name);
            client.setSurname(surname);
            client.setDadname(dad);
            client.setPassword(password);
            doctor.setInfo(info);
            doctor.setServicetypeid(service_type_id);
            doctorRepo.save(doctor);
            clientRepo.save(client);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        return false;}
    }
    @RequestMapping(value = {"/setAddress/{name}/{id}"}, method = RequestMethod.POST)
    public  @ResponseBody boolean addAddress(@PathVariable("id") String id, @PathVariable("name") String name, @RequestParam String cityId, @RequestParam String address, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Doctor doctor=doctorRepo.findById(id);
            switch (name) {
                case "work":
                    doctor.setWorkAddress(new Address(cityId,address));
                    break;
                case "home":
                    doctor.setHomeAddress(new Address(cityId,address));
                    break;
            }
            doctorRepo.save(doctor);
            return true;}
        return false;
    }
    @RequestMapping(value = {"/setLang/{id}"}, method = RequestMethod.POST)
    public  @ResponseBody boolean setLang(@PathVariable("id") String id, @RequestParam String lang, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Doctor doctor = doctorRepo.findById(id);
            Client client = clientRepo.findById(doctor.getClientid());
            client.setLang(lang.toUpperCase());
            clientRepo.save(client);
            return true;}
        return false;
    }

    //addTimeSchedule
    @RequestMapping(value = "/addOwnService/{id}",method = RequestMethod.POST) //ottid change
    public @ResponseBody boolean addOwnService(@PathVariable("id") String id, @RequestParam String name, @RequestParam String ottid, @RequestParam String info, @RequestParam int price, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                doctor.addOwnService(new OwnService(name, ottid, info, price));
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/getOwnServices/{id}", method = RequestMethod.POST)
    public @ResponseBody List<OwnService> getOwnServices(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Doctor doctor = doctorRepo.findById(id);
            return doctor.getOwns();
        }
        else return null;
    }
    @RequestMapping(value="/deleteOwnService/{id}", method = RequestMethod.POST)
    public @ResponseBody boolean deleteOwnService(@PathVariable("id") String id, @RequestParam String ownserviceid, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                for (OwnService i : doctor.getOwns()) {
                    if (i.getId().equals(ownserviceid)) {
                        doctor.getOwns().remove(i);
                        return true;
                    }
                }
            }
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value = "/setServiceTypeId/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean setSerTypeId(@PathVariable("id")  String id, @RequestParam String service_type_id, HttpServletRequest request){
        try {
            if(request.getSession().getAttribute("auth")!=null) {
                ServiceType st = serviceTypeRepo.findById(service_type_id);
                if (st.equals(null)) {
                    return false;
                } else {
                    Doctor doctor = doctorRepo.findById(id);
                    doctor.setServicetypeid(service_type_id);
                    doctorRepo.save(doctor);
                    return true;
                }
            }
            return false;
        }
        catch (NullPointerException e){
            return false;
        }
    }
    @RequestMapping(value="/setServices/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean setServices(@PathVariable("id") String id, @RequestParam String services, HttpServletRequest request){
        try {
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                doctor.clearServices();
                String[] service = services.split(" ");
                for (int i = 0; i < service.length; i++) {
                    doctor.addService(service[i]);
                }
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value="/servicesId/{id}", method = RequestMethod.POST)
    public @ResponseBody List<String> servicesId(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Doctor doctor = doctorRepo.findById(id);
            return doctor.getServices();
        }
        else return null;
    }
    @RequestMapping(value="/getServices/{id}", method = RequestMethod.POST)
    public @ResponseBody List<Service> getServices(@PathVariable("id") String id, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null){
                Doctor doctor=doctorRepo.findById(id);
                List<Service> services=new ArrayList<>();
                for(String i:doctor.getServices()){
                    services.add(serviceRepo.findById(i));
                }
                return services;
            }
            else return null;
        }
        catch (Exception e){
            e.printStackTrace(); return null;
        }
    }
    @RequestMapping(value="/addCertificate/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean addCertificate(@PathVariable("id") String id, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Doctor doctor = doctorRepo.findById(id);
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
                return true;
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
        else return false;
    }
    @RequestMapping(value="/deleteCertificate/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean deleteCertificate(@PathVariable("id") String id, @RequestParam String url, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Doctor doctor = doctorRepo.findById(id);
            boolean a = false;
            a = doctor.deleteCertificate(url);
            fileUploader.deletePhoto(url);
            doctorRepo.save(doctor);
            return a;
        }
        else return false;
    }
    @RequestMapping(value = "/addEducation/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean addEducation(@PathVariable("id") String id, @RequestParam String ed_type_id,@RequestParam String name, @RequestParam String speciality, @RequestParam String start, @RequestParam String stop, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Education education = new Education(ed_type_id, name, speciality, start, stop);
            Doctor doctor = doctorRepo.findById(id);
            doctor.addEducation(education);
            doctorRepo.save(doctor);
            return true;
        }
        return false;
    }
    @RequestMapping(value="/deleteEducation/{id}/{edid}",method =RequestMethod.POST)
    public @ResponseBody boolean deleteEducation(@PathVariable("id") String id, @PathVariable("edid") String edid, HttpServletRequest request){
        try {
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                Education education = doctor.getEducationById(edid);
                for (String i : education.getUrls()) {
                    fileUploader.deletePhoto(i);
                }
                doctor.deleteEducation(education);
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/addEducationCertificate/{id}/{edid}", method = RequestMethod.POST)
    public @ResponseBody boolean addEdCert(@PathVariable("id") String id, @PathVariable("edid") String certid, @RequestParam MultipartFile file,  RedirectAttributes redirectAttributes, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Doctor doctor = doctorRepo.findById(id);
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
                return true;
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
        else return false;
    }
    @RequestMapping(value="/deleteEducationCertificate/{id}/{certid}", method = RequestMethod.POST)
    public @ResponseBody boolean deleteEdCert(@PathVariable("id") String id, @PathVariable("certid") String certid, @RequestParam String url, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Doctor doctor = doctorRepo.findById(id);
            try {
                Education ed = doctor.getEducationById(certid);
                if (ed.getUrls().contains(url)) {
                    ed.getUrls().remove(url);
                } else {
                    return false;
                }
                doctorRepo.save(doctor);
                fileUploader.deletePhoto(url);
                return true;
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
        else return false;
    }
    @RequestMapping(value="/getEducationList/{id}",method = RequestMethod.POST)
    @ResponseBody List<EducationForm> getEducations(@PathVariable("id") String id, HttpServletRequest request){
        try {
            if(request.getSession().getAttribute("auth")!=null){
                List<EducationForm> educationForms=new ArrayList<>();
                Doctor doctor=doctorRepo.findById(id);
                for(Education i:doctor.getEducations()){
                    educationForms.add(new EducationForm(i,educationTypeRepo.findById(i.getEd_type_id())));
                }
                return educationForms;
            }
            else return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping(value={"/addProfessionalAch/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean addProfAch(@PathVariable("id") String id, @RequestParam String info, HttpServletRequest request){
        try {
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                doctor.addProfAch(info);
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (NullPointerException e){
            return false;
        }
    }
    @RequestMapping(value={"/deleteProfessionalAch/{id}"}, method = RequestMethod.POST)
    public @ResponseBody boolean deleteProfAch(@PathVariable("id") String id, @RequestParam int index, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                doctor.getProfachievments().remove(index);
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (NullPointerException e){
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value={"/updateProfessionalAch/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean updateProfAch(@PathVariable("id") String id, @RequestParam int index, @RequestParam String info, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                doctor.getProfachievments().set(index, info);
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value={"/addExtraInfo/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean addExtraInfo(@PathVariable("id") String id, @RequestParam String info, HttpServletRequest request){
        try {
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                doctor.addExtraInfo(info);
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (NullPointerException e){
            return false;
        }
    }
    @RequestMapping(value={"/deleteExtraInfo/{id}"}, method = RequestMethod.POST)
    public @ResponseBody boolean deleteExtraInfo(@PathVariable("id") String id, @RequestParam int index,HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                doctor.getExtrainfo().remove(index);
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (NullPointerException e){
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value={"/updateExtraInfo/{id}"},method = RequestMethod.POST)
    public @ResponseBody boolean updateExtraInfo(@PathVariable("id") String id, @RequestParam int index, @RequestParam String info, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                doctor.getExtrainfo().set(index, info);
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    @RequestMapping(value = {"/addPhoto/{id}"}, method = RequestMethod.POST)
    public String addPhoto(@PathVariable("id") String id, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "file is empty");

            } else {
                Doctor patient = doctorRepo.findById(id);
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
            Doctor patient = doctorRepo.findById(id);
            Client client = clientRepo.findById(patient.getClientid());
            client.deletePhoto(url);
            clientRepo.save(client);
            fileUploader.deletePhoto(url);
        }
        return "index";
    }

    @RequestMapping(value="/addExperience/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean addExperience(@PathVariable("id") String id, @RequestParam String name, @RequestParam int years, @RequestParam String position, @RequestParam String startyear, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                Experience experience = new Experience(name, position, years, startyear);
                doctor.addExperience(experience);
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/deleteExperience/{id}/{expid}",method = RequestMethod.POST)
    public @ResponseBody boolean deleteExperience(@PathVariable("id") String id, @PathVariable("expid") String experienceid, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                doctor.deleteExperiencebyId(experienceid);
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/updateExperience/{id}/{expid}",method = RequestMethod.POST)
    public @ResponseBody boolean updateExperience(@PathVariable("id") String id, @PathVariable("expid") String experienceid, @RequestParam String name, @RequestParam int years, @RequestParam String position, @RequestParam String startyear, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                doctor.updateExperiencebyId(experienceid, name, position, years, startyear);
                doctorRepo.save(doctor);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value = "/setSchedule/{id}/{name}", method = RequestMethod.POST)
    public @ResponseBody boolean addSchedule(@PathVariable("id") String id, @PathVariable("name") String name, @RequestParam String sch, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
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
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    @RequestMapping(value="/openChat/{id}", method = RequestMethod.POST)
    public @ResponseBody String openChat(@PathVariable("id") String id, @RequestParam String patientid, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                Chat chat = chatRepo.findOneByDoctoridAndPatientid(doctor.getId(), patientid);
                Patient doctor1 = patientRepo.findById(patientid);
                if (doctor1 == null) {
                    return null;
                }
                if (doctor == null) {
                    return null;
                }
                if (chat == null) {
                    Chat chat1 = new Chat(doctor.getId(), patientid);
                    chatRepo.save(chat1);
                    chat = chatRepo.findOneByDoctoridAndPatientid(id, patientid);
                }
                return chat.getId();
            }
            else return null;
        }
        catch (NullPointerException e){
            log.info(null);
            return null;
        }
        catch (Exception e){
            return "";
        }
    }

    @RequestMapping(value="/getAllChats/{id}",method = RequestMethod.POST)
    public @ResponseBody List<Chat> getAllChats(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            return chatRepo.findByDoctorid(id);
        }
        else return null;
    }
    @RequestMapping(value="/getAllChatsList/{id}", method = RequestMethod.POST)
    public @ResponseBody List<ChatListForm> getAllChatsList(@PathVariable("id") String id, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                List<Chat> chats = chatRepo.findByDoctorid(id);
                List<ChatListForm> forms = new ArrayList<>();
                for (Chat i : chats) {
                    forms.add(new ChatListForm(i, i.getLastMessage(), clientRepo.findById(i.getLastMessage().getClientid())));
                }
                return forms;
            }
            else return null;
        }
        catch(Exception e){

            return null;
        }
    }
    @RequestMapping(value="/sendTextMessage/{id}/{chatid}",method = RequestMethod.POST)
    public @ResponseBody boolean sendTextMes(@PathVariable("id") String id,@PathVariable("chatid") String chatid, @RequestParam String text, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                Chat chat = chatRepo.findById(chatid);
                Message message = new Message(doctor.getClientid(), "text", text);
                chat.addMessage(message);
                chat.setStatus("doctor");
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
    @RequestMapping(value="/sendFileMessage/{id}/{chatid}",method = RequestMethod.POST)
    public @ResponseBody boolean sendFileMes(@PathVariable("id") String id,@PathVariable("chatid") String chatid, @RequestParam String type, @RequestParam MultipartFile file, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
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
    @RequestMapping(value="/getMessages/{id}/{chatid}", method = RequestMethod.POST)
    public @ResponseBody List<Message> getMessages(@PathVariable("id") String id, @PathVariable("chatid") String chatid, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Doctor doctor = doctorRepo.findById(id);
                Chat chat = chatRepo.findById(chatid);
                if (!chat.getStatus().equals("doctor")) {
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
    @RequestMapping(value = "/getListOrders/{id}",method = RequestMethod.POST)
    public @ResponseBody ArrayList<OrderListForm> getListOrders(@PathVariable("id") String id, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null){
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
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping(value="/getMyOrders/{id}", method = RequestMethod.POST)
    public @ResponseBody ArrayList<Order> getMyOrders(@PathVariable("id") String id, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null){ return orderRepo.findByDoctorid(id);}
            else return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping(value="/cancelOrder/{id}/{orderid}",method = RequestMethod.POST)
    public @ResponseBody boolean cancelOrder(@PathVariable("id") String id,@PathVariable("orderid") String orderid, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
                Order order = orderRepo.findById(orderid);
                order.setStatus("doctorcancelled");
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
    @RequestMapping(value="/acceptOrder/{id}/{orderid}", method = RequestMethod.POST)
    public @ResponseBody boolean acceptOrder(@PathVariable("id") String id,@PathVariable("orderid") String orderid, HttpServletRequest request) {
        try{
            Order order=orderRepo.findById(orderid);
            order.setStatus("doctoraccepted");
            orderRepo.save(order);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value = "/setTextAnswers/{id}/{orderid}",method = RequestMethod.POST)
    public @ResponseBody boolean setTextAnswers(@PathVariable("id") String id, @PathVariable("orderid") String orderid, @RequestParam String diagnos, @RequestParam String healing, @RequestParam String comment, HttpServletRequest request){
        try{
            Order order=orderRepo.findById(orderid);
            order.setDiagnosAnswer(diagnos);
            order.setHealingAnswer(healing);
            order.setTextAnswer(comment);
            order.setStatus("doctoranswered");
            orderRepo.save(order);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/setAudioHealingAnswer/{id}/{orderid}", method = RequestMethod.POST)
    public @ResponseBody boolean setAudioHealingAnswer(@PathVariable("id") String id, @PathVariable("orderid") String orderid, @RequestParam MultipartFile file, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
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
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/addOrderFileComment/{id}/{orderid}",method=RequestMethod.POST)
    public @ResponseBody boolean addOrderFileComment(@PathVariable("id") String id, @PathVariable("orderid") String orderid, @RequestParam String type,@RequestParam MultipartFile file, HttpServletRequest request){
        try{
            if(request.getSession().getAttribute("auth")!=null) {
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
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping(value="/testRequest/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean test(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null)
        log.info("all is good");
        return true;
    }
    @RequestMapping(value="/getDoctorInfo/{id}")
    public @ResponseBody ClientWithDoctorForm getDoctorInfo(@PathVariable("id") String id, HttpServletRequest request){
        if(request.getSession().getAttribute("auth")!=null) {
            Doctor doctor = doctorRepo.findById(id);
            Client client = clientRepo.findById(doctor.getClientid());
            return new ClientWithDoctorForm(client, doctor);
        }
        return null;
    }
}
