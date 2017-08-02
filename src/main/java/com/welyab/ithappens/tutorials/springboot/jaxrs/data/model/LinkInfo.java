package com.welyab.ithappens.tutorials.springboot.jaxrs.data.model;

import javax.ws.rs.core.Link;

public class LinkInfo {

    private String uri;
    private String method;
    private String rel;
    private String type;
    private String title;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public static LinkInfo create(String method, Link link) {
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setMethod(method);
        linkInfo.setRel(link.getRel());
        linkInfo.setTitle(link.getTitle());
        linkInfo.setType(link.getType());
        linkInfo.setUri(link.getUri().toString());
        return linkInfo;
    }
}
