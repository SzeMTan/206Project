package tatai.view.statistics;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tatai.model.Stats;
import tatai.model.levels.LevelSelection;

/**
 * This controller is associated with the Statistics fxml. It contains and loads a bar chart for the last 10 games played.
 * It also shows the current high score and average score for each level which can be toggled through a tab bar
 * 
 * @author stan557
 *
 */
public class StatisticsController {
	@FXML private Scene menuScene;
	@FXML private Stage window;

	//Max values for all the levels
	@FXML private Label easyMax;
	@FXML private Label mediumMax;
	@FXML private Label hardMax;
	@FXML private Label customMax;

	//Average values for all the levels
	@FXML private Label easyAverage;
	@FXML private Label mediumAverage;
	@FXML private Label hardAverage;
	@FXML private Label customAverage;

	//bar charts for all the levels
	@FXML private BarChart<String, Integer> _easyChart;
	@FXML private BarChart<String, Integer> _mediumChart;
	@FXML private BarChart<String, Integer> _hardChart;
	@FXML private BarChart<String, Integer> _customChart;

	//y-axis for all the levels
	@FXML private NumberAxis yEasyAxis;
	@FXML private NumberAxis yMediumAxis;
	@FXML private NumberAxis yHardAxis;
	@FXML private NumberAxis yCustomAxis;

	//help components
	@FXML private Button _helpOne;
	@FXML private Button _helpTwo;
	@FXML private Button _helpThree;
	@FXML private Button _helpFour;
	@FXML private ImageView _tahi;
	@FXML private Group _speech;
	@FXML private Button _nextBtn;
	@FXML private TextArea _instructions;
	private int _clicks = 0;

	@FXML
	private void initialize() {
		setAxisRange(yEasyAxis);
		setAxisRange(yMediumAxis);
		setAxisRange(yHardAxis);
		setAxisRange(yCustomAxis);

		//hide help
		_tahi.setVisible(false);
		_speech.setVisible(false);
	}

	//sends user back to the main menu
	public void homeClick(ActionEvent event) throws IOException{
		Parent menu = FXMLLoader.load(getClass().getResource("/tatai/view/MainMenu.fxml"));
		menuScene = new Scene(menu);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(menuScene);
		window.setTitle("T\u0101tai");
		window.show();
	}

	//Retrives the resultArrays for each of the levels and adds the statistics to each of the bar charts
	public void setStats(){	
		// Gets stats objects for each of the levels
		Stats stats = Stats.getInstance();
		addStats(stats,LevelSelection.EASY,_easyChart,easyMax,easyAverage);
		addStats(stats,LevelSelection.MEDIUM,_mediumChart,mediumMax,mediumAverage);
		addStats(stats,LevelSelection.HARD,_hardChart,hardMax,hardAverage);
		addStats(stats,LevelSelection.CUSTOM,_customChart,customMax,customAverage);
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

		//if the max or average have not been set yet, then rather than showing "-1", a "-" is displayed.
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
		//assigns the scores from the resultArray to the chart
		for (int i = 0; i < 10; i++){
			scores.getData().add(new XYChart.Data<String, Integer>(i+1 + "", scoreArray[i]));
		}

		chart.getData().add(scores);
	}

				@FXML
				private void helpClick(){
		//disable all help buttons
		_helpOne.setDisable(true);
		_helpTwo.setDisable(true);
		_helpThree.setDisable(true);
		_helpFour.setDisable(true);

		_nextBtn.setText("Next");

		_instructions.setText("Welcome to the stats page.");
		_speech.setVisible(true);
		_tahi.setVisible(true);

		_clicks = 0;
	}

	@FXML 
	private void nextClick() {
		if (_clicks == 0) {
			_instructions.setText("Here you can see your progress, average score and highest score for each level.");
			_clicks++;
		} else if (_clicks == 1) {
			_instructions.setText("The graph shows your last ten scores.");
			_clicks++;
		} else if (_clicks == 2) {
			_nextBtn.setText("Done!");
			_instructions.setText("Your most recent score will be on the right side of the graph.");
			_clicks++;
		} else if (_clicks == 3) {
			//hide help components
			_speech.setVisible(false);
			_tahi.setVisible(false);

			//reenable help buttons
			_helpOne.setDisable(false);
			_helpTwo.setDisable(false);
			_helpThree.setDisable(false);
			_helpFour.setDisable(false);
		}
	}

}
