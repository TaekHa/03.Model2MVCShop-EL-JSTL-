package com.model2.mvc.view.purchase;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdatePurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int tranNo=Integer.parseInt(request.getParameter("tranNo"));
		
		Purchase purchase = new Purchase();
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		if(request.getParameter("receiverDate") != null && request.getParameter("receiverDate").trim() != "") {
			purchase.setDivyDate(Date.valueOf(request.getParameter("receiverDate")));
		}else {
			purchase.setDivyDate(null);
		}
		purchase.setTranNo(tranNo);
		
		PurchaseService service = new PurchaseServiceImpl();
		service.updatePurchase(purchase);

		
		return "redirect:/getPurchase.do?tranNo="+tranNo;
	}

}
