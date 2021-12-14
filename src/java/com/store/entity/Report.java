package com.store.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Report {

    String repUrl;
    String repNamespace;
    String repAuthor;
    String repType;
    String repService;
    String repStatus;
    String repDocCode;

}
