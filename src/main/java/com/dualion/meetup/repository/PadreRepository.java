package com.dualion.meetup.repository;

import com.dualion.meetup.domain.Padre;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Padre entity.
 */
@SuppressWarnings("unused")
public interface PadreRepository extends JpaRepository<Padre,Long> {

}
