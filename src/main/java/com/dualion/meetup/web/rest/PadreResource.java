package com.dualion.meetup.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dualion.meetup.service.PadreService;
import com.dualion.meetup.web.rest.util.HeaderUtil;
import com.dualion.meetup.web.rest.util.PaginationUtil;
import com.dualion.meetup.service.dto.PadreDTO;
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
 * REST controller for managing Padre.
 */
@RestController
@RequestMapping("/api")
public class PadreResource {

    private final Logger log = LoggerFactory.getLogger(PadreResource.class);
        
    @Inject
    private PadreService padreService;

    /**
     * POST  /padres : Create a new padre.
     *
     * @param padreDTO the padreDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new padreDTO, or with status 400 (Bad Request) if the padre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/padres",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PadreDTO> createPadre(@RequestBody PadreDTO padreDTO) throws URISyntaxException {
        log.debug("REST request to save Padre : {}", padreDTO);
        if (padreDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("padre", "idexists", "A new padre cannot already have an ID")).body(null);
        }
        PadreDTO result = padreService.save(padreDTO);
        return ResponseEntity.created(new URI("/api/padres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("padre", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /padres : Updates an existing padre.
     *
     * @param padreDTO the padreDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated padreDTO,
     * or with status 400 (Bad Request) if the padreDTO is not valid,
     * or with status 500 (Internal Server Error) if the padreDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/padres",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PadreDTO> updatePadre(@RequestBody PadreDTO padreDTO) throws URISyntaxException {
        log.debug("REST request to update Padre : {}", padreDTO);
        if (padreDTO.getId() == null) {
            return createPadre(padreDTO);
        }
        PadreDTO result = padreService.save(padreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("padre", padreDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /padres : get all the padres.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of padres in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/padres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PadreDTO>> getAllPadres(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Padres");
        Page<PadreDTO> page = padreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/padres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /padres/:id : get the "id" padre.
     *
     * @param id the id of the padreDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the padreDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/padres/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PadreDTO> getPadre(@PathVariable Long id) {
        log.debug("REST request to get Padre : {}", id);
        PadreDTO padreDTO = padreService.findOne(id);
        return Optional.ofNullable(padreDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /padres/:id : delete the "id" padre.
     *
     * @param id the id of the padreDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/padres/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePadre(@PathVariable Long id) {
        log.debug("REST request to delete Padre : {}", id);
        padreService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("padre", id.toString())).build();
    }

}
