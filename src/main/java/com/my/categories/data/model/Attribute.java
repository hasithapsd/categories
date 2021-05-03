package com.my.categories.data.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
public class Attribute {

    @Id
    private String id;

    private String name;
    private String description;
    private String type;
    private String value;
}
