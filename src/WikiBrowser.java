import javafx.scene.Parent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;



public class WikiBrowser extends Parent {

    WebView browser = new WebView();
    WebEngine webEngine= browser.getEngine();

    public WikiBrowser(){
        webEngine.load("https://en.wikipedia.org/wiki/Warsaw");
        getChildren().add(browser);
    }

    public void load(String city) {
        webEngine.load("https://en.wikipedia.org/wiki/"+city);
    }

}
