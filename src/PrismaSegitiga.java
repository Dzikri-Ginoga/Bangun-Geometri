// Instruksi 2: Thread dijadikan satu di dalam class (implements Runnable)
public class PrismaSegitiga extends Segitiga implements Runnable {
    
    // Instruksi 1: Atribut public
    public double tinggiPrisma, volumePrisma, luasPermukaanPrisma;
    
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
    public double hitungVolumePrisma(){
        // Menggunakan "super.luas" yang sudah ada, TIDAK PERLU panggil method hitungLuas() lagi
        this.volumePrisma = super.luas * this.tinggiPrisma;
        return this.volumePrisma;                
    }

    // Instruksi 4: Overloading Dengan Parameter (menggunakan referensi luas parent)
    public double hitungVolumePrisma(double luasAlasParent, double tinggiPrismaMasuk){
        this.volumePrisma = luasAlasParent * tinggiPrismaMasuk;
        return this.volumePrisma;
    }

    // Instruksi 3 & 4: Luas Permukaan tanpa hitung ulang luas & keliling
    public double hitungLuasPermukaanPrisma() {
        // Ambil super.luas untuk alas/tutup, dan super.keliling untuk selimut
        this.luasPermukaanPrisma = (2 * super.luas) + (super.keliling * this.tinggiPrisma);
        return this.luasPermukaanPrisma;
    }

    // Overloading Luas Permukaan dengan parameter
    public double hitungLuasPermukaanPrisma(double luasAlasParent, double kelilingAlasParent, double tinggiPrismaMasuk) {
        this.luasPermukaanPrisma = (2 * luasAlasParent) + (kelilingAlasParent * tinggiPrismaMasuk);
        return this.luasPermukaanPrisma;
    }

    // Instruksi 2: Wajib memiliki run() karena implements Runnable
    @Override
    public void run() {
        // Menjalankan proses hitung secara sinkron di dalam Thread ini
        this.hitungVolumePrisma();
        this.hitungLuasPermukaanPrisma();
        
        // Print untuk memastikan thread berjalan saat di-start()
        // System.out.println("Thread Prisma selesai mengeksekusi volume dan LP.");
    }
}