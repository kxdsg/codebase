package com.argus.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GroovyServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String scriptBody = req.getParameter("scriptBody");
			Binding binding = new Binding ();
			binding.setVariable("request", req);
			binding.setVariable("response", resp);
			GroovyShell shell = new GroovyShell (binding);
			Object obj = shell.evaluate(scriptBody);
			req.setAttribute("scriptResponse", obj);			
		} catch (Exception e) {
			req.setAttribute("scriptResponse", constructStackTrace(e));
		}
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
		dispatcher.forward(req, resp);
	}
	
    public static String constructStackTrace (Exception e) {
    	StackTraceElement[] elems = e.getStackTrace();
    	boolean firstElem = true;
    	StringBuffer buf = new StringBuffer ();
    	for (StackTraceElement elem : elems) {
    		if (firstElem) {
    			firstElem = false;
    		} else {
    			buf.append("\n    ");
    		}
    		buf.append(elem.getClassName() + "." + elem.getMethodName() + ":" + elem.getLineNumber());
    	}
    	return buf.toString();
    }


	public static void main(String[] args) {
		GroovyShell shell = new GroovyShell ();
		System.out.println (shell.evaluate("System.out.println (\"Hello\"); return \"Hello world\";"));
	}
}
