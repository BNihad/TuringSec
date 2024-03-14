package com.turingSecApp.turingSec.Request;

import com.turingSecApp.turingSec.dao.entities.ReportsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ReportsByUserDTO {
    private Long userId;
    private List<ReportsEntity> reports;
}
