/*
Copyright 2008-2010 Gephi
Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gephi.toolkit.demos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.AutoLayout;

import myPlugin.*;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.plugin.NodeColorTransformer;
import org.gephi.preview.api.*;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import processing.core.PApplet;

/**
 *
 * @author Mathieu Bastian
 */
public class PreviewJFrame {
    
    public void script() {
        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
        
        //Import file
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        Container container;
        try {
            File file = new File(getClass().getResource("/org/gephi/toolkit/demos/resources/dependency.dot").toURI());
            container = importController.importFile(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);

        //Import DirectedGraph
        DirectedGraph graph = graphModel.getDirectedGraph();
        
        //Partition with 'source' column, which is in the data
        PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
        Partition p = partitionController.buildPartition(attributeModel.getNodeTable().getColumn("ClusterID"), graph);
        NodeColorTransformer nodeColorTransformer = new NodeColorTransformer();
        nodeColorTransformer.randomizeColors(p);
        partitionController.transform(p, nodeColorTransformer);
        
        //Layout for 1 minute
        AutoLayout autoLayout = new AutoLayout(10, TimeUnit.SECONDS);
        autoLayout.setGraphModel(graphModel);
        //YifanHuLayout firstLayout = new YifanHuLayout(null, new StepDisplacement(1f));       
        //FruchtermanReingold secondLayout = new FruchtermanReingold(null);
        
        //AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout.createDynamicProperty("forceAtlas.adjustSizes.name", Boolean.TRUE, 0.1f);//True after 10% of layout time
        //AutoLayout.DynamicProperty repulsionProperty = AutoLayout.createDynamicProperty("forceAtlas.repulsionStrength.name", new Double(500.), 0f);//500 for the complete period
        Mylayout my = new Mylayout(null);
        autoLayout.addLayout(my, 1.0f);
        //autoLayout.addLayout(secondLayout, 0.3f, new AutoLayout.DynamicProperty[]{adjustBySizeProperty, repulsionProperty});
        autoLayout.execute();
                
        //Preview configuration
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);       
        PreviewModel previewModel = previewController.getModel();
        
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.BLACK));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 10f);
        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE);
        previewController.refreshPreview();

        //New Processing target, get the PApplet
        ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
        PApplet applet = target.getApplet();
        applet.init();

        //Refresh the preview and reset the zoom
        previewController.render(target);
        target.refresh();
        target.resetZoom();

        //Add the applet to a JFrame and display
        JFrame frame = new JFrame("Test Preview");
        frame.setLayout(new BorderLayout());
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(applet, BorderLayout.CENTER);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        PreviewJFrame previewJFrame = new PreviewJFrame();
        previewJFrame.script();
    }
}
