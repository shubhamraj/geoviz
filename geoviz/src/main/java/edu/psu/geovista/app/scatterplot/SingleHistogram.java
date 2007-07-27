package edu.psu.geovista.app.scatterplot;

/**
 * Title: SingleHistogram
 * @author Frank Hardisty
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.BitSet;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.psu.geovista.data.DescriptiveStatistics;
import edu.psu.geovista.data.geog.DataSetForApps;
import edu.psu.geovista.ui.event.DataSetEvent;
import edu.psu.geovista.ui.event.DataSetListener;
import edu.psu.geovista.ui.event.SelectionEvent;
import edu.psu.geovista.ui.event.SelectionListener;

public class SingleHistogram extends JPanel implements DataSetListener,
		SelectionListener, ActionListener, ChangeListener, TableModelListener {
	Histogram histo;
	JSlider binSlider;
	JLabel nBins;
	JComboBox variableCombo;
	DataSetForApps dataSet;
	protected final static Logger logger = Logger.getLogger(SingleHistogram.class.getName());
	public SingleHistogram() {
		this.setPreferredSize(new Dimension(450, 300));
		this.histo = new Histogram();
		JPanel bottomPanel = new JPanel();
		binSlider = new JSlider();
		binSlider.addChangeListener(this);
		binSlider.setMinimum(3);
		binSlider.setMaximum(100);
		nBins = new JLabel(new Integer(histo.getHistNumber()).toString());
		JLabel sliderLabel = new JLabel("Max N Bins:");
		variableCombo = new JComboBox();
		variableCombo.addActionListener(this);
		bottomPanel.add(variableCombo);
		bottomPanel.add(sliderLabel);
		bottomPanel.add(binSlider);
		bottomPanel.add(nBins);
		this.setLayout(new BorderLayout());
		this.add(histo, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

	}

	/**
	 * adds an SelectionListener.
	 * 
	 * @see EventListenerList
	 */
	public void addSelectionListener(SelectionListener l) {
		histo.addSelectionListener(l);
	}

	/**
	 * removes an SelectionListener from the component.
	 * 
	 * @see EventListenerList
	 */
	public void removeSelectionListener(SelectionListener l) {
		histo.addSelectionListener(l);

	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireSelectionChanged(int[] newSelection) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SelectionEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SelectionListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SelectionEvent(this, newSelection);
				}
				((SelectionListener) listeners[i + 1]).selectionChanged(e);
			}
		}// next i

	}

	public void dataSetChanged(DataSetEvent e) {
		if (this.histo == null){
			return;
		}
		this.dataSet = e.getDataSetForApps();
		this.dataSet.addTableModelListener(this);
		this.histo.dataSetChanged(e);
		this.variableCombo.removeActionListener(this);
		this.variableCombo.removeAllItems();
		for (int i = 0; i < this.dataSet.getNumberNumericAttributes(); i++){
			this.variableCombo.addItem(this.dataSet.getNumericArrayName(i));
		}
		this.variableCombo.addActionListener(this);
	}

	public void selectionChanged(SelectionEvent e) {
		this.histo.selectionChanged(e);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.variableCombo)){
			String name = (String)this.variableCombo.getSelectedItem();
			this.histo.setVariableName(name);
			
			double[] data = this.dataSet.getNumericDataAsDouble(this.variableCombo.getSelectedIndex());
			BitSet selections = (BitSet)this.histo.getSelections().clone();
			this.histo.setData(data);
			this.histo.setSelections(selections);
		}
		
	}

	public void stateChanged(ChangeEvent e) {
		if(this.nBins == null || this.dataSet == null){
			return;
		}
		
		if(e.getSource().equals(this.binSlider)){
			BitSet selections = (BitSet) this.histo.getSelections().clone();
			Integer nBins = this.binSlider.getValue();
			
			this.nBins.setText(nBins.toString());
			//if (!this.binSlider.getValueIsAdjusting()){
			this.histo.setHistNumber(nBins);
			this.histo.setSelections(selections);
			this.histo.repaint();
			//}
		}
		
	}

	public void tableChanged(TableModelEvent e) {
		this.variableCombo.removeActionListener(this);
		this.variableCombo.removeAllItems();
		for (int i = 0; i < this.dataSet.getNumberNumericAttributes(); i++){
			this.variableCombo.addItem(this.dataSet.getNumericArrayName(i));
		}
		this.variableCombo.addActionListener(this);
		
		
	}
	
	public static void main (String[] args){
		int n = 1000;
	   double[] bigData = new double[n];
	   double[] bigData2 = new double[n];
	   String[] names = {"one", "two"};
	   
	   for (int i = 0; i < n; i++){
		   bigData[i] = Math.random() * 100;
		   bigData2[i] = Math.random() * 100;
	   }

	   int m = 10;
	   long start = System.currentTimeMillis();
	   
	   for (int i = 0; i < m; i++){
		   DescriptiveStatistics.mean(bigData);
	   }
	   
	   long end = System.currentTimeMillis();	   
	   
	   logger.finest("mean: that took: " + (end - start)/(float)m);
	   
	   start = System.currentTimeMillis();
	   
	   for (int i = 0; i < m; i++){
		   //DescriptiveStatistics.fineMean(bigData);
	   }
	   
	   end = System.currentTimeMillis();	   
	   
	   logger.finest("fine mean: that took: " + (end - start)/(float)m);	 
	   
	   
	   start = System.currentTimeMillis();
	   
	   for (int i = 0; i < m; i++){
		   //DescriptiveStatistics.stdDev(bigData, false);
	   }
	   
	   end = System.currentTimeMillis();	   
	   
	   logger.finest("stdDev: that took: " + (end - start)/(float)m);
	   
	   
	   
	   Object[] dataObject = {names, bigData, bigData2};
	   
	   DataSetForApps data = new DataSetForApps(dataObject);
	   
	   
	   JFrame app = new JFrame("test big data");
	   
	   SingleHistogram single = new SingleHistogram();
	   DataSetEvent e = new DataSetEvent(data,app);
	   single.dataSetChanged(e);


	   
	   app.add(single);
	   app.pack();
	   app.setVisible(true);
	   //single.setVariableName("one");
	   //single.setData(bigData);	   
	   app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   
	}
	
    public static int f(int i, int j) { return 0; }

    public static void main2(String[] args) {
        //int N = Integer.parseInt(args[0]);
        
    	int N = 5000;
    	long start, stop, elapsed;
        double freq;

        logger.finest("Nanoseconds per operation");
 
       /***************************************************************
        * empty loop
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                ;
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Empty loop:" + "\t" + freq);


       /***************************************************************
        * addition
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Addition:" + "\t" + freq);


       /***************************************************************
        * multiplication
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Multiply:" + "\t" + freq);


       /***************************************************************
        * comparison
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Boolean:" + "\t" + freq);


       /***************************************************************
        * remainder
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Remainder:" + "\t" + freq);


       /***************************************************************
        * division
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Division:" + "\t" + freq);


       /***************************************************************
        * floating point add
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            @SuppressWarnings("unused")
			double fi = i;
            for (int j = 1; j <= N; j++) {
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Float Add:" + "\t" + freq);


       /***************************************************************
        * floating point division
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            @SuppressWarnings("unused")
			double fi = i;
            for (int j = 1; j <= N; j++) {
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Float Division:" + "\t" + freq);


       /***************************************************************
        * floating point multiply
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            @SuppressWarnings("unused")
			double fi = i;
            for (int j = 1; j <= N; j++) {
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Float Multiply:" + "\t" + freq);


       /***************************************************************
        * Empty integer function call
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            @SuppressWarnings("unused")
			int k;
            for (int j = 1; j <= N; j++) {
                k = f(i, j);
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Function call:" + "\t" + freq);


       /***************************************************************
        * Math.sin
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            @SuppressWarnings("unused")
			double k;
            for (int j = 1; j <= N; j++) {
                k = Math.sin(i + j);
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Math.sin:" + "\t" + freq);

       /***************************************************************
        * Math.atan2
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            @SuppressWarnings("unused")
			double k;
            for (int j = 1; j <= N; j++) {
                k = Math.atan2(i, j);
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Math.atan2:" + "\t" + freq);

       /***************************************************************
        * Math.random
        ***************************************************************/
        start = System.currentTimeMillis();
        for (int i = 1; i <= N; i++) {
            @SuppressWarnings("unused")
			double k;
            for (int j = 1; j <= N; j++) {
                k = Math.random();
            }
        }
        stop = System.currentTimeMillis();
        elapsed = stop - start;
        freq = 1.0E6 * elapsed / N / N;
        logger.finest("Math.random:" + "\t" + freq);

   }
	
}
