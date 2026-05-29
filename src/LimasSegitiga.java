// Instruksi 2: Thread dijadikan satu di dalam class (implements Runnable)
public class LimasSegitiga extends Segitiga implements Runnable {
    
    // Instruksi 1: Atribut public semua
    public double tinggiLimas, volume, luasPermukaan;
    
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
    private void hitungTinggiLimasInternal() {
        this.tinggiLimas = Math.sqrt(Math.pow(super.alas / 2.0, 2) + Math.pow(super.tinggi, 2)) * 0.8;
    }

    // Instruksi 3 & 4: Overloading Tanpa Parameter & Tidak perlu hitung ulang
    public double hitungVolume() {
        // Menggunakan "super.luas", TIDAK PERLU panggil method hitungLuas() lagi
        this.volume = (1.0 / 3.0) * super.luas * this.tinggiLimas;
        return this.volume;
    }

    // Instruksi 4: Overloading Dengan Parameter
    public double hitungVolume(double luasAlasParent, double tinggiLimasMasuk) {
        this.volume = (1.0 / 3.0) * luasAlasParent * tinggiLimasMasuk;
        return this.volume;
    }

    // Instruksi 3 & 4: Luas Permukaan tanpa hitung ulang luas alas
    public double hitungLuasPermukaan() {
        // Cari tinggi sisi tegak menggunakan super.alas
        double tinggiSisiTegak = Math.sqrt(Math.pow(super.alas / 2.0, 2) + Math.pow(this.tinggiLimas, 2));
        
        // Luas selimut (3 buah segitiga tegak)
        double luasSelimut = 3 * (0.5 * super.alas * tinggiSisiTegak);
        
        // Tambahkan luas alas dari parent (super.luas) dengan luas selimut
        this.luasPermukaan = super.luas + luasSelimut;
        return this.luasPermukaan;
    }

    // Overloading Luas Permukaan dengan parameter (menggunakan referensi data parent)
    public double hitungLuasPermukaan(double luasAlasParent, double alasParent, double tinggiLimasMasuk) {
        double tinggiSisiTegak = Math.sqrt(Math.pow(alasParent / 2.0, 2) + Math.pow(tinggiLimasMasuk, 2));
        double luasSelimut = 3 * (0.5 * alasParent * tinggiSisiTegak);
        
        this.luasPermukaan = luasAlasParent + luasSelimut;
        return this.luasPermukaan;
    }

    // Instruksi 2: Wajib memiliki run() karena implements Runnable
    @Override
    public void run() {
        // Begitu Thread dijalankan ( start() ), class otomatis menghitung Volume dan Luas Permukaan
        this.hitungVolume();
        this.hitungLuasPermukaan();
    }
}