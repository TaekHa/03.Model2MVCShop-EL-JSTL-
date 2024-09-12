package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.dao.ProductDao;
import com.model2.mvc.service.user.dao.UserDao;


public class PurchaseDAO {
	
	UserDao userDAO;
	ProductDao productDAO;

	public PurchaseDAO() {
		// TODO Auto-generated constructor stub
		userDAO = new UserDao();
		productDAO = new ProductDao();
	}
	
	public Purchase findPurchase(int tranNo) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM transaction WHERE tran_no LIKE ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);
		
		ResultSet rs = stmt.executeQuery();
		
		Purchase purchase = null;
		while (rs.next()) {
			purchase = new Purchase();
			purchase.setTranNo(rs.getInt("TRAN_NO"));
			purchase.setPurchaseProd(productDAO.findProduct(rs.getInt("PROD_NO")));
			purchase.setBuyer(userDAO.findUser(rs.getString("BUYER_ID")));
			purchase.setPaymentOption(rs.getString("PAYMENT_OPTION"));
			purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchase.setDivyAddr(rs.getString("DEMAILADDR"));
			purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchase.setTranCode(rs.getString("TRAN_STATUS_CODE").trim());
			purchase.setOrderDate(rs.getDate("ORDER_DATA"));
			purchase.setDivyDate(rs.getDate("DLVY_DATE"));

		}
		
		con.close();
		
		
		return purchase;
	}
	
	public Purchase findPurchaseByProd(int prodNo) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM transaction WHERE prod_no LIKE ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);
		
		ResultSet rs = stmt.executeQuery();
		
		Purchase purchase = null;
		while (rs.next()) {
			purchase = new Purchase();
			purchase.setTranNo(rs.getInt("TRAN_NO"));
			purchase.setPurchaseProd(productDAO.findProduct(rs.getInt("PROD_NO")));
			purchase.setBuyer(userDAO.findUser(rs.getString("BUYER_ID")));
			purchase.setPaymentOption(rs.getString("PAYMENT_OPTION"));
			purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchase.setDivyAddr(rs.getString("DEMAILADDR"));
			purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchase.setTranCode(rs.getString("TRAN_STATUS_CODE").trim());
			purchase.setOrderDate(rs.getDate("ORDER_DATA"));
			purchase.setDivyDate(rs.getDate("DLVY_DATE"));

		}
		
		con.close();
		
		
		return purchase;
	}
	
	//work from here!
	public Map<String,Object> getPurchaseList(Search search, String userID) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT tran_no, buyer_id, receiver_name, receiver_phone,tran_status_code,order_data FROM transaction WHERE buyer_id LIKE '"+userID+"' ORDER BY order_data";
		
		PreparedStatement stmt = 
				con.prepareStatement(sql , ResultSet.TYPE_SCROLL_INSENSITIVE,
														ResultSet.CONCUR_UPDATABLE);
		
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql);
		System.out.println("PurchaseDAO :: totalCount  :: " + totalCount);
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
	
		System.out.println(search);

		List<Purchase> list = new ArrayList<Purchase>();
		
		while(rs.next()){
			Purchase purchase = new Purchase();
			purchase.setTranNo(rs.getInt("tran_no"));
			purchase.setBuyer(userDAO.findUser(rs.getString("buyer_id")));
			purchase.setReceiverName(rs.getString("receiver_name"));
			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setTranCode(rs.getString("tran_status_code").trim());
			purchase.setOrderDate(rs.getDate("order_data"));
			
			list.add(purchase);
		}
		
		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);

		rs.close();
		pStmt.close();
		con.close();

		return map;
		
	}
	
	public Map<String, Object> getSaleList(Search search) throws Exception{
		
		return null;
	}
	
	public void insertPurchase(Purchase purchase) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "INSERT INTO transaction VALUES (seq_transaction_tran_no.nextval,?,?,?,?,?,?,?,1,sysdate,?)";
	 
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, purchase.getPurchaseProd().getProdNo());
		stmt.setString(2, purchase.getBuyer().getUserId());
		stmt.setString(3, purchase.getPaymentOption());
		stmt.setString(4, purchase.getReceiverName());
		stmt.setString(5, purchase.getReceiverPhone());
		stmt.setString(6, purchase.getDivyAddr());
		stmt.setString(7, purchase.getDivyRequest());
		stmt.setDate(8, purchase.getDivyDate());
		stmt.executeUpdate();
		
		con.close();
	}
	
	public void updatePurchase(Purchase purchase) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "UPDATE transaction SET payment_option=?,receiver_name=?,receiver_phone=?,demailaddr=?,dlvy_request=?,dlvy_date=? WHERE tran_no LIKE ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1,purchase.getPaymentOption());
		stmt.setString(2, purchase.getReceiverName());
		stmt.setString(3, purchase.getReceiverPhone());
		stmt.setString(4, purchase.getDivyAddr());
		stmt.setString(5, purchase.getDivyRequest());
		stmt.setDate(6, purchase.getDivyDate());
		stmt.setInt(7, purchase.getTranNo());
		stmt.executeUpdate();
		
		con.close();
		
	}
	
	public void updateTranCode(Purchase purchase, int tranStatusCode) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "UPDATE transaction SET tran_status_code = ? WHERE tran_no LIKE ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, Integer.toString(tranStatusCode));
		stmt.setInt(2, purchase.getTranNo());
		stmt.executeUpdate();
		
		con.close();
		
	}
	
	// 게시판 Page 처리를 위한 전체 Row(totalCount)  return
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	// 게시판 currentPage Row 만  return 
	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
					"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
									" 	FROM (	"+sql+" ) inner_table "+
									"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
					"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("UserDAO :: make SQL :: "+ sql);	
		
		return sql;
	}

}
