package coloreffect;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class testdivseqellipseup extends JPanel{
  public static final int row = 5;
  public static final int column = 5;

  public testdivseqellipseup() {
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new GridLayout(row, column));
    f.setVisible(true);
    f.setSize(300, 300);

    Divseqellipseup divseqellipseup1 = new Divseqellipseup(row, column, 95, 20, 80, 99, 0, 150, 0);



    for(int i = 0; i < row; i ++){
      for(int j = 0; j < column; j ++){


        CIELabToSRGB cIELabToSRGB1 = new CIELabToSRGB(divseqellipseup1.labcolor[i][j].L, divseqellipseup1.labcolor[i][j].a, divseqellipseup1.labcolor[i][j].b);
        JPanel p = new JPanel();
        p.setBackground(new Color((int)cIELabToSRGB1.R255, (int)cIELabToSRGB1.G255, (int)cIELabToSRGB1.B255));
        f.getContentPane().add(p);

      }
    }

    f.setTitle("Sample effects: Diverging-Sequential");

    f.repaint();
    f.validate();
  }

}


