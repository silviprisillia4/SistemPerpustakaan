
package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.*;


public class databaseChange {

    static controller.DatabaseHandler conn = new controller.DatabaseHandler();

    public boolean insertNewBook(PaidBook book) {
        conn.connect();
        String query = "INSERT INTO books VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.con.prepareStatement(query);
            stmt.setInt(1, book.getIdBook());
            stmt.setInt(2, book.getIdBranch());
            stmt.setString(3, book.getTitle());
            stmt.setString(4, book.getAuthor());
            stmt.setString(5, book.getGenre());
            stmt.setString(6, book.getPublisher());
            stmt.setInt(7, book.getPages());
            stmt.setInt(8, book.getBorrowPrice());
            stmt.setInt(9, book.getYear());
            stmt.executeUpdate();
            return (true);
        } catch (SQLException e) {
            e.printStackTrace();
            return (false);
        }
    }

    public Object getAMember(int idUser) {
        Object object = new Object();
        conn.connect();
        String query = "SELECT * FROM users WHERE idUser = '" + idUser + "'";
        try {
            Statement stmt = conn.con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String mail = rs.getString("email");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                UserTypeEnum type = UserTypeEnum.MEMBER;
                String password = rs.getString("password");
                if (type == UserTypeEnum.MEMBER) {
                    String address = rs.getString("address");
                    int cash = rs.getInt("cash");
                    int debt = rs.getInt("debt");
                    String phone = rs.getString("phoneNumber");
                    int idBranch = rs.getInt("idBranch");
                    object = new Member(idUser, firstName, lastName, mail, password, type, address, phone, cash, debt, idBranch);
                } else if (type == UserTypeEnum.ADMIN) {
                    int idBranch = rs.getInt("idBranch");
                    object = new Admin(idUser, firstName, lastName, mail, password, type, idBranch);
                } else {
                    object = new User(idUser, firstName, lastName, mail, password, type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return object;
    }

    public ArrayList<Member> getAllMember(int idBranch) {
        ArrayList<Member> members = new ArrayList<>();
        conn.connect();
        String query;

        if (idBranch == 0) {
            query = "SELECT * FROM Users WHERE type = '" + UserTypeEnum.MEMBER + "'";
        } else {
            query = "SELECT * FROM Users WHERE type = '" + UserTypeEnum.MEMBER + "' && idbranch = '" + idBranch + "'";
        }

        try {
            Statement stmt = conn.con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Member member = new Member();
                member.setAddress(rs.getString("address"));
                member.setCash(rs.getInt("cash"));
                member.setDebt(rs.getInt("debt"));
                member.setEmail(rs.getString("email"));
                member.setFirstName(rs.getString("firstname"));
                member.setLastName(rs.getString("lastname"));
                member.setPhoneNumber(rs.getString("phonenumber"));
                member.setIdBranch(rs.getInt("idbranch"));
                member.setType(UserTypeEnum.MEMBER);
                member.setPassword(rs.getString("password"));
                member.setIdUser(rs.getInt("iduser"));
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public Admin getAdmin(int idUser) {
        Admin admin = new Admin();
        conn.connect();
        String query;
        query = "SELECT * FROM Users WHERE type = '" + UserTypeEnum.ADMIN + "' && idUser = '" + idUser +"'";
        try {
            Statement stmt = conn.con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                admin.setFirstName(rs.getString("firstName"));
                admin.setLastName(rs.getString("lastName"));
                admin.setIdUser(rs.getInt("iduser"));
                admin.setIdBranch(rs.getInt("idBranch"));
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Borrowing> getAllBorrowList(int id, boolean condition) {
        ArrayList<Borrowing> listBorrows = new ArrayList<>();
        conn.connect();
        String query = "";
        if (condition) {
            query = "SELECT * FROM Borrows WHERE iduser = '" + id + "'";
        } else {
            query = "SELECT * FROM Borrows WHERE idbranch = '" + id + "' && status = '0'";
        }

        try {
            Statement stmt = conn.con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Borrowing borrow = new Borrowing();
                borrow.setBorrowDays(rs.getInt("borrowdays"));
                borrow.setDate(rs.getDate("date"));
                borrow.setIdBook(rs.getInt("idbook"));
                borrow.setPriceTotal(rs.getInt("pricetotal"));
                borrow.setIdBranch(rs.getInt("idbranch"));
                borrow.setIdUser(rs.getInt("idUser"));
                borrow.setIdBorrow(rs.getInt("idBorrow"));
                borrow.setMoneyFine(rs.getInt("moneyFine"));
                borrow.setStatus(rs.getInt("status"));
                listBorrows.add(borrow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listBorrows;
    }

    public boolean updateBorrowing(int idBorrow, int finePrice) {
        conn.connect();
        String query = "SELECT * FROM borrows WHERE idBorrow = '" + idBorrow + "'";
        try {
            Statement stmt = conn.con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                if (updateBookState(rs.getInt("idBook"))) {
                    query = "UPDATE borrows SET status='1', moneyFine = '" + finePrice + "' WHERE idBorrow = '" + idBorrow + "'";
                    try {
                        stmt.executeUpdate(query);
                        return (true);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return (false);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBookState(int idBook) {
        conn.connect();
        String query = "UPDATE books SET status='1' WHERE idBook = '" + idBook + "'";
        try {
            Statement stmt = conn.con.createStatement();
            stmt.executeUpdate(query);
            return (true);
        } catch (SQLException e) {
            e.printStackTrace();
            return (false);
        }
    }

    public Object getABook(int idBuku) {
        Object object = new Object();
        conn.connect();
        String query = "SELECT * FROM Books WHERE idBook = '" + idBuku + "'";

        try {
            Statement stmt = conn.con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                int idBook = rs.getInt("idBook");
                int idBranch = rs.getInt("idBranch");
                int pages = rs.getInt("pages");
                String publisher = rs.getString("publisher");
                int year = rs.getInt("year");
                int availability = rs.getInt("status");
                if (rs.getInt("borrowPrice") != 0) {
                    int borrowPrice = rs.getInt("borrowPrice");
                    object = new PaidBook(idBook, idBranch, title, author, publisher, pages, year, genre, availability, borrowPrice);
                } else {
                    object = new Book(idBook, idBranch, title, author, publisher, pages, year, genre, availability);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static boolean updateBook(PaidBook book) {
        conn.connect();
        String query = "UPDATE books SET borrowPrice='" + book.getBorrowPrice() + "' WHERE idBook = '" + book.getIdBook() + "'";
        try {
            Statement stmt = conn.con.createStatement();
            stmt.executeUpdate(query);
            return (true);
        } catch (SQLException e) {
            e.printStackTrace();
            return (false);
        }
    }

    public void updateUser(int idUser, int fine) {
        conn.connect();
        Member member = new Member();
        int cash;
        String query = "SELECT * FROM users WHERE idUser ='" + idUser + "'";
        try {
            Statement stmt = conn.con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                member.setCash(rs.getInt("cash"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (member.getCash() >= fine) {
            cash = member.getCash() - fine;
            query = "UPDATE users SET cash = '" + cash + "' WHERE idUser = '" + idUser + "'";
            try {
                Statement stmt = conn.con.createStatement();
                stmt.executeUpdate(query);
//                    return (true);
            } catch (SQLException e) {
                e.printStackTrace();
//                    return (false);
            }
        } else {
            cash = fine - member.getCash();
            query = "UPDATE users SET cash = '0', debt = '" + cash + "' WHERE idUser = '" + idUser + "'";
            try {
                Statement stmt = conn.con.createStatement();
                stmt.executeUpdate(query);
//                    return (true);
            } catch (SQLException e) {
                e.printStackTrace();
//                    return (false);
            }
        }

    }

    public void print() {
//        ArrayList<Member> members = getAllMember();
//
//        for (int i = 0; i < members.size(); i++) {
//            System.out.println(members.get(i).getFirstName());
//        }

//        for (int i = 0; i < getAllBooks().size(); i++) {
//            System.out.println(getAllBooks().get(i).getTitle());
//        }
//        for (int i = 0; i < getAllBorrowList().size(); i++) {
//            System.out.println(getAllBorrowList().get(i).getPriceTotal());
//        }
    }
}
