package thewaypointers.trafficsimulator.common;

public class RoadDTO {
    private float length;
    private NodeDTO from;
    private NodeDTO to;
    private String label;

    public RoadDTO(){}

    public RoadDTO(NodeDTO from, NodeDTO to, float length) {
        initialize(from, to, length);
    }

    public RoadDTO(RoadDTO other){
        initialize(other.getFrom(), other.getTo(), other.getLength());
    }

    private void initialize(NodeDTO start, NodeDTO end, float length){
        setLength(length);
        setFrom(start);
        setTo(end);
    }

    public float getLength() {
        return length;
    }

    public NodeDTO getFrom() {
        return from;
    }

    public NodeDTO getTo() {
        return to;
    }

    void setLength(float length) {
        this.length = length;
    }

    void setFrom(NodeDTO from) {
        this.from = from;
    }

    void setTo(NodeDTO to) {
        this.to = to;
    }

    public RoadDTO changeLength(float length){
        RoadDTO ret = new RoadDTO(this);
        ret.setLength(length);
        return ret;
    }

    public RoadDTO changeFrom(NodeDTO from){
        RoadDTO ret = new RoadDTO(this);
        ret.setFrom(from);
        return ret;
    }

    public RoadDTO changeTo(NodeDTO to){
        RoadDTO ret = new RoadDTO(this);
        ret.setTo(to);
        return ret;
    }

    public NodeDTO getOtherEnd(NodeDTO oneEnd){
        if(!isConnectedTo(oneEnd)){
            throw new IllegalArgumentException("Road is not connected to this node!");
        }

        if (getFrom().equals(oneEnd)){
            return getTo();
        }else{
            return getFrom();
        }
    }

    void setOtherEnd(NodeDTO oneEnd, NodeDTO value){
        if(!isConnectedTo(oneEnd)){
            throw new IllegalArgumentException("Road is not connected to this node!");
        }

        if (getFrom().equals(oneEnd)){
            setTo(value);
        }else{
            setFrom(value);
        }
    }

    public NodeDTO getEnd(String label){
        if(getFrom().getLabel().equals(label)){
            return getFrom();
        }
        else{
            return getTo();
        }
    }

    /**
     * Replaces one end of the road with the specified node.
     * @param end the end of the road to replace (current value)
     * @param value the value to replace with (new value)
     */
    void setEnd(NodeDTO end, NodeDTO value){
        if(!isConnectedTo(end)){
            throw new IllegalArgumentException("Road is not connected to this node!");
        }

        if (getFrom().equals(end)){
            setFrom(value);
        }else{
            setTo(value);
        }
    }

    public boolean isConnectedTo(NodeDTO node){
        return getFrom().equals(node) || getTo().equals(node);
    }

    public boolean equals(RoadDTO other) {
        return  other!=null &&
                isConnectedTo(other.getFrom()) &&
                isConnectedTo(other.getTo()) &&
                getLength() == other.getLength();
    }

    @Override
    public boolean equals(Object o) {
        return equals((RoadDTO) o);
    }

    public String getLabel() {
        return getFrom().getLabel() + getTo().getLabel();
    }

}
