package net.metrosystems.adaptr.service;

import java.util.HashSet;

public interface RoutingInformation {

	HashSet<String> getRoutingInfo();

	HashSet<String> getRoutingInfo(int seed);
}
