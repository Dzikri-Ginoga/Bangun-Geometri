public class Segitiga implements BangunGeometri, Runnable {
    
    // Instruksi 1: Atribut private diganti public semua
    public double alas, tinggi, luas, keliling;
    
    public Segitiga (double alas, double tinggi){
        
        this.alas = alas;
        this.tinggi = tinggi;
        
        this.hitungLuas();
        this.hitungKeliling();
                
    }
    
    @Override
    public double hitungLuas() {
       this.luas = 0.5 * this.alas * this.tinggi;
       return this.luas;
    }

    //Overloading (Dengan Parameter)
    @Override
    public double hitungLuas(double alas, double tinggi) {
       luas = 0.5 * alas * tinggi;
       return luas;
    }

    @Override
    public double hitungKeliling() {
        double sisiMiring = Math.sqrt(Math.pow(this.alas / 2.0, 2) + Math.pow(this.tinggi, 2));
        keliling = this.alas + (2 * sisiMiring);
        return keliling;
    }

    //Overloading (Dengan Parameter)
    @Override
    public double hitungKeliling(double alas, double tinggi) {
        double sisiMiring = Math.sqrt(Math.pow(alas / 2.0, 2) + Math.pow(tinggi, 2));
        keliling = alas + (2 * sisiMiring);
        return keliling;
    }
    
    @Override
    public void run() {
        // Menjalankan proses hitung secara sinkron di dalam Thread ini
        this.hitungLuas();
        this.hitungLuas(alas, tinggi);
        this.hitungKeliling();
        this.hitungKeliling(alas, tinggi);
        
        // Print untuk memastikan thread berjalan saat di-start()
    }
}