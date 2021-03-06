package com.at.library.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.at.library.dto.BookDTO;
import com.at.library.model.Book;

@Repository
public interface BookDao extends CrudRepository<Book, Integer> {

	@Query(value="SELECT new com.at.library.dto.BookDTO(b.id,b.isbn,b.title,b.author) from Book as b"
			+ " where b.id in (select r.rentPK.book.id from Rent as r where r.endDate is null)")
	public List<BookDTO> findUnavailable();
	
	@Query(value = "SELECT b from Book as b where (b.author like %:author% OR :author is null) "
			+ "AND (b.title like %:title% OR :title is null) "
			+ "AND (b.isbn like %:isbn% OR :isbn is null)"
			+ "AND b.status != com.at.library.enums.StatusEnum.DELETED")
	List<Book> findParams(@Param(value="author") String author,
					   	  @Param(value="title") String title,
					   	  @Param(value="isbn") String isbn,
					   	  Pageable pageable);

}
