package me.denniss.handshake;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tomer on 2/22/2015.
 */
public class LearnNewLessonFragment extends Fragment{


    TextView currentGesture;
    int curGestureIndex = 0;
    private Button startTraining,arrowLeft,arrowRight, selectApp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.training_fragment, container, false);



        startTraining = (Button)parentView.findViewById(R.id.wearTrainingButton);
        arrowLeft = (Button)parentView.findViewById(R.id.gestureLeftButton);
        arrowRight = (Button)parentView.findViewById(R.id.gestureRightButton);
        currentGesture = (TextView)parentView.findViewById(R.id.gestureSelected);
        selectApp = (Button)parentView.findViewById(R.id.selectAppButton);

        selectApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SelectAppActivity.class);
                getActivity().startActivity(i);
            }
        });


        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curGestureIndex++;
                currentGesture.setText(Integer.toString(curGestureIndex));
            }
        });

        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curGestureIndex--;
                currentGesture.setText(Integer.toString(curGestureIndex));
            }
        });

        startTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startedTraining();
                    }
                }, 2000);
            }
        });



        return parentView;
    }




    private void startedTraining()
    {
        Toast.makeText(getActivity(), "STARTED TRAINING!", Toast.LENGTH_SHORT).show();

        WearActivity.g.trainingState(curGestureIndex);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                stopTraining();
            }
        }, 10000);
    }

    private void stopTraining()
    {
        WearActivity.g.noState();

        // Save data

        Toast.makeText(getActivity(),"STOPPED TRAINING!",Toast.LENGTH_SHORT).show();
    }

}
