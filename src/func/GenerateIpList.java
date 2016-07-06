package func;


import app.Application;
import interfacees.CallBack;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hrant on 3/5/16.
 */
public class GenerateIpList extends Thread implements Runnable {

    private String mStIp = new String();
    private String mEndIp = new String();
    private int mPort;

    private List<String> mIpList = new ArrayList<String>();
    private ProgressMonitor mProgress;

    public GenerateIpList(String stIp, String endIp, int port) {
        mStIp = stIp;
        mEndIp = endIp;
        mPort = port;
    }

    public void run() {
        System.out.println("GenerateIpList");

        // ip parts
        int st0 = 0;
        int st1 = 0;
        int st2 = 0;
        int st3 = 0;

        int en0 = 0;
        int en1 = 0;
        int en2 = 0;
        int en3 = 0;

        if (mEndIp.equals("")) {
            mIpList.add(mStIp);
        } else {

            String s[];
            if ((s = mStIp.split("\\.")).length == 4) {
                st0 = Integer.parseInt(s[0]);
                st1 = Integer.parseInt(s[1]);
                st2 = Integer.parseInt(s[2]);
                st3 = Integer.parseInt(s[3]);

                s = null;
                if ((s = mEndIp.split("\\.")).length == 4) {

                    en0 = Integer.parseInt(s[0]);
                    en1 = Integer.parseInt(s[1]);
                    en2 = Integer.parseInt(s[2]);
                    en3 = Integer.parseInt(s[3]);
                } else {
                    JOptionPane.showMessageDialog(null, "(EndIp) Formula Type the address does not match.");
                    return;
                }


                int progress = 0;
                for (int i = st0; i < en0 + 1; i++) {
                    for (int j = st1; j < en1 + 1; j++) {
                        for (int k = st2; k < en2 + 1; k++) {
                            for (int l = st3; l < en3 + 1; l++) {
                                mIpList.add(String.format("%d.%d.%d.%d", i, j, k, l));
                                if (progress == 101) progress = 0;
                                Application.getGui().setProgress("Creating Ip list..", progress++);
                            }
                        }
                    }
                }

                try {
                    Application.getGui().setProgress("Ip list created.", 100);
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "(StIp) Formula Type the address does not match.");
                return;
            }
        }

        for (int i = 0; i < mIpList.size(); i++) {
            System.out.println(mIpList.get(i));
        }

        CallBack callBack = Application.getGui();
        callBack.onSuccess(mIpList);

    }


}
