package com.workflow.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface congeRepository extends JpaRepository<TDemande, Long> {

	 @Query("select td from TDemande td where td.Commentaire=?1 or td.Typeconge=?2  or td.empName=?3")
	 
	   List<TDemande> findByrechercheid(String Commentaire , String Typeconge, String empName);

}
