package com.roboapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Robot {
	double charge;
	double mincharge;
	double maxWeight;
	double maxDistanceCoveredPerCharge;
	int totalDistanceCovenerd;
}
