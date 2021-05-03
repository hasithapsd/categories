package com.my.categories.data.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "items")
@Getter
@Setter
@Builder
public class Item {
    @Id
    private String id;

    private String categoryId;

    private List<Attribute> attributes;

}
