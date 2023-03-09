/*
 * Copyright (c) 2023 OCLC, Inc. All Rights Reserved.
 * OCLC proprietary information: the enclosed materials contain
 * proprietary information of OCLC Online Computer Library Center,
 * Inc. and shall not be disclosed in whole or in any part to any
 * third party or used by any person for any purpose, without written
 * consent of OCLC, Inc.  Duplication of any portion of these
 * materials shall include this notice.
 */

package zman0900.demo.springdocerror;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * The Demo Controller.
 */
@RestController
@RequestMapping(value = "/demo", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemoController {
    private final ObjectMapper objectMapper;


    @Autowired
    public DemoController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    /**
     * Update an existing DemoItem.
     *
     * @param id     the item ID
     * @param update updated DemoItem (maybe partial) (this would be response body doc without swagger @RequestBody)
     * @return result with the updated DemoItem
     */
    @PutMapping("/{id}")
    public ResponseEntity<DemoItem> update(
            @PathVariable final int id,
            // Adding this swagger RequestBody annotation without description is what triggers bug
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    // Without description here, Swagger UI fails to load (this works fine on 1.6.12)
                    // On 1.6.12, this works fine and uses description from javadoc @param instead
                    content = @Content(schema = @Schema(implementation = DemoItem.class))
            )
            @RequestBody final JsonNode update
    ) {
        return updateInternal(id, update);
    }

    /**
     * Update an existing DemoItem (no swagger @ResponseBody).
     *
     * @param id     the item ID
     * @param update updated DemoItem (maybe partial)
     * @return result with the updated DemoItem
     */
    @PutMapping("/alt/{id}")
    public ResponseEntity<DemoItem> updateAlt(
            @PathVariable final int id,
            // This shows up with wrong schema without swagger @ResponseBody
            @RequestBody final JsonNode update
    ) {
        return updateInternal(id, update);
    }

    /**
     * Retrieve the DemoItem with ID.
     *
     * @param id the ID
     * @return the DemoItem
     */
    @GetMapping("/{id}")
    public DemoItem retrieve(@PathVariable final int id) {
        // fake retrieving some object
        return new DemoItem(id, "demo 1", "demo 2");
    }

    /**
     * Create a DemoItem with given values.
     *
     * @param value1 the first value
     * @param value2 the second value
     * @return result with created DemoItem
     */
    @PostMapping
    public ResponseEntity<DemoItem> create(final String value1, final String value2) {
        if ("fail".equals(value1)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new DemoItem(42, value1, value2));
    }


    private ResponseEntity<DemoItem> updateInternal(final int id, final JsonNode update) {
        // fake retrieving object with ID
        final DemoItem orig = new DemoItem(id, "orig 1", "orig 2");

        try {
            // Update it based on fields in 'update', pretend result saved to DB
            return ResponseEntity.ok(objectMapper.readerForUpdating(orig).readValue(update));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
