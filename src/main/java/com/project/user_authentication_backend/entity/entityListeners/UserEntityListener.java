package com.project.user_authentication_backend.entity.entityListeners;


import com.project.user_authentication_backend.config.WebConfig;
import com.project.user_authentication_backend.entity.User;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.Instant;

@Component
@AllArgsConstructor
public class UserEntityListener {

    private final WebConfig webConfig;

    @PrePersist
    public void prePersist(User user) {
        Timestamp now = Timestamp.from(Instant.now());
        user.setCreatedAt(now);
        user.setModifiedAt(now);
        // Set createdBy and modifiedBy if you have a way to fetch the current user's information
        String userName = webConfig.getCurrentUsername();
        if (!userName.equals( "anonymousUser")) {
            user.setCreatedBy(userName);
            user.setModifiedBy(userName);
        } else{
            user.setCreatedBy(user.getUserName());
            user.setModifiedBy(user.getUserName());
        }
    }

    @PreUpdate
    public void preUpdate(User user) {
        user.setModifiedAt(Timestamp.from(Instant.now()));
        // Update modifiedBy if you have a way to fetch the current user's information
        String userName = webConfig.getCurrentUsername();
        if (!userName.equals( "anonymousUser")) {
            if(user.getUserName()!=null){
                user.setModifiedBy(user.getUserName());
            }
            user.setModifiedBy(userName);
        } else{
            user.setModifiedBy(user.getUserName());
        }
    }
}

