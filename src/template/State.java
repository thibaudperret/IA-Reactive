package template;

import java.util.ArrayList;
import java.util.List;

import logist.topology.Topology.City;

public abstract class State {
    
    protected City city;
    
    public List<Decision> doable;
    
    public State(City city) {
        this.city = city;
        doable = new ArrayList<Decision>();
    }
    
    
    
    public abstract boolean isTaskState();
    public abstract boolean isNoTaskState();
    
    public City currentCity() {
    	return city;
    }
   


}
