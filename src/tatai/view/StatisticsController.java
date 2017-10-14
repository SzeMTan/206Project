package tatai.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

	//private List<Stats,LevelSelection> statsList = new ArrayList<Stats,LevelSelection>();
	
	
	public void backButtonClicked(ActionEvent event) throws IOException{
		Parent menu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		menuScene = new Scene(menu);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(menuScene);
		window.setTitle("Tatai");
		window.show();
	}


	public void setStats(){	
		
		// Gets stats objects for each of the levels
		Stats statsEasy = Stats.getEasyInstance();
		addStats(statsEasy,LevelSelection.EASY,_easyChart,easyMin,easyMax,easyAverage);
////		Stats statsMedium = Stats.getMediumInstance();
////		addStats(statsMedium,LevelSelection.MEDIUM,_easyChart,easyMin,easyMax,easyAverage);
//		Stats statsHard = Stats.getHardInstance();
//		addStats(statsHard,LevelSelection.HARD,_hardChart,hardMin,hardMax,hardAverage);
////		Stats statsCustom = Stats.getCustomInstance();
////		addStats(statsEasy,LevelSelection.EASY,_easyChart,easyMin,easyMax,easyAverage);
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		yAxis.setAutoRanging(false);
	    yAxis.setLowerBound(0);
	    yAxis.setUpperBound(10);
	    yAxis.setTickUnit(1);
		
	}
	
	private void addStats(Stats stats, LevelSelection level, BarChart<String, Integer> chart, Label minLabel, Label maxLabel, Label avLabel){
		//initialise easy bar chart
				Integer[] scoreArray = stats.getResultArray(level);
				
				if (stats.getMin(level) >= 0){
					minLabel.setText(stats.getMin(level) + "");
				}
				else{
					minLabel.setText("-");
				}
				if (stats.getMax(level) >=0){
					maxLabel.setText(stats.getMax(level) + "");
				}
				else{
					maxLabel.setText("-");
				}
				if (stats.getAverage(level) >=0){
					avLabel.setText((Math.round(stats.getAverage(level)*(double)100))/(double)100 + "");
				}
				else{
					avLabel.setText("-");
				}
				

				XYChart.Series<String,Integer> scores = new Series<String, Integer>();
				for (int i = 0; i < 10; i++){
					scores.getData().add(new XYChart.Data<String, Integer>(i+1 + "", scoreArray[i]));
				}

				chart.getData().add(scores);
			}
	
	
}
