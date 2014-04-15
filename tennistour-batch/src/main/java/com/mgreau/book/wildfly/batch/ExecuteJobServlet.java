package com.mgreau.book.wildfly.batch;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.operations.JobOperator;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.JobStartException;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/executeJob" })
public class ExecuteJobServlet extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet Execute Job</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet TestServlet at "
					+ request.getContextPath() + "</h1>");
			out.println("About to start the job<br>");
			JobOperator jo = BatchRuntime.getJobOperator();
			out.println("Got the job operator: " + jo + "<br>");

			Properties props = new Properties();
			props.put("csvDatasFileName",
					request.getParameter("tt"));

			long jid = jo.start("loadHistoryJob", props);
			out.println("Job submitted: " + jid + "<br>");
			out.println(jo.getJobInstanceCount("loadHistoryJob")
					+ " job instance found<br/>");
			JobExecution je = jo.getJobExecution(jid);

			// jo.abandon(jid);
			out.println("Job created on: " + je.getCreateTime() + "<br>");
			out.println("Job started on: " + je.getStartTime() + "<br>");
			out.println("Found: " + jo.getJobNames().size() + " jobs<br>");
			for (String j : jo.getJobNames()) {
				out.println("--> " + j + "<br>");
			}

			out.println("<br><br><a href=\"./\">Home</a>  - <a href=\"./list\">All players.</a>");
			out.println("</body>");
			out.println("</html>");
		} catch (JobStartException | JobSecurityException ex) {
			Logger.getLogger(ExecuteJobServlet.class.getName()).log(Level.SEVERE,
					"erreur jobs", ex);
		}
	}

	private void sendFileNameToLoad(String name) {

	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
