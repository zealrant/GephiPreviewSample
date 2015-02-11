package myPlugin;


import javax.swing.Icon;
import javax.swing.JPanel;
import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutBuilder;
import org.gephi.layout.spi.LayoutUI;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Joon
 */
@ServiceProvider(service = LayoutBuilder.class)
public class MyLayoutBuilder implements  LayoutBuilder{
    
    @Override
    public String getName() {
        return NbBundle.getMessage(MyLayoutBuilder.class, "name");
    }
    
    @Override
    public Mylayout buildLayout(){
        return Mylayout(this);
    }

    @Override
    public LayoutUI getUI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Mylayout Mylayout(MyLayoutBuilder aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private static class MyLayoutUI implements LayoutUI {

        @Override
        public String getDescription() {
            return NbBundle.getMessage(MyLayoutBuilder.class, "description");
        }

        @Override
        public Icon getIcon() {
            return null;
        }

        @Override
        public JPanel getSimplePanel(Layout layout) {
            return null;
        }

        @Override
        public int getQualityRank() {
            return -1;
        }

        @Override
        public int getSpeedRank() {
            return -1;
        }
    }
    
}
