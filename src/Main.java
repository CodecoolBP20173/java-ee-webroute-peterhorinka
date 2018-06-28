import framework.WebFramework;

public class Main {
    public static void main(String[] args) {
        WebFramework framework = new WebFramework(new Class[] {MyRoutes.class});
        framework.startServer();
    }
}
