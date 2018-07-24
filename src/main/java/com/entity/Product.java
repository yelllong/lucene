package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: lenovo
 * \* Date: 2018/7/23
 * \* Time: 13:47
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Product {
    private Integer id;
    private String title;
    private Double price;
    private Date date;
    private String description;
    private String picPath;
}

