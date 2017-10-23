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
	private Label easyMax;
	@FXML
	private Label easyAverage;
	@FXML
	private Label mediumMax;
	@FXML
	private Label mediumAverage;
	@FXML
	private Label hardMax;
	@FXML
	private Label hardAverage;
	@FXML
	private Label customMax;
	@FXML
	private Label customAverage;
	@FXML
	private BarChart<String, Integer> _easyChart;
	@FXML
	private BarChart<String, Integer> _mediumChart;
	@FXML
	private BarChart<String, Integer> _hardChart;
	@FXML
	private BarChart<String, Integer> _customChart;
	
	@FXML
	private NumberAxis yEasyAxis;
	@FXML
	private NumberAxis yMediumAxis;
	@FXML
	private NumberAxis yHardAxis;
	@FXML
	private NumberAxis yCustomAxis;

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
		Stats stats = Stats.getInstance();
		addStats(stats,LevelSelection.EASY,_easyChart,easyMax,easyAverage);
		addStats(stats,LevelSelection.MEDIUM,_mediumChart,mediumMax,mediumAverage);
		addStats(stats,LevelSelection.HARD,_hardChart,hardMax,hardAverage);
		addStats(stats,LevelSelection.CUSTOM,_customChart,customMax,customAverage);
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setAxisRange(yEasyAxis);
		setAxisRange(yMediumAxis);
		setAxisRange(yHardAxis);
		setAxisRange(yCustomAxis);
		
	}
	
	//This method changed the auto ranging, upper and lower bound and the division unit for the specified axis
	private void setAxisRange(NumberAxis axis){
		axis.setAutoRanging(false);
	    axis.setLowerBound(0);
	    axis.setUpperBound(10);
	    axis.setTickUnit(1);
	}
	
	private void addStats(Stats stats, LevelSelection level, BarChart<String, Integer> chart, Label maxLabel, Label avLabel){
		//load all the information for the specified parameters
				Integer[] scoreArray = stats.getResultArray(level);
				
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
