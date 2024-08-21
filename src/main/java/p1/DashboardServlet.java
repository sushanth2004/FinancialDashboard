package p1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/financial-dashboard/data")
public class DashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to your SQL database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/financial_db", "root", "tiger");
            stmt = conn.createStatement();

            // Total Transactions
            rs = stmt.executeQuery("SELECT COUNT(*) FROM financial_data");
            rs.next();
            int totalTransactions = rs.getInt(1);
            out.println("Total Transactions: " + totalTransactions);

            // Total Amount
            rs = stmt.executeQuery("SELECT SUM(amount) FROM financial_data");
            rs.next();
            double totalAmount = rs.getDouble(1);
            out.println("Total Amount: $" + String.format("%.2f", totalAmount));

            // Total Fraudulent Transactions
            rs = stmt.executeQuery("SELECT COUNT(*) FROM financial_data WHERE fraud = 1");
            rs.next();
            int totalFraudulent = rs.getInt(1);
            out.println("Total Fraudulent Transactions: " + totalFraudulent);

            // Average Transaction Amount
            rs = stmt.executeQuery("SELECT AVG(amount) FROM financial_data");
            rs.next();
            double averageAmount = rs.getDouble(1);
            out.println("Average Transaction Amount: $" + String.format("%.2f", averageAmount));

            // Fraud Rate
            double fraudRate = ((double) totalFraudulent / totalTransactions) * 100;
            out.println("Fraud Rate: " + String.format("%.2f", fraudRate) + "%");

        } catch (ClassNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Error: MySQL Driver not found - " + e.getMessage());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("SQL Error: " + e.getMessage());
            e.printStackTrace(); // Log stack trace for debugging
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace(); // Log stack trace for debugging
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        out.flush();
    }
}
