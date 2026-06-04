public class PrismaSegitiga extends Segitiga implements Runnable {
    
    public double tinggiPrisma, volumePrisma, luasPermukaanPrisma;
    
    public PrismaSegitiga(double alas, double tinggi, double tinggiPrisma){
        super(alas, tinggi);
        this.tinggiPrisma = tinggiPrisma;
    }
    
    public double hitungVolumePrisma(){
        volumePrisma = super.luas * this.tinggiPrisma;
        return volumePrisma;                
    }

    // Instruksi 3 & 4: Luas Permukaan tanpa hitung ulang luas & keliling
    public double hitungLuasPermukaanPrisma() {
        luasPermukaanPrisma = (2 * super.luas) + (super.keliling * this.tinggiPrisma);
        return luasPermukaanPrisma;
    }

//    // Overloading Luas Permukaan dengan parameter
//    public double hitungLuasPermukaanPrisma(double luasAlasParent, double kelilingAlasParent, double tinggiPrismaMasuk) {
//        this.luasPermukaanPrisma = (2 * luasAlasParent) + (kelilingAlasParent * tinggiPrismaMasuk);
//        return this.luasPermukaanPrisma;
//    }

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