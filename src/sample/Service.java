package sample;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;

public class Service {
    private Repository repo = new Repository();

    public ArrayList<Meal> currentMeals = new ArrayList<>();
    public ArrayList<Product> listProducts = new ArrayList<>();
    public LocalDate date;

    public void initData(){
        repo.createTablesIfNotExist();
    }

    public void SetMealsByDate(LocalDate date){
        currentMeals = repo.GetMealsByDate(date);
    }

    public void SetProductsToMeals(){
        RemoveProductsFromCurrentMeals();
        for(Meal meal : currentMeals){
            meal.listProduct = repo.GetProductsToMeal(meal);
        }
    }

    public void AddMeal(Meal meal){
        meal.Id = repo.AddMeal(meal);
        currentMeals.add(meal);
    }

    public void RemoveMeal(Meal meal){
        repo.RemoveMeal(meal);
        for (int i = 0; i < currentMeals.size(); i++)
        {
            if(meal.equals(currentMeals.get(i)))
            {
                currentMeals.remove(i);
                break;
            }
        }
        SetMealsByDate(date);
        SetProductsToMeals();
    }

    public void SetListProductsByText(String text){
        listProducts = repo.GetProductsByText(text);
    }

    public void AddProduct(Product product){
        repo.AddProduct(product);
    }

    public void RemoveProduct(Product product){
        repo.RemoveProduct(product);
        SetProductsToMeals();
    }

    public void EditProduct(Product product, Product productToEdit){
        repo.EditProduct(product, productToEdit);
        SetProductsToMeals();
    }

    public void AddMealProduct(Meal meal, Product product, int grams){
        product.Grams = grams;
        repo.AddMealProduct(meal, product);
        for(Meal m : currentMeals){
            if(m.equals(meal)){
                m.listProduct.add(product);
                break;
            }
        }
    }

    public void RemoveMealProduct(Meal meal, Product product){
        repo.RemoveMealProduct(meal, product);
        for(Meal m : currentMeals){
            if(m.equals(meal)){
                for(int i=0; i < m.listProduct.size(); i++){
                    if(m.listProduct.get(i).equals(product)){
                        m.listProduct.remove(i);
                        break;
                    }
                }
                break;
            }
        }
    }

    public void EditMealProduct(Meal meal, Product productToEdit, int gramsToEdit){
        repo.EditMealProduct(meal, productToEdit, gramsToEdit);
        for(Meal m : currentMeals){
            if(m.equals(meal)){
                for(int i=0; i < m.listProduct.size(); i++){
                    if(m.listProduct.get(i).equals(productToEdit)){
                        m.listProduct.get(i).Grams = gramsToEdit;
                        break;
                    }
                }
                break;
            }
        }
    }


    ////
    private void RemoveProductsFromCurrentMeals()
    {
        for (int i = 0; i < currentMeals.size(); i++)
        {
            //In every meal have to remove in list product
            //first product from list
            while (currentMeals.get(i).listProduct.size() > 0)
                currentMeals.get(i).listProduct.remove(0);
        }
    }


    //MACROS FOR DAY
    public int GetKcalDay()
    {
        int sum = 0;
        for (Meal meal : currentMeals)
        {
            sum = sum + meal.GetKcalMeal();
        }
        BigDecimal bg = new BigDecimal(sum);
        bg = bg.setScale(2, RoundingMode.HALF_UP);
        return bg.intValue();
    }

    public double GetProteinDay()
    {
        double sum = 0;
        for (Meal meal : currentMeals)
        {
            sum = sum + meal.GetProteinMeal();
        }
        BigDecimal bg = new BigDecimal(sum);
        bg = bg.setScale(2, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }

    public double GetFatsDay()
    {
        double sum = 0;
        for (Meal meal : currentMeals)
        {
            sum = sum + meal.GetFatsMeal();
        }
        BigDecimal bg = new BigDecimal(sum);
        bg = bg.setScale(2, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }

    public double GetCarbohydratesDay()
    {
        double sum = 0;
        for (Meal meal : currentMeals)
        {
            sum = sum + meal.GetCarbohydratesMeal();
        }
        BigDecimal bg = new BigDecimal(sum);
        bg = bg.setScale(2, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }

    public String ToStringMacrosDay()
    {
        return GetKcalDay() + " kcal | "+ GetProteinDay()+" (g) | "+ GetFatsDay() +" (g) | "+ GetCarbohydratesDay()+" (g)";
    }

    public String ToStringMacrosMeal(Meal meal)
    {
        return meal.GetKcalMeal() + " kcal | "+ meal.GetProteinMeal() +" (g) | "+ meal.GetFatsMeal() +" (g) | "+ meal.GetCarbohydratesMeal() +" (g)";
    }
}
