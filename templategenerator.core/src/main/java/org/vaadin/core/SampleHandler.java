package org.vaadin.core;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.vaadin.pojo2design.Generator;

/**
 * Executed by click menu.<br/>
 */
public class SampleHandler extends AbstractHandler {

    private final IWorkbenchWindow window;
    private final Display display;

    /**
     * constructor.
     */
    public SampleHandler() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        this.window = workbench.getActiveWorkbenchWindow();
        this.display = workbench.getDisplay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	Object variable = HandlerUtil.getVariable(event, ISources.ACTIVE_CURRENT_SELECTION_NAME);
    	IStructuredSelection selection = (IStructuredSelection)  variable;
    	Object firstElement = selection.getFirstElement();
    	ICompilationUnit file = (ICompilationUnit) firstElement;
    	try {
    		String sourceCode=file.getSource();
        	String template=new Generator(sourceCode).generate();
        	if(template==null || template.trim().length()==0){
    			MessageDialog.openInformation(window.getShell(), "The exported design was empty", "The exported design was empty. Choose a proper pojo class with getters and setters");
        	} else{
            	Clipboard clipBoard=new Clipboard(display);
            	TextTransfer textTransfer=TextTransfer.getInstance();
            	clipBoard.setContents(new Object[]{template}, new Transfer[]{textTransfer});
            	clipBoard.dispose();
        	}
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openInformation(window.getShell(), "Error while exporting design", e.getCause().toString());
		}
        return null;
    }

}
