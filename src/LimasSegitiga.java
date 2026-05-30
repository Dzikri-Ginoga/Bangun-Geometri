// Instruksi 2: Thread dijadikan satu di dalam class (implements Runnable)
public class LimasSegitiga extends Segitiga implements Runnable {
    
    // Instruksi 1: Atribut public semua
    public double tinggiLimas, volumeLimas, luasPermukaanLimas;
    
    public LimasSegitiga() {
        super();
        this.tinggiLimas = 0.0;
    }
    
    public LimasSegitiga(double alas, double tinggi) {
        // Super akan memanggil constructor Segitiga yang otomatis menghitung luas & keliling
        super(alas, tinggi);
        
        // Tinggi limas tidak diinput dari luar, melainkan dihitung internal 
        this.hitungTinggiLimasInternal();
    }
    
    // Proses internal kalkulasi tinggi limas menggunakan atribut public milik parent
    public void hitungTinggiLimasInternal() {
        this.tinggiLimas = Math.sqrt(Math.pow(super.alas / 2.0, 2) + Math.pow(super.tinggi, 2)) * 0.8;
    }

    // Instruksi 3 & 4: Overloading Tanpa Parameter & Tidak perlu hitung ulang
    public double hitungVolumeLimas() {
        // Menggunakan "super.luas", TIDAK PERLU panggil method hitungLuas() lagi
        this.volumeLimas = (1.0 / 3.0) * super.luas * this.tinggiLimas;
        return this.volumeLimas;
    }

    // Instruksi 4: Overloading Dengan Parameter
    public double hitungVolumeLimas(double luasAlasParent, double tinggiLimasMasuk) {
        this.volumeLimas = (1.0 / 3.0) * luasAlasParent * tinggiLimasMasuk;
        return this.volumeLimas;
    }

    // Instruksi 3 & 4: Luas Permukaan tanpa hitung ulang luas alas
    public double hitungLuasPermukaanLimas() {
        // Cari tinggi sisi tegak menggunakan super.alas
        double tinggiSisiTegak = Math.sqrt(Math.pow(super.alas / 2.0, 2) + Math.pow(this.tinggiLimas, 2));
        
        // Luas selimut (3 buah segitiga tegak)
        double luasSelimut = 3 * (0.5 * super.alas * tinggiSisiTegak);
        
        // Tambahkan luas alas dari parent (super.luas) dengan luas selimut
        this.luasPermukaanLimas = super.luas + luasSelimut;
        return this.luasPermukaanLimas;
    }

    // Overloading Luas Permukaan dengan parameter (menggunakan referensi data parent)
    public double hitungLuasPermukaanLimas(double luasAlasParent, double alasParent, double tinggiLimasMasuk) {
        double tinggiSisiTegak = Math.sqrt(Math.pow(alasParent / 2.0, 2) + Math.pow(tinggiLimasMasuk, 2));
        double luasSelimut = 3 * (0.5 * alasParent * tinggiSisiTegak);
        
        this.luasPermukaanLimas = luasAlasParent + luasSelimut;
        return this.luasPermukaanLimas;
    }

    // Instruksi 2: Wajib memiliki run() karena implements Runnable
    @Override
    public void run() {
        // Begitu Thread dijalankan ( start() ), class otomatis menghitung Volume dan Luas Permukaan
        this.hitungVolumeLimas();
        this.hitungLuasPermukaanLimas();
    }
}