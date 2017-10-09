package template;

import java.util.ArrayList;
import java.util.HashMap;
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
    

    @Override
    public void setup(Topology topology, TaskDistribution td, Agent agent) {
        // Reads the discount factor from the agents.xml file.
        // If the property is not present it defaults to 0.95
        Double discount = agent.readProperty("discount-factor", Double.class, 0.95);

        this.random = new Random();
        this.pPickup = discount;
        this.numActions = 0;
        this.myAgent = agent;
        
        Map<State, Map<Action, Double>> Q = new HashMap<State, Map<Action, Double>>(); 
        Map<State, Map<Action, Double>> R = new HashMap<State, Map<Action, Double>>();
        Map<State, Action> best = new HashMap<State, Action>();
        Map<State, Double> V = new HashMap<State, Double>();
        ArrayList<State> states = new ArrayList<State>();

        
        
        //State creation
        for(City from: topology.cities()) {
        	for(City to: topology.cities()) {
            	if( (!to.equals(from)) && (td.probability(from, to) != 0)) {
            		states.add(new TaskState(from, new Task(0, from, to, td.reward(from, to), td.weight(from, to))));
            	}
        	}
        	states.add(new NoTaskState(from));
        }
        
        
        //Reward definition
        for (State s: states) {
        	Map<Action, Double> tmp = new HashMap<Action, Double>();
        	City from = s.city;
        	for (int i = 0; i < s.doable.size(); ++i) {

            	City to = s.reachable.get(i);
    			double reward = td.reward(s.city, s.reachable.get(i)) - (agent.vehicles().get(0).costPerKm() * from.distanceTo(to));
        		tmp.put(s.doable.get(i), reward);
    		}
        	R.put(s, new HashMap<Action, Double>(tmp));
        }
        
        
        //V initialization
        
        for(State s: states) {
        	V.put(s, 0.0);
        }
        
        
        //V-optimization
        double deltaV = 1000;
       
        while (deltaV > 0.001) {
        	deltaV = 0;
        	for (State s: states) {
            	
        		Map<Action, Double> tmp = new HashMap<Action, Double>();
        		double maxQValue = 0;
        		Action bestAction = null;
        		for (int i = 0; i < s.doable.size(); ++i) {
                	
        			City to = s.reachable.get(i);
                	Action a = s.doable.get(i);
                	double qValue = 0;
                	
                	for(State s1: states) {
                		qValue += transition(s, a, s1, to, td) * V.get(s1);
                	}
                	
                	qValue += R.get(s).get(a);                	
        			tmp.put(a, qValue);
        			
        			if(qValue > maxQValue) {
        				maxQValue = qValue;
        				bestAction = a;
        			}
        			
        			deltaV += Math.abs(V.get(s) - maxQValue);
        		}
        		
        		best.put(s, bestAction);
        		Q.put(s, tmp);
        		V.put(s, maxQValue);
        	}
        }
        
        
        
        
        
    }
    
    
    private double transition(State current, Action a, State future, City destination, TaskDistribution td) {
    	if(destination != future.city) {
    		return 0;
    	}
    	
    	if(current.isNoTaskState()) {
    		return 1 - td.probability(future.city, null);
    	} else {
    		return td.probability(future.city, null);
    	}
    }

    @Override
    public Action act(Vehicle vehicle, Task availableTask) {
        Action action;

        if (availableTask == null || random.nextDouble() > pPickup) {
            City currentCity = vehicle.getCurrentCity();
            action = new Move(currentCity.randomNeighbor(random));
        } else {
            action = new Pickup(availableTask);
        }

        if (numActions >= 1) {
            System.out.println("The total profit after " + numActions + " actions is " + myAgent.getTotalProfit() + " (average profit: " + (myAgent.getTotalProfit() / (double) numActions) + ")");
        }
        numActions++;

        return action;
    }
}
