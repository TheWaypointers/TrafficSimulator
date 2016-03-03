package thewaypointers.trafficsimulator.utils;

import sun.font.StrikeCache;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class FloatPoint {
    private float x;
    private float y;

    public FloatPoint() { this(0,0); }

    public FloatPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public FloatPoint rotate90(FloatPoint center, Rotation rotation)
    {
        FloatPoint n = this.subtract(center);
        FloatPoint rotated = rotation == Rotation.Clockwise?
                             new FloatPoint(n.getY(), -n.getX()) :
                             new FloatPoint(-n.getY(), n.getX());
        return rotated.add(center);
    }

    public FloatPoint mirror(FloatPoint center){
        return this
                .rotate90(center, Rotation.Clockwise)
                .rotate90(center, Rotation.Clockwise);
    }

    public boolean equals(FloatPoint other){
        return  getX() == other.getX() &&
                getY() == other.getY();
    }

    @Override
    public boolean equals(Object o) {
        return  o.getClass()==FloatPoint.class &&
                equals((FloatPoint)o);
    }

    public FloatPoint add(FloatPoint other){
        return new FloatPoint(getX()+other.getX(),
                              getY()+other.getY());
    }

    public FloatPoint subtract(FloatPoint other){
        return new FloatPoint(getX()-other.getX(),
                              getY()-other.getY());
    }

    @Override
    public String toString() {
        return String.format("Point(%f,%f)", getX(), getY());
    }
}
