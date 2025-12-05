import java.util.Date;
import java.sql.*;

public class Customer {
    public int customerID;
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public String address;
    public Date dob;

    public Customer(int customerID, String username, String password, String firstName, String lastName, String email, String phoneNumber, String address, Date dob) {
        this.customerID = customerID;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dob = dob;
    }

    public Customer(ResultSet rs) throws SQLException {
        this.customerID = rs.getInt("CustomerID");
        this.username = rs.getString("Username");
        this.password = rs.getString("Password");
        this.firstName = rs.getString("FirstName");
        this.lastName = rs.getString("LastName");
        this.email = rs.getString("Email");
        this.phoneNumber = rs.getString("PhoneNumber");
        this.address = rs.getString("MailingAddress");
        this.dob = rs.getDate("DateOfBirth");
    }
}
