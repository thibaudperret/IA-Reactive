package template;

import java.util.List;

import logist.plan.Action;
import logist.simulation.Vehicle;
import logist.topology.Topology.City;

public abstract class State {
    
    protected City city;
    
    public State(City city) {
        this.city = city;
    }
    
    public abstract List<Action> actions();

}
