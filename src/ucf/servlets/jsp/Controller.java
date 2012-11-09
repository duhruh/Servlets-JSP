package ucf.servlets.jsp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ucf.mysql.jdbc.DAO;
import ucf.mysql.jdbc.DBEngine;

/**
 * Servlet implementation class Controller
 */
//@WebServlet("/Controller")
@WebServlet(urlPatterns={"/Controller"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBEngine mDBEngine;
	private DAO mDAO;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map mParameters = request.getParameterMap();
		String mForward = "/MySQL.jsp";
		if(mParameters.containsKey("execute")){
			mDBEngine = new DBEngine("jdbc:mysql://localhost:3306/project4", "root", "");
			mDAO = new DAO(mDBEngine.getConnection());
			
			//mDBEngine.CloseConnection();
			//need to execute query
		}else{
			//mForward = "MySQL";
		}
		
		RequestDispatcher mDispatcher = request.getRequestDispatcher(mForward);
		mDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
		Vector<Vector<String>> results = new Vector<Vector<String>>();
		Vector<String> columns = new Vector<String>();
		
		String destination  ="/MySQL.jsp";
		String mQuery = request.getParameter("textarea");
		String HTML;
		
		if(mQuery == "") mQuery = "select * from suppliers";
		
		mDBEngine = new DBEngine("jdbc:mysql://localhost:3306/project4", "root", "");
		
		try {
			mDBEngine.EstablishConnection();
			mDAO = new DAO(mDBEngine.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		if(mQuery.toLowerCase().startsWith("select")){
			try {
				results = mDAO.runQuery(mQuery);
				columns = mDAO.getColumns();
				HTML = generateHTML(results,columns);
			}catch (SQLException e1) {
				HTML = generateHTMLError(e1.getMessage());
				//e1.printStackTrace();
			}
		}else{
			try{
				int supplierStatusUpdate = 0;
				String supplierSnum = "";
				boolean updateSupplier = false;
				if(mQuery.toLowerCase().contains("insert into shipments") || mQuery.toLowerCase().contains("update shipments")){
					int first = mQuery.indexOf("(");
					int last = mQuery.indexOf(")");
					String temp = mQuery.substring(first+1, last);
					temp = temp.replaceAll("'", "");
					temp = temp.replaceAll(" ", "");
					String[] brokenString = temp.split(",");
					
					for(String word: brokenString){
						try{
							if(Integer.valueOf(word) >= 100){
								//supplierStatusUpdate = (Integer.valueOf(word) / 100) + 1;
								updateSupplier = true;
							}
						}catch(NumberFormatException e){
							if(word.startsWith("S")){
								supplierSnum = word;
							}
						}
					}
				}
				
				if(updateSupplier){
					//HTML = generateHTMLOK(mDAO.runUpdate(mQuery), mDAO.runUpdate("update suppliers set status = status +"+5+"where quantity >="+100));
					Vector<Vector<String>> temp = mDAO.runQuery("select DISTINCT(suppliers.snum) from suppliers join shipments on suppliers.snum = shipments.snum and shipments.quantity >= 100");
					String mIN = "";
					for(Vector row: temp){
						if(mIN == "")
							mIN += "'"+row.get(0)+"'";
						else
							mIN += ",'"+row.get(0)+"'";
					}
					
					if(supplierSnum != "") mIN += ",'"+supplierSnum+"'";
					
					String blah = "UPDATE suppliers set status = (status+"+5+") where snum IN ("+mIN+")";

					HTML = generateHTMLOK(mDAO.runUpdate(mQuery),mDAO.runUpdate(blah));
				}else{
					HTML = generateHTMLOK(mDAO.runUpdate(mQuery));
				}
				
			}catch (SQLException e1) {
				HTML = generateHTMLError(e1.getMessage());
				//e1.printStackTrace();
				
			}
		}
		 
		
		request.setAttribute("results", HTML);
		//response.sendRedirect(response.encodeRedirectURL(destination));
		RequestDispatcher mDispatcher = request.getRequestDispatcher(destination);
		mDispatcher.forward(request, response);
		
		try {
			mDBEngine.CloseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private String generateHTML(Vector<Vector<String>> mResults,Vector<String> mColumns){
		String mHTML = "<div id='table' style='margin:auto;text-align:center;width: 627px;'><table border='1' style='margin: auto;'><tr>";
		
		for(String mCol: mColumns){
			mHTML+= "<td style='background-color: green;'>"+mCol+"</td>";
		}
		mHTML+="</tr>";
		
		for(Vector row: mResults){
			mHTML += "<tr>";
			for(int i=0; i < row.size(); i++){
				mHTML += "<td>";
				mHTML += row.get(i);
				mHTML += "</td>";
			}
			mHTML += "</tr>";
		}
		return mHTML+="</table></div>";
	}
	private String generateHTMLError(String mMessage){
		String mHTML = "<div id='table' style='margin:auto;text-align:center;width: 627px;'><table border='1' style='margin: auto;background-color: red;'><tr>";
		mHTML += "<td>Error executing the SQL statement:<br/>"+mMessage+"</td>";
		return mHTML;
	}
	private String generateHTMLOK(int mColumns){
		String mHTML = "<div id='table' style='margin:auto;text-align:center;width: 627px;'><table border='1' style='margin: auto;background-color: green;'><tr>";
		mHTML += "<td>The SQL statement completed successfully:<br/>"+mColumns+ " row(s) affected.</td>";
		return mHTML;
	}
	private String generateHTMLOK(int mColumns, int mSuppliers){
		String mHTML = "<div id='table' style='margin:auto;text-align:center;width: 627px;'><table border='1' style='margin: auto;background-color: green;'><tr>";
		mHTML += "<td>The SQL statement completed successfully:<br/>"+mColumns+ " row(s) affected.<br> " +
				"Business Logic Detected! - Updating Suppliers Status<br> Business Logic update "+mSuppliers+" supplier status marks.</td>";
		return mHTML;
	}

}
