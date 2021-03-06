package com.at.library.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.at.library.dto.BookDTO;
import com.at.library.dto.HistoryRentedDTO;
import com.at.library.exception.BookNotFoundException;
import com.at.library.exception.InvalidDataException;
import com.at.library.exception.RentNotFoundException;
import com.at.library.service.book.BookService;

@RestController
@RequestMapping(value = "/book")
public class BookController {

	@Autowired
	private BookService bookService;

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(BookController.class);
	
	@RequestMapping(method={RequestMethod.POST})
	public BookDTO create(@RequestBody BookDTO book) throws InvalidDataException {
		log.debug(String.format("Vamos a crear el libro %s", book));
		return bookService.create(book);
	}
	
	@RequestMapping(value="/{id}", method={RequestMethod.GET})
	public BookDTO get(@PathVariable("id") Integer id) throws BookNotFoundException {
		log.debug(String.format("Recuperando libros con id: %s",id));
		return bookService.getByIdDTO(id);
	}
	
	@RequestMapping(method={RequestMethod.GET})
	public List<BookDTO> get(@RequestParam(value="name",required=false) String name, 
							 @RequestParam(value="isbn",required=false) String isbn, 
							 @RequestParam(value="author",required=false) String author,
							 @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
							 @RequestParam(value = "size", required = false, defaultValue = "10") Integer size)
							 throws BookNotFoundException {
		
		log.debug(String.format("Recuperando libro con nombre: %s, isbn: %s y autor: %s",name,isbn,author));
		return bookService.getByParams(name,isbn,author, new PageRequest(page,size));
	}

	@RequestMapping(value="/{id}", method={RequestMethod.PUT})
	public void update(@PathVariable("id") Integer id, @RequestBody BookDTO book) throws InvalidDataException,BookNotFoundException {
		log.debug(String.format("Vamos a modificar el libro %s", book));
		bookService.update(book);
	}
	
	@RequestMapping(value="/{id}", method={RequestMethod.DELETE})
	public void delete(@PathVariable("id") Integer id) throws BookNotFoundException {
		log.debug(String.format("Vamos a modificar el libro con id %s", id));
		bookService.delete(id);
	}
	
	@RequestMapping(value="/{id}/rent", method={RequestMethod.GET})
	public List<HistoryRentedDTO> history(@PathVariable("id") Integer id,
				@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
				@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) 
				throws BookNotFoundException, RentNotFoundException {
		
		log.debug(String.format("Recuperando alquileres del libro con id %s", id));
		List<HistoryRentedDTO> r = bookService.getRents(id, new PageRequest(page,size));
		return r;
	}

}
