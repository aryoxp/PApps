package id.ac.ub.ptiik.papps.base;

public class Matakuliah {
	private String kode;
	private String nama;
	private int sks;
	
	public Matakuliah(String kode, String nama, int sks) {
		this.kode = kode;
		this.nama = nama;
		this.sks = sks;
	}
	
	public String getKode() { return this.kode; }
	public String getNama() { return this.nama; }
	public int getSks() { return this.sks; }
	
	public String getNamaSks() { return this.nama + " - " + this.kode +" (" + String.valueOf(this.sks) + " sks)"; }
}
