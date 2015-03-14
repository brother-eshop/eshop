package com.eshop.model.manager;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Province implements Serializable{
    private Long codeid;
    private Long parentid;
    private String cityName;
}
