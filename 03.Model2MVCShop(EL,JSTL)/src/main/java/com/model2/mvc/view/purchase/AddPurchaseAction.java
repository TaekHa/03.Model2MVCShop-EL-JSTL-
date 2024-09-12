package com.model2.mvc.view.purchase;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


public class AddPurchaseAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		ProductService productService = new ProductServiceImpl();
		UserService userService = new UserServiceImpl();
		PurchaseService purchaseService = new PurchaseServiceImpl();
		
		Purchase purchase=new Purchase();
		purchase.setPurchaseProd(productService.getProduct(Integer.parseInt(request.getParameter("prodNo"))));
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setBuyer(userService.getUser(request.getParameter("buyerId")));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		if(request.getParameter("receiverDate") != null && request.getParameter("receiverDate").trim() != "") {
			purchase.setDivyDate(Date.valueOf(request.getParameter("receiverDate")));
		}else {
			purchase.setDivyDate(null);
		}
		
		purchaseService.addPurchase(purchase);
		System.out.println(purchase);
		request.setAttribute("purchase", purchase);
		
		return "forward:/purchase/addPurchase.jsp";
	}
}