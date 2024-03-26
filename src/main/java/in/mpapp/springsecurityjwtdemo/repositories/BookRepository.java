package in.mpapp.springsecurityjwtdemo.repositories;

import in.mpapp.springsecurityjwtdemo.entities.BookEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends IDedRepository<BookEntity> {


}
