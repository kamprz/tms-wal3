package wat.semestr7.bachelor.config;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import wat.semestr7.bachelor.mvc.controller.CrawlingController;
import wat.semestr7.bachelor.mvc.view.allOffers.AllOffersView;

@SpringBootApplication
@ComponentScan(basePackages = "wat.semestr7.bachelor")
public class ApplicationConfiguration extends Application {

	private ConfigurableApplicationContext context;
	private Parent rootNode;

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ApplicationConfiguration.class);
		context = builder.run(getParameters().getRaw().toArray(new String[0]));
		CrawlingController controller = context.getBean("crawlingController", CrawlingController.class);
		controller.startCrawling();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/choose.fxml"));
		loader.setControllerFactory(context::getBean);
		rootNode = loader.load();
	}


	@Override
	public void start(Stage primaryStage) throws Exception {


		primaryStage.setScene(new Scene(rootNode));
		primaryStage.centerOnScreen();
		primaryStage.show();

		//Stage primaryStage = new Stage();
		/*Thread.sleep(3000);
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
		primaryStage.show();*/
	}

	@Override
	public void stop() throws Exception {
		context.close();
	}
}
