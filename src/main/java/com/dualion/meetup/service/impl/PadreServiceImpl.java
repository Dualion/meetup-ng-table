package com.dualion.meetup.service.impl;

import com.dualion.meetup.service.PadreService;
import com.dualion.meetup.domain.Padre;
import com.dualion.meetup.repository.PadreRepository;
import com.dualion.meetup.service.dto.PadreDTO;
import com.dualion.meetup.service.mapper.PadreMapper;
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
 * Service Implementation for managing Padre.
 */
@Service
@Transactional
public class PadreServiceImpl implements PadreService{

    private final Logger log = LoggerFactory.getLogger(PadreServiceImpl.class);
    
    @Inject
    private PadreRepository padreRepository;

    @Inject
    private PadreMapper padreMapper;

    /**
     * Save a padre.
     *
     * @param padreDTO the entity to save
     * @return the persisted entity
     */
    public PadreDTO save(PadreDTO padreDTO) {
        log.debug("Request to save Padre : {}", padreDTO);
        Padre padre = padreMapper.padreDTOToPadre(padreDTO);
        padre = padreRepository.save(padre);
        PadreDTO result = padreMapper.padreToPadreDTO(padre);
        return result;
    }

    /**
     *  Get all the padres.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PadreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Padres");
        Page<Padre> result = padreRepository.findAll(pageable);
        return result.map(padre -> padreMapper.padreToPadreDTO(padre));
    }

    /**
     *  Get one padre by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PadreDTO findOne(Long id) {
        log.debug("Request to get Padre : {}", id);
        Padre padre = padreRepository.findOne(id);
        PadreDTO padreDTO = padreMapper.padreToPadreDTO(padre);
        return padreDTO;
    }

    /**
     *  Delete the  padre by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Padre : {}", id);
        padreRepository.delete(id);
    }
}
