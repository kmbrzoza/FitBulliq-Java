package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    Service service;
    Meal selectedMeal;

    @FXML
    ListView<String> listViewProducts;
    @FXML
    TextField textFieldSearch;
    @FXML
    TextField textFieldGrams;
    @FXML
    Label labelKcal;
    @FXML
    Label labelProteins;
    @FXML
    Label labelFats;
    @FXML
    Label labelCarbohydrates;
    @FXML
    Button buttonAdd;
    @FXML
    Button buttonAddOwnProduct;
    @FXML
    Button buttonRemove;
    @FXML
    Button buttonEdit;

    public void initialize(URL url, ResourceBundle rb){

    }

    public void initData(Service service, Meal selectedMeal){
        this.service = service;
        this.selectedMeal = selectedMeal;
        textFieldGrams.setText("" + 0);
    }

    public void UpdateListViewOfProducts(){
        listViewProducts.getItems().clear();
        String text = textFieldSearch.getText();
        service.SetListProductsByText(text);
        for(int i=0; i< service.listProducts.size(); i++) {
            listViewProducts.getItems().add(service.listProducts.get(i).ToStringWithoutGrams());
        }
    }

    public void textFieldSearchChanged(){
        UpdateListViewOfProducts();
    }

    public void textFieldGramsChanged(){
        int indexOfSelectedProduct = listViewProducts.getSelectionModel().getSelectedIndex();
        if(indexOfSelectedProduct >= 0){
            try{
                int grams = Integer.parseInt(textFieldGrams.getText());
                if (grams >= 0) {
                    BigDecimal bd;
                    Product selectedProduct = service.listProducts.get(indexOfSelectedProduct);

                    bd = new BigDecimal(selectedProduct.Kcal * (grams * 0.01));
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    labelKcal.setText("" + bd.doubleValue());

                    bd = new BigDecimal(selectedProduct.Protein * (grams * 0.01));
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    labelProteins.setText("" + bd.doubleValue());

                    bd = new BigDecimal(selectedProduct.Fats * (grams * 0.01));
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    labelFats.setText("" + bd.doubleValue());

                    bd = new BigDecimal(selectedProduct.Carbohydrates * (grams * 0.01));
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    labelCarbohydrates.setText("" + bd.doubleValue());
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość gram");
                    alert.setHeaderText("Musisz wpisać liczbę całkowitą dodatnią!");
                    alert.setTitle("Błąd!");
                    alert.showAndWait();
                    textFieldGrams.setText("" + 0);
                    labelKcal.setText("" + 0);
                    labelProteins.setText("" + 0.0);
                    labelFats.setText("" + 0.0);
                    labelCarbohydrates.setText("" + 0.0);
                }
            } catch (Exception ex){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość gram");
                alert.setHeaderText("Musisz wpisać liczbę całkowitą dodatnią!");
                alert.setTitle("Błąd!");
                alert.showAndWait();
                textFieldGrams.setText("" + 0);
                labelKcal.setText("" + 0);
                labelProteins.setText("" + 0.0);
                labelFats.setText("" + 0.0);
                labelCarbohydrates.setText("" + 0.0);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość gram");
            alert.setHeaderText("Musisz wybrać produkt aby wpisać ilość!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
            textFieldGrams.setText("" + 0);
            labelKcal.setText("" + 0);
            labelProteins.setText("" + 0.0);
            labelFats.setText("" + 0.0);
            labelCarbohydrates.setText("" + 0.0);
        }
    }

    public void buttonAddPressed() {
        int indexOfSelectedProduct = listViewProducts.getSelectionModel().getSelectedIndex();
        if (indexOfSelectedProduct >= 0) {
            Product selectedProduct = service.listProducts.get(indexOfSelectedProduct);
            int grams = Integer.parseInt(textFieldGrams.getText());
            service.AddMealProduct(selectedMeal, selectedProduct, grams);

            Stage stage = (Stage) buttonAdd.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Produkt");
            alert.setHeaderText("Musisz wybrać produkt aby go dodać!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
    }

    public void buttonAddOwnProductPressed(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddOwnProduct.fxml"));

        Stage stageAddMeal = new Stage();
        stageAddMeal.setScene(new Scene(loader.load()));
        AddOwnProductController controller = loader.getController();
        controller.initData(service);

        stageAddMeal.setTitle("FitBulliq Java - Dodaj własny produkt");
        stageAddMeal.setResizable(false);
        stageAddMeal.initModality(Modality.WINDOW_MODAL);
        stageAddMeal.initOwner(((Node) event.getSource()).getScene().getWindow());
        stageAddMeal.showAndWait();

        UpdateListViewOfProducts();
    }

    public void buttonRemovePressed(){
        int indexOfSelectedProduct = listViewProducts.getSelectionModel().getSelectedIndex();
        if (indexOfSelectedProduct >= 0){
            Product productToRemove = service.listProducts.get(indexOfSelectedProduct);
            service.RemoveProduct(productToRemove);

            UpdateListViewOfProducts();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Posiłek");
            alert.setHeaderText("Musisz wybrać posiłek aby go usunąć!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
    }

    public void buttonEditPressed(ActionEvent event) throws Exception{
        int indexOfSelectedProduct = listViewProducts.getSelectionModel().getSelectedIndex();
        if (indexOfSelectedProduct >= 0) {
            Product productSelected = service.listProducts.get(indexOfSelectedProduct);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditProduct.fxml"));

            Stage stageAddMeal = new Stage();
            stageAddMeal.setScene(new Scene(loader.load()));
            EditProductController controller = loader.getController();
            controller.initData(service, productSelected);

            stageAddMeal.setTitle("FitBulliq Java - Edytuj produkt");
            stageAddMeal.setResizable(false);
            stageAddMeal.initModality(Modality.WINDOW_MODAL);
            stageAddMeal.initOwner(((Node) event.getSource()).getScene().getWindow());
            stageAddMeal.showAndWait();

            UpdateListViewOfProducts();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ilość gram");
            alert.setHeaderText("Musisz wybrać posiłek aby wpisać ilość!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
    }
}
