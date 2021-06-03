package de.uni_trier.wi2.pki.io.attr;

@SuppressWarnings("rawtypes")

public class Categoric implements CSVAttribute{
    private Object value;
    private String category;

    public Categoric(String value){
        this.value = value;
        this.category = value;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Object clone() {
        return this.clone();
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}