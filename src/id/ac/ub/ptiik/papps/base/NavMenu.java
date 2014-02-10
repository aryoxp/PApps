package id.ac.ub.ptiik.papps.base;

public class NavMenu {
	
	public static final int MENU_HOME = 0;
	public static final int MENU_MESSAGES = 1;
	public static final int MENU_SCHEDULE = 2;
	public static final int MENU_AGENDA = 3;
	public static final int MENU_NEWS = 4;
	
	public String title;
	public String description;
	public int imageResourceId = 0;
	public boolean isActive = false;
	public int id;
	public String tag;
	
	public NavMenu(){}
	public NavMenu(int id, String title, String description, int imageResourceId, String tag){
		this.id = id;
		this.title = title;
		this.description = description;
		this.imageResourceId = imageResourceId;
		this.tag = tag;
	}
	public void activate(){
		this.isActive = true;
	}
	public void deactivate() {
		this.isActive = false;
	}
}
