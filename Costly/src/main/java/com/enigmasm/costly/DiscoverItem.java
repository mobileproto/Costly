package com.enigmasm.costly;

/**
 * Created by lego6245 on 12/12/13.
 */
public class DiscoverItem {

    private String name;
    private String conversion;

    private DiscoverItem() { }

    DiscoverItem(String name, String conversion) {
        this.name = name;
        this.conversion = conversion;
    }

    public String getName() {
        return name;
    }

    public String getConversion() {
        return conversion;
    }


}
