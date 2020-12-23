package sample;

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
        return (Protein * (Grams * 0.01));
    }
    public double GetFatsByGrams()
    {
        return (Fats * (Grams * 0.01));
    }
    public double GetCarbohydratesByGrams()
    {
        return (Carbohydrates * (Grams * 0.01));
    }
    ///////////////
    public String ToStringWithoutGrams()
    {
        return (""+Name+" | "+Kcal+" (kcal) | "+Protein+" (g) | "+Fats+" (g) | "+Carbohydrates+" (g)");
    }

    public String toString(){
        return (""+Name+" | "+Kcal*(Grams * 0.01)+" (kcal) | "+Protein*(Grams * 0.01)+" (g) | "+Fats*(Grams * 0.01)+" (g) | "+Carbohydrates*(Grams * 0.01)+" (g)");
    }
    @Override
    public boolean equals(Object obj){
        if (Name == ((Product)obj).Name && Grams == ((Product)obj).Grams && Id == ((Product)obj).Id)
            return true;
        else
            return false;

    }
}
