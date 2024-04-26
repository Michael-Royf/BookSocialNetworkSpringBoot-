package com.michael.book_social_network.enumeration;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account");

    private final String name;


    EmailTemplateName(String name) {
        this.name = name;
    }
}
