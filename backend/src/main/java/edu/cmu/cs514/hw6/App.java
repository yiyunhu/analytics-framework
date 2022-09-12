
package edu.cmu.cs514.hw6;

import com.github.jknack.handlebars.Template;
import edu.cmu.cs514.hw6.framework.SchoolFrameworkImpl;
import edu.cmu.cs514.hw6.plugin.DataPlugin;
import edu.cmu.cs514.hw6.plugin.VisualPlugin;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private SchoolFrameworkImpl schoolHunt;
    private List<DataPlugin> dataPlugins;
    private List<VisualPlugin> visualPlugins;


    public App() throws IOException {
        super(8080);

        this.schoolHunt = new SchoolFrameworkImpl();
        dataPlugins = loadDataPlugins();
        visualPlugins = loadVisualPlugins();
        for (DataPlugin p : dataPlugins) {
            schoolHunt.registerDataPlugin(p);
        }
        for (VisualPlugin p : visualPlugins) {
            schoolHunt.registerVisualPlugin(p);
        }

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();
        if (uri.equals("/search")) {
            schoolHunt.search(params.get("text"));
            System.out.println(params.get("text"));
            schoolHunt.sentimentAnalyze();
        }

        JSONObject display = schoolHunt.visualize();
        return newFixedLengthResponse(display.toString());
    }


    /**
     * Load data plugins listed in META-INF/services/...
     *
     * @return List of instantiated plugins
     */
    private static List<DataPlugin> loadDataPlugins() {
        ServiceLoader<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class);
        List<DataPlugin> result = new ArrayList<>();
        for (DataPlugin plugin : plugins) {
            result.add(plugin);
        }
        return result;
    }

    /**
     * Load visualization plugins listed in META-INF/services/...
     *
     * @return List of instantiated plugins
     */
    private static List<VisualPlugin> loadVisualPlugins() {
        ServiceLoader<VisualPlugin> plugins = ServiceLoader.load(VisualPlugin.class);
        List<VisualPlugin> result = new ArrayList<>();
        for (VisualPlugin plugin : plugins) {
            result.add(plugin);
        }
        return result;
    }
}
