package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdateTranCodeByProdAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		int tranCode=Integer.parseInt(request.getParameter("tranCode"));
		int currentPage=Integer.parseInt(request.getParameter("currentPage"));
		
		System.out.println(prodNo+":"+tranCode+":"+currentPage);
		
		Search search = new Search();
		
		search.setCurrentPage(currentPage);
		
		
		PurchaseService service = new PurchaseServiceImpl();
		Purchase purchase = service.getPurchaseByProd(prodNo);
		System.out.println(purchase);
		
		service.updateTranCode(purchase, tranCode);
		request.setAttribute("search", search);

		
		return "redirect:/listProduct.do?menu=manage";
	}

}
