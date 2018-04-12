package com.roboapp.client;

import com.roboapp.model.Product;

public interface IScanBarCode {
	Product scan( byte[] fileContent);
}
