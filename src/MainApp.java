/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

//ini cok, tadi hitung tinggi limas internal itu buat ngitung tinggi limas pake data dari super (segitiga, udah di public)
//nama luas permukaan sama volume di limas dan prisma sudah di spesifikkan

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import com.formdev.flatlaf.FlatLightLaf;


public class MainApp extends JFrame {

    //ini di ubah: semua komponen GUI menjadi public (haram private)
    public JButton btnGenerate;
    public JLabel lblProgress1, lblProgress2, lblProgress3;
    public JProgressBar pbThread1, pbThread2, pbThread3;
    
    //ini di ubah: menambah variabel kotak input dinamis
    public JTextField txtJumlahData; 

    //ini di ubah: memecah variabel 1 tabel besar menjadi 3 tabel terpisah
    public JTable tabelSegitiga, tabelPrisma, tabelLimas;
    public DefaultTableModel modelSegitiga, modelPrisma, modelLimas;

    public MainApp() {
        // Langkah 1: Membuat Frame
        super("Sistem Komputasi Geometri Multithreading - 3 Tabel");

        // Langkah 2: Membuat Komponen
        btnGenerate = new JButton("Generate Data");
        lblProgress1 = new JLabel(" Thread 1 (2D Segitiga):");
        lblProgress2 = new JLabel(" Thread 2 (3D Prisma):");
        lblProgress3 = new JLabel(" Thread 3 (3D Limas):");
        
        pbThread1 = new JProgressBar(0, 10000); pbThread1.setStringPainted(true);
        pbThread2 = new JProgressBar(0, 10000); pbThread2.setStringPainted(true);
        pbThread3 = new JProgressBar(0, 10000); pbThread3.setStringPainted(true);

        //ini di ubah: memisahkan kolom menjadi 3 array berbeda untuk masing-masing tabel
        String[] kolSegitiga = {"No", "Alas S3", "Tinggi S3", "Luas S3", "Keliling S3"};
        String[] kolPrisma = {"No", "Vol Prisma", "LP Prisma"};
        String[] kolLimas = {"No", "Vol Limas", "LP Limas"};

        //ini di ubah: menginisialisasi 3 model dan 3 tabel
        modelSegitiga = new DefaultTableModel(kolSegitiga, 0);
        modelPrisma = new DefaultTableModel(kolPrisma, 0);
        modelLimas = new DefaultTableModel(kolLimas, 0);

        tabelSegitiga = new JTable(modelSegitiga);
        tabelPrisma = new JTable(modelPrisma);
        tabelLimas = new JTable(modelLimas);

        // Langkah 3: Container
        Container container = this.getContentPane();

        // Langkah 4: Layout Manager
        container.setLayout(new BorderLayout());
        
        //ini di ubah: Mengubah grid panel utara jadi 5 baris untuk menaruh kotak input
        JPanel panelUtara = new JPanel(new GridLayout(5, 2, 5, 5)); 
        panelUtara.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //ini di ubah: instansiasi kotak input dengan default 1000
        txtJumlahData = new JTextField("1000"); 

        // Langkah 5: Meletakkan Komponen Utara
        //ini di ubah: Memasukkan label dan kotak input ke dalam panel utara
        panelUtara.add(new JLabel(" Jumlah Data (Max 50.000):"));
        panelUtara.add(txtJumlahData);
        panelUtara.add(lblProgress1); panelUtara.add(pbThread1);
        panelUtara.add(lblProgress2); panelUtara.add(pbThread2);
        panelUtara.add(lblProgress3); panelUtara.add(pbThread3);
        panelUtara.add(new JLabel("")); panelUtara.add(btnGenerate);
        container.add(panelUtara, BorderLayout.NORTH);

        //ini di ubah: Membuat panel tengah dengan GridLayout(3,1) agar 3 tabel tersusun atas-bawah
        JPanel panelTengah = new JPanel(new GridLayout(3, 1, 5, 5));
        
        //ini di ubah: Membungkus tabel dengan JScrollPane dan memberi Judul Border agar rapi
        JScrollPane scrollS3 = new JScrollPane(tabelSegitiga);
        scrollS3.setBorder(BorderFactory.createTitledBorder("Tabel Hasil Segitiga (Thread 1)"));
        
        JScrollPane scrollPrisma = new JScrollPane(tabelPrisma);
        scrollPrisma.setBorder(BorderFactory.createTitledBorder("Tabel Hasil Prisma (Thread 2)"));
        
        JScrollPane scrollLimas = new JScrollPane(tabelLimas);
        scrollLimas.setBorder(BorderFactory.createTitledBorder("Tabel Hasil Limas (Thread 3)"));

        //ini di ubah: memasukkan 3 tabel ke panel tengah
        panelTengah.add(scrollS3);
        panelTengah.add(scrollPrisma);
        panelTengah.add(scrollLimas);

        container.add(panelTengah, BorderLayout.CENTER);

        // Langkah 6: Event Handling
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jalankanMasterThread();
            }
        });

        // Pengaturan dasar Frame (Diperbesar agar 3 tabel muat)
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    //ini di ubah: modifier diubah jadi public
    public void jalankanMasterThread() {
        btnGenerate.setEnabled(false);
        
        //ini di ubah: Mereset isi ketiga tabel menjadi kosong (0 baris)
        modelSegitiga.setRowCount(0); 
        modelPrisma.setRowCount(0);
        modelLimas.setRowCount(0);

        Thread masterThread = new Thread(() -> {
            //ini di ubah: Mengambil data jumlah secara dinamis dengan proteksi error
            int inputData = 1000;
            try {
                inputData = Integer.parseInt(txtJumlahData.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid! Menggunakan 1000 data.");
                txtJumlahData.setText("1000");
            }
            final int jumlahData = Math.min(inputData, 50000); // Batas Max

            //ini di ubah: Set limit ujung Progress Bar secara dinamis
            SwingUtilities.invokeLater(() -> {
                pbThread1.setMaximum(jumlahData);
                pbThread2.setMaximum(jumlahData);
                pbThread3.setMaximum(jumlahData);
                pbThread1.setValue(0);
                pbThread2.setValue(0);
                pbThread3.setValue(0);
            });
            
            //ini di ubah: Memecah matriks besar menjadi 3 array khusus untuk masing-masing tabel
            String[][] dataSegitiga = new String[jumlahData][5];
            String[][] dataPrisma = new String[jumlahData][3];
            String[][] dataLimas = new String[jumlahData][3];
            
            Random rand = new Random();

            double[] kumpulanAlasMentah = new double[jumlahData];
            double[] kumpulanTinggiMentah = new double[jumlahData];
            double[] kumpulanTinggiPrismaMentah = new double[jumlahData];

            for (int i = 0; i < jumlahData; i++) {
                kumpulanAlasMentah[i] = 1 + (rand.nextDouble() * 49); 
                kumpulanTinggiMentah[i] = 1 + (rand.nextDouble() * 49);
                kumpulanTinggiPrismaMentah[i] = 1 + (rand.nextDouble() * 49);
                
                // Mengisi Nomor Proses
                dataSegitiga[i][0] = String.valueOf(i + 1);
                dataPrisma[i][0] = String.valueOf(i + 1);
                dataLimas[i][0] = String.valueOf(i + 1);
                
                // Mengisi Alas dan Tinggi di tabel Segitiga saja
                dataSegitiga[i][1] = String.format("%.2f", kumpulanAlasMentah[i]);
                dataSegitiga[i][2] = String.format("%.2f", kumpulanTinggiMentah[i]);
            }

            //ini di ubah: Variabel pelacak agar Master Thread tahu seberapa jauh Segitiga sudah jalan
            final int[] trackerSegitiga = {0};

            // --- THREAD 1: Menghitung 2D Segitiga ---
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < jumlahData; i++) {
                    Segitiga s3 = new Segitiga(kumpulanAlasMentah[i], kumpulanTinggiMentah[i]);
                    
                    dataSegitiga[i][3] = String.format("%.2f", s3.luas);
                    dataSegitiga[i][4] = String.format("%.2f", s3.keliling);
                    
                    final int progress = i + 1;
                    
                    // Update pelacak progress untuk Master Thread
                    trackerSegitiga[0] = progress; 
                    
                    SwingUtilities.invokeLater(() -> pbThread1.setValue(progress));
                    
                    // INI YANG DIUBAH: Mesin Segitiga di-upgrade!
                    // Dia HANYA istirahat 1 milidetik setiap 100 langkah. 
                    // Ini menjamin Segitiga akan selalu jadi yang tercepat dan tidak mungkin kesalip!
                    if (i % 100 == 0) {
                        try { Thread.sleep(1); } catch (Exception ex) {} 
                    }
                }
            });

            //ini di ubah: Menentukan batas pergantian fase salip-menyalip (tiap 150 data)
            int chunkSize = 150; 

            // --- THREAD 2: Menghitung 3D Prisma Segitiga ---
            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < jumlahData; i++) {
                    PrismaSegitiga prisma = new PrismaSegitiga(kumpulanAlasMentah[i], kumpulanTinggiMentah[i], kumpulanTinggiPrismaMentah[i]);
                    prisma.run(); 
                    
                    dataPrisma[i][1] = String.format("%.2f", prisma.volumePrisma);
                    dataPrisma[i][2] = String.format("%.2f", prisma.luasPermukaanPrisma);
                    
                    final int progress = i + 1;
                    SwingUtilities.invokeLater(() -> pbThread2.setValue(progress));
                    
                    //ini di ubah: Logika Gelombang Cosinus (Start Cepat -> Melambat -> Cepat lagi)
                    double kurva = Math.cos((double) i / jumlahData * Math.PI * 3.0); 
                    // Jeda range: 5 (paling lambat) sampai 49 (paling ngebut)
                    int jeda = (int) (27 + 22 * kurva); 
                    if (jeda < 1) jeda = 1;
                    
                    // Semakin besar nilai jeda, semakin jarang dia sleep = NGEBUT
                    if (i % jeda == 0) {
                        try { Thread.sleep(1); } catch (Exception ex) {} 
                    }
                }
            });

            // --- THREAD 3: Menghitung 3D Limas Segitiga ---
            Thread thread3 = new Thread(() -> {
                for (int i = 0; i < jumlahData; i++) {
                    LimasSegitiga limas = new LimasSegitiga(kumpulanAlasMentah[i], kumpulanTinggiMentah[i]);
                    limas.run();
                    
                    dataLimas[i][1] = String.format("%.2f", limas.volumeLimas);
                    dataLimas[i][2] = String.format("%.2f", limas.luasPermukaanLimas);
                    
                    final int progress = i + 1;
                    SwingUtilities.invokeLater(() -> pbThread3.setValue(progress));
                    
                    //ini di ubah: Logika Gelombang Sinus (Start Sedang -> Ngebut nyalip T2 -> Melambat)
                    double kurva = Math.sin((double) i / jumlahData * Math.PI * 3.0); 
                    int jeda = (int) (27 + 22 * kurva); 
                    if (jeda < 1) jeda = 1;
                    
                    if (i % jeda == 0) {
                        try { Thread.sleep(1); } catch (Exception ex) {} 
                    }
                }
            });

            //ini di ubah: SKENARIO BARU (STAGGERED START)
            
            // 1. Segitiga mulai lari duluan
            thread1.start();

            // 2. Master Thread memantau (menunggu) sampai Segitiga mencapai 30% perjalanan
            int targetSusul = jumlahData * 30 / 100;
            while (trackerSegitiga[0] < targetSusul) {
                try {
                    Thread.sleep(20); // Cek setiap 20 milidetik
                } catch (InterruptedException ex) {}
            }

            // 3. Setelah Segitiga menyentuh 30%, Prisma dan Limas tiba-tiba ikut lari menyusul!
            // (Segitiga tidak berhenti, tetap terus lari menuju 100%)
            thread2.start();
            thread3.start();

            // 4. Tunggu sampai ketiga-tiganya benar-benar menyentuh garis finish (100%)
            try {
                thread1.join();
                thread2.join();
                thread3.join();
            } catch (InterruptedException ex) {}

            // 5. Setelah semua progress bar mentok 100%, tuangkan semua datanya ke 3 Tabel
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < jumlahData; i++) {
                    modelSegitiga.addRow(dataSegitiga[i]);
                    modelPrisma.addRow(dataPrisma[i]);
                    modelLimas.addRow(dataLimas[i]);
                }
                btnGenerate.setEnabled(true); 
                JOptionPane.showMessageDialog(MainApp.this, "Balapan Selesai! Data Berhasil Ditampilkan.");
            });
        });

        masterThread.start();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Gagal Mengambil FlatLaf" + e.getMessage());
        }
        SwingUtilities.invokeLater(() -> {
            new MainApp().setVisible(true);
        });
    }
}