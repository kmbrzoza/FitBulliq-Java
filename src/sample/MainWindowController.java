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
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    Service service = new Service();
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox<String> comboBoxMeals;
    @FXML
    Button buttonAddMeal;
    @FXML
    Button buttonRemoveMeal;
    @FXML
    Button buttonAddProduct;
    @FXML
    Button buttonRemoveProduct;
    @FXML
    Button buttonEditProduct;
    @FXML
    ListView<String> listViewMealsProducts;
    @FXML
    Label labelDayMacros;
    @FXML
    Label labelMealMacros;

    public void initialize(URL url, ResourceBundle rb){
        LocalDate now = LocalDate.now();
        datePicker.setValue(now);
        service.date = now;
        service.initData();
        service.SetMealsByDate(now);
        service.SetProductsToMeals();
        UpdateComboBoxMeals();
        UpdateListViewMealsProducts();
        UpdateMacros();

    }

    public void UpdateComboBoxMeals(){
        comboBoxMeals.getItems().clear();
        for(Meal meal : service.currentMeals)
            comboBoxMeals.getItems().add(meal.Name);
    }

    public void UpdateListViewMealsProducts(){
        listViewMealsProducts.getItems().clear();
        int indexOfSelectedMeal = comboBoxMeals.getSelectionModel().getSelectedIndex();
        if (indexOfSelectedMeal >= 0){
            for(Product product : service.currentMeals.get(indexOfSelectedMeal).listProduct){
                listViewMealsProducts.getItems().add(product.toString());
            }
        }
    }

    public void UpdateMacros(){
        labelDayMacros.setText(service.ToStringMacrosDay());

        int indexOfSelectedMeal = comboBoxMeals.getSelectionModel().getSelectedIndex();
        if(indexOfSelectedMeal >= 0){
            labelMealMacros.setText(service.ToStringMacrosMeal(service.currentMeals.get(indexOfSelectedMeal)));
        } else {
            labelMealMacros.setText("0 kcal | 0 (g) | 0 (g) | 0 (g)");
        }
    }

    public void datePickerValueChanged(){
        service.date = datePicker.getValue();
        service.SetMealsByDate(service.date);
        service.SetProductsToMeals();
        UpdateComboBoxMeals();
        UpdateListViewMealsProducts();
        UpdateMacros();
    }

    public void comboBoxmealsChanged(){
        UpdateListViewMealsProducts();
        UpdateMacros();
    }

    public void buttonAddMealPressed(ActionEvent event) throws Exception{
        LocalDate selectedDate = datePicker.getValue();
        String date = selectedDate.getYear() + "-" + selectedDate.getMonthValue() + "-" + selectedDate.getDayOfMonth();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddMeal.fxml"));

        Stage stageAddMeal = new Stage();
        stageAddMeal.setScene(new Scene(loader.load()));
        AddMealController controller = loader.getController();
        controller.initData(service);

        stageAddMeal.setTitle("FitBulliq Java - Dodaj posilek");
        stageAddMeal.setResizable(false);
        stageAddMeal.initModality(Modality.WINDOW_MODAL);
        stageAddMeal.initOwner(((Node) event.getSource()).getScene().getWindow());
        stageAddMeal.showAndWait();

        UpdateComboBoxMeals();
        UpdateListViewMealsProducts();
        UpdateMacros();
    }

    public void buttonRemoveMealPressed(){
        int indexOfSelectedMeal = comboBoxMeals.getSelectionModel().getSelectedIndex();
        if(indexOfSelectedMeal>=0){
            service.RemoveMeal(service.currentMeals.get(indexOfSelectedMeal));
            comboBoxMeals.getItems().clear();
        }
        UpdateComboBoxMeals();
        UpdateListViewMealsProducts();
        UpdateMacros();
    }

    public void buttonAddProductPressed(ActionEvent event) throws Exception{
        int indexOfSelectedMeal = comboBoxMeals.getSelectionModel().getSelectedIndex();
        if (indexOfSelectedMeal >= 0){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));

            Stage stageAddMeal = new Stage();
            stageAddMeal.setScene(new Scene(loader.load()));
            AddProductController controller = loader.getController();
            controller.initData(service, service.currentMeals.get(indexOfSelectedMeal));

            stageAddMeal.setTitle("FitBulliq Java - Dodaj produkt");
            stageAddMeal.setResizable(false);
            stageAddMeal.initModality(Modality.WINDOW_MODAL);
            stageAddMeal.initOwner(((Node) event.getSource()).getScene().getWindow());
            stageAddMeal.showAndWait();

            UpdateListViewMealsProducts();
            UpdateMacros();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Dodawanie produktu");
            alert.setHeaderText("Musisz wybrać posiłek!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
    }

    public void buttonRemoveProductPressed(){
        int indexOfSelectedMeal = comboBoxMeals.getSelectionModel().getSelectedIndex();
        int indexOfSelectedProduct = listViewMealsProducts.getSelectionModel().getSelectedIndex();
        if(indexOfSelectedMeal >= 0){
            if (indexOfSelectedProduct >= 0){
                Meal selectedMeal = service.currentMeals.get(indexOfSelectedMeal);
                Product selectedProduct = service.currentMeals.get(indexOfSelectedMeal).listProduct.get(indexOfSelectedProduct);

                service.RemoveMealProduct(selectedMeal, selectedProduct);

                UpdateListViewMealsProducts();
                UpdateMacros();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Usuwanie produktu");
                alert.setHeaderText("Musisz wybrać produkt!");
                alert.setTitle("Błąd!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Usuwanie produktu");
            alert.setHeaderText("Musisz wybrać posiłek!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
    }

    public void buttonEditProductPressed(ActionEvent event) throws Exception {
        int indexOfSelectedMeal = comboBoxMeals.getSelectionModel().getSelectedIndex();
        int indexOfSelectedProduct = listViewMealsProducts.getSelectionModel().getSelectedIndex();
        if(indexOfSelectedMeal >= 0){
            if (indexOfSelectedProduct >= 0){
                Meal meal = service.currentMeals.get(indexOfSelectedMeal);
                Product productToEdit = service.currentMeals.get(indexOfSelectedMeal).listProduct.get(indexOfSelectedProduct);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditMealProduct.fxml"));

                Stage stageAddMeal = new Stage();
                stageAddMeal.setScene(new Scene(loader.load()));
                EditMealProductController controller = loader.getController();
                controller.initData(service, meal, productToEdit);

                stageAddMeal.setTitle("FitBulliq Java - Dodaj produkt");
                stageAddMeal.setResizable(false);
                stageAddMeal.initModality(Modality.WINDOW_MODAL);
                stageAddMeal.initOwner(((Node) event.getSource()).getScene().getWindow());
                stageAddMeal.showAndWait();

                UpdateListViewMealsProducts();
                UpdateMacros();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Edytowanie produktu");
                alert.setHeaderText("Musisz wybrać produkt!");
                alert.setTitle("Błąd!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Edytowanie produktu");
            alert.setHeaderText("Musisz wybrać posiłek!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
    }
}
