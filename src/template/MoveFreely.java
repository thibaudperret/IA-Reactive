package template;

import java.util.Objects;

import logist.topology.Topology.City;

public class MoveFreely extends Decision {
		
	public MoveFreely(City city) {
		super(city);
	}

	@Override
	public boolean isMove() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isTaskAccomplishment() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public City destination() {
		// TODO Auto-generated method stub
		return super.destination;
	}
	
	@Override
	public String toString() {
		return "Mv " + destination;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(destination, "moveFreely");
	}
	
	@ Override
	public boolean equals(Object o) {
		return ((o instanceof MoveFreely) && ((((MoveFreely)o).destination).equals(destination)));
	} 
	
}
