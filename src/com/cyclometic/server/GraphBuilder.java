package com.cyclometic.server;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 

public class GraphBuilder extends ApplicationFrame {
	   
	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	


	public  static HashMap<Integer, String> completeHashMapListList = new HashMap<Integer, String>();
    
  
	public GraphBuilder( String applicationTitle , String chartTitle, HashMap<Integer, String> completeHashMapListList ) {
	      super( applicationTitle );        
	    GraphBuilder.completeHashMapListList=completeHashMapListList;
		JFreeChart barChart = ChartFactory.createBarChart(
	         chartTitle,           
	         "Commits",            
	         "Cyclomatic Complexity",            
	         createDataset(),          
	         PlotOrientation.VERTICAL,           
	         true, true, false);
	         
	      ChartPanel chartPanel = new ChartPanel( barChart );        
	      chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
	      setContentPane( chartPanel ); 
	   }
	   
	   public CategoryDataset createDataset() {
	   final DefaultCategoryDataset dataset = 
	      new DefaultCategoryDataset( );  
        for (Entry<Integer, String> entry :GraphBuilder.completeHashMapListList.entrySet()) {
        Number key = entry.getKey();
         String tab = entry.getValue();
         dataset.addValue( key, tab, "FILE");
        
    
          }
	      return dataset; 
 }
	   
	   public void dostuff( ) {
	    
	      pack( );        
	      RefineryUtilities.centerFrameOnScreen( this );        
	      setVisible( true ); 
	   }
	}