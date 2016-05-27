package cs165.edu.dartmouth.cs.beforespoiled.database;

/**
 * Created by oubai on 5/27/16.
 */
public class TemplateChild {
    private String ingredient;

    public TemplateChild(String ingre) {
            ingredient = ingre;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
