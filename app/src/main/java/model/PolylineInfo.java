package model;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Andrew1 on 6/13/17.
 */

public class PolylineInfo {
    private int width;
    private int color;
    private LatLng selectedLatLng;
    private LatLng otherLatLng;

    public PolylineInfo(int width, int color, LatLng selectedLatLng, LatLng otherLatLng) {
        this.width = width;
        this.color = color;
        this.selectedLatLng = selectedLatLng;
        this.otherLatLng = otherLatLng;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public LatLng getSelectedLatLng() {
        return selectedLatLng;
    }

    public void setSelectedLatLng(LatLng selectedLatLng) {
        this.selectedLatLng = selectedLatLng;
    }

    public LatLng getOtherLatLng() {
        return otherLatLng;
    }

    public void setOtherLatLng(LatLng otherLatLng) {
        this.otherLatLng = otherLatLng;
    }
}
