package org.vaadin.core;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
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
        	Clipboard clipBoard=new Clipboard(display);
        	TextTransfer textTransfer=TextTransfer.getInstance();
        	clipBoard.setContents(new Object[]{sourceCode}, new Transfer[]{textTransfer});
        	clipBoard.dispose();
		} catch (JavaModelException e) {
			MessageDialog.openInformation(window.getShell(), "Error while exporting design", e.getCause().toString());
			e.printStackTrace();
		}
        return null;
    }

}
