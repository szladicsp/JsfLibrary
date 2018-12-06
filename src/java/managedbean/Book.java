/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;

/**
 *
 * @author szlad
 */
@ManagedBean(name = "book")
@SessionScoped
public class Book implements Serializable {
    
    int id;
    String title;
    String author;
    int pageNumber;
    int prize;
   // Date yearOfPublication;
    boolean inStorage;
    int isbn;
    
    ArrayList bookList;
    private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    Connection connection;

    //Connection strings
    private final String databaseUrl = "jdbc:mysql://localhost:3306/library?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final String user = "root";
    private final String password = "";
    //private static final long serialVersionUID = -1;
    
    public int getId() {
       return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
  
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public int getPageNumber() {
        return pageNumber;
    }
    
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    
    public int getPrize() {
        return prize;
    }
    
    public void setPrize(int prize) {
        this.prize = prize;
    }
    
//    public Date getYearOfPublication() {
//        return yearOfPublication;
//    }
//    
//    public void setYearOfPublication(Date yearOfPublication) {
//        this.yearOfPublication = yearOfPublication;
//    }
    
    public boolean isInStorage() {
        return inStorage;
    }
    
    public void setInStorage(boolean inStorage) {
        this.inStorage = inStorage;
    }
    
    public int getIsbn() {
        return isbn;
    }
    
    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }
    public ArrayList getBookList() {
        return bookList;
    }

    public void setBookList(ArrayList bookList) {
        this.bookList = bookList;
    }

    public Map<String, Object> getSessionMap() {
        return sessionMap;
    }

    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

    //Kapcsolat léátrehozása
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(databaseUrl, user, password);
        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }
    
    public ArrayList bookList() {
        try {
            bookList = new ArrayList();
            connection = getConnection();
            Statement stmt = getConnection().createStatement();            
            ResultSet rs = stmt.executeQuery("select * from books");            
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPageNumber(rs.getInt("pageNumber"));
                book.setPrize(rs.getInt("prize"));
               // book.setYearOfPublication(rs.getDate("yearOfPublication"));
                book.setInStorage(rs.getBoolean("inStorage"));
                book.setIsbn(rs.getInt("isbn"));
                bookList.add(book);
                
                
                
            }
            connection.close();            
        } catch (Exception e) {
            System.out.println(e);
        }
        return bookList;
    }
    
    public boolean save() {
        int result = 0;
        try {
            
            connection = getConnection();
            
            PreparedStatement stmt = connection.prepareStatement("insert into books(title, author,pageNumber,prize,inStorage,isbn) values(?,?,?,?,?,?)");
           // stmt.setInt(1, id);//yearOfPublication, ?
            stmt.setString(1, title);
            stmt.setString(2,author);
            stmt.setInt(3, pageNumber);
            stmt.setInt(4, prize);
            //stmt.setDate(6, yearOfPublication);
            stmt.setBoolean(5, inStorage);
            stmt.setInt(6,isbn);
            result = stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (result != 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public String submit() {
        if (this.save()) {
            System.out.println("submitig lefut a program");
            return "response.xhtml";
        } else {
            System.out.println("Az index html-t nyitja meg mindig");
            return "index.xhtml";
        }
    }
    
    
}
