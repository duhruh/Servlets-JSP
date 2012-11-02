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
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter(); 
		mDBEngine = new DBEngine("jdbc:mysql://localhost:3306/project4", "root", "");
		try {
			mDBEngine.EstablishConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mDAO = new DAO(mDBEngine.getConnection());
		out.println(request.getParameter("textarea"));
		String destination  ="/MySQL.jsp";        
		Vector<Vector<String>> results = new Vector<Vector<String>>();
		Vector<String> columns = new Vector<String>();
		String HTML = "";
		try {
			results = mDAO.runQuery(request.getParameter("textarea"));
			columns = mDAO.getColumns();
			HTML = generateHTML(results,columns);
		} catch (SQLException e1) {
			HTML = generateHTMLError();
			e1.printStackTrace();
		}
		request.setAttribute("results", HTML);
		//response.sendRedirect(response.encodeRedirectURL(destination));
		RequestDispatcher mDispatcher = request.getRequestDispatcher(destination);
		mDispatcher.forward(request, response);
		try {
			mDBEngine.CloseConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String generateHTML(Vector<Vector<String>> mResults,Vector<String> mColumns){
		String mHTML = "<div id='table' style='margin:auto;text-align:center;'><table border='1' style='margin: auto;'><tr>";
		
		for(String mCol: mColumns){
			mHTML+= "<td>"+mCol+"</td>";
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
	private String generateHTMLError(){
		String mHTML = "<div id='table' style='margin:auto;text-align:center;'><table border='1' style='margin: auto;background-color: red;'><tr>";
		mHTML += "<td>Error executing the SQL statement:<br/></td>";
		return mHTML;
	}

}
