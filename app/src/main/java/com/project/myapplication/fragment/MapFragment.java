package com.project.myapplication.fragment;

import android.app.Fragment;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.myapplication.R;


/**
 * Created by 최형식 on 2015-02-26.
 */
public class MapFragment extends Fragment {
	private Matrix matrix;
	private Matrix savedMatrix;
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode;
	private PointF start;
	private PointF mid;
	private float oldDist;
	private static final String LOG_TAG = "Touch Event";
	private ImageView mapImage;
	private ImageView mapText;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		return inflater.inflate(R.layout.fragment_map, container, false);
	}

	public void onStart(){
		super.onStart();
		matrix = new Matrix();
		savedMatrix = new Matrix();
		mode = NONE;
		start = new PointF();
		mid = new PointF();
		oldDist = 1f;

		mapImage = (ImageView) getActivity().findViewById(R.id.mapImage);
		mapText = (ImageView) getActivity().findViewById(R.id.mapText);
		mapImage.setImageDrawable(getResources().getDrawable(R.drawable.brick));
		/*SVG svg = SVGParser.getSVGFromResource(getResources(), R.drawable.ic_launcher);
		imageView.setImageDrawable(svg.createPictureDrawable());
		imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);*/
		mapImage.setOnTouchListener(onTouch);
	}
	private View.OnTouchListener onTouch = new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				switch(event.getAction() & MotionEvent.ACTION_MASK){
					case MotionEvent.ACTION_DOWN:
						Log.i(LOG_TAG, "Move Down");
						savedMatrix.set(matrix);
						start.set(event.getX(), event.getY());
						mode = DRAG;
						break;
					case MotionEvent.ACTION_POINTER_DOWN:
						Log.i(LOG_TAG, "Move Pointer Down");
						oldDist = spacing(event);
						if(oldDist > 10f){
							savedMatrix.set(matrix);
							midPoint(mid, event);
							mode = ZOOM;
						}
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_POINTER_UP:
						mode = NONE;
						break;
					case MotionEvent.ACTION_MOVE:
						Log.i(LOG_TAG, "Move");
						if(mode == DRAG){
							matrix.set(savedMatrix);
							matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
						} else if(mode == ZOOM){
							float newDist = spacing(event);
							if(newDist > 10f){
								matrix.set(savedMatrix);
								float scale = newDist / oldDist;
								matrix.postScale(scale, scale, mid.x, mid.y);
							}
						}
						break;
				}
				mapImage.setImageMatrix(matrix);
				mapText.setImageMatrix(matrix);
				return true;
			}
			private float spacing(MotionEvent event){
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				return FloatMath.sqrt(x * x + y * y);
			}
			private void midPoint(PointF point, MotionEvent event){
				float x = event.getX(0) + event.getX(1);
				float y = event.getY(0) + event.getY(1);
				point.set(x/2, y/2);
			}
		};
}

