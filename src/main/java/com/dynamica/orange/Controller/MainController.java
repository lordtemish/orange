package com.dynamica.orange.Controller;

import com.dynamica.orange.Classes.Blood;
import com.dynamica.orange.Classes.City;
import com.dynamica.orange.Classes.OwnTimeType;
import com.dynamica.orange.Repo.BloodRepo;
import com.dynamica.orange.Repo.CityRepo;
import com.dynamica.orange.Repo.OwnTimeTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @RequestMapping(value = {"/deleteblood"},method = RequestMethod.POST)
    public String deleteBlood(){
        bloodRepo.deleteAll();
        return "index";
    }
}
