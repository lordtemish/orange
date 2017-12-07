package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.*;
import com.dynamica.orange.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Created by lordtemich on 10/27/17.
 */
@Controller
@RequestMapping(value= {"/"})
public class MainController {
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

    @RequestMapping(value = {"/"},method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(value = {"/allcity"},method = RequestMethod.POST)
    public @ResponseBody ArrayList<City> getCities(){
        return cityRepo.findAll();
    }

    @RequestMapping(value = {"/cityadd"},method = RequestMethod.POST)
    public String addCity(@RequestParam String rus, @RequestParam String kaz){
        City city=new City(rus,kaz);
        cityRepo.save(city);
        return "index";
    }
    @RequestMapping(value = {"/deletecities"},method = RequestMethod.POST)
    public String deleteCities(){
        cityRepo.deleteAll();
        return "index";
    }

    @RequestMapping(value = {"/deletecity/{id}"},method = RequestMethod.POST)
    public String deleteCity(@PathVariable("id") String id){
        City city=cityRepo.findById(id);
        cityRepo.delete(city);
        return "index";
    }

    @RequestMapping(value = {"/allott"},method = RequestMethod.POST)
    public @ResponseBody ArrayList<OwnTimeType> getOtts(){
        return ownTimeTypeRepo.findAll();
    }
    @RequestMapping(value={"/ottAdd"}, method = RequestMethod.POST)
    public String addOtt(@RequestParam String rus, @RequestParam String kaz ){
        OwnTimeType ott=new OwnTimeType(rus,kaz);
        ownTimeTypeRepo.save(ott);
        return "index";
    }
    @RequestMapping(value = {"/deleteotts"},method = RequestMethod.POST)
    public String deleteOtts(){
        ownTimeTypeRepo.deleteAll();
        return "index";
    }
    @RequestMapping(value = {"/deleteott/{id}"},method = RequestMethod.POST)
    public String deleteOtt(@PathVariable("id") String id){
        OwnTimeType city=ownTimeTypeRepo.findById(id);
        ownTimeTypeRepo.delete(city);
        return "index";
    }


    @RequestMapping(value = {"/allblood"},method = RequestMethod.POST)
    public @ResponseBody ArrayList<Blood> getBlood(){
        return bloodRepo.findAll();
    }
    @RequestMapping(value={"/bloodAdd"}, method = RequestMethod.POST)
    public String addBlood(@RequestParam String name){
        Blood blood=new Blood(name);
        bloodRepo.save(blood);
        return "index";
    }
    @RequestMapping(value = {"/deletebloods"},method = RequestMethod.POST)
    public String deleteBloods(){
        bloodRepo.deleteAll();
        return "index";
    }
    @RequestMapping(value = {"/deleteblood/{id}"},method = RequestMethod.POST)
    public String deleteBlood(@PathVariable("id") String id){
        Blood city=bloodRepo.findById(id);
        bloodRepo.delete(city);
        return "index";
    }

    @RequestMapping(value = {"/allsertype"}, method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<ServiceType> getSerType() {
        return sertypeRepo.findAll();
    }

    @RequestMapping(value = {"/sertypeAdd"}, method = RequestMethod.POST)
    public String addSerType(@RequestParam String rus, @RequestParam String kaz) {
        ServiceType serviceType = new ServiceType(rus, kaz);
        sertypeRepo.save(serviceType);
        return "index";
    }

    @RequestMapping(value = {"/deletesertypes"}, method = RequestMethod.POST)
    public String deleteSerTypes() {
        sertypeRepo.deleteAll();
        return "index";
    }
    @RequestMapping(value = {"/deletesertype/{id}"},method = RequestMethod.POST)
    public String deleteSerType(@PathVariable("id") String id){
        ServiceType city=sertypeRepo.findById(id);
        sertypeRepo.delete(city);
        return "index";
    }


    @RequestMapping(value={"/allmaps"}, method = RequestMethod.POST)
    public @ResponseBody  ArrayList<Map> allMaps(){
        return mapRepo.findAll();
    }

    @RequestMapping(value={"/deletemaps"}, method = RequestMethod.POST)
    public String deleteMaps(){
        mapRepo.deleteAll();
        return "index";
    }
    @RequestMapping(value = {"/deletemap/{id}"},method = RequestMethod.POST)
    public String deleteMap(@PathVariable("id") String id){
        Map city=mapRepo.findById(id);
        mapRepo.delete(city);
        return "index";
    }



    @RequestMapping(value = {"/addEdType"},method = RequestMethod.POST)
    public String addEdType(@RequestParam String rus, @RequestParam String kaz){
        edTypeRepo.save(new EducationType(rus,kaz));
        return "index";
    }
    @RequestMapping(value = {"/allEdTypes"}, method = RequestMethod.POST)
    public @ResponseBody ArrayList<EducationType> allEdTypes(){
        return edTypeRepo.findAll();
    }
    @RequestMapping(value={"/deleteEdTypes"}, method = RequestMethod.POST)
    public String deleteEdTypes(){
        edTypeRepo.deleteAll();
        return "index";
    }
    @RequestMapping(value = {"/deleteEdType/{id}"},method = RequestMethod.POST)
    public String deleteEdType(@PathVariable("id") String id){
        EducationType city=edTypeRepo.findById(id);
        edTypeRepo.delete(city);
        return "index";
    }



    @RequestMapping(value = {"/addService/{id}"},method = RequestMethod.POST)
    public String addService(@PathVariable("id") String id,@RequestParam String rus, @RequestParam String kaz){
        serviceRepo.save(new Service(id, rus, kaz));
        return "index";
    }
    @RequestMapping(value = {"/allServices"}, method = RequestMethod.POST)
    public @ResponseBody ArrayList<Service> allServices(){
        return serviceRepo.findAll();
    }
    @RequestMapping(value={"/deleteServices"}, method = RequestMethod.POST)
    public String deleteServices(){
        serviceRepo.deleteAll();
        return "index";
    }
    @RequestMapping(value = {"/deleteService/{id}"},method = RequestMethod.POST)
    public String deleteService(@PathVariable("id") String id){
        Service city=serviceRepo.findById(id);
        serviceRepo.delete(city);
        return "index";
    }
}
