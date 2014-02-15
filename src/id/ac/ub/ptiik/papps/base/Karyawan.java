package id.ac.ub.ptiik.papps.base;

public class Karyawan {

	public String id;
	public String nama;
	public String gelar_awal;
	public String gelar_akhir;
	public String nik;
	
	public Karyawan(String id, String nama, String gelar_awal, String gelar_akhir) {
		this.id = id;
		this.nama = nama;
		this.gelar_awal = gelar_awal;
		this.gelar_akhir = gelar_akhir;
	}
	
	public String getNamaGelar() {
		String nama = this.nama;
		if(gelar_awal != null && !gelar_awal.equals("null") && !gelar_awal.trim().equals("")) 
			nama = gelar_awal + " " + nama;
		if(gelar_akhir != null && !gelar_akhir.equals("null") && !gelar_akhir.trim().equals("")) 
			nama = nama + ", " + gelar_akhir;
		return nama;
	}

}
