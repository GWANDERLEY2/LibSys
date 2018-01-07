package ace4sd.views;

import java.util.Map;    
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;
import ace4sd.Activator;    
  
public class SampleHandler2 extends 
             AbstractHandler implements IElementUpdater{       
    private static ImageDescriptor image_enable = 
        Activator.getImageDescriptor("icons/flag_green.png");
    private static ImageDescriptor image_disable = 
        Activator.getImageDescriptor("icons/stop.png");        
    /**
     * The constructor.
     */
    public SampleHandler2() {

    }    
    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        //...
        return null;
    }    
    @Override
    public void updateElement(UIElement element, @SuppressWarnings("rawtypes") Map map) {
        boolean condition = false;            
        //...            
        if( condition ) { 
            element.setIcon(image_disable);
        }else{
            element.setIcon(image_enable);
        }        
    }
}