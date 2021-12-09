package com.store.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Report {

    String repUrl;
    String repNamespace;
    String repAuthor;
    String repType;
    String repService;
    String repStatus;
    String repDocCode;

}
