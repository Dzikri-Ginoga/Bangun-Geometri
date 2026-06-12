public class Segitiga implements BangunGeometri, Runnable {
    
    // Instruksi 1: Atribut private diganti public semua
    public double alas, tinggi, luas, keliling;
    public boolean isManual;
    
    public Segitiga (double alas, double tinggi, boolean isManual){
        if (alas <= 0 || tinggi <= 0) {
            throw new IllegalArgumentException("Nilai alas dan tinggi segitiga harus lebih besar dari 0!");
        }
        this.alas = alas;
        this.tinggi = tinggi;
        this.isManual = isManual;           
    }
    
    @Override
    public double hitungLuas() throws IllegalStateException {
        if (this.alas <= 0 || this.tinggi <= 0) {
            throw new IllegalStateException("Gagal menghitung luas: Atribut alas atau tinggi tidak valid.");
        }
        
       luas = 0.5 * this.alas * this.tinggi;
       return luas;
    }

    //Overloading (Dengan Parameter)
    @Override
    public double hitungLuas(double alas, double tinggi) throws IllegalArgumentException {
        if (alas <= 0 || tinggi <= 0) {
            throw new IllegalArgumentException("Input Alas dan tinggi tidak boleh 0 atau minus!");
        }
       luas = 0.5 * alas * tinggi;
       return luas;
    }

    @Override
    public double hitungKeliling() throws IllegalStateException {
         if (this.alas <= 0 || this.tinggi <= 0) {
            throw new IllegalStateException("Gagal menghitung keliling: Atribut alas atau tinggi tidak valid.");
        }
        double sisiMiring = Math.sqrt(Math.pow(this.alas / 2.0, 2) + Math.pow(this.tinggi, 2));
        keliling = this.alas + (2 * sisiMiring);
        return keliling;
    }

    //Overloading (Dengan Parameter)
    @Override
    public double hitungKeliling(double alas, double tinggi) throws IllegalArgumentException {
        if (alas <= 0 || tinggi <= 0) {
            throw new IllegalArgumentException("Alas dan tinggi input tidak boleh 0 atau minus!");
        }
        double sisiMiring = Math.sqrt(Math.pow(alas / 2.0, 2) + Math.pow(tinggi, 2));
        keliling = alas + (2 * sisiMiring);
        return keliling;
    }
    
    @Override
    public void run() {
        if(this.isManual){
            this.hitungLuas(alas, tinggi);
            this.hitungKeliling(alas, tinggi);
        } else{
            this.hitungKeliling();
            this.hitungLuas();
        }
    }
}