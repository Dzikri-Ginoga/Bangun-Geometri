// Instruksi 2: Thread dijadikan satu di dalam class (implements Runnable)
public class PrismaSegitiga extends Segitiga implements Runnable {
    
    // Instruksi 1: Atribut public
    public double tinggiPrisma, volume, luasPermukaan;
    
    public PrismaSegitiga(){
        super();
        this.tinggiPrisma = 0.0;
    }
    
    public PrismaSegitiga(double alas, double tinggi, double tinggiPrisma){
        // Super akan otomatis memanggil constructor Segitiga yang menghitung luas & keliling
        super(alas, tinggi); 
        this.tinggiPrisma = tinggiPrisma;
    }

    // Instruksi 3 & 4: Overloading Tanpa Parameter & Tidak perlu hitung ulang
    public double hitungVolume(){
        // Menggunakan "super.luas" yang sudah ada, TIDAK PERLU panggil method hitungLuas() lagi
        this.volume = super.luas * this.tinggiPrisma;
        return this.volume;                
    }

    // Instruksi 4: Overloading Dengan Parameter (menggunakan referensi luas parent)
    public double hitungVolume(double luasAlasParent, double tinggiPrismaMasuk){
        this.volume = luasAlasParent * tinggiPrismaMasuk;
        return this.volume;
    }

    // Instruksi 3 & 4: Luas Permukaan tanpa hitung ulang luas & keliling
    public double hitungLuasPermukaan() {
        // Ambil super.luas untuk alas/tutup, dan super.keliling untuk selimut
        this.luasPermukaan = (2 * super.luas) + (super.keliling * this.tinggiPrisma);
        return this.luasPermukaan;
    }

    // Overloading Luas Permukaan dengan parameter
    public double hitungLuasPermukaan(double luasAlasParent, double kelilingAlasParent, double tinggiPrismaMasuk) {
        this.luasPermukaan = (2 * luasAlasParent) + (kelilingAlasParent * tinggiPrismaMasuk);
        return this.luasPermukaan;
    }

    // Instruksi 2: Wajib memiliki run() karena implements Runnable
    @Override
    public void run() {
        // Menjalankan proses hitung secara sinkron di dalam Thread ini
        this.hitungVolume();
        this.hitungLuasPermukaan();
        
        // Print untuk memastikan thread berjalan saat di-start()
        // System.out.println("Thread Prisma selesai mengeksekusi volume dan LP.");
    }
}