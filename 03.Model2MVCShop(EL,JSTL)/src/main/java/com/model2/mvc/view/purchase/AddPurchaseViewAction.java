package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class AddPurchaseViewAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User user = (User)request.getSession().getAttribute("user");
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		
		if(user == null) {
			return "forward:/product/listProduct.do?menu=search";
		}
		
		ProductService productService = new ProductServiceImpl();
		Product product = productService.getProduct(prodNo);		
		
		request.setAttribute("user", user);
		request.setAttribute("product", product);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}

}
