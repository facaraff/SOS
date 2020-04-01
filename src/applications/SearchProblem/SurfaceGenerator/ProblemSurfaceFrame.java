package applications.SearchProblem.SurfaceGenerator;

import org.sf.surfaceplot.SurfaceCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Mikolaj on 18/03/2018.
 */
public class ProblemSurfaceFrame extends javax.swing.JFrame implements ActionListener, KeyListener, ContainerListener {

  
	private static final long serialVersionUID = 1L;//@Fabio to stop warnings
	private SurfaceCanvas canvas;
    private boolean controlDown;

    /** Creates new form Example 
     * @param surfaceGenerator The surface to be plotted.
     * */
    public ProblemSurfaceFrame(ProblemSurfaceGenerator surfaceGenerator) {
        initComponents();
        ProblemSurfaceGenerator model = surfaceGenerator;
        canvas = new SurfaceCanvas();
        canvas.setModel(model);


        setSize(1000, 800);


        centerPanel.add(canvas, BorderLayout.CENTER);
        canvas.repaint();
        setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
 //   @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        centerPanel = new javax.swing.JPanel();
//       this.addContainerListener(this);
//        this.addKeyListener(this);

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        button1 = new javax.swing.JButton("Save Image");

        button1.addActionListener(this);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        centerPanel.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 5));

        jLabel1.setText("Rotate: Mouse Click & Drag");
        jPanel1.add(jLabel1);

        jLabel2.setText("Zoom: Shift Key + Mouse Click & Drag");
        jPanel1.add(jLabel2);

        jLabel3.setText("Move: Control Key + Mouse Click & Drag");
        jPanel1.add(jLabel3);

        jPanel1.add(button1);

        centerPanel.add(jPanel1, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveImage()
    {
        try
        {
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = image.createGraphics();
            canvas.paint(graphics2D);
            ImageIO.write(image, "jpeg", new File("/Users/Mikolaj/Google Drive/MSc/MSc_New/ComputationalIntelligenceOptimisation-IMAT5232/Labs/SOS Software/SOS/results/SavedImages/"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-DDD_HH-mm-ss")) + ".jpeg"));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

//   /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Example().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel centerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton button1;

    @Override
    public void componentAdded(ContainerEvent e)
    {
        //addKeyAndContainerListenerRecursively(e.getChild());
    }

    @Override
    public void componentRemoved(ContainerEvent e) {
        //removeKeyAndContainerListenerRecursively(e.getChild());
    }

//    private void addKeyAndContainerListenerRecursively(Component c)
//    {
////Add KeyListener to the Component passed as an argument
//        c.addKeyListener(this);
////Check if the Component is a Container
//        if(c instanceof Container) {
////Component c is a Container. The following cast is safe.
//            Container cont = (Container)c;
////Add ContainerListener to the Container.
//            cont.addContainerListener(this);
////Get the Container's array of children Components.
//            Component[] children = cont.getComponents();
////For every child repeat the above operation.
//            for(int i = 0; i < children.length; i++){
//                addKeyAndContainerListenerRecursively(children[i]);
//            }
//        }
//    }

//    private void removeKeyAndContainerListenerRecursively(Component c)
//    {
////Add KeyListener to the Component passed as an argument
//        c.removeKeyListener(this);
////Check if the Component is a Container
//        if(c instanceof Container) {
////Component c is a Container. The following cast is safe.
//            Container cont = (Container)c;
////Add ContainerListener to the Container.
//            cont.removeContainerListener(this);
////Get the Container's array of children Components.
//            Component[] children = cont.getComponents();
////For every child repeat the above operation.
//            for(int i = 0; i < children.length; i++){
//                removeKeyAndContainerListenerRecursively(children[i]);
//            }
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        saveImage();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_SPACE)
            jPanel1.setVisible(!jPanel1.isVisible());
//        else if (e.isActionKey() && e.getKeyChar() == 'c')
//            saveImage();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //if (e.getKeyCode()==KeyEvent.VK_SPACE)
        //    jPanel1.setVisible(!jPanel1.isVisible());
        if (controlDown && (e.getKeyChar() == 'c'))
            saveImage();
        else if (e.getKeyCode() == KeyEvent.VK_CONTROL)
            controlDown = true;

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL)
            controlDown = false;
    }
    // End of variables declaration//GEN-END:variables

}
