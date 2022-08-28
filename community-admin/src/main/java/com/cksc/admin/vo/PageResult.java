package com.cksc.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * 分页泛型
 * @param <T>
 */

@Data
public class PageResult<T> {

    private List<T> list;

    private int total;
}
