package sample;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class Repository {
    private Connection connection;

    private void Connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Polaczono z baza danych");

        } catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Database");
            alert.setHeaderText("Błąd polaczenia z baza danych!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
    }
    private void CloseConnection(){
        try{
            connection.close();
            System.out.println("Zakonczono polaczenie z baza danych");

        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Database");
            alert.setHeaderText("Błąd zamykania polaczenia z baza danych!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
    }

    public void createTablesIfNotExist(){
        Connect();
        try{
            Statement stmt = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS meals(id integer unique primary key autoincrement, name text, date date)";
            stmt.executeUpdate(query);
            stmt.close();

            query = "CREATE TABLE IF NOT EXISTS products(id integer unique primary key autoincrement, name text, " +
            "kcal int, protein numeric(6,2), fats numeric(6,2), carbohydrates numeric(6,2) )";
            stmt.executeUpdate(query);
            stmt.close();

            query = "CREATE TABLE IF NOT EXISTS mealsProducts(id integer unique primary key autoincrement, " +
            "idMeal integer REFERENCES meals(id), idProduct integer REFERENCES products(id), grams int)";
            stmt.executeUpdate(query);
            stmt.close();
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - createTablesIfNotExist()");
            alert.setHeaderText("Błąd tworzenia tabel w bazie danych!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
        CloseConnection();
    }

    // MEALS
    public ArrayList<Meal> GetMealsByDate(LocalDate date){
        ArrayList<Meal> listMeals = new ArrayList<>();
        Connect();

        String strDate = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
        String query = "SELECT id, name, date FROM meals WHERE date=" + strDate;
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Meal meal = new Meal(id, name, date);
                listMeals.add(meal);
            }

            rs.close();
            stmt.close();
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - GetMealsByDate()");
            alert.setHeaderText("Błąd odczytu posiłków z bazy danych!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }

        CloseConnection();
        return listMeals;
    }

    public ArrayList<Product> GetProductsToMeal(Meal meal){
        ArrayList<Product> listProduct = new ArrayList<>();
        Connect();

        String queryForIdProduct = "SELECT mp.idProduct, mp.grams FROM mealsProducts mp INNER JOIN" +
                " products p on mp.idProduct = p.id INNER JOIN meals m on mp.idMeal = m.id WHERE m.id = " + meal.Id;

        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(queryForIdProduct);

            while(rs.next()){
                int id = rs.getInt("idProduct");
                int grams = rs.getInt("grams");

                String queryFOrProductSpecs = "SELECT name, kcal, protein, fats, carbohydrates FROM products WHERE id=" + id;
                Statement stmt2 = connection.createStatement();
                ResultSet readerForProductSpecs = stmt2.executeQuery(queryFOrProductSpecs);

                if(readerForProductSpecs.next()){
                    String name = readerForProductSpecs.getString("name");
                    int kcal = readerForProductSpecs.getInt("kcal");
                    double protein = readerForProductSpecs.getDouble("protein");
                    double fats = readerForProductSpecs.getDouble("fats");
                    double carbohydrates = readerForProductSpecs.getDouble("carbohydrates");

                    listProduct.add(new Product(id, name, kcal, protein, fats, carbohydrates, grams));
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - GetProductsToMeal()");
                    alert.setHeaderText("Błąd w odczycie informacji o danym produkcie!");
                    alert.setTitle("Błąd!");
                    alert.showAndWait();
                }
                readerForProductSpecs.close();
                stmt2.close();
            }
            rs.close();
            stmt.close();

        } catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - GetProductsToMeal()");
            alert.setHeaderText("Błąd odczytu produktów z bazy danych!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }

        CloseConnection();
        return listProduct;
    }

    public int AddMeal(Meal meal){
        Connect();
        String dateMeal = meal.Date.getYear() + "-" + meal.Date.getMonthValue() + "-" + meal.Date.getDayOfMonth();
        int id = -1;
        String query = "INSERT INTO meals (name, date) VALUES ('" + meal.Name + "', "+ dateMeal +")";
        try{
            Statement stmt = connection.createStatement();

            stmt.executeUpdate(query);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                id = rs.getInt(1);

            rs.close();
            stmt.close();
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - AddMeal()");
            alert.setHeaderText("Bład dodawania posiłku!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
        CloseConnection();
        return id;
    }

    public void RemoveMeal(Meal meal){
        Connect();
        String queryDeleteMealsProducts = "DELETE FROM MealsProducts WHERE IdMeal = " + meal.Id;
        String queryDeleteMeals = "DELETE FROM Meals where Id = " + meal.Id;
        try{
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(queryDeleteMealsProducts);
            stmt.close();

            stmt.executeUpdate(queryDeleteMeals);
            stmt.close();
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - RemoveMeal()");
            alert.setHeaderText("Błąd usuwania posiłku!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }

        CloseConnection();
    }


    // PRODUCTS
    public void AddProduct(Product product) {
        Connect();
        String query = "INSERT INTO Products (name, kcal, protein, fats, carbohydrates) " +
                "VALUES ('"+ product.Name +"', " + product.Kcal + ", "+ product.Protein +", "+ product.Fats +", " + product.Carbohydrates +")";
        try{
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - AddProduct()");
            alert.setHeaderText("Błąd dodawania produktu!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
        CloseConnection();
    }

    public ArrayList<Product> GetProductsByText(String text){
        ArrayList<Product> listProducts = new ArrayList<>();
        Connect();
        String query = "SELECT id, name, kcal, protein, fats, carbohydrates FROM products WHERE NAME LIKE '"+ text +"'";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int kcal = rs.getInt("kcal");
                double protein = rs.getDouble("protein");
                double fats = rs.getDouble("fats");
                double carbohydrates = rs.getDouble("carbohydrates");

                listProducts.add(new Product(id, name, kcal, protein, fats, carbohydrates));
            }
            rs.close();
            stmt.close();
        }catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - GetProductsByText()");
            alert.setHeaderText("Błąd odczytu produktów po nazwie!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
        CloseConnection();
        return listProducts;
    }

    public void RemoveProduct(Product product){
        Connect();
        String queryDeleteFromMealsProducts = "DELETE FROM MealsProducts WHERE IdProduct="+ product.Id;
        String queryDeleteFromProducts = "DELETE FROM Products WHERE Id=" + product.Id;

        try{
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(queryDeleteFromMealsProducts);
            stmt.close();

            stmt.executeUpdate(queryDeleteFromProducts);
            stmt.close();
        } catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - RemoveProduct()");
            alert.setHeaderText("Błąd usuwania produktu!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }

        CloseConnection();
    }

    public void EditProduct(Product product, Product productEdited){
        Connect();

        String query = "UPDATE products SET name="+ productEdited.Name +", kcal="+productEdited.Kcal+", protein="+productEdited.Protein+", fats="+productEdited.Fats+"," +
                "carbohydrates="+productEdited.Carbohydrates+" WHERE Id="+ product.Id;
        try{
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - EditProduct()");
            alert.setHeaderText("Błąd edytowania produktu!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
        CloseConnection();
    }


    //MEALSPRODUCTS
    public void AddMealProduct(Meal meal, Product product){
        Connect();

        String query = "INSERT INTO mealsProducts (idMeal, idProduct, grams) VALUES ("+meal.Id+", "+product.Id+", "+product.Grams+")";
        try{
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - AddMealProduct()");
            alert.setHeaderText("Błąd dodawania produktu do posiłku!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }

        CloseConnection();
    }

    public void RemoveMealProduct(Meal meal, Product productToRemove){
        Connect();
        String query = "DELETE FROM mealsProducts WHERE idMeal="+meal.Id+" AND idProduct="+productToRemove.Id+" AND grams=" + productToRemove.Grams;
        try{
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - RemoveMealProduct()");
            alert.setHeaderText("Błąd usuwania produktu z posiłku!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
        CloseConnection();
    }

    public void EditMealProduct(Meal meal, Product productToEdit, int gramsToEdit){
        Connect();
        String query = "UPDATE mealsProducts SET grams="+gramsToEdit+" WHERE idMeal="+meal.Id+" AND idProduct="+productToEdit.Id+" AND grams="+productToEdit.Grams;
        try{
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Repo - EditMealProduct()");
            alert.setHeaderText("Błąd edytowania produktu z posiłku!");
            alert.setTitle("Błąd!");
            alert.showAndWait();
        }
        CloseConnection();
    }


}
