package raf.rs.domaci4;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "menuServlet", value = "/home")
public class FirstPage extends HttpServlet {


    private List<String> menu = new ArrayList<>();
    public static String password;
    public static Map<String, Map<String, Integer>> weeklyOrders = new HashMap<>();
    public static Map<String, List<String>> ordersForUser = new HashMap<>();
    private String[] days;

    public void init() {

        System.out.println("init method");
        readFromFile("C:\\Users\\User\\Desktop\\fakultet\\wp\\Domaci4\\src\\main\\resources\\monday.txt");
        readFromFile("C:\\Users\\User\\Desktop\\fakultet\\wp\\Domaci4\\src\\main\\resources\\tuesday.txt");
        readFromFile("C:\\Users\\User\\Desktop\\fakultet\\wp\\Domaci4\\src\\main\\resources\\wednesday.txt");
        readFromFile("C:\\Users\\User\\Desktop\\fakultet\\wp\\Domaci4\\src\\main\\resources\\thursday.txt");
        readFromFile("C:\\Users\\User\\Desktop\\fakultet\\wp\\Domaci4\\src\\main\\resources\\friday.txt");
        readFromFile("C:\\Users\\User\\Desktop\\fakultet\\wp\\Domaci4\\src\\main\\resources\\password.txt");
        weeklyOrders.put("Monday", new HashMap<>());
        weeklyOrders.put("Tuesday", new HashMap<>());
        weeklyOrders.put("Wednesday", new HashMap<>());
        weeklyOrders.put("Thursday", new HashMap<>());
        weeklyOrders.put("Friday", new HashMap<>());
        days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        int i = 0;
        for(String item : menu) {
            weeklyOrders.get(days[i/3]).put(item, 0);
            i++;
        }
    }


    private void readFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            if(fileName.contains("password.txt")) {
                password = reader.readLine();
                return;
            }
            while ((line = reader.readLine()) != null)
                menu.add(line);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String sessionId = request.getSession().getId();
        System.out.println("Session id: " + sessionId);
        PrintWriter out = response.getWriter();
        if(!ordersForUser.containsKey(sessionId)) {
            StringBuilder outString = new StringBuilder("<html> <body> <h1>Choose your lunch</h1> <form method=\"POST\" action=\"/home\">");

            for (int i = 0; i < menu.size(); i++) {
                if (i % 3 == 0) {
                    outString.append("<label for=\"").append(days[i / 3]).append("\">").append(days[i / 3]).append(":</label>");
                    outString.append("<select id=\"").append(days[i / 3]).append("\" name = \"").append(days[i / 3]).append("\">");

                }
                outString.append("<option value=\"").append(menu.get(i)).append("\">").append(menu.get(i)).append("</option>");
                if (i % 3 == 2) {
                    outString.append("</select><br>");
                }
            }
            outString.append("<br><br><input type=\"submit\" name=\"submit\" value=\"Potvrda\"/> " + "</form> " + "</body> " + "</html> ");
            out.println(outString);

        }
        else {
            out.println("<html><body><h2>You have already ordered!</h2>");
            out.println("<h3>Your orders:</h3>");
            for(String order : ordersForUser.get(sessionId))
                out.println("<h4>" + order + "</h4>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();
        System.out.println("Session id: " + sessionId);
        if(!ordersForUser.containsKey(sessionId)) {
            ordersForUser.put(sessionId, new ArrayList<>());
            List<String> orders = ordersForUser.get(sessionId);
            for (String day : days) {
                String order = request.getParameter(day);
                orders.add(order);
                weeklyOrders.get(day).put(order, weeklyOrders.get(day).get(order) + 1);
            }
//            int i = 0;
//            for(Map<String, Integer> dailyOrders : weeklyOrders.values()) {
//
//                System.out.println("Orders for " + days[i] + ":");
//
//                for(Map.Entry<String, Integer> entry : dailyOrders.entrySet()){
//                    System.out.println(entry.getKey() + " " + entry.getValue());
//                }
//                i++;
//            }

        }
        response.sendRedirect("/home");
    }

    public void destroy() {
        System.out.println("destroy method");
    }
}

