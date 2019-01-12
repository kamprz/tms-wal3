package wat.semestr7.bachelor.config;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import wat.semestr7.bachelor.mvc.controller.CrawlingController;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;
import wat.semestr7.bachelor.mvc.controller.fx.FxSceneController;
import wat.semestr7.bachelor.mvc.view.allOffers.AllOffersView;
import wat.semestr7.bachelor.mvc.view.profitable.CurrencyIndicator;
import wat.semestr7.bachelor.mvc.view.profitable.NewDataIndicator;
import wat.semestr7.bachelor.mvc.view.profitable.ProfitableOffersView;

import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = "wat.semestr7.bachelor")
public class ApplicationConfiguration extends Application {

	private ConfigurableApplicationContext context;
	private Parent rootNode;

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ApplicationConfiguration.class);
		context = builder.run(getParameters().getRaw().toArray(new String[0]));


		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/choose.fxml"));
		loader.setControllerFactory(context::getBean);
		rootNode = loader.load();
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane box = new GridPane();
		//ProfitableOffersView profitableOffersView = context.getBean("profitableOffersView", ProfitableOffersView.class);


		//box.getChildren().add(profitableOffersView);
		//box.getChildren().add(rootNode);
		Scene scene = new Scene(rootNode);
		FxSceneController fxSceneController = context.getBean("fxSceneController", FxSceneController.class);
		fxSceneController.setSelectingCurrenciesScene(scene);
		fxSceneController.setMainStage(primaryStage);
		primaryStage.setScene(scene);

		primaryStage.centerOnScreen();
		primaryStage.show();

		CrawlingController controller = context.getBean("crawlingController", CrawlingController.class);
		controller.startCrawling();
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
