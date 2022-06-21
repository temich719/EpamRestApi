package com.epam.esm.audit;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
@Component
@Data
public class Audit {

    @Column
    private LocalDateTime creationEntityDate;

    @Column
    private LocalDateTime modificationEntityDate;

    @Column
    private String createdEntityBy;

    @Column
    private String modifiedEntityBy;

    @PrePersist
    public void onPrePersist() {
        if (this.getCreationEntityDate() == null) {
            this.setCreationEntityDate(LocalDateTime.now());
            this.setCreatedEntityBy("Artem");
        }
    }

    @PreUpdate
    public void onPreUpdate() {
        this.setModificationEntityDate(LocalDateTime.now());
        this.setModifiedEntityBy("Artem");
    }
}
