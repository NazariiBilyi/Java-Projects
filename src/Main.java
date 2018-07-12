import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	
	static Connection conn;
	static Scanner sc = new Scanner(System.in);
	static InputStreamReader isr = new InputStreamReader (System.in);
	static BufferedReader bf = new BufferedReader(isr);
	
	public static void main(String[] args) throws SQLException, IOException {
		String dbUrl = "jdbc:mysql://localhost:3306/yellow_pages?useSSL=false";
		 String username = "root";
		 String password = "Tobius1988";
		 
		 conn = DriverManager.getConnection(dbUrl, username, password);
		 System.out.println("Connected?" + !conn.isClosed());
		 createTablePerson();
		 createTableCity();
		 createTableCountry();
		 createConnection();
		 int choice = 0;
		 while (choice != 13) {
			 MenuList.startMenu();
			 choice = sc.nextInt();
			switch (choice) {
			case 1:
				System.out.println("Enter the name of the country");
				String countryName = bf.readLine();
				addCountry(countryName);
				break;
			case 2:
				System.out.println("Enter the name of the city");
				String cityName = bf.readLine();
				addCity(cityName);
				break;
			case 3:
				System.out.println("Enter the name of the person");
				String personName = bf.readLine();
				System.out.println("Enter the last name of the person");
				String persontLastName = bf.readLine();
				System.out.println("Enter the age of the person");
				int personAge = sc.nextInt();
				System.out.println("Enter persons hobby");
				String personHobby = bf.readLine();
				System.out.println("Enter the number of the city");
				int cityNumber = sc.nextInt();
				System.out.println("Enter the number of the country");
				int countryNumber = sc.nextInt();
				addPerson(personName, persontLastName, personAge, personHobby, cityNumber, countryNumber);
				break;
			case 4:
				viewPersons();
				break;
			case 5:
				System.out.println("Enter the id of the person whose data you want to see");
				int personsId = sc.nextInt();
				selectOnePerson(personsId);
				break;
			case 6:
				System.out.println("Enter the id of the city which data you want to see");
				int cityId = sc.nextInt();
				selectOneCity(cityId);
				break;
			case 7:
				System.out.println("Enter the id of the country which data you want to see");
				int countryId = sc.nextInt();
				selectOneCountry(countryId);
				break;
			case 8:
				System.out.println("Enter the id of the city whose inhabitanrs you want to see");
				int joinPersonIndex = sc.nextInt();
				joinPersonAndCity(joinPersonIndex);
				break;
			case 11:
				for (int i = 0; i < 50; i++) {
					randomFill(i);
				}
			case 13:
				System.out.println("Goodbye!!!");
				break;
			}
		}
		 conn.close();

	}

	
	
	public static void createTablePerson() throws SQLException {
		String dropQuerty = "drop table if exists person";
		String query = "create table person("
				+ "id int primary key auto_increment,"
				+ "first_name varchar(255) not null,"
				+ "last_name varchar(255) not null,"
				+ "age int not null,"
				+ "hobby varchar(255) not null,"
				+ "country_id int not null,"
				+ "city_id int not null"
				+ ");";
		Statement stmt = conn.createStatement();
		stmt.execute(dropQuerty);
		stmt.execute(query);
		System.out.println("Table Person has been created");
		stmt.close();
	}
	
	public static void createTableCity() throws SQLException {
		String dropQuerty = "drop table if exists city";
		String query = "create table city("
			+ "id int primary key auto_increment,"
			+ "name varchar(255) not null"
			+ ");";
		Statement stmt = conn.createStatement();
		stmt.execute(dropQuerty);
		stmt.execute(query);
		System.out.println("Table City has been created");
		stmt.close();
	}
	
	public static void createTableCountry() throws SQLException {
		String dropQuery = "drop table if exists country";
		String query = "create table country("
				+ "id int primary key auto_increment,"
				+ "name varchar(255) not null"
				+ ");";
		Statement stmt = conn.createStatement();
		stmt.execute(dropQuery);
		stmt.execute(query);
		System.out.println("Table Country has been created");
		stmt.close();
		
	}
	
	public static void createConnection() throws SQLException {
		String querycon1 = "alter table person add foreign key (city_id) references city(id);";
		String querycon2 = "alter table person add foreign key (country_id) references country(id);";
		Statement stmt = conn.createStatement();
		stmt.execute(querycon1);
		stmt.execute(querycon2);
		System.out.println("Connection has been created");
		stmt.close();
	}
	
	public static void addCountry(String name) throws SQLException {
		String query = "insert into country(name) values(?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, name);
	
		pstmt.executeUpdate();
		pstmt.close();
		
	}
	
	public static void addCity(String name) throws SQLException {
		String query = "insert into city(name) values(?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, name);
	
		pstmt.executeUpdate();
		pstmt.close();
		
	}
	
	public static void addPerson(String name, String lastName, int age, String hobby, int cityNumber, int countryNumber) throws SQLException {
		String query = "insert into person(first_name, last_name, age, hobby, country_id, city_id) values(?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, name);
		pstmt.setString(2, lastName);
		pstmt.setInt(3, age);
		pstmt.setString(4, hobby);
		pstmt.setInt(5, cityNumber);
		pstmt.setInt(6, countryNumber);
		
		pstmt.executeUpdate();
		pstmt.close();
		
	}
	
	public static void viewPersons() throws SQLException {
		String query = "select p.*, cit.name, con.name from person p "
				+ "join city cit on cit.id = p.city_id "
				+ "join country con on con.id = p.country_id;";
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			System.out.println("ID: " + rs.getInt("id") + "\t|" 
					+ "First Name: " + rs.getString("first_name") + "\t|"
					+ "Last name: " + rs.getString("last_name") + "\t|"
					+ "Age: " + rs.getInt("age") + "\t|"
					+ "Hobby" + rs.getString("hobby")+ "\t|"
					+ "City: " + rs.getString("cit.name") + "\t|"
					+ "Country " + rs.getString("con.name") + "\t|"
					);	
			}
		pstmt.close();
		rs.close();
		}
	
	public static void selectOnePerson(int a) throws SQLException {
		String query = "select p.* from person p where id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, a);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			System.out.println("ID: " + rs.getInt("id") + "\t|" 
					+ "First Name: " + rs.getString("first_name") + "\t|"
					+ "Last name: " + rs.getString("last_name") + "\t|"
					+ "Age: " + rs.getInt("age") + "\t|"
					+ "Hobby: " + rs.getString("hobby")+ "\t|"
			);
		}
		pstmt.close();
		rs.close();
	}
	
	public static void selectOneCity(int a) throws SQLException {
		String query = "select cit.* from city cit where id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, a);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			System.out.println("ID: " + rs.getInt("id") + "\t|" 
					+ "City Name: " + rs.getString("name") + "\t|"
			);
		}
		pstmt.close();
		rs.close();
	}
	
	public static void selectOneCountry(int a) throws SQLException {
		String query = "select con.* from country con where id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, a);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			System.out.println("ID: " + rs.getInt("id") + "\t|" 
					+ "Country Name: " + rs.getString("name") + "\t|"
			);
		}
		pstmt.close();
		rs.close();
	}
	
	public static void joinPersonAndCity(int a) throws SQLException {
		String query = "select c.* from country c where id = ?;";
		String query2 = "select c.name, p.* from country c join "
				+ "person p on c.id = p.city_id;";
		Statement stmt = conn.createStatement();
		stmt.execute(query2);
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, a);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			System.out.println("ID: " + rs.getInt("id") + "\t|" 
					+ "First Name: " + rs.getString("p.first_name") + "\t|"
					+ "Last name: " + rs.getString("p.last_name") + "\t|"
					+ "Age: " + rs.getInt("p.age") + "\t|"
					+ "Hobby" + rs.getString("p.hobby")+ "\t|"
					+ "City: " + rs.getString("c.name") + "\t|"
					);	
			}
	}
	
	public static void randomFill (int i) throws SQLException {
		String query = "insert into person (first_name, last_name, age, hobby, city_id, country_id) values(?, ?, ?, ?, ?, ?)";
		
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, "John ");
		pstmt.setString(2, "McLane ");
		pstmt.setInt(3, i + 10);
		pstmt.setString(4, " doing nothing ");
		pstmt.setInt(5, 1);
		pstmt.setInt(6, 1);
		
		pstmt.executeUpdate();
		pstmt.close();
		
	}
}
