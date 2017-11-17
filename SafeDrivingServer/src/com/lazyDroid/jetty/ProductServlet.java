package com.lazyDroid.jetty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = -8673169800264790419L;
	private final String productName;
	private final int cost;
	private final File targetPic;
	
	ProductServlet(String name, int cost, String path) {
		this.productName = name;
		this.cost = cost;
		targetPic = new File(path);
	}
	
	public String getProductName() {
		return this.productName;
	} 
	
	public int getProductCost() {
		return this.cost;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Content-Type", "image/jpeg");
		FileInputStream pic = new FileInputStream(targetPic);
		OutputStream outStream = response.getOutputStream();
		
		byte[] buf = new byte[1024];
		
		while (pic.read(buf) > 0) {
			outStream.write(buf);
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		
		pic.close();
		outStream.flush();
		outStream.close();
	}
}
