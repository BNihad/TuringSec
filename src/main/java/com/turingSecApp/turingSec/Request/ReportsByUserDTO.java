package com.turingSecApp.turingSec.Request;

import com.turingSecApp.turingSec.dao.entities.ReportsEntity;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ReportsByUserDTO {
    private UserDTO user;
    private List<ReportsEntity> reports;
}
