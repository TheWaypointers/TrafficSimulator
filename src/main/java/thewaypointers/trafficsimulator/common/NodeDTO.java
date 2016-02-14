package thewaypointers.trafficsimulator.common;

public abstract class NodeDTO {
    private String label;

    protected NodeDTO(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public boolean equals(NodeDTO other){
        return getLabel().equals(other.getLabel());
    }

    @Override
    public boolean equals(Object o) {
        return equals((NodeDTO)o);
    }
}
