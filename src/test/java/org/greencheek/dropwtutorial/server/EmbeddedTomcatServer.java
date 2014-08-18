package org.greencheek.dropwtutorial.server;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.deploy.FilterDef;
import org.apache.catalina.deploy.FilterMap;
import org.apache.catalina.startup.Tomcat;
import org.apache.naming.resources.VirtualDirContext;

import javax.servlet.Servlet;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class EmbeddedTomcatServer {
    private final static String mWorkingDir = System.getProperty("java.io.tmpdir");
    private final static File  docBase = new File(mWorkingDir);

    private volatile Tomcat tomcat;
    private String contextName;
    private Context ctx;

    public final void shutdownTomcat()  {
        try {
            if (tomcat != null && tomcat.getServer() != null
                    && tomcat.getServer().getState() != LifecycleState.DESTROYED) {
                if (tomcat.getServer().getState() != LifecycleState.STOPPED) {
                    tomcat.stop();
                }
                tomcat.destroy();
                tomcat.getServer().await();
            }
        } catch (Exception e) {

        }
    }

    protected int getTomcatPort() {
        return tomcat.getConnector().getLocalPort();
    }

    public String setupServlet(String url, String name, Servlet servlet, boolean async) {
        Wrapper wrapper = tomcat.addServlet(ctx, name, servlet);
        return setupServlet(wrapper,async,url);
    }

    public String setupServlet(String url, String name, String className, boolean async) {
        Wrapper wrapper = tomcat.addServlet(ctx, name, className);
        return setupServlet(wrapper,async,url);
    }

    private String setupServlet(Wrapper wrapper,boolean async, String url) {
        wrapper.setAsyncSupported(async);
        wrapper.addMapping(url);

        String httpUrl =  "http://localhost:{PORT}";
        if(contextName.startsWith("/")) {
            httpUrl += contextName;
        } else {
            httpUrl += "/" + contextName;
        }

        if(url.startsWith("/")) {
            httpUrl += url;
        } else {
            httpUrl += "/" + url;

        }
        httpUrl = httpUrl.replaceAll("^(.*)/\\*$","$1");
        return httpUrl;
    }

    public String replacePort(String url) {
        return url.replace("{PORT}",""+getTomcatPort());
    }

    public void setupTomcat(String context) {
        this.contextName = context;

        tomcat = new Tomcat();
        tomcat.setPort(0);
        tomcat.setBaseDir(mWorkingDir);
        tomcat.getHost().setAppBase(mWorkingDir);
        tomcat.getHost().setAutoDeploy(true);

        ctx = tomcat.addContext(tomcat.getHost(),context,contextName.replace("/",""),docBase.getAbsolutePath());

        ctx.setCrossContext(true);
        ctx.setPath(contextName);
        ((StandardContext)ctx).setProcessTlds(false);  // disable tld processing.. we don't use any
        ctx.addParameter("com.sun.faces.forceLoadConfiguration","false");

        //declare an alternate location for your "WEB-INF/classes" dir:
        File additionWebInfClasses = new File("target/classes");
        VirtualDirContext resources = new VirtualDirContext();
        resources.setExtraResourcePaths("/WEB-INF/classes=" + additionWebInfClasses);
        ctx.setResources(resources);
    }

    public boolean startTomcat() {
        try {
            tomcat.start();
            return true;
        } catch (LifecycleException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
    }
}