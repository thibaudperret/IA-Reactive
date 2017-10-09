package template;

import java.util.ArrayList;
import java.util.List;

import logist.plan.Action;
import logist.task.Task;
import logist.topology.Topology.City;

public class TaskState extends State {
    
    private Task task;
    
    public TaskState(City city, Task task) {
        super(city);        
        this.task = task;
        initialize();
        
    }
    
    private void initialize() {
    	
        for (City neighbor: city.neighbors()) {
            doable.add(new Action.Move(neighbor));
            reachable.add(neighbor);
            
        }        
        doable.add(new Action.Pickup(task));
        reachable.add(task.deliveryCity);
    }
    

	@Override
	public boolean isTaskState() {
		return true;
	}

	@Override
	public boolean isNoTaskState() {
		return false;
	}

    
}
