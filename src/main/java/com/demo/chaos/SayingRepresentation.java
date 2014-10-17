package com.demo.chaos;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "A SayingRepresentation is a representation of greeting")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SayingRepresentation {

    private long id;

    @ApiModelProperty(value = "greeting content", required = true)
    private String content;

    public SayingRepresentation(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}