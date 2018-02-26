package net.metrosystems.adaptr.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

@Service
public class RoutingInformationService implements RoutingInformation {

	private HashMap<Integer, HashSet<String>> routes;

	public RoutingInformationService() {
		init();
	}

	private void init() {
		routes = new HashMap<>();
		HashSet<String> temp = new HashSet<>();
		temp.add("adaptr-target_11");
		temp.add("adaptr-target_12");
		routes.put(1, temp);

		temp = new HashSet<>();
		temp.add("adaptr-target_21");
		temp.add("adaptr-target_22");
		routes.put(2, temp);

		temp = new HashSet<>();
		temp.add("adaptr-target_31");
		temp.add("adaptr-target_32");
		routes.put(3, temp);

		temp = new HashSet<>();
		temp.add("adaptr-target_41");
		temp.add("adaptr-target_42");
		routes.put(4, temp);
	}


	@Override
	public HashSet<String> getRoutingInfo() {
		return routes.get(new Random().nextInt(routes.size()));
	}

	@Override
	public HashSet<String> getRoutingInfo(int seed) {
		return routes.get(seed);
	}
}
