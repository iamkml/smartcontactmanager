package com.smartcontactmanager.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer>{
	@Query("FROM Contact as c WHERE c.user.uId=:userId")
	public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pePageable);
	
	public List<Contact> findBycFirstNameContainingAndUser(String name,User user);
}
