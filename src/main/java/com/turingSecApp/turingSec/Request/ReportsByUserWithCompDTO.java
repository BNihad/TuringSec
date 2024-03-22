package com.turingSecApp.turingSec.Request;

import com.turingSecApp.turingSec.dao.entities.ReportsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportsByUserWithCompDTO {


    private String companyName;
    private List<ReportsEntity> reports;



}
