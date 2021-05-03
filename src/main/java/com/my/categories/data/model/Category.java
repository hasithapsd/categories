package com.my.categories.data.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "categories")
@Getter
@Setter
@Builder
public class Category {

    @Id
    private String id;

    private String title;
    private String name;
    private String description;

    private List<Attribute> attributes;
}
