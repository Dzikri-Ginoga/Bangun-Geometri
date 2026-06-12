public class PrismaSegitiga extends Segitiga implements Runnable {
    
    public double tinggiPrisma, volumePrisma, luasPermukaanPrisma;
    
    public PrismaSegitiga(double alas, double tinggi,  double tinggiPrisma, boolean isManual){
        super(alas, tinggi, isManual);
        
        if (alas <= 0 || tinggi <= 0 || tinggiPrisma <= 0) {
            throw new IllegalArgumentException("Nilai alas, tinggi segitiga, dan tinggi prisma harus lebih besar dari 0!");
        }
        
        this.tinggiPrisma = tinggiPrisma;
    }
    
    public double hitungVolumePrisma() throws IllegalStateException {
        
        if (super.luas <= 0) {
            throw new IllegalStateException("Gagal menghitung volume: Luas alas dari class Segitiga belum dihitung atau bernilai 0.");
        }
        
        volumePrisma = super.luas * this.tinggiPrisma;
        return volumePrisma;                
    }
    
    public double hitungVolumePrisma(double alas, double tinggi) throws IllegalStateException {
        
        double luasAlas = super.hitungLuas(alas, tinggi);
        
        if (luasAlas <= 0) {
            throw new IllegalStateException("Gagal menghitung volume: Luas alas dari class Segitiga belum dihitung atau bernilai 0.");
        }
        
        volumePrisma = luasAlas * this.tinggiPrisma;
        return volumePrisma;                
    }

    // Instruksi 3 & 4: Luas Permukaan tanpa hitung ulang luas & keliling
    public double hitungLuasPermukaanPrisma() throws IllegalStateException{
        
         if (super.luas <= 0 || super.keliling <= 0) {
            throw new IllegalStateException("Gagal menghitung luas permukaan: Luas atau keliling dari class Segitiga belum dihitung.");
        }
        
        luasPermukaanPrisma = (2 * super.luas) + (super.keliling * this.tinggiPrisma);
        return luasPermukaanPrisma;
    }

    // Overloading Luas Permukaan dengan parameter
    public double hitungLuasPermukaanPrisma(double alas, double tinggi) throws IllegalStateException  {
        double luasAlas = super.hitungLuas(alas, tinggi);
        double kelilingAlas = super.hitungKeliling(alas, tinggi);
        
       if(luasAlas <= 0 || kelilingAlas <= 0){
            throw new IllegalStateException("Gagal menghitung luas permukaan: Luas atau keliling dari class Segitiga belum dihitung.");
       }
       
       luasPermukaanPrisma = (2 * luasAlas) + (kelilingAlas * this.tinggiPrisma);
       return luasPermukaanPrisma;
    }

    // Instruksi 2: Wajib memiliki run() karena implements Runnable
    @Override
    public void run() {
       super.run();
       if(this.isManual){
           this.hitungLuasPermukaanPrisma(alas, tinggi);
           this.hitungVolumePrisma(alas, tinggi);
        } else{
            this.hitungLuasPermukaanPrisma();
            this.hitungVolumePrisma();
        }   
    }
}