package template;

import java.util.Objects;

import logist.topology.Topology.City;

public abstract class Decision {

	protected City destination;
	
	public Decision(City city) {
		this.destination = city;
	}
	
	public abstract boolean isMove();
	public abstract boolean isTaskAccomplishment();
	public abstract City destination();
	
	
	
	
	
	

}
