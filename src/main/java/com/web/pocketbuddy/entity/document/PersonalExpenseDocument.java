package com.web.pocketbuddy.entity.document;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EnableMongoAuditing
public class PersonalExpenseDocument {

    @Id
    private String expenseId;

    private String userId;

    @NotNull
    private String expenseDescription;

    @CreatedDate
    private Date expenseDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    private boolean isUpdated;
    private boolean isDeleted;

    private boolean isExpenseFromGroup;
    private String groupId;
    private String groupExpenseId;

    @NotNull
    private double amount;
}
