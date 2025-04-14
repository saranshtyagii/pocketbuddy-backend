package com.web.pocketbuddy.entity.document;

import com.web.pocketbuddy.entity.document.enums.SettlementCycle;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EnableMongoAuditing
public class GroupDocument {

    @Id
    private String groupId;
    @NotNull
    private String groupName;
    private String description;

    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastUpdateDate;
    @NotNull
    private String createdByUser;

    private Map<String, Map<String, Double>> members; // userID, UserName, ExpenseAmount

    private SettlementCycle settlementCycle;

    private double groupBudget;
    private double budgetPerDay;

    private Date tripStartDate;
    private Date tripEndDate;

    private boolean deleted;
    private Date groupDeletedDate;



}
