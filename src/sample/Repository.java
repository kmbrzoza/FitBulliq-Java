package sample;

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
            System.out.println("Nie polaczono z baza danych");
        }
    }
    private void CloseConnection(){
        try{
            connection.close();
            System.out.println("Zakonczono polaczenie z baza danych");

        } catch (Exception ex){
            System.out.println("Nie zakonczono polaczenia z baza danych");
        }
    }

    public void insertMeal(){
        Connect();
        try{
            Statement stmt = connection.createStatement();
            String query = "INSERT INTO meals(name, date) VALUES ('ciasto',2020-12-23)";
            int a = stmt.executeUpdate(query);
            int b = -1;
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                b = rs.getInt(1);
            rs.close();


            System.out.println(a + " ---- " + b);
            stmt.close();
        }catch (Exception ex){
            System.out.println("Nie udalo sie insertowac");
        }
        CloseConnection();
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
            System.out.println("Blad tworzenia tabel");
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
            System.out.println("Blad pobierania posilkow po dacie");
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
                    System.out.println("Błąd w odczycie informacji o danym produkcie! Repo - GetProductsToMeal()");
                }
                readerForProductSpecs.close();
                stmt2.close();
            }
            rs.close();
            stmt.close();

        } catch(Exception ex){
            System.out.println("Blad pobierania produktow do posilku");
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
            System.out.println("Blad dodawania meala");
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
            System.out.println("Blad usuwania meala");
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
            System.out.println("Blad dodawania produktu");
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
            System.out.println("Blad GetProductsByText(text)");
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
            System.out.println("Blad RemoveProduct()");
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
            System.out.println("Blad EditProduct()");
        }
        CloseConnection();
    }


    //MEALSPRODUCTS
    public void AddMealproduct(Meal meal, Product product){
        Connect();

        String query = "INSERT INTO mealsProducts (idMeal, idProduct, grams) VALUES ("+meal.Id+", "+product.Id+", "+product.Grams+")";
        try{
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch(Exception ex){
            System.out.println("Blad AddMealproduct()");
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
            System.out.println("Blad RemoveMealProduct()");
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
            System.out.println("Blad EditMealProduct()");
        }
        CloseConnection();
    }


}
