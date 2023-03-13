package covidfinal.estadisticas;

import covidfinal.colombia.Colombia;

public class Datos extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    Colombia c;

    public Datos(Colombia c) {
        initComponents();
        this.c = c;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        departamento = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("SoulMission", 0, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 204, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Estadisticas Covid-19 Colombia");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(28, 28, 756, 120);

        departamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Colombia", "Amazonas", "Antioquia", "Arauca", "Archipiélago de San Andrés Providencia y Santa Catalina", "Atlántico", "Barranquilla D.E.", "Bogotá D.C.", "Bolívar", "Boyacá", "Buenaventura D.E.", "Caldas", "Caquetá", "Cartagena D.T. y C.", "Casanare", "Cauca", "Cesar", "Chocó", "Cundinamarca", "Córdoba", "Huila", "La Guajira", "Magdalena", "Meta", "Nariño", "Norte de Santander", "Putumayo", "Quindio", "Risaralda", "Santa Marta D.T. y C.", "Santander", "Sucre", "Tolima", "Valle del Cauca", "Vaupés" }));
        departamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departamentoActionPerformed(evt);
            }
        });
        getContentPane().add(departamento);
        departamento.setBounds(216, 156, 324, 96);

        jButton1.setText("Rt");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(96, 276, 240, 96);

        jButton2.setText("Log Infectados");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(420, 276, 240, 96);

        jButton3.setText("Infectados");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(264, 397, 228, 96);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void departamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departamentoActionPerformed

    }//GEN-LAST:event_departamentoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String nombre = this.departamento.getSelectedItem().toString();

        c.graficarRtProfesor(nombre);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        String nombre = this.departamento.getSelectedItem().toString();

        c.graficarLogInfectados(nombre);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        String nombre = this.departamento.getSelectedItem().toString();

        c.graficasActivos(nombre);
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> departamento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
