package net.lbruun.nb.exploreinos;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.CookieAction;

/**
 * Action which will open the local System's file explorer. 
 * Acts on folders only.
 * 
 * @author Lars Bruun-Hansen
 */
@ActionID(
        category = "Projects",
        id = "net.lbruun.nb.exploreinos.ExploreInOsAction"
)
@ActionRegistration(
        displayName = "#CTL_ExploreInOsAction",
        lazy = false,
        asynchronous = true
)
@ActionReferences({
    @ActionReference(path = "Loaders/folder/any/Actions", position = 1600),
    @ActionReference(path = "Projects/Actions")}
)
@Messages("CTL_ExploreInOsAction=Explore Location in OS")
public final class ExploreInOsAction extends CookieAction {

    private final boolean isJDKDesktopAPISupported = Desktop.isDesktopSupported();
    private static final Logger logger = Logger.getLogger(ExploreInOsAction.class.getName());
    private static final Class[] COOKIE_CLASSES = new Class[]{
        DataObject.class,
        FileObject.class
    };


    @Override
    protected Class<?>[] cookieClasses() {
        // If the Node does not have any of these in its lookup
        // then for sure the Node does not represent something which is
        // an object in the host's local file system.
        return COOKIE_CLASSES;
    }

    
    @Override
    protected void performAction(Node[] nodes) {
        FileObject fileObject = getFileObject(nodes);
        if (fileObject != null) {
            File file = FileUtil.toFile(fileObject);
            if (file != null) {
                final File dir = (!file.isDirectory()) ? file.getParentFile() : file;
                if (dir != null) {
                    browseFolderLocation(dir);
                }
            }
        }
    }

    
    @Override
    protected boolean enable(Node[] nodes) {
        if (super.enable(nodes) && isJDKDesktopAPISupported) {
            FileObject fileObject = getFileObject(nodes);
            if (fileObject != null) {
                return isFileURL(fileObject.toURL());
            }
        }
        return false;
    }
 
    @Override
    public String getName() {
         return Bundle.CTL_ExploreInOsAction();
    }

    @Override
    public HelpCtx getHelpCtx() {
         return HelpCtx.DEFAULT_HELP;  // Same as no help
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }
    
    
    private FileObject getFileObject(Node[] nodes) {
        if (nodes != null && nodes.length == 1) { // only enabled for single selection
            Node selectedNode = nodes[0];
            if (selectedNode != null) {
                Lookup lookup = selectedNode.getLookup();
                if (lookup != null) {
                    FileObject fileObject = lookup.lookup(FileObject.class);
                    if (fileObject != null) {
                        return fileObject;
                    }
                    DataObject dataObject = lookup.lookup(DataObject.class);
                    if (dataObject != null) {
                        FileObject primaryFile = dataObject.getPrimaryFile();
                        if (primaryFile != null) {
                            return primaryFile;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isFileURL(URL url) {
        if (url == null) {
            return false;
        }
        String urlProtocol = url.getProtocol();
        if (urlProtocol != null) {
            return (urlProtocol.equals("file"));
        }
        return false;
    }

    private void browseFolderLocation(File dir) {
        // From JDK 9 onwards there is now a dedicated method for this in the 
        // Desktop class: browseFileDirectory(). However, it does not work, nor 
        // is it intended to work. See https://bugs.openjdk.java.net/browse/JDK-8233994
        // for more information. Instead the open() method actually seems to
        // work on all platforms.
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(dir);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Could not open " + dir, ex);
        }
    }
}
