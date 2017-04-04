package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.qcloud.jc.CosUtil;

/**
 * Servlet implementation class FileUpload
 */
@WebServlet("/FileUpload")	
public class FileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUpload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		try {
			// 在解析请求之前先判断请求类型是否为文件上传类型
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if(isMultipart || true){
				// 文件上传处理工厂
				FileItemFactory factory = new DiskFileItemFactory();
				// 创建文件上传处理器
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");
				// 开始解析请求信息
				List<FileItem> items = upload.parseRequest(request);
				// 对所有请求信息进行判断
				Iterator<FileItem> iter = items.iterator();
				List<String> fileList = null;
				int i = 1;
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					// 信息为普通的格式
					if (item.isFormField()) {
						String fieldName = item.getFieldName();
						String value = item.getString("UTF-8");
						
						if (value == null) {
							value = "";
						}
					}
					// 信息为文件格式
					else {
						String fileName = item.getName();
						fileName = fileName.substring(fileName.lastIndexOf(".")+1);
						String sjs = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
						CosUtil c = new CosUtil();
						String s = c.uploadByIo("/jc/"+sjs+"-"+(i++)+"."+fileName, item.getInputStream());
						System.out.println(s);
					}
				}
			}
			
			out.println("ok");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		out.flush();
		out.close();
		
	}

}
