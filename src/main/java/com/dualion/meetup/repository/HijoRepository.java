package com.dualion.meetup.repository;

import com.dualion.meetup.domain.Hijo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Hijo entity.
 */
@SuppressWarnings("unused")
public interface HijoRepository extends JpaRepository<Hijo,Long> {

}
