package com.dualion.meetup.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dualion.meetup.service.HijoService;
import com.dualion.meetup.web.rest.util.HeaderUtil;
import com.dualion.meetup.web.rest.util.PaginationUtil;
import com.dualion.meetup.service.dto.HijoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Hijo.
 */
@RestController
@RequestMapping("/api")
public class HijoResource {

    private final Logger log = LoggerFactory.getLogger(HijoResource.class);
        
    @Inject
    private HijoService hijoService;

    /**
     * POST  /hijos : Create a new hijo.
     *
     * @param hijoDTO the hijoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hijoDTO, or with status 400 (Bad Request) if the hijo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hijos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HijoDTO> createHijo(@RequestBody HijoDTO hijoDTO) throws URISyntaxException {
        log.debug("REST request to save Hijo : {}", hijoDTO);
        if (hijoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hijo", "idexists", "A new hijo cannot already have an ID")).body(null);
        }
        HijoDTO result = hijoService.save(hijoDTO);
        return ResponseEntity.created(new URI("/api/hijos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hijo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hijos : Updates an existing hijo.
     *
     * @param hijoDTO the hijoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hijoDTO,
     * or with status 400 (Bad Request) if the hijoDTO is not valid,
     * or with status 500 (Internal Server Error) if the hijoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hijos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HijoDTO> updateHijo(@RequestBody HijoDTO hijoDTO) throws URISyntaxException {
        log.debug("REST request to update Hijo : {}", hijoDTO);
        if (hijoDTO.getId() == null) {
            return createHijo(hijoDTO);
        }
        HijoDTO result = hijoService.save(hijoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hijo", hijoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hijos : get all the hijos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of hijos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/hijos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<HijoDTO>> getAllHijos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Hijos");
        Page<HijoDTO> page = hijoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hijos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hijos/:id : get the "id" hijo.
     *
     * @param id the id of the hijoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hijoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/hijos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HijoDTO> getHijo(@PathVariable Long id) {
        log.debug("REST request to get Hijo : {}", id);
        HijoDTO hijoDTO = hijoService.findOne(id);
        return Optional.ofNullable(hijoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hijos/:id : delete the "id" hijo.
     *
     * @param id the id of the hijoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/hijos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHijo(@PathVariable Long id) {
        log.debug("REST request to delete Hijo : {}", id);
        hijoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hijo", id.toString())).build();
    }

}
