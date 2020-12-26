package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddMealController {
    public Service service;

    @FXML
    TextField textFieldName;
    @FXML
    Button buttonAdd;

    public void initData(Service service){
        this.service = service;
    }

    public void buttonAddPressed(ActionEvent event){
        String mealName = textFieldName.getText();
        if (mealName == null || mealName == ""){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nazwa posiłku");
            alert.setHeaderText("Nazwa posiłku nie może być pusta!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        } else {
            Meal meal = new Meal(mealName, service.date);
            service.AddMeal(meal);
            Stage stage = (Stage) buttonAdd.getScene().getWindow();
            stage.close();
        }
    }
}
