package in.mpapp.springsecurityjwtdemo.services;

import in.mpapp.springsecurityjwtdemo.dtos.BookDTO;

import java.util.List;

public interface IBookService {

    BookDTO createBook(final BookDTO bookDTO);
    List<BookDTO> getAll();
}
