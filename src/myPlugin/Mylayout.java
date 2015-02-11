package myPlugin;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.layout.plugin.AbstractLayout;
import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutBuilder;
import org.gephi.layout.spi.LayoutProperty;
import org.openide.util.Exceptions;

/**
 *
 * @author Joon
 */
public class Mylayout extends AbstractLayout implements  Layout{

    protected Graph graph;
    GraphModel gm;
    static Random random = new Random();
    ArrayList<Point> staticPoint = new ArrayList<Point>();
    
    public Mylayout(LayoutBuilder mylayoutBuilder){
        super(mylayoutBuilder);
    }

    
    
    @Override
    public void initAlgo() {
        setConverged(false);
    }

    @Override
    public void setGraphModel(GraphModel gm) {
       this.gm = gm;       
    }

    @Override
    public void goAlgo() {
        for(int i =0; i< 100; i++){
            staticPoint.add(new Point());
        }
        
        this.graph = gm.getGraphVisible();
        graph.readLock();
        Node[] nodes = graph.getNodes().toArray();
            
       
        //Reset Layout Data
        for (Node n : nodes) {
                       
            if (n.getNodeData().getLayoutData() == null || !(n.getNodeData().getLayoutData() instanceof myLayoutData)) {
                n.getNodeData().setLayoutData(new myLayoutData());
            }
            myLayoutData layoutData = n.getNodeData().getLayoutData();
            layoutData.freeze = 0;
            layoutData.dx = 0;
            layoutData.dy = 0;
            
            n.getNodeData().setX( staticPoint.get(Integer.parseInt(n.getAttributes().getValue("ClusterID").toString())).x + random.nextInt(30)*10 );
            n.getNodeData().setY( staticPoint.get(Integer.parseInt(n.getAttributes().getValue("ClusterID").toString())).y + random.nextInt(30)*10 );
            
        }
        
        graph.readUnlock();
        
    }

    @Override
    public void endAlgo() {
       for (Node n : graph.getNodes()) {
            n.getNodeData().setLayoutData(null);
        }
    }

    @Override
    public LayoutProperty[] getProperties() {
        List<LayoutProperty> properties = new ArrayList<LayoutProperty>();
        try {
            LayoutProperty mySpeedProperty = LayoutProperty.createProperty(this, Float.class,"Speed","Category","A short description what is this propery doing and how users may modify it",                   "getSpeed",                    "setSpeed");
            properties.add(mySpeedProperty);            
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        }
        return properties.toArray(new LayoutProperty[0]);
    }


    @Override
    public void resetPropertiesValues() {
        
    }

    private static class Point {

        public int x;
        public int y;
        
        public Point() {
            x =  random.nextInt(100) * 50;
            y = random.nextInt(100) * 50;
        }        
    }
    
}
