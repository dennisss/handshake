package me.denniss.handshake;

import java.io.IOException;
import java.util.ArrayList;

import libsvm.*;


public class Learn {
    private static final String MODEL_FILE = "/sdcard/model.txt";

    private svm_problem problem = new svm_problem();
    private svm_parameter param = new svm_parameter();
    private svm_model model;


    public void train(ArrayList<float[]> samples /* Note: the final 7th element should be the classification, but they need to be split as x (6 dim) and y (1 dim) */){

        param.kernel_type = svm_parameter.POLY;
        param.svm_type = svm_parameter.C_SVC;
        //param.kernel_type = svm_parameter.RBF;
        param.degree = 3;
        param.gamma = 0;
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 40;
        param.C = 1;
        param.eps = 1e-3;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 0;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];




        problem.l = samples.size();
        problem.x = new svm_node[problem.l][];
        problem.y = new double[problem.l];

        for(int i = 0; i < problem.l; i++){
            float[] s = samples.get(i);
            problem.x[i] = new svm_node[6];

            for(int j = 0; j < 6; j++){
                problem.x[i][j] = new svm_node();
                problem.x[i][j].value = s[j];
                problem.x[i][j].index = j+1;
            }

            problem.y[i] = s[6];
        }


        model = svm.svm_train(problem, param);

        /*
        prob = new svm_problem();
        prob.l = vy.size();
        prob.x = new svm_node[prob.l][];
        for(int i=0;i<prob.l;i++)
            prob.x[i] = vx.elementAt(i);
        prob.y = new double[prob.l];
        for(int i=0;i<prob.l;i++)
            prob.y[i] = vy.elementAt(i);

        if(param.gamma == 0 && max_index > 0)
            param.gamma = 1.0/max_index;

        if(param.kernel_type == svm_parameter.PRECOMPUTED)
            for(int i=0;i<prob.l;i++)
            {
                if (prob.x[i][0].index != 0)
                {
                    System.err.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
                    System.exit(1);
                }
                if ((int)prob.x[i][0].value <= 0 || (int)prob.x[i][0].value > max_index)
                {
                    System.err.print("Wrong input format: sample_serial_number out of range\n");
                    System.exit(1);
                }
            }

        fp.close();



        model = svm.svm_train(prob,param);
        svm.svm_save_model(model_file_name,model);


        */

    }

    public void load_model(){
        try {
            model = svm.svm_load_model(MODEL_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save_model(){
        try {
            svm.svm_save_model(MODEL_FILE, model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public float predict(float[] sample){

        svm_node[] x = new svm_node[6];

        for(int i = 0; i < 6; i++){
            x[i].value = sample[i];

        }

        return (float)svm.svm_predict(model, x);
    }




}
