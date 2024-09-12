package com.model2.mvc.view.product;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class GetProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String prodNo = request.getParameter("prodNo");
		String menu = request.getParameter("menu");
		
		ProductService service = new ProductServiceImpl();
		Product product = service.getProduct(Integer.parseInt(prodNo));
		
		request.setAttribute("product", product);
		request.setAttribute("menu",menu);
		
		//Cookie
		Cookie[] cookies = request.getCookies();
		//default 쿠키생성
		Cookie historyCookie = new Cookie("history", null);
		
		//있으면 가져오기
		if (cookies != null && cookies.length>0) {
			for (Cookie cookie : cookies) {
				historyCookie = (cookie.getName().equals("history"))? cookie : historyCookie;
			}
		}
		
		String historyCookieValue = historyCookie.getValue();
		String value = "";
		
		//같은 항목을 두번봤을때 최신화
		if (historyCookieValue == null) {
			value = prodNo;
		}else {
			if(!historyCookieValue.contains(prodNo)) {
				value = prodNo+"#"+historyCookieValue; // 추가해주기
			} else {
				for(String s : historyCookieValue.split(prodNo)) {
					value += s;
				}
				
				String[] splittedValue = value.split("#");
				value = "";
				
				for (int i = 0; i < splittedValue.length; i++) {
					if ( i < splittedValue.length - 1) {
						value += splittedValue[i] + "#";						
					} else {
						value += splittedValue[i];
					}
				}
				
				value = prodNo + "#" + value;
			}
		}
		
		historyCookie.setValue(value);
		response.addCookie(historyCookie);
		
		if(menu.equals("manage")) {
			return "forward:/updateProductView.do";
		}else {
			return "forward:/product/getProduct.jsp";
		}
		
	}

}
