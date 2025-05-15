package com.web.pocketbuddy.payload;

import com.web.pocketbuddy.entity.helper.GroupExpenseMetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegisterGroupExpense {

    private String groupId;
    private String description;
    private Double amount;

    private String registerByUserId;

    private Map<String , Double> includedMembers;

}
