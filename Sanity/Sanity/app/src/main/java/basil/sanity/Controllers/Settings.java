package basil.sanity.Controllers;

/**
 * Created by Tri on 10/2/2017.
 */

public class Settings {
    private double threshold;
    private CategoryPeriod categoryPeriod;
    private String frequencyType;
    private int frequencyValue;

    public Settings() {
        threshold = 0.75;
        categoryPeriod = new CategoryPeriod();
        frequencyType = "Week";
        frequencyValue = 4;
    }

    public Settings(double pr, CategoryPeriod bp, String ft, int fv) {
        threshold = pr;
        categoryPeriod = bp;
        frequencyType = ft;
        frequencyValue = fv;
    }

    public double getThreshold() { return threshold; }

    public CategoryPeriod getCategoryPeriod() { return categoryPeriod; }

    public int getFrequencyValue () { return frequencyValue; }

    public String getFrequencyType () { return  frequencyType; }

    public void rollover() {
        categoryPeriod.rollover();
    }

    @Override
    public boolean equals(Object o) {
        return threshold == ((Settings)o).threshold &&
                categoryPeriod.equals(((Settings)o).categoryPeriod);
    }
}
