package template;

import java.util.Objects;

import logist.topology.Topology.City;

public class NoTaskState extends State {
    
    public NoTaskState(City city) {
        super(city);
        initialize();
    }

  private void initialize() {
    	
        for (City neighbor: city.neighbors()) {
            
            doable.add(new MoveFreely(neighbor));
            
        }        
  }

  	@Override
  	public String toString() {
  		return city.toString();
  	}
  
	@Override
	public boolean isTaskState() {
		return false;
	}

	@Override
	public boolean isNoTaskState() {
		return true;
	}
	
	@Override
    public boolean equals(Object o) {
    	if(! (o instanceof NoTaskState)) {
    		return false;
    	} else {
    		return (((NoTaskState)o).currentCity().equals(city) );
    	}
    	
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(city);
	}

}
