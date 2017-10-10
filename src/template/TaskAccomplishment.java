package template;

import java.util.Objects;

import logist.topology.Topology.City;

public class TaskAccomplishment extends Decision{

	public TaskAccomplishment(City city) {
		super(city);
	}

	@Override
	public boolean isMove() {
		return false;
	}

	@Override
	public boolean isTaskAccomplishment() {
		return true;
	}

	@Override
	public City destination() {
		// TODO Auto-generated method stub
		return super.destination;
	}
	
	@Override
	public String toString() {
		return "Deliver " + destination;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(destination, "taskAccomplishment");
	}
	
	@ Override
	public boolean equals(Object o) {
		return ((o instanceof TaskAccomplishment) && ((((TaskAccomplishment)o).destination).equals(destination)));
	} 
}
