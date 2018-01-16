package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Education;
import com.dynamica.orange.Classes.EducationType;

/**
 * Created by lordtemich on 1/16/18.
 */
public class EducationForm {
    Education education;
    EducationType educationType;
    public EducationForm(Education education,EducationType educationType){
        this.education=education;
        this.educationType=educationType;
    }

    public Education getEducation() {
        return education;
    }

    public EducationType getEducationType() {
        return educationType;
    }
}
