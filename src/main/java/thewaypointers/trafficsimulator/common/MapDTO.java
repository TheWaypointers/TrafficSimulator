package thewaypointers.trafficsimulator.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapDTO {
    public List<JunctionDTO> junctions;

    public MapDTO(JunctionDTO... junctions){
        initialize(Arrays.asList(junctions));
    }
    public MapDTO(List<JunctionDTO> junctions) {
        initialize(junctions);
    }

    public void initialize(List<JunctionDTO> junctions)
    {
        this.junctions = junctions;
    }
}
