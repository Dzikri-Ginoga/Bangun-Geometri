public class Segitiga extends BangunGeometri {
    
    // Instruksi 1: Atribut private diganti public semua
    public double alas, tinggi, luas, keliling;

    public Segitiga() {
        this.alas = 0.0;
        this.tinggi = 0.0;
    }
    
    public Segitiga(double alas, double tinggi){
        this.alas = alas;
        this.tinggi = tinggi;
        
        // Memastikan nilai awal luas dan keliling sudah terisi
        this.hitungLuas(); 
        this.hitungKeliling();
    }

    // Instruksi 4: Overloading (Tanpa Parameter)
    @Override
    public double hitungLuas() {
       luas = 0.5 * this.alas * this.tinggi;
       return this.luas;
    }

    // Instruksi 4: Overloading (Dengan Parameter)
    public double hitungLuas(double alasMasuk, double tinggiMasuk) {
       this.luas = 0.5 * alasMasuk * tinggiMasuk;
       return this.luas;
    }

    // Instruksi 4: Overloading (Tanpa Parameter)
    @Override
    public double hitungKeliling() {
        double sisiMiring = Math.abs(Math.pow(this.alas / 2.0, 2) + Math.pow(this.tinggi, 2));
        this.keliling = this.alas + (2 * sisiMiring);
        return this.keliling;
    }

    // Instruksi 4: Overloading (Dengan Parameter)
    public double hitungKeliling(double alasMasuk, double tinggiMasuk) {
        double sisiMiring = Math.abs(Math.pow(alasMasuk / 2.0, 2) + Math.pow(tinggiMasuk, 2));
        this.keliling = alasMasuk + (2 * sisiMiring);
        return this.keliling;
    }
}