package com.ecom.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom.entity.Seller;
import com.ecom.service.SellerService;

@Controller
public class SellerController {

	@Autowired
	SellerService sellerService;

	String driverName = "org.postgresql.Driver";
	String connectionUrl = "jdbc:postgresql://localhost/";
	String dbName = "student";
	String userId = "postgres";
	String password = "postgres";
	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;

	public static boolean is_vaild_password(String password) {
		int n = password.length();
		boolean hasLower = false, hasUpper = false, hasDigit = false, specialChar = false;
		Set<Character> set = new HashSet<Character>(
				Arrays.asList('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'));
		for (char i : password.toCharArray()) {
			if (Character.isLowerCase(i))
				hasLower = true;
			if (Character.isUpperCase(i))
				hasUpper = true;
			if (Character.isDigit(i))
				hasDigit = true;
			if (set.contains(i))
				specialChar = true;
		}
		if (hasDigit && hasLower && hasUpper && specialChar && (n >= 8))
			return true;
		else
			return false;
	}

	@RequestMapping("/addingSeller")
	public String addingSeller(Seller s) throws ClassNotFoundException, SQLException {
		String Spassword = s.getSpassword();
		String Semail = s.getSemail();
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(connectionUrl + dbName, userId, password);
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		// System.out.println(name);
		resultSet = statement.executeQuery("select * from Seller where Semail=" + "'" + Semail + "'");
		resultSet.last();
		// System.out.println(resultSet.getRow());
		if (resultSet.getRow() > 0) {
			System.out.println(Semail + "--> e-mail already exists");
			return "selleradd.jsp";
		} else if (!is_vaild_password(Spassword)) {
			System.out.println("Your Password is : " + Spassword);
			System.out.println("Your password should contain at least one lowercase English character.\r\n"
					+ "Your password should contain at least one uppercase English character.\r\n"
					+ "Your password should contain at least one special character. The special characters are: !@#$%^&*()-+\r\n"
					+ "Your password's length should be at least 8.\r\n"
					+ "Your password should contain at least one digit.");
			return "selleradd.jsp";
		} else {
			sellerService.addSeller(s);
			return "sellersignin.jsp";
		}
	}

	@RequestMapping("/updatingSeller")
	public String updatingSeller(Seller s) {
		sellerService.addSeller(s);
		return "sellerdisplay.jsp";
	}

	@RequestMapping("/deletingSeller")
	public String deletingSeller(int id) {
		sellerService.deleteSeller(id);
		return "admincontrol.jsp";
	}

	@RequestMapping("/seller")
	public String seller() {
		return "sellerloginsignup.jsp";
	}

	@RequestMapping("/sellersignup")
	public String sellerSignUp() {
		return "selleradd.jsp";
	}

	@RequestMapping("/sellersignin")
	public String sellerSignIn() {
		return "sellersignin.jsp";
	}

	@RequestMapping("/addseller")
	public String addSeller() {
		return "selleradd.jsp";
	}

	@RequestMapping("/adminseller")
	public String adminSeller() {
		return "admincontrol.jsp";
	}

	@RequestMapping("/updateseller")
	public String updateSeller() {
		return "sellerupdate.jsp";
	}

	@RequestMapping("/sellerafterlogin")
	public String sellerAfterLogin() {
		return "sellerdisplay.jsp";
	}

	// DELETE SELLER //
	@RequestMapping("/deleteseller")
	public String deleteSeller() {
		return "sellerdelete.jsp";
	}

	@RequestMapping("/sellerproductshow")
	public String sellerProductShow() {
		return "sellerlistedproducts.jsp";
	}

	@RequestMapping("/sellergobacktohome")
	public String sellerGoBackToHome() {
		return "Home.jsp";
	}

}