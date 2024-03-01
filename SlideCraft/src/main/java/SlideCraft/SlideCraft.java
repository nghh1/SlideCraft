
package SlideCraft;

import com.formdev.flatlaf.FlatLightLaf;

import SlideCraft.ui.SlideCraftFrame;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class SlideCraft {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.invokeLater(() -> SlideCraftFrame.getInstance((args.length > 0) ? args[0] : null));
        }
        catch (Exception e){
            System.out.println("Error getting instance of a frame: "+e);
        }
    }
}