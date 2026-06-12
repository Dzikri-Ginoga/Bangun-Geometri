// Instruksi 2: Thread dijadikan satu di dalam class (implements Runnable)
public class LimasSegitiga extends Segitiga implements Runnable {
    
    // Instruksi 1: Atribut public semua
    public double tinggiLimas, volumeLimas, luasPermukaanLimas, luasSelimut;
    
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

    public double hitungVolumeLimas() throws IllegalStateException {
        
        if (super.luas <= 0) {
            throw new IllegalStateException("Gagal menghitung volume: Luas alas dari class Segitiga belum dihitung atau tidak valid.");
        }
        
        volumeLimas = (1.0 / 3.0) * super.luas * this.tinggiLimas;
        return volumeLimas;
    }
    
    public double hitungVolumeLimas(double alas, double tinggi, double tinggiLimas) throws IllegalStateException{
        
        if (super.hitungLuas(alas, tinggi) <= 0) {
            throw new IllegalStateException("Gagal menghitung volume: Luas alas dari class Segitiga belum dihitung atau tidak valid.");
        }
        
        volumeLimas = (1.0 / 3.0) * super.hitungLuas(alas, tinggi) * tinggiLimas;
        return volumeLimas;
    }

    // Instruksi 3 & 4: Luas Permukaan tanpa hitung ulang luas alas
    public double hitungLuasPermukaanLimas() throws IllegalStateException {
        
        if (super.luas <= 0) {
            throw new IllegalStateException("Gagal menghitung luas permukaan: Luas alas dari class Segitiga belum dihitung atau tidak valid.");
        }
        
        double tinggiSisiTegak = Math.sqrt(Math.pow(super.alas / 2.0, 2) + Math.pow(this.tinggiLimas, 2));
        this.luasSelimut = 0.5 * super.keliling * tinggiSisiTegak;
        
        luasPermukaanLimas = super.luas + luasSelimut;
        return luasPermukaanLimas;
    }

     public double hitungLuasPermukaanLimas(double alas, double tinggi, double tinggiLimas) throws IllegalStateException {
        
        if (super.hitungLuas(alas, tinggi) <= 0) {
            throw new IllegalStateException("Gagal menghitung luas permukaan: Luas alas dari class Segitiga belum dihitung atau tidak valid.");
        }
        
        double tinggiSisiTegak = Math.sqrt(Math.pow(alas / 2.0, 2) + Math.pow(tinggiLimas, 2));
        
        this.luasSelimut = 0.5 * super.hitungKeliling(alas, tinggi) * tinggiSisiTegak;
        
        this.luasPermukaanLimas = super.hitungLuas(alas, tinggi)  + luasSelimut;
        return luasPermukaanLimas;
    }
     
    // Instruksi 2: Wajib memiliki run() karena implements Runnable
    @Override
    public void run() {
        
        super.run();
       if(this.isManual){
            this.hitungLuasPermukaanLimas(alas, tinggi, tinggiLimas);
            this.hitungVolumeLimas(alas, tinggi, tinggiLimas);
        } else{
            this.hitungLuasPermukaanLimas();
            this.hitungVolumeLimas();
        }
    }
}