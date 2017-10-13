package tatai.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tatai.model.LevelSelection;
import tatai.model.Stats;

public class StatisticsController implements Initializable {

	@FXML
	private Scene menuScene;
	@FXML
	private Stage window;
	@FXML
	private Label easyMin;
	@FXML
	private Label easyMax;
	@FXML
	private Label easyAverage;
	@FXML
	private BarChart<String, Integer> _easyChart;
	@FXML
	private Label hardMin;
	@FXML
	private Label hardMax;
	@FXML
	private Label hardAverage;
	@FXML
	private BarChart<String, Integer> _hardChart;
	@FXML
	private NumberAxis yAxis;

	public void backButtonClicked(ActionEvent event) throws IOException{
		Parent menu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		menuScene = new Scene(menu);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(menuScene);
		window.setTitle("Tatai");
		window.show();
	}


	public void setStats(){		
		Stats statsEasy = Stats.getEasyInstance();

		//initialise easy bar chart
		Integer[] scoreArrayEasy = statsEasy.getResultArray(LevelSelection.EASY);

		
		System.out.println("getResultArray invoked");
		
		
		if (statsEasy.getMin(LevelSelection.EASY) >= 0){
			easyMin.setText(statsEasy.getMin(LevelSelection.EASY) + "");
		}
		else{
			easyMin.setText("-");
		}
		if (statsEasy.getMax(LevelSelection.EASY) >=0){
			easyMax.setText(statsEasy.getMax(LevelSelection.EASY) + "");
		}
		else{
			easyMax.setText("-");
		}
		if (statsEasy.getAverage(LevelSelection.EASY) >=0){
			easyAverage.setText((Math.round(statsEasy.getAverage(LevelSelection.EASY)*(double)100))/(double)100 + "");
		}
		else{
			easyAverage.setText("-");
		}
		

		XYChart.Series<String,Integer> scoresEasy = new Series<String, Integer>();
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("1", scoreArrayEasy[0]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("2", scoreArrayEasy[1]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("3", scoreArrayEasy[2]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("4", scoreArrayEasy[3]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("5", scoreArrayEasy[4]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("6", scoreArrayEasy[5]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("7", scoreArrayEasy[6]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("8", scoreArrayEasy[7]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("9", scoreArrayEasy[8]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("10", scoreArrayEasy[9]));

		_easyChart.getData().add(scoresEasy);
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		yAxis.setAutoRanging(false);
	    yAxis.setLowerBound(0);
	    yAxis.setUpperBound(10);
	    yAxis.setTickUnit(1);
		
	}
}
