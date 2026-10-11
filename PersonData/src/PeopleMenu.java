import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PeopleMenu {
    // Komponen GUI
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunField;
    private JSpinner jumlahSpinner;
    private JComboBox kategoriBox;
    // Radio button untuk menentukan status penulis
    private JRadioButton tidakAktifRadioButton;
    private JRadioButton pensiunRadioButton;
    private JRadioButton aktifRadioButton;
    // Komponen tabel dan tombol
    private JTable karyawanTable;
    private JButton updateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JLabel kategoriLabel;
    private JLabel judulLabel;
    // Menyimpan data penulis dan indeks baris yang dipilih
    private ArrayList<Person> listPenulis;
    private int selectedIndex = -1;

    // Konstruktor
    public PeopleMenu(){
        // Menginisialisasi ArrayList untuk menyimpan data penulis
        listPenulis = new ArrayList<>();
        // Memasukkan data dummy ke dalam ArrayList
        populateList();

        // Mengatur teks, ukuran, dan posisi judul
        judulLabel.setText("Data Penulis");
        judulLabel.setFont(judulLabel.getFont().deriveFont(Font.BOLD, 30f));
        judulLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Menentukan pilihan kategori yang tersedia dan memasukkan pilihan ke dalam ComboBox
        String[] kategoriData = {"Novel", "Esai", "Non-Fiksi", "Biografi", "Puisi", "Cerpen"};
        kategoriBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // Mengelompokkan RadioButton agar hanya satu yang dipilih
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(aktifRadioButton);
        statusGroup.add(pensiunRadioButton);
        statusGroup.add(tidakAktifRadioButton);
        // Statu awal adalah aktif
        aktifRadioButton.setSelected(true);

        // Mengatur nilai minimum, maksimum, dan kenaikan spinner
        jumlahSpinner.setModel(new SpinnerNumberModel(0, 0,10000,1));

        // Mengatur font pada komponen input
        Font inputFont = new Font("SansSerif", Font.PLAIN, 14);
        idField.setFont(inputFont);
        namaField.setFont(inputFont);
        tahunField.setFont(inputFont);
        kategoriBox.setFont(inputFont);
        jumlahSpinner.setFont(inputFont);

        // Mengatur model dan tampilan tabel
        karyawanTable.setModel(setTable());
        karyawanTable.setFillsViewportHeight(true);
        karyawanTable.setRowHeight(26);
        karyawanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        karyawanTable.setAutoCreateRowSorter(true);

        // Mengatur teks dan kondisi awal tombol
        updateButton.setText("Add");
        deleteButton.setEnabled(false);

        // Listener untuk tombol Add dan Update
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Jika belum ada baris yang dipilih, maka tambahkan data
                if(selectedIndex == -1){
                    insertData();
                } else {
                // Jika ada baris yang dipilih, maka perbarui data
                    updateData();
                }
            }
        });

        // Listener untuk tombol Cancel
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mengosongkan form dan membatalkan pilihan
                clearForm();
            }
        });

        // Listener untuk tombol Delete
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menjalankan proses penghapusan data
                deleteData();
            }
        });

        // Listener untuk mendeteksi pemilihan baris pada tabel
        karyawanTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener(){
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e){
                // Memproses perubahan setelah pemilihan selsai
                if (!e.getValueIsAdjusting()) {
                    int row = karyawanTable.getSelectedRow();
                    // Memastikan ada baris yang dipilih
                    if (row != -1) {
                        // Mengubah indeks tampilan menjadi indeks model
                        selectedIndex = karyawanTable.convertRowIndexToModel(row);
                        // Menampilkan data terpilih ke forrm
                        showSelectedData(selectedIndex);
                    }
                }
            }
        });
    }

    // Mengisi ArrayList dengan data dummy
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

    // Mengambil status berdasarkan RadioButton yang dipilih
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
        // Aktif sebagai status defaul
        return "Aktif";
    }

    // Memilih RadioButton sesuai status dari data penulis
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

    // Membuat model tabel berdasarkan data ArrayList
    public DefaultTableModel setTable(){
        // Menentukan nama kolom tabel
        Object[] columns = {"ID", "Nama", "Tahun Lahir", "Kategori", "Jumlah Karya", "Status"};
        // Membuat model tabel yang tidak dapat diedit langsung
        DefaultTableModel model = new DefaultTableModel(columns, 0){
            @Override
            public  boolean isCellEditable(int row, int column){
                return false;
            }
        };

        // Memasukkan data penulis ke dalam tabel
        for(Person penulis: listPenulis){
            Object[] row = {penulis.getId(), penulis.getNama(), penulis.getTahunLahir(), penulis.getKategori(), penulis.getJumlahKarya(), penulis.getStatusPenulis()};
            model.addRow(row);
        }
        return model;
    }

    // Membuat objek Person berdasarkan input dari form
    private Person createPenulis(){
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        int tahunlahir = Integer.parseInt(tahunField.getText().trim());
        String kategori = (String) kategoriBox.getSelectedItem();
        int jumlahkarya = ((Number) jumlahSpinner.getValue()).intValue();
        String status = getStatus();
        return new Person(id, nama, tahunlahir, kategori, jumlahkarya, status);
    }

    // Memeriksa kelengkapan dan validitas input form
    private boolean validateForm(){
        // Memastikan ID penulis tidak kosong
        if(idField.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(mainPanel, "ID penulis wajib diisi.");
            idField.requestFocus();
            return false;
        }

        // Memastikan nama penulis tidak kosong
        if(namaField.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(mainPanel, "Nama penulis wajib diisi");
            namaField.requestFocus();
            return false;
        }

        // Mengubah tahun lahir menjadi bilangan bulat
        int tahunlahir;
        try{
            tahunlahir = Integer.parseInt(tahunField.getText().trim());
        } catch (NumberFormatException e){
            // Menampilkan pesan jika tahun bukan angka
            JOptionPane.showMessageDialog(mainPanel, "Tahun lahir harus berupa angka.");
            tahunField.requestFocus();
            return false;
        }

        // Memastikan tahun lahir berada dalam rentang waktu yang valid
        if (tahunlahir < 1 || tahunlahir > 2026){
            JOptionPane.showMessageDialog(mainPanel, "Tahun lahir tidak valid.");
            tahunField.requestFocus();
            return false;
        }

        // Memeriksa apakah ID sudah digunakn
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

    // Menambahkan data penulis baru ke dalam ArrayList
    public void insertData(){
        if(!validateForm()){
            return;
        }

        // Membuat objek penulis dari input forrm dan memasukkan ke dalam ArrayList
        Person penulis = createPenulis();
        listPenulis.add(penulis);
        
        // Memperbarui tabel dan mengosongkan form
        karyawanTable.setModel(setTable());
        clearForm();
        JOptionPane.showMessageDialog(mainPanel, "Data penulis berhasil ditambahkan");
    }

    // Memperbarui data penulis yang dipilih
    public void updateData(){
        if(!validateForm()){
            return;
        }
        // Memastikan indeks yang dipilih valid
        if(selectedIndex < 0 || selectedIndex >= listPenulis.size()){
            return;
        }

        Person penulis = createPenulis();
        // Mengganti data lama dengan data baru
        listPenulis.set(selectedIndex, penulis);

        // Memperbarui data lama dengan data baru
        karyawanTable.setModel(setTable());
        clearForm();
        JOptionPane.showMessageDialog(mainPanel, "Data berhasil diperbaharui.");
    }

    // Menghapus data penulis yang dipilih
    public void deleteData(){
        if(selectedIndex < 0 || selectedIndex >= listPenulis.size()){
            return;
        }

        // Meminta konfirmasi sebelum menghapus data
        int action = JOptionPane.showConfirmDialog(mainPanel, "Yakin ingin menghapus data?", "Konfirmasi hapus", JOptionPane.YES_NO_OPTION);
        // Menghapus data jika pengguna memilih YES
        if(action == JOptionPane.YES_OPTION){
            listPenulis.remove(selectedIndex);
            karyawanTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(mainPanel, "Data berhasil dihapus.");
        }
    }

    // Menampilkan data terpilih ke dalam form
    private void showSelectedData(int index){
        if(index < 0 || index >= listPenulis.size()){
            return;
        }

        // Mengambil objek penulis dari ArrayList
        Person penulis = listPenulis.get(index);
        // Mengisi field berdasarkan data penulis yang dipilih
        idField.setText(penulis.getId());
        namaField.setText(penulis.getNama());
        tahunField.setText(String.valueOf(penulis.getTahunLahir()));
        kategoriBox.setSelectedItem(penulis.getKategori());
        jumlahSpinner.setValue(penulis.getJumlahKarya());
        setStatus(penulis.getStatusPenulis());

        // Mengubah tombol add menjadi update
        updateButton.setText("Update");
        // Mengaktifkan tombol delete
        deleteButton.setEnabled(true);
    }

    // Mengosongkon semua input dan membatalkan pemilihan data
    public void clearForm(){
        idField.setText("");
        namaField.setText("");
        tahunField.setText("");
        jumlahSpinner.setValue(0);
        kategoriBox.setSelectedIndex(0);
        aktifRadioButton.setSelected(true);
        
        // Mengatur ulang indeks dan pilihan tabel
        selectedIndex = -1;
        karyawanTable.clearSelection();

        // Mengembalikan tombol ke kondisi awal
        updateButton.setText("Add");
        deleteButton.setEnabled(false);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Membuat objek form PeopleMenu
                PeopleMenu form = new PeopleMenu();
                // Membuat jendela utama aplikasi
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
