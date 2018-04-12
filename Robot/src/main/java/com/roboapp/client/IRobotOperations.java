package com.roboapp.client;

import java.util.HashMap;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.roboapp.model.Product;
import com.roboapp.model.Robot;

public interface IRobotOperations {
	Robot walk(Robot robot,double weight,double totalDist);
	boolean chargeCheck(Robot robot);
	String addProducts(Product product);
	HashMap<String,Product> getProductMap();
	String getBarCodeNumberFromFile(byte[] fileContent) throws NotFoundException, ChecksumException, FormatException;
}
