package thewaypointers.trafficsimulator.common;

import thewaypointers.trafficsimulator.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Direction {
    Up,
    Down,
    Left,
    Right;

    private static final List<Pair<Direction, Direction>> oppositePairs;
    private static final Map<Direction, Direction> oppositeMap;
    static {
        oppositePairs = new ArrayList<>();
        oppositePairs.add(new Pair<>(Direction.Up, Direction.Down));
        oppositePairs.add(new Pair<>(Direction.Left, Direction.Right));

        oppositeMap = new HashMap<>();
        for(Pair<Direction, Direction> pair : oppositePairs){
            oppositeMap.put(pair.getItem1(), pair.getItem2());
            oppositeMap.put(pair.getItem2(), pair.getItem1());
        }
    }

    public boolean isVertical(){
        return this.equals(Direction.Up) || this.equals(Direction.Down);
    }

    public boolean isHorizontal(){
        return !this.isVertical();
    }

    public Direction opposite(){
        return oppositeMap.get(this);
    }
}
