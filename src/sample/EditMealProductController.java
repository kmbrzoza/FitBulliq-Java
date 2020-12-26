package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditMealProductController {
    Service service;
    Meal meal;
    Product productToEdit;

    @FXML
    TextField textFieldGrams;
    @FXML
    Button buttonEdit;

    public void initData(Service service, Meal meal, Product productToEdit){
        this.service = service;
        this.meal = meal;
        this.productToEdit = productToEdit;
    }

    public void buttonEditPressed(){
        int grams = Integer.parseInt(textFieldGrams.getText());
        service.EditMealProduct(meal, productToEdit, grams);

        Stage stage = (Stage) buttonEdit.getScene().getWindow();
        stage.close();
    }

    public void textFieldGramsChanged(){
        try{
            int grams = Integer.parseInt(textFieldGrams.getText());

        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość gram");
            alert.setHeaderText("Musisz wpisać liczbę całkowitą dodatnią!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
            textFieldGrams.setText("" + 0);
        }
    }

}
