package in.mpapp.springsecurityjwtdemo.models.responses;

import lombok.Data;

import java.time.LocalDateTime;

/*
APIs that send privately will be using this
This will be implemented by only ADIN or who has the access privately
 */
@Data
public abstract class AuditedResponse implements IResponse {

    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
