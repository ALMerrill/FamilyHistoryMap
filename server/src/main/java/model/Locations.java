package model;

/**
 * Created by Andrew1 on 5/26/17.
 */

public class Locations {
    private Location[] data;

    public Locations(Location[] data) {
        this.data = data;
    }

    public Location[] getData() {
        return data;
    }

    public void setData(Location[] data) {
        this.data = data;
    }
}
