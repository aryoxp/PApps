package id.ac.ub.ptiik.papps.base;

public class User {
	public String username;
	public String password;
	public String nama;
	public String gelar_awal;
	public String gelar_akhir;
	public String karyawan_id;
	public String role;
	public int level;
	public int status;
	
	public String getNamaGelar() {
		String nama = this.nama;
		if(gelar_awal != null && !gelar_awal.equals("null") && !gelar_awal.trim().equals("")) 
			nama = gelar_awal + " " + nama;
		if(gelar_akhir != null && !gelar_akhir.equals("null") && !gelar_akhir.trim().equals("")) 
			nama = nama + ", " + gelar_akhir;
		return nama;
	}
}
