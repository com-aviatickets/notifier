package com.aviatickets.notifier.dto;

import com.aviatickets.notifier.model.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
    private OperationType operation;
    private UserData data;
}
