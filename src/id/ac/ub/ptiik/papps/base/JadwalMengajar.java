package id.ac.ub.ptiik.papps.base;

public class JadwalMengajar {

	public Matakuliah matakuliah;
	public Karyawan dosen;
	public String kelas;
	public String hari;
	public String ruang;
	public String prodi;
	public String jam_mulai;
	public String jam_selesai;
	
	public static final int DOSEN = 1;
	public static final int DAY = 2;
	public static final int SUBJECT = 3;
	
	public JadwalMengajar(String kelas, String hari, String ruang, String prodi, 
			String jam_mulai, String jam_selesai) {
		this.kelas = kelas;
		this.hari = hari;
		this.ruang = ruang;
		this.prodi = prodi;
		this.jam_mulai = jam_mulai;
		this.jam_selesai = jam_selesai;
	}
	
	public void setMatakuliah(Matakuliah matakuliah) {
		this.matakuliah = matakuliah;
	}
	
	public void setDosen(Karyawan dosen) {
		this.dosen = dosen;
	}

}
