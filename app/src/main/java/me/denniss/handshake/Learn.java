package me.denniss.handshake;

import libsvm.*;


public class Learn {
    private svm_problem problem = new svm_problem();
    private svm_parameter param = new svm_parameter();
    private svm_model model;

    public void train(float[][] samples){

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


    public float predict(float[] sample){


        svm_node[] x = new svm_node[6];


        //svn

        //double res = svm.svm_predict(model, );


        return 0;
    }




}
