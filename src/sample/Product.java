package sample;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {
    public int Id;
    public String Name;
    public int Kcal;
    public double Protein;
    public double Fats;
    public double Carbohydrates;
    public int Grams;
    //CTORS
    public Product()
    {

    }
    public Product(String name, int kcal, double protein, double fats, double carbohydrates)
    {
        Name = name;
        Kcal = kcal;
        Protein = protein;
        Fats = fats;
        Carbohydrates = carbohydrates;
    }
    public Product(int id, String name, int kcal, double protein, double fats, double carbohydrates)
    {
        this(name, kcal, protein, fats, carbohydrates);
        Id = id;
    }
    public Product(int id, String name, int kcal, double protein, double fats, double carbohydrates, int grams)
    {
        this(id, name, kcal, protein, fats, carbohydrates);
        Grams = grams;
    }

    //GETTERS BY GRAMS
    public int GetKcalByGrams()
    {
        double temp = Kcal * (Grams * 0.01);
        return (int)temp;
    }
    public double GetProteinByGrams()
    {
        BigDecimal bd = new BigDecimal(Protein * (Grams * 0.01));
        bd = bd.setScale(2,RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public double GetFatsByGrams()
    {
        BigDecimal bd = new BigDecimal(Fats * (Grams * 0.01));
        bd = bd.setScale(2,RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public double GetCarbohydratesByGrams()
    {
        BigDecimal bd = new BigDecimal(Carbohydrates * (Grams * 0.01));
        bd = bd.setScale(2,RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    ///////////////
    public String ToStringWithoutGrams()
    {
        return (""+Name+" | "+Kcal+" (kcal) | "+Protein+" (g) | "+Fats+" (g) | "+Carbohydrates+" (g)");
    }

    public String toString(){
        BigDecimal bg;
        bg = new BigDecimal(Kcal*(Grams * 0.01));
        bg = bg.setScale(2, RoundingMode.HALF_UP);
        int kcal = bg.intValue();

        bg = new BigDecimal(Protein*(Grams * 0.01));
        bg = bg.setScale(2, RoundingMode.HALF_UP);
        double prot = bg.doubleValue();

        bg = new BigDecimal(Fats*(Grams * 0.01));
        bg = bg.setScale(2, RoundingMode.HALF_UP);
        double fat = bg.doubleValue();

        bg = new BigDecimal(Carbohydrates*(Grams * 0.01));
        bg = bg.setScale(2, RoundingMode.HALF_UP);
        double carb = bg.doubleValue();
        return (""+Name+" | "+Grams+ " (g) | "+kcal+" (kcal) | "+prot+" (g) | "+fat+" (g) | "+carb+" (g)");
    }
    @Override
    public boolean equals(Object obj){
        if (Name == ((Product)obj).Name && Grams == ((Product)obj).Grams && Id == ((Product)obj).Id)
            return true;
        else
            return false;

    }
}
