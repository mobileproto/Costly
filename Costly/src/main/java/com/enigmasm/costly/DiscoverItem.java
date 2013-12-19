package com.enigmasm.costly;

/**
 * Created by lego6245 on 12/12/13.
 */
public class DiscoverItem {

    private String name;
    private String conversion;
    private String uid;

    private DiscoverItem() { }

    DiscoverItem(String name, String conversion, String uid) {
        this.name = name;
        this.conversion = conversion;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getConversion() {
        return conversion;
    }

    public String getUid() { return uid; }


}
