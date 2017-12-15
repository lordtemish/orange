package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Form.ClientWithDoctorForm;
import com.dynamica.orange.Repo.ClientRepo;
import com.dynamica.orange.Repo.DoctorRepo;
import com.dynamica.orange.Repo.ServiceTypeRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    DoctorRepo doctorRepo;
    @Autowired
    ServiceTypeRepo serviceTypeRepo;
    FileUploader fileUploader=new FileUploader();
    @RequestMapping(value = "/addDoctor/{id}",method = RequestMethod.POST)
    public String addDoctor(@PathVariable("id") String id, @RequestParam String name,@RequestParam String surname, @RequestParam String dad, @RequestParam String position, @RequestParam String info, @RequestParam String service_type_id, @RequestParam String password){
        Doctor doctor=new Doctor(id,position,info);
        Client client=clientRepo.findById(id);
        client.setName(name);
        client.setSurname(surname);
        client.setDadname(dad);
        client.setPassword(password);
        doctor.setInfo(info);
        doctor.setServicetypeid(service_type_id);
        doctorRepo.save(doctor);
        clientRepo.save(client);
        return "index";
    }
    @RequestMapping(value = "/setServiceTypeId/{id}",method = RequestMethod.POST)
    public @ResponseBody boolean setSerTypeId(@PathVariable("id")  String id, @RequestParam String service_type_id){
        try {
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
        catch (NullPointerException e){
            return false;
        }
    }
    @RequestMapping(value = "/addEducation/{id}",method = RequestMethod.POST)
    public String addEducation(@PathVariable("id") String id, @RequestParam String ed_type_id,@RequestParam String name, @RequestParam String speciality, @RequestParam String start, @RequestParam String stop){
        Education education=new Education(ed_type_id,name,speciality,start,stop);
        Doctor doctor=doctorRepo.findById(id);
        doctor.addEducation(education);
        doctorRepo.save(doctor);
        return "index";
    }
    @RequestMapping(value="/addEducationCertificate/{id}/{certid}", method = RequestMethod.POST)
    public @ResponseBody boolean addEdCert(@PathVariable("id") String id, @PathVariable("certid") String certid, @RequestParam MultipartFile file,  RedirectAttributes redirectAttributes){
        Doctor doctor=doctorRepo.findById(id);
        try{
            Education ed=doctor.getEducationById(certid);
            String url="educationphoto-"+ed.getId();
            int i=0;
            while(true){
                String s=fileUploader.upload(file, url+i);
                if(s.equals("")){
                    i++;
                    continue;
                }
                else{
                    url=s;
                    break;
                }
            }
            ed.addUrl(url);
            doctor.setEducationById(certid,ed);
            doctorRepo.save(doctor);
            return true;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }

    }
    @RequestMapping(value="/getDoctorInfo/{id}")
    public @ResponseBody
    ClientWithDoctorForm getDoctorInfo(@PathVariable("id") String id){
        Doctor doctor=doctorRepo.findById(id);
        Client client=clientRepo.findById(doctor.getClientid());
        return new ClientWithDoctorForm(client,doctor);
    }
}
