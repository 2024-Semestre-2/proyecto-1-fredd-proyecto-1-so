
package com.mycompany.Bslogic;

/**
 *
 * @author fredd
 */
public class Register {
    private String name;
    private String value;
    
    public Register (String name, String value) {
        this.name = name;
        this.value = value;
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
}
