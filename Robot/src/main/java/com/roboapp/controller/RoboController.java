package com.roboapp.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Preconditions;
import com.roboapp.client.IRobotOperations;
import com.roboapp.client.IScanBarCode;
import com.roboapp.model.Product;
import com.roboapp.model.Robot;
import com.roboapp.util.ERROR_CODE;

@RestController
@RequestMapping(value = "/robo/")
public class RoboController extends AbstractController {

	@Autowired
	private IRobotOperations roboOperations;

	@Autowired
	private IScanBarCode scanBarCode;

	@RequestMapping(value = "/operations/getPrice", method = RequestMethod.POST)
	public Object barCodeScanner(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {

		List<Error> errors = null;
		HttpStatus httpStatus = HttpStatus.OK;
		Product res = null;
		try {
			Preconditions.checkArgument(!file.isEmpty(), ERROR_CODE.EMPTY_FILE);
			byte[] bytes = file.getBytes();
			res = this.scanBarCode.scan(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			Map<String, Object> returnValues = super.populateErrors(e);
			errors = (List<Error>) returnValues.get("errors");
			httpStatus = (HttpStatus) returnValues.get("status");
		}
		response.setStatus(httpStatus.value());
		ApiResponse result = new ApiResponse(res, errors);
		if(result.getData()==null) {
			return result.getErrors();
		}
		return result.getData();
	}

	@RequestMapping(value = "/operations/walk", method = RequestMethod.POST)
	public Object roboOperations(@RequestBody Robot robot,
			@RequestParam(value = "weight", defaultValue = "0.0") String weight,
			@RequestParam(value = "totalDist", required = false) String totalDist, HttpServletRequest request,
			HttpServletResponse response) {

		List<Error> errors = null;
		HttpStatus httpStatus = HttpStatus.OK;
		Robot res = null;
		try {
			double weightToBeCarried = Double.valueOf(weight);
			double distanceToTravel = (totalDist != null) ? Double.valueOf(totalDist) : Integer.MAX_VALUE;
			Preconditions.checkArgument(weightToBeCarried <= robot.getMaxWeight(), ERROR_CODE.OVERWEIGHT);
			Preconditions.checkArgument(weightToBeCarried > -1, ERROR_CODE.INVALID_WEIGHT);
			Preconditions.checkArgument(distanceToTravel > -1, ERROR_CODE.INVALID_DISTANCE);
			res = this.roboOperations.walk(robot, weightToBeCarried, distanceToTravel);
		} catch (Exception e) {
			Map<String, Object> returnValues = super.populateErrors(e);
			errors = (List<Error>) returnValues.get("errors");
			httpStatus = (HttpStatus) returnValues.get("status");
		}
		response.setStatus(httpStatus.value());
		ApiResponse result = new ApiResponse(res, errors);
		if(result.getData()==null) {
			return result.getErrors();
		}
		return result.getData();
	}

	@RequestMapping(value = "/operations/addProduct", method = RequestMethod.POST)
	public String addProducts(@RequestBody Product product, HttpServletRequest request,
			HttpServletResponse response) {
		HttpStatus httpStatus = HttpStatus.OK;
		String res = null;
		try {
			Preconditions.checkArgument(product.getProductNo() != null, ERROR_CODE.EMPTY_PRODCUT_NUMBER);
			Integer.parseInt(product.getProductNo());
			res = this.roboOperations.addProducts(product);
		} catch (NumberFormatException e) {
			Map<String, Object> returnValues = super.populateErrors(e);
			httpStatus = (HttpStatus) returnValues.get("status");
		} catch (Exception e) {
			Map<String, Object> returnValues = super.populateErrors(e);
			httpStatus = (HttpStatus) returnValues.get("status");
		}
		return res;
	}
}
