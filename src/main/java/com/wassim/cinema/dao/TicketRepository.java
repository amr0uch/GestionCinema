package com.wassim.cinema.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import com.wassim.cinema.entities.Ticket;


@RepositoryRestResource
public interface TicketRepository extends JpaRepository<Ticket, Long>{

}
