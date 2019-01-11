package wat.semestr7.bachelor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import wat.semestr7.bachelor.config.AppConfig;
import wat.semestr7.bachelor.mvc.controller.CrawlingController;
import wat.semestr7.bachelor.mvc.view.allOffers.AllOffersView;


public class Main extends Application {
    static ApplicationContext context;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        CrawlingController controller = context.getBean("crawlingController", CrawlingController.class);
        controller.startCrawling();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.sleep(2000);
        primaryStage.setTitle("Hello World");
        System.out.println("debug");
        AllOffersView view = context.getBean("allOffersView", AllOffersView.class);

        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(view);

        //pane.setMinWidth(940);
        //pane.setMinHeight(230);
        primaryStage.setMinWidth(950);
        primaryStage.setHeight(210);
        primaryStage.setScene(new Scene(pane, 300, 275));
        primaryStage.setMaximized(true);
        // primaryStage.setFullScreen(true);
        primaryStage.show();


        /*
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/choose.fxml"));

        Stage secondStage = new Stage();
        AnchorPane pane2 = loader.load();
        Scene scene = new Scene(pane2);
        secondStage.setScene(scene);
        secondStage.show();
        */
    }
}
