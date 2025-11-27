/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tugasAkhir;

import java.awt.Color;
import java.io.File;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author user
 */
public class HomeFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(HomeFrame.class.getName());

    /**
     * Creates new form HomePage2
     */
    public HomeFrame() {
        initComponents();
        initCustomComponents();
        initializeJadwalSholat();
        initializeLocationData(); 
        initializeToggleColors();
        updateWaktuSholat(cmbLocation.getSelectedItem().toString());
        
        startReminder(); // Pindahkan ke sini
    }

    private void initCustomComponents() {
        mainPanel.setBackground(new java.awt.Color(255, 255, 255, 80));
        
        btnSave.setBackground(new Color(0, 102, 153));
        btnSave.setForeground(Color.white);
        
        btnClose.setBackground(Color.red);
        btnClose.setForeground(Color.white);
    }
    
    private void manageReminderState(String sholatName, boolean isActive) {

        if (isActive) {
            System.out.println("Status reminder " + sholatName + ": AKTIF");
            // Logika untuk mengatur timer atau scheduler di Java (seperti java.util.Timer)
        } else {
            System.out.println("Status reminder " + sholatName + ": NONAKTIF");
            // Logika untuk membatalkan timer yang sudah aktif
        }
    }
    private void initializeLocationData() {
        // 1. Dapatkan model default dari JComboBox
        javax.swing.DefaultComboBoxModel<String> model = 
            new javax.swing.DefaultComboBoxModel<>();

        // 2. Tambahkan lokasi ke model
        model.addElement("Jakarta, WIB");
        model.addElement("Bandung, WIB");
        model.addElement("Semarang, WIB");
        model.addElement("Surabaya, WIB");
        model.addElement("Medan, WIB");
        model.addElement("Denpasar, WITA");
        model.addElement("Makassar, WITA");
        model.addElement("Jayapura, WIT");

        // 3. Terapkan model ke JComboBox
        cmbLocation.setModel(model);

        // (Opsional) Pilih item pertama sebagai default
        cmbLocation.setSelectedIndex(0);
    }
    private Map<String, String[]> jadwalSholat = new HashMap<>();
    private void initializeJadwalSholat() {
        // [Subuh, Dzuhur, Ashar, Maghrib, Isya]
        jadwalSholat.put("Jakarta, WIB",    new String[]{"04:06", "11:47", "15:11", "17:59", "19:14"});
        jadwalSholat.put("Bandung, WIB",    new String[]{"04:05", "11:46", "15:10", "17:58", "19:13"});
        jadwalSholat.put("Semarang, WIB",   new String[]{"03:56", "11:36", "14:59", "17:47", "19:03"});
        jadwalSholat.put("Surabaya, WIB",   new String[]{"03:44", "11:23", "14:47", "17:35", "18:52"});
        jadwalSholat.put("Medan, WIB",      new String[]{"04:19", "12:03", "15:26", "18:13", "19:27"});
        jadwalSholat.put("Denpasar, WITA",  new String[]{"03:33", "11:22", "14:45", "17:33", "18:47"});
        jadwalSholat.put("Makassar, WITA",  new String[]{"03:55", "12:03", "15:26", "18:08", "19:19"});
        jadwalSholat.put("Jayapura, WIT",   new String[]{"03:27", "11:50", "15:16", "18:12", "19:20"});
        
    }
    
    private void updateWaktuSholat(String lokasi) {
        if (jadwalSholat.containsKey(lokasi)) {
            String[] waktu = jadwalSholat.get(lokasi);

            // 1. Memperbarui JLabel dengan data baru
            lblTimeSubuh.setText(waktu[0]);
            lblTimeDzuhur.setText(waktu[1]);
            lblTimeAshar.setText(waktu[2]);
            lblTimeMaghrib.setText(waktu[3]);
            lblTimeIsya.setText(waktu[4]);

            // 2. Memperbarui variabel instance LocalTime
            timeSubuh = LocalTime.parse(waktu[0]);
            timeDzuhur = LocalTime.parse(waktu[1]);
            timeAshar = LocalTime.parse(waktu[2]);
            timeMaghrib = LocalTime.parse(waktu[3]);
            timeIsya = LocalTime.parse(waktu[4]);

            System.out.println("Jadwal Sholat untuk " + lokasi + " berhasil diperbarui.");
        } else {
            System.out.println("Data jadwal sholat untuk " + lokasi + " tidak tersedia.");
        }
    }
    
    private void playAdzan() {
        try {
            File file = new File("src/audio/adzan.wav"); 
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void startReminder() {
        Timer timer = new Timer(1000, e -> {
            LocalTime now = LocalTime.now().withSecond(0).withNano(0);
            LocalTime checkResetTime = LocalTime.of(3, 0).withSecond(0).withNano(0); // 03:00

            // **LOGIKA RESET HARIAN**
            if (now.equals(checkResetTime)) {
                 // Reset semua flag Adzan pada jam 03:00 pagi
                subuhAdzanPlayed = false;
                dzuhurAdzanPlayed = false;
                asharAdzanPlayed = false;
                maghribAdzanPlayed = false;
                isyaAdzanPlayed = false;
            }

            // **CEK SUBUH**
            if (tglSubuh.isSelected() && timeSubuh != null && now.equals(timeSubuh) && !subuhAdzanPlayed) {
                playAdzan();
                subuhAdzanPlayed = true; // Setel flag agar tidak berbunyi lagi
            }

            // **CEK DZUHUR**
            if (tglDzuhur.isSelected() && timeDzuhur != null && now.equals(timeDzuhur) && !dzuhurAdzanPlayed) {
                playAdzan();
                dzuhurAdzanPlayed = true;
            }

            // **CEK ASHAR**
            if (tglAshar.isSelected() && timeAshar != null && now.equals(timeAshar) && !asharAdzanPlayed) {
                playAdzan();
                asharAdzanPlayed = true;
            }

            // **CEK MAGHRIB**
            if (tglMaghrib.isSelected() && timeMaghrib != null && now.equals(timeMaghrib) && !maghribAdzanPlayed) {
                playAdzan();
                maghribAdzanPlayed = true;
            }

            // **CEK ISYA**
            if (tglIsya.isSelected() && timeIsya != null && now.equals(timeIsya) && !isyaAdzanPlayed) {
                playAdzan();
                isyaAdzanPlayed = true;
            }

        });
        timer.start();
    }

    
    private boolean reminderStarted = false;
    // Hapus LocalTime timeSubuh, timeDzuhur, timeAshar, timeMaghrib, timeIsya; dari bawah //
    // private boolean reminderStarted = false;
    private LocalTime timeSubuh, timeDzuhur, timeAshar, timeMaghrib, timeIsya; // Biarkan ini
    private boolean subuhAdzanPlayed = false;
    private boolean dzuhurAdzanPlayed = false;
    private boolean asharAdzanPlayed = false;
    private boolean maghribAdzanPlayed = false;
    private boolean isyaAdzanPlayed = false;

     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        pnlLocation = new javax.swing.JPanel();
        cmbLocation = new javax.swing.JComboBox<>();
        pnlSubuh = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblTimeSubuh = new javax.swing.JLabel();
        tglSubuh = new javax.swing.JToggleButton();
        pnlDzuhur = new javax.swing.JPanel();
        lblTimeDzuhur = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tglDzuhur = new javax.swing.JToggleButton();
        pnlAshar = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblTimeAshar = new javax.swing.JLabel();
        tglAshar = new javax.swing.JToggleButton();
        pnlMaghrib = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblTimeMaghrib = new javax.swing.JLabel();
        tglMaghrib = new javax.swing.JToggleButton();
        pnlIsya = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblTimeIsya = new javax.swing.JLabel();
        tglIsya = new javax.swing.JToggleButton();
        btnSave = new javax.swing.JButton();
        pnlBackGround = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(900, 500));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnClose.setText("X");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        mainPanel.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 40, 40));

        lblTitle.setFont(new java.awt.Font("Segoe Print", 1, 20)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("Jadwal Sholat Hari Ini");
        mainPanel.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 250, 30));

        pnlLocation.setOpaque(false);
        pnlLocation.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbLocation.setForeground(new java.awt.Color(0, 0, 153));
        cmbLocation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jakarta, WIB", "Bandung, WIB", "Semarang, WIB", "Surabaya, WIB", "Medan, WIB", "Denpasar, WITA", "Makassar, WITA", "Jayapura, WIT" }));
        cmbLocation.setToolTipText("");
        cmbLocation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbLocationItemStateChanged(evt);
            }
        });
        pnlLocation.add(cmbLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 130, 25));

        mainPanel.add(pnlLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 140, 35));

        pnlSubuh.setBackground(new java.awt.Color(255, 255, 255));
        pnlSubuh.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe Print", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 153));
        jLabel1.setText("Subuh");
        pnlSubuh.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 20));

        lblTimeSubuh.setForeground(new java.awt.Color(0, 0, 102));
        lblTimeSubuh.setText("00.00");
        pnlSubuh.add(lblTimeSubuh, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 80, 20));

        tglSubuh.setName(""); // NOI18N
        tglSubuh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglSubuhActionPerformed(evt);
            }
        });
        pnlSubuh.add(tglSubuh, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 5, 60, 30));

        mainPanel.add(pnlSubuh, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 300, 40));

        pnlDzuhur.setBackground(new java.awt.Color(255, 255, 255));
        pnlDzuhur.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTimeDzuhur.setForeground(new java.awt.Color(0, 0, 102));
        lblTimeDzuhur.setText("00.00");
        pnlDzuhur.add(lblTimeDzuhur, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 80, 20));

        jLabel3.setFont(new java.awt.Font("Segoe Print", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 153));
        jLabel3.setText("Dhuhur");
        pnlDzuhur.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 20));

        tglDzuhur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglDzuhurActionPerformed(evt);
            }
        });
        pnlDzuhur.add(tglDzuhur, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 5, 60, 30));

        mainPanel.add(pnlDzuhur, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 195, 300, 40));

        pnlAshar.setBackground(new java.awt.Color(255, 255, 255));
        pnlAshar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe Print", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Ashar");
        pnlAshar.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 20));

        lblTimeAshar.setForeground(new java.awt.Color(0, 0, 102));
        lblTimeAshar.setText("00.00");
        pnlAshar.add(lblTimeAshar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 80, 20));

        tglAshar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglAsharActionPerformed(evt);
            }
        });
        pnlAshar.add(tglAshar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 5, 60, 30));

        mainPanel.add(pnlAshar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 300, 40));

        pnlMaghrib.setBackground(new java.awt.Color(255, 255, 255));
        pnlMaghrib.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe Print", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 153));
        jLabel5.setText("Maghrib");
        pnlMaghrib.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 20));

        lblTimeMaghrib.setForeground(new java.awt.Color(0, 0, 102));
        lblTimeMaghrib.setText("00.00");
        pnlMaghrib.add(lblTimeMaghrib, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 80, 20));

        tglMaghrib.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglMaghribActionPerformed(evt);
            }
        });
        pnlMaghrib.add(tglMaghrib, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 5, 60, 30));

        mainPanel.add(pnlMaghrib, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 285, 300, 40));

        pnlIsya.setBackground(new java.awt.Color(255, 255, 255));
        pnlIsya.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Segoe Print", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 153));
        jLabel6.setText("Isya");
        pnlIsya.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 20));

        lblTimeIsya.setForeground(new java.awt.Color(0, 0, 102));
        lblTimeIsya.setText("00.00");
        pnlIsya.add(lblTimeIsya, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 80, 20));

        tglIsya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglIsyaActionPerformed(evt);
            }
        });
        pnlIsya.add(tglIsya, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 5, 60, 30));

        mainPanel.add(pnlIsya, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 300, 40));

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        mainPanel.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, 300, 40));

        jPanel1.add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 400, 460));

        pnlBackGround.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/background masjid 2.jpg"))); // NOI18N
        pnlBackGround.setPreferredSize(new java.awt.Dimension(900, 500));
        jPanel1.add(pnlBackGround, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tglDzuhurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglDzuhurActionPerformed
        // TODO add your handling code here:
        boolean isReminderOn = tglDzuhur.isSelected();

        if (isReminderOn) {
            // Status ON
            // 1. Ubah Teks menjadi ON
            tglDzuhur.setText("ON"); 

            // 2. Ubah Warna menjadi HIJAU
            tglDzuhur.setBackground(Color.GREEN.darker()); 
            tglDzuhur.setForeground(Color.WHITE); 

            // 3. Panggil logika reminder
            manageReminderState("Dzuhur", true);

        } else {
            // Status OFF
            // 1. Ubah Teks menjadi OFF
            tglDzuhur.setText("OFF"); 

            // 2. Ubah Warna menjadi MERAH
            tglDzuhur.setBackground(Color.RED.darker());
            tglDzuhur.setForeground(Color.WHITE); 

            // 3. Panggil logika reminder
            manageReminderState("Dzuhur", false);
        }
    }//GEN-LAST:event_tglDzuhurActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void tglSubuhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglSubuhActionPerformed
        // 1. Cek Status Toggle Button
        boolean isReminderOn = tglSubuh.isSelected();

        if (isReminderOn) {
            // Status ON
            // 1. Ubah Teks menjadi ON
            tglSubuh.setText("ON"); 

            // 2. Ubah Warna menjadi HIJAU
            tglSubuh.setBackground(Color.GREEN.darker()); 
            tglSubuh.setForeground(Color.WHITE); 

            // 3. Panggil logika reminder
            manageReminderState("Subuh", true);

        } else {
            // Status OFF
            // 1. Ubah Teks menjadi OFF
            tglSubuh.setText("OFF"); 

            // 2. Ubah Warna menjadi MERAH
            tglSubuh.setBackground(Color.RED.darker());
            tglSubuh.setForeground(Color.WHITE); 

            // 3. Panggil logika reminder
            manageReminderState("Subuh", false);
        }
    }//GEN-LAST:event_tglSubuhActionPerformed

    private void tglAsharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglAsharActionPerformed
        // TODO add your handling code here:
        boolean isReminderOn = tglAshar.isSelected();

        if (isReminderOn) {
            // Status ON
            // 1. Ubah Teks menjadi ON
            tglAshar.setText("ON"); 

            // 2. Ubah Warna menjadi HIJAU
            tglAshar.setBackground(Color.GREEN.darker()); 
            tglAshar.setForeground(Color.WHITE); 

            // 3. Panggil logika reminder
            manageReminderState("Ashar", true);

        } else {
            // Status OFF
            // 1. Ubah Teks menjadi OFF
            tglAshar.setText("OFF"); 

            // 2. Ubah Warna menjadi MERAH
            tglAshar.setBackground(Color.RED.darker());
            tglAshar.setForeground(Color.WHITE); 

            // 3. Panggil logika reminder
            manageReminderState("Ashar", false);
        }
    }//GEN-LAST:event_tglAsharActionPerformed

    private void tglMaghribActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglMaghribActionPerformed
        // TODO add your handling code here:
        boolean isReminderOn = tglMaghrib.isSelected();

        if (isReminderOn) {
            // Status ON
            // 1. Ubah Teks menjadi ON
            tglMaghrib.setText("ON"); 

            // 2. Ubah Warna menjadi HIJAU
            tglMaghrib.setBackground(Color.GREEN.darker()); 
            tglMaghrib.setForeground(Color.WHITE); 

            // 3. Panggil logika reminder
            manageReminderState("Maghrib", true);

        } else {
            // Status OFF
            // 1. Ubah Teks menjadi OFF
            tglMaghrib.setText("OFF"); 

            // 2. Ubah Warna menjadi MERAH
            tglMaghrib.setBackground(Color.RED.darker());
            tglMaghrib.setForeground(Color.WHITE); 

            // 3. Panggil logika reminder
            manageReminderState("Maghrib", false);
        }
    }//GEN-LAST:event_tglMaghribActionPerformed

    private void tglIsyaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglIsyaActionPerformed
        // TODO add your handling code here:
        boolean isReminderOn = tglIsya.isSelected();

        if (isReminderOn) {
            // Status ON
            // 1. Ubah Teks menjadi ON
            tglIsya.setText("ON"); 

            // 2. Ubah Warna menjadi HIJAU
            tglIsya.setBackground(Color.GREEN.darker()); 
            tglIsya.setForeground(Color.WHITE); 

            // 3. Panggil logika reminder
            manageReminderState("Isya", true);

        } else {
            // Status OFF
            // 1. Ubah Teks menjadi OFF
            tglIsya.setText("OFF"); 

            // 2. Ubah Warna menjadi MERAH
            tglIsya.setBackground(Color.RED.darker());
            tglIsya.setForeground(Color.WHITE); 

            // 3. Panggil logika reminder
            manageReminderState("Isya", false);
        }
    }//GEN-LAST:event_tglIsyaActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        // Ambil status setiap tombol
        boolean subuhStatus = tglSubuh.isSelected();
        boolean dzuhurStatus = tglDzuhur.isSelected();
        boolean asharStatus = tglAshar.isSelected();
        boolean maghribStatus = tglMaghrib.isSelected();
        boolean isyaStatus = tglIsya.isSelected();
        
        timeSubuh = LocalTime.parse("04:25");
        timeDzuhur = LocalTime.parse("11:40");
        timeAshar = LocalTime.parse("15:05");
        timeMaghrib = LocalTime.parse("17:25");
        timeIsya = LocalTime.parse("18:45");

        // tampilkan status
        System.out.println("Jadwal berhasil disimpan.");

        // Beri feedback ke pengguna
        JOptionPane.showMessageDialog(this, "Pengaturan Reminder berhasil disimpan!");
    }//GEN-LAST:event_btnSaveActionPerformed

    private void cmbLocationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbLocationItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            // Mendapatkan lokasi yang dipilih
            String lokasiTerpilih = cmbLocation.getSelectedItem().toString();

            // Memanggil fungsi pembaruan jadwal sholat
            updateWaktuSholat(lokasiTerpilih);
        }
    }//GEN-LAST:event_cmbLocationItemStateChanged
    
    private void initializeToggleColors() {
        // Asumsikan default status Subuh adalah ON (true)
        // Untuk tglSubuh
        if (tglSubuh.isSelected()) {
            tglSubuh.setBackground(Color.GREEN.darker());
            tglSubuh.setText("ON"); // <-- Tambahkan ini
        } else {
            // Jika defaultnya OFF
            tglSubuh.setBackground(Color.RED.darker());
            tglSubuh.setText("OFF");
        }
        tglSubuh.setForeground(Color.WHITE);
        
        if (tglDzuhur.isSelected()) {
            tglDzuhur.setBackground(Color.GREEN.darker());
            tglDzuhur.setText("ON");
        } else {
            // Jika defaultnya OFF
            tglDzuhur.setBackground(Color.RED.darker());
            tglDzuhur.setText("OFF");
        }
        tglDzuhur.setForeground(Color.WHITE);
        
        if (tglAshar.isSelected()) {
            tglAshar.setBackground(Color.GREEN.darker());
            tglAshar.setText("ON");
        } else {
            // Jika defaultnya OFF
            tglAshar.setBackground(Color.RED.darker());
            tglAshar.setText("OFF");
        }
        tglAshar.setForeground(Color.WHITE);
        
        if (tglMaghrib.isSelected()) {
            tglMaghrib.setBackground(Color.GREEN.darker());
            tglMaghrib.setText("ON");
        } else {
            // Jika defaultnya OFF
            tglMaghrib.setBackground(Color.RED.darker());
            tglMaghrib.setText("OFF");
        }
        tglMaghrib.setForeground(Color.WHITE);
        
        if (tglIsya.isSelected()) {
            tglIsya.setBackground(Color.GREEN.darker());
            tglIsya.setText("ON");
        } else {
            // Jika defaultnya OFF
            tglIsya.setBackground(Color.RED.darker());
            tglIsya.setText("OFF");
        }
        tglIsya.setForeground(Color.WHITE);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new HomeFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbLocation;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblTimeAshar;
    private javax.swing.JLabel lblTimeDzuhur;
    private javax.swing.JLabel lblTimeIsya;
    private javax.swing.JLabel lblTimeMaghrib;
    private javax.swing.JLabel lblTimeSubuh;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel pnlAshar;
    private javax.swing.JLabel pnlBackGround;
    private javax.swing.JPanel pnlDzuhur;
    private javax.swing.JPanel pnlIsya;
    private javax.swing.JPanel pnlLocation;
    private javax.swing.JPanel pnlMaghrib;
    private javax.swing.JPanel pnlSubuh;
    private javax.swing.JToggleButton tglAshar;
    private javax.swing.JToggleButton tglDzuhur;
    private javax.swing.JToggleButton tglIsya;
    private javax.swing.JToggleButton tglMaghrib;
    private javax.swing.JToggleButton tglSubuh;
    // End of variables declaration//GEN-END:variables
}
