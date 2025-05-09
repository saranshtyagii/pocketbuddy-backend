package com.web.pocketbuddy.entity.document;

import com.web.pocketbuddy.entity.helper.GroupExpenseMetaData;
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
public class GroupExpenseDocument {

    @Id
    private String expenseId;

    @NotNull
    private String groupId;

    private String expenseDescription;

    @NotNull
    private double expenseAmount;

    private Map<String, GroupExpenseMetaData> includedMembers;

    @NotNull
    private String registerByUserId;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date updatedDate;

    private boolean isUpdated;

    private boolean isDeleted;
}
