package in.mpapp.springsecurityjwtdemo.entities;

import in.mpapp.springsecurityjwtdemo.constants.EntityName;
import in.mpapp.springsecurityjwtdemo.constants.TableName;
import in.mpapp.springsecurityjwtdemo.enums.BookCategory;
import in.mpapp.springsecurityjwtdemo.enums.BookStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = EntityName.BOOK)
@Table(name = TableName.BOOK)
public class BookEntity extends IDedEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookCategory category;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatus status;
}
