package inhibition;

import java.awt.*;

/**
 * This class represents a figure on the tests
 */
public class Figure {
    private double size;
    private double location_x;
    private double location_y;
    private long creation;
    private long destruction;
    private String type;

    public Figure(double size, double location_x, double location_y, String type) {
        this.size = size;
        this.location_x = location_x;
        this.location_y = location_y;
        this.type = type;
    }

    public boolean collide(Figure f){
        int p1X = (int) (location_x+(size/2));
        int p1Y = (int) (location_y+(size/2));
        Point p1 = new Point(p1X, p1Y);

        int p2X = (int) (f.getLocation_x()+(f.getSize()/2));
        int p2Y = (int) (f.getLocation_y()+(f.getSize()/2));
        Point p2 = new Point(p2X, p2Y);

        int r = (int) ((size/2) + (f.getSize()/2));

        boolean axis_x = Math.abs(p1.x - p2.x) < r;
        boolean axis_y = Math.abs(p1.y - p2.y) < r;

        return (axis_x && axis_y);
    }

    public long lifespan(){
        return (destruction - creation);
    }

    public double getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public double getLocation_x() {
        return location_x;
    }

    public double getLocation_y() {
        return location_y;
    }

    public void setCreation(long creation) {
        this.creation = creation;
    }

    public void setDestruction(long destruction) {
        this.destruction = destruction;
    }
}
