package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdateTranCodeAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int tranNo=Integer.parseInt(request.getParameter("tranNo"));
		int tranCode=Integer.parseInt(request.getParameter("tranCode"));
		int currentPage=Integer.parseInt(request.getParameter("page"));
		
		Search search = new Search();
		
		search.setCurrentPage(currentPage);
		
		
		PurchaseService service = new PurchaseServiceImpl();
		Purchase purchase = service.getPurchase(tranNo);
		System.out.println(purchase);
		
		service.updateTranCode(purchase, tranCode);
		
		request.setAttribute("search", search);

		
		return "forward:/listPurchase.do";
	}

}
