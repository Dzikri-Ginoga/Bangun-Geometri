// Instruksi 2: Thread dijadikan satu di dalam class (implements Runnable)
public class LimasSegitiga extends Segitiga implements Runnable {
    
    // Instruksi 1: Atribut public semua
    public double tinggiLimas, volumeLimas, luasPermukaanLimas;
    
    public LimasSegitiga(double alas, double tinggi, boolean isManual) {
        
        super(alas, tinggi, isManual);
        
        if (alas <= 0 || tinggi <= 0) {
            throw new IllegalArgumentException("Nilai alas dan tinggi untuk Limas harus lebih besar dari 0!");
        }
        
        // Tinggi limas tidak diinput dari luar, melainkan dihitung internal 
        this.hitungTinggiLimasInternal();
    }
    
    public void hitungTinggiLimasInternal() {
        this.tinggiLimas = Math.sqrt(Math.pow(super.alas / 2.0, 2) + Math.pow(super.tinggi, 2)) * 0.8;
    }

    public double hitungVolumeLimas() {
        
        if (super.luas <= 0) {
            throw new IllegalStateException("Gagal menghitung volume: Luas alas dari class Segitiga belum dihitung atau tidak valid.");
        }
        
        volumeLimas = (1.0 / 3.0) * super.luas * this.tinggiLimas;
        return volumeLimas;
    }

    // Instruksi 4: Overloading Dengan Parameter
//    public double hitungVolumeLimas(double luasAlasParent, double tinggiLimasMasuk) {
//        volumeLimas = (1.0 / 3.0) * luasAlasParent * tinggiLimasMasuk;
//        return volumeLimas;
//    }

    // Instruksi 3 & 4: Luas Permukaan tanpa hitung ulang luas alas
    public double hitungLuasPermukaanLimas() {
        
        if (super.luas <= 0) {
            throw new IllegalStateException("Gagal menghitung luas permukaan: Luas alas dari class Segitiga belum dihitung atau tidak valid.");
        }
        
        double tinggiSisiTegak = Math.sqrt(Math.pow(super.alas / 2.0, 2) + Math.pow(this.tinggiLimas, 2));
        double luasSelimut = 0.5 * super.keliling * tinggiSisiTegak;
        
        // Tambahkan luas alas dari parent (super.luas) dengan luas selimut
        luasPermukaanLimas = super.luas + luasSelimut;
        return luasPermukaanLimas;
    }

    // Overloading Luas Permukaan dengan parameter (menggunakan referensi data parent)
//    public double hitungLuasPermukaanLimas(double luasAlasParent, double alasParent, double tinggiLimasMasuk) {
//        double tinggiSisiTegak = Math.sqrt(Math.pow(alasParent / 2.0, 2) + Math.pow(tinggiLimasMasuk, 2));
//        double luasSelimut = 3 * (0.5 * alasParent * tinggiSisiTegak);
//        
//        luasPermukaanLimas = luasAlasParent + luasSelimut;
//        return luasPermukaanLimas;
//    }

    // Instruksi 2: Wajib memiliki run() karena implements Runnable
    @Override
    public void run() {
        
        super.run();
        // Begitu Thread dijalankan ( start() ), class otomatis menghitung Volume dan Luas Permukaan
        this.hitungVolumeLimas();
        this.hitungLuasPermukaanLimas();
    }
}