package template;

import java.util.Objects;

import logist.topology.Topology.City;

public class TaskState extends State {
    
	protected City taskDestination;
	
    public TaskState(City city, City c) {
        super(city);        
        this.taskDestination = c;
        initialize();
        
    }
    
    private void initialize() {
    	
        for (City neighbor: city.neighbors()) {
            doable.add(new MoveFreely(neighbor));
            
        }        
        doable.add(new TaskAccomplishment(taskDestination));
    }
    
    @Override
    public String toString() {
    	return city + " -> " + taskDestination;
    }

	@Override
	public boolean isTaskState() {
		return true;
	}

	@Override
	public boolean isNoTaskState() {
		return false;
	}
	
	  @Override
	    public boolean equals(Object o) {
	    	if(! (o instanceof TaskState)) {
	    		return false;
	    	} else {
	    		return (((TaskState)o).currentCity().equals(city) && (((TaskState)o).taskDestination.equals(taskDestination)));
	    	}
	    	
	    }
	  
	  @Override
	  public int hashCode() {
		  return Objects.hash(city, taskDestination);
	  }
	    

    
}
