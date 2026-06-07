public class PrismaSegitiga extends Segitiga implements Runnable {
    
    public double tinggiPrisma, volumePrisma, luasPermukaanPrisma;
    
    public PrismaSegitiga(double alas, double tinggi,  double tinggiPrisma, boolean isManual){
        super(alas, tinggi, isManual);
        
        if (alas <= 0 || tinggi <= 0 || tinggiPrisma <= 0) {
            throw new IllegalArgumentException("Nilai alas, tinggi segitiga, dan tinggi prisma harus lebih besar dari 0!");
        }
        
        this.tinggiPrisma = tinggiPrisma;
    }
    
    public double hitungVolumePrisma(){
        
        if (super.luas <= 0) {
            throw new IllegalStateException("Gagal menghitung volume: Luas alas dari class Segitiga belum dihitung atau bernilai 0.");
        }
        
        volumePrisma = super.luas * this.tinggiPrisma;
        return volumePrisma;                
    }

    // Instruksi 3 & 4: Luas Permukaan tanpa hitung ulang luas & keliling
    public double hitungLuasPermukaanPrisma() {
        
         if (super.luas <= 0 || super.keliling <= 0) {
            throw new IllegalStateException("Gagal menghitung luas permukaan: Luas atau keliling dari class Segitiga belum dihitung.");
        }
        
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
        super.run();
        // Menjalankan proses hitung secara sinkron di dalam Thread ini
        this.hitungVolumePrisma();
        this.hitungLuasPermukaanPrisma();
        
        // Print untuk memastikan thread berjalan saat di-start()
        // System.out.println("Thread Prisma selesai mengeksekusi volume dan LP.");
    }
}