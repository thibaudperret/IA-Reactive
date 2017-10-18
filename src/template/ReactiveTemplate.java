package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import logist.agent.Agent;
import logist.behavior.ReactiveBehavior;
import logist.plan.Action;
import logist.plan.Action.Move;
import logist.plan.Action.Pickup;
import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskDistribution;
import logist.topology.Topology;
import logist.topology.Topology.City;

public class ReactiveTemplate implements ReactiveBehavior {

	private Random random;
	private double pPickup;
	private int numActions;
	private Agent myAgent;

	Map<State, Map<Decision, Double>> Q = new HashMap<State, Map<Decision, Double>>();
	Map<State, Map<Decision, Double>> R = new HashMap<State, Map<Decision, Double>>();
	Map<State, Map<Decision, Map<State, Double>>> T = new HashMap<State, Map<Decision, Map<State, Double>>>();
	Map<State, Decision> best = new HashMap<State, Decision>();
	Map<State, Double> V = new HashMap<State, Double>();
	List<State> states = new ArrayList<State>();
	
	@Override
	public void setup(Topology topology, TaskDistribution td, Agent agent) {
		// Reads the discount factor from the agents.xml file.
		// If the property is not present it defaults to 0.95
		Double discount = agent.readProperty("discount-factor", Double.class, 0.95);

		this.random = new Random();
		this.pPickup = discount;
		this.numActions = 0;
		this.myAgent = agent;


		// State creation
		for (City from : topology.cities()) {
			for (City to : topology.cities()) {
				if ((!to.equals(from)) && (td.probability(from, to) != 0)) {
					states.add(new TaskState(from, to));
				}
			}
			states.add(new NoTaskState(from));
		}

		// Reward definition
		for (State s : states) {
			Map<Decision, Double> tmp = new HashMap<Decision, Double>();
			City from = s.city;
			for (Decision d : s.doable) {

				if (d.isMove()) {
					tmp.put(d, -agent.vehicles().get(0).costPerKm() * from.distanceTo(d.destination));
				} else {
					double reward = td.reward(s.city, d.destination)
							- (agent.vehicles().get(0).costPerKm() * from.distanceTo(d.destination));
					tmp.put(d, reward);
				}

			}
			R.put(s, new HashMap<Decision, Double>(tmp));
		}
		
		//Fill transition table

		for (State s : states) {
			System.out.println(s);
			Map<Decision, Map<State, Double>> dtemp = new HashMap<Decision, Map<State, Double>>();
			for (Decision d : s.doable) {
				Map<State, Double> s1tmp = new HashMap<State, Double>();
				for (State s1 : states) {
					System.out.println("\t" + d + " " + s1 + ": " + transition(s, d, s1, td));
					s1tmp.put(s1, transition(s, d, s1, td));
				}
				dtemp.put(d, new HashMap<State, Double>(s1tmp));
			}
			T.put(s, new HashMap<Decision, Map<State, Double>>(dtemp));
		}

		// V initialization

		for (State s : states) {
			V.put(s, 0.0);
		}

		// V-optimization
			
		
		
		double delta = 10;
		
		while(delta > 0.0001) {
			Map<State, Double> Vprev = new HashMap<State, Double>(V);
			
			for(State s : states) {
				Map<Decision, Double> Qtmp = new HashMap<Decision, Double>(); 
				Decision bestDecision = null;
				Double maxQValue = Double.NEGATIVE_INFINITY;
				for(Decision d : s.doable) {
					double sum = 0;
					for(State s1: states) {						
						sum += T.get(s).get(d).get(s1) * Vprev.get(s1);						
					}
					sum = sum * discount + R.get(s).get(d);
					if(sum > maxQValue) {
						maxQValue = sum;
						bestDecision = d;
					}
					Qtmp.put(d, sum);
				}
				best.put(s, bestDecision);
				V.put(s, maxQValue);
				Q.put(s, Qtmp);
				

			}
			
			delta = diff(states, Vprev, V);
			System.out.println(delta);
		}
	}
	
	private double diff(List<State> states, Map<State, Double> old, Map<State, Double> updated) {
		double a = 0.0;
		
		for (State s : states) {
			a += Math.abs(old.get(s) - updated.get(s));
		}
		
		return a;
	}

	private double transition(State current, Decision d, State future, TaskDistribution td) {
		if (!d.destination.equals(future.city)) {
			return 0;
		}

		if (future.isNoTaskState()) {
			return td.probability(future.city, null);
		} else {
			return td.probability(future.city, ((TaskState) future).taskDestination);
		}
	}

	@Override
	public Action act(Vehicle vehicle, Task availableTask) {
		 Action action;
		 City currentCity = vehicle.getCurrentCity();		 
		
		if (availableTask == null) {
			State current = new NoTaskState(currentCity);
			City toReach = best.get(current).destination;
			action = new Move(toReach);
		} else {
			State current = new TaskState(currentCity, availableTask.deliveryCity);
			Decision bestDecision = best.get(current);
			if(bestDecision.isMove()) {
				City toReach = bestDecision.destination;
				action = new Move(toReach);
			} else {				
				action = new Pickup(availableTask);
			}
		}

		if (numActions >= 1) {
			System.out.println("The total profit after " + numActions + " actions is " + myAgent.getTotalProfit()
					+ " (average profit: " + (myAgent.getTotalProfit() / (double) numActions) + ")");
		}
		numActions++;

		return action;
	}
}
