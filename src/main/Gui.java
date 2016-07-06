package main;

import app.Application;
import func.GenerateIpList;
import func.Check;
import interfacees.CallBack;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by hrant on 2/27/16.
 */
public class Gui extends JFrame implements ActionListener, CallBack {

    private JLabel mTxt = new JLabel();

    // Program Name Octavia port scanner
    private JPanel mProgramNamePanel = new JPanel();

    // ip panel
    private JPanel mIpPanel = new JPanel();
    // Start ip
    private JPanel mStartIpPanel = new JPanel();
    private JTextField mStIpField = new JTextField(10);


    // End ip
    private JPanel mEndIpPanel = new JPanel();
    private JTextField mEndIpField = new JTextField(10);

    // Port
    private JPanel mPortPanel = new JPanel();

    // St port
    private JTextField mStPortField = new JTextField(6);
//    private JTextField mEndPortField = new JTextField(6);

    // Btn
    private JPanel mBtnPanel = new JPanel();
    private JButton mScanBtn = new JButton("Scan");

    // table
    private JPanel mTbPanel = new JPanel();
    private Vector<String> mColumnNames = new Vector<String>();
    private Vector<Object> mData = new Vector<Object>();
    private DefaultTableModel mTableModel = new DefaultTableModel();
    private JTable mTable = new JTable(mTableModel);

    // ProgressBar
    private Thread mProgressThread;
    private JProgressBar mBar = new JProgressBar(0, 100);
    private JLabel mProgressNameLabel = new JLabel();
    private int mPort = 0;

    public Gui() {
        initGuiComponents();
        mScanBtn.addActionListener(this);
    }

    public Gui(int i) {
    }


    private void initGuiComponents() {
        setLayout(new FlowLayout());
        setSize(new Dimension(618, 500));

        //init program Name panel
        mProgramNamePanel.setLayout(new FlowLayout());
        mProgramNamePanel.setPreferredSize(new Dimension(590, 40));
        mTxt.setPreferredSize(new Dimension(580, 40));
        mProgramNamePanel.setBackground(Color.ORANGE);
//        mTxt.setFont(new Font("Serif", Font.BOLD, 74));
        mTxt = new JLabel("<html><span style='font-family:Courier; font-size: 20px;'>Octavia port scanner</span></html>");
        mProgramNamePanel.add(mTxt);

        //init ip panel
        mIpPanel.setLayout(new FlowLayout());
        mTxt = new JLabel("Start ip: ");
        mIpPanel.add(mTxt);
        mIpPanel.add(mStIpField);
        mTxt = new JLabel(" -  End ip: ");
        mIpPanel.add(mTxt);
        mIpPanel.add(mEndIpField);

        // init port panel
        mPortPanel.setLayout(new FlowLayout());
        mTxt = new JLabel("Port: ");
        mPortPanel.add(mTxt);
        mPortPanel.add(mStPortField);
//        mTxt = new JLabel(" -  End port: ");
//        mPortPanel.add(mTxt);
//        mPortPanel.add(mEndPortField);

        // init Btn panel
        mBtnPanel.setLayout(new FlowLayout());
        mBtnPanel.add(mScanBtn);

        // init tbPanel
        mTbPanel.setLayout(new FlowLayout());

        mTable.setPreferredScrollableViewportSize(new Dimension(580, 300));
        mTable.setFillsViewportHeight(true);

        JScrollPane js = new JScrollPane(mTable);
        js.setVisible(true);
        mTbPanel.add(js);


        add(mProgramNamePanel);
        add(mIpPanel);
        add(mPortPanel);
        add(mBtnPanel);
        add(mTbPanel);


        add(mProgressNameLabel);
        add(mBar);

        mColumnNames.add("No");
        mColumnNames.add("Ip v4 address ");
        mColumnNames.add("Open ports");

        mTableModel.setColumnIdentifiers(mColumnNames);

        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public boolean addNewRow(Object object) {

        if (object != null) {
            if (object instanceof Vector) {
                mTableModel.addRow((Vector) object);
                System.out.println("Added new row");
                return true;
            } else {
                System.out.println("Object is not Vector");
                return false;
            }
        } else {
            System.out.println("Vector is null");
            return false;
        }
    }

    public void setProgress(String label, int progress) {
        mProgressNameLabel.setText(label);
        mBar.setValue(progress);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mScanBtn) {
            String stIp = new String();
            String endIp = new String();
            int port;
            /*
            * if start ip filed is not empty then continue
            * else
            *   stop process
            * */
            if (mStIpField.getText().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "StIp filed is empty.");
                return;
            }

            /*
            * if filed not null and port num > 0 && num < 65000 port = filed.getText parse int
            * else return
            * */
            if (!mStPortField.getText().toString().equals("")) {
                try {
                    port = Integer.parseInt(mStPortField.getText().toString());

                    if (!(port > 0 && port < 65000)) {
                        JOptionPane.showMessageDialog(null, "Port number interval from 0 to 65000.");
                        return;
                    }
                } catch (java.lang.NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Port is not number.");
                    ex.printStackTrace();
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Port filed is empty.");
                return;
            }
            stIp = mStIpField.getText().toString();
            endIp = mEndIpField.getText().toString();
            mPort = port;
            new GenerateIpList(stIp, endIp, port).start();
        }
    }

    // CallBack function from GenerateIpList class
    public int onSuccess(List<String> ipList) {


        mBar.setMaximum(ipList.size());
        Check ping = new Check();
        for (int i = 0; i < ipList.size(); i++) {


            boolean isOpen;
            System.out.println(mPort);

            isOpen = ping.checkByPort(ipList.get(i), mPort);

            setProgress(String.format("Checking...  %d", i), i);

            mData = new Vector<Object>();
            mData.add("No: " + (new Integer((Application.getNo()))));
            mData.add(ipList.get(i));
            mData.add(new Boolean(isOpen));
            addNewRow(mData);
        }

        setProgress("Complete.", 100);
//        46.162.192.20
        return 0;
    }
}
