package com.lazyDroid.jetty;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ShopServlet extends HttpServlet {
	private List<ProductServlet> products;
	/**
	 * The constructor of ShopServlet.
	 */
	ShopServlet() {
		// TODO may add more things here
		products = new ArrayList<ProductServlet>();
	}
	
	void addProduct(ProductServlet product) {
		products.add(product);
	}

	/**
	 * Handle the HTTP GET method.
	 * 
	 * @param request
	 *            - The HTTP request from a client.
	 * 
	 * @param response
	 *            - The HTTP response that will be send to the client.
	 * 
	 * @throws IOException
	 *             When a servlet IO exception occurs.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO needs more things here
		PrintWriter writer = response.getWriter();
		
		Integer i = 1;
		for (ProductServlet entry : products) {
			String productMessage = "product" + i + ":" + entry.getProductName() + 
					"\ncost" + i + ":" + entry.getProductCost() + "\n";
			writer.write(productMessage);
			i++;
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
