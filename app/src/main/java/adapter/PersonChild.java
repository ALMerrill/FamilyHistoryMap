package adapter;

/**
 * Created by Andrew1 on 6/14/17.
 */

public class PersonChild {
    private int iconResource;
    private String textTop;
    private String textBottom;
    private String ID;

    public PersonChild(int iconResource, String textTop, String textBottom, String ID) {
        this.iconResource = iconResource;
        this.textTop = textTop;
        this.textBottom = textBottom;
        this.ID = ID;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public String getTextTop() {
        return textTop;
    }

    public void setTextTop(String textTop) {
        this.textTop = textTop;
    }

    public String getTextBottom() {
        return textBottom;
    }

    public void setTextBottom(String textBottom) {
        this.textBottom = textBottom;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
