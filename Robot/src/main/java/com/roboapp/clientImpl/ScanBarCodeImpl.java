package com.roboapp.clientImpl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.roboapp.client.IRobotOperations;
import com.roboapp.client.IScanBarCode;
import com.roboapp.model.Product;

@Component
public class ScanBarCodeImpl implements IScanBarCode {

	@Autowired
	IRobotOperations roboOp;
	
	@Override
	public Product scan(byte[] fileContent) {
		String val = null;
		HashMap<String,Product> productMap = roboOp.getProductMap();
		try {
			val = roboOp.getBarCodeNumberFromFile(fileContent);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return productMap.get(val);
	}

}
