import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PeopleMenu {
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunField;
    private JSpinner jumlahSpinner;
    private JComboBox kategoriBox;
    private JRadioButton tidakAktifRadioButton;
    private JRadioButton pensiunRadioButton;
    private JRadioButton aktifRadioButton;
    private JTable karyawanTable;
    private JButton updateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JLabel kategoriLabel;
    private JLabel judulLabel;
    private ArrayList<Person> listPenulis;
    private int selectedIndex = -1;

    public PeopleMenu(){
        listPenulis = new ArrayList<>();
        populateList();

        judulLabel.setText("Data Penulis");
        judulLabel.setFont(judulLabel.getFont().deriveFont(Font.BOLD, 30f));
        judulLabel.setHorizontalAlignment(SwingConstants.CENTER);

        String[] kategoriData = {"Novel", "Esai", "Non-Fiksi", "Biografi", "Puisi", "Cerpen"};
        kategoriBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(aktifRadioButton);
        statusGroup.add(pensiunRadioButton);
        statusGroup.add(tidakAktifRadioButton);
        aktifRadioButton.setSelected(true);

        jumlahSpinner.setModel(new SpinnerNumberModel(0, 0,10000,1));

        Font inputFont = new Font("SansSerif", Font.PLAIN, 14);
        idField.setFont(inputFont);
        namaField.setFont(inputFont);
        tahunField.setFont(inputFont);
        kategoriBox.setFont(inputFont);
        jumlahSpinner.setFont(inputFont);

        karyawanTable.setModel(setTable());
        karyawanTable.setFillsViewportHeight(true);
        karyawanTable.setRowHeight(26);
        karyawanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        karyawanTable.setAutoCreateRowSorter(true);

        updateButton.setText("Add");
        deleteButton.setEnabled(false);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex == -1){
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        karyawanTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener(){
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e){
                if (!e.getValueIsAdjusting()) {
                    int row = karyawanTable.getSelectedRow();
                    if (row != -1) {
                        selectedIndex = karyawanTable.convertRowIndexToModel(row);
                        showSelectedData(selectedIndex);
                    }
                }
            }
        }
        );
    }

    private void populateList(){
        listPenulis.add(new Person("P001", "Andrea Hirata", 1967, "Novel", 12, "Aktif"));
        listPenulis.add(new Person("P002", "Dee Lestari", 1976, "Novel", 15, "Aktif"));
        listPenulis.add(new Person("P003", "Joko Pinurbo", 1962, "Puisi", 20, "Tidak Aktif"));
        listPenulis.add(new Person("P004", "Seno Gumira Ajidarma", 1958, "Cerpen", 18, "Aktif"));
        listPenulis.add(new Person("P005", "Raditya Dika", 1984, "Non-Fiksi", 10, "Aktif"));
        listPenulis.add(new Person("P006", "Tere Liye", 1979, "Novel", 30, "Aktif"));
        listPenulis.add(new Person("P007", "Chairil Anwar", 1922, "Puisi", 15, "Pensiun"));
        listPenulis.add(new Person("P008", "Pramoedya Ananta Toer", 1925, "Novel", 25, "Pensiun"));
        listPenulis.add(new Person("P009", "Ahmad Tohari", 1948, "Novel", 18, "Aktif"));
        listPenulis.add(new Person("P010", "Mira W.", 1951, "Novel", 40, "Aktif"));
        listPenulis.add(new Person("P011", "Sapardi Djoko Damono", 1940, "Puisi", 22, "Pensiun"));
        listPenulis.add(new Person("P012", "Goenawan Mohamad", 1941, "Esai", 15, "Aktif"));
    }

    private String getStatus(){
        if(aktifRadioButton.isSelected()){
            return "Aktif";
        }
        if(tidakAktifRadioButton.isSelected()){
            return "Tidak Aktif";
        }
        if(pensiunRadioButton.isSelected()){
            return "Pensiun";
        }
        return "Aktif";
    }

    private void setStatus(String status){
        if("Aktif".equals(status)){
            aktifRadioButton.setSelected(true);
        } else if("Tidak Aktif".equals(status)){
            tidakAktifRadioButton.setSelected(true);
        } else if("Pensiun".equals(status)){
            pensiunRadioButton.setSelected(true);
        } else {
            aktifRadioButton.setSelected(true);
        }
    }

    public DefaultTableModel setTable(){
        Object[] columns = {"ID", "Nama", "Tahun Lahir", "Kategori", "Jumlah Karya", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0){
            @Override
            public  boolean isCellEditable(
                    int row, int column
            ){
                return false;
            }
        };

        for(Person penulis: listPenulis){
            Object[] row = {penulis.getId(), penulis.getNama(), penulis.getTahunLahir(), penulis.getKategori(), penulis.getJumlahKarya(), penulis.getStatusPenulis()};
            model.addRow(row);
        }
        return model;
    }

    private Person createPenulis(){
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        int tahunlahir = Integer.parseInt(tahunField.getText().trim());
        String kategori = (String) kategoriBox.getSelectedItem();
        int jumlahkarya = ((Number) jumlahSpinner.getValue()).intValue();
        String status = getStatus();
        return new Person(id, nama, tahunlahir, kategori, jumlahkarya, status);
    }

    private boolean validateForm(){
        if(idField.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(mainPanel, "ID penulis wajib diisi.");
            idField.requestFocus();
            return false;
        }

        if(namaField.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(mainPanel, "Nama penulis wajib diisi");
            namaField.requestFocus();
            return false;
        }

        int tahunlahir;
        try{
            tahunlahir = Integer.parseInt(tahunField.getText().trim());
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(mainPanel, "Tahun lahir harus berupa angka.");
            tahunField.requestFocus();
            return false;
        }

        if (tahunlahir < 1 || tahunlahir > 2026){
            JOptionPane.showMessageDialog(mainPanel, "Tahun lahir tidak valid.");
            tahunField.requestFocus();
            return false;
        }

        String id = idField.getText().trim();
        for(int i = 0; i < listPenulis.size(); i++){
            if(i != selectedIndex && listPenulis.get(i).getId().equalsIgnoreCase(id)){
                JOptionPane.showMessageDialog(mainPanel, "ID penulis sudah digunakan.");
                idField.requestFocus();
                return false;
            }
        }
        return true;
    }

    public void insertData(){
        if(!validateForm()){
            return;
        }

        Person penulis = createPenulis();
        listPenulis.add(penulis);
        karyawanTable.setModel(setTable());
        clearForm();
        JOptionPane.showMessageDialog(mainPanel, "Data penulis berhasil ditambahkan");
    }

    public void updateData(){
        if(!validateForm()){
            return;
        }
        if(selectedIndex < 0 || selectedIndex >= listPenulis.size()){
            return;
        }

        Person penulis = createPenulis();
        listPenulis.set(selectedIndex, penulis);
        karyawanTable.setModel(setTable());
        clearForm();
        JOptionPane.showMessageDialog(mainPanel, "Data berhasil diperbaharui.");
    }

    public void deleteData(){
        if(selectedIndex < 0 || selectedIndex >= listPenulis.size()){
            return;
        }

        int action = JOptionPane.showConfirmDialog(mainPanel, "Yakin ingin menghapus data?", "Konfirmasi hapus", JOptionPane.YES_NO_OPTION);
        if(action == JOptionPane.YES_OPTION){
            listPenulis.remove(selectedIndex);
            karyawanTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(mainPanel, "Data berhasil dihapus.");
        }
    }

    private void showSelectedData(int index){
        if(index < 0 || index >= listPenulis.size()){
            return;
        }

        Person penulis = listPenulis.get(index);
        idField.setText(penulis.getId());
        namaField.setText(penulis.getNama());
        tahunField.setText(String.valueOf(penulis.getTahunLahir()));
        kategoriBox.setSelectedItem(penulis.getKategori());
        jumlahSpinner.setValue(penulis.getJumlahKarya());
        setStatus(penulis.getStatusPenulis());

        updateButton.setText("Update");
        deleteButton.setEnabled(true);
    }

    public void clearForm(){
        idField.setText("");
        namaField.setText("");
        tahunField.setText("");
        jumlahSpinner.setValue(0);
        kategoriBox.setSelectedIndex(0);
        aktifRadioButton.setSelected(true);
        selectedIndex = -1;
        karyawanTable.clearSelection();
        updateButton.setText("Add");
        deleteButton.setEnabled(false);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PeopleMenu form = new PeopleMenu();
                JFrame menu = new JFrame("Writer Database");
                menu.setContentPane(form.mainPanel);
                menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menu.setSize(650, 530);
                menu.setLocationRelativeTo(null);
                menu.setVisible(true);
            }
        });
    }
}
