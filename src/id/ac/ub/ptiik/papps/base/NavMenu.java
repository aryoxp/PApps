package id.ac.ub.ptiik.papps.base;

public class NavMenu {
	public String title;
	public String description;
	public int imageResourceId = 0;
	public boolean isActive = false;
	
	public NavMenu(){}
	public NavMenu(String title, String description, int imageResourceId){
		this.title = title;
		this.description = description;
		this.imageResourceId = imageResourceId;
	}
	public void activate(){
		this.isActive = true;
	}
	public void deactivate() {
		this.isActive = false;
	}
}
