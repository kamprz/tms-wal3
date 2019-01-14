package wat.semestr7.bachelor.config;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import wat.semestr7.bachelor.mvc.controller.CrawlingController;
import wat.semestr7.bachelor.mvc.controller.fx.FxMainStageController;

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
		Scene scene = new Scene(rootNode);

		FxMainStageController fxMainStageController = context.getBean("fxMainStageController", FxMainStageController.class);
		fxMainStageController.setSelectingCurrenciesScene(scene);
		fxMainStageController.setMainStage(primaryStage);

		//AllOffersView allOffersView = context.getBean("allOffersView",AllOffersView.class);
		//allOffersView.open();


		primaryStage.setScene(scene);
		primaryStage.setTitle("Wybór par walutowych");
		primaryStage.getIcons().add(new Image("/stageIcon.png"));
		primaryStage.centerOnScreen();
		primaryStage.show();

		CrawlingController controller = context.getBean("crawlingController", CrawlingController.class);
		controller.startCrawling();
	}

	@Override
	public void stop() throws Exception {
		context.close();
	}
}
