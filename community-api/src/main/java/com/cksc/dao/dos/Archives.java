package com.cksc.dao.dos;

import lombok.Data;

/**
 * 文章归档
 */

@Data
public class Archives {

    private Integer year;

    private Integer month;

    private Long count;
}
