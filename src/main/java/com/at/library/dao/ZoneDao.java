package com.at.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.at.library.model.Zone;

@Repository
public interface ZoneDao extends CrudRepository<Zone, Integer> {

}
