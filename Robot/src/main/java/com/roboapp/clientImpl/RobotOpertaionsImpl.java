package com.roboapp.clientImpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.roboapp.client.IRobotOperations;
import com.roboapp.model.Product;
import com.roboapp.model.Robot;

@Component
public class RobotOpertaionsImpl implements IRobotOperations{
	static HashMap<String, Product> prodcutMap = new HashMap<>();

	@Override
	public Robot walk(Robot robot, double weight, double totalDist) {
		int distanceCovered = 0;
		while(chargeCheck(robot) && distanceCovered <= totalDist) {
			double batteryConsumptionFromWeight = getRemainingBattery(2,robot.getCharge(),weight);
			robot.setCharge(robot.getCharge() - (batteryConsumptionFromWeight+1));
			distanceCovered += 5;
		}
		robot.setTotalDistanceCovenerd(distanceCovered);
		return robot;
	}

	@Override
	public boolean chargeCheck(Robot robot) {
		if (robot.getCharge() < robot.getMincharge()) {
			System.out.println("Out of Battery !!");
			return false;
		} else
			return true;
	}
	
	@Override
	public String addProducts(Product product) {
		String text = product.getProductNo();
		  
		int width = 400;  
		int height = 300; // change the height and width as per your requirement  
		  
		String imageFormat = "png"; // could be "gif", "tiff", "jpeg"   
		try {
			BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height);  
			File file = new File("qrcode_"+text+".png");
			MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, new FileOutputStream(file));
			prodcutMap.put(text, product);
			return file.getAbsolutePath();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "Invalid product";
	}
	
	@Override
	public HashMap<String, Product> getProductMap() {
		return prodcutMap;
	}
	
	private String getBarCodeNumber(String path) throws NotFoundException, ChecksumException, FormatException {
		try {
			InputStream barCodeInputStream = new FileInputStream(path);  
			BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);
	
			LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Reader reader = new MultiFormatReader();
			Result result = reader.decode(bitmap);
	
			System.out.println("Barcode text is " + result.getText());
			return result.getText();
		}catch(NotFoundException e) {
			e.printStackTrace();
		}catch(ChecksumException e) {
			e.printStackTrace();
		}catch(FormatException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return "Product Not Found !";
	}
	
	@Override
	public String getBarCodeNumberFromFile(byte[] fileContent) throws NotFoundException, ChecksumException, FormatException {
		try {
			InputStream input = new ByteArrayInputStream(fileContent);
			BufferedImage barCodeBufferedImage = ImageIO.read(input);
	
			LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Reader reader = new MultiFormatReader();
			Result result = reader.decode(bitmap);
	
			System.out.println("Barcode text is " + result.getText());
			return result.getText();
		}catch(NotFoundException e) {
			e.printStackTrace();
		}catch(ChecksumException e) {
			e.printStackTrace();
		}catch(FormatException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return "Product Not Found !";
	}

	private static double getRemainingBattery(int n, double total,double weight) {
		double proportion = ((double) n) / ((double) total);
		return (proportion * 100)*weight;
	}

}
