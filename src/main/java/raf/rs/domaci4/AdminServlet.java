package raf.rs.domaci4;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "odabranaJela", value = "/odabrana-jela")
public class AdminServlet extends HttpServlet {

    private Map<String, Map<String, Integer>> weeklyOrders;
    private Map<String, List<String>> ordersForUser;
    private String password;
    private String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    public void init() {
        weeklyOrders = FirstPage.weeklyOrders;
        ordersForUser = FirstPage.ordersForUser;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        password = request.getParameter("password");
        if(password == null || !password.equals(FirstPage.password)) {
            response.sendRedirect("home");
            return;
        }
        int i = 0;
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Weekly orders</h1>");
        for(Map<String, Integer> dailyOrders : weeklyOrders.values()) {
            out.println("<h2>Orders for " + days[i] + "</h2>");
            for(Map.Entry<String, Integer> entry : dailyOrders.entrySet())
                out.println(entry.getKey() + " : " + entry.getValue() + "<br>");
            i++;
        }

        out.println("<form method=\"POST\" action = \"odabrana-jela\"><input type=\"submit\" name=\"submit\" value=\"CLEAR\"/>");
        out.println("</body></html>");

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(password == null || !password.equals(FirstPage.password)) {
            response.sendRedirect("home");
            return;
        }
        ordersForUser.clear();
        for(Map<String, Integer> dailyOrders : weeklyOrders.values())
            dailyOrders.replaceAll((k, v) -> 0);

        response.sendRedirect("odabrana-jela?password=" + password);
    }

}
