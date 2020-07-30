package com.example.valuteconverter;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ValCurs", strict = false)
public class ValCursFeed {

    @Attribute(name = "name")
    private String name;

    @Attribute(name = "Date")
    private String date;

    @ElementList(required = false, name = "Valute", inline = true)
    private List<Valute> valuteResponses;

    public List<Valute> getValuteResponses() {
        return valuteResponses;
    }

    public void setValuteResponses(List<Valute> valuteResponses) {
        this.valuteResponses = valuteResponses;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
