package tatai.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tatai.model.LevelSelection;
import tatai.model.Stats;

public class StatisticsController {

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

	public void backButtonClicked(ActionEvent event) throws IOException{
		Parent menu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		menuScene = new Scene(menu);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(menuScene);
		window.setTitle("Tatai");
		window.show();
	}


	public void setStats(){		
		Stats stats = Stats.getInstance();

		//initialise easy bar chart
		int[] scoreArrayEasy = stats.getResultArray(LevelSelection.EASY);

		XYChart.Series<String,Integer> scoresEasy = new Series<String, Integer>();
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("zero", scoreArrayEasy[0]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("one", scoreArrayEasy[1]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("two", scoreArrayEasy[2]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("three", scoreArrayEasy[3]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("four", scoreArrayEasy[4]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("five", scoreArrayEasy[5]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("six", scoreArrayEasy[6]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("seven", scoreArrayEasy[7]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("eight", scoreArrayEasy[8]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("nine", scoreArrayEasy[9]));
		scoresEasy.getData().add(new XYChart.Data<String, Integer>("ten", scoreArrayEasy[10]));

		_easyChart.getData().add(scoresEasy);

		//initialise hard bar chart
		int[] scoreArrayHard = stats.getResultArray(LevelSelection.HARD);

		XYChart.Series<String,Integer> scoresHard = new Series<String, Integer>();
		scoresHard.getData().add(new XYChart.Data<String, Integer>("zero", scoreArrayHard[0]));
		scoresHard.getData().add(new XYChart.Data<String, Integer>("one", scoreArrayHard[1]));
		scoresHard.getData().add(new XYChart.Data<String, Integer>("two", scoreArrayHard[2]));
		scoresHard.getData().add(new XYChart.Data<String, Integer>("three", scoreArrayHard[3]));
		scoresHard.getData().add(new XYChart.Data<String, Integer>("four", scoreArrayHard[4]));
		scoresHard.getData().add(new XYChart.Data<String, Integer>("five", scoreArrayHard[5]));
		scoresHard.getData().add(new XYChart.Data<String, Integer>("six", scoreArrayHard[6]));
		scoresHard.getData().add(new XYChart.Data<String, Integer>("seven", scoreArrayHard[7]));
		scoresHard.getData().add(new XYChart.Data<String, Integer>("eight", scoreArrayHard[8]));
		scoresHard.getData().add(new XYChart.Data<String, Integer>("nine", scoreArrayHard[9]));
		scoresHard.getData().add(new XYChart.Data<String, Integer>("ten", scoreArrayHard[10]));

		_hardChart.getData().add(scoresHard);

		if (stats.getMin(LevelSelection.EASY) >= 0){
			easyMin.setText(stats.getMin(LevelSelection.EASY) + "");
		}
		else{
			easyMin.setText("-");
		}
		if (stats.getMax(LevelSelection.EASY) >=0){
			easyMax.setText(stats.getMax(LevelSelection.EASY) + "");
		}
		else{
			easyMax.setText("-");
		}
		if (stats.getAverage(LevelSelection.EASY) >=0){
			easyAverage.setText((Math.round(stats.getAverage(LevelSelection.EASY)*100))/100 + "");
		}
		else{
			easyAverage.setText("-");
		}
		if (stats.getMin(LevelSelection.HARD) >=0){
			hardMin.setText(stats.getMin(LevelSelection.HARD) + "");
		}
		else{
			hardMin.setText("-");
		}
		if (stats.getMax(LevelSelection.HARD) >=0){
			hardMax.setText(stats.getMax(LevelSelection.HARD) + "");
		}
		else{
			hardMax.setText("-");
		}
		if (stats.getAverage(LevelSelection.HARD) >= 0){
			hardAverage.setText((Math.round(stats.getAverage(LevelSelection.HARD)*100))/100 + "");
		}
		else{
			hardAverage.setText("-");
		}
	}


}
