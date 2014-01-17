package id.ac.ub.ptiik.papps.animations;

import id.ac.ub.ptiik.papps.R;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

public class NavigationAnimation implements AnimationListener {
	
	private Context context;
	private View navView;
	private Animation slideIn;
	private Animation slideOut;
	private Boolean isVisible;
	
	public NavigationAnimation(Context context, View v) {
		this.context = context;
		this.navView = v;
		this.slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out);
		this.slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in);
		this.slideIn.setAnimationListener(this);
		this.slideOut.setAnimationListener(this);
		this.navView.setVisibility(View.INVISIBLE);
		this.hide();
	}
	
	public void toggle() {
		if(
			!(this.slideIn.hasStarted() && !this.slideIn.hasEnded())
			&&
			!(this.slideOut.hasStarted() && !this.slideOut.hasEnded())
		) 
		{
			if(this.isVisible) this.navView.startAnimation(slideOut);
			else this.navView.startAnimation(slideIn);
		}
	}
	
	private void hide() {
		this.navView.startAnimation(
				AnimationUtils.loadAnimation(
						this.context, R.anim.hide));
		this.isVisible = false;
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		if(this.isVisible) {
			this.navView.setVisibility(View.GONE);
			this.isVisible = false;
		} else {
			this.isVisible = true;
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {}

	@Override
	public void onAnimationStart(Animation animation) {
		if(!this.isVisible) {
			this.navView.setVisibility(View.VISIBLE);
		}
	}
}
