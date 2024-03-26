package in.mpapp.springsecurityjwtdemo.services.impls;

import in.mpapp.springsecurityjwtdemo.dtos.BookDTO;
import in.mpapp.springsecurityjwtdemo.entities.BookEntity;
import in.mpapp.springsecurityjwtdemo.enums.BookStatus;
import in.mpapp.springsecurityjwtdemo.repositories.BookRepository;
import in.mpapp.springsecurityjwtdemo.services.IBookService;
import in.mpapp.springsecurityjwtdemo.utils.DataMapperUtil;
import in.mpapp.springsecurityjwtdemo.utils.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DefaultBookService implements IBookService {


    @Autowired
    private BookRepository bookRepository;


    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        log.info("Creating Book with Book Details in service layer: {} ", bookDTO);
        BookEntity bookEntity = DataMapperUtil.convertTo(bookDTO, BookEntity.class);
//        bookEntity.setCreatedBy(SecurityContext.getCurrentUser());
        bookEntity.setCreatedOn(LocalDateTime.now());
        bookEntity.setCreatedBy(-999L);
        bookEntity.setStatus(BookStatus.AVAILABLE);
        bookEntity = bookRepository.save(bookEntity);
        log.info("Saved Book in service layer: {} ", bookEntity );
        return DataMapperUtil.convertTo(bookEntity, BookDTO.class);
    }

    @Override
    public List<BookDTO> getAll() {
        List<BookDTO> bookDTOS = new ArrayList<>();
        bookRepository.findAll().forEach((bookEntity) -> {
            bookDTOS.add(DataMapperUtil.convertTo(bookEntity, BookDTO.class));
        });
        return bookDTOS;
    }
}
