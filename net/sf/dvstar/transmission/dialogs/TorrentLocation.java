/*
 * transmission-remote-java remote control for transmission daemon
 *
 * Copyright (C) 2009  dvstar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


/*
 * TorrentMoveData.java
 *
 * Created on 28 серп 2009, 14:40:01
 */
package net.sf.dvstar.transmission.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;
import net.sf.dvstar.transmission.protocol.ProtocolConstants;
import net.sf.dvstar.transmission.protocol.Torrent;
import net.sf.dvstar.transmission.protocol.TransmissionManager;
import net.sf.dvstar.transmission.utils.ConfigStorage;
import net.sf.dvstar.transmission.utils.LocalSettiingsConstants;
import org.klomp.snark.MetaInfo;

/**
 *
 * @author dstarzhynskyi
 */
public class TorrentLocation extends javax.swing.JDialog implements ProtocolConstants, LocalSettiingsConstants {

    public static final int MODE_LOCA = 1;
    public static final int MODE_MOVE = 2;
    private Torrent torrent;
    private ConfigStorage config;
    private Logger loggerProvider;
    private String srcDir;
    private String dstDirs;
    private String dstDir;
    private int mode = MODE_LOCA;
    private MetaInfo fileMeta = null;

    /** Creates new form TorrentMoveData */
    public TorrentLocation(java.awt.Frame parent,
            boolean modal,
            int mode,
            Torrent torrent,
            MetaInfo fileMeta,
            TransmissionManager manager) {
        super(parent, modal);
        this.loggerProvider = manager.getGlobalLogger();
        this.config = manager.getGlobalConfigStorage();
        this.torrent = torrent;
        this.mode = mode;
        this.fileMeta = fileMeta;
        initComponents();
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(net.sf.dvstar.transmission.TransmissionApp.class).getContext().getResourceMap(TorrentLocation.class);
        String title = "...";
        switch (mode) {
            case MODE_LOCA: {
                chMove.setEnabled(false);
                chMove.setVisible(false);
                title = resourceMap.getString("TorrentLocation.title.loca.text");
                if (fileMeta != null) {
                    taTorrentName.setText(fileMeta.getName());
                }
            }
            break;
            case MODE_MOVE: {
                chMove.setEnabled(true);
                title = resourceMap.getString("TorrentLocation.title.move.text");
            }
            break;
        }

        this.setTitle(title);

        cbDestination.addActionListener(new MyActionListener());

        //List values = new Vector();

        if (torrent != null) {
            srcDir = torrent.getDownloadDir();
            taTorrentName.setText(torrent.getTorrentName());
            tfSource.setText(srcDir);
        } else {
            tfSource.setVisible(false);
            lbSource.setVisible(false);
        }

        String dlist = config.getProperty(CONF_CLI_USED_DIRS);
        if (dlist != null) {
            dstDirs = dlist;
            cbDestination.removeAllItems();
            StringTokenizer stoken = new StringTokenizer(dlist, ",");
            if (stoken.countTokens() > 0) {
                while (stoken.hasMoreElements()) {
                    //values.add(stoken.nextToken());
                    cbDestination.addItem(stoken.nextToken());
                }
            } else {
                //values.add(dlist);
                cbDestination.addItem(dlist);
            }
        }

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                dstDir = null;
            }
        });


//        MyComboBoxModel model = new MyComboBoxModel(values);
//        cbDestination.setModel(model);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbSource = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        taTorrentName = new javax.swing.JTextArea();
        tfSource = new javax.swing.JTextField();
        cbDestination = new javax.swing.JComboBox();
        chMove = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        btOk = new javax.swing.JButton();
        btClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(320, 200));
        setName("Form"); // NOI18N

        jPanel2.setMinimumSize(new java.awt.Dimension(400, 180));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(320, 137));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(net.sf.dvstar.transmission.TransmissionApp.class).getContext().getResourceMap(TorrentLocation.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        lbSource.setText(resourceMap.getString("lbSource.text")); // NOI18N
        lbSource.setName("lbSource"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        taTorrentName.setColumns(20);
        taTorrentName.setFont(resourceMap.getFont("taTorrentName.font")); // NOI18N
        taTorrentName.setForeground(resourceMap.getColor("taTorrentName.foreground")); // NOI18N
        taTorrentName.setLineWrap(true);
        taTorrentName.setRows(5);
        taTorrentName.setText(resourceMap.getString("taTorrentName.text")); // NOI18N
        taTorrentName.setDisabledTextColor(resourceMap.getColor("taTorrentName.disabledTextColor")); // NOI18N
        taTorrentName.setEnabled(false);
        taTorrentName.setFocusable(false);
        taTorrentName.setName("taTorrentName"); // NOI18N
        taTorrentName.setOpaque(false);

        tfSource.setText(resourceMap.getString("tfSource.text")); // NOI18N
        tfSource.setName("tfSource"); // NOI18N

        cbDestination.setEditable(true);
        cbDestination.setName("cbDestination"); // NOI18N

        chMove.setSelected(true);
        chMove.setText(resourceMap.getString("chMove.text")); // NOI18N
        chMove.setName("chMove"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbSource, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfSource, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                            .addComponent(cbDestination, 0, 283, Short.MAX_VALUE)
                            .addComponent(chMove, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(taTorrentName, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(taTorrentName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbSource)
                    .addComponent(tfSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbDestination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chMove)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(320, 48));

        btOk.setText(resourceMap.getString("btOk.text")); // NOI18N
        btOk.setToolTipText(resourceMap.getString("btOk.toolTipText")); // NOI18N
        btOk.setName("btOk"); // NOI18N
        btOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOkActionPerformed(evt);
            }
        });

        btClose.setText(resourceMap.getString("btClose.text")); // NOI18N
        btClose.setName("btClose"); // NOI18N
        btClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btClose)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 244, Short.MAX_VALUE)
                .addComponent(btOk)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btOk)
                    .addComponent(btClose))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCloseActionPerformed
        dstDir = null;
        //System. out.println( this.getSize().toString() );
        this.dispose();
    }//GEN-LAST:event_btCloseActionPerformed

    private void btOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOkActionPerformed
        if (cbDestination.getItemCount() > 0) {
            dstDirs = "";
            for (int i = 0; i < cbDestination.getItemCount(); i++) {
                String delim = ",";
                if (i == cbDestination.getItemCount() - 1) {
                    delim = "";

                }
                dstDirs += cbDestination.getItemAt(i) + delim;
            }
        }
        config.setProperty(CONF_CLI_USED_DIRS, dstDirs);
        config.saveConfig();
        this.dispose();
    }//GEN-LAST:event_btOkActionPerformed

    public String getDstDir() {
        return dstDir;
    }


    public boolean isMoveData(){ return chMove.isSelected(); }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClose;
    private javax.swing.JButton btOk;
    private javax.swing.JComboBox cbDestination;
    private javax.swing.JCheckBox chMove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbSource;
    private javax.swing.JTextArea taTorrentName;
    private javax.swing.JTextField tfSource;
    // End of variables declaration//GEN-END:variables

    class MyComboBoxModel implements ComboBoxModel {

        private List values;
        private Object selected;

        public MyComboBoxModel(List values) {
            this.values = values;
        }

        public List getValues() {
            return values;
        }

        public void setValues(List values) {
            this.values = values;
        }

        @Override
        public void setSelectedItem(Object anItem) {
            selected = anItem;
        }

        @Override
        public Object getSelectedItem() {
            return selected;
        }

        @Override
        public int getSize() {
            return values.size();
        }

        @Override
        public Object getElementAt(int index) {
            return values.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }
    }

    class MyActionListener implements ActionListener, ItemListener {

        Object oldItem;

        @Override
        public void actionPerformed(ActionEvent evt) {
            JComboBox cb = (JComboBox) evt.getSource();
            Object newItem = cb.getSelectedItem();

            dstDir = cb.getSelectedItem().toString();


            JComboBox anCombo = (JComboBox) evt.getSource();
            DefaultComboBoxModel comboModel = (DefaultComboBoxModel) anCombo.getModel();
            int index = comboModel.getIndexOf(anCombo.getSelectedItem());
            if (index == -1) {
                comboModel.addElement(anCombo.getSelectedItem());
            }//else already added

            /*
            System.out.println("+++++++"+newItem+"="+oldItem);

            boolean same = newItem.equals(oldItem);
            oldItem = newItem;


            if (!same) {
            if(((MyComboBoxModel) cb.getModel()).getValues().indexOf(newItem)<0){
            ((MyComboBoxModel) cb.getModel()).getValues().add(newItem);
            System.out.println("======="+cb.getItemCount());

            }
            }

            if ("comboBoxEdited".equals(evt.getActionCommand())) {
            // User has typed in a string; only possible with an editable combobox
            System.out.println("======="+evt);
            } else if ("comboBoxChanged".equals(evt.getActionCommand())) {
            }
             */
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
        }
    }
}
