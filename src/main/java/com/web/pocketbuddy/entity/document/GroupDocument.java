package com.web.pocketbuddy.entity.document;

import com.web.pocketbuddy.entity.document.enums.SettlementCycle;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupDocument {

    @Id
    private String groupId;

    // Ensure this is unique at the DB level using indexing
    private String groupDiscoverableId;

    @NotNull
    private String groupName;

    private String description;

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date lastUpdateDate;

    @NotNull
    private String createdByUser;

    private Map<String, Double> members; // Map<userId, totalExpense>

    private SettlementCycle settlementCycle;

    private double groupBudget;
    private double budgetPerDay;

    private Date tripStartDate;
    private Date tripEndDate;

    private boolean deleted;
    private Date groupDeletedDate;
}
