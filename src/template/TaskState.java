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
    }
    
    @Override
    public List<Action> actions() {
        List<Action> actions = new ArrayList<Action>();
        for (City neighbor: city.neighbors()) {
            actions.add(new Action.Move(neighbor));
        }
        
        actions.add(new Action.Pickup(task));
        return actions;
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
