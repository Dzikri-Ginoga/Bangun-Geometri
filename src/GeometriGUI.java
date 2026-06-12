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

public class GeometriGUI extends JFrame {

    // ===================================================================
    // EXCEPTION KUSTOM: Didefinisikan sebagai inner class
    // Digunakan untuk error validasi input yang spesifik dari aplikasi ini
    // ===================================================================
    /**
     * Exception untuk menandai bahwa nilai input numerik dari user tidak valid.
     * Contoh: alas = -5, tinggi = 0, jumlahData = "abc"
     */
    public static class InvalidInputException extends Exception {

        public InvalidInputException(String message) {
            super(message);
        }
    }

    /**
     * Exception untuk menandai kegagalan fatal pada sebuah Thread worker.
     * Membungkus (wraps) exception aslinya sebagai "cause".
     */
    public static class ThreadComputationException extends RuntimeException {

        public ThreadComputationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ===================================================================
    // DEKLARASI KOMPONEN GUI (tidak diubah dari aslinya)
    // ===================================================================
    public JButton btnGenerate;
    public JLabel lblProgress1, lblProgress2, lblProgress3;
    public JProgressBar pbThread1, pbThread2, pbThread3;

    public JTextField txtJumlahData;
    public JComboBox<String> cbMode;
    public JTextField txtAlas;
    public JTextField txtTinggi;

    public JTable tabelSegitiga, tabelPrisma, tabelLimas;
    public DefaultTableModel modelSegitiga, modelPrisma, modelLimas;

    // ===================================================================
    // KONSTRUKTOR (tidak diubah dari aslinya)
    // ===================================================================
    public GeometriGUI() {
        super("Sistem Komputasi Geometri Multithreading - 3 Tabel");

        btnGenerate = new JButton("Generate Data");
        lblProgress1 = new JLabel(" Thread 1 (2D Segitiga):");
        lblProgress2 = new JLabel(" Thread 2 (3D Prisma):");
        lblProgress3 = new JLabel(" Thread 3 (3D Limas):");

        pbThread1 = new JProgressBar(0, 10000);
        pbThread1.setStringPainted(true);
        pbThread2 = new JProgressBar(0, 10000);
        pbThread2.setStringPainted(true);
        pbThread3 = new JProgressBar(0, 10000);
        pbThread3.setStringPainted(true);

        String[] kolSegitiga = {"No", "Alas Segitiga", "Tinggi Segitiga", "Luas Segitiga", "Keliling Segitiga"};
        String[] kolPrisma = {"No", "Tinggi Prisma", "Volume Prisma Segitiga", "LP Prisma Segitiga"};
        String[] kolLimas = {"No", "Tinggi Limas", "Volume Limas", "LP Limas"};

        modelSegitiga = new DefaultTableModel(kolSegitiga, 0);
        modelPrisma = new DefaultTableModel(kolPrisma, 0);
        modelLimas = new DefaultTableModel(kolLimas, 0);

        tabelSegitiga = new JTable(modelSegitiga);
        tabelPrisma = new JTable(modelPrisma);
        tabelLimas = new JTable(modelLimas);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel panelUtara = new JPanel(new GridLayout(8, 2, 5, 5));
        panelUtara.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] pilihanOpsi = {"Opsi 1: Generate Angka Random", "Opsi 2: Gunakan Input Manual"};
        cbMode = new JComboBox<>(pilihanOpsi);

        txtJumlahData = new JTextField("1000");
        txtAlas = new JTextField("10");
        txtTinggi = new JTextField("8");

        panelUtara.add(new JLabel(" Pilih Opsi:"));
        panelUtara.add(cbMode);

        panelUtara.add(new JLabel(" Jumlah Data:"));
        panelUtara.add(txtJumlahData);

        panelUtara.add(new JLabel("Input Alas: "));
        panelUtara.add(txtAlas);

        panelUtara.add(new JLabel("Input Tinggi: "));
        panelUtara.add(txtTinggi);

        panelUtara.add(lblProgress1);
        panelUtara.add(pbThread1);
        panelUtara.add(lblProgress2);
        panelUtara.add(pbThread2);
        panelUtara.add(lblProgress3);
        panelUtara.add(pbThread3);
        panelUtara.add(new JLabel(""));
        panelUtara.add(btnGenerate);
        container.add(panelUtara, BorderLayout.NORTH);

        JPanel panelTengah = new JPanel(new GridLayout(3, 1, 5, 5));

        JScrollPane scrollS3 = new JScrollPane(tabelSegitiga);
        scrollS3.setBorder(BorderFactory.createTitledBorder("Tabel Hasil Segitiga (Thread 1)"));

        JScrollPane scrollPrisma = new JScrollPane(tabelPrisma);
        scrollPrisma.setBorder(BorderFactory.createTitledBorder("Tabel Hasil Prisma (Thread 2)"));

        JScrollPane scrollLimas = new JScrollPane(tabelLimas);
        scrollLimas.setBorder(BorderFactory.createTitledBorder("Tabel Hasil Limas (Thread 3)"));

        panelTengah.add(scrollS3);
        panelTengah.add(scrollPrisma);
        panelTengah.add(scrollLimas);

        container.add(panelTengah, BorderLayout.CENTER);

        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jalankanMasterThread();
            }
        });

        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // ===================================================================
    // [BARU] METODE VALIDASI INPUT - menggunakan InvalidInputException
    // Dipisahkan agar mudah diuji (unit test) dan bertanggung jawab satu hal.
    // ===================================================================
    /**
     * Memvalidasi semua input dari GUI sebelum komputasi dimulai. Melempar
     * InvalidInputException jika ada input yang tidak memenuhi syarat.
     *
     * @return int[] berisi {jumlahData} jika valid
     * @throws InvalidInputException jika input tidak valid
     */
    public int[] validasiInputJumlahData() throws InvalidInputException {
        String rawJumlah = txtJumlahData.getText().trim();

        // Cek: apakah field kosong?
        if (rawJumlah.isEmpty()) {
            throw new InvalidInputException("Field 'Jumlah Data' tidak boleh kosong.");
        }

        int jumlahData;
        try {
            jumlahData = Integer.parseInt(rawJumlah);
        } catch (NumberFormatException e) {
            // NumberFormatException dilempar oleh Integer.parseInt jika bukan angka
            throw new InvalidInputException(
                    "Jumlah Data '" + rawJumlah + "' bukan angka bulat yang valid.\n"
                    + "Contoh yang benar: 1000"
            );
        }

        // Cek: apakah nilainya logis?
        if (jumlahData <= 0) {
            throw new InvalidInputException(
                    "Jumlah Data harus lebih dari 0. Nilai yang dimasukkan: " + jumlahData
            );
        }
        if (jumlahData > 150_000) {
            throw new InvalidInputException(
                    "Jumlah Data terlalu besar (maks. 100.000). Nilai yang dimasukkan: " + jumlahData
            );
        }

        return new int[]{jumlahData};
    }

    /**
     * Memvalidasi input alas dan tinggi khusus untuk Opsi 2 (input manual).
     * Melempar InvalidInputException jika nilai tidak valid.
     *
     * @return double[] berisi {alas, tinggi} jika valid
     * @throws InvalidInputException jika input tidak valid
     */
    public double[] validasiInputGeometri() throws InvalidInputException {
        String rawAlas = txtAlas.getText().trim();
        String rawTinggi = txtTinggi.getText().trim();

        if (rawAlas.isEmpty() || rawTinggi.isEmpty()) {
            throw new InvalidInputException("Field 'Alas' dan 'Tinggi' tidak boleh kosong pada Opsi 2.");
        }

        double alas, tinggi;
        try {
            alas = Double.parseDouble(rawAlas);
        } catch (NumberFormatException e) {
            throw new InvalidInputException(
                    "Nilai Alas '" + rawAlas + "' bukan angka desimal yang valid.\n"
                    + "Contoh yang benar: 10.5"
            );
        }

        try {
            tinggi = Double.parseDouble(rawTinggi);
        } catch (NumberFormatException e) {
            throw new InvalidInputException(
                    "Nilai Tinggi '" + rawTinggi + "' bukan angka desimal yang valid.\n"
                    + "Contoh yang benar: 8.0"
            );
        }

        // Cek: nilai geometri tidak boleh nol atau negatif
        if (alas <= 0 && tinggi <= 0) {
            throw new InvalidInputException("Nilai Alas dan Tinggi harus lebih dari 0. Nilai Alas yang dimasukkan: " + alas + " Nilai Tinggi yang dimasukkan: " + tinggi);
        }

        // Cek: nilai terlalu besar bisa menyebabkan overflow kalkulasi
        if (alas > 1_000_000 || tinggi > 1_000_000) {
            throw new InvalidInputException(
                    "Nilai Alas/Tinggi terlalu besar (maks. 1.000.000) untuk menghindari overflow."
            );
        }

        return new double[]{alas, tinggi};
    }

    // ===================================================================
    // METODE UTAMA: jalankanMasterThread (direfaktor dengan exception handling)
    // ===================================================================
    public void jalankanMasterThread() {
        btnGenerate.setEnabled(false);
        modelSegitiga.setRowCount(0);
        modelPrisma.setRowCount(0);
        modelLimas.setRowCount(0);

        Thread masterThread = new Thread(() -> {

            // -----------------------------------------------------------------
            // FASE 1: VALIDASI INPUT (menggunakan exception kustom)
            // Semua validasi dikerjakan di sini sebelum thread worker dibuat.
            // -----------------------------------------------------------------
            final int jumlahData;
            final double alasFinal;
            final double tinggiFinal;

            boolean isManual = (cbMode.getSelectedIndex() == 1);

            try {
                // Validasi jumlah data (selalu dijalankan)
                int[] hasilJumlah = validasiInputJumlahData();
                jumlahData = hasilJumlah[0];

                // Validasi alas & tinggi HANYA jika mode manual dipilih
                if (isManual) {
                    double[] hasilGeometri = validasiInputGeometri();
                    alasFinal = hasilGeometri[0];
                    tinggiFinal = hasilGeometri[1];
                } else {
                    // Mode random: nilai ini tidak dipakai, set ke default
                    alasFinal = 10.0;
                    tinggiFinal = 8.0;
                }

            } catch (InvalidInputException e) {
                // Tangkap exception validasi, tampilkan pesan ERROR spesifik ke user
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(GeometriGUI.this,
                            "Input Tidak Valid:\n" + e.getMessage(),
                            "Error Validasi",
                            JOptionPane.ERROR_MESSAGE
                    );
                    btnGenerate.setEnabled(true); // Re-enable tombol agar user bisa coba lagi
                });
                return; // Hentikan master thread, jangan lanjutkan komputasi
            }

            // -----------------------------------------------------------------
            // FASE 2: PERSIAPAN DATA
            // -----------------------------------------------------------------
            SwingUtilities.invokeLater(() -> {
                pbThread1.setMaximum(jumlahData);
                pbThread2.setMaximum(jumlahData);
                pbThread3.setMaximum(jumlahData);
                pbThread1.setValue(0);
                pbThread2.setValue(0);
                pbThread3.setValue(0);
            });

            String[][] dataSegitiga = new String[jumlahData][5];
            String[][] dataPrisma = new String[jumlahData][4];
            String[][] dataLimas = new String[jumlahData][4];

            Random rand = new Random();

            double[] kumpulanAlasMentah = new double[jumlahData];
            double[] kumpulanTinggiMentah = new double[jumlahData];
            double[] kumpulanTinggiPrismaMentah = new double[jumlahData];

            for (int i = 0; i < jumlahData; i++) {
                if (isManual) {
                    kumpulanAlasMentah[i] = alasFinal;
                    kumpulanTinggiMentah[i] = tinggiFinal;
                } else {
                    kumpulanAlasMentah[i] = 1 + (rand.nextDouble() * 49);
                    kumpulanTinggiMentah[i] = 1 + (rand.nextDouble() * 49);
                }
                kumpulanTinggiPrismaMentah[i] = 1 + (rand.nextDouble() * 49);

                dataSegitiga[i][0] = String.valueOf(i + 1);
                dataPrisma[i][0] = String.valueOf(i + 1);
                dataLimas[i][0] = String.valueOf(i + 1);
            }

            // -----------------------------------------------------------------
            // FASE 3: THREAD WORKER dengan exception handling internal
            // Setiap thread membungkus logikanya dalam try-catch untuk menangkap:
            //   - ArithmeticException   : pembagian nol, NaN, Infinity
            //   - NullPointerException  : objek geometri null (seharusnya tidak terjadi)
            //   - InterruptedException  : thread diinterupsi secara eksplisit
            //   - ThreadComputationException: exception kustom untuk error fatal thread
            // -----------------------------------------------------------------
            // Array untuk menyimpan exception yang terjadi di dalam thread worker.
            // Menggunakan array karena lambda tidak bisa memodifikasi variabel biasa (harus effectively final).
            final Throwable[] errorThread1 = {null};
            final Throwable[] errorThread2 = {null};
            final Throwable[] errorThread3 = {null};

            final int[] trackerSegitiga = {0};

            // --- THREAD 1: Segitiga ---
            
            Thread thread1 = new Thread(() -> {
                Random randDelay = new Random();
                try {
                    for (int i = 0; i < jumlahData; i++) {
                        double alas = kumpulanAlasMentah[i];
                        double tinggi = kumpulanTinggiMentah[i];

                        // [BARU] Guard: pastikan nilai tidak NaN atau Infinity sebelum kalkulasi
                        if (Double.isNaN(alas) || Double.isInfinite(alas)
                                || Double.isNaN(tinggi) || Double.isInfinite(tinggi)) {
                            throw new ArithmeticException(
                                    "Nilai NaN/Infinity terdeteksi pada data ke-" + (i + 1)
                                    + " di Thread Segitiga."
                            );
                        }

                        Segitiga segitiga = new Segitiga(alas, tinggi, isManual);
                        segitiga.run();

                        // [BARU] Guard: pastikan hasil kalkulasi bukan NaN atau Infinity
                        if (Double.isNaN(segitiga.luas) || Double.isInfinite(segitiga.luas)) {
                            throw new ArithmeticException(
                                    "Hasil luas Segitiga tidak valid (NaN/Infinity) pada data ke-" + (i + 1)
                            );
                        }

                        dataSegitiga[i][3] = String.format("%.2f", segitiga.luas);
                        dataSegitiga[i][4] = String.format("%.2f", segitiga.keliling);
                        dataSegitiga[i][1] = String.format("%.2f", alas);
                        dataSegitiga[i][2] = String.format("%.2f", tinggi);

                        final int progress = i + 1;
                        trackerSegitiga[0] = progress;
                        
                        if (i % 50 == 0 || i == jumlahData - 1) {
                            SwingUtilities.invokeLater(() -> pbThread1.setValue(progress));
                        }

                        if (randDelay.nextInt(100) < 3) {
                            try {
                                Thread.sleep(randDelay.nextInt(4) + 1);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                throw ie;
                            }
                        }
                    }
                } catch (Exception e) {
                    errorThread1[0] = new ThreadComputationException("Error di Thread Segitiga: " + e.getMessage(), e);
                }
            });

            // --- THREAD 2: Prisma ---
            Thread thread2 = new Thread(() -> {
                Random randDelay = new Random();
                try {
                    for (int i = 0; i < jumlahData; i++) {
                        double alas = kumpulanAlasMentah[i];
                        double tinggi = kumpulanTinggiMentah[i];
                        double tinggiPrisma = kumpulanTinggiPrismaMentah[i];
                        
                        if (Double.isNaN(alas) || Double.isInfinite(alas) || Double.isNaN(tinggi) || Double.isInfinite(tinggi) || Double.isNaN(tinggiPrisma) || Double.isInfinite(tinggiPrisma)) {
                            throw new ArithmeticException("Nilai NaN/Infinity pada data ke-" + (i + 1));
                        }

                        Segitiga prismaSegitiga = new PrismaSegitiga(alas, tinggi, tinggiPrisma, isManual);
                        
                        prismaSegitiga.run();
//                      
                        PrismaSegitiga pReal = (PrismaSegitiga) prismaSegitiga;
                        
                        dataPrisma[i][1] = String.format("%.2f", pReal.tinggiPrisma);
                        dataPrisma[i][2] = String.format("%.2f", pReal.volumePrisma);
                        dataPrisma[i][3] = String.format("%.2f", pReal.luasPermukaanPrisma);

                        final int progress = i + 1;
                        if (i % 50 == 0 || i == jumlahData - 1) {
                            SwingUtilities.invokeLater(() -> pbThread2.setValue(progress));
                        }

                        if (randDelay.nextInt(100) < 3) {
                            try {
                                Thread.sleep(randDelay.nextInt(4) + 1);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                throw ie;
                            }
                        }
                    }
                } catch (Exception e) {
                    errorThread2[0] = new ThreadComputationException("Error di Thread Prisma: " + e.getMessage(), e);
                }
            });

            // --- THREAD 3: Limas ---
            Thread thread3 = new Thread(() -> {
                Random randDelay = new Random();
                try {
                    for (int i = 0; i < jumlahData; i++) {
                        double alas = kumpulanAlasMentah[i];
                        double tinggi = kumpulanTinggiMentah[i];
                        
                        if (Double.isNaN(alas) || Double.isInfinite(alas) || Double.isNaN(tinggi) || Double.isInfinite(tinggi)) {
                            throw new ArithmeticException("Nilai NaN/Infinity pada data ke-" + (i + 1));
                        }

                        Segitiga limasSegitiga = new LimasSegitiga(alas, tinggi, isManual);
                        
                        limasSegitiga.run();
                        
                        LimasSegitiga lReal = (LimasSegitiga) limasSegitiga;
                        
                        dataLimas[i][1] = String.format("%.2f", lReal.tinggiLimas);
                        dataLimas[i][2] = String.format("%.2f", lReal.volumeLimas);
                        dataLimas[i][3] = String.format("%.2f", lReal.luasPermukaanLimas);

                        final int progress = i + 1;
                        if (i % 50 == 0 || i == jumlahData - 1) {
                            SwingUtilities.invokeLater(() -> pbThread3.setValue(progress));
                        }

                        if (randDelay.nextInt(100) < 3) {
                            try {
                                Thread.sleep(randDelay.nextInt(4) + 1);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                throw ie;
                            }
                        }
                    }
                } catch (Exception e) {
                    errorThread3[0] = new ThreadComputationException("Error di Thread Limas: " + e.getMessage(), e);
                }
            });

            thread1.start();

            int targetSusul = jumlahData * 30 / 100;
            while (trackerSegitiga[0] < targetSusul) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(GeometriGUI.this, "Proses dihentikan paksa saat menunggu Thread Segitiga.", "Interupsi", JOptionPane.WARNING_MESSAGE);
                        btnGenerate.setEnabled(true);
                    });
                    return;
                }
            }

            thread2.start();
            thread3.start();

            try {
                thread1.join();
                thread2.join();
                thread3.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(GeometriGUI.this, "Proses komputasi dihentikan paksa.", "Interupsi", JOptionPane.WARNING_MESSAGE);
                    btnGenerate.setEnabled(true);
                });
                return;
            }

            // --- FASE 5: TAMPILKAN HASIL ---
            SwingUtilities.invokeLater(() -> {
                StringBuilder pesanError = new StringBuilder();
                if (errorThread1[0] != null) pesanError.append("• Thread 1 (Segitiga): ").append(errorThread1[0].getMessage()).append("\n");
                if (errorThread2[0] != null) pesanError.append("• Thread 2 (Prisma): ").append(errorThread2[0].getMessage()).append("\n");
                if (errorThread3[0] != null) pesanError.append("• Thread 3 (Limas): ").append(errorThread3[0].getMessage()).append("\n");

                if (pesanError.length() > 0) {
                    JOptionPane.showMessageDialog(GeometriGUI.this, "Proses selesai dengan ERROR:\n\n" + pesanError.toString() + "\nData yang berhasil dihitung tetap ditampilkan.", "Error Komputasi Thread", JOptionPane.ERROR_MESSAGE);
                }

                for (int i = 0; i < jumlahData; i++) {
                    modelSegitiga.addRow(dataSegitiga[i]);
                    modelPrisma.addRow(dataPrisma[i]);
                    modelLimas.addRow(dataLimas[i]);
                }

                btnGenerate.setEnabled(true);

                if (pesanError.length() == 0) {
                    JOptionPane.showMessageDialog(GeometriGUI.this, "Proses selesai! Data Berhasil Ditampilkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        });

        masterThread.start();
    }

    // ===================================================================
    // MAIN METHOD
    // ===================================================================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Gagal mengambil FlatLaf, menggunakan L&F default: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            new GeometriGUI().setVisible(true);
        });
    }
}
