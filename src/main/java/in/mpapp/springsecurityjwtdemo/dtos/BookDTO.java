package in.mpapp.springsecurityjwtdemo.dtos;

import in.mpapp.springsecurityjwtdemo.enums.BookCategory;
import in.mpapp.springsecurityjwtdemo.enums.BookStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BookDTO extends IDedDTO {

    private String name;
    private String isbn;
    private BookCategory category;
    private BookStatus status;
}
