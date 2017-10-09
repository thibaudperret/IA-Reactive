package template;

import java.util.ArrayList;
import java.util.List;

import logist.plan.Action;
import logist.topology.Topology.City;

public class NoTaskState extends State {
    
    public NoTaskState(City city) {
        super(city);
    }

    @Override
    public List<Action> actions() {
        List<Action> actions = new ArrayList<Action>();
        for (City neighbor: city.neighbors()) {
            actions.add(new Action.Move(neighbor));
        }
        
        return actions;
    }

	@Override
	public boolean isTaskState() {
		return false;
	}

	@Override
	public boolean isNoTaskState() {
		return true;
	}

}
