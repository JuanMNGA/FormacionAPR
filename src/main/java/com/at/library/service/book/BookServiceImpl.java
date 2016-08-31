package com.at.library.service.book;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.at.library.dao.BookDao;
import com.at.library.dto.BookDTO;
import com.at.library.dto.RentDTO;
import com.at.library.enums.StatusEnum;
import com.at.library.exception.BookNotFoundException;
import com.at.library.exception.InvalidDataException;
import com.at.library.exception.RentNotFoundException;
import com.at.library.model.Book;
import com.at.library.service.rent.RentService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookDao bookDao;

	@Autowired
	private DozerBeanMapper dozer;
	
	@Autowired
	private RentService rentService;

	@Override
	@Transactional(readOnly = true)
	public List<BookDTO> findAll() {
		final Iterable<Book> findAll = bookDao.findAll();
		final Iterator<Book> iterator = findAll.iterator();
		final List<BookDTO> res = new ArrayList<>();
		while (iterator.hasNext()) {
			final Book b = iterator.next();
			final BookDTO bDTO = transform(b);
			res.add(bDTO);
		}
		return res;
	}

	@Override
	public BookDTO transform(Book book) {
		return dozer.map(book, BookDTO.class);
	}

	@Override
	public Book transform(BookDTO book) {
		return dozer.map(book, Book.class);
	}
	
	@Override
	public List<BookDTO> transform(List<Book> book) {	
		final Iterator<Book> iterator = book.iterator();
		final List<BookDTO> res = new ArrayList<>();
		while (iterator.hasNext()) {
			final Book b = iterator.next();
			final BookDTO bDTO = transform(b);
			res.add(bDTO);
		}
		return res;
	}

	@Override
	public BookDTO create(BookDTO book) throws InvalidDataException {
		if(book == null){
			throw new InvalidDataException();
		}
		else{
			Book b = transform(book);
			b.setStartDate(new Date());
			return transform(bookDao.save(b));
		}
	}

	@Override
	public Book getById(Integer id) throws BookNotFoundException {
		Book b = bookDao.findOne(id);
		if(b == null){
			throw new BookNotFoundException();
		}
		else {
			return b;
		}
	}
	
	@Override
	public BookDTO getByIdDTO(Integer id) throws BookNotFoundException {
		Book b = bookDao.findOne(id);
		if(b == null){
			throw new BookNotFoundException();
		}
		else {
			return transform(b);
		}
	}

	@Override
	public void update(BookDTO book) throws InvalidDataException {
		if(book == null){
			throw new InvalidDataException();
		}
		else{
			Book b = transform(book);
			bookDao.save(b);
		}
	}

	@Override
	public void delete(Integer id) throws BookNotFoundException {
		Book b = bookDao.findOne(id);
		if(b == null){
			throw new BookNotFoundException();
		}
		else{
			bookDao.delete(id);
		}
	}

	@Override
	public List<BookDTO> getByParams(String name, String isbn, String author) throws BookNotFoundException {
		List<BookDTO> b = transform(bookDao.findParams(author, name, isbn));
		if(b.isEmpty())
			throw new BookNotFoundException();
		else
			return b;
	}

	@Override
	public void modifyStatus(Book b, StatusEnum s) throws BookNotFoundException {
		if(b == null){
			throw new BookNotFoundException();
		}
		else {
			b.setStatus(s);
			bookDao.save(b);
		}
	}
	
	@Override
	public boolean checkStatus(Book b){
		if(b.getStatus() == StatusEnum.OK)
			return true;
		else
			return false;
	}

	@Override
	public List<RentDTO> getRents(Integer id) throws BookNotFoundException, RentNotFoundException {
		Book b = bookDao.findOne(id);
		if(b == null){
			throw new BookNotFoundException();
		}
		else{
			List<RentDTO> r = rentService.getByBookId(id);
			return r;
		}
	}
}
