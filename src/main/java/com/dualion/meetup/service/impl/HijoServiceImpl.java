package com.dualion.meetup.service.impl;

import com.dualion.meetup.service.HijoService;
import com.dualion.meetup.domain.Hijo;
import com.dualion.meetup.repository.HijoRepository;
import com.dualion.meetup.service.dto.HijoDTO;
import com.dualion.meetup.service.mapper.HijoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Hijo.
 */
@Service
@Transactional
public class HijoServiceImpl implements HijoService{

    private final Logger log = LoggerFactory.getLogger(HijoServiceImpl.class);
    
    @Inject
    private HijoRepository hijoRepository;

    @Inject
    private HijoMapper hijoMapper;

    /**
     * Save a hijo.
     *
     * @param hijoDTO the entity to save
     * @return the persisted entity
     */
    public HijoDTO save(HijoDTO hijoDTO) {
        log.debug("Request to save Hijo : {}", hijoDTO);
        Hijo hijo = hijoMapper.hijoDTOToHijo(hijoDTO);
        hijo = hijoRepository.save(hijo);
        HijoDTO result = hijoMapper.hijoToHijoDTO(hijo);
        return result;
    }

    /**
     *  Get all the hijos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<HijoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Hijos");
        Page<Hijo> result = hijoRepository.findAll(pageable);
        return result.map(hijo -> hijoMapper.hijoToHijoDTO(hijo));
    }

    /**
     *  Get one hijo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public HijoDTO findOne(Long id) {
        log.debug("Request to get Hijo : {}", id);
        Hijo hijo = hijoRepository.findOne(id);
        HijoDTO hijoDTO = hijoMapper.hijoToHijoDTO(hijo);
        return hijoDTO;
    }

    /**
     *  Delete the  hijo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Hijo : {}", id);
        hijoRepository.delete(id);
    }
}
