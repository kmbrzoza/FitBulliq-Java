package sample;

import java.time.LocalDate;
import java.util.ArrayList;

public class Meal {
    public int Id;
    public String Name;
    public LocalDate Date;
    public ArrayList<Product> listProduct = new ArrayList<>();

    // CTORS
    public Meal()
    {

    }
    public Meal(String name, LocalDate date)
    {
        this.Name = name;
        this.Date = date;
    }
    public Meal(int id, String name, LocalDate date)
    {
        this.Id = id;
        this.Name = name;
        this.Date = date;
    }

    //GETTERS BY MEAL (from listProduct)
    public int GetKcalMeal()
    {
        int sum = 0;
        for (int i = 0; i < listProduct.size(); i++)
        {
            sum = sum + listProduct.get(i).GetKcalByGrams();
        }
        return sum;
    }
    public double GetProteinMeal()
    {
        double sum = 0;
        for (int i = 0; i < listProduct.size(); i++)
        {
            sum = sum + listProduct.get(i).GetProteinByGrams();
        }
        return sum;
    }
    public double GetFatsMeal()
    {
        double sum = 0;
        for (int i = 0; i < listProduct.size(); i++)
        {
            sum = sum + listProduct.get(i).GetFatsByGrams();
        }
        return sum;
    }
    public double GetCarbohydratesMeal()
    {
        double sum = 0;
        for (int i = 0; i < listProduct.size(); i++)
        {
            sum = sum + listProduct.get(i).GetCarbohydratesByGrams();
        }
        return sum;
    }
    //////////

    public boolean equals(Object obj){
        if (Name == ((Meal)obj).Name && Id == ((Meal)obj).Id)
            return true;
        else
            return false;
    }

}
