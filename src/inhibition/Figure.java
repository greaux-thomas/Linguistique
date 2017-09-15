package inhibition;

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

    public Figure(double size, double location_x, double location_y, long creation, String type) {
        this.size = size;
        this.location_x = location_x;
        this.location_y = location_y;
        this.creation = creation;
        this.type = type;
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

    public void setDestruction(long destruction) {
        this.destruction = destruction;
    }
}
