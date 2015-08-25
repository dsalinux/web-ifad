/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.util.FileUtil;
import br.edu.ifnmg.ifad.util.StringHelper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
@WebServlet(name = "ImagemServlet", urlPatterns = {"/imagemServlet"})
public class ImagemServlet extends HttpServlet {

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
        try {
            String nomeImage = request.getParameter("imageName");
            File foto = null;
            if(!StringHelper.isEmpty(nomeImage)){
                foto = new File(nomeImage);
            }
            if (foto == null || !foto.exists()) {
                URL url =  getClass().getResource("/sem_foto.gif");
                if(url != null){
                    foto = new File(url.getPath());
                }
            }
            if(foto != null){
                response.setHeader("Content-Disposition", "attachment;filename=" + foto.getName());
                response.setHeader("Cache-Control", "public");
                Calendar dataExpiracao = Calendar.getInstance();
                dataExpiracao.add(Calendar.MONTH, 2);
                response.setHeader("expires", new SimpleDateFormat().format(dataExpiracao.getTime()));
                response.setContentType("image/png");
                Long tamanho = foto.length();
                response.setContentLength(tamanho.intValue());
                byte[] img;
                img = FileUtil.fileToByteArray(foto);
                response.getOutputStream().write(img);
            }
        } catch (IOException ex) {
            Logger.getLogger(ImagemServlet.class.getName()).log(Level.SEVERE, null, ex);
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
