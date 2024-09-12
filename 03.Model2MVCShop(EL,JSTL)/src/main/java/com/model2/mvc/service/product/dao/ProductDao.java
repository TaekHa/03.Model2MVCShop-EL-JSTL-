package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;

public class ProductDao {

	public ProductDao() {
		// TODO Auto-generated constructor stub
	}
	
	public Product findProduct(int prodNo) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT "
				+ "prod_no, prod_name, prod_detail, manufacture_day, price, image_file, reg_date "
				+ "FROM product WHERE prod_no LIKE (?)";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, prodNo);
		
		ResultSet rs = pStmt.executeQuery();
		
		Product product = null;
		while (rs.next()) {
			product = new Product();
			product.setProdNo(rs.getInt("prod_no"));
			product.setProdName(rs.getString("prod_name"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(rs.getInt("price"));
			product.setFileName(rs.getString("image_file"));
			product.setRegDate(rs.getDate("reg_date"));
		}
		
		rs.close();
		pStmt.close();
		con.close();
		
		
		return product;
	}
	
	public Map<String,Object> getProductList(Search search) throws Exception {
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT "
				+ "p.prod_no, p.prod_name, p.price, p.reg_date, t.tran_status_code "
				+ "FROM product p, transaction t "
				+ "WHERE p.prod_no = t.prod_no(+) ";
		if (search.getSearchCondition() != null) {
			if(search.getSearchCondition().equals("0")&&  !search.getSearchKeyword().equals("")) {
				sql +="AND prod_no LIKE '%" + search.getSearchKeyword()
				+"%'";				
			} else if(search.getSearchCondition().equals("1")&&  !search.getSearchKeyword().equals("")) {
				sql +="AND prod_name LIKE '%" + search.getSearchKeyword()
				+"%'";
			} else if (search.getSearchCondition().equals("2")&&  !search.getSearchKeyword().equals("")) {
				sql += "AND price LIKE'" + search.getSearchKeyword()
				+"'";
			}
		}
		sql += "ORDER BY prod_no";
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount:: " + totalCount);
		
		sql = makeCurrentPageSql(sql,search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		System.out.println(search);
		
		List<Product> list = new ArrayList<Product>();
		
		while(rs.next()) {
			Product product = new Product();
			product.setProdNo(rs.getInt("prod_no"));
			product.setProdName(rs.getString("prod_name"));
			product.setPrice(rs.getInt("price"));
			product.setRegDate(rs.getDate("reg_date"));
			if(rs.getString("tran_status_code") != null){
				product.setProTranCode(rs.getString("tran_status_code").trim());
			}else {
				product.setProTranCode("0");
			}
			list.add(product);
		}
		
		//totalCount
		map.put("totalCount",new Integer(totalCount));
		//currentPage 게시물 정보 갖는 list
		map.put("list", list);
		
		rs.close();
		pStmt.close();
		con.close();
		
		return map;
	}
	
	public void insertProduct(Product productVO) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "INSERT INTO product VALUES (seq_product_prod_no.nextval,?,?,?,?,?,sysdate)";
	 
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, productVO.getProdName());
		pStmt.setString(2, productVO.getProdDetail());
		pStmt.setString(3, productVO.getManuDate().split("-")[0]+productVO.getManuDate().split("-")[1]+productVO.getManuDate().split("-")[2]);
		pStmt.setInt(4, productVO.getPrice());
		pStmt.setString(5, productVO.getFileName());
		pStmt.executeUpdate();
		
		
		con.close();
	}
	
	public void updateProduct(Product productVO) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "UPDATE product "
				+ "SET prod_name=?,prod_detail=?,price=?,image_file=? "
				+ "WHERE prod_no=?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1,productVO.getProdName());
		pStmt.setString(2, productVO.getProdDetail());
		pStmt.setInt(3, productVO.getPrice());
		pStmt.setString(4, productVO.getFileName());
		pStmt.setInt(5, productVO.getProdNo());
		pStmt.executeUpdate();
		
		pStmt.close();
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
