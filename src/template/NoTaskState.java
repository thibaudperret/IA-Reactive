package template;

import java.util.ArrayList;
import java.util.List;

import logist.plan.Action;
import logist.topology.Topology.City;

public class NoTaskState extends State {
    
    public NoTaskState(City city) {
        super(city);
        initialize();
    }

  private void initialize() {
    	
        for (City neighbor: city.neighbors()) {
            doable.add(new Action.Move(neighbor));
            reachable.add(neighbor);
            
        }        
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
