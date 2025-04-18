package com.aviatickets.notifier.service;

import com.aviatickets.notifier.dto.UserData;
import com.aviatickets.notifier.dto.UserEvent;
import com.aviatickets.notifier.model.NotifierUser;
import com.aviatickets.notifier.model.OperationType;
import com.aviatickets.notifier.repository.NotifierUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserSyncServiceTest {

    @Mock
    private NotifierUserRepository repository;

    @InjectMocks
    private UserSyncService service;

    @Captor
    private ArgumentCaptor<NotifierUser> userCaptor;

    private UserEvent createUserEvent(Long id, String email, OperationType operation) {
        UserData data = new UserData();
        data.setId(id);
        data.setEmail(email);
        UserEvent event = new UserEvent();
        event.setOperation(operation);
        event.setData(data);
        return event;
    }

    @Test
    void testHandleCreateEvent_savesNewUser() {
        UserEvent event = createUserEvent(1L, "test@example.com", OperationType.CREATE);

        service.handleUserEvent(event);

        verify(repository, times(1)).save(userCaptor.capture());
        NotifierUser savedUser = userCaptor.getValue();
        assertEquals(1L, savedUser.getId());
        assertEquals("test@example.com", savedUser.getEmail());

    }

    @Test
    void testHandleUpdateEvent_userExists_updatesUser() {
        NotifierUser existingUser = new NotifierUser();
        existingUser.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));

        UserEvent event = createUserEvent(1L, "updated@example.com", OperationType.UPDATE);

        service.handleUserEvent(event);

        verify(repository, times(1)).save(userCaptor.capture());
        NotifierUser updatedUser = userCaptor.getValue();
        assertEquals("updated@example.com", updatedUser.getEmail());
    }

    @Test
    void testHandleUpdateEvent_userNotExists_doesNotSave() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        UserEvent event = createUserEvent(1L, "updated@example.com", OperationType.UPDATE);

        service.handleUserEvent(event);

        verify(repository, never()).save(any());
    }

    @Test
    void testHandleUnknownOperation_throwsException() {
        UserEvent event = createUserEvent(1L, "test@example.com", null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.handleUserEvent(event));
        assertEquals("Unknown operation: null", exception.getMessage());
    }
}
