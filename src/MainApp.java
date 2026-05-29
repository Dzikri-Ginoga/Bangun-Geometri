/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MainApp extends JFrame {

    // Komponen GUI
    private JButton btnGenerate;
    private JLabel lblProgress1, lblProgress2, lblProgress3;
    private JProgressBar pbThread1, pbThread2, pbThread3;
    private JTable tableData;
    private DefaultTableModel tableModel;

    public MainApp() {
        // Langkah 1: Membuat Frame
        super("Sistem Komputasi Geometri Multithreading - Revisi Atribut Public");

        // Langkah 2: Membuat Komponen
        btnGenerate = new JButton("Generate Data (10.000)");
        lblProgress1 = new JLabel(" Thread 1 (2D Segitiga):");
        lblProgress2 = new JLabel(" Thread 2 (3D Prisma):");
        lblProgress3 = new JLabel(" Thread 3 (3D Limas):");
        
        pbThread1 = new JProgressBar(0, 10000); pbThread1.setStringPainted(true);
        pbThread2 = new JProgressBar(0, 10000); pbThread2.setStringPainted(true);
        pbThread3 = new JProgressBar(0, 10000); pbThread3.setStringPainted(true);

        String[] kolom = {"No", "Alas S3", "Tinggi S3", "Luas S3", "Keliling S3", "Vol Prisma", "LP Prisma", "Vol Limas", "LP Limas"};
        tableModel = new DefaultTableModel(kolom, 0);
        tableData = new JTable(tableModel);

        // Langkah 3: Container
        Container container = this.getContentPane();

        // Langkah 4: Layout Manager
        container.setLayout(new BorderLayout());
        JPanel panelUtara = new JPanel(new GridLayout(4, 2, 5, 5));
        panelUtara.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Langkah 5: Meletakkan Komponen
        panelUtara.add(lblProgress1); panelUtara.add(pbThread1);
        panelUtara.add(lblProgress2); panelUtara.add(pbThread2);
        panelUtara.add(lblProgress3); panelUtara.add(pbThread3);
        panelUtara.add(new JLabel("")); panelUtara.add(btnGenerate);

        container.add(panelUtara, BorderLayout.NORTH);
        container.add(new JScrollPane(tableData), BorderLayout.CENTER);

        // Langkah 6: Event Handling
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jalankanMasterThread();
            }
        });

        // Pengaturan dasar Frame
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void jalankanMasterThread() {
        btnGenerate.setEnabled(false);
        tableModel.setRowCount(0); // Reset isi tabel

        // Master Thread agar GUI tidak Freeze (Pilar 5: Multithreading)
        Thread masterThread = new Thread(() -> {
            int jumlahData = 10000;
            
            // Matriks untuk menampung hasil data dari 3 thread yang saling balapan
            // Tipe data String agar mudah diformat 2 angka di belakang koma
            String[][] hasilKomputasi = new String[jumlahData][9];
            Random rand = new Random();

            // Men-generate 10.000 bahan baku (Alas, Tinggi, Tinggi Prisma)
            double[] arrAlas = new double[jumlahData];
            double[] arrTinggi = new double[jumlahData];
            double[] arrTinggiPrisma = new double[jumlahData];

            for (int i = 0; i < jumlahData; i++) {
                arrAlas[i] = 1 + (rand.nextDouble() * 49); // Angka acak 1.0 - 50.0
                arrTinggi[i] = 1 + (rand.nextDouble() * 49);
                arrTinggiPrisma[i] = 1 + (rand.nextDouble() * 49);
                
                hasilKomputasi[i][0] = String.valueOf(i + 1);
                hasilKomputasi[i][1] = String.format("%.2f", arrAlas[i]);
                hasilKomputasi[i][2] = String.format("%.2f", arrTinggi[i]);
            }

            // --- THREAD 1: Menghitung 2D Segitiga ---
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < jumlahData; i++) {
                    Segitiga s3 = new Segitiga(arrAlas[i], arrTinggi[i]);
                    // Ingat: atribut sekarang public, jadi kita bisa langsung ambil nilainya
                    hasilKomputasi[i][3] = String.format("%.2f", s3.luas);
                    hasilKomputasi[i][4] = String.format("%.2f", s3.keliling);
                    
                    final int progress = i + 1;
                    // Wajib Mutlak: Update GUI dari dalam Thread harus pakai invokeLater
                    SwingUtilities.invokeLater(() -> pbThread1.setValue(progress));
                }
            });

            // --- THREAD 2: Menghitung 3D Prisma Segitiga ---
            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < jumlahData; i++) {
                    PrismaSegitiga prisma = new PrismaSegitiga(arrAlas[i], arrTinggi[i], arrTinggiPrisma[i]);
                    
                    // Karena Prisma meng-implements Runnable (Instruksi Baru),
                    // kita bisa memanggil .run() untuk mengeksekusi perhitungan internalnya
                    prisma.run(); 
                    
                    // Atribut public, langsung panggil variabelnya
                    hasilKomputasi[i][5] = String.format("%.2f", prisma.volume);
                    hasilKomputasi[i][6] = String.format("%.2f", prisma.luasPermukaan);
                    
                    final int progress = i + 1;
                    SwingUtilities.invokeLater(() -> pbThread2.setValue(progress));
                }
            });

            // --- THREAD 3: Menghitung 3D Limas Segitiga ---
            Thread thread3 = new Thread(() -> {
                for (int i = 0; i < jumlahData; i++) {
                    LimasSegitiga limas = new LimasSegitiga(arrAlas[i], arrTinggi[i]);
                    
                    // Mengeksekusi komputasi menggunakan Runnable bawaan class
                    limas.run();
                    
                    // Atribut public, langsung panggil variabelnya
                    hasilKomputasi[i][7] = String.format("%.2f", limas.volume);
                    hasilKomputasi[i][8] = String.format("%.2f", limas.luasPermukaan);
                    
                    final int progress = i + 1;
                    SwingUtilities.invokeLater(() -> pbThread3.setValue(progress));
                }
            });

            // Eksekusi ketiga Thread serentak untuk efek balapan di JProgressBar
            thread1.start();
            thread2.start();
            thread3.start();

            // Gunakan .join() agar Master Thread menunggu ketiga balapan selesai
            try {
                thread1.join();
                thread2.join();
                thread3.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            // Menuangkan 10.000 data yang sudah dihitung ke JTable dengan aman
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < jumlahData; i++) {
                    tableModel.addRow(hasilKomputasi[i]);
                }
                btnGenerate.setEnabled(true); // Tombol diaktifkan kembali
                JOptionPane.showMessageDialog(MainApp.this, "Komputasi 10.000 Objek Geometri Selesai secara Paralel!");
            });
        });

        masterThread.start();
    }

    public static void main(String[] args) {
        // Standar baku pemanggilan GUI Java Swing
        SwingUtilities.invokeLater(() -> {
            new MainApp().setVisible(true);
        });
    }
}
