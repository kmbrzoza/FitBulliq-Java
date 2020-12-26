package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EditProductController {
    Service service;
    Product productSelected;

    @FXML
    TextField textFieldName;
    @FXML
    TextField textFieldKcal;
    @FXML
    TextField textFieldProteins;
    @FXML
    TextField textFieldFats;
    @FXML
    TextField textFieldCarbohydrates;
    @FXML
    Button buttonEdit;

    public void initData(Service service, Product productSelected){
        this.service = service;
        this.productSelected = productSelected;
        textFieldName.setText(productSelected.Name);
        textFieldKcal.setText("" + productSelected.Kcal);
        textFieldProteins.setText("" + productSelected.Protein);
        textFieldFats.setText("" + productSelected.Fats);
        textFieldCarbohydrates.setText("" + productSelected.Carbohydrates);
    }

    public void buttonEditPressed() {
        String nameProduct = textFieldName.getText();
        if(nameProduct == null || nameProduct == ""){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nazwa produktu");
            alert.setHeaderText("Musisz podać nazwę produktu!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        } else {
            int kcal = Integer.parseInt(textFieldKcal.getText());
            double proteins = getDoubleFromTextField(textFieldProteins.getText());
            double fats = getDoubleFromTextField(textFieldFats.getText());
            double carbohydrates = getDoubleFromTextField(textFieldCarbohydrates.getText());

            Product productEdited = new Product(nameProduct, kcal, proteins, fats, carbohydrates);
            service.EditProduct(productSelected, productEdited);

            Stage stage = (Stage) buttonEdit.getScene().getWindow();
            stage.close();
        }
    }

    public void textFieldKcalChanged(){
        try{
            int kcal = Integer.parseInt(textFieldKcal.getText());
            if (kcal<0){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość kcal");
                alert.setHeaderText("Musisz wpisać liczbę dodatnią całkowitą do Kcal!");
                alert.setTitle("Błąd!");
                alert.showAndWait();
                textFieldKcal.setText("" + productSelected.Kcal);
            }
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość kcal");
            alert.setHeaderText("Musisz wpisać liczbę dodatnią całkowitą do Kcal!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
            textFieldKcal.setText("" + productSelected.Kcal);
        }
    }

    public void textFieldProteinsChanged(){
        try{
            double proteins = Double.parseDouble(textFieldProteins.getText());
            if (proteins < 0){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość białka");
                alert.setHeaderText("Musisz wpisać liczbę rzeczywistą dodatnią do ilości białka!");
                alert.setTitle("Błąd!");
                alert.showAndWait();
                textFieldProteins.setText("" + productSelected.Protein);
            }
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość białka");
            alert.setHeaderText("Musisz wpisać liczbę rzeczywistą do ilości białka!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
            textFieldProteins.setText("" + productSelected.Protein);
        }
    }

    public void textFieldFatsChanged(){
        try{
            double fats = Double.parseDouble(textFieldFats.getText());
            if (fats < 0){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość tłuszczy");
                alert.setHeaderText("Musisz wpisać liczbę rzeczywistą dodatnią do ilości tłuszczy!");
                alert.setTitle("Błąd!");
                alert.showAndWait();
                textFieldFats.setText("" + productSelected.Fats);
            }
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość tłuszczy");
            alert.setHeaderText("Musisz wpisać liczbę rzeczywistą do ilości tłuszczy!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
            textFieldFats.setText("" + productSelected.Fats);
        }
    }

    public void textFieldCarbohydratesChanged(){
        try{
            double carbohydrates = Double.parseDouble(textFieldCarbohydrates.getText());
            if (carbohydrates < 0){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość węglowodanów");
                alert.setHeaderText("Musisz wpisać liczbę rzeczywistą dodatnią do ilości węglowodanów!");
                alert.setTitle("Błąd!");
                alert.showAndWait();
                textFieldCarbohydrates.setText("" + productSelected.Carbohydrates);
            }
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość węglowodanów");
            alert.setHeaderText("Musisz wpisać liczbę rzeczywistą do ilości węglowodanów!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
            textFieldCarbohydrates.setText("" + productSelected.Carbohydrates);
        }
    }

    private double getDoubleFromTextField(String textField){
        double value = Double.parseDouble(textField);
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
