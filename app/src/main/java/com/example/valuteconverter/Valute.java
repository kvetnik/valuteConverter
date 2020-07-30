package com.example.valuteconverter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import java.util.Objects;

@Root(name = "Valute", strict = false)
public class Valute {

    public Valute() {
        this.numCode = "111";
        this.charCode = "RUB";
        this.nominal = "1";
        this.name = "rubl";
        this.value = "1";
    }

    @Element(name = "NumCode")
    private String numCode;

    @Element(name = "CharCode")
    private String charCode;

    @Element(name = "Nominal")
    private String nominal;

    @Element(name = "Name")
    private String name;

    @Element(name = "Value")
    private String value;

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // Если не переопределить методы equals и hashcode
    // объекты будут сравниваться по ссылке
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Valute valute = (Valute) o;
        return numCode.equals(valute.numCode) &&
                charCode.equals(valute.charCode) &&
                nominal.equals(valute.nominal) &&
                name.equals(valute.name) &&
                value.equals(valute.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCode, charCode, nominal, name, value);
    }

    public String convertTO(Valute valute, Integer num){
        Float res = num*(
                (Float.parseFloat(this.getValue().replace(",",".")) /
                        Float.parseFloat(this.nominal.replace(",",".")))
                        / (Float.parseFloat(valute.getValue().replace(",",".")) /
                        Float.parseFloat((valute.getNominal().replace(",","."))))
        );

        return res.toString();
    }

}
