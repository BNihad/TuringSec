package com.turingSecApp.turingSec.Response;

import com.turingSecApp.turingSec.dao.entities.CompanyEntity;


public class RegistrationResponse {
    private CompanyEntity company;

    public RegistrationResponse(CompanyEntity company) {
        this.company = company;

    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }


}