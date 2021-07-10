package view;

import controller.TableCheckBoxBorrowing;
import controller.databaseChange;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApproveBookReturn {
    public void returnBorrow() {
        //declare
        JFrame frame = new DefaultFrameSetting().defaultFrame();
        JPanel panel1 = new DefaultFrameSetting().defaultPanel();
        TableCheckBoxBorrowing table = new controller.TableCheckBoxBorrowing();
        DefaultTableModel model = new DefaultTableModel(getTableData(), getColumnData());
        JScrollPane scroll = new JScrollPane();
        JButton button = new JButton("Approve");
        JButton exit = new JButton("Keluar");

        //set position
        table.setBounds(50, 50, 1100, 300);
        scroll.setBounds(50, 50, 1100, 300);
        panel1.setSize(1150, 1150);
        button.setBounds(50, 400, 1100, 30);
        exit.setBounds(50, 450, 1100, 30);

        //set component
        table.setModel(model);
        scroll.setViewportView(table);

        //add component to panel
        panel1.add(scroll);

        //buttons action listener
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel tModel = (DefaultTableModel) table.getModel();
                for (int i = 0; i < tModel.getRowCount(); i++) {
                    Boolean checked = (Boolean) model.getValueAt(i, 8);
                    if (checked) {
                        new controller.databaseChange().updateBorrowing((int) model.getValueAt(i, 0), (int) model.getValueAt(i, 7));
                        if ((int) model.getValueAt(i, 7) != 0) {
                            new controller.databaseChange().updateUser((int) model.getValueAt(i, 2), (int) model.getValueAt(i, 7));
                        }
                    }
                }
                new OutputInfo().infoApprovePengembalian(true);
                frame.setVisible(false);
                new AdminMenu().adminMenu();
            }

        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new OutputInfo().exit();
                new AdminMenu().adminMenu();
            }

        });

        //add component frame
        frame.add(button);
        frame.add(exit);
        frame.add(panel1);

        //set frame size
        frame.setSize(1200, 600);
    }
    public String[] getColumnData() {
        String[] column = {"ID Pinjam", "Nama Peminjam", "ID User", "Judul Buku", "Lama Peminjaman", "Tanggal Pinjam", "Tanggal Kembali", "Denda", "Approval"};
        return column;
    }
    public Object[][] getTableData() {
        Admin admin = UserManager.getInstance().getAdmin();
        int idBranch = admin.getIdBranch();

        Object[][] data = new Object[new databaseChange().getAllBorrowList(idBranch, false).size()][9];
        for (int j = 0; j < new databaseChange().getAllBorrowList(idBranch, false).size(); j++) {
            Borrowing borrow = new databaseChange().getAllBorrowList(idBranch, false).get(j);
            Member member = (Member) new databaseChange().getAMember(borrow.getIdUser());
            data[j][0] = borrow.getIdBorrow();
            data[j][1] = member.getFirstName() + " " + member.getLastName();
            data[j][2] = member.getIdUser();
            data[j][3] = ((Book)new databaseChange().getABook(borrow.getIdBook())).getTitle();
            data[j][4] = borrow.getBorrowDays();
            Format formatter = new SimpleDateFormat("dd/MM/yyyy");
            data[j][5] = formatter.format(borrow.getDate());
            data[j][6] = formatter.format(new Date());
            int diffDays = (int) ((new Date().getTime() - borrow.getDate().getTime()) / (24 * 60 * 60 * 1000));
            System.out.println(diffDays);
            if (diffDays <= borrow.getBorrowDays()) {
                data[j][7] = 0;
            } else {
                diffDays -= borrow.getBorrowDays();
                data[j][7] = diffDays * 2000;
            }
            data[j][8] = Boolean.FALSE;
        }
        return data;
    }
}