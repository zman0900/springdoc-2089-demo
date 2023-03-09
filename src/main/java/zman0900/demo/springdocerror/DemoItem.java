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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An object for the demo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemoItem {
    /**
     * The item ID.
     */
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int id;
    /**
     * Sample string field 1.
     */
    private String field1;
    /**
     * Sample string field 2.
     */
    private String field2;
}
