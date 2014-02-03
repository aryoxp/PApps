package id.ac.ub.ptiik.papps.parsers;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.JadwalMengajar;
import id.ac.ub.ptiik.papps.base.Karyawan;
import id.ac.ub.ptiik.papps.base.Matakuliah;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JadwalMengajarParser {
	public static ArrayList<JadwalMengajar> Parse(String JSONString){
		
		try {
			JSONObject object = new JSONObject(JSONString);
			
			ArrayList<JadwalMengajar> jadwalMengajarList = new ArrayList<JadwalMengajar>();
			
			JSONArray jadwalMengajarArray = object.getJSONArray("jadwal");
			for(int i=0; i<jadwalMengajarArray.length(); i++) {
				
				JSONObject jadwalMengajar = (JSONObject) jadwalMengajarArray.get(i);
				String kode = jadwalMengajar.getString("kode_mk");
				String namamk = jadwalMengajar.getString("namamk");
				int sks = Integer.parseInt(jadwalMengajar.getString("sks"));
				Matakuliah matakuliah = new Matakuliah(kode, namamk, sks);
				String kelas = jadwalMengajar.getString("kelas_id");
				String ruang = jadwalMengajar.getString("ruang");
				String hari = jadwalMengajar.getString("hari");
				String prodi = jadwalMengajar.getString("prodi");
				String jam_mulai = jadwalMengajar.getString("jam_mulai");
				String jam_selesai = jadwalMengajar.getString("jam_selesai");
				JadwalMengajar u = new JadwalMengajar(kelas, hari, ruang, prodi, jam_mulai, jam_selesai);
				String id = jadwalMengajar.getString("karyawan_id");
				String nama = jadwalMengajar.getString("nama");
				String gelar_awal = jadwalMengajar.getString("gelar_awal");
				String gelar_akhir = jadwalMengajar.getString("gelar_akhir");
				Karyawan dosen = new Karyawan(id, nama, gelar_awal, gelar_akhir);
				dosen.nik = jadwalMengajar.getString("nik");
				u.setDosen(dosen);
				u.setMatakuliah(matakuliah);
				jadwalMengajarList.add(u);
			}
			return jadwalMengajarList;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
