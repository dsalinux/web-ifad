/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.entity.Usuario;
import br.edu.ifnmg.ifad.util.Context;
import br.edu.ifnmg.ifad.util.FileUtil;
import br.edu.ifnmg.ifad.util.StringHelper;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author danilo
 */
@WebServlet(name = "FileServlet", urlPatterns = {"/fileServlet"})
public class FileServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         Object objLogin = request.getSession().getAttribute(Context.USUARIO_LOGADO);
         Usuario login = null;
         if(objLogin != null){
             login = (Usuario)objLogin;
         }
         if(login != null){
            try {
                String fileName = request.getParameter("fileName");
                String contentType = request.getParameter("contentType");
                File file = null;
                if(!StringHelper.isEmpty(fileName)){
                    file = new File(fileName);
                }
                if(file != null){
                    response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
                    response.setHeader("Cache-Control", "public");
                    response.setContentLength((int)file.length());
                    Calendar dataExpiracao = Calendar.getInstance();
                    dataExpiracao.add(Calendar.HOUR, 2);
                    response.setHeader("expires", new SimpleDateFormat().format(dataExpiracao.getTime()));
                    if(!StringHelper.isEmpty(contentType)){
                        response.setContentType(contentType);
                    } else {
                        response.setContentType("application/octet-stream");
                    }
                    byte[] img = null;
                    if(file.exists()){
                        img = FileUtil.fileToByteArray(file);
                        response.getOutputStream().write(img);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(FileServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
