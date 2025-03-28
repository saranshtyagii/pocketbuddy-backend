package com.web.pocketbuddy.entity.document;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonalExpenseDocument {

    @Id
    private String expenseId;

    @NotNull(message = "userId can't be null")
    private String userId;

    @NotNull(message = "expenseDescription can't be null")
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

    @NotNull(message = "amount can't be null")
    private double amount;
}
