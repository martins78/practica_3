package com.emergentes.controlador;
import com.emergentes.modelo.GestorLibro;
import com.emergentes.modelo.Libro;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "controller", urlPatterns = {"/controller"})
public class controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Libro objLibro = new Libro();
        int id;
        int pos;
        String opcion = request.getParameter("op");
        String op = (opcion != null) ? request.getParameter("op"):"view";
        
        if (op.equals("nuevo")) {
            HttpSession ses = request.getSession();
            GestorLibro agenda = (GestorLibro) ses.getAttribute("agenda");
            objLibro.setId(agenda.obtieneId());
            request.setAttribute("op", op);
            request.setAttribute("miTarea", objLibro);
            request.getRequestDispatcher("editar.jsp").forward(request, response);
        }
        if (op.equals("modificar")) {
            id = Integer.parseInt(request.getParameter("id"));
            HttpSession ses = request.getSession();
            GestorLibro agenda = (GestorLibro) ses.getAttribute("agenda");
            pos = agenda.ubicarLibro(id);
            objLibro = agenda.getLista().get(pos);
            
            request.setAttribute("op", op);
            request.setAttribute("miTarea", objLibro);
            request.getRequestDispatcher("editar.jsp").forward(request, response);
        }
        if (op.equals("eliminar")) {
            id = Integer.parseInt(request.getParameter("id"));
            HttpSession ses = request.getSession();
            GestorLibro agenda = (GestorLibro) ses.getAttribute("agenda");
            pos = agenda.ubicarLibro(id);
            agenda.eliminarLibro(pos);
            ses.setAttribute("agenda", agenda);
            response.sendRedirect("index.jsp");
        }
        if (op.equals("view")) {
            response.sendRedirect("index.jsp");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Libro objLibro = new Libro();
        int pos;
        String op = request.getParameter("op");
        
        if (op.equals("grabar")) {
            //recuperar valores del formulario
            //verificar si es nuevo o ese una modificacion
            objLibro.setId(Integer.parseInt(request.getParameter("id")));
            objLibro.setTitulo(request.getParameter("titulo"));
            objLibro.setAutor(request.getParameter("autor"));
            objLibro.setResumen(request.getParameter("resumen"));
            objLibro.setMedio(request.getParameter("medio"));
            
            HttpSession ses = request.getSession();
            GestorLibro agenda = (GestorLibro) ses.getAttribute("agenda");
            
            String opg = request.getParameter("opg");
            if (opg.equals("nuevo")) {
                agenda.insertarLibro(objLibro);
            }
            else{
                pos = agenda.ubicarLibro(objLibro.getId());
                agenda.insertarLibro(objLibro);
            }
            ses.setAttribute("agenda", agenda);
            response.sendRedirect("index.jsp");
        }
    }
}
