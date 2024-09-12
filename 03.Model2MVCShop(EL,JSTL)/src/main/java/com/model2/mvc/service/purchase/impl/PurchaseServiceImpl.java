package com.model2.mvc.service.purchase.impl;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;

public class PurchaseServiceImpl implements PurchaseService {
	//Field
	private PurchaseDAO purchaseDAO;
	
	//Constructor
	public PurchaseServiceImpl() {
		purchaseDAO = new PurchaseDAO();
	}
	
	//method
	@Override
	public void addPurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		purchaseDAO.insertPurchase(purchase);
	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDAO.findPurchase(tranNo);
	}
	
	@Override
	public Purchase getPurchaseByProd(int prodNo) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDAO.findPurchaseByProd(prodNo);
	}

	@Override
	public Map<String, Object> getPurchaseList(Search search, String userId) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDAO.getPurchaseList(search, userId);
	}

	@Override
	public Map<String, Object> getSaleList(Search search) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDAO.getSaleList(search);
	}

	@Override
	public void updatePurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		purchaseDAO.updatePurchase(purchase);
	}

	@Override
	public void updateTranCode(Purchase purchase, int tranStatusCode) throws Exception {
		// TODO Auto-generated method stub
		purchaseDAO.updateTranCode(purchase, tranStatusCode);
	}

}
