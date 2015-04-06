package com.project.myapplication.fragment;

import android.app.Fragment;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.project.myapplication.R;
import com.qozix.tileview.TileView;


/**
 * Created by 최형식 on 2015-02-26.
 */
public class MapFragment extends Fragment {
	private FrameLayout container;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		return inflater.inflate(R.layout.fragment_map, container, false);
	}

	public void onStart(){
		super.onStart();
		container = (FrameLayout) getActivity().findViewById(R.id.frame);
		setTileView();
	}
	private void setTileView(){
		TileView tileView = new TileView(getActivity());
		tileView.setSize( 3090, 2536 );
		tileView.addDetailLevel( 0.25f, "tiles/boston/boston-125-%col%_%row%.jpg", "samples/boston-overview.jpg" );
		tileView.addDetailLevel( 1.0f, "tiles/boston/boston-500-%col%_%row%.jpg", "samples/boston-pedestrian.jpg" );
		tileView.addDetailLevel( 0.5f, "tiles/boston/boston-250-%col%_%row%.jpg", "samples/boston-overview.jpg" );

		// some preferences...
		tileView.setMarkerAnchorPoints( -0.5f, -1.0f );
		tileView.setScaleLimits( 0, 2 );
		tileView.setTransitionsEnabled( false );

		// provide the corner coordinates for relative positioning 시작화면위치?
		tileView.defineRelativeBounds( 42.379676, -71.094919, 42.346550, -71.040280 );

		// get the default paint and style it.  the same effect could be achieved by passing a custom Paint instnace
		Paint paint = tileView.getPathPaint();
		paint.setShadowLayer( 4, 2, 2, 0x66000000 );
		paint.setPathEffect( new CornerPathEffect( 5 ) );

		container.addView(tileView);
	}
}

